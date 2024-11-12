package com.publiccms.controller.admin.oauth;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang3.time.DateUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.view.UrlBasedViewResolver;

import com.fasterxml.jackson.core.type.TypeReference;
import com.publiccms.common.constants.CommonConstants;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.common.tools.ControllerUtils;
import com.publiccms.common.tools.ExtendUtils;
import com.publiccms.common.tools.LanguagesUtils;
import com.publiccms.common.tools.RequestUtils;
import com.publiccms.controller.admin.LoginAdminController;
import com.publiccms.entities.log.LogLogin;
import com.publiccms.entities.sys.SysSite;
import com.publiccms.entities.sys.SysUser;
import com.publiccms.entities.sys.SysUserAttribute;
import com.publiccms.entities.sys.SysUserToken;
import com.publiccms.logic.component.config.ConfigDataComponent;
import com.publiccms.logic.component.config.SafeConfigComponent;
import com.publiccms.logic.component.config.SiteConfigComponent;
import com.publiccms.logic.component.site.LockComponent;
import com.publiccms.logic.service.log.LogLoginService;
import com.publiccms.logic.service.sys.SysUserAttributeService;
import com.publiccms.logic.service.sys.SysUserService;
import com.publiccms.logic.service.sys.SysUserTokenService;
import com.publiccms.views.pojo.oauth.CredentialRecordData;
import com.publiccms.views.pojo.oauth.WebauthnCreateOption;
import com.publiccms.views.pojo.oauth.WebauthnLoginOption;
import com.webauthn4j.WebAuthnManager;
import com.webauthn4j.converter.util.ObjectConverter;
import com.webauthn4j.data.AttestationConveyancePreference;
import com.webauthn4j.data.AuthenticationData;
import com.webauthn4j.data.AuthenticationParameters;
import com.webauthn4j.data.AuthenticatorAttachment;
import com.webauthn4j.data.AuthenticatorSelectionCriteria;
import com.webauthn4j.data.PublicKeyCredentialDescriptor;
import com.webauthn4j.data.PublicKeyCredentialParameters;
import com.webauthn4j.data.PublicKeyCredentialRpEntity;
import com.webauthn4j.data.PublicKeyCredentialType;
import com.webauthn4j.data.PublicKeyCredentialUserEntity;
import com.webauthn4j.data.RegistrationData;
import com.webauthn4j.data.RegistrationParameters;
import com.webauthn4j.data.ResidentKeyRequirement;
import com.webauthn4j.data.UserVerificationRequirement;
import com.webauthn4j.data.attestation.authenticator.AttestedCredentialData;
import com.webauthn4j.data.attestation.statement.COSEAlgorithmIdentifier;
import com.webauthn4j.data.client.Origin;
import com.webauthn4j.data.client.challenge.Challenge;
import com.webauthn4j.data.client.challenge.DefaultChallenge;
import com.webauthn4j.data.extension.client.AuthenticationExtensionClientInput;
import com.webauthn4j.data.extension.client.AuthenticationExtensionsClientInputs;
import com.webauthn4j.server.ServerProperty;
import com.webauthn4j.verifier.exception.VerificationException;

import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.ValidationException;

@Controller
@RequestMapping("webauthn")
public class WebAuthnController {
    @Resource
    private SysUserService service;
    @Resource
    private SysUserAttributeService attributeService;
    @Resource
    private ConfigDataComponent configDataComponent;
    @Resource
    private SysUserTokenService sysUserTokenService;
    @Resource
    private LogLoginService logLoginService;
    @Resource
    private LockComponent lockComponent;
    @Resource
    protected LocaleResolver localeResolver;

    private static WebAuthnManager webAuthnManager = WebAuthnManager.createNonStrictWebAuthnManager();
    private static ObjectConverter objectConverter = new ObjectConverter();
    private static List<PublicKeyCredentialParameters> pubKeyCredParams = new ArrayList<>();
    private static long timeout = Duration.ofMinutes(1).toMillis();
    private static TypeReference<Map<String, CredentialRecordData>> typeReference = new TypeReference<>() {
    };
    static {
        pubKeyCredParams
                .add(new PublicKeyCredentialParameters(PublicKeyCredentialType.PUBLIC_KEY, COSEAlgorithmIdentifier.ES256));
        pubKeyCredParams
                .add(new PublicKeyCredentialParameters(PublicKeyCredentialType.PUBLIC_KEY, COSEAlgorithmIdentifier.ES384));
        pubKeyCredParams
                .add(new PublicKeyCredentialParameters(PublicKeyCredentialType.PUBLIC_KEY, COSEAlgorithmIdentifier.ES512));
        pubKeyCredParams
                .add(new PublicKeyCredentialParameters(PublicKeyCredentialType.PUBLIC_KEY, COSEAlgorithmIdentifier.EdDSA));
        pubKeyCredParams
                .add(new PublicKeyCredentialParameters(PublicKeyCredentialType.PUBLIC_KEY, COSEAlgorithmIdentifier.RS256));
        pubKeyCredParams
                .add(new PublicKeyCredentialParameters(PublicKeyCredentialType.PUBLIC_KEY, COSEAlgorithmIdentifier.RS384));
        pubKeyCredParams
                .add(new PublicKeyCredentialParameters(PublicKeyCredentialType.PUBLIC_KEY, COSEAlgorithmIdentifier.RS512));
    }

    @PostMapping("attestation/options")
    @ResponseBody
    public WebauthnCreateOption registerOptions(@SessionAttribute SysUser admin, HttpServletRequest request) {
        PublicKeyCredentialRpEntity rpEntity = new PublicKeyCredentialRpEntity(request.getServerName(), request.getServerName());
        PublicKeyCredentialUserEntity userEntity = new PublicKeyCredentialUserEntity(admin.getName().getBytes(), admin.getName(),
                admin.getName());
        UserVerificationRequirement userVerificationRequirement = UserVerificationRequirement.PREFERRED;
        AuthenticatorSelectionCriteria authenticatorSelectionCriteria = new AuthenticatorSelectionCriteria(
                AuthenticatorAttachment.PLATFORM, true, ResidentKeyRequirement.PREFERRED, userVerificationRequirement);
        List<PublicKeyCredentialDescriptor> excludeCredentials = null;

        return new WebauthnCreateOption(rpEntity, userEntity, new DefaultChallenge(admin.getName().getBytes()), pubKeyCredParams,
                timeout, excludeCredentials, authenticatorSelectionCriteria, AttestationConveyancePreference.NONE, null);
    }

    @PostMapping("attestation/result")
    @ResponseBody
    public Map<String, String> registerCredential(@RequestAttribute SysSite site, @SessionAttribute SysUser admin,
            @RequestBody String registrationResponseJSON, HttpServletRequest request) {
        Challenge challenge = new DefaultChallenge(admin.getName().getBytes());
        Origin origin = new Origin(RequestUtils.getOrigin(request));
        byte[] tokenBindingId = null;
        RegistrationData registrationData = webAuthnManager.parseRegistrationResponseJSON(registrationResponseJSON);
        ServerProperty serverProperty = new ServerProperty(origin, request.getServerName(), challenge, tokenBindingId);
        boolean userVerificationRequired = false;
        boolean userPresenceRequired = true;
        RegistrationParameters registrationParameters = new RegistrationParameters(serverProperty, pubKeyCredParams,
                userVerificationRequired, userPresenceRequired);
        Map<String, String> result = new HashMap<>();
        result.put("status", "ok");
        result.put("errorMessage", "");
        try {
            webAuthnManager.verify(registrationData, registrationParameters);
            CredentialRecordData credentialRecord = new CredentialRecordData(registrationData.getAttestationObject(),
                    registrationData.getCollectedClientData(), registrationData.getClientExtensions(),
                    registrationData.getTransports(), objectConverter);
            SysUserAttribute attribute = attributeService.getEntity(admin.getId());
            Map<String, String> map = ExtendUtils.getSettingsMap(attribute);
            String webauthnSettings = map.get(SysUserAttributeService.SETTINGS_CODE_WEBAUTHN);
            Map<String, CredentialRecordData> credentialRecordMap = null;
            if (CommonUtils.notEmpty(webauthnSettings)) {
                credentialRecordMap = objectConverter.getJsonConverter().readValue(webauthnSettings, typeReference);
            } else {
                credentialRecordMap = new LinkedHashMap<>();
            }
            @SuppressWarnings("deprecation")
            AttestedCredentialData attestedCredentialData = credentialRecord.toRecord(objectConverter)
                    .getAttestedCredentialData();
            credentialRecordMap.put(Base64.getUrlEncoder().encodeToString(attestedCredentialData.getCredentialId()),
                    credentialRecord);
            map.put(SysUserAttributeService.SETTINGS_CODE_WEBAUTHN,
                    objectConverter.getJsonConverter().writeValueAsString(credentialRecordMap));
            attributeService.updateSettings(admin.getId(), ExtendUtils.getExtendString(map));
            return result;
        } catch (ValidationException e) {
            result.put("status", "failed");
            result.put("errorMessage", e.getMessage());
            return result;
        }
    }

    @RequestMapping("assertion/options")
    @ResponseBody
    public WebauthnLoginOption loginOptions(@RequestBody Map<String, String> body, HttpServletRequest request,
            HttpSession session) {
        String username = body.get("username");
        List<PublicKeyCredentialDescriptor> allowCredentials = null;
        AuthenticationExtensionsClientInputs<AuthenticationExtensionClientInput> extensions = null;
        session.setAttribute("webauthnuser", username);
        return new WebauthnLoginOption(new DefaultChallenge(username.getBytes()), timeout, request.getServerName(),
                allowCredentials, UserVerificationRequirement.PREFERRED, extensions);
    }

    @PostMapping("assertion/result")
    @ResponseBody
    public Map<String, String> loginResult(@RequestAttribute SysSite site, @RequestBody String authenticationResponseJSON,
            @SessionAttribute String webauthnuser, HttpServletRequest request, HttpServletResponse response,
            HttpSession session) {
        Origin origin = new Origin(RequestUtils.getOrigin(request));
        byte[] tokenBindingId = null;
        AuthenticationData authenticationData = webAuthnManager.parseAuthenticationResponseJSON(authenticationResponseJSON);
        ServerProperty serverProperty = new ServerProperty(origin, request.getServerName(),
                new DefaultChallenge(webauthnuser.getBytes()), tokenBindingId);
        List<byte[]> allowCredentials = null;
        boolean userVerificationRequired = true;
        boolean userPresenceRequired = true;
        SysUser user = service.findByName(site.getId(), webauthnuser);
        Map<String, String> result = new HashMap<>();
        result.put("status", "failed");
        result.put("errorMessage", LanguagesUtils.getMessage(CommonConstants.applicationContext,
                localeResolver.resolveLocale(request), "verify.custom.fingerprint"));
        String ip = RequestUtils.getIpAddress(request);
        ModelMap model = new ModelMap();
        boolean locked = lockComponent.isLocked(site.getId(), LockComponent.ITEM_TYPE_IP_LOGIN, ip, null);
        if (ControllerUtils.errorCustom("locked.ip", locked && ControllerUtils.ipNotEquals(ip, user), model)
                || ControllerUtils.errorNotEquals("password", user, model)) {
            lockComponent.lock(site.getId(), LockComponent.ITEM_TYPE_IP_LOGIN, ip, null, true);
            logLoginService.save(new LogLogin(site.getId(), webauthnuser, null == user ? null : user.getId(), ip,
                    LogLoginService.CHANNEL_WEB_MANAGER, false, CommonUtils.getDate(), "fingerprint"));
            result.put("errorMessage", (String) model.get(CommonConstants.ERROR));
            return result;
        }
        if (null != user) {
            locked = lockComponent.isLocked(site.getId(), LockComponent.ITEM_TYPE_LOGIN, String.valueOf(user.getId()), null);
            if (ControllerUtils.errorCustom("locked.user", locked, model) || LoginAdminController.verifyNotAdmin(user, model)
                    || LoginAdminController.verifyNotEnablie(user, model)) {
                Long userId = user.getId();
                lockComponent.lock(site.getId(), LockComponent.ITEM_TYPE_LOGIN, String.valueOf(user.getId()), null, true);
                lockComponent.lock(site.getId(), LockComponent.ITEM_TYPE_IP_LOGIN, ip, null, true);
                logLoginService.save(new LogLogin(site.getId(), webauthnuser, userId, ip, LogLoginService.CHANNEL_WEB_MANAGER,
                        false, CommonUtils.getDate(), "fingerprint"));
                result.put("errorMessage", (String) model.get(CommonConstants.ERROR));
                return result;
            }
            SysUserAttribute attribute = attributeService.getEntity(user.getId());
            Map<String, String> map = ExtendUtils.getSettingsMap(attribute);
            String webauthn = map.get(SysUserAttributeService.SETTINGS_CODE_WEBAUTHN);
            if (CommonUtils.notEmpty(webauthn)) {
                try {
                    Map<String, CredentialRecordData> credentialRecordMap = objectConverter.getJsonConverter().readValue(webauthn,
                            typeReference);
                    CredentialRecordData credentialRecord = credentialRecordMap
                            .get(Base64.getUrlEncoder().encodeToString(authenticationData.getCredentialId()));
                    if (null != credentialRecord) {
                        AuthenticationParameters authenticationParameters = new AuthenticationParameters(serverProperty,
                                credentialRecord.toRecord(objectConverter), allowCredentials, userVerificationRequired,
                                userPresenceRequired);
                        webAuthnManager.verify(authenticationData, authenticationParameters);

                        service.updateLoginStatus(user.getId(), ip);
                        String authToken = UUID.randomUUID().toString();
                        Date now = CommonUtils.getDate();
                        Map<String, String> safeConfig = configDataComponent.getConfigData(site.getId(),
                                SafeConfigComponent.CONFIG_CODE);
                        int expiryMinutes = ConfigDataComponent.getInt(
                                safeConfig.get(SafeConfigComponent.CONFIG_EXPIRY_MINUTES_MANAGER),
                                SafeConfigComponent.DEFAULT_EXPIRY_MINUTES);
                        LoginAdminController.addLoginStatus(user, authToken, request, response, expiryMinutes);
                        sysUserTokenService.save(new SysUserToken(authToken, site.getId(), user.getId(),
                                LogLoginService.CHANNEL_WEB_MANAGER, now, DateUtils.addMinutes(now, expiryMinutes), ip));
                        logLoginService.save(new LogLogin(site.getId(), webauthnuser, user.getId(), ip,
                                LogLoginService.CHANNEL_WEB_MANAGER, false, CommonUtils.getDate(), "fingerprint"));
                        session.removeAttribute("webauthnuser");

                        if (0 < authenticationData.getAuthenticatorData().getSignCount() || 0 < credentialRecord.getCounter()) {
                            credentialRecord.setCounter(authenticationData.getAuthenticatorData().getSignCount());
                            credentialRecordMap.put(Base64.getUrlEncoder().encodeToString(authenticationData.getCredentialId()),
                                    credentialRecord);
                            map.put(SysUserAttributeService.SETTINGS_CODE_WEBAUTHN,
                                    objectConverter.getJsonConverter().writeValueAsString(credentialRecordMap));
                            attributeService.updateSettings(user.getId(), ExtendUtils.getExtendString(map));
                        }

                        result.put("status", "ok");
                        result.put("errorMessage", "");
                        return result;
                    }
                } catch (VerificationException e) {
                    result.put("errorMessage", e.getMessage());
                }
            }
        }
        return result;
    }

    @GetMapping("getCredentials")
    @ResponseBody
    public Map<String, Object> getCredentials(@RequestAttribute SysSite site, @SessionAttribute SysUser admin,
            HttpServletRequest request) {
        SysUserAttribute attribute = attributeService.getEntity(admin.getId());
        Map<String, String> map = ExtendUtils.getSettingsMap(attribute);
        String webauthnSettings = map.get(SysUserAttributeService.SETTINGS_CODE_WEBAUTHN);
        Map<String, Object> result = new HashMap<>();
        if (CommonUtils.notEmpty(webauthnSettings)) {
            Map<String, CredentialRecordData> webauthnMap = objectConverter.getJsonConverter().readValue(webauthnSettings,
                    typeReference);
            result.put("credentials", webauthnMap.keySet());
        }
        return result;
    }

    @PostMapping("deleteCredential")
    @ResponseBody
    public Map<String, Object> deleteCredential(@RequestAttribute SysSite site, @SessionAttribute SysUser admin,
            String credentialId, HttpServletRequest request) {
        SysUserAttribute attribute = attributeService.getEntity(admin.getId());
        Map<String, String> map = ExtendUtils.getSettingsMap(attribute);
        String webauthnSettings = map.get(SysUserAttributeService.SETTINGS_CODE_WEBAUTHN);
        Map<String, CredentialRecordData> webauthnMap = null;
        if (CommonUtils.notEmpty(webauthnSettings)) {
            webauthnMap = objectConverter.getJsonConverter().readValue(webauthnSettings, typeReference);
        } else {
            webauthnMap = new LinkedHashMap<>();
        }
        webauthnMap.remove(credentialId);
        map.put(SysUserAttributeService.SETTINGS_CODE_WEBAUTHN,
                objectConverter.getJsonConverter().writeValueAsString(webauthnMap));
        attributeService.updateSettings(admin.getId(), ExtendUtils.getExtendString(map));
        return Collections.singletonMap("result", true);
    }

    @GetMapping("login")
    public String login(@RequestAttribute SysSite site, String returnUrl, HttpServletRequest request) {
        Map<String, String> config = configDataComponent.getConfigData(site.getId(), SiteConfigComponent.CONFIG_CODE);
        String safeReturnUrl = config.get(SafeConfigComponent.CONFIG_RETURN_URL);
        if (SafeConfigComponent.isUnSafeUrl(returnUrl, site, safeReturnUrl, request.getContextPath())) {
            returnUrl = CommonUtils.joinString("../", CommonConstants.getDefaultPage());
        }
        return CommonUtils.joinString(UrlBasedViewResolver.REDIRECT_URL_PREFIX, returnUrl);
    }
}
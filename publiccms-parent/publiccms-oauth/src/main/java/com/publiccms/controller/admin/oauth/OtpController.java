package com.publiccms.controller.admin.oauth;

import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang3.time.DateUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.UrlBasedViewResolver;

import com.bastiaanjansen.otp.SecretGenerator;
import com.bastiaanjansen.otp.TOTPGenerator;
import com.publiccms.common.constants.CommonConstants;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.common.tools.ControllerUtils;
import com.publiccms.common.tools.RequestUtils;
import com.publiccms.controller.admin.LoginAdminController;
import com.publiccms.entities.log.LogLogin;
import com.publiccms.entities.sys.SysSite;
import com.publiccms.entities.sys.SysUser;
import com.publiccms.entities.sys.SysUserSetting;
import com.publiccms.entities.sys.SysUserSettingId;
import com.publiccms.entities.sys.SysUserToken;
import com.publiccms.logic.component.config.ConfigDataComponent;
import com.publiccms.logic.component.config.SafeConfigComponent;
import com.publiccms.logic.service.log.LogLoginService;
import com.publiccms.logic.service.sys.SysUserService;
import com.publiccms.logic.service.sys.SysUserSettingService;
import com.publiccms.logic.service.sys.SysUserTokenService;

import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("otp")
public class OtpController {
    protected final Log log = LogFactory.getLog(getClass());
    @Resource
    private ConfigDataComponent configDataComponent;
    @Resource
    private SysUserTokenService sysUserTokenService;
    @Resource
    private LogLoginService logLoginService;
    @Resource
    private SysUserService service;
    @Resource
    private SysUserSettingService settingService;

    /**
     * @param site
     * @param admin
     * @param otpadmin
     * @param returnUrl
     * @param secret
     * @param code
     * @param model
     * @return view name
     */
    @RequestMapping(value = "login")
    public String login(@SessionAttribute(required = false) SysUser otpadmin, String returnUrl, RedirectAttributes model) {
        model.addAttribute("returnUrl", returnUrl);
        if (null == otpadmin) {
            return "redirect:../login";
        }
        SysUserSetting userSetting = settingService
                .getEntity(new SysUserSettingId(otpadmin.getId(), SysUserSettingService.OPTSECRET_SETTINGS_CODE));
        if (null != userSetting) {
            byte[] secret = SecretGenerator.generate();
            TOTPGenerator totp = new TOTPGenerator.Builder(secret).build();
            model.addAttribute("secret", new String(secret, StandardCharsets.UTF_8));
            try {
                model.addAttribute("bindURI", totp.getURI("cms", otpadmin.getName()).toString());
            } catch (URISyntaxException e) {
            }
            return "otp/register";
        } else {
            return "otp/login";
        }
    }

    /**
     * @param site
     * @param admin
     * @param otpadmin
     * @param secret
     * @param code
     * @param returnUrl
     * @param request
     * @param model
     * @return view name
     */
    @PostMapping(value = "doRegister")
    public String doRegister(@RequestAttribute SysSite site, @SessionAttribute SysUser otpadmin, String secret, String code,
            String returnUrl, HttpServletRequest request, RedirectAttributes model) {
        TOTPGenerator totp = new TOTPGenerator.Builder(secret.getBytes()).build();
        if (totp.verify(code)) {
            settingService.getOrCreateOrUpdate(otpadmin.getId(), SysUserSettingService.OPTSECRET_SETTINGS_CODE, secret);
            Map<String, String> config = configDataComponent.getConfigData(site.getId(), SafeConfigComponent.CONFIG_CODE);
            String safeReturnUrl = config.get(SafeConfigComponent.CONFIG_RETURN_URL);
            if (SafeConfigComponent.isUnSafeUrl(returnUrl, site, safeReturnUrl, request.getContextPath())) {
                returnUrl = CommonConstants.getDefaultPage();
            }
            return CommonUtils.joinString(UrlBasedViewResolver.REDIRECT_URL_PREFIX, returnUrl);
        } else {
            model.addAttribute("returnUrl", returnUrl);
            return "redirect:login";
        }
    }

    /**
     * @param site
     * @param otpadmin
     * @param returnUrl
     * @param code
     * @param request
     * @param response
     * @param model
     * @return view name
     */
    @PostMapping(value = "doLogin")
    public String doLogin(@RequestAttribute(required = true) SysSite site, @SessionAttribute SysUser otpadmin, String returnUrl,
            String code, HttpServletRequest request, HttpServletResponse response, RedirectAttributes model) {
        model.addAttribute("returnUrl", returnUrl);
        if (null == otpadmin) {
            return "redirect:../login";
        } else {
            SysUserSetting userSetting = settingService
                    .getEntity(new SysUserSettingId(otpadmin.getId(), SysUserSettingService.OPTSECRET_SETTINGS_CODE));
            if (null != userSetting && CommonUtils.notEmpty(userSetting.getData())) {
                TOTPGenerator totp = new TOTPGenerator.Builder(userSetting.getData().getBytes()).build();
                if (totp.verify(code)) {
                    ControllerUtils.clearOptAdminToSession(request.getSession());
                    String ip = RequestUtils.getIpAddress(request);
                    service.updateLoginStatus(otpadmin.getId(), ip);
                    String authToken = UUID.randomUUID().toString();
                    Date now = CommonUtils.getDate();
                    Map<String, String> safeConfig = configDataComponent.getConfigData(site.getId(),
                            SafeConfigComponent.CONFIG_CODE);
                    int expiryMinutes = ConfigDataComponent.getInt(
                            safeConfig.get(SafeConfigComponent.CONFIG_EXPIRY_MINUTES_MANAGER),
                            SafeConfigComponent.DEFAULT_EXPIRY_MINUTES);
                    LoginAdminController.addLoginStatus(otpadmin, authToken, request, response, expiryMinutes);
                    sysUserTokenService.save(new SysUserToken(authToken, site.getId(), otpadmin.getId(),
                            LogLoginService.CHANNEL_WEB_MANAGER, now, DateUtils.addMinutes(now, expiryMinutes), ip));
                    logLoginService.save(new LogLogin(site.getId(), otpadmin.getName(), otpadmin.getId(), ip,
                            LogLoginService.CHANNEL_WEB_MANAGER, LogLoginService.METHOD_OTP, true, now, null));
                    Map<String, String> config = configDataComponent.getConfigData(site.getId(), SafeConfigComponent.CONFIG_CODE);
                    String safeReturnUrl = config.get(SafeConfigComponent.CONFIG_RETURN_URL);
                    if (SafeConfigComponent.isUnSafeUrl(returnUrl, site, safeReturnUrl, request.getContextPath())) {
                        returnUrl = CommonUtils.joinString("../", CommonConstants.getDefaultPage());
                    }
                    return CommonUtils.joinString(UrlBasedViewResolver.REDIRECT_URL_PREFIX, returnUrl);
                } else {
                    return "redirect:login";
                }
            } else {
                return "redirect:login";
            }
        }
    }
}

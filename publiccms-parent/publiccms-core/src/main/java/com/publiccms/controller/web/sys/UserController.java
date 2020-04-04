package com.publiccms.controller.web.sys;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.mail.MessagingException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.view.UrlBasedViewResolver;

import com.publiccms.common.annotation.Csrf;
import com.publiccms.common.api.Config;
import com.publiccms.common.constants.CommonConstants;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.common.tools.ControllerUtils;
import com.publiccms.common.tools.FreeMarkerUtils;
import com.publiccms.common.tools.RequestUtils;
import com.publiccms.common.tools.UserPasswordUtils;
import com.publiccms.entities.log.LogOperate;
import com.publiccms.entities.sys.SysEmailToken;
import com.publiccms.entities.sys.SysSite;
import com.publiccms.entities.sys.SysUser;
import com.publiccms.entities.sys.SysUserToken;
import com.publiccms.logic.component.config.ConfigComponent;
import com.publiccms.logic.component.config.EmailTemplateConfigComponent;
import com.publiccms.logic.component.config.LoginConfigComponent;
import com.publiccms.logic.component.site.EmailComponent;
import com.publiccms.logic.component.site.SiteComponent;
import com.publiccms.logic.component.template.TemplateComponent;
import com.publiccms.logic.service.log.LogLoginService;
import com.publiccms.logic.service.log.LogOperateService;
import com.publiccms.logic.service.sys.SysEmailTokenService;
import com.publiccms.logic.service.sys.SysUserService;
import com.publiccms.logic.service.sys.SysUserTokenService;

import freemarker.template.TemplateException;

/**
 * 
 * UserController 用户逻辑处理
 *
 */
@Controller
@RequestMapping("user")
public class UserController {
    @Autowired
    private SysUserService service;
    @Autowired
    private SysUserTokenService sysUserTokenService;
    @Autowired
    private EmailComponent emailComponent;
    @Autowired
    private TemplateComponent templateComponent;
    @Autowired
    private SysEmailTokenService sysEmailTokenService;
    @Autowired
    private ConfigComponent configComponent;
    @Autowired
    protected LogOperateService logOperateService;
    @Autowired
    protected SiteComponent siteComponent;

    /**
     * @param site
     * @param oldpassword
     * @param password
     * @param repassword
     * @param returnUrl
     * @param request
     * @param session
     * @param response
     * @param model
     * @return view name
     */
    @RequestMapping(value = "changePassword", method = RequestMethod.POST)
    @Csrf
    public String changePassword(@RequestAttribute SysSite site, String oldpassword, String password, String repassword,
            String returnUrl, HttpServletRequest request, HttpSession session, HttpServletResponse response, ModelMap model) {
        Map<String, String> config = configComponent.getConfigData(site.getId(), Config.CONFIG_CODE_SITE);
        String safeReturnUrl = config.get(LoginConfigComponent.CONFIG_RETURN_URL);
        if (ControllerUtils.isUnSafeUrl(returnUrl, site, safeReturnUrl, request)) {
            returnUrl = site.isUseStatic() ? site.getSitePath() : site.getDynamicPath();
        }
        SysUser user = ControllerUtils.getUserFromSession(session);
        if (null != user) {
            user = service.getEntity(user.getId());
        }
        if (ControllerUtils.verifyNotEmpty("user", user, model) || ControllerUtils.verifyNotEmpty("password", password, model)
                || ControllerUtils.verifyNotEquals("repassword", password, repassword, model)
                || null != user.getPassword() && ControllerUtils.verifyNotEquals("password", user.getPassword(),
                        UserPasswordUtils.passwordEncode(oldpassword, user.getSalt()), model)) {
            return UrlBasedViewResolver.REDIRECT_URL_PREFIX + returnUrl;
        } else {
            Cookie userCookie = RequestUtils.getCookie(request.getCookies(), CommonConstants.getCookiesUser());
            if (null != userCookie && CommonUtils.notEmpty(userCookie.getValue())) {
                String value = userCookie.getValue();
                if (null != value) {
                    String[] userData = value.split(CommonConstants.getCookiesUserSplit());
                    if (userData.length > 1) {
                        sysUserTokenService.delete(userData[1]);
                    }
                }
            }
            ControllerUtils.clearUserToSession(request.getContextPath(), session, response);
            String salt = UserPasswordUtils.getSalt();
            service.updatePassword(user.getId(), UserPasswordUtils.passwordEncode(password, salt), salt);
            if (user.isWeakPassword() && !UserPasswordUtils.isWeek(user.getName(), password)) {
                service.updateWeekPassword(user.getId(), false);
            }
            model.addAttribute(CommonConstants.MESSAGE, CommonConstants.SUCCESS);
            logOperateService.save(new LogOperate(site.getId(), user.getId(), LogLoginService.CHANNEL_WEB, "changepassword",
                    RequestUtils.getIpAddress(request), CommonUtils.getDate(), user.getPassword()));
            return UrlBasedViewResolver.REDIRECT_URL_PREFIX + returnUrl;
        }
    }

    /**
     * @param password
     * @param session
     * @return result
     */
    @RequestMapping("isWeak")
    @ResponseBody
    public Map<String, Object> isWeak(String password, HttpSession session) {
        SysUser user = ControllerUtils.getUserFromSession(session);
        Map<String, Object> result = new HashMap<>();
        if (null != user) {
            result.put("weak", UserPasswordUtils.isWeek(user.getName(), password));
        }
        return result;
    }

    /**
     * @param site
     * @param email
     * @param returnUrl
     * @param request
     * @param session
     * @param model
     * @return view name
     */
    @RequestMapping(value = "saveEmail", method = RequestMethod.POST)
    @Csrf
    public String saveEmail(@RequestAttribute SysSite site, String email, String returnUrl, HttpServletRequest request,
            HttpSession session, ModelMap model) {
        Map<String, String> loginConfig = configComponent.getConfigData(site.getId(), Config.CONFIG_CODE_SITE);
        String safeReturnUrl = loginConfig.get(LoginConfigComponent.CONFIG_RETURN_URL);
        if (ControllerUtils.isUnSafeUrl(returnUrl, site, safeReturnUrl, request)) {
            returnUrl = site.isUseStatic() ? site.getSitePath() : site.getDynamicPath();
        }
        Map<String, String> config = configComponent.getConfigData(site.getId(), EmailTemplateConfigComponent.CONFIG_CODE);
        String emailTitle = config.get(EmailTemplateConfigComponent.CONFIG_EMAIL_TITLE);
        String emailPath = config.get(EmailTemplateConfigComponent.CONFIG_EMAIL_PATH);
        SysUser user = ControllerUtils.getUserFromSession(session);
        if (ControllerUtils.verifyNotEmpty("user", user, model) || ControllerUtils.verifyNotEmpty("email", email, model)
                || ControllerUtils.verifyNotEmpty("email.config", emailTitle, model)
                || ControllerUtils.verifyNotEmpty("email.config", emailPath, model)
                || ControllerUtils.verifyNotEMail("email", email, model)
                || ControllerUtils.verifyHasExist("email", service.findByEmail(site.getId(), email), model)) {
            return UrlBasedViewResolver.REDIRECT_URL_PREFIX + returnUrl;
        } else {
            int expiryMinutes = ConfigComponent.getInt(config.get(EmailTemplateConfigComponent.CONFIG_EXPIRY_MINUTES),
                    EmailTemplateConfigComponent.DEFAULT_EXPIRY_MINUTES);
            SysEmailToken sysEmailToken = new SysEmailToken();
            sysEmailToken.setUserId(user.getId());
            sysEmailToken.setAuthToken(UUID.randomUUID().toString());
            sysEmailToken.setEmail(email);
            sysEmailToken.setExpiryDate(DateUtils.addMinutes(CommonUtils.getDate(), expiryMinutes));
            sysEmailTokenService.save(sysEmailToken);
            try {
                Map<String, Object> emailModel = new HashMap<>();
                emailModel.put("user", user);
                emailModel.put("site", site);
                emailModel.put("email", email);
                emailModel.put("authToken", sysEmailToken.getAuthToken());
                emailModel.put("expiryDate", sysEmailToken.getExpiryDate());
                if (emailComponent.sendHtml(site.getId(), email,
                        FreeMarkerUtils.generateStringByString(emailTitle, templateComponent.getWebConfiguration(), emailModel),
                        FreeMarkerUtils.generateStringByFile(SiteComponent.getFullTemplatePath(site, emailPath),
                                templateComponent.getWebConfiguration(), emailModel))) {
                    model.addAttribute(CommonConstants.MESSAGE, "sendEmail.success");
                } else {
                    sysEmailTokenService.delete(sysEmailToken.getAuthToken());
                    model.addAttribute(CommonConstants.MESSAGE, "sendEmail.error");
                }
            } catch (IOException | TemplateException | MessagingException e) {
                sysEmailTokenService.delete(sysEmailToken.getAuthToken());
                model.addAttribute(CommonConstants.ERROR, "sendEmail.error");
            }
            return UrlBasedViewResolver.REDIRECT_URL_PREFIX + returnUrl;
        }
    }

    /**
     * @param site
     * @param authToken
     * @param returnUrl
     * @param request
     * @param session
     * @param model
     * @return view name
     */
    @RequestMapping(value = "verifyEmail")
    public String verifyEmail(@RequestAttribute SysSite site, String authToken, String returnUrl, HttpServletRequest request,
            HttpSession session, ModelMap model) {
        Map<String, String> config = configComponent.getConfigData(site.getId(), Config.CONFIG_CODE_SITE);
        String safeReturnUrl = config.get(LoginConfigComponent.CONFIG_RETURN_URL);
        if (ControllerUtils.isUnSafeUrl(returnUrl, site, safeReturnUrl, request)) {
            returnUrl = site.isUseStatic() ? site.getSitePath() : site.getDynamicPath();
        }
        SysEmailToken sysEmailToken = sysEmailTokenService.getEntity(authToken);
        if (null != sysEmailToken && CommonUtils.getDate().after(sysEmailToken.getExpiryDate())) {
            sysEmailToken = null;
        }
        if (ControllerUtils.verifyNotEmpty("verifyEmail.authToken", authToken, model)
                || ControllerUtils.verifyNotExist("verifyEmail.sysEmailToken", sysEmailToken, model)) {
            return UrlBasedViewResolver.REDIRECT_URL_PREFIX + returnUrl;
        } else {
            sysEmailTokenService.delete(sysEmailToken.getAuthToken());
            service.checked(sysEmailToken.getUserId(), sysEmailToken.getEmail());
            ControllerUtils.clearUserTimeToSession(session);
            model.addAttribute(CommonConstants.MESSAGE, "verifyEmail.success");
            return UrlBasedViewResolver.REDIRECT_URL_PREFIX + returnUrl;
        }
    }

    /**
     * @param site
     * @param authToken
     * @param returnUrl
     * @param request
     * @param session
     * @param model
     * @return view name
     */
    @RequestMapping(value = "deleteToken", method = RequestMethod.POST)
    public String deleteToken(@RequestAttribute SysSite site, String authToken, String returnUrl, HttpServletRequest request,
            HttpSession session, ModelMap model) {
        Map<String, String> config = configComponent.getConfigData(site.getId(), Config.CONFIG_CODE_SITE);
        String safeReturnUrl = config.get(LoginConfigComponent.CONFIG_RETURN_URL);
        if (ControllerUtils.isUnSafeUrl(returnUrl, site, safeReturnUrl, request)) {
            returnUrl = site.isUseStatic() ? site.getSitePath() : site.getDynamicPath();
        }
        SysUserToken entity = sysUserTokenService.getEntity(authToken);
        SysUser user = ControllerUtils.getUserFromSession(session);
        if (null != entity && null != user) {
            if (ControllerUtils.verifyNotEquals("userId", user.getId(), entity.getUserId(), model)) {
                return UrlBasedViewResolver.REDIRECT_URL_PREFIX + returnUrl;
            }
            sysUserTokenService.delete(authToken);
        }
        return UrlBasedViewResolver.REDIRECT_URL_PREFIX + returnUrl;
    }
}

package com.publiccms.controller.web.sys;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import jakarta.mail.MessagingException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import org.apache.commons.lang3.time.DateUtils;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.servlet.view.UrlBasedViewResolver;

import com.publiccms.common.annotation.Csrf;
import com.publiccms.common.constants.CommonConstants;
import com.publiccms.common.handler.PageHandler;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.common.tools.ControllerUtils;
import com.publiccms.common.tools.FreeMarkerUtils;
import com.publiccms.common.tools.JsonUtils;
import com.publiccms.common.tools.RequestUtils;
import com.publiccms.common.tools.UserPasswordUtils;
import com.publiccms.entities.log.LogOperate;
import com.publiccms.entities.sys.SysEmailToken;
import com.publiccms.entities.sys.SysSite;
import com.publiccms.entities.sys.SysUser;
import com.publiccms.entities.sys.SysUserToken;
import com.publiccms.logic.component.config.ConfigComponent;
import com.publiccms.logic.component.config.EmailTemplateConfigComponent;
import com.publiccms.logic.component.config.SiteConfigComponent;
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
    @Resource
    private SysUserService service;
    @Resource
    private SysUserTokenService sysUserTokenService;
    @Resource
    private EmailComponent emailComponent;
    @Resource
    private TemplateComponent templateComponent;
    @Resource
    private SysEmailTokenService sysEmailTokenService;
    @Resource
    private ConfigComponent configComponent;
    @Resource
    protected SiteConfigComponent siteConfigComponent;
    @Resource
    protected LogOperateService logOperateService;
    @Resource
    protected SiteComponent siteComponent;

    /**
     * @param site
     * @param user
     * @param oldpassword
     * @param password
     * @param repassword
     * @param encoding
     * @param returnUrl
     * @param request
     * @param session
     * @param response
     * @param model
     * @return view name
     */
    @RequestMapping(value = "changePassword", method = RequestMethod.POST)
    @Csrf
    public String changePassword(@RequestAttribute SysSite site, @SessionAttribute SysUser user, String oldpassword,
            String password, String repassword, String encoding, String returnUrl, HttpServletRequest request,
            HttpSession session, HttpServletResponse response, ModelMap model) {
        returnUrl = siteConfigComponent.getSafeUrl(returnUrl, site, request.getContextPath());
        user = service.getEntity(user.getId());
        if (ControllerUtils.errorNotEmpty("user", user, model) || ControllerUtils.errorNotEmpty("password", password, model)
                || ControllerUtils.errorNotEquals("repassword", password, repassword, model)
                || null != user.getPassword() && ControllerUtils.errorNotEquals("password", user.getPassword(),
                        UserPasswordUtils.passwordEncode(oldpassword, null, user.getPassword(), encoding), model)) {
            return new StringBuilder(UrlBasedViewResolver.REDIRECT_URL_PREFIX).append(returnUrl).toString();
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
            ControllerUtils.clearUserToSession(request.getContextPath(), request.getScheme(), session, response);
            service.updatePassword(user.getId(),
                    UserPasswordUtils.passwordEncode(password, UserPasswordUtils.getSalt(), null, encoding));
            if (user.isWeakPassword()) {
                service.updateWeekPassword(user.getId(), false);
            }
            model.addAttribute(CommonConstants.MESSAGE, CommonConstants.SUCCESS);
            logOperateService.save(new LogOperate(site.getId(), user.getId(), user.getDeptId(), LogLoginService.CHANNEL_WEB,
                    "changepassword", RequestUtils.getIpAddress(request), CommonUtils.getDate(), user.getPassword()));
            return new StringBuilder(UrlBasedViewResolver.REDIRECT_URL_PREFIX).append(returnUrl).toString();
        }
    }

    /**
     * @param site
     * @param user
     * @param nickname
     * @param cover
     * @param returnUrl
     * @param request
     * @param model
     * @return view name
     */
    @RequestMapping("update")
    @Csrf
    public String update(@RequestAttribute SysSite site, @SessionAttribute SysUser user, String nickname, String cover,
            String returnUrl, HttpServletRequest request, ModelMap model) {
        returnUrl = siteConfigComponent.getSafeUrl(returnUrl, site, request.getContextPath());
        if (ControllerUtils.errorNotEmpty("nickname", nickname, model)
                || ControllerUtils.errorNotNickname("nickname", nickname, model)) {
            return new StringBuilder(UrlBasedViewResolver.REDIRECT_URL_PREFIX).append(returnUrl).toString();
        }
        SysUser entity = service.updateProfile(user.getId(), nickname, cover, null);
        if (null != entity) {
            ControllerUtils.setUserToSession(request.getSession(), entity);
            logOperateService.save(new LogOperate(site.getId(), user.getId(), user.getDeptId(), LogLoginService.CHANNEL_WEB,
                    "update.user", RequestUtils.getIpAddress(request), CommonUtils.getDate(), JsonUtils.getString(entity)));
        }
        return new StringBuilder(UrlBasedViewResolver.REDIRECT_URL_PREFIX).append(returnUrl).toString();
    }

    /**
     * @param site
     * @param user
     * @param email
     * @param returnUrl
     * @param request
     * @param model
     * @return view name
     */
    @RequestMapping(value = "saveEmail", method = RequestMethod.POST)
    @Csrf
    public String saveEmail(@RequestAttribute SysSite site, @SessionAttribute SysUser user, String email, String returnUrl,
            HttpServletRequest request, ModelMap model) {
        returnUrl = siteConfigComponent.getSafeUrl(returnUrl, site, request.getContextPath());
        Map<String, String> config = configComponent.getConfigData(site.getId(), EmailTemplateConfigComponent.CONFIG_CODE);
        String emailTitle = config.get(EmailTemplateConfigComponent.CONFIG_EMAIL_TITLE);
        String emailPath = config.get(EmailTemplateConfigComponent.CONFIG_EMAIL_PATH);
        PageHandler page = sysEmailTokenService.getPage(user.getId(), null, 0);
        if (ControllerUtils.errorNotEmpty("email", email, model)
                || ControllerUtils.errorNotEmpty("email.config", emailTitle, model)
                || ControllerUtils.errorNotEmpty("email.config", emailPath, model)
                || ControllerUtils.errorNotEMail("email", email, model)
                || ControllerUtils.errorNotGreaterThen("email.token", page.getTotalCount(), 2, model)
                || ControllerUtils.errorHasExist("email", service.findByEmail(site.getId(), email), model)) {
            return new StringBuilder(UrlBasedViewResolver.REDIRECT_URL_PREFIX).append(returnUrl).toString();
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
                        FreeMarkerUtils.generateStringByFile(SiteComponent.getFullTemplatePath(site.getId(), emailPath),
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
            return new StringBuilder(UrlBasedViewResolver.REDIRECT_URL_PREFIX).append(returnUrl).toString();
        }
    }

    /**
     * @param site
     * @param authToken
     * @param returnUrl
     * @param request
     * @param model
     * @return view name
     */
    @RequestMapping(value = "verifyEmail")
    public String verifyEmail(@RequestAttribute SysSite site, String authToken, String returnUrl, HttpServletRequest request,
            ModelMap model) {
        returnUrl = siteConfigComponent.getSafeUrl(returnUrl, site, request.getContextPath());
        SysEmailToken sysEmailToken = sysEmailTokenService.getEntity(authToken);
        if (null != sysEmailToken && CommonUtils.getDate().after(sysEmailToken.getExpiryDate())) {
            sysEmailToken = null;
        }
        if (ControllerUtils.errorNotEmpty("verifyEmail.authToken", authToken, model)
                || ControllerUtils.errorNotExist("verifyEmail.authToken", sysEmailToken, model)) {
            return new StringBuilder(UrlBasedViewResolver.REDIRECT_URL_PREFIX).append(returnUrl).toString();
        } else {
            sysEmailTokenService.delete(sysEmailToken.getAuthToken());
            service.checked(sysEmailToken.getUserId(), sysEmailToken.getEmail());
            model.addAttribute(CommonConstants.MESSAGE, "verifyEmail.success");
            return new StringBuilder(UrlBasedViewResolver.REDIRECT_URL_PREFIX).append(returnUrl).toString();
        }
    }

    /**
     * @param site
     * @param user
     * @param authToken
     * @param returnUrl
     * @param request
     * @param model
     * @return view name
     */
    @RequestMapping(value = "deleteToken")
    @Csrf
    public String deleteToken(@RequestAttribute SysSite site, @SessionAttribute SysUser user, String authToken, String returnUrl,
            HttpServletRequest request, ModelMap model) {
        returnUrl = siteConfigComponent.getSafeUrl(returnUrl, site, request.getContextPath());
        SysUserToken entity = sysUserTokenService.getEntity(authToken);
        if (null != entity) {
            if (ControllerUtils.errorNotEquals("userId", user.getId(), entity.getUserId(), model)) {
                return new StringBuilder(UrlBasedViewResolver.REDIRECT_URL_PREFIX).append(returnUrl).toString();
            }
            sysUserTokenService.delete(authToken);
        }
        return new StringBuilder(UrlBasedViewResolver.REDIRECT_URL_PREFIX).append(returnUrl).toString();
    }
}

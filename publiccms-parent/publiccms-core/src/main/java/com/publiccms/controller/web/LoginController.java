package com.publiccms.controller.web;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.view.UrlBasedViewResolver;

import com.publiccms.common.api.Config;
import com.publiccms.common.base.AbstractController;
import com.publiccms.common.constants.CommonConstants;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.common.tools.ControllerUtils;
import com.publiccms.common.tools.RequestUtils;
import com.publiccms.common.tools.VerificationUtils;
import com.publiccms.entities.log.LogLogin;
import com.publiccms.entities.sys.SysSite;
import com.publiccms.entities.sys.SysUser;
import com.publiccms.entities.sys.SysUserToken;
import com.publiccms.logic.component.config.ConfigComponent;
import com.publiccms.logic.component.config.LoginConfigComponent;
import com.publiccms.logic.service.log.LogLoginService;
import com.publiccms.logic.service.sys.SysUserService;
import com.publiccms.logic.service.sys.SysUserTokenService;

/**
 *
 * LoginController 登录逻辑
 *
 */
@Controller
public class LoginController extends AbstractController {
    @Autowired
    private SysUserService service;
    @Autowired
    private SysUserTokenService sysUserTokenService;
    @Autowired
    private LogLoginService logLoginService;
    @Autowired
    private ConfigComponent configComponent;

    /**
     * @param username
     * @param password
     * @param returnUrl
     * @param request
     * @param response
     * @param model
     * @return view name
     */
    @RequestMapping(value = "doLogin", method = RequestMethod.POST)
    public String login(String username, String password, String returnUrl, HttpServletRequest request,
            HttpServletResponse response, ModelMap model) {
        SysSite site = getSite(request);
        if (CommonUtils.empty(returnUrl)) {
            returnUrl = site.getDynamicPath();
        }
        Map<String, String> config = configComponent.getConfigData(site.getId(), Config.CONFIG_CODE_SITE);
        String loginPath = config.get(LoginConfigComponent.CONFIG_LOGIN_PATH);
        if (CommonUtils.empty(loginPath)) {
            loginPath = site.getDynamicPath();
        }
        username = StringUtils.trim(username);
        password = StringUtils.trim(password);
        if (ControllerUtils.verifyNotEmpty("username", username, model)
                || ControllerUtils.verifyNotEmpty("password", password, model)) {
            return UrlBasedViewResolver.REDIRECT_URL_PREFIX + loginPath;
        } else {
            SysUser user;
            if (ControllerUtils.verifyNotEMail(username)) {
                user = service.findByName(site.getId(), username);
            } else {
                user = service.findByEmail(site.getId(), username);
            }
            String ip = RequestUtils.getIpAddress(request);
            if (ControllerUtils.verifyNotExist("username", user, model) || ControllerUtils.verifyNotEquals("password",
                    VerificationUtils.md5Encode(password), user.getPassword(), model) || verifyNotEnablie(user, model)) {
                Long userId = null;
                if (null != user) {
                    userId = user.getId();
                }
                logLoginService.save(new LogLogin(site.getId(), username, userId, ip, LogLoginService.CHANNEL_WEB, false,
                        CommonUtils.getDate(), password));
                return UrlBasedViewResolver.REDIRECT_URL_PREFIX + loginPath;
            } else {
                user.setPassword(null);
                ControllerUtils.setUserToSession(request.getSession(), user);
                String authToken = UUID.randomUUID().toString();
                addLoginStatus(user, authToken, request, response);
                sysUserTokenService.save(new SysUserToken(authToken, site.getId(), user.getId(), LogLoginService.CHANNEL_WEB,
                        CommonUtils.getDate(), ip));
                service.updateLoginStatus(user.getId(), ip);
                logLoginService.save(new LogLogin(site.getId(), username, user.getId(), ip, LogLoginService.CHANNEL_WEB, true,
                        CommonUtils.getDate(), null));
                return UrlBasedViewResolver.REDIRECT_URL_PREFIX + returnUrl;
            }
        }
    }

    /**
     * @param request
     * @param session
     * @param response
     * @param model
     * @return result
     */
    @RequestMapping("loginStatus")
    @ResponseBody
    public ModelMap loginStatus(HttpServletRequest request, HttpSession session, HttpServletResponse response, ModelMap model) {
        SysUser user = ControllerUtils.getUserFromSession(session);
        if (null != user) {
            model.addAttribute("id", user.getId());
            model.addAttribute("name", user.getName());
            model.addAttribute("nickname", user.getNickName());
            model.addAttribute("email", user.getEmail());
            model.addAttribute("emailChecked", user.isEmailChecked());
            model.addAttribute("superuserAccess", user.isSuperuserAccess());
        }
        return model;
    }

    /**
     * @param entity
     * @param repassword
     * @param returnUrl
     * @param channel
     * @param openId
     * @param request
     * @param response
     * @param model
     * @return view name
     */
    @RequestMapping(value = "doRegister", method = RequestMethod.POST)
    public String register(SysUser entity, String repassword, String returnUrl, String channel, String openId,
            HttpServletRequest request, HttpServletResponse response, ModelMap model) {
        SysSite site = getSite(request);
        if (CommonUtils.empty(returnUrl)) {
            returnUrl = site.getDynamicPath();
        }
        entity.setName(StringUtils.trim(entity.getName()));
        entity.setNickName(StringUtils.trim(entity.getNickName()));
        entity.setPassword(StringUtils.trim(entity.getPassword()));
        repassword = StringUtils.trim(repassword);

        if (ControllerUtils.verifyNotEmpty("username", entity.getName(), model)
                || ControllerUtils.verifyNotEmpty("nickname", entity.getNickName(), model)
                || ControllerUtils.verifyNotEmpty("password", entity.getPassword(), model)
                || ControllerUtils.verifyNotUserName("username", entity.getName(), model)
                || ControllerUtils.verifyNotNickName("nickname", entity.getNickName(), model)
                || ControllerUtils.verifyNotEquals("repassword", entity.getPassword(), repassword, model)
                || ControllerUtils.verifyHasExist("username", service.findByName(site.getId(), entity.getName()), model)
                || ControllerUtils.verifyHasExist("nickname", service.findByNickName(site.getId(), entity.getNickName()),
                        model)) {
            model.addAttribute("name", entity.getName());
            model.addAttribute("nickname", entity.getNickName());
            return UrlBasedViewResolver.REDIRECT_URL_PREFIX + returnUrl;
        } else {
            String ip = RequestUtils.getIpAddress(request);
            entity.setPassword(VerificationUtils.md5Encode(entity.getPassword()));
            entity.setLastLoginIp(ip);
            entity.setSiteId(site.getId());
            service.save(entity);
            entity.setPassword(null);
            ControllerUtils.setUserToSession(request.getSession(), entity);
            String authToken = UUID.randomUUID().toString();
            addLoginStatus(entity, authToken, request, response);
            sysUserTokenService.save(new SysUserToken(authToken, site.getId(), entity.getId(), LogLoginService.CHANNEL_WEB,
                    CommonUtils.getDate(), ip));
            if (null != channel && null != openId) {
                String oauthToken = new StringBuilder(channel).append(CommonConstants.DOT).append(site.getId())
                        .append(CommonConstants.DOT).append(openId).toString();
                sysUserTokenService
                        .save(new SysUserToken(oauthToken, site.getId(), entity.getId(), channel, CommonUtils.getDate(), ip));
            }
            return UrlBasedViewResolver.REDIRECT_URL_PREFIX + returnUrl;
        }
    }

    /**
     * @param userId
     * @param returnUrl
     * @param request
     * @param response
     * @param model
     * @return view name
     */
    @RequestMapping(value = "doLogout", method = RequestMethod.POST)
    public String logout(Long userId, String returnUrl, HttpServletRequest request, HttpServletResponse response,
            ModelMap model) {
        SysSite site = getSite(request);
        if (CommonUtils.empty(returnUrl)) {
            returnUrl = site.getDynamicPath();
        }
        SysUser user = ControllerUtils.getUserFromSession(request.getSession());
        if (null != userId && null != user && userId == user.getId()) {
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
            ControllerUtils.clearUserToSession(request.getContextPath(), request.getSession(), response);
        }
        return UrlBasedViewResolver.REDIRECT_URL_PREFIX + returnUrl;
    }

    private void addLoginStatus(SysUser user, String authToken, HttpServletRequest request, HttpServletResponse response) {
        try {
            StringBuilder sb = new StringBuilder();
            sb.append(user.getId()).append(CommonConstants.getCookiesUserSplit()).append(authToken)
                    .append(CommonConstants.getCookiesUserSplit()).append(user.isSuperuserAccess())
                    .append(CommonConstants.getCookiesUserSplit())
                    .append(URLEncoder.encode(user.getNickName(), CommonConstants.DEFAULT_CHARSET_NAME));
            RequestUtils.addCookie(request.getContextPath(), response, CommonConstants.getCookiesUser(), sb.toString(),
                    Integer.MAX_VALUE, null);
        } catch (UnsupportedEncodingException e) {
            log.error(e.getMessage(), e);
        }
    }

    /**
     * @param user
     * @param model
     * @return 用户是否禁用
     */
    public boolean verifyNotEnablie(SysUser user, ModelMap model) {
        if (user.isDisabled()) {
            model.addAttribute(CommonConstants.ERROR, "verify.user.notEnablie");
            return true;
        }
        return false;
    }
}

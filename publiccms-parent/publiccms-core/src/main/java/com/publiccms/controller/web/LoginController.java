package com.publiccms.controller.web;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.view.UrlBasedViewResolver;

import com.publiccms.common.api.Config;
import com.publiccms.common.constants.CommonConstants;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.common.tools.ControllerUtils;
import com.publiccms.common.tools.RequestUtils;
import com.publiccms.common.tools.UserPasswordUtils;
import com.publiccms.entities.log.LogLogin;
import com.publiccms.entities.sys.SysAppClient;
import com.publiccms.entities.sys.SysSite;
import com.publiccms.entities.sys.SysUser;
import com.publiccms.entities.sys.SysUserToken;
import com.publiccms.logic.component.config.ConfigComponent;
import com.publiccms.logic.component.config.SiteConfigComponent;
import com.publiccms.logic.component.site.LockComponent;
import com.publiccms.logic.component.site.SiteComponent;
import com.publiccms.logic.service.log.LogLoginService;
import com.publiccms.logic.service.sys.SysAppClientService;
import com.publiccms.logic.service.sys.SysUserService;
import com.publiccms.logic.service.sys.SysUserTokenService;

/**
 *
 * LoginController 登录逻辑
 *
 */
@Controller
public class LoginController {
    protected static final Log log = LogFactory.getLog(LoginController.class);
    @Resource
    private SysUserService service;
    @Resource
    private SysUserTokenService sysUserTokenService;
    @Resource
    private SysAppClientService appClientService;
    @Resource
    private LogLoginService logLoginService;
    @Resource
    private LockComponent lockComponent;
    @Resource
    protected SiteComponent siteComponent;
    @Resource
    protected ConfigComponent configComponent;
    @Resource
    protected SiteConfigComponent siteConfigComponent;

    /**
     * @param site 站点
     * @param username 用户名
     * @param password 密码
     * @param returnUrl 返回地址
     * @param encoding 密码编码
     * @param clientId 客户端id
     * @param uuid 客户端uuid
     * @param request 请求
     * @param response 返回
     * @param model 模型
     * @return view name 视图名
     */
    @RequestMapping(value = "doLogin", method = RequestMethod.POST)
    public String login(@RequestAttribute SysSite site, String username, String password, String returnUrl, String encoding,
            Long clientId, String uuid, HttpServletRequest request, HttpServletResponse response, ModelMap model) {
        Map<String, String> config = configComponent.getConfigData(site.getId(), Config.CONFIG_CODE_SITE);
        String loginPath = config.get(SiteConfigComponent.CONFIG_LOGIN_PATH);
        if (CommonUtils.empty(loginPath)) {
            loginPath = site.getDynamicPath();
        }
        returnUrl = siteConfigComponent.getSafeUrl(returnUrl, site, request.getContextPath());
        username = StringUtils.trim(username);
        password = StringUtils.trim(password);
        if (ControllerUtils.errorNotEmpty("username", username, model)
                || ControllerUtils.errorNotEmpty("password", password, model)) {
            return UrlBasedViewResolver.REDIRECT_URL_PREFIX + loginPath;
        } else {
            SysUser user;
            if (ControllerUtils.notEMail(username)) {
                user = service.findByName(site.getId(), username);
            } else {
                user = service.findByEmail(site.getId(), username);
            }
            String ip = RequestUtils.getIpAddress(request);
            Date now = CommonUtils.getDate();
            boolean locked = lockComponent.isLocked(site.getId(), LockComponent.ITEM_TYPE_IP_LOGIN, ip, null);
            if (ControllerUtils.errorCustom("locked.ip", locked && ControllerUtils.ipNotEquals(ip, user), model)
                    || ControllerUtils.errorNotEquals("password", user, model)) {
                lockComponent.lock(site.getId(), LockComponent.ITEM_TYPE_IP_LOGIN, ip, null, true);
                return UrlBasedViewResolver.REDIRECT_URL_PREFIX + loginPath;
            }
            locked = lockComponent.isLocked(site.getId(), LockComponent.ITEM_TYPE_LOGIN, String.valueOf(user.getId()), null);
            if (ControllerUtils.errorCustom("locked.user", locked, model) || ControllerUtils.errorNotEquals("password",
                    UserPasswordUtils.passwordEncode(password, null, user.getPassword(), encoding), user.getPassword(), model)
                    || verifyNotEnablie(user, model)) {
                Long userId = user.getId();
                lockComponent.lock(site.getId(), LockComponent.ITEM_TYPE_LOGIN, String.valueOf(user.getId()), null, true);
                lockComponent.lock(site.getId(), LockComponent.ITEM_TYPE_IP_LOGIN, ip, null, true);
                logLoginService.save(
                        new LogLogin(site.getId(), username, userId, ip, LogLoginService.CHANNEL_WEB, false, now, password));
                return UrlBasedViewResolver.REDIRECT_URL_PREFIX + loginPath;
            } else {
                lockComponent.unLock(site.getId(), LockComponent.ITEM_TYPE_IP_LOGIN, ip, user.getId());
                lockComponent.unLock(site.getId(), LockComponent.ITEM_TYPE_LOGIN, String.valueOf(user.getId()), null);
                if (UserPasswordUtils.needUpdate(user.getPassword())) {
                    service.updatePassword(user.getId(), UserPasswordUtils.passwordEncode(password, UserPasswordUtils.getSalt(), null, encoding));
                }
                service.updateLoginStatus(user.getId(), ip);

                if (null != clientId && null != uuid) {
                    SysAppClient appClient = appClientService.getEntity(clientId);
                    if (null != appClient && appClient.getSiteId() == site.getId() && appClient.getUuid().equals(uuid)
                            && null == appClient.getUserId()) {
                        appClientService.updateUser(appClient.getId(), user.getId());
                    }
                }

                String authToken = UUID.randomUUID().toString();
                int expiryMinutes = ConfigComponent.getInt(config.get(SiteConfigComponent.CONFIG_EXPIRY_MINUTES_WEB),
                        SiteConfigComponent.DEFAULT_EXPIRY_MINUTES);
                addLoginStatus(user, authToken, request, response, expiryMinutes);
                sysUserTokenService.save(new SysUserToken(authToken, site.getId(), user.getId(), LogLoginService.CHANNEL_WEB, now,
                        DateUtils.addMinutes(now, expiryMinutes), ip));
                logLoginService.save(
                        new LogLogin(site.getId(), username, user.getId(), ip, LogLoginService.CHANNEL_WEB, true, now, null));
                return UrlBasedViewResolver.REDIRECT_URL_PREFIX + returnUrl;
            }
        }
    }

    /**
     * @param session
     * @return result
     */
    @RequestMapping("loginStatus")
    @ResponseBody
    public Map<String, Object> loginStatus(HttpSession session) {
        SysUser user = ControllerUtils.getUserFromSession(session);
        Map<String, Object> result = new HashMap<>();
        if (null != user) {
            result.put("id", user.getId());
            result.put("name", user.getName());
            result.put("nickname", user.getNickname());
            result.put("weakPassword", user.isWeakPassword());
            result.put("email", user.getEmail());
            result.put("emailChecked", user.isEmailChecked());
            result.put("superuserAccess", user.isSuperuser());
        }
        return result;
    }

    /**
     * @param site
     * @param entity
     * @param repassword
     * @param returnUrl
     * @param encode
     * @param clientId
     * @param uuid
     * @param request
     * @param response
     * @param model
     * @return view name
     */
    @RequestMapping(value = "doRegister", method = RequestMethod.POST)
    public String register(@RequestAttribute SysSite site, SysUser entity, String repassword, String returnUrl, String encode,
            Long clientId, String uuid, HttpServletRequest request, HttpServletResponse response, ModelMap model) {
        Map<String, String> config = configComponent.getConfigData(site.getId(), Config.CONFIG_CODE_SITE);
        String registerPath = config.get(SiteConfigComponent.CONFIG_REGISTER_URL);
        if (CommonUtils.empty(registerPath)) {
            registerPath = site.getDynamicPath();
        }
        String ip = RequestUtils.getIpAddress(request);
        boolean locked = lockComponent.isLocked(site.getId(), LockComponent.ITEM_TYPE_REGISTER, ip, null);
        if (ControllerUtils.errorCustom("locked.ip", locked, model)) {
            lockComponent.lock(site.getId(), LockComponent.ITEM_TYPE_REGISTER, ip, null, true);
            return UrlBasedViewResolver.REDIRECT_URL_PREFIX + registerPath;
        }
        entity.setName(StringUtils.trim(entity.getName()));
        entity.setNickname(StringUtils.trim(entity.getNickname()));
        entity.setPassword(StringUtils.trim(entity.getPassword()));
        repassword = StringUtils.trim(repassword);
        returnUrl = siteConfigComponent.getSafeUrl(returnUrl, site, request.getContextPath());
        if (ControllerUtils.errorNotEmpty("username", entity.getName(), model)
                || ControllerUtils.errorNotEmpty("nickname", entity.getNickname(), model)
                || ControllerUtils.errorNotEmpty("password", entity.getPassword(), model)
                || ControllerUtils.errorNotUserName("username", entity.getName(), model)
                || ControllerUtils.errorNotNickname("nickname", entity.getNickname(), model)
                || ControllerUtils.errorNotEquals("repassword", entity.getPassword(), repassword, model)
                || ControllerUtils.errorHasExist("username", service.findByName(site.getId(), entity.getName()), model)) {
            model.addAttribute("name", entity.getName());
            model.addAttribute("nickname", entity.getNickname());
            return UrlBasedViewResolver.REDIRECT_URL_PREFIX + registerPath;
        } else {
            String salt = UserPasswordUtils.getSalt();
            entity.setPassword(UserPasswordUtils.passwordEncode(entity.getPassword(), salt, null, encode));
            entity.setLastLoginIp(ip);
            entity.setSiteId(site.getId());
            entity.setDisabled(false);
            entity.setRoles(null);
            entity.setSuperuser(false);
            entity.setContentPermissions(SysUserService.CONTENT_PERMISSIONS_SELF);
            entity.setLoginCount(0);
            entity.setDeptId(null);
            service.save(entity);
            lockComponent.lock(site.getId(), LockComponent.ITEM_TYPE_REGISTER, ip, null, true);
            if (null != clientId && null != uuid) {
                SysAppClient appClient = appClientService.getEntity(clientId);
                if (null != appClient && appClient.getSiteId() == site.getId() && appClient.getUuid().equals(uuid)
                        && null == appClient.getUserId()) {
                    appClientService.updateUser(appClient.getId(), entity.getId());
                }
            }
            int expiryMinutes = ConfigComponent.getInt(config.get(SiteConfigComponent.CONFIG_EXPIRY_MINUTES_WEB),
                    SiteConfigComponent.DEFAULT_EXPIRY_MINUTES);

            Date now = CommonUtils.getDate();
            String authToken = UUID.randomUUID().toString();
            Date expiryDate = DateUtils.addMinutes(now, expiryMinutes);
            addLoginStatus(entity, authToken, request, response, expiryMinutes);
            sysUserTokenService.save(
                    new SysUserToken(authToken, site.getId(), entity.getId(), LogLoginService.CHANNEL_WEB, now, expiryDate, ip));
        }
        return UrlBasedViewResolver.REDIRECT_URL_PREFIX + returnUrl;
    }

    /**
     * @param site
     * @param userId
     * @param returnUrl
     * @param request
     * @param response
     * @return view name
     */
    @RequestMapping(value = "doLogout", method = RequestMethod.POST)
    public String logout(@RequestAttribute SysSite site, Long userId, String returnUrl, HttpServletRequest request,
            HttpServletResponse response) {
        returnUrl = siteConfigComponent.getSafeUrl(returnUrl, site, request.getContextPath());
        SysUser user = ControllerUtils.getUserFromSession(request.getSession());
        if (null != userId && null != user && userId.equals(user.getId())) {
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
            ControllerUtils.clearUserToSession(request.getContextPath(), request.getScheme(), request.getSession(), response);
        }
        return UrlBasedViewResolver.REDIRECT_URL_PREFIX + returnUrl;
    }

    public static void addLoginStatus(SysUser user, String authToken, HttpServletRequest request, HttpServletResponse response,
            int expiryMinutes) {
        user.setPassword(null);
        ControllerUtils.setUserToSession(request.getSession(), user);
        StringBuilder sb = new StringBuilder();
        sb.append(user.getId()).append(CommonConstants.getCookiesUserSplit()).append(authToken);
        RequestUtils.addCookie(request.getContextPath(), request.getScheme(), response, CommonConstants.getCookiesUser(),
                sb.toString(), expiryMinutes * 60, null);
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

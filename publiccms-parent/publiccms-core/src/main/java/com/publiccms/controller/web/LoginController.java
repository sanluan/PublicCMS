package com.publiccms.controller.web;

import java.io.IOException;
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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.UrlBasedViewResolver;

import com.publiccms.common.constants.CommonConstants;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.common.tools.ControllerUtils;
import com.publiccms.common.tools.ImageUtils;
import com.publiccms.common.tools.RequestUtils;
import com.publiccms.common.tools.UserPasswordUtils;
import com.publiccms.common.tools.VerificationUtils;
import com.publiccms.entities.log.LogLogin;
import com.publiccms.entities.sys.SysAppClient;
import com.publiccms.entities.sys.SysSite;
import com.publiccms.entities.sys.SysUser;
import com.publiccms.entities.sys.SysUserToken;
import com.publiccms.logic.component.config.ConfigDataComponent;
import com.publiccms.logic.component.config.SafeConfigComponent;
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
    protected ConfigDataComponent configDataComponent;
    @Resource
    protected SafeConfigComponent safeConfigComponent;

    /**
     * @param site
     *            站点
     * @param username
     *            用户名
     * @param password
     *            密码
     * @param returnUrl
     *            返回地址
     * @param encoding
     *            密码编码
     * @param captcha
     *            验证码
     * @param clientId
     *            客户端id
     * @param uuid
     *            客户端uuid
     * @param request
     *            请求
     * @param response
     *            返回
     * @param model
     *            模型
     * @return view name 视图名
     */
    @PostMapping("doLogin")
    public String login(@RequestAttribute SysSite site, String username, String password, String returnUrl, String encoding,
            String captcha, Long clientId, String uuid, HttpServletRequest request, HttpServletResponse response,
            RedirectAttributes model) {
        Map<String, String> config = configDataComponent.getConfigData(site.getId(), SiteConfigComponent.CONFIG_CODE);
        String loginPath = config.get(SiteConfigComponent.CONFIG_LOGIN_PATH);
        if (CommonUtils.empty(loginPath)) {
            loginPath = site.getDynamicPath();
        }
        returnUrl = safeConfigComponent.getSafeUrl(returnUrl, site, request.getContextPath());
        username = StringUtils.trim(username);
        password = StringUtils.trim(password);
        if (ControllerUtils.errorNotEmpty("username", username, model)
                || ControllerUtils.errorNotLongThen("password", password, UserPasswordUtils.PASSWORD_MAX_LENGTH, model)) {
            return CommonUtils.joinString(UrlBasedViewResolver.REDIRECT_URL_PREFIX, loginPath);
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
                return CommonUtils.joinString(UrlBasedViewResolver.REDIRECT_URL_PREFIX, loginPath);
            }
            locked = lockComponent.isLocked(site.getId(), LockComponent.ITEM_TYPE_LOGIN, String.valueOf(user.getId()), null);
            if (CommonUtils.notEmpty(captcha)
                    || safeConfigComponent.enableCaptcha(site.getId(), SafeConfigComponent.CAPTCHA_MODULE_LOGIN)) {
                String sessionCaptcha = (String) request.getSession().getAttribute("captcha");
                request.getSession().removeAttribute("captcha");
                if (ControllerUtils.errorCustom("locked.user", locked, model) || ControllerUtils.errorCustom("captcha.error",
                        null == sessionCaptcha || !sessionCaptcha.equalsIgnoreCase(captcha), model)) {
                    lockComponent.lock(site.getId(), LockComponent.ITEM_TYPE_LOGIN, String.valueOf(user.getId()), null, true);
                    lockComponent.lock(site.getId(), LockComponent.ITEM_TYPE_IP_LOGIN, ip, null, true);
                    logLoginService.save(new LogLogin(site.getId(), username, user.getId(), ip, LogLoginService.CHANNEL_WEB,
                            false, now, password));
                    return CommonUtils.joinString(UrlBasedViewResolver.REDIRECT_URL_PREFIX, loginPath);
                }
            }
            if (ControllerUtils.errorNotEquals("password",
                    UserPasswordUtils.passwordEncode(password, null, user.getPassword(), encoding), user.getPassword(), model)
                    || verifyNotEnablie(user, model)) {
                Long userId = user.getId();
                lockComponent.lock(site.getId(), LockComponent.ITEM_TYPE_LOGIN, String.valueOf(user.getId()), null, true);
                lockComponent.lock(site.getId(), LockComponent.ITEM_TYPE_IP_LOGIN, ip, null, true);
                logLoginService.save(
                        new LogLogin(site.getId(), username, userId, ip, LogLoginService.CHANNEL_WEB, false, now, password));
                return CommonUtils.joinString(UrlBasedViewResolver.REDIRECT_URL_PREFIX, loginPath);
            } else {
                lockComponent.unLock(site.getId(), LockComponent.ITEM_TYPE_IP_LOGIN, ip, user.getId());
                lockComponent.unLock(site.getId(), LockComponent.ITEM_TYPE_LOGIN, String.valueOf(user.getId()), null);
                if (UserPasswordUtils.needUpdate(user.getPassword())) {
                    service.updatePassword(user.getId(),
                            UserPasswordUtils.passwordEncode(password, UserPasswordUtils.getSalt(), null, encoding));
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
                Map<String, String> safeConfig = configDataComponent.getConfigData(site.getId(), SafeConfigComponent.CONFIG_CODE);
                int expiryMinutes = ConfigDataComponent.getInt(safeConfig.get(SafeConfigComponent.CONFIG_EXPIRY_MINUTES_WEB),
                        SafeConfigComponent.DEFAULT_EXPIRY_MINUTES);
                addLoginStatus(user, authToken, request, response, expiryMinutes);
                sysUserTokenService.save(new SysUserToken(authToken, site.getId(), user.getId(), LogLoginService.CHANNEL_WEB, now,
                        DateUtils.addMinutes(now, expiryMinutes), ip));
                logLoginService.save(
                        new LogLogin(site.getId(), username, user.getId(), ip, LogLoginService.CHANNEL_WEB, true, now, null));
                return CommonUtils.joinString(UrlBasedViewResolver.REDIRECT_URL_PREFIX, returnUrl);
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
            result.put("superuser", user.isSuperuser());
        }
        return result;
    }

    /**
     * @param site
     * @param entity
     * @param repassword
     * @param returnUrl
     * @param encode
     * @param captcha
     *            验证码
     * @param clientId
     * @param uuid
     * @param request
     * @param response
     * @param model
     * @return view name
     */
    @PostMapping("doRegister")
    public String register(@RequestAttribute SysSite site, SysUser entity, String repassword, String returnUrl, String encode,
            String captcha, Long clientId, String uuid, HttpServletRequest request, HttpServletResponse response,
            RedirectAttributes model) {
        Map<String, String> config = configDataComponent.getConfigData(site.getId(), SiteConfigComponent.CONFIG_CODE);
        String registerPath = config.get(SiteConfigComponent.CONFIG_REGISTER_URL);
        if (CommonUtils.empty(registerPath)) {
            registerPath = site.getDynamicPath();
        }
        String ip = RequestUtils.getIpAddress(request);
        boolean locked = lockComponent.isLocked(site.getId(), LockComponent.ITEM_TYPE_REGISTER, ip, null);
        if (ControllerUtils.errorCustom("locked.ip", locked, model)) {
            lockComponent.lock(site.getId(), LockComponent.ITEM_TYPE_REGISTER, ip, null, true);
            return CommonUtils.joinString(UrlBasedViewResolver.REDIRECT_URL_PREFIX, registerPath);
        }
        if (CommonUtils.notEmpty(captcha)
                || safeConfigComponent.enableCaptcha(site.getId(), SafeConfigComponent.CAPTCHA_MODULE_REGISTER)) {
            String sessionCaptcha = (String) request.getSession().getAttribute("captcha");
            request.getSession().removeAttribute("captcha");
            if (ControllerUtils.errorCustom("captcha.error", null == sessionCaptcha || !sessionCaptcha.equalsIgnoreCase(captcha),
                    model)) {
                return CommonUtils.joinString(UrlBasedViewResolver.REDIRECT_URL_PREFIX, registerPath);
            }
        }
        entity.setName(StringUtils.trim(entity.getName()));
        entity.setNickname(StringUtils.trim(entity.getNickname()));
        entity.setPassword(StringUtils.trim(entity.getPassword()));
        repassword = StringUtils.trim(repassword);
        returnUrl = safeConfigComponent.getSafeUrl(returnUrl, site, request.getContextPath());
        if (ControllerUtils.errorNotEmpty("username", entity.getName(), model)
                || ControllerUtils.errorNotEmpty("nickname", entity.getNickname(), model)
                || ControllerUtils.errorNotEmpty("password", entity.getPassword(), model)
                || ControllerUtils.errorNotUserName("username", entity.getName(), model)
                || ControllerUtils.errorNotNickname("nickname", entity.getNickname(), model)
                || ControllerUtils.errorNotEquals("repassword", entity.getPassword(), repassword, model)
                || ControllerUtils.errorHasExist("username", service.findByName(site.getId(), entity.getName()), model)) {
            model.addAttribute("name", entity.getName());
            model.addAttribute("nickname", entity.getNickname());
            return CommonUtils.joinString(UrlBasedViewResolver.REDIRECT_URL_PREFIX, registerPath);
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
            Map<String, String> safeconfig = configDataComponent.getConfigData(site.getId(), SafeConfigComponent.CONFIG_CODE);
            int expiryMinutes = ConfigDataComponent.getInt(safeconfig.get(SafeConfigComponent.CONFIG_EXPIRY_MINUTES_WEB),
                    SafeConfigComponent.DEFAULT_EXPIRY_MINUTES);

            Date now = CommonUtils.getDate();
            String authToken = UUID.randomUUID().toString();
            Date expiryDate = DateUtils.addMinutes(now, expiryMinutes);
            addLoginStatus(entity, authToken, request, response, expiryMinutes);
            sysUserTokenService.save(
                    new SysUserToken(authToken, site.getId(), entity.getId(), LogLoginService.CHANNEL_WEB, now, expiryDate, ip));
        }
        return CommonUtils.joinString(UrlBasedViewResolver.REDIRECT_URL_PREFIX, returnUrl);
    }

    @RequestMapping(value = "getCaptchaImage")
    public void getCaptchaImage(HttpSession session, HttpServletResponse response) {
        try {
            String captcha = VerificationUtils.getRandomString("ABCDEFGHJKMNPQRSTUVWXYZ23456789", 4);
            session.setAttribute("captcha", captcha);
            ImageUtils.drawImage(120, 30, captcha, response.getOutputStream());
        } catch (IOException e) {
        }
    }

    /**
     * @param site
     * @param userId
     * @param returnUrl
     * @param request
     * @param response
     * @return view name
     */
    @PostMapping("doLogout")
    public String logout(@RequestAttribute SysSite site, Long userId, String returnUrl, HttpServletRequest request,
            HttpServletResponse response) {
        returnUrl = safeConfigComponent.getSafeUrl(returnUrl, site, request.getContextPath());
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
        return CommonUtils.joinString(UrlBasedViewResolver.REDIRECT_URL_PREFIX, returnUrl);
    }

    public static void addLoginStatus(SysUser user, String authToken, HttpServletRequest request, HttpServletResponse response,
            int expiryMinutes) {
        user.setPassword(null);
        ControllerUtils.setUserToSession(request.getSession(), user);
        String cookie = CommonUtils.joinString(user.getId(), CommonConstants.getCookiesUserSplit(), authToken);
        RequestUtils.addCookie(request.getContextPath(), request.getScheme(), response, CommonConstants.getCookiesUser(), cookie,
                expiryMinutes * 60, null);
    }

    /**
     * @param user
     * @param model
     * @return 用户是否禁用
     */
    public boolean verifyNotEnablie(SysUser user, RedirectAttributes model) {
        if (user.isDisabled()) {
            model.addAttribute(CommonConstants.ERROR, "verify.user.notEnablie");
            return true;
        }
        return false;
    }
}

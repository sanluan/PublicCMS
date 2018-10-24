package com.publiccms.controller.admin;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.view.UrlBasedViewResolver;

import com.publiccms.common.api.Config;
import com.publiccms.common.base.AbstractController;
import com.publiccms.common.constants.CommonConstants;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.common.tools.ControllerUtils;
import com.publiccms.common.tools.RequestUtils;
import com.publiccms.common.tools.VerificationUtils;
import com.publiccms.entities.log.LogLogin;
import com.publiccms.entities.log.LogOperate;
import com.publiccms.entities.sys.SysSite;
import com.publiccms.entities.sys.SysUser;
import com.publiccms.entities.sys.SysUserToken;
import com.publiccms.logic.component.cache.CacheComponent;
import com.publiccms.logic.component.config.ConfigComponent;
import com.publiccms.logic.component.config.LoginConfigComponent;
import com.publiccms.logic.service.log.LogLoginService;
import com.publiccms.logic.service.sys.SysUserService;
import com.publiccms.logic.service.sys.SysUserTokenService;

/**
 *
 * LoginAdminController
 *
 */
@Controller
public class LoginAdminController extends AbstractController {

    @Autowired
    private SysUserService service;
    @Autowired
    private SysUserTokenService sysUserTokenService;
    @Autowired
    private LogLoginService logLoginService;
    @Autowired
    private CacheComponent cacheComponent;
    @Autowired
    private ConfigComponent configComponent;

    /**
     * @param username
     * @param password
     * @param returnUrl
     * @param request
     * @param session
     * @param response
     * @param model
     * @return view name
     */
    @RequestMapping(value = "login", method = RequestMethod.POST)
    public String login(String username, String password, String returnUrl, HttpServletRequest request, HttpSession session,
            HttpServletResponse response, ModelMap model) {
        SysSite site = getSite(request);
        username = StringUtils.trim(username);
        password = StringUtils.trim(password);
        if (ControllerUtils.verifyNotEmpty("username", username, model)
                || ControllerUtils.verifyNotEmpty("password", password, model)) {
            model.addAttribute("username", username);
            model.addAttribute("returnUrl", returnUrl);
            return "login";
        }
        String ip = RequestUtils.getIpAddress(request);
        SysUser user = service.findByName(site.getId(), username);
        if (ControllerUtils.verifyNotExist("username", user, model)
                || ControllerUtils.verifyNotEquals("password", VerificationUtils.md5Encode(password), user.getPassword(), model)
                || verifyNotAdmin(user, model) || verifyNotEnablie(user, model)) {
            model.addAttribute("username", username);
            model.addAttribute("returnUrl", returnUrl);
            Long userId = null;
            if (null != user) {
                userId = user.getId();
            }
            logLoginService.save(new LogLogin(site.getId(), username, userId, ip, LogLoginService.CHANNEL_WEB_MANAGER, false,
                    CommonUtils.getDate(), password));
            return "login";
        }

        ControllerUtils.setAdminToSession(session, user);
        service.updateLoginStatus(user.getId(), ip);
        String authToken = UUID.randomUUID().toString();
        Date now = CommonUtils.getDate();
        Map<String, String> config = configComponent.getConfigData(site.getId(), Config.CONFIG_CODE_SITE);
        int expiryMinutes = ConfigComponent.getInt(config.get(LoginConfigComponent.CONFIG_EXPIRY_MINUTES_WEB),
                LoginConfigComponent.DEFAULT_EXPIRY_MINUTES);
        sysUserTokenService.save(new SysUserToken(authToken, site.getId(), user.getId(), LogLoginService.CHANNEL_WEB_MANAGER, now,
                DateUtils.addMinutes(now, expiryMinutes), ip));
        try {
            StringBuilder sb = new StringBuilder();
            sb.append(user.getId()).append(CommonConstants.getCookiesUserSplit()).append(authToken)
                    .append(CommonConstants.getCookiesUserSplit()).append(user.isSuperuserAccess())
                    .append(CommonConstants.getCookiesUserSplit())
                    .append(URLEncoder.encode(user.getNickName(), CommonConstants.DEFAULT_CHARSET_NAME));
            RequestUtils.addCookie(request.getContextPath(), response, CommonConstants.getCookiesAdmin(), sb.toString(),
                    expiryMinutes * 60, null);
        } catch (UnsupportedEncodingException e) {
            log.error(e.getMessage(), e);
        }
        logLoginService.save(new LogLogin(site.getId(), username, user.getId(), ip, LogLoginService.CHANNEL_WEB_MANAGER, true,
                CommonUtils.getDate(), null));
        if (isUnSafeUrl(returnUrl, site, request)) {
            return UrlBasedViewResolver.REDIRECT_URL_PREFIX + returnUrl;
        }
        return UrlBasedViewResolver.REDIRECT_URL_PREFIX + CommonConstants.getDefaultPage();
    }

    /**
     * @param username
     * @param password
     * @param request
     * @param session
     * @param response
     * @param model
     * @return view name
     */
    @RequestMapping(value = "loginDialog", method = RequestMethod.POST)
    public String loginDialog(String username, String password, HttpServletRequest request, HttpSession session,
            HttpServletResponse response, ModelMap model) {
        if ("login".equals(login(username, password, null, request, session, response, model))) {
            return CommonConstants.TEMPLATE_ERROR;
        }
        return CommonConstants.TEMPLATE_DONE;
    }

    /**
     * @param oldpassword
     * @param password
     * @param repassword
     * @param _csrf
     * @param request
     * @param session
     * @param response
     * @param model
     * @return view name
     */
    @RequestMapping(value = "changePassword", method = RequestMethod.POST)
    public String changeMyselfPassword(String oldpassword, String password, String repassword, String _csrf,
            HttpServletRequest request, HttpSession session, HttpServletResponse response, ModelMap model) {
        SysSite site = getSite(request);
        SysUser user = service.getEntity(ControllerUtils.getAdminFromSession(session).getId());
        if (ControllerUtils.verifyNotEquals("siteId", site.getId(), user.getSiteId(), model)
                || ControllerUtils.verifyNotEquals("_csrf", ControllerUtils.getAdminToken(request), _csrf, model)) {
            return CommonConstants.TEMPLATE_ERROR;
        }
        String encodedOldPassword = VerificationUtils.md5Encode(oldpassword);
        if (ControllerUtils.verifyNotEquals("password", user.getPassword(), encodedOldPassword, model)) {
            return CommonConstants.TEMPLATE_ERROR;
        } else if (ControllerUtils.verifyNotEmpty("password", password, model)
                || ControllerUtils.verifyNotEquals("repassword", password, repassword, model)) {
            return CommonConstants.TEMPLATE_ERROR;
        } else {
            ControllerUtils.clearAdminToSession(request.getContextPath(), request.getSession(), response);
            model.addAttribute(CommonConstants.MESSAGE, "message.needReLogin");
        }
        service.updatePassword(user.getId(), VerificationUtils.md5Encode(password));
        sysUserTokenService.delete(user.getId());
        logOperateService.save(new LogOperate(site.getId(), user.getId(), LogLoginService.CHANNEL_WEB_MANAGER, "changepassword",
                RequestUtils.getIpAddress(request), CommonUtils.getDate(), encodedOldPassword));
        return "common/ajaxTimeout";
    }

    /**
     * @param userId
     * @param request
     * @param response
     * @return view name
     */
    @RequestMapping(value = "logout", method = RequestMethod.GET)
    public String logout(Long userId, HttpServletRequest request, HttpServletResponse response) {
        SysUser admin = ControllerUtils.getAdminFromSession(request.getSession());
        if (null != userId && null != admin && userId.equals(admin.getId())) {
            Cookie userCookie = RequestUtils.getCookie(request.getCookies(), CommonConstants.getCookiesAdmin());
            if (null != userCookie && CommonUtils.notEmpty(userCookie.getValue())) {
                String value = userCookie.getValue();
                if (null != value) {
                    String[] userData = value.split(CommonConstants.getCookiesUserSplit());
                    if (userData.length > 1) {
                        sysUserTokenService.delete(userData[1]);
                    }
                }
            }
            ControllerUtils.clearAdminToSession(request.getContextPath(), request.getSession(), response);
        }
        return UrlBasedViewResolver.REDIRECT_URL_PREFIX + CommonConstants.getDefaultPage();
    }

    /**
     * @return view name
     */
    @RequestMapping(value = "clearCache")
    public String clearCache() {
        cacheComponent.clear();
        return CommonConstants.TEMPLATE_DONE;
    }

    protected boolean verifyNotAdmin(SysUser user, ModelMap model) {
        if (!user.isDisabled() && !user.isSuperuserAccess()) {
            model.addAttribute(CommonConstants.ERROR, "verify.user.notAdmin");
            return true;
        }
        return false;
    }

    protected boolean verifyNotEnablie(SysUser user, ModelMap model) {
        if (user.isDisabled()) {
            model.addAttribute(CommonConstants.ERROR, "verify.user.notEnablie");
            return true;
        }
        return false;
    }
}

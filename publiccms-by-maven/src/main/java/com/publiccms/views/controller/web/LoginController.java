package com.publiccms.views.controller.web;

import static com.publiccms.common.constants.CommonConstants.COOKIES_USER;
import static com.publiccms.common.constants.CommonConstants.COOKIES_USER_SPLIT;
import static com.publiccms.logic.service.log.LogLoginService.CHANNEL_WEB;
import static com.sanluan.common.tools.RequestUtils.addCookie;
import static com.sanluan.common.tools.RequestUtils.getCookie;
import static com.sanluan.common.tools.RequestUtils.getIpAddress;
import static com.sanluan.common.tools.VerificationUtils.encode;

import java.util.UUID;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.publiccms.common.base.AbstractController;
import com.publiccms.entities.log.LogLogin;
import com.publiccms.entities.sys.SysDomain;
import com.publiccms.entities.sys.SysEmailToken;
import com.publiccms.entities.sys.SysSite;
import com.publiccms.entities.sys.SysUser;
import com.publiccms.entities.sys.SysUserToken;
import com.publiccms.logic.service.log.LogLoginService;
import com.publiccms.logic.service.sys.SysEmailTokenService;
import com.publiccms.logic.service.sys.SysUserService;
import com.publiccms.logic.service.sys.SysUserTokenService;

/**
 * 
 * LoginController 登陆逻辑
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
    private SysEmailTokenService sysEmailTokenService;

    /**
     * @param username
     * @param password
     * @param callback
     * @param returnUrl
     * @param request
     * @param session
     * @param response
     * @param model
     * @return
     */
    @RequestMapping(value = "doLogin", method = RequestMethod.POST)
    public String login(String username, String password, String returnUrl, HttpServletRequest request, HttpSession session,
            HttpServletResponse response, ModelMap model) {
        SysSite site = getSite(request);
        SysDomain domain = getDomain(request);
        if (virifyNotEmpty("domain", domain, model) || virifyNotEmpty("loginPath", domain.getLoginPath(), model)
                || virifyNotEmpty("username", username, model) || virifyNotEmpty("password", password, model)) {
            model.addAttribute("username", username);
            model.addAttribute("returnUrl", returnUrl);
            return REDIRECT + domain.getLoginPath();
        }
        SysUser user;
        if (virifyNotEMail(username)) {
            user = service.findByName(site.getId(), username);
        } else {
            user = service.findByEmail(site.getId(), username);
        }
        String ip = getIpAddress(request);
        if (virifyNotExist("username", user, model) || virifyNotEquals("password", encode(password), user.getPassword(), model)
                || virifyNotEnablie(user, model)) {
            model.addAttribute("username", username);
            model.addAttribute("returnUrl", returnUrl);
            Integer userId = null;
            if (notEmpty(user)) {
                userId = user.getId();
            }
            logLoginService.save(new LogLogin(site.getId(), username, userId, ip, CHANNEL_WEB, false, getDate(), password));
            return REDIRECT + domain.getLoginPath();
        }
        user.setPassword(null);
        setUserToSession(session, user);
        String authToken = UUID.randomUUID().toString();
        addCookie(request.getContextPath(), response, COOKIES_USER, user.getId() + COOKIES_USER_SPLIT + authToken,
                Integer.MAX_VALUE, null);
        sysUserTokenService.save(new SysUserToken(authToken, site.getId(), user.getId(), CHANNEL_WEB, getDate(), ip));
        service.updateLoginStatus(user.getId(), ip);
        logLoginService.save(new LogLogin(site.getId(), username, user.getId(), ip, CHANNEL_WEB, true, getDate(), null));
        return REDIRECT + returnUrl;
    }

    /**
     * @param callback
     * @param request
     * @param session
     * @param response
     * @param model
     * @return
     */
    @RequestMapping(value = "loginStatus")
    @ResponseBody
    public MappingJacksonValue loginStatus(String callback, HttpServletRequest request, HttpSession session,
            HttpServletResponse response, ModelMap model) {
        SysUser user = getUserFromSession(session);
        if (notEmpty(user)) {
            model.addAttribute("id", user.getId());
            model.addAttribute("name", user.getName());
            model.addAttribute("nickname", user.getNickName());
            model.addAttribute("email", user.getEmail());
            model.addAttribute("emailChecked", user.isEmailChecked());
            model.addAttribute("superuserAccess", user.isSuperuserAccess());
        }
        return getMappingJacksonValue(model, callback);
    }

    /**
     * @param entity
     * @param repassword
     * @param callback
     * @param request
     * @param session
     * @param response
     * @param model
     * @return
     */
    @RequestMapping(value = "doRegister", method = RequestMethod.POST)
    public String register(SysUser entity, String repassword, String returnUrl, HttpServletRequest request, HttpSession session,
            HttpServletResponse response, ModelMap model) {
        SysSite site = getSite(request);
        SysDomain domain = getDomain(request);
        if (virifyNotEmpty("domain", domain, model) || virifyNotEmpty("registerPath", domain.getRegisterPath(), model)
                || virifyNotEmpty("username", entity.getName(), model) || virifyNotEmpty("nickname", entity.getNickName(), model)
                || virifyNotEmpty("password", entity.getPassword(), model)
                || virifyNotUserName("username", entity.getName(), model)
                || virifyNotNickName("nickname", entity.getNickName(), model)
                || virifyNotEquals("repassword", entity.getPassword(), repassword, model)
                || virifyHasExist("username", service.findByName(site.getId(), entity.getName()), model)
                || virifyHasExist("nickname", service.findByNickName(site.getId(), entity.getNickName()), model)) {
            return REDIRECT + domain.getRegisterPath();
        }
        String ip = getIpAddress(request);
        entity.setPassword(encode(entity.getPassword()));
        entity.setLastLoginIp(ip);
        entity.setSiteId(site.getId());
        service.save(entity);
        String authToken = UUID.randomUUID().toString();
        entity.setPassword(null);
        setUserToSession(session, entity);
        addCookie(request.getContextPath(), response, COOKIES_USER, entity.getId() + COOKIES_USER_SPLIT + authToken,
                Integer.MAX_VALUE, null);
        sysUserTokenService.save(new SysUserToken(authToken, site.getId(), entity.getId(), CHANNEL_WEB, getDate(), ip));
        return REDIRECT + returnUrl;
    }

    /**
     * @param code
     * @param session
     * @param model
     * @return
     */
    @RequestMapping("verifyEmail")
    @ResponseBody
    public String verifyEmail(String authToken, String returnUrl, HttpSession session, ModelMap model) {
        SysEmailToken sysEmailToken = sysEmailTokenService.getEntity(authToken);
        if (virifyNotEmpty("verifyEmail.authToken", authToken, model)
                || virifyNotExist("verifyEmail.sysEmailToken", sysEmailToken, model)) {
            return REDIRECT + returnUrl;
        }
        sysEmailTokenService.delete(sysEmailToken.getAuthToken());
        service.checked(sysEmailToken.getUserId(), sysEmailToken.getEmail());
        clearUserTimeToSession(session);
        model.addAttribute(MESSAGE, "verifyEmail.success");
        return REDIRECT + returnUrl;
    }

    /**
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "logout", method = RequestMethod.GET)
    public String logout(String returnUrl, HttpServletRequest request, HttpServletResponse response) {
        Cookie userCookie = getCookie(request.getCookies(), COOKIES_USER);
        if (null != userCookie && notEmpty(userCookie.getValue())) {
            String value = userCookie.getValue();
            if (null != value) {
                String[] userData = value.split(COOKIES_USER_SPLIT);
                if (userData.length > 1) {
                    sysUserTokenService.delete(userData[1]);
                }
            }
        }
        clearUserToSession(request.getContextPath(), request.getSession(), response);
        if (notEmpty(returnUrl)) {
            return REDIRECT + returnUrl;
        }
        SysDomain domain = getDomain(request);
        return REDIRECT + domain.getLoginPath();
    }

    protected boolean virifyNotEnablie(SysUser user, ModelMap model) {
        if (user.isDisabled()) {
            model.addAttribute(ERROR, "verify.user.notEnablie");
            return true;
        }
        return false;
    }
}

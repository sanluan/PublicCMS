package org.publiccms.controller.web;

import static com.publiccms.common.tools.CommonUtils.empty;
import static com.publiccms.common.tools.CommonUtils.getDate;
import static com.publiccms.common.tools.CommonUtils.notEmpty;
import static com.publiccms.common.tools.ControllerUtils.verifyHasExist;
import static com.publiccms.common.tools.ControllerUtils.verifyNotEmpty;
import static com.publiccms.common.tools.ControllerUtils.verifyNotEquals;
import static com.publiccms.common.tools.ControllerUtils.verifyNotExist;
import static com.publiccms.common.tools.RequestUtils.addCookie;
import static com.publiccms.common.tools.RequestUtils.getCookie;
import static com.publiccms.common.tools.RequestUtils.getIpAddress;
import static com.publiccms.common.tools.VerificationUtils.encode;
import static org.apache.commons.lang3.StringUtils.trim;
import static org.publiccms.common.api.Config.CONFIG_CODE_SITE;
import static org.publiccms.common.constants.CommonConstants.getCookiesUser;
import static org.publiccms.common.constants.CommonConstants.getCookiesUserSplit;
import static org.publiccms.logic.component.config.LoginConfigComponent.CONFIG_LOGIN_PATH;
import static org.publiccms.logic.service.log.LogLoginService.CHANNEL_WEB;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.publiccms.common.base.AbstractController;
import org.publiccms.entities.log.LogLogin;
import org.publiccms.entities.sys.SysSite;
import org.publiccms.entities.sys.SysUser;
import org.publiccms.entities.sys.SysUserToken;
import org.publiccms.logic.component.config.ConfigComponent;
import org.publiccms.logic.service.log.LogLoginService;
import org.publiccms.logic.service.sys.SysUserService;
import org.publiccms.logic.service.sys.SysUserTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

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
    private ConfigComponent configComponent;

    /**
     * @param username
     * @param password
     * @param returnUrl
     * @param request
     * @param response
     * @param model
     * @return 
     */
    @RequestMapping(value = "doLogin", method = RequestMethod.POST)
    public String login(String username, String password, String returnUrl, HttpServletRequest request,
            HttpServletResponse response, ModelMap model) {
        SysSite site = getSite(request);
        if (empty(returnUrl)) {
            returnUrl = site.getDynamicPath();
        }
        Map<String, String> config = configComponent.getConfigData(site.getId(), CONFIG_CODE_SITE);
        String loginPath = config.get(CONFIG_LOGIN_PATH);
        if (empty(loginPath)) {
            loginPath = site.getDynamicPath();
        }
        username = trim(username);
        password = trim(password);
        if (verifyNotEmpty("username", username, model) || verifyNotEmpty("password", password, model)) {
            return REDIRECT + returnUrl;
        } else {
            SysUser user;
            if (verifyNotEMail(username)) {
                user = service.findByName(site.getId(), username);
            } else {
                user = service.findByEmail(site.getId(), username);
            }
            String ip = getIpAddress(request);
            if (verifyNotExist("username", user, model)
                    || verifyNotEquals("password", encode(password), user.getPassword(), model)
                    || verifyNotEnablie(user, model)) {
                Long userId = null;
                if (null != user) {
                    userId = user.getId();
                }
                logLoginService.save(new LogLogin(site.getId(), username, userId, ip, CHANNEL_WEB, false, getDate(), password));
                return REDIRECT + returnUrl;
            } else {
                user.setPassword(null);
                setUserToSession(request.getSession(), user);
                String authToken = UUID.randomUUID().toString();
                sysUserTokenService.save(new SysUserToken(authToken, site.getId(), user.getId(), CHANNEL_WEB, getDate(), ip));
                try {
                    StringBuilder sb = new StringBuilder();
                    sb.append(user.getId()).append(getCookiesUserSplit()).append(authToken).append(getCookiesUserSplit())
                            .append(user.isSuperuserAccess()).append(getCookiesUserSplit())
                            .append(URLEncoder.encode(user.getNickName(), DEFAULT_CHARSET_NAME));
                    addCookie(request.getContextPath(), response, getCookiesUser(), sb.toString(), Integer.MAX_VALUE, null);
                } catch (UnsupportedEncodingException e) {
                    log.error(e);
                }
                service.updateLoginStatus(user.getId(), ip);
                logLoginService.save(new LogLogin(site.getId(), username, user.getId(), ip, CHANNEL_WEB, true, getDate(), null));
                return REDIRECT + returnUrl;
            }
        }
    }

    /**
     * @param request
     * @param session
     * @param response
     * @param model
     * @return
     */
    @RequestMapping("loginStatus")
    @ResponseBody
    public ModelMap loginStatus(HttpServletRequest request, HttpSession session, HttpServletResponse response, ModelMap model) {
        SysUser user = getUserFromSession(session);
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
     * @param request
     * @param response
     * @param model
     * @return 
     */
    @RequestMapping(value = "doRegister", method = RequestMethod.POST)
    public String register(SysUser entity, String repassword, String returnUrl, HttpServletRequest request,
            HttpServletResponse response, ModelMap model) {
        SysSite site = getSite(request);
        if (empty(returnUrl)) {
            returnUrl = site.getDynamicPath();
        }
        entity.setName(trim(entity.getName()));
        entity.setNickName(trim(entity.getNickName()));
        entity.setPassword(trim(entity.getPassword()));
        repassword = trim(repassword);

        if (verifyNotEmpty("username", entity.getName(), model) || verifyNotEmpty("nickname", entity.getNickName(), model)
                || verifyNotEmpty("password", entity.getPassword(), model)
                || verifyNotUserName("username", entity.getName(), model)
                || verifyNotNickName("nickname", entity.getNickName(), model)
                || verifyNotEquals("repassword", entity.getPassword(), repassword, model)
                || verifyHasExist("username", service.findByName(site.getId(), entity.getName()), model)
                || verifyHasExist("nickname", service.findByNickName(site.getId(), entity.getNickName()), model)) {
            return REDIRECT + returnUrl;
        } else {
            String ip = getIpAddress(request);
            entity.setPassword(encode(entity.getPassword()));
            entity.setLastLoginIp(ip);
            entity.setSiteId(site.getId());
            service.save(entity);
            entity.setPassword(null);
            setUserToSession(request.getSession(), entity);
            String authToken = UUID.randomUUID().toString();
            sysUserTokenService.save(new SysUserToken(authToken, site.getId(), entity.getId(), CHANNEL_WEB, getDate(), ip));
            String loginInfo = entity.getId() + getCookiesUserSplit() + authToken + getCookiesUserSplit() + entity.getNickName();
            model.addAttribute("loginInfo", loginInfo);
            addCookie(request.getContextPath(), response, getCookiesUser(), loginInfo, Integer.MAX_VALUE, null);
            return REDIRECT + returnUrl;
        }
    }

    /**
     * @param request
     * @param response
     * @param model
     */
    @RequestMapping(value = "doLogout", method = RequestMethod.POST)
    public void logout(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
        Cookie userCookie = getCookie(request.getCookies(), getCookiesUser());
        if (null != userCookie && notEmpty(userCookie.getValue())) {
            String value = userCookie.getValue();
            if (null != value) {
                String[] userData = value.split(getCookiesUserSplit());
                if (userData.length > 1) {
                    sysUserTokenService.delete(userData[1]);
                }
            }
        }
        clearUserToSession(request.getContextPath(), request.getSession(), response);
    }

    public boolean verifyNotEnablie(SysUser user, ModelMap model) {
        if (user.isDisabled()) {
            model.addAttribute(ERROR, "verify.user.notEnablie");
            return true;
        }
        return false;
    }
}

package com.publiccms.views.controller.web.changyan;

import static com.publiccms.common.constants.CommonConstants.getCookiesUser;
import static com.publiccms.common.constants.CommonConstants.getCookiesUserSplit;
import static com.publiccms.views.controller.web.changyan.tools.HmacSha1Utils.getSign;
import static com.sanluan.common.tools.RequestUtils.addCookie;
import static com.sanluan.common.tools.RequestUtils.getCookie;
import static com.sanluan.common.tools.RequestUtils.getIpAddress;

import java.util.UUID;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.publiccms.common.base.AbstractController;
import com.publiccms.entities.log.LogLogin;
import com.publiccms.entities.sys.SysSite;
import com.publiccms.entities.sys.SysUser;
import com.publiccms.entities.sys.SysUserToken;
import com.publiccms.logic.service.log.LogLoginService;
import com.publiccms.logic.service.sys.SysUserService;
import com.publiccms.logic.service.sys.SysUserTokenService;
import com.publiccms.views.controller.web.changyan.pojo.LoginResult;
import com.publiccms.views.controller.web.changyan.pojo.LogoutResult;
import com.publiccms.views.controller.web.changyan.pojo.User;
import com.publiccms.views.controller.web.changyan.pojo.UserInfo;

@Controller
@RequestMapping("changyan")
public class ChangyanController extends AbstractController {
    private static final LogoutResult LOGOUTRESULT = new LogoutResult(1, 0);
    private static final String CHANNEL = "changyan";
    @Autowired
    private SysUserService service;
    @Autowired
    private SysUserTokenService sysUserTokenService;
    @Autowired
    private LogLoginService logLoginService;

    @RequestMapping("login")
    public MappingJacksonValue login(User user, String callback, HttpServletRequest request, HttpSession session,
            HttpServletResponse response) {
        LoginResult loginResult = new LoginResult();
        String ip = getIpAddress(request);
        SysSite site = getSite(request);
        if (empty(user.getUser_id())) {
            SysUser entity = new SysUser();
            entity.setNickName(user.getNickname());
            entity.setPassword(null);
            entity.setLastLoginIp(ip);
            entity.setSiteId(site.getId());
            service.save(entity);
            String authToken = UUID.randomUUID().toString();
            entity.setPassword(null);
            setUserToSession(session, entity);
            addCookie(request.getContextPath(), response, getCookiesUser(), entity.getId() + getCookiesUserSplit() + authToken,
                    Integer.MAX_VALUE, null);
            sysUserTokenService.save(new SysUserToken(authToken, site.getId(), entity.getId(), CHANNEL, getDate(), ip));
            loginResult.setUser_id(entity.getId());
        } else {
            SysUser sysuser = service.getEntity(user.getUser_id());
            if (notEmpty(sysuser) && site.getId() == sysuser.getSiteId() && !sysuser.isDisabled()) {
                sysuser.setPassword(null);
                setUserToSession(session, sysuser);
                String authToken = UUID.randomUUID().toString();
                addCookie(request.getContextPath(), response, getCookiesUser(),
                        sysuser.getId() + getCookiesUserSplit() + authToken, Integer.MAX_VALUE, null);
                sysUserTokenService.save(new SysUserToken(authToken, site.getId(), sysuser.getId(), CHANNEL, getDate(), ip));
                service.updateLoginStatus(sysuser.getId(), ip);
                logLoginService
                        .save(new LogLogin(site.getId(), sysuser.getName(), sysuser.getId(), ip, CHANNEL, true, getDate(), null));
            } else {
                logLoginService.save(new LogLogin(site.getId(), null, user.getUser_id(), ip, CHANNEL, false, getDate(), null));
            }
        }
        return getMappingJacksonValue(loginResult, callback);
    }

    @RequestMapping("loginout")
    public MappingJacksonValue login(String callback, HttpServletRequest request, HttpServletResponse response) {
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
        return getMappingJacksonValue(LOGOUTRESULT, callback);
    }

    @RequestMapping("getUserInfo")
    public MappingJacksonValue getUserInfo(String callback, HttpSession session) {
        SysUser sysUser = getUserFromSession(session);
        UserInfo userinfo = new UserInfo();
        if (notEmpty(sysUser)) {
            userinfo.setIs_login(1);
            User user = new User();
            user.setUser_id(sysUser.getId());
            user.setNickname(sysUser.getNickName());
            user.setSign(getSign("", sysUser.getId().toString(), sysUser.getNickName()));
            userinfo.setUser(user);
        } else {
            userinfo.setIs_login(0);// 用户未登录
        }
        return getMappingJacksonValue(userinfo, callback);
    }
}

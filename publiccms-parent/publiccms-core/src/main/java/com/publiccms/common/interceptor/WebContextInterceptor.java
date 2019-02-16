package com.publiccms.common.interceptor;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.util.UrlPathHelper;

import com.publiccms.common.annotation.Csrf;
import com.publiccms.common.constants.CmsVersion;
import com.publiccms.common.constants.CommonConstants;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.common.tools.ControllerUtils;
import com.publiccms.common.tools.LanguagesUtils;
import com.publiccms.common.tools.RequestUtils;
import com.publiccms.entities.log.LogLogin;
import com.publiccms.entities.sys.SysSite;
import com.publiccms.entities.sys.SysUser;
import com.publiccms.entities.sys.SysUserToken;
import com.publiccms.logic.component.site.SiteComponent;
import com.publiccms.logic.service.log.LogLoginService;
import com.publiccms.logic.service.sys.SysUserService;
import com.publiccms.logic.service.sys.SysUserTokenService;

/**
 * 
 * WebContextInterceptor 权限拦截器
 *
 */
public class WebContextInterceptor extends HandlerInterceptorAdapter {
    protected UrlPathHelper urlPathHelper = new UrlPathHelper();
    protected final Log log = LogFactory.getLog(getClass());
    @Autowired
    private SysUserService sysUserService;
    @Autowired
    private SysUserTokenService sysUserTokenService;
    @Autowired
    private SiteComponent siteComponent;
    @Autowired
    private LogLoginService logLoginService;
    protected LocaleChangeInterceptor localeChangeInterceptor = new LocaleChangeInterceptor();
    private Map<HandlerMethod, Boolean> methodCache = new HashMap<>();

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws ServletException {
        if (unsafe(request, response, handler)) {
            return false;
        }
        SysSite site = siteComponent.getSite(request.getServerName());
        request.setAttribute("site", site);
        HttpSession session = request.getSession(false);
        SysUser user = initUser(ControllerUtils.getUserFromSession(session), LogLoginService.CHANNEL_WEB,
                CommonConstants.getCookiesUser(), site, request, response);
        if (null != user) {
            if (null == session) {
                session = request.getSession(true);
                ControllerUtils.setUserToSession(session, user);
            } else {
                Long last = ControllerUtils.getUserTimeFromSession(session);
                if (null == last || System.currentTimeMillis() - last > 1000 * 60) {
                    SysUser entity = sysUserService.getEntity(user.getId());
                    if (null != entity && !entity.isDisabled() && null != site && !site.isDisabled()
                            && site.getId() == entity.getSiteId()) {
                        entity.setPassword(null);
                        ControllerUtils.setUserToSession(session, entity);
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
                    }
                }
            }
        }
        localeChangeInterceptor.preHandle(request, response, handler);
        return true;
    }

    protected boolean unsafe(HttpServletRequest request, HttpServletResponse response, Object handler) {
        if (handler instanceof HandlerMethod) {
            boolean flag = false;
            Boolean temp = methodCache.get(handler);
            if (null != temp && temp) {
                flag = true;
            } else {
                HandlerMethod handlerMethod = (HandlerMethod) handler;
                flag = handlerMethod.hasMethodAnnotation(Csrf.class);
                methodCache.put(handlerMethod, flag);
            }
            if (flag) {
                String csrf = request.getParameter("_csrf");
                if (null == csrf || !csrf.equals(ControllerUtils.getAdminToken(request))) {
                    try {
                        String message = LanguagesUtils.getMessage(CommonConstants.applicationContext, request.getLocale(),
                                "verify.notEquals._csrf");
                        response.sendError(HttpServletResponse.SC_FORBIDDEN, message);
                    } catch (IOException e) {
                    }
                    return true;
                }
            }
        }
        return false;
    }

    protected SysUser initUser(SysUser user, String channel, String cookiesName, SysSite site, HttpServletRequest request,
            HttpServletResponse response) {
        response.addHeader(CommonConstants.getXPowered(), CmsVersion.getVersion());
        String contextPath = request.getContextPath();
        if (null == user) {
            Cookie userCookie = RequestUtils.getCookie(request.getCookies(), cookiesName);
            if (null != userCookie && CommonUtils.notEmpty(userCookie.getValue())) {
                String value = userCookie.getValue();
                if (null != value) {
                    String[] userData = value.split(CommonConstants.getCookiesUserSplit());
                    if (userData.length > 1) {
                        try {
                            Long userId = Long.parseLong(userData[0]);
                            SysUserToken userToken = sysUserTokenService.getEntity(userData[1]);
                            if (null != userToken && null != site && !site.isDisabled() && userToken.getSiteId() == site.getId()
                                    && userToken.getUserId() == userId && channel.equals(userToken.getChannel())
                                    && (null == userToken.getExpiryDate()
                                            || CommonUtils.getDate().before(userToken.getExpiryDate()))
                                    && null != (user = sysUserService.getEntity(userId)) && !user.isDisabled()) {
                                user.setPassword(null);
                                String ip = RequestUtils.getIpAddress(request);
                                sysUserService.updateLoginStatus(user.getId(), ip);
                                logLoginService.save(new LogLogin(site.getId(), user.getName(), user.getId(), ip, channel, true,
                                        CommonUtils.getDate(), null));
                            } else {
                                user = null;
                                if (null != userToken) {
                                    sysUserTokenService.delete(userToken.getAuthToken());
                                }
                                RequestUtils.cancleCookie(contextPath, response, cookiesName, null);
                            }
                        } catch (NumberFormatException e) {
                            RequestUtils.cancleCookie(contextPath, response, cookiesName, null);
                        }
                    } else {
                        RequestUtils.cancleCookie(contextPath, response, cookiesName, null);
                    }
                }
            }
        }
        return user;
    }
}

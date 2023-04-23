package com.publiccms.common.interceptor;

import java.util.Date;
import java.util.Map;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import org.apache.commons.lang3.time.DateUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import jakarta.annotation.Resource;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.util.UrlPathHelper;

import com.publiccms.common.constants.CmsVersion;
import com.publiccms.common.constants.CommonConstants;
import com.publiccms.common.tools.CmsUrlUtils;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.common.tools.ControllerUtils;
import com.publiccms.common.tools.RequestUtils;
import com.publiccms.controller.admin.LoginAdminController;
import com.publiccms.controller.web.LoginController;
import com.publiccms.entities.log.LogLogin;
import com.publiccms.entities.sys.SysDomain;
import com.publiccms.entities.sys.SysSite;
import com.publiccms.entities.sys.SysUser;
import com.publiccms.entities.sys.SysUserToken;
import com.publiccms.logic.component.config.ConfigDataComponent;
import com.publiccms.logic.component.config.SafeConfigComponent;
import com.publiccms.logic.component.site.FileUploadComponent;
import com.publiccms.logic.component.site.SiteComponent;
import com.publiccms.logic.service.log.LogLoginService;
import com.publiccms.logic.service.sys.SysUserService;
import com.publiccms.logic.service.sys.SysUserTokenService;

/**
 * 
 * WebContextInterceptor 权限拦截器
 *
 */
public class WebContextInterceptor implements HandlerInterceptor {
    protected final Log log = LogFactory.getLog(getClass());
    @Resource
    protected SysUserService sysUserService;
    @Resource
    private SysUserTokenService sysUserTokenService;
    @Resource
    protected SiteComponent siteComponent;
    @Resource
    private LogLoginService logLoginService;
    @Resource
    private ConfigDataComponent configDataComponent;
    @Resource
    protected FileUploadComponent fileUploadComponent;

    protected LocaleChangeInterceptor localeChangeInterceptor = new LocaleChangeInterceptor();

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws ServletException {
        SysDomain domain = siteComponent.getDomain(request.getServerName());
        SysSite site = null;
        if (domain.isMultiple()) {
            String currentSiteId = request.getParameter("currentSiteId");
            if (null != currentSiteId) {
                site = siteComponent.getSiteById(currentSiteId);
            }
            if (null == site || (null == site.getParentId() || site.getParentId() != domain.getSiteId())
                    && site.getId() != domain.getSiteId()) {
                site = siteComponent.getSite(domain, request.getServerName(),
                        UrlPathHelper.defaultInstance.getLookupPathForRequest(request));
            }
        } else {
            site = siteComponent.getSite(domain, request.getServerName(), null);
        }
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
                        entity.setCover(CmsUrlUtils.getUrl(fileUploadComponent.getPrefix(site), entity.getCover()));
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
                        ControllerUtils.clearUserToSession(request.getContextPath(), request.getScheme(), session, response);
                    }
                }
            }
        } else if (null != session) {
            ControllerUtils.clearUserToSession(request.getContextPath(), request.getScheme(), session, response);
        }
        localeChangeInterceptor.preHandle(request, response, handler);
        return true;
    }

    protected SysUser initUser(SysUser user, String channel, String cookiesName, SysSite site, HttpServletRequest request,
            HttpServletResponse response) {
        response.setHeader(CommonConstants.getXPowered(), CmsVersion.getVersion());
        String contextPath = request.getContextPath();
        Cookie userCookie = RequestUtils.getCookie(request.getCookies(), cookiesName);
        if (null == user && null != userCookie && CommonUtils.notEmpty(userCookie.getValue())) {
            String[] userData = userCookie.getValue().split(CommonConstants.getCookiesUserSplit());
            if (userData.length > 1) {
                try {
                    Long userId = Long.parseLong(userData[0]);
                    SysUserToken userToken = sysUserTokenService.getEntity(userData[1]);
                    Date now = CommonUtils.getDate();
                    if (null != userToken && null != site && !site.isDisabled() && userToken.getSiteId() == site.getId()
                            && userToken.getUserId() == userId && channel.equals(userToken.getChannel())
                            && (null == userToken.getExpiryDate() || now.before(userToken.getExpiryDate()))
                            && null != (user = sysUserService.getEntity(userId)) && !user.isDisabled()) {
                        user.setPassword(null);
                        String ip = RequestUtils.getIpAddress(request);
                        logLoginService
                                .save(new LogLogin(site.getId(), user.getName(), user.getId(), ip, channel, true, now, null));
                        Map<String, String> config = configDataComponent.getConfigData(site.getId(),
                                SafeConfigComponent.CONFIG_CODE);
                        int expiryMinutes;
                        if (LogLoginService.CHANNEL_WEB.equalsIgnoreCase(channel)) {
                            expiryMinutes = ConfigDataComponent.getInt(config.get(SafeConfigComponent.CONFIG_EXPIRY_MINUTES_WEB),
                                    SafeConfigComponent.DEFAULT_EXPIRY_MINUTES);
                        } else {
                            expiryMinutes = ConfigDataComponent.getInt(
                                    config.get(SafeConfigComponent.CONFIG_EXPIRY_MINUTES_MANAGER),
                                    SafeConfigComponent.DEFAULT_EXPIRY_MINUTES);
                        }
                        if (DateUtils.addMinutes(now, expiryMinutes / 3).after(userToken.getExpiryDate())) {
                            Date expiryDate = DateUtils.addMinutes(now, expiryMinutes);
                            if (LogLoginService.CHANNEL_WEB.equalsIgnoreCase(channel)) {
                                LoginController.addLoginStatus(user, userToken.getAuthToken(), request, response, expiryMinutes);
                            } else {
                                LoginAdminController.addLoginStatus(user, userToken.getAuthToken(), request, response,
                                        expiryMinutes);
                            }
                            sysUserTokenService.updateExpiryDate(userToken.getAuthToken(), expiryDate);
                        }
                    } else {
                        user = null;
                        if (null != userToken) {
                            sysUserTokenService.delete(userToken.getAuthToken());
                        }
                        RequestUtils.cancleCookie(contextPath, request.getScheme(), response, cookiesName, null);
                    }
                } catch (NumberFormatException e) {
                    RequestUtils.cancleCookie(contextPath, request.getScheme(), response, cookiesName, null);
                }
            } else {
                RequestUtils.cancleCookie(contextPath, request.getScheme(), response, cookiesName, null);
            }
        } else if (null != user && (null == userCookie || CommonUtils.empty(userCookie.getValue()))) {
            user = null;
        }
        return user;
    }
}

package com.publiccms.common.interceptor;

import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;

import com.publiccms.common.base.AbstractController;
import com.publiccms.common.base.BaseInterceptor;
import com.publiccms.common.constants.CmsVersion;
import com.publiccms.common.constants.CommonConstants;
import com.publiccms.common.tools.CommonUtils;
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
public class WebContextInterceptor extends BaseInterceptor {
    @Autowired
    private SysUserService sysUserService;
    @Autowired
    private SysUserTokenService sysUserTokenService;
    @Autowired
    private SiteComponent siteComponent;
    @Autowired
    private LogLoginService logLoginService;
    protected LocaleChangeInterceptor localeChangeInterceptor = new LocaleChangeInterceptor();

    protected SysUser initUser(SysUser user, String channel, String cookiesName, SysSite site, HttpServletRequest request,
            HttpServletResponse response) {
        response.addHeader(CommonConstants.getXPowered(), CmsVersion.getVersion());
        String contextPath = request.getContextPath();
        if (null == user) {
            Cookie userCookie = RequestUtils.getCookie(request.getCookies(), cookiesName);
            if (null != userCookie && StringUtils.isNotBlank(userCookie.getValue())) {
                String value = userCookie.getValue();
                if (null != value) {
                    String[] userData = value.split(CommonConstants.getCookiesUserSplit());
                    if (userData.length > 1) {
                        try {
                            Long userId = Long.parseLong(userData[0]);
                            SysUserToken userToken = sysUserTokenService.getEntity(userData[1]);
                            if (null != userToken && null != site && !site.isDisabled() && site.getId() == userToken.getSiteId()
                                    && userId == userToken.getUserId() && channel.equals(userToken.getChannel())
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

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws ServletException {
        HttpSession session = request.getSession();
        SysSite site = siteComponent.getSite(request.getServerName());
        SysUser user = initUser(AbstractController.getUserFromSession(session), LogLoginService.CHANNEL_WEB,
                CommonConstants.getCookiesUser(), site, request, response);
        if (null != user) {
            Date date = AbstractController.getUserTimeFromSession(session);
            if (null == date || date.before(DateUtils.addSeconds(new Date(), -30))) {
                SysUser entity = sysUserService.getEntity(user.getId());
                if (null != entity && !entity.isDisabled() && null != site && !site.isDisabled()
                        && site.getId() == entity.getSiteId()) {
                    user.setName(entity.getName());
                    user.setNickName(entity.getNickName());
                    user.setEmail(entity.getEmail());
                    user.setEmailChecked(entity.isEmailChecked());
                    user.setSuperuserAccess(entity.isSuperuserAccess());
                    AbstractController.setUserToSession(session, user);
                } else {
                    Cookie userCookie = RequestUtils.getCookie(request.getCookies(), CommonConstants.getCookiesUser());
                    if (null != userCookie && StringUtils.isNotBlank(userCookie.getValue())) {
                        String value = userCookie.getValue();
                        if (null != value) {
                            String[] userData = value.split(CommonConstants.getCookiesUserSplit());
                            if (userData.length > 1) {
                                sysUserTokenService.delete(userData[1]);
                            }
                        }
                    }
                    AbstractController.clearUserToSession(request.getContextPath(), session, response);
                }
            }
        }
        localeChangeInterceptor.preHandle(request, response, handler);
        return true;
    }
}

package com.publiccms.common.interceptor.web;

import static com.publiccms.common.base.AbstractController.clearUserToSession;
import static com.publiccms.common.base.AbstractController.getUserFromSession;
import static com.publiccms.common.base.AbstractController.getUserTimeFromSession;
import static com.publiccms.common.base.AbstractController.setUserToSession;
import static com.publiccms.common.constants.CmsVersion.getVersion;
import static com.publiccms.common.constants.CommonConstants.getCookiesUser;
import static com.publiccms.common.constants.CommonConstants.getCookiesUserSplit;
import static com.publiccms.common.constants.CommonConstants.getXPowered;
import static com.publiccms.logic.service.log.LogLoginService.CHANNEL_WEB;
import static com.sanluan.common.tools.RequestUtils.cancleCookie;
import static com.sanluan.common.tools.RequestUtils.getCookie;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.apache.commons.lang3.time.DateUtils.addSeconds;

import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;

import com.publiccms.entities.sys.SysUser;
import com.publiccms.entities.sys.SysUserToken;
import com.publiccms.logic.service.sys.SysUserService;
import com.publiccms.logic.service.sys.SysUserTokenService;
import com.sanluan.common.base.BaseInterceptor;

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

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws ServletException {
        response.addHeader(getXPowered(), getVersion());
        HttpSession session = request.getSession();
        String contextPath = request.getContextPath();
        SysUser user = getUserFromSession(session);
        if (null == user) {
            Cookie userCookie = getCookie(request.getCookies(), getCookiesUser());
            if (null != userCookie && isNotBlank(userCookie.getValue())) {
                String value = userCookie.getValue();
                if (null != value) {
                    String[] userData = value.split(getCookiesUserSplit());
                    if (userData.length > 1) {
                        try {
                            Long userId = Long.parseLong(userData[0]);
                            SysUserToken userToken = sysUserTokenService.getEntity(userData[1]);
                            if (null != userToken && userId == userToken.getUserId() && CHANNEL_WEB.equals(userToken.getChannel())
                                    && null != (user = sysUserService.getEntity(userId)) && !user.isDisabled()) {
                                user.setPassword(null);
                                setUserToSession(session, user);
                            } else {
                                sysUserTokenService.delete(userToken.getAuthToken());
                                cancleCookie(contextPath, response, getCookiesUser(), null);
                            }
                        } catch (NumberFormatException e) {
                            cancleCookie(contextPath, response, getCookiesUser(), null);
                        }
                    }
                }
            }
        } else {
            Date date = getUserTimeFromSession(session);
            if (null == date || date.before(addSeconds(new Date(), -30))) {
                SysUser entity = sysUserService.getEntity(user.getId());
                if (null != entity && !entity.isDisabled()) {
                    user.setName(entity.getName());
                    user.setNickName(entity.getNickName());
                    user.setEmail(entity.getEmail());
                    user.setEmailChecked(entity.isEmailChecked());
                    user.setSuperuserAccess(entity.isSuperuserAccess());
                    setUserToSession(session, user);
                } else {
                    Cookie userCookie = getCookie(request.getCookies(), getCookiesUser());
                    if (null != userCookie && isNotBlank(userCookie.getValue())) {
                        String value = userCookie.getValue();
                        if (null != value) {
                            String[] userData = value.split(getCookiesUserSplit());
                            if (userData.length > 1) {
                                sysUserTokenService.delete(userData[1]);
                            }
                        }
                    }
                    clearUserToSession(contextPath, session, response);
                }
            }
        }
        return true;
    }
}

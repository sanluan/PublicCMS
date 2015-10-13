package com.publiccms.web.common.interceptor;

import static com.sanluan.common.constants.CommonConstants.COOKIES_USER;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.apache.commons.lang3.time.DateUtils.addSeconds;

import java.io.IOException;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;

import com.publiccms.common.tools.UserUtils;
import com.publiccms.entities.system.SystemUser;
import com.publiccms.logic.service.system.SystemUserService;
import com.sanluan.common.base.BaseInterceptor;

/**
 * 
 * WebContextInterceptor 权限拦截器
 *
 */
public class WebContextInterceptor extends BaseInterceptor {
    private String[] needLoginUrls;
    private String loginUrl;
    @Autowired
    private SystemUserService systemUserService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws ServletException {
        SystemUser user = UserUtils.getUserFromSession(request.getSession());
        if (null == user) {
            Cookie userCookie = UserUtils.getCookie(request, COOKIES_USER);
            if (null != userCookie && isNotBlank(userCookie.getValue())) {
                user = systemUserService.findByAuthToken(userCookie.getValue());
                if (null != user) {
                    UserUtils.setUserToSession(request.getSession(), user);
                } else {
                    UserUtils.cancleCookie(request, response, COOKIES_USER, null);
                }
            }
        } else {
            Date date = UserUtils.getUserTimeFromSession(request.getSession());
            if (null == date || date.before(addSeconds(new Date(), -30))) {
                user = systemUserService.getEntity(user.getId());
                UserUtils.setUserToSession(request.getSession(), user);
            }
        }

        if (verify(getURL(request))) {
            if (null != user && user.isDisabled()) {
                user = null;
                UserUtils.clearUserToSession(request, response);
                UserUtils.cancleCookie(request, response, COOKIES_USER, null);
            }
            if (null == user) {
                try {
                    response.sendRedirect(urlPathHelper.getOriginatingContextPath(request) + loginUrl + "?returnUrl="
                            + getURL(request) + getEncodeQueryString(request.getQueryString()));
                    return false;
                } catch (IOException e) {
                    log.debug(e.getMessage());
                }
            }
        }
        return true;
    }

    private boolean verify(String url) {
        if (null != needLoginUrls && null != loginUrl && null != url) {
            for (String needLoginUrl : needLoginUrls) {
                if (null != needLoginUrl) {
                    if (url.startsWith(needLoginUrl)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * @param needLoginUrls
     *            the needLoginUrls to set
     */
    public void setNeedLoginUrls(String[] needLoginUrls) {
        this.needLoginUrls = needLoginUrls;
    }

    /**
     * @param loginUrl
     *            the loginUrl to set
     */
    public void setLoginUrl(String loginUrl) {
        this.loginUrl = loginUrl;
    }
}

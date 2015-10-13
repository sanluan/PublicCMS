package com.publiccms.admin.common.interceptor;

import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.apache.commons.lang3.StringUtils.split;
import static org.apache.commons.lang3.time.DateUtils.addSeconds;
import static org.apache.commons.logging.LogFactory.getLog;

import java.io.IOException;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.springframework.beans.factory.annotation.Autowired;

import com.publiccms.common.tools.UserUtils;
import com.publiccms.entities.system.SystemUser;
import com.publiccms.logic.service.system.SystemRoleAuthorizedService;
import com.publiccms.logic.service.system.SystemRoleService;
import com.publiccms.logic.service.system.SystemUserService;
import com.sanluan.common.base.BaseInterceptor;

public class AdminContextInterceptor extends BaseInterceptor {
    private static final String SEPARATOR = "/";

    private String[] needNotLoginUrls;
    private String loginUrl;
    private String loginJsonUrl;
    private String unauthorizedUrl;
    @Autowired
    private SystemRoleAuthorizedService roleAuthorizedService;
    @Autowired
    private SystemRoleService systemRoleService;
    @Autowired
    private SystemUserService systemUserService;

    private final Log log = getLog(getClass());

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws ServletException {
        if (verify(getURL(request))) {
            SystemUser user = UserUtils.getAdminFromSession(request.getSession());
            if (null == user) {
                try {
                    if ("XMLHttpRequest".equalsIgnoreCase(request.getHeader("X-Requested-With"))) {
                        response.sendRedirect(urlPathHelper.getOriginatingContextPath(request) + loginJsonUrl);
                    } else {
                        response.sendRedirect(urlPathHelper.getOriginatingContextPath(request) + loginUrl + "?returnUrl="
                                + getURL(request) + getEncodeQueryString(request.getQueryString()));
                    }
                    return false;
                } catch (IllegalStateException e) {
                    log.error(e.getMessage());
                } catch (IOException e) {
                    log.error(e.getMessage());
                }
            } else {
                Date date = UserUtils.getUserTimeFromSession(request.getSession());
                if (null == date || date.before(addSeconds(new Date(), -30))) {
                    user = systemUserService.getEntity(user.getId());
                    UserUtils.setUserToSession(request.getSession(), user);
                }
                if (!user.isDisabled() && !user.isSuperuserAccess()) {
                    try {
                        response.sendRedirect(urlPathHelper.getOriginatingContextPath(request) + loginUrl + "?returnUrl="
                                + getURL(request) + getEncodeQueryString(request.getQueryString()));
                    } catch (IllegalStateException e) {
                        log.error(e.getMessage());
                    } catch (IOException e) {
                        log.error(e.getMessage());
                    }
                    return false;
                } else if (null != unauthorizedUrl) {
                    String path = urlPathHelper.getLookupPathForRequest(request);
                    if (isNotBlank(path) && !SEPARATOR.equals(path)) {
                        int index = path.lastIndexOf(".");
                        path = path.substring(path.indexOf(SEPARATOR) > 0 ? 0 : 1, index > -1 ? index : path.length());
                        if (0 == roleAuthorizedService.count(user.getRoles(), path) && !ownsAllRight(user.getRoles())) {
                            try {
                                response.sendRedirect(urlPathHelper.getOriginatingContextPath(request) + unauthorizedUrl);
                                return false;
                            } catch (IOException e) {
                                log.error(e.getMessage());
                            }
                        }
                    }
                }
            }
        }
        return true;
    }

    private boolean ownsAllRight(String roles) {
        String[] roleIdArray = split(roles, ",");
        Integer[] roleIds = null;
        if (null != roles && 0 < roleIdArray.length) {
            roleIds = new Integer[roleIdArray.length];
            for (int i = 0; i < roleIdArray.length; i++) {
                roleIds[i] = Integer.parseInt(roleIdArray[i]);
            }
            return systemRoleService.ownsAllRight(roleIds);
        }
        return false;
    }

    private boolean verify(String url) {
        if (null == loginUrl) {
            return false;
        } else if (null != needNotLoginUrls && null != url) {
            for (String needNotLoginUrl : needNotLoginUrls) {
                if (null != needNotLoginUrl) {
                    if (url.startsWith(needNotLoginUrl)) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    public void setLoginUrl(String loginUrl) {
        this.loginUrl = loginUrl;
    }

    public void setNeedNotLoginUrls(String[] needNotLoginUrls) {
        this.needNotLoginUrls = needNotLoginUrls;
    }

    public void setLoginJsonUrl(String loginJsonUrl) {
        this.loginJsonUrl = loginJsonUrl;
    }

    public void setUnauthorizedUrl(String unauthorizedUrl) {
        this.unauthorizedUrl = unauthorizedUrl;
    }
}

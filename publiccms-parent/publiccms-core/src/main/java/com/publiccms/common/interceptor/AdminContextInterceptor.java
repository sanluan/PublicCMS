package com.publiccms.common.interceptor;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.publiccms.common.constants.CommonConstants;
import com.publiccms.common.tools.ControllerUtils;
import com.publiccms.common.tools.RequestUtils;
import com.publiccms.entities.sys.SysSite;
import com.publiccms.entities.sys.SysUser;
import com.publiccms.logic.component.site.SiteComponent;
import com.publiccms.logic.service.log.LogLoginService;
import com.publiccms.logic.service.sys.SysRoleAuthorizedService;
import com.publiccms.logic.service.sys.SysRoleService;
import com.publiccms.logic.service.sys.SysUserService;

/**
 *
 * AdminContextInterceptor
 *
 */
public class AdminContextInterceptor extends WebContextInterceptor {
    private String adminContextPath;
    private String loginUrl;
    private String loginJsonUrl;
    private String unauthorizedUrl;
    private String[] needNotLoginUrls;
    private String[] needNotAuthorizedUrls;

    @Autowired
    private SysRoleAuthorizedService roleAuthorizedService;
    @Autowired
    private SysRoleService sysRoleService;
    @Autowired
    private SysUserService sysUserService;
    @Autowired
    private SiteComponent siteComponent;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        SysSite site = siteComponent.getSite(request.getServerName());
        request.setAttribute("site", site);
        String path = urlPathHelper.getLookupPathForRequest(request);
        String ctxPath = urlPathHelper.getOriginatingContextPath(request);
        if (adminContextPath.equals(path)) {
            try {
                StringBuilder sb = new StringBuilder(ctxPath);
                sb.append(adminContextPath).append(CommonConstants.SEPARATOR);
                response.sendRedirect(sb.toString());
                return false;
            } catch (IOException e) {
                return true;
            }
        } else if (verifyNeedLogin(path)) {
            HttpSession session = request.getSession();
            SysUser user = initUser(ControllerUtils.getAdminFromSession(session), LogLoginService.CHANNEL_WEB_MANAGER,
                    CommonConstants.getCookiesAdmin(), site, request, response);
            if (null == user) {
                ControllerUtils.clearAdminToSession(request.getContextPath(), session, response);
                try {
                    redirectLogin(ctxPath, path, request.getQueryString(), request.getHeader("X-Requested-With"), response);
                    return false;
                } catch (IllegalStateException | IOException e) {
                    return true;
                }
            }
            SysUser entity = sysUserService.getEntity(user.getId());
            if (null == entity || entity.isDisabled() || !entity.isSuperuserAccess() || null == site || site.isDisabled()
                    || site.getId() != entity.getSiteId()) {
                ControllerUtils.clearAdminToSession(request.getContextPath(), session, response);
                try {
                    redirectLogin(ctxPath, path, request.getQueryString(), request.getHeader("X-Requested-With"), response);
                    return false;
                } catch (IllegalStateException | IOException e) {
                    return true;
                }
            } else if (verifyNeedAuthorized(path)) {
                if (!CommonConstants.SEPARATOR.equals(path)) {
                    int index = path.lastIndexOf(CommonConstants.DOT);
                    path = path.substring(0 < path.indexOf(CommonConstants.SEPARATOR) ? 0 : 1,
                            index > -1 ? index : path.length());
                    if (0 == roleAuthorizedService.count(entity.getRoles(), path) && !ownsAllRight(entity.getRoles())) {
                        try {
                            StringBuilder sb = new StringBuilder(ctxPath);
                            sb.append(adminContextPath).append(unauthorizedUrl);
                            response.sendRedirect(sb.toString());
                            return false;
                        } catch (IOException e) {
                            return true;
                        }
                    }
                }
            }
            entity.setPassword(null);
            ControllerUtils.setAdminToSession(session, entity);
        }
        return true;
    }

    private void redirectLogin(String ctxPath, String path, String queryString, String requestedWith,
            HttpServletResponse response) throws IOException {
        if ("XMLHttpRequest".equalsIgnoreCase(requestedWith)) {
            StringBuilder sb = new StringBuilder(ctxPath);
            sb.append(adminContextPath).append(loginJsonUrl);
            response.sendRedirect(sb.toString());
        } else {
            StringBuilder sb = new StringBuilder(ctxPath);
            sb.append(adminContextPath).append(loginUrl).append("?returnUrl=");
            sb.append(RequestUtils.getEncodePath(adminContextPath + path, queryString));
            response.sendRedirect(sb.toString());
        }
    }

    private boolean ownsAllRight(String roles) {
        String[] roleIdArray = StringUtils.split(roles, CommonConstants.COMMA_DELIMITED);
        if (null != roles && 0 < roleIdArray.length) {
            Integer[] roleIds = new Integer[roleIdArray.length];
            for (int i = 0; i < roleIdArray.length; i++) {
                roleIds[i] = Integer.parseInt(roleIdArray[i]);
            }
            return sysRoleService.ownsAllRight(roleIds);
        }
        return false;
    }

    private boolean verifyNeedLogin(String url) {
        if (null == loginUrl) {
            return false;
        } else if (null != needNotLoginUrls && null != url) {
            if (url.startsWith(loginUrl) || (null != loginJsonUrl && url.startsWith(loginJsonUrl))) {
                return false;
            }
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

    private boolean verifyNeedAuthorized(String url) {
        if (null == unauthorizedUrl) {
            return false;
        } else if (null != needNotAuthorizedUrls && null != url) {
            if (url.startsWith(unauthorizedUrl)) {
                return false;
            }
            for (String needNotAuthorizedUrl : needNotAuthorizedUrls) {
                if (null != needNotAuthorizedUrl) {
                    if (url.startsWith(needNotAuthorizedUrl)) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    /**
     * @param adminContextPath
     *            the adminContextPath to set
     */
    public void setAdminContextPath(String adminContextPath) {
        this.adminContextPath = adminContextPath;
    }

    /**
     * @param loginUrl
     */
    public void setLoginUrl(String loginUrl) {
        this.loginUrl = loginUrl;
    }

    /**
     * @param loginJsonUrl
     */
    public void setLoginJsonUrl(String loginJsonUrl) {
        this.loginJsonUrl = loginJsonUrl;
    }

    /**
     * @param unauthorizedUrl
     */
    public void setUnauthorizedUrl(String unauthorizedUrl) {
        this.unauthorizedUrl = unauthorizedUrl;
    }

    /**
     * @param needNotLoginUrls
     */
    public void setNeedNotLoginUrls(String[] needNotLoginUrls) {
        this.needNotLoginUrls = needNotLoginUrls;
    }

    /**
     * @param needNotAuthorizedUrls
     */
    public void setNeedNotAuthorizedUrls(String[] needNotAuthorizedUrls) {
        this.needNotAuthorizedUrls = needNotAuthorizedUrls;
    }
}

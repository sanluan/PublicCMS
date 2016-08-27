package com.publiccms.common.interceptor.admin;

import static com.publiccms.common.base.AbstractController.getAdminFromSession;
import static com.publiccms.common.base.AbstractController.setAdminToSession;
import static com.publiccms.common.constants.CmsVersion.getVersion;
import static com.publiccms.common.constants.CommonConstants.getXPowered;
import static com.sanluan.common.tools.RequestUtils.getEncodePath;
import static org.apache.commons.lang3.StringUtils.split;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;

import com.publiccms.entities.sys.SysUser;
import com.publiccms.logic.service.sys.SysRoleAuthorizedService;
import com.publiccms.logic.service.sys.SysRoleService;
import com.publiccms.logic.service.sys.SysUserService;
import com.sanluan.common.base.BaseInterceptor;

import config.initializer.AdminInitializer;

public class AdminContextInterceptor extends BaseInterceptor {
    private static final String SEPARATOR = "/";

    private String[] needNotLoginUrls;
    private String[] needNotAuthorizedUrls;
    private String loginUrl;
    private String loginJsonUrl;
    private String unauthorizedUrl;
    @Autowired
    private SysRoleAuthorizedService roleAuthorizedService;
    @Autowired
    private SysRoleService sysRoleService;
    @Autowired
    private SysUserService sysUserService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws ServletException {
        response.addHeader(getXPowered(), getVersion());
        String path = urlPathHelper.getLookupPathForRequest(request);
        String ctxPath = urlPathHelper.getOriginatingContextPath(request);
        if (AdminInitializer.BASEPATH.equals(path)) {
            try {
                response.sendRedirect(ctxPath + AdminInitializer.BASEPATH + SEPARATOR);
                return false;
            } catch (IOException e) {
                return true;
            }
        } else if (verifyNeedLogin(path)) {
            SysUser user = getAdminFromSession(request.getSession());
            if (null == user) {
                try {
                    redirectLogin(ctxPath, path, request.getQueryString(), request.getHeader("X-Requested-With"), response);
                    return false;
                } catch (IllegalStateException | IOException e) {
                    return true;
                }
            }
            SysUser entity = sysUserService.getEntity(user.getId());
            if (!entity.isDisabled() && !entity.isSuperuserAccess()) {
                try {
                    redirectLogin(ctxPath, path, request.getQueryString(), request.getHeader("X-Requested-With"), response);
                    return false;
                } catch (IllegalStateException | IOException e) {
                    return true;
                }
            } else if (verifyNeedAuthorized(path)) {
                if (!SEPARATOR.equals(path)) {
                    int index = path.lastIndexOf(".");
                    path = path.substring(path.indexOf(SEPARATOR) > 0 ? 0 : 1, index > -1 ? index : path.length());
                    if (0 == roleAuthorizedService.count(entity.getRoles(), path) && !ownsAllRight(entity.getRoles())) {
                        try {
                            response.sendRedirect(ctxPath + unauthorizedUrl);
                            return false;
                        } catch (IOException e) {
                            return true;
                        }
                    }
                    user.setName(entity.getName());
                    user.setNickName(entity.getNickName());
                    user.setRoles(entity.getRoles());
                    user.setDeptId(entity.getDeptId());
                    setAdminToSession(request.getSession(), user);
                }
            }
        }
        return true;
    }

    private void redirectLogin(String ctxPath, String path, String queryString, String requestedWith,
            HttpServletResponse response) throws IOException {
        if ("XMLHttpRequest".equalsIgnoreCase(requestedWith)) {
            response.sendRedirect(ctxPath + loginJsonUrl);
        } else {
            response.sendRedirect(
                    ctxPath + loginUrl + "?returnUrl=" + getEncodePath(AdminInitializer.BASEPATH + path, queryString));
        }
    }

    private boolean ownsAllRight(String roles) {
        String[] roleIdArray = split(roles, ",");
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

    public void setLoginUrl(String loginUrl) {
        this.loginUrl = AdminInitializer.BASEPATH + loginUrl;
    }

    public void setNeedNotLoginUrls(String[] needNotLoginUrls) {
        this.needNotLoginUrls = needNotLoginUrls;
    }

    public void setLoginJsonUrl(String loginJsonUrl) {
        this.loginJsonUrl = AdminInitializer.BASEPATH + loginJsonUrl;
    }

    public void setUnauthorizedUrl(String unauthorizedUrl) {
        this.unauthorizedUrl = AdminInitializer.BASEPATH + unauthorizedUrl;
    }

    public void setNeedNotAuthorizedUrls(String[] needNotAuthorizedUrls) {
        this.needNotAuthorizedUrls = needNotAuthorizedUrls;
    }
}

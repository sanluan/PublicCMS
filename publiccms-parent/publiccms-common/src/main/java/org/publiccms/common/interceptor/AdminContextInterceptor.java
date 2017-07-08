package org.publiccms.common.interceptor;

import static com.publiccms.common.tools.RequestUtils.getEncodePath;
import static org.apache.commons.lang3.StringUtils.split;
import static org.publiccms.common.base.AbstractController.getAdminFromSession;
import static org.publiccms.common.base.AbstractController.setAdminToSession;
import static org.publiccms.common.constants.CmsVersion.getVersion;
import static org.publiccms.common.constants.CommonConstants.ADMIN_BASE_PATH;
import static org.publiccms.common.constants.CommonConstants.getXPowered;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.publiccms.entities.sys.SysUser;
import org.publiccms.logic.service.sys.SysRoleAuthorizedService;
import org.publiccms.logic.service.sys.SysRoleService;
import org.publiccms.logic.service.sys.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;

import com.publiccms.common.base.Base;
import com.publiccms.common.base.BaseInterceptor;

/**
 *
 * AdminContextInterceptor
 *
 */
public class AdminContextInterceptor extends BaseInterceptor implements Base {
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
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        response.addHeader(getXPowered(), getVersion());
        String path = urlPathHelper.getLookupPathForRequest(request);
        String ctxPath = urlPathHelper.getOriginatingContextPath(request);
        if (ADMIN_BASE_PATH.equals(path)) {
            try {
                response.sendRedirect(ctxPath + ADMIN_BASE_PATH + SEPARATOR);
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
                    int index = path.lastIndexOf(DOT);
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
            StringBuilder sb = new StringBuilder(ctxPath);
            sb.append(loginUrl);
            sb.append("?returnUrl=");
            sb.append(getEncodePath(ADMIN_BASE_PATH + path, queryString));
            response.sendRedirect(sb.toString());
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

    /**
     * @param loginUrl
     */
    public void setLoginUrl(String loginUrl) {
        this.loginUrl = ADMIN_BASE_PATH + loginUrl;
    }

    /**
     * @param needNotLoginUrls
     */
    public void setNeedNotLoginUrls(String[] needNotLoginUrls) {
        this.needNotLoginUrls = needNotLoginUrls;
    }

    /**
     * @param loginJsonUrl
     */
    public void setLoginJsonUrl(String loginJsonUrl) {
        this.loginJsonUrl = ADMIN_BASE_PATH + loginJsonUrl;
    }

    /**
     * @param unauthorizedUrl
     */
    public void setUnauthorizedUrl(String unauthorizedUrl) {
        this.unauthorizedUrl = ADMIN_BASE_PATH + unauthorizedUrl;
    }

    /**
     * @param needNotAuthorizedUrls
     */
    public void setNeedNotAuthorizedUrls(String[] needNotAuthorizedUrls) {
        this.needNotAuthorizedUrls = needNotAuthorizedUrls;
    }
}

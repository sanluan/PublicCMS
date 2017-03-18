package com.publiccms.common.base;

import static org.apache.commons.lang3.ArrayUtils.contains;
import static org.apache.commons.lang3.StringUtils.split;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;

import com.publiccms.entities.sys.SysApp;
import com.publiccms.entities.sys.SysAppToken;
import com.publiccms.entities.sys.SysSite;
import com.publiccms.entities.sys.SysUser;
import com.publiccms.entities.sys.SysUserToken;
import com.publiccms.logic.component.site.SiteComponent;
import com.publiccms.logic.service.sys.SysAppService;
import com.publiccms.logic.service.sys.SysAppTokenService;
import com.publiccms.logic.service.sys.SysUserService;
import com.publiccms.logic.service.sys.SysUserTokenService;
import com.sanluan.common.directive.BaseHttpDirective;
import com.sanluan.common.handler.RenderHandler;

/**
 * 
 * BaseDirective 自定义接口指令基类
 *
 */
public abstract class AbstractAppDirective extends BaseHttpDirective {
    public SysSite getSite(RenderHandler handler) throws IOException, Exception {
        HttpServletRequest request = handler.getRequest();
        return siteComponent.getSite(request.getServerName());
    }

    @Override
    public void execute(RenderHandler handler) throws IOException, Exception {
        SysApp app = null;
        SysUser user = null;
        if (needAppToken() && (null == (app = getApp(handler)) || empty(app.getAuthorizedApis())
                || !contains(split(app.getAuthorizedApis(), COMMA_DELIMITED), getName()))) {
            if (null == app) {
                handler.put("error", "needAppToken").render();
            } else {
                handler.put("error", "unAuthorized").render();
            }
        } else if (needUserToken() && null == (user = getUser(handler))) {
            handler.put("error", "needLogin").render();
        } else {
            execute(handler, app, user);
            handler.render();
        }
    }

    protected SysApp getApp(RenderHandler handler) throws Exception {
        SysAppToken appToken = appTokenService.getEntity(handler.getString("appToken"));
        if (null != appToken) {
            return appService.getEntity(appToken.getAppId());
        }
        return null;
    }

    protected SysUser getUser(RenderHandler handler) throws Exception {
        String authToken = handler.getString("authToken");
        Long authUserId = handler.getLong("authUserId");
        if (notEmpty(authToken) && null != authUserId) {
            SysUserToken sysUserToken = sysUserTokenService.getEntity(authToken);
            if (null != sysUserToken && sysUserToken.getUserId() == authUserId) {
                return sysUserService.getEntity(sysUserToken.getUserId());
            }
        }
        return null;
    }

    public abstract void execute(RenderHandler handler, SysApp app, SysUser user) throws IOException, Exception;

    public abstract boolean needAppToken();

    public abstract boolean needUserToken();

    @Autowired
    private SysUserTokenService sysUserTokenService;
    @Autowired
    private SysUserService sysUserService;
    @Autowired
    private SysAppTokenService appTokenService;
    @Autowired
    private SysAppService appService;
    @Autowired
    private SiteComponent siteComponent;
}

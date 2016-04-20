package com.publiccms.common.base;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;

import com.publiccms.common.base.app.AppV1;
import com.publiccms.entities.sys.SysApp;
import com.publiccms.entities.sys.SysAppToken;
import com.publiccms.entities.sys.SysUser;
import com.publiccms.entities.sys.SysUserToken;
import com.publiccms.logic.service.sys.SysAppService;
import com.publiccms.logic.service.sys.SysAppTokenService;
import com.publiccms.logic.service.sys.SysUserService;
import com.publiccms.logic.service.sys.SysUserTokenService;
import com.sanluan.common.handler.RenderHandler;

/**
 * 
 * BaseDirective 自定义接口指令基类
 *
 */
public abstract class AbstractAppV1Directive extends AbstractAppDirective implements AppV1 {
    private SysUser getUser(RenderHandler handler, SysApp app) throws Exception {
        String authToken = handler.getString("authToken");
        Integer authUserId = handler.getInteger("authUserId");
        if (notEmpty(authToken) && notEmpty(authUserId)) {
            SysUserToken sysUserToken = sysUserTokenService.getEntity(authToken);
            if (notEmpty(sysUserToken) && sysUserToken.getUserId() == authUserId
                    && sysUserToken.getChannel().equals(app.getChannel())) {
                return sysUserService.getEntity(sysUserToken.getUserId());
            }
        }
        return null;
    }

    private SysApp getApp(RenderHandler handler) throws Exception {
        SysAppToken appToken = appTokenService.getEntity(handler.getString("appToken"));
        if (notEmpty(appToken)) {
            return appService.getEntity(appToken.getAppId());
        }
        return null;
    }

    @Override
    public void execute(RenderHandler handler) throws IOException, Exception {
        SysApp app = null;
        SysUser user = null;
        if ((needApp() || needUser()) && empty((app = getApp(handler)))) {
            handler.put("error", "needAppToken").render();
        } else if (needUser() && empty((user = getUser(handler, app)))) {
            handler.put("error", "needLogin").render();
        } else {
            execute(handler, app, user);
        }
    }

    public abstract void execute(RenderHandler handler, SysApp app, SysUser user) throws IOException, Exception;

    public abstract boolean needUser();

    public abstract boolean needApp();

    @Override
    public String getVersion() {
        return VERSION;
    }

    @Autowired
    private SysAppTokenService appTokenService;
    @Autowired
    private SysAppService appService;
    @Autowired
    private SysUserTokenService sysUserTokenService;
    @Autowired
    private SysUserService sysUserService;
}

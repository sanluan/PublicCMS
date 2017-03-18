package com.publiccms.views.directive.api;

import static com.sanluan.common.tools.RequestUtils.getIpAddress;

import java.io.IOException;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.publiccms.common.base.AbstractAppDirective;
import com.publiccms.entities.log.LogLogin;
import com.publiccms.entities.sys.SysApp;
import com.publiccms.entities.sys.SysAppClient;
import com.publiccms.entities.sys.SysAppClientId;
import com.publiccms.entities.sys.SysSite;
import com.publiccms.entities.sys.SysUser;
import com.publiccms.entities.sys.SysUserToken;
import com.publiccms.logic.service.log.LogLoginService;
import com.publiccms.logic.service.sys.SysAppClientService;
import com.publiccms.logic.service.sys.SysUserService;
import com.publiccms.logic.service.sys.SysUserTokenService;
import com.sanluan.common.handler.RenderHandler;

@Component
public class AutoLoginDirective extends AbstractAppDirective {

    @Override
    public void execute(RenderHandler handler, SysApp app, SysUser user) throws IOException, Exception {
        String uuid = handler.getString("uuid");
        boolean result = false;
        if (notEmpty(uuid)) {
            SysSite site = getSite(handler);
            SysAppClientId sysAppClientId = new SysAppClientId(site.getId(), app.getChannel(), uuid);
            SysAppClient appClient = appClientService.getEntity(sysAppClientId);
            if (null != appClient && notEmpty(appClient.getUserId())) {
                user = service.getEntity(appClient.getUserId());
                if (null != user) {
                    String authToken = UUID.randomUUID().toString();
                    String ip = getIpAddress(handler.getRequest());
                    sysUserTokenService
                            .save(new SysUserToken(authToken, site.getId(), user.getId(), app.getChannel(), getDate(), ip));
                    service.updateLoginStatus(user.getId(), ip);
                    logLoginService
                            .save(new LogLogin(site.getId(), uuid, user.getId(), app.getChannel(), ip, true, getDate(), ip));
                    user.setPassword(null);
                    result = true;
                    handler.put("authToken", authToken).put("user", user);
                }
            }
        }
        handler.put("result", result);
    }

    @Autowired
    private SysAppClientService appClientService;
    @Autowired
    private SysUserService service;
    @Autowired
    private SysUserTokenService sysUserTokenService;
    @Autowired
    private LogLoginService logLoginService;

    @Override
    public boolean needUserToken() {
        return false;
    }

    @Override
    public boolean needAppToken() {
        return true;
    }
}
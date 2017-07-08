package org.publiccms.views.directive.api;

import static com.publiccms.common.tools.CommonUtils.getDate;
import static com.publiccms.common.tools.CommonUtils.notEmpty;
import static com.publiccms.common.tools.RequestUtils.getIpAddress;

import java.io.IOException;
import java.util.UUID;

import org.publiccms.common.base.AbstractAppDirective;
import org.publiccms.entities.log.LogLogin;
import org.publiccms.entities.sys.SysApp;
import org.publiccms.entities.sys.SysAppClient;
import org.publiccms.entities.sys.SysAppClientId;
import org.publiccms.entities.sys.SysSite;
import org.publiccms.entities.sys.SysUser;
import org.publiccms.entities.sys.SysUserToken;
import org.publiccms.logic.service.log.LogLoginService;
import org.publiccms.logic.service.sys.SysAppClientService;
import org.publiccms.logic.service.sys.SysUserService;
import org.publiccms.logic.service.sys.SysUserTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.publiccms.common.handler.RenderHandler;

/**
 *
 * AutoLoginDirective
 * 
 */
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
                            .save(new LogLogin(site.getId(), uuid, user.getId(), ip, app.getChannel(), true, getDate(), null));
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
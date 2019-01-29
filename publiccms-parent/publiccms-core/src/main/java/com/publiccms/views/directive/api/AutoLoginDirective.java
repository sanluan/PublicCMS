package com.publiccms.views.directive.api;

import java.io.IOException;
import java.util.Date;
import java.util.UUID;

import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.publiccms.common.base.AbstractAppDirective;
import com.publiccms.common.handler.RenderHandler;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.common.tools.RequestUtils;
import com.publiccms.entities.log.LogLogin;
import com.publiccms.entities.sys.SysApp;
import com.publiccms.entities.sys.SysAppClient;
import com.publiccms.entities.sys.SysSite;
import com.publiccms.entities.sys.SysUser;
import com.publiccms.entities.sys.SysUserToken;
import com.publiccms.logic.service.log.LogLoginService;
import com.publiccms.logic.service.sys.SysAppClientService;
import com.publiccms.logic.service.sys.SysUserService;
import com.publiccms.logic.service.sys.SysUserTokenService;

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
        if (CommonUtils.notEmpty(uuid)) {
            SysSite site = getSite(handler);
            SysAppClient appClient = appClientService.getEntity(site.getId(), app.getChannel(), uuid);
            if (null != appClient && null != appClient.getUserId()) {
                user = service.getEntity(appClient.getUserId());
                if (null != user && !user.isDisabled()) {
                    String authToken = UUID.randomUUID().toString();
                    String ip = RequestUtils.getIpAddress(handler.getRequest());
                    Date now = CommonUtils.getDate();
                    sysUserTokenService.save(new SysUserToken(authToken, site.getId(), user.getId(), app.getChannel(), now,
                            DateUtils.addDays(now, 30), ip));
                    service.updateLoginStatus(user.getId(), ip);
                    logLoginService.save(new LogLogin(site.getId(), uuid, user.getId(), ip, app.getChannel(), true, now, null));
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
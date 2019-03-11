package com.publiccms.views.directive.api;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.publiccms.common.base.AbstractAppDirective;
import com.publiccms.common.handler.RenderHandler;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.common.tools.RequestUtils;
import com.publiccms.entities.sys.SysApp;
import com.publiccms.entities.sys.SysAppClient;
import com.publiccms.entities.sys.SysSite;
import com.publiccms.entities.sys.SysUser;
import com.publiccms.logic.service.sys.SysAppClientService;

/**
 *
 * AppClientDirective
 * 
 */
@Component
public class AppClientDirective extends AbstractAppDirective {

    @Override
    public void execute(RenderHandler handler, SysApp app, SysUser user) throws IOException, Exception {
        String uuid = handler.getString("uuid");
        String clientVersion = handler.getString("clientVersion");
        if (CommonUtils.notEmpty(uuid)) {
            SysSite site = getSite(handler);
            SysAppClient appClient = appClientService.getEntity(site.getId(), app.getChannel(), uuid);
            if (null == appClient) {
                appClient = new SysAppClient(site.getId(), app.getChannel(), uuid, CommonUtils.getDate(), false);
                appClient.setClientVersion(clientVersion);
                appClient.setLastLoginIp(RequestUtils.getIpAddress(handler.getRequest()));
                appClientService.save(appClient);
            } else {
                appClientService.updateLastLogin(appClient.getId(), clientVersion,
                        RequestUtils.getIpAddress(handler.getRequest()));
            }
        }
    }

    @Autowired
    private SysAppClientService appClientService;

    @Override
    public boolean needUserToken() {
        return false;
    }

    @Override
    public boolean needAppToken() {
        return true;
    }
}
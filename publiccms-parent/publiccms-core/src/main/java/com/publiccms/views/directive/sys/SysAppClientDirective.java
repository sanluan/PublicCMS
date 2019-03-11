package com.publiccms.views.directive.sys;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.publiccms.common.base.AbstractTemplateDirective;
import com.publiccms.common.handler.RenderHandler;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.common.tools.RequestUtils;
import com.publiccms.entities.sys.SysAppClient;
import com.publiccms.entities.sys.SysSite;
import com.publiccms.logic.service.sys.SysAppClientService;

/**
 *
 * CreateAppClientDirective
 * 
 */
@Component
public class SysAppClientDirective extends AbstractTemplateDirective {

    @Override
    public void execute(RenderHandler handler) throws IOException, Exception {
        String uuid = handler.getString("uuid");
        String channel = handler.getString("channel");
        String clientVersion = handler.getString("clientVersion");
        if (CommonUtils.notEmpty(uuid) && CommonUtils.notEmpty(channel)) {
            SysSite site = getSite(handler);
            SysAppClient appClient = appClientService.getEntity(site.getId(), channel, uuid);
            String ip = RequestUtils.getIpAddress(handler.getRequest());
            if (null == appClient) {
                appClient = new SysAppClient(site.getId(), channel, uuid, CommonUtils.getDate(), false);
                appClient.setClientVersion(uuid);
                appClient.setLastLoginIp(ip);
                appClientService.save(appClient);
            } else {
                appClientService.updateLastLogin(appClient.getId(), clientVersion, ip);
            }
            handler.render();
        }
    }

    @Autowired
    private SysAppClientService appClientService;

    @Override
    public boolean needAppToken() {
        return true;
    }
}
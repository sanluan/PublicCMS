package org.publiccms.views.directive.api;

//Generated 2015-5-10 17:54:56 by com.publiccms.common.source.SourceGenerator
import static com.publiccms.common.tools.CommonUtils.getDate;
import static com.publiccms.common.tools.CommonUtils.notEmpty;
import static com.publiccms.common.tools.RequestUtils.getIpAddress;

import java.io.IOException;

import org.publiccms.common.base.AbstractAppDirective;
import org.publiccms.entities.sys.SysApp;
import org.publiccms.entities.sys.SysAppClient;
import org.publiccms.entities.sys.SysAppClientId;
import org.publiccms.entities.sys.SysUser;
import org.publiccms.logic.service.sys.SysAppClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.publiccms.common.handler.RenderHandler;

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
        if (notEmpty(uuid)) {
            SysAppClientId sysAppClientId = new SysAppClientId(getSite(handler).getId(), app.getChannel(), uuid);
            SysAppClient appClient = appClientService.getEntity(sysAppClientId);
            if (null == appClient) {
                appClient = new SysAppClient(sysAppClientId, getDate(), false);
                appClient.setClientVersion(clientVersion);
                appClient.setLastLoginIp(getIpAddress(handler.getRequest()));
                appClientService.save(appClient);
            } else {
                appClientService.updateLastLogin(sysAppClientId, clientVersion, getIpAddress(handler.getRequest()));
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
package com.publiccms.views.directive.api;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.publiccms.common.base.AbstractAppDirective;
import com.publiccms.common.handler.RenderHandler;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.entities.sys.SysApp;
import com.publiccms.entities.sys.SysAppClientId;
import com.publiccms.entities.sys.SysUser;
import com.publiccms.logic.service.sys.SysAppClientService;

/**
 *
 * BindingUserDirective
 * 
 */
@Component
public class BindingUserDirective extends AbstractAppDirective {

    @Override
    public void execute(RenderHandler handler, SysApp app, SysUser user) throws IOException, Exception {
        String uuid = handler.getString("uuid");
        boolean result = false;
        if (CommonUtils.notEmpty(uuid)) {
            SysAppClientId sysAppClientId = new SysAppClientId(getSite(handler).getId(), app.getChannel(), uuid);
            appClientService.updateUser(sysAppClientId, user.getId());
            result = true;
        }
        handler.put("result", result);
    }

    @Autowired
    private SysAppClientService appClientService;

    @Override
    public boolean needUserToken() {
        return true;
    }

    @Override
    public boolean needAppToken() {
        return true;
    }
}
package org.publiccms.views.directive.api;

import static com.publiccms.common.tools.CommonUtils.notEmpty;

import java.io.IOException;

import org.publiccms.common.base.AbstractAppDirective;
import org.publiccms.entities.sys.SysApp;
import org.publiccms.entities.sys.SysAppClientId;
import org.publiccms.entities.sys.SysUser;
import org.publiccms.logic.service.sys.SysAppClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.publiccms.common.handler.RenderHandler;

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
        if (notEmpty(uuid)) {
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
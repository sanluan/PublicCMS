package com.publiccms.views.directive.api;

import java.io.IOException;
import java.util.Date;

import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.publiccms.common.base.AbstractAppDirective;
import com.publiccms.common.handler.RenderHandler;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.entities.sys.SysApp;
import com.publiccms.entities.sys.SysUser;
import com.publiccms.logic.service.sys.SysAppTokenService;

/**
 *
 * AppTokenDirective
 * 
 */
@Component
public class RefreshTokenDirective extends AbstractAppDirective {
    private final static String NEED_NOT_REFRESH = "needNotRefresh";

    @Override
    public void execute(RenderHandler handler, SysApp app, SysUser user) throws IOException, Exception {
        String appToken = handler.getString("appToken");
        if (null != app.getExpiryMinutes()) {
            Date now = CommonUtils.getDate();
            Date expiryDate = DateUtils.addMinutes(now, app.getExpiryMinutes());
            service.updateExpiryDate(appToken, expiryDate);
            handler.put("appToken", appToken);
            handler.put("expiryDate", expiryDate);
        } else {
            handler.put("error", NEED_NOT_REFRESH);
        }
    }

    @Autowired
    private SysAppTokenService service;

    @Override
    public boolean needUserToken() {
        return false;
    }

    @Override
    public boolean needAppToken() {
        return true;
    }
}
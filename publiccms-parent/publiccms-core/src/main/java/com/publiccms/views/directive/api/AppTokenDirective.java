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
import com.publiccms.entities.sys.SysApp;
import com.publiccms.entities.sys.SysAppToken;
import com.publiccms.entities.sys.SysUser;
import com.publiccms.logic.service.sys.SysAppService;
import com.publiccms.logic.service.sys.SysAppTokenService;

/**
 *
 * AppTokenDirective
 * 
 */
@Component
public class AppTokenDirective extends AbstractAppDirective {
    private final static String KEY_NOT_EXISTS = "keyNotExists";
    private final static String SECRET_ERROR = "secretError";

    @Override
    public void execute(RenderHandler handler, SysApp app, SysUser user) throws IOException, Exception {
        SysApp entity = appService.getEntity(handler.getString("appKey"));
        if (null != entity) {
            if (entity.getAppSecret().equalsIgnoreCase(handler.getString("appSecret"))) {
                Date now = CommonUtils.getDate();
                SysAppToken token = new SysAppToken(UUID.randomUUID().toString(), entity.getId(), now);
                if (null != entity.getExpiryMinutes()) {
                    token.setExpiryDate(DateUtils.addMinutes(now, entity.getExpiryMinutes()));
                }
                appTokenService.save(token);
                handler.put("appToken", token.getAuthToken());
                handler.put("expiryDate", token.getExpiryDate());
            } else {
                handler.put("error", SECRET_ERROR);
            }
        } else {
            handler.put("error", KEY_NOT_EXISTS);
        }
    }

    @Autowired
    private SysAppTokenService appTokenService;
    @Autowired
    private SysAppService appService;

    @Override
    public boolean needUserToken() {
        return false;
    }

    @Override
    public boolean needAppToken() {
        return false;
    }
}
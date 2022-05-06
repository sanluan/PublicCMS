package com.publiccms.views.directive.api;

import java.io.IOException;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.publiccms.common.api.Config;
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
import com.publiccms.logic.component.config.ConfigComponent;
import com.publiccms.logic.component.config.SiteConfigComponent;
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
        String username = handler.getString("username");
        String channel = handler.getString("channel", LogLoginService.CHANNEL_WEB);
        boolean result = false;
        if (CommonUtils.notEmpty(uuid) && CommonUtils.notEmpty(username)) {
            SysSite site = getSite(handler);
            SysAppClient appClient = appClientService.getEntity(site.getId(), channel, uuid);
            if (null != appClient && null != appClient.getUserId()) {
                user = service.getEntity(appClient.getUserId());
                if (null != user && !user.isDisabled() && username.equals(user.getName())) {
                    String authToken = UUID.randomUUID().toString();
                    String ip = RequestUtils.getIpAddress(handler.getRequest());
                    Date now = CommonUtils.getDate();
                    Map<String, String> config = configComponent.getConfigData(site.getId(), Config.CONFIG_CODE_SITE);
                    int expiryMinutes = ConfigComponent.getInt(config.get(SiteConfigComponent.CONFIG_EXPIRY_MINUTES_WEB),
                            SiteConfigComponent.DEFAULT_EXPIRY_MINUTES);
                    Date expiryDate = DateUtils.addMinutes(now, expiryMinutes);
                    sysUserTokenService
                            .save(new SysUserToken(authToken, site.getId(), user.getId(), channel, now, expiryDate, ip));
                    service.updateLoginStatus(user.getId(), ip);
                    logLoginService.save(new LogLogin(site.getId(), uuid, user.getId(), ip, channel, true, now, null));
                    user.setPassword(null);
                    result = true;
                    handler.put("authToken", authToken).put("expiryDate", expiryDate).put("user", user);
                }
            }
        }
        handler.put("result", result).render();
    }

    @Autowired
    private SysAppClientService appClientService;
    @Autowired
    private SysUserService service;
    @Autowired
    private SysUserTokenService sysUserTokenService;
    @Autowired
    private LogLoginService logLoginService;
    @Autowired
    private ConfigComponent configComponent;

    @Override
    public boolean needUserToken() {
        return false;
    }

    @Override
    public boolean needAppToken() {
        return false;
    }
}
package com.publiccms.views.directive.api;

import java.io.IOException;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.publiccms.common.api.Config;
import com.publiccms.common.base.AbstractAppDirective;
import com.publiccms.common.handler.RenderHandler;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.common.tools.ControllerUtils;
import com.publiccms.common.tools.RequestUtils;
import com.publiccms.common.tools.UserPasswordUtils;
import com.publiccms.entities.log.LogLogin;
import com.publiccms.entities.sys.SysApp;
import com.publiccms.entities.sys.SysSite;
import com.publiccms.entities.sys.SysUser;
import com.publiccms.entities.sys.SysUserToken;
import com.publiccms.logic.component.config.ConfigComponent;
import com.publiccms.logic.component.config.LoginConfigComponent;
import com.publiccms.logic.service.log.LogLoginService;
import com.publiccms.logic.service.sys.SysUserService;
import com.publiccms.logic.service.sys.SysUserTokenService;

/**
 *
 * LoginDirective
 * 
 */
@Component
public class LoginDirective extends AbstractAppDirective {

    @Override
    public void execute(RenderHandler handler, SysApp app, SysUser user) throws IOException, Exception {
        String username = StringUtils.trim(handler.getString("username"));
        String password = StringUtils.trim(handler.getString("password"));
        boolean result = false;
        if (CommonUtils.notEmpty(username) && CommonUtils.notEmpty(password)) {
            SysSite site = getSite(handler);
            if (ControllerUtils.verifyNotEMail(username)) {
                user = service.findByName(site.getId(), username);
            } else {
                user = service.findByEmail(site.getId(), username);
            }
            String ip = RequestUtils.getIpAddress(handler.getRequest());
            if (null != user && !user.isDisabled()
                    && user.getPassword().equals(UserPasswordUtils.passwordEncode(password, user.getSalt()))) {
                if (UserPasswordUtils.needUpdate(user.getSalt())) {
                    String salt = UserPasswordUtils.getSalt();
                    service.updatePassword(user.getId(), UserPasswordUtils.passwordEncode(password, salt), salt);
                }
                if (!user.isWeakPassword() && UserPasswordUtils.isWeek(username, password)) {
                    service.updateWeekPassword(user.getId(), true);
                }
                service.updateLoginStatus(user.getId(), ip);
                String authToken = UUID.randomUUID().toString();
                Date now = CommonUtils.getDate();
                Map<String, String> config = configComponent.getConfigData(site.getId(), Config.CONFIG_CODE_SITE);
                int expiryMinutes = ConfigComponent.getInt(config.get(LoginConfigComponent.CONFIG_EXPIRY_MINUTES_WEB),
                        LoginConfigComponent.DEFAULT_EXPIRY_MINUTES);
                Date expiryDate = DateUtils.addMinutes(now, expiryMinutes);
                sysUserTokenService
                        .save(new SysUserToken(authToken, site.getId(), user.getId(), app.getChannel(), now, expiryDate, ip));
                logLoginService.save(new LogLogin(site.getId(), username, user.getId(), ip, app.getChannel(), true,
                        CommonUtils.getDate(), null));
                user.setPassword(null);
                result = true;
                handler.put("authToken", authToken).put("expiryDate", expiryDate).put("user", user);
            } else {
                LogLogin log = new LogLogin();
                log.setSiteId(site.getId());
                log.setName(username);
                log.setErrorPassword(password);
                log.setIp(ip);
                log.setChannel(app.getChannel());
                logLoginService.save(log);
            }
        }
        handler.put("result", result);
    }

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
        return true;
    }
}
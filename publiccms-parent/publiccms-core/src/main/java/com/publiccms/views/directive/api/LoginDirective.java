package com.publiccms.views.directive.api;

import java.io.IOException;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import jakarta.annotation.Resource;
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
import com.publiccms.logic.component.config.SiteConfigComponent;
import com.publiccms.logic.component.site.LockComponent;
import com.publiccms.logic.service.log.LogLoginService;
import com.publiccms.logic.service.sys.SysLockService;
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
        String encoding = StringUtils.trim(handler.getString("encoding"));
        boolean result = false;
        if (CommonUtils.notEmpty(username) && CommonUtils.notEmpty(password)) {
            SysSite site = getSite(handler);
            if (ControllerUtils.notEMail(username)) {
                user = service.findByName(site.getId(), username);
            } else {
                user = service.findByEmail(site.getId(), username);
            }
            String ip = RequestUtils.getIpAddress(handler.getRequest());
            boolean locked = lockComponent.isLocked(site.getId(), SysLockService.ITEM_TYPE_IP_LOGIN, ip, null);
            if (null != user && (!locked || !ControllerUtils.ipNotEquals(ip, user)) && !user.isDisabled()
                    && user.getPassword().equals(UserPasswordUtils.passwordEncode(password, user.getSalt(), encoding))) {
                lockComponent.unLock(site.getId(), SysLockService.ITEM_TYPE_IP_LOGIN, String.valueOf(user.getId()), null);
                lockComponent.unLock(site.getId(), SysLockService.ITEM_TYPE_LOGIN, String.valueOf(user.getId()), null);
                if (UserPasswordUtils.needUpdate(user.getSalt())) {
                    String salt = UserPasswordUtils.getSalt();
                    service.updatePassword(user.getId(), UserPasswordUtils.passwordEncode(password, salt, encoding), salt);
                }
                if (!user.isWeakPassword() && UserPasswordUtils.isWeek(username, password)) {
                    service.updateWeekPassword(user.getId(), true);
                }
                service.updateLoginStatus(user.getId(), ip);
                String authToken = UUID.randomUUID().toString();
                Date now = CommonUtils.getDate();
                Map<String, String> config = configComponent.getConfigData(site.getId(), Config.CONFIG_CODE_SITE);
                int expiryMinutes = ConfigComponent.getInt(config.get(SiteConfigComponent.CONFIG_EXPIRY_MINUTES_WEB),
                        SiteConfigComponent.DEFAULT_EXPIRY_MINUTES);
                Date expiryDate = DateUtils.addMinutes(now, expiryMinutes);
                sysUserTokenService
                        .save(new SysUserToken(authToken, site.getId(), user.getId(), app.getChannel(), now, expiryDate, ip));
                logLoginService.save(new LogLogin(site.getId(), username, user.getId(), ip, app.getChannel(), true,
                        CommonUtils.getDate(), null));
                user.setPassword(null);
                result = true;
                handler.put("authToken", authToken).put("expiryDate", expiryDate).put("user", user);
            } else {
                if (null != user) {
                    lockComponent.lock(site.getId(), SysLockService.ITEM_TYPE_LOGIN, String.valueOf(user.getId()), null, true);
                }
                lockComponent.lock(site.getId(), SysLockService.ITEM_TYPE_IP_LOGIN, ip, null, true);
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

    @Resource
    private SysUserService service;
    @Resource
    private SysUserTokenService sysUserTokenService;
    @Resource
    private LogLoginService logLoginService;
    @Resource
    private ConfigComponent configComponent;
    @Resource
    private LockComponent lockComponent;

    @Override
    public boolean needUserToken() {
        return false;
    }

    @Override
    public boolean needAppToken() {
        return true;
    }
}
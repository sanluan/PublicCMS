package org.publiccms.views.directive.api;

import static com.publiccms.common.tools.CommonUtils.getDate;
import static com.publiccms.common.tools.CommonUtils.notEmpty;
import static com.publiccms.common.tools.RequestUtils.getIpAddress;
import static com.publiccms.common.tools.VerificationUtils.encode;
import static org.apache.commons.lang3.StringUtils.trim;

import java.io.IOException;
import java.util.UUID;

import org.publiccms.common.base.AbstractAppDirective;
import org.publiccms.common.base.AbstractController;
import org.publiccms.entities.log.LogLogin;
import org.publiccms.entities.sys.SysApp;
import org.publiccms.entities.sys.SysSite;
import org.publiccms.entities.sys.SysUser;
import org.publiccms.entities.sys.SysUserToken;
import org.publiccms.logic.service.log.LogLoginService;
import org.publiccms.logic.service.sys.SysUserService;
import org.publiccms.logic.service.sys.SysUserTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.publiccms.common.handler.RenderHandler;

/**
 *
 * LoginDirective
 * 
 */
@Component
public class LoginDirective extends AbstractAppDirective {

    @Override
    public void execute(RenderHandler handler, SysApp app, SysUser user) throws IOException, Exception {
        String username = trim(handler.getString("username"));
        String password = trim(handler.getString("password"));
        boolean result = false;
        if (notEmpty(username) && notEmpty(password)) {
            SysSite site = getSite(handler);
            if (AbstractController.verifyNotEMail(username)) {
                user = service.findByName(site.getId(), username);
            } else {
                user = service.findByEmail(site.getId(), username);
            }
            String ip = getIpAddress(handler.getRequest());
            if (null != user && user.getPassword().equals(encode(password))) {
                String authToken = UUID.randomUUID().toString();
                sysUserTokenService
                        .save(new SysUserToken(authToken, site.getId(), user.getId(), app.getChannel(), getDate(), ip));
                service.updateLoginStatus(user.getId(), ip);
                logLoginService
                        .save(new LogLogin(site.getId(), username, user.getId(), ip, app.getChannel(), true, getDate(), null));
                user.setPassword(null);
                result = true;
                handler.put("authToken", authToken).put("user", user);
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

    @Override
    public boolean needUserToken() {
        return false;
    }

    @Override
    public boolean needAppToken() {
        return true;
    }
}
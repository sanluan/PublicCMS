package com.publiccms.views.directive.api;

import java.io.IOException;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang3.time.DateUtils;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

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
import com.publiccms.logic.component.config.ConfigDataComponent;
import com.publiccms.logic.component.config.SafeConfigComponent;
import com.publiccms.logic.service.log.LogLoginService;
import com.publiccms.logic.service.sys.SysAppClientService;
import com.publiccms.logic.service.sys.SysUserService;
import com.publiccms.logic.service.sys.SysUserTokenService;

import freemarker.template.TemplateException;

/**
*
* autoLogin 自动登录接口
* <p>
* 参数列表
* <ul>
* <li><code>uuid</code>:设备唯一id
* <li><code>username</code>:用户名
* <li><code>channel</code>:登陆渠道
* </ul>
* <p>
* 返回结果
* <ul>
* <li><code>result</code>:登录结果,【true,false】
* <li><code>authToken</code>:用户登录授权
* <li><code>expiryDate</code>:过期日期
* <li><code>user</code>:用户信息 {@link com.publiccms.entities.sys.SysUser}
* </ul>
* 使用示例
* <p>
* <pre>
&lt;script&gt;
$.getJSON('${site.dynamicPath}api/autoLogin?uuid=1&amp;username=admin&amp;channel=web', function(data){
    console.log(result+","+authToken+","+user.nickname+","+expiryDate);
});
&lt;/script&gt;
* </pre>
*/
@Component
public class AutoLoginDirective extends AbstractAppDirective {

    @Override
    public void execute(RenderHandler handler, SysApp app, SysUser user) throws IOException, TemplateException {
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
                    Map<String, String> config = configDataComponent.getConfigData(site.getId(), SafeConfigComponent.CONFIG_CODE);
                    int expiryMinutes = ConfigDataComponent.getInt(config.get(SafeConfigComponent.CONFIG_EXPIRY_MINUTES_WEB),
                            SafeConfigComponent.DEFAULT_EXPIRY_MINUTES);
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

    @Resource
    private SysAppClientService appClientService;
    @Resource
    private SysUserService service;
    @Resource
    private SysUserTokenService sysUserTokenService;
    @Resource
    private LogLoginService logLoginService;
    @Resource
    private ConfigDataComponent configDataComponent;

    @Override
    public boolean needUserToken() {
        return false;
    }

    @Override
    public boolean needAppToken() {
        return false;
    }
}
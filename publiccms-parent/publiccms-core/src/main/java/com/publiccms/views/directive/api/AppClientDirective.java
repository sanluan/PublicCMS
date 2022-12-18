package com.publiccms.views.directive.api;

import java.io.IOException;

import javax.annotation.Resource;
import org.springframework.stereotype.Component;

import com.publiccms.common.base.AbstractAppDirective;
import com.publiccms.common.handler.RenderHandler;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.common.tools.RequestUtils;
import com.publiccms.entities.sys.SysApp;
import com.publiccms.entities.sys.SysAppClient;
import com.publiccms.entities.sys.SysSite;
import com.publiccms.entities.sys.SysUser;
import com.publiccms.logic.service.sys.SysAppClientService;

/**
*
* appClient 客户端注册接口
* <p>
* 参数列表
* <ul>
* <li><code>uuid</code> 设备唯一id
* <li><code>clientVersion</code> 客户端版本
* </ul>
* <p>
* 使用示例
* <p>
* <pre>
&lt;script&gt;
$.getJSON('${site.dynamicPath}api/appClient?uuid=1&amp;clientVersion=1.0&amp;appToken=接口访问授权Token', function(data){
});
&lt;/script&gt;
* </pre>
*/
@Component
public class AppClientDirective extends AbstractAppDirective {

    @Override
    public void execute(RenderHandler handler, SysApp app, SysUser user) throws IOException, Exception {
        String uuid = handler.getString("uuid");
        String clientVersion = handler.getString("clientVersion");
        if (CommonUtils.notEmpty(uuid)) {
            SysSite site = getSite(handler);
            SysAppClient appClient = appClientService.getEntity(site.getId(), app.getChannel(), uuid);
            if (null == appClient) {
                appClient = new SysAppClient(site.getId(), app.getChannel(), uuid, CommonUtils.getDate(), false);
                appClient.setClientVersion(clientVersion);
                appClient.setLastLoginIp(RequestUtils.getIpAddress(handler.getRequest()));
                appClientService.save(appClient);
            } else {
                appClientService.updateLastLogin(appClient.getId(), clientVersion,
                        RequestUtils.getIpAddress(handler.getRequest()));
            }
        }
        handler.render();
    }

    @Resource
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
package com.publiccms.views.directive.api;

import java.io.IOException;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.publiccms.common.base.AbstractAppDirective;
import com.publiccms.common.handler.RenderHandler;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.entities.sys.SysApp;
import com.publiccms.entities.sys.SysAppClient;
import com.publiccms.entities.sys.SysUser;
import com.publiccms.logic.service.log.LogLoginService;
import com.publiccms.logic.service.sys.SysAppClientService;

import freemarker.template.TemplateException;

/**
*
* bindingUser 客户端绑定用户接口
* <p>
* 参数列表
* <ul>
* <li><code>uuid</code>:设备唯一id
* <li><code>channel</code>:客户端版本
* </ul>
* <p>
* 返回结果
* <ul>
* <li><code>result</code>:绑定结果
* </ul>
* 使用示例
* <p>
* <pre>
&lt;script&gt;
$.getJSON('${site.dynamicPath}api/bindingUser?uuid=1&amp;channel=web&amp;authToken=用户登录授权&amp;authUserId=1', function(data){
console.log(data.result);
});
&lt;/script&gt;
* </pre>
*/
@Component
public class BindingUserDirective extends AbstractAppDirective {

    @Override
    public void execute(RenderHandler handler, SysApp app, SysUser user) throws IOException, TemplateException {
        String uuid = handler.getString("uuid");
        String channel = handler.getString("channel", LogLoginService.CHANNEL_WEB);
        boolean result = false;
        if (CommonUtils.notEmpty(uuid)) {
            SysAppClient sysAppClien = appClientService.getEntity(getSite(handler).getId(), channel, uuid);
            if (null != sysAppClien) {
                appClientService.updateUser(sysAppClien.getId(), user.getId());
                result = true;
            }
        }
        handler.put("result", result).render();
    }

    @Resource
    private SysAppClientService appClientService;

    @Override
    public boolean needUserToken() {
        return true;
    }

    @Override
    public boolean needAppToken() {
        return false;
    }
}
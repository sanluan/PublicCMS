package com.publiccms.views.directive.api;

import java.io.IOException;
import java.util.Date;

import org.apache.commons.lang3.time.DateUtils;
import javax.annotation.Resource;
import org.springframework.stereotype.Component;

import com.publiccms.common.base.AbstractAppDirective;
import com.publiccms.common.handler.RenderHandler;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.entities.sys.SysApp;
import com.publiccms.entities.sys.SysUser;
import com.publiccms.logic.service.sys.SysAppTokenService;

/**
*
* refreshToken 刷新appToken接口
* <p>
* 参数列表
* <ul>
* <li><code>appToken</code> 设备唯一id
* </ul>
* <p>
* 返回结果
* <ul>
* <li><code>authToken</code> 用户登录授权
* <li><code>expiryDate</code> 过期日期
* <li><code>error</code> 错误信息【needNotRefresh】
* </ul>
* 使用示例
* <p>
* <pre>
&lt;script&gt;
$.getJSON('${site.dynamicPath}api/login?appToken=接口访问授权Token', function(data){
    console.log(appToken+","+expiryDate);
});
&lt;/script&gt;
* </pre>
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
        handler.render();
    }

    @Resource
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
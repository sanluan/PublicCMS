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
* appToken 接口访问授权Token获取接口
* <p>
* 参数列表
* <ul>
* <li><code>appKey</code> 应用key
* <li><code>appSecret</code> 应用密钥
* </ul>
* <p>
* 返回结果
* <ul>
* <li><code>appToken</code> 接口访问授权Token
* <li><code>expiryDate</code> 过期日期
* <li><code>error</code> 错误信息,当appKey、appSecret为空或错误时返回【secretError】
* </ul>
* 使用示例
* <p>
* <pre>
&lt;script&gt;
$.getJSON('${site.dynamicPath}api/appToken?appKey=1&amp;appSecret=1', function(data){
  $('article p em').text(data.clicks);
});
&lt;/script&gt;
* </pre>
*/
@Component
public class AppTokenDirective extends AbstractAppDirective {
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
            handler.put("error", SECRET_ERROR);
        }
        handler.render();
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
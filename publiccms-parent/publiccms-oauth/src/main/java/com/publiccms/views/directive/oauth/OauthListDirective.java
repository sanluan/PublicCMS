package com.publiccms.views.directive.oauth;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.publiccms.common.api.oauth.OauthGateway;
import com.publiccms.common.base.AbstractTemplateDirective;
import com.publiccms.common.handler.RenderHandler;
import com.publiccms.entities.sys.SysSite;

import freemarker.template.TemplateException;

/**
*
* oauthList 账户查询指令
* <p>
* 参数列表
返回结果
 * <ul>
 * <li><code>list</code>登录渠道名称列表
* </ul>
* 使用示例
* <p>
* &lt;@oauth.list&gt;&lt;#list list as a&gt;${a}&lt;#sep&gt;,&lt;/#list&gt;&lt;/@oauth.list&gt;
*
* <pre>
 &lt;script&gt;
  $.getJSON('${site.dynamicPath}api/directive/oauth/list', function(data){
    console.log(data);
  });
  &lt;/script&gt;
* </pre>
*/
@Component
public class OauthListDirective extends AbstractTemplateDirective {
    @Override
    public void execute(RenderHandler handler) throws IOException, TemplateException {
        SysSite site = getSite(handler);
        List<String> list = new ArrayList<>();
        if (null != oauthList) {
            for (OauthGateway oauth : oauthList) {
                if (oauth.enabled(site.getId())) {
                    list.add(oauth.getChannel());
                }
            }
        }
        handler.put("list", list).render();
    }

    @Autowired(required = false)
    private List<OauthGateway> oauthList;
}

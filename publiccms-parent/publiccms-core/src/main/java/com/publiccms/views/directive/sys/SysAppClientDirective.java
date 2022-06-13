package com.publiccms.views.directive.sys;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import jakarta.annotation.Resource;

import org.springframework.stereotype.Component;

import com.publiccms.common.base.AbstractTemplateDirective;
import com.publiccms.common.handler.RenderHandler;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.entities.sys.SysAppClient;
import com.publiccms.entities.sys.SysSite;
import com.publiccms.logic.service.sys.SysAppClientService;

/**
*
* sysAppClient 应用客户端查询指令
* <p>
* 参数列表
* <ul>
* <li><code>id</code> 客户端id，结果返回<code>object</code>
* {@link com.publiccms.entities.sys.SysAppClient}
* <li><code>ids</code>
* 多个客户端id，逗号或空格间隔，当id为空时生效，结果返回<code>map</code>(id,<code>object</code>)
* </ul>
* 使用示例
* <p>
* &lt;@sys.appClient id=1&gt;${object.clientVersion}&lt;/@sys.appClient&gt;
* <p>
* &lt;@sys.appClient ids='1,2,3'&gt;&lt;#list map as
* k,v&gt;${k}:${v.clientVersion}&lt;#sep&gt;,&lt;/#list&gt;&lt;/@sys.appClient&gt;
* 
* <pre>
&lt;script&gt;
$.getJSON('//cms.publiccms.com/api/directive/sys/appClient?id=1&amp;appToken=接口访问授权Token', function(data){    
  console.log(data.clientVersion);
});
&lt;/script&gt;
* </pre>
*/
@Component
public class SysAppClientDirective extends AbstractTemplateDirective {

    @Override
    public void execute(RenderHandler handler) throws IOException, Exception {
        Long id = handler.getLong("id");
        SysSite site = getSite(handler);
        if (CommonUtils.notEmpty(id)) {
            SysAppClient entity = service.getEntity(id);
            if (null != entity && site.getId() == entity.getSiteId()) {
                handler.put("object", entity).render();
            }
        } else {
            Long[] ids = handler.getLongArray("ids");
            if (CommonUtils.notEmpty(ids)) {
                List<SysAppClient> entityList = service.getEntitys(ids);
                Map<String, SysAppClient> map = CommonUtils.listToMap(entityList, k -> k.getId().toString(), null,
                        entity -> site.getId() == entity.getSiteId());
                handler.put("map", map).render();
            }
        }
    }

    @Resource
    private SysAppClientService service;

    @Override
    public boolean needAppToken() {
        return true;
    }
}
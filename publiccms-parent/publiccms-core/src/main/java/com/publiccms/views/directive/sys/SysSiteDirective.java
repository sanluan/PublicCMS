package com.publiccms.views.directive.sys;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.springframework.stereotype.Component;

import com.publiccms.common.base.AbstractTemplateDirective;
import com.publiccms.common.handler.RenderHandler;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.entities.sys.SysSite;
import com.publiccms.logic.service.sys.SysSiteService;

import freemarker.template.TemplateException;

/**
*
* sysSite 站点查询指令
* <p>
* 参数列表
* <ul>
* <li><code>id</code>:站点id,结果返回<code>object</code>
* {@link com.publiccms.entities.sys.SysSite}
* <li><code>ids</code>:
* 多个站点id,逗号或空格间隔,当id为空时生效,结果返回<code>map</code>(id,<code>object</code>)
* </ul>
* 使用示例
* <p>
* &lt;@sys.site id=1&gt;${object.name}&lt;/@sys.site&gt;
* <p>
* &lt;@sys.site ids='1,2,3'&gt;&lt;#list map as
* k,v&gt;${k}:${v.name}&lt;#sep&gt;,&lt;/#list&gt;&lt;/@sys.site&gt;
* 
* <pre>
&lt;script&gt;
$.getJSON('//sys.publicsys.com/api/directive/sys/site?id=1&amp;appToken=接口访问授权Token', function(data){    
  console.log(data.name);
});
&lt;/script&gt;
* </pre>
*/
@Component
public class SysSiteDirective extends AbstractTemplateDirective {

    @Override
    public void execute(RenderHandler handler) throws IOException, TemplateException {
        Short id = handler.getShort("id");
        if (CommonUtils.notEmpty(id)) {
            SysSite entity = service.getEntity(id);
            if (null != entity) {
                handler.put("object", entity).render();
            }
        } else {
            Short[] ids = handler.getShortArray("ids");
            if (CommonUtils.notEmpty(ids)) {
                List<SysSite> entityList = service.getEntitys(ids);
                Map<String, SysSite> map = CommonUtils.listToMapSorted(entityList, k -> k.getId().toString(), ids);
                handler.put("map", map).render();
            }
        }
    }

    @Override
    public boolean needAppToken() {
        return true;
    }

    @Resource
    private SysSiteService service;

}

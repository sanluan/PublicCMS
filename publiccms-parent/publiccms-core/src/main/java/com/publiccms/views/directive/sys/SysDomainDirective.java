package com.publiccms.views.directive.sys;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

import com.publiccms.common.base.AbstractTemplateDirective;
import com.publiccms.common.handler.RenderHandler;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.entities.sys.SysDomain;
import com.publiccms.logic.service.sys.SysDomainService;

import freemarker.template.TemplateException;

/**
*
* sysDomain 域名查询指令
* <p>
* 参数列表
* <ul>
* <li><code>id</code>:域名,结果返回<code>object</code>
* {@link com.publiccms.entities.sys.SysDomain}
* <li><code>ids</code>:
* 多个域名,逗号或空格间隔,当id为空时生效,结果返回<code>map</code>(id,<code>object</code>)
* </ul>
* 使用示例
* <p>
* &lt;@sys.domain id='www.publiccms.com'&gt;${object.name}&lt;/@sys.domaing&gt;
* <p>
* &lt;@sys.domain ids='www.publiccms.com,www.sanluan.com'&gt;&lt;#list map as
* k,v&gt;${k}:${v.name}&lt;#sep&gt;,&lt;/#list&gt;&lt;/@sys.domain&gt;
* 
* <pre>
&lt;script&gt;
$.getJSON('${site.dynamicPath}api/directive/sys/domain?id=1&amp;appToken=接口访问授权Token', function(data){    
  console.log(data.name);
});
&lt;/script&gt;
* </pre>
*/
@Component
public class SysDomainDirective extends AbstractTemplateDirective {

    @Override
    public void execute(RenderHandler handler) throws IOException, TemplateException {
        String id = handler.getString("id");
        if (CommonUtils.notEmpty(id)) {
            SysDomain entity = service.getEntity(id);
            if (null != entity) {
                handler.put("object", entity).render();
            }
        } else {
            String[] ids = handler.getStringArray("ids");
            if (CommonUtils.notEmpty(ids)) {
                List<SysDomain> entityList = service.getEntitys(ids);
                Map<String, SysDomain> map = CommonUtils.listToMapSorted(entityList, k -> k.getName(), ids);
                handler.put("map", map).render();
            }
        }
    }

    @Override
    public boolean needAppToken() {
        return true;
    }

    @Resource
    private SysDomainService service;

}

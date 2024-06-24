package com.publiccms.views.directive.cms;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

import com.publiccms.common.base.AbstractTemplateDirective;
import com.publiccms.common.handler.RenderHandler;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.entities.cms.CmsTag;
import com.publiccms.entities.sys.SysSite;
import com.publiccms.logic.service.cms.CmsTagService;

import freemarker.template.TemplateException;

/**
*
* tag 标签查询指令
* <p>
* 参数列表
* <ul>
* <li><code>id</code>:标签id,结果返回<code>object</code>
* {@link com.publiccms.entities.cms.CmsTag}
* <li><code>ids</code>:
* 多个标签id,逗号或空格间隔,当id为空时生效,结果返回<code>map</code>(id,<code>object</code>)
* </ul>
* 使用示例
* <p>
* &lt;@cms.tag id=1&gt;${object.name}&lt;/@cms.tag&gt;
* <p>
* &lt;@cms.tag ids='1,2,3'&gt;&lt;#list map as
* k,v&gt;${k}:${v.name}&lt;#sep&gt;,&lt;/#list&gt;&lt;/@cms.tag&gt;
* 
* <pre>
&lt;script&gt;
$.getJSON('${site.dynamicPath}api/directive/cms/tag?id=1', function(data){    
  console.log(data.name);
});
&lt;/script&gt;
* </pre>
*/
@Component
public class CmsTagDirective extends AbstractTemplateDirective {

    @Override
    public void execute(RenderHandler handler) throws IOException, TemplateException {
        Long id = handler.getLong("id");
        SysSite site = getSite(handler);
        if (CommonUtils.notEmpty(id)) {
            CmsTag entity = service.getEntity(id);
            if (null != entity && site.getId() == entity.getSiteId()) {
                handler.put("object", entity).render();
            }
        } else {
            Long[] ids = handler.getLongArray("ids");
            if (CommonUtils.notEmpty(ids)) {
                List<CmsTag> entityList = service.getEntitys(ids);
                Map<String, CmsTag> map =  CommonUtils.listToMap(entityList, k -> k.getId().toString(), null,
                        entity -> site.getId() == entity.getSiteId());
                handler.put("map", map).render();
            }
        }
    }

    @Resource
    private CmsTagService service;

}

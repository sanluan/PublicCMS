package com.publiccms.views.directive.cms;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

import com.publiccms.common.base.AbstractTemplateDirective;
import com.publiccms.common.handler.RenderHandler;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.entities.cms.CmsTagType;
import com.publiccms.entities.sys.SysSite;
import com.publiccms.logic.service.cms.CmsTagTypeService;

import freemarker.template.TemplateException;

/**
*
* tagType 标签类型查询指令
* <p>
* 参数列表
* <ul>
* <li><code>id</code>:标签类型id,结果返回<code>object</code>
* {@link com.publiccms.entities.cms.CmsTagType}
* <li><code>ids</code>:
* 多个标签类型id,逗号或空格间隔,当id为空时生效,结果返回<code>map</code>(id,<code>object</code>)
* </ul>
* 使用示例
* <p>
* &lt;@cms.tagType id=1&gt;${object.name}&lt;/@cms.tagType&gt;
* <p>
* &lt;@cms.tagType ids='1,2,3'&gt;&lt;#list map as
* k,v&gt;${k}:${v.name}&lt;#sep&gt;,&lt;/#list&gt;&lt;/@cms.tagType&gt;
* 
* <pre>
&lt;script&gt;
$.getJSON('${site.dynamicPath}api/directive/cms/tagType?id=1', function(data){    
  console.log(data.name);
});
&lt;/script&gt;
* </pre>
*/
@Component
public class CmsTagTypeDirective extends AbstractTemplateDirective {

    @Override
    public void execute(RenderHandler handler) throws IOException, TemplateException {
        Integer id = handler.getInteger("id");
        SysSite site = getSite(handler);
        if (CommonUtils.notEmpty(id)) {
            CmsTagType entity = service.getEntity(id);
            if (null != entity && site.getId() == entity.getSiteId()) {
                handler.put("object", entity).render();
            }
        } else {
            Integer[] ids = handler.getIntegerArray("ids");
            if (CommonUtils.notEmpty(ids)) {
                List<CmsTagType> entityList = service.getEntitys(ids);
                Map<String, CmsTagType> map = CommonUtils.listToMap(entityList, k -> k.getId().toString(), null,
                        entity -> site.getId() == entity.getSiteId());
                handler.put("map", map).render();
            }
        }
    }

    @Resource
    private CmsTagTypeService service;

}

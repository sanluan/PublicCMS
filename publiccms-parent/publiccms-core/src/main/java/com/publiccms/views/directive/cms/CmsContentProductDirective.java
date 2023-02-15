package com.publiccms.views.directive.cms;

// Generated 2021-6-26 17:53:08 by com.publiccms.common.generator.SourceGenerator

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

import com.publiccms.common.base.AbstractTemplateDirective;
import com.publiccms.common.handler.RenderHandler;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.entities.cms.CmsContentProduct;
import com.publiccms.entities.sys.SysSite;
import com.publiccms.logic.component.template.TemplateComponent;
import com.publiccms.logic.service.cms.CmsContentProductService;

/**
*
* contentProduct 产品查询指令
* <p>
* 参数列表
* <ul>
* <li><code>id</code>:内容id,结果返回<code>object</code>{@link com.publiccms.entities.cms.CmsContentProduct} 
* <li><code>absoluteURL</code>:cover处理为绝对路径 默认为<code> true</code>
* <li><code>ids</code>:多个内容id,逗号或空格间隔,当id为空时生效,结果返回<code>map</code>(id,<code>object</code>)
* </ul>
* 使用示例
* <p>
* &lt;@cms.contentProduct id=1&gt;${object.title}&lt;/@cms.contentProduct&gt;
* <p>
* &lt;@cms.contentProduct ids=1,2,3&gt;&lt;#list map as
* k,v&gt;${k}:${v.title}&lt;#sep&gt;,&lt;/#list&gt;&lt;/@cms.contentProduct&gt;
* 
* <pre>
*  &lt;script&gt;
   $.getJSON('${site.dynamicPath}api/directive/cms/contentProduct?id=1', function(data){    
     console.log(data.title);
   });
   &lt;/script&gt;
* </pre>
*/
@Component
public class CmsContentProductDirective extends AbstractTemplateDirective {

    @Override
    public void execute(RenderHandler handler) throws IOException, Exception {
        Long id = handler.getLong("id");
        boolean absoluteURL = handler.getBoolean("absoluteURL", true);
        SysSite site = getSite(handler);
        if (CommonUtils.notEmpty(id)) {
            CmsContentProduct entity = service.getEntity(id);
            if (null != entity && site.getId() == entity.getSiteId()) {
                if (absoluteURL) {
                    entity.setCover(TemplateComponent.getUrl(site, true, entity.getCover()));
                }
                handler.put("object", entity).render();
            }
        } else {
            Long[] ids = handler.getLongArray("ids");
            if (CommonUtils.notEmpty(ids)) {
                List<CmsContentProduct> entityList = service.getEntitys(ids);
                Consumer<CmsContentProduct> consumer = e -> {
                    if (absoluteURL) {
                        e.setCover(TemplateComponent.getUrl(site, true, e.getCover()));
                    }
                };
                Map<String, CmsContentProduct> map = CommonUtils.listToMap(entityList, k -> k.getId().toString(), consumer,
                        entity -> site.getId() == entity.getSiteId());
                handler.put("map", map).render();
            }
        }
    }

    @Resource
    private CmsContentProductService service;
}

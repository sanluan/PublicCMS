package com.publiccms.views.directive.cms;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.publiccms.common.base.AbstractTemplateDirective;
import com.publiccms.common.handler.RenderHandler;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.common.tools.ExtendUtils;
import com.publiccms.entities.cms.CmsCategory;
import com.publiccms.entities.cms.CmsCategoryAttribute;
import com.publiccms.entities.sys.SysSite;
import com.publiccms.logic.component.template.TemplateComponent;
import com.publiccms.logic.service.cms.CmsCategoryAttributeService;
import com.publiccms.logic.service.cms.CmsCategoryService;

/**
 *
 * category 分类查询指令
 * <p>
 * 参数列表
 * <ul>
 * <li><code>id</code> 分类id,结果返回<code>object</code>
 * {@link com.publiccms.entities.cms.CmsCategory}
 * <li><code>code</code> 分类编码,当id为空时生效,结果返回<code>object</code>
 * <li><code>absoluteURL</code> url处理为绝对路径 默认为<code> true</code>
 * <li><code>ids</code>
 * 多个分类id,逗号或空格间隔,当id或code为空时生效,结果返回<code>map</code>(id,<code>object</code>)
 * </ul>
 * 使用示例
 * <p>
 * &lt;@cms.category id=1&gt;${object.name}&lt;/@cms.category&gt;
 * <p>
 * &lt;@cms.category ids=1,2,3&gt;&lt;#list map as
 * k,v&gt;${k}:${v.name}&lt;#sep&gt;,&lt;/#list&gt;&lt;/@cms.category&gt;
 * 
 * <pre>
   &lt;script&gt;
    $.getJSON('//cms.publiccms.com/api/directive/cms/category?id=1', function(data){    
      console.log(data.name);
    });
    &lt;/script&gt;
 * </pre>
 */
@Component
public class CmsCategoryDirective extends AbstractTemplateDirective {

    @Override
    public void execute(RenderHandler handler) throws IOException, Exception {
        Integer id = handler.getInteger("id");
        String code = handler.getString("code");
        boolean absoluteURL = handler.getBoolean("absoluteURL", true);
        SysSite site = getSite(handler);
        if (CommonUtils.notEmpty(id) || CommonUtils.notEmpty(code)) {
            CmsCategory entity;
            if (CommonUtils.notEmpty(id)) {
                entity = service.getEntity(id);
            } else {
                entity = service.getEntityByCode(site.getId(), code);
            }
            if (null != entity && site.getId() == entity.getSiteId()) {
                if (absoluteURL) {
                    TemplateComponent.initCategoryUrl(site, entity);
                }
                handler.put("object", entity);
                if (handler.getBoolean("containsAttribute", false)) {
                    CmsCategoryAttribute attribute = attributeService.getEntity(id);
                    if (null != attribute) {
                        Map<String, String> map = ExtendUtils.getExtendMap(attribute.getData());
                        map.put("title", attribute.getTitle());
                        map.put("keywords", attribute.getKeywords());
                        map.put("description", attribute.getDescription());
                        handler.put("attribute", map);
                    }
                }
                handler.render();
            }
        } else {
            Integer[] ids = handler.getIntegerArray("ids");
            if (CommonUtils.notEmpty(ids)) {
                List<CmsCategory> entityList = service.getEntitys(ids);
                Consumer<CmsCategory> consumer = null;
                if (absoluteURL) {
                    consumer = e -> {
                        TemplateComponent.initCategoryUrl(site, e);
                    };
                }
                Map<String, CmsCategory> map = CommonUtils.listToMap(entityList, k -> k.getId().toString(), consumer,
                        entity -> site.getId() == entity.getSiteId());
                handler.put("map", map).render();
            }
        }
    }

    @Autowired
    private CmsCategoryService service;
    @Autowired
    private CmsCategoryAttributeService attributeService;
}

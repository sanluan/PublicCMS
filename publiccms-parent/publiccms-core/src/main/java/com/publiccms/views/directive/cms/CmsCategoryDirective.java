package com.publiccms.views.directive.cms;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.publiccms.common.base.AbstractTemplateDirective;
import com.publiccms.common.handler.RenderHandler;
import com.publiccms.common.tools.CmsUrlUtils;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.common.tools.ExtendUtils;
import com.publiccms.entities.cms.CmsCategory;
import com.publiccms.entities.cms.CmsCategoryAttribute;
import com.publiccms.entities.sys.SysSite;
import com.publiccms.logic.service.cms.CmsCategoryAttributeService;
import com.publiccms.logic.service.cms.CmsCategoryService;

import freemarker.template.TemplateException;

/**
 *
 * category 分类查询指令
 * <p>
 * 参数列表
 * <ul>
 * <li><code>id</code>:分类id,结果返回<code>object</code>
 * {@link com.publiccms.entities.cms.CmsCategory}
 * <li><code>code</code>:分类编码,当id为空时生效,结果返回<code>object</code>
 * <li><code>absoluteURL</code>:url处理为绝对路径 默认为<code>true</code>
 * <li><code>containsAttribute</code>默认为<code>false</code>,http请求时为高级选项,为true时<code>object.attribute</code>为分类扩展数据<code>map</code>(字段编码,<code>value</code>)
 * <li><code>ids</code>:
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
    $.getJSON('${site.dynamicPath}api/directive/cms/category?id=1', function(data){    
      console.log(data.name);
    });
    &lt;/script&gt;
 * </pre>
 */
@Component
public class CmsCategoryDirective extends AbstractTemplateDirective {

    @Override
    public void execute(RenderHandler handler) throws IOException, TemplateException {
        Integer id = handler.getInteger("id");
        String code = handler.getString("code");
        boolean absoluteURL = handler.getBoolean("absoluteURL", true);
        boolean containsAttribute = handler.getBoolean("containsAttribute", false);
        containsAttribute = handler.inHttp() ? getAdvanced(handler) && containsAttribute : containsAttribute;
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
                    CmsUrlUtils.initCategoryUrl(site, entity);
                }
                if (containsAttribute) {
                    entity.setAttribute(ExtendUtils.getAttributeMap(attributeService.getEntity(id)));
                }
                handler.put("object", entity);
                handler.render();
            }
        } else {
            Integer[] ids = handler.getIntegerArray("ids");
            if (CommonUtils.notEmpty(ids)) {
                List<CmsCategory> entityList = service.getEntitys(ids);
                Consumer<CmsCategory> consumer = null;
                if (containsAttribute) {
                    List<CmsCategoryAttribute> attributeList = attributeService.getEntitys(ids);
                    Map<Integer, CmsCategoryAttribute> attributeMap = CommonUtils.listToMap(attributeList,
                            k -> k.getCategoryId());
                    consumer = e -> {
                        if (absoluteURL) {
                            CmsUrlUtils.initCategoryUrl(site, e);
                        }
                        e.setAttribute(ExtendUtils.getAttributeMap(attributeMap.get(e.getId())));
                    };
                } else {
                    if (absoluteURL) {
                        consumer = e -> {
                            CmsUrlUtils.initCategoryUrl(site, e);
                        };
                    }
                }
                Map<String, CmsCategory> map = CommonUtils.listToMap(entityList, k -> k.getId().toString(), consumer,
                        entity -> site.getId() == entity.getSiteId());
                handler.put("map", map).render();
            }
        }
    }

    @Override
    public boolean supportAdvanced() {
        return true;
    }

    @Resource
    private CmsCategoryService service;
    @Resource
    private CmsCategoryAttributeService attributeService;
}

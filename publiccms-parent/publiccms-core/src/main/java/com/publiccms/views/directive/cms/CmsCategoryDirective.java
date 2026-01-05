package com.publiccms.views.directive.cms;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.function.UnaryOperator;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.publiccms.common.base.AbstractTemplateDirective;
import com.publiccms.common.handler.RenderHandler;
import com.publiccms.common.tools.CmsLangUtils;
import com.publiccms.common.tools.CmsUrlUtils;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.common.tools.ExtendUtils;
import com.publiccms.entities.cms.CmsCategory;
import com.publiccms.entities.cms.CmsCategoryAttribute;
import com.publiccms.entities.cms.CmsCategoryLang;
import com.publiccms.entities.cms.CmsCategoryLangId;
import com.publiccms.entities.sys.SysSite;
import com.publiccms.logic.service.cms.CmsCategoryAttributeService;
import com.publiccms.logic.service.cms.CmsCategoryLangService;
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
 * <li><code>lang</code>:语言
 * <li><code>absoluteURL</code>:url处理为绝对路径 默认为<code>true</code>
 * <li><code>containsAttribute</code>默认为<code>false</code>,http请求时为高级选项,为true时<code>object.attribute</code>为分类扩展数据<code>map</code>(字段编码,<code>value</code>)
 * <li><code>ids</code>:
 * 多个分类id,逗号或空格间隔,当id或code为空时生效,结果返回<code>map</code>(id,<code>object</code>)
 * </ul>
 * <p>
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

    @Resource
    private CmsCategoryLangService langService;

    @Override
    public void execute(RenderHandler handler) throws IOException, TemplateException {
        Integer id = handler.getInteger("id");
        String code = handler.getString("code");
        boolean absoluteURL = handler.getBoolean("absoluteURL", true);
        boolean containsAttribute = handler.getBoolean("containsAttribute", false) && (!handler.inHttp() || getAdvanced(handler));
        SysSite site = getSite(handler);
        String lang = handler.getString("lang");
        if (CommonUtils.notEmpty(id) || CommonUtils.notEmpty(code)) {
            CmsCategory entity;
            if (CommonUtils.notEmpty(id)) {
                entity = service.getEntity(id);
            } else {
                entity = service.getEntityByCode(site.getId(), code);
            }
            if (null != entity && site.getId() == entity.getSiteId()) {
                CmsCategoryLang langEntity = null;

                if (CommonUtils.notEmpty(lang) && !lang.equalsIgnoreCase(entity.getLang())) {
                    langEntity = langService.getEntity(new CmsCategoryLangId(entity.getId(), lang));
                    CmsLangUtils.initLang(entity, langEntity);
                }

                if (absoluteURL) {
                    CmsUrlUtils.initCategoryUrl(site, entity);
                }
                if (containsAttribute) {
                    CmsCategoryAttribute attribute = attributeService.getEntity(entity.getId());
                    CmsLangUtils.initLang(attribute, lang, langEntity);
                    entity.setAttribute(ExtendUtils.getAttributeMap(attribute));
                }
                handler.put("object", entity);
                handler.render();
            }
        } else

        {
            Integer[] ids = handler.getIntegerArray("ids");
            if (CommonUtils.notEmpty(ids)) {
                List<CmsCategory> entityList = service.getEntitys(ids);
                Map<Integer, CmsCategoryAttribute> attributeMap = containsAttribute
                        ? CommonUtils.listToMap(attributeService.getEntitys(ids), k -> k.getCategoryId())
                        : null;

                CmsCategoryLangId[] langIds = entityList.stream().map(e -> new CmsCategoryLangId(e.getId(), lang))
                        .toArray(CmsCategoryLangId[]::new);
                Map<Integer, CmsCategoryLang> langMap = CommonUtils.listToMap(langService.getEntitys(langIds),
                        k -> k.getId().getCategoryId());

                UnaryOperator<CmsCategory> valueMapper = e -> {

                    CmsCategoryLang langEntity = langMap.get(e.getId());
                    CmsLangUtils.initLang(e, langEntity);

                    if (absoluteURL) {
                        CmsUrlUtils.initCategoryUrl(site, e);
                    }
                    if (containsAttribute) {
                        CmsCategoryAttribute attribute = attributeMap.get(e.getId());
                        CmsLangUtils.initLang(attribute, lang, langEntity);
                        e.setAttribute(ExtendUtils.getAttributeMap(attribute));
                    }
                    return e;
                };
                Map<String, CmsCategory> map = CommonUtils.listToMapSorted(entityList, k -> k.getId().toString(), valueMapper,
                        ids, e -> e.getId(), entity -> site.getId() == entity.getSiteId());
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

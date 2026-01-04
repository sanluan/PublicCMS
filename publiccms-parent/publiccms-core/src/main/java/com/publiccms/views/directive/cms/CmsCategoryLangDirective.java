package com.publiccms.views.directive.cms;

import java.io.IOException;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.publiccms.common.base.AbstractTemplateDirective;
import com.publiccms.common.handler.RenderHandler;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.common.tools.ExtendUtils;
import com.publiccms.entities.cms.CmsCategory;
import com.publiccms.entities.cms.CmsCategoryLang;
import com.publiccms.entities.cms.CmsCategoryLangId;
import com.publiccms.entities.sys.SysSite;
import com.publiccms.logic.service.cms.CmsCategoryLangService;
import com.publiccms.logic.service.cms.CmsCategoryService;

import freemarker.template.TemplateException;

/**
 *
 * categoryLang 分类语言查询指令
 * <p>
 * 参数列表
 * <ul>
 * <li><code>id</code>:分类id,结果返回<code>object</code>
 * {@link com.publiccms.entities.cms.CmsCategoryLang}
 * <li><code>lang</code>:语言
 * </ul>
 * <p>
 * 使用示例
 * <p>
 * &lt;@cms.categoryLang id=1 lang=cn&gt;${object.name}&lt;/@cms.category&gt;
 *
 * <pre>
   &lt;script&gt;
    $.getJSON('${site.dynamicPath}api/directive/cms/categoryLang?id=1&amp;lang=cn&amp;appToken=接口访问授权Token', function(data){
      console.log(data.name);
    });
    &lt;/script&gt;
 * </pre>
 */
@Component
public class CmsCategoryLangDirective extends AbstractTemplateDirective {
    @Override
    public void execute(RenderHandler handler) throws IOException, TemplateException {
        Integer id = handler.getInteger("id");
        String lang = handler.getString("lang");
        SysSite site = getSite(handler);
        if (CommonUtils.notEmpty(id) && CommonUtils.notEmpty(lang)) {
            CmsCategory category = categoryService.getEntity(id);
            if (null != category && site.getId() == category.getSiteId()) {
                CmsCategoryLang entity = service.getEntity(new CmsCategoryLangId(id, lang));
                if (null != entity) {
                    entity.setAttribute(ExtendUtils.getExtendMap(entity.getData()));
                    handler.put("object", entity).render();
                }
            }
        }
    }

    /**
     * @return whether need the app token
     */
    public boolean needAppToken() {
        return true;
    }

    @Resource
    private CmsCategoryService categoryService;
    @Resource
    private CmsCategoryLangService service;
}

package com.publiccms.views.directive.tools;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.publiccms.common.base.AbstractTemplateDirective;
import com.publiccms.common.handler.RenderHandler;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.entities.cms.CmsCategory;
import com.publiccms.entities.sys.SysSite;
import com.publiccms.logic.component.template.TemplateComponent;
import com.publiccms.logic.service.cms.CmsCategoryService;

import freemarker.template.TemplateException;

/**
 *
 * CreateCategoryFileDirective
 * 
 */
@Component
public class CreateCategoryFileDirective extends AbstractTemplateDirective {

    @Override
    public void execute(RenderHandler handler) throws IOException, Exception {
        Integer id = handler.getInteger("id");
        String templatePath = handler.getString("templatePath");
        String filePath = handler.getString("filePath");
        Integer pageIndex = handler.getInteger("pageIndex");
        if (CommonUtils.notEmpty(id) && CommonUtils.notEmpty(templatePath) && CommonUtils.notEmpty(filePath)) {
            SysSite site = getSite(handler);
            try {
                CmsCategory category = categoryService.getEntity(id);
                if (null != category && site.getId() == category.getSiteId()) {
                    handler.put("url",
                            templateComponent.createCategoryFile(site, category, templatePath, filePath, pageIndex, null))
                            .render();
                }
            } catch (IOException | TemplateException e) {
                handler.print(e.getMessage());
            }
        }
    }

    @Override
    public boolean needAppToken() {
        return true;
    }

    @Autowired
    private TemplateComponent templateComponent;
    @Autowired
    private CmsCategoryService categoryService;

}

package com.publiccms.views.directive.task;

import static com.publiccms.logic.component.SiteComponent.getFullFileName;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.publiccms.common.base.AbstractTaskDirective;
import com.publiccms.entities.cms.CmsCategory;
import com.publiccms.entities.cms.CmsContent;
import com.publiccms.entities.sys.SysSite;
import com.publiccms.logic.component.TemplateComponent;
import com.publiccms.logic.service.cms.CmsCategoryService;
import com.publiccms.logic.service.cms.CmsContentService;
import com.sanluan.common.handler.RenderHandler;

import freemarker.template.TemplateException;

@Component
public class CreateContentFileDirective extends AbstractTaskDirective {
    @Override
    public void execute(RenderHandler handler) throws IOException, Exception {
        Integer id = handler.getInteger("id");
        String templatePath = handler.getString("templatePath");
        String filePath = handler.getString("filePath");
        Integer pageIndex = handler.getInteger("pageIndex");
        if (notEmpty(id) && notEmpty(templatePath) && notEmpty(filePath)) {
            SysSite site = getSite(handler);
            String templateFullPath = getFullFileName(site, templatePath);
            try {
                CmsContent content = contentService.getEntity(id);
                if (notEmpty(content) && site.getId() == content.getSiteId()) {
                    CmsCategory category = categoryService.getEntity(content.getCategoryId());
                    handler.put(
                            "url",
                            templateComponent.createContentFile(site, content, category, false, templateFullPath, filePath,
                                    pageIndex)).render();
                }
            } catch (IOException | TemplateException e) {
                handler.print(e.getMessage());
            }
        }
    }

    @Autowired
    private TemplateComponent templateComponent;
    @Autowired
    private CmsCategoryService categoryService;
    @Autowired
    private CmsContentService contentService;

}

package com.publiccms.views.directive.tools;

import java.io.IOException;

import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

import com.publiccms.common.base.AbstractTemplateDirective;
import com.publiccms.common.handler.RenderHandler;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.entities.sys.SysSite;
import com.publiccms.logic.component.site.SiteComponent;
import com.publiccms.logic.component.template.TemplateComponent;

import freemarker.template.TemplateException;

/**
 *
 * CreateTemplateFileDirective
 * 
 */
@Component
public class CreateTemplateFileDirective extends AbstractTemplateDirective {

    @Override
    public void execute(RenderHandler handler) throws IOException, Exception {
        String templatePath = handler.getString("templatePath");
        String filepath = handler.getString("filePath");
        Integer pageIndex = handler.getInteger("pageIndex");
        if (CommonUtils.notEmpty(templatePath) && CommonUtils.notEmpty(filepath)) {
            SysSite site = getSite(handler);
            String templateFullPath = SiteComponent.getFullTemplatePath(site, templatePath);
            try {
                handler.put("url",
                        templateComponent.createStaticFile(site, templateFullPath, filepath, pageIndex, null, null, null))
                        .render();
            } catch (IOException | TemplateException e) {
                handler.print(e.getMessage());
            }
        }
    }

    @Override
    public boolean needAppToken() {
        return true;
    }

    @Resource
    private TemplateComponent templateComponent;

}

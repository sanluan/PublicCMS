package com.publiccms.views.directive.task;

import static com.publiccms.logic.component.SiteComponent.getFullFileName;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.publiccms.common.base.AbstractTaskDirective;
import com.publiccms.entities.sys.SysSite;
import com.publiccms.logic.component.TemplateComponent;
import com.sanluan.common.handler.RenderHandler;

import freemarker.template.TemplateException;

@Component
public class CreateTemplateFileDirective extends AbstractTaskDirective {
    @Override
    public void execute(RenderHandler handler) throws IOException, Exception {
        String templatePath = handler.getString("templatePath");
        String filePath = handler.getString("filePath");
        Integer pageIndex = handler.getInteger("pageIndex");
        if (notEmpty(templatePath) && notEmpty(filePath)) {
            SysSite site = getSite(handler);
            String templateFullPath = getFullFileName(site, templatePath);
            try {
                handler.put("url", templateComponent.createStaticFile(site, templateFullPath, filePath, pageIndex, null, null))
                        .render();
            } catch (IOException | TemplateException e) {
                handler.print(e.getMessage());
            }
        }
    }

    @Autowired
    private TemplateComponent templateComponent;

}

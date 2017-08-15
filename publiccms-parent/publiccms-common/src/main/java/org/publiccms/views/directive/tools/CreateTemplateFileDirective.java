package org.publiccms.views.directive.tools;

import static com.publiccms.common.tools.CommonUtils.notEmpty;
import static org.publiccms.logic.component.site.SiteComponent.getFullFileName;

import java.io.IOException;

import org.publiccms.common.base.AbstractTemplateDirective;
import org.publiccms.entities.sys.SysSite;
import org.publiccms.logic.component.template.TemplateComponent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.publiccms.common.handler.RenderHandler;

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
    
    @Override
    public boolean needAppToken() {
        return true;
    }

    @Autowired
    private TemplateComponent templateComponent;

}

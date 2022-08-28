package com.publiccms.views.directive.tools;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

import com.publiccms.common.base.AbstractTemplateDirective;
import com.publiccms.common.handler.RenderHandler;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.entities.sys.SysSite;
import com.publiccms.logic.component.site.SiteComponent;
import com.publiccms.logic.component.template.MetadataComponent;
import com.publiccms.logic.component.template.TemplateComponent;
import com.publiccms.views.pojo.entities.CmsPageData;
import com.publiccms.views.pojo.entities.CmsPageMetadata;

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
                Map<String, Object> model = new HashMap<>();
                model.put("parameters", handler.getMap("parameters"));
                String realTemplatePath = siteComponent.getTemplateFilePath(site, templatePath);
                CmsPageMetadata metadata = metadataComponent.getTemplateMetadata(realTemplatePath);
                CmsPageData data = metadataComponent.getTemplateData(realTemplatePath);
                Map<String, Object> metadataMap = metadata.getAsMap(data);
                handler.put("url",
                        templateComponent.createStaticFile(site, templateFullPath, filepath, pageIndex, metadataMap, model, null))
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
    @Resource
    private MetadataComponent metadataComponent;
}

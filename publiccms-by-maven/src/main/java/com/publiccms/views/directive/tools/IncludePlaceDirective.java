package com.publiccms.views.directive.tools;

import static com.publiccms.logic.component.TemplateComponent.INCLUDE_DIRECTORY;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.publiccms.common.base.AbstractTemplateDirective;
import com.publiccms.entities.sys.SysSite;
import com.publiccms.logic.component.MetadataComponent;
import com.publiccms.logic.component.TemplateComponent;
import com.publiccms.views.pojo.CmsPlaceMetadata;
import com.sanluan.common.handler.RenderHandler;

@Component
public class IncludePlaceDirective extends AbstractTemplateDirective {

    @Override
    public void execute(RenderHandler handler) throws IOException, Exception {
        String path = handler.getString("path");
        if (notEmpty(path)) {
            SysSite site = getSite(handler);
            CmsPlaceMetadata metadata = metadataComponent.getPlaceMetadata(siteComponent.getWebTemplateFilePath(site,
                    INCLUDE_DIRECTORY + path));
            if (site.isUseSsi()) {
                handler.print("<!--#include virtual=\"/" + INCLUDE_DIRECTORY + path + "\"-->");
            } else {
                handler.print(templateComponent.printPlace(site, path, metadata));
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
    private MetadataComponent metadataComponent;
}

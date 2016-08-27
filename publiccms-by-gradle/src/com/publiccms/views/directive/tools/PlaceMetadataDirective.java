package com.publiccms.views.directive.tools;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.publiccms.common.base.AbstractTemplateDirective;
import com.publiccms.logic.component.MetadataComponent;

import static com.publiccms.logic.component.TemplateComponent.INCLUDE_DIRECTORY;

import com.sanluan.common.handler.RenderHandler;

@Component
public class PlaceMetadataDirective extends AbstractTemplateDirective {

    @Override
    public void execute(RenderHandler handler) throws IOException, Exception {
        String path = handler.getString("path");
        String dir = handler.getString("dir");
        if (notEmpty(path) && !path.endsWith(SEPARATOR)) {
            handler.put(
                    "object",
                    metadataComponent.getPlaceMetadata(siteComponent.getWebTemplateFilePath(getSite(handler), INCLUDE_DIRECTORY
                            + path))).render();
        } else if (null != dir) {
            handler.put(
                    "object",
                    metadataComponent.getPlaceMetadataMap(siteComponent.getWebTemplateFilePath(getSite(handler),
                            INCLUDE_DIRECTORY + dir))).render();
        }
    }
    
    @Override
    public boolean needAppToken() {
        return true;
    }

    @Autowired
    private MetadataComponent metadataComponent;
}

package org.publiccms.views.directive.tools;

import static com.publiccms.common.tools.CommonUtils.notEmpty;
import static org.publiccms.logic.component.template.TemplateComponent.INCLUDE_DIRECTORY;

import java.io.IOException;

import org.publiccms.common.base.AbstractTemplateDirective;
import org.publiccms.logic.component.template.MetadataComponent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.publiccms.common.handler.RenderHandler;

/**
 *
 * PlaceMetadataDirective
 * 
 */
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

package com.publiccms.views.directive.tools;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.publiccms.common.base.AbstractTemplateDirective;
import com.publiccms.common.constants.CommonConstants;
import com.publiccms.common.handler.RenderHandler;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.logic.component.template.MetadataComponent;
import com.publiccms.logic.component.template.TemplateComponent;

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
        if (CommonUtils.notEmpty(path) && !path.endsWith(CommonConstants.SEPARATOR)) {
            handler.put("object",
                    metadataComponent.getPlaceMetadata(
                            siteComponent.getWebTemplateFilePath(getSite(handler), TemplateComponent.INCLUDE_DIRECTORY + path)))
                    .render();
        }
    }

    @Override
    public boolean needAppToken() {
        return true;
    }

    @Autowired
    private MetadataComponent metadataComponent;
}

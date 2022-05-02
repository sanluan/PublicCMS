package com.publiccms.views.directive.tools;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.publiccms.common.base.AbstractTemplateDirective;
import com.publiccms.common.constants.CommonConstants;
import com.publiccms.common.handler.RenderHandler;
import com.publiccms.common.tools.CmsFileUtils;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.entities.sys.SysSite;
import com.publiccms.logic.component.template.MetadataComponent;
import com.publiccms.logic.component.template.TemplateComponent;
import com.publiccms.views.pojo.entities.CmsPageData;
import com.publiccms.views.pojo.entities.CmsPlaceMetadata;

/**
 *
 * IncludePlaceDirective
 * 
 */
@Component
public class IncludePlaceDirective extends AbstractTemplateDirective {

    @Override
    public void execute(RenderHandler handler) throws IOException, Exception {
        String path = handler.getString("path");
        if (CommonUtils.notEmpty(path)) {
            SysSite site = getSite(handler);
            String filepath = siteComponent.getWebTemplateFilePath(site, TemplateComponent.INCLUDE_DIRECTORY + path);
            CmsPlaceMetadata metadata = metadataComponent.getPlaceMetadata(filepath);
            if (site.isUseSsi()) {
                StringBuilder sb = new StringBuilder("<!--#include virtual=\"/");
                if (null != site.getParentId() && CommonUtils.notEmpty(site.getDirectory())) {
                    sb.append(site.getDirectory()).append(CommonConstants.SEPARATOR);
                }
                sb.append(TemplateComponent.INCLUDE_DIRECTORY).append(path).append("\"-->");
                handler.print(sb.toString());
            } else {
                String webfilepath = siteComponent.getWebFilePath(site, TemplateComponent.INCLUDE_DIRECTORY + path);
                if (CmsFileUtils.exists(webfilepath)) {
                    handler.print(CmsFileUtils.getFileContent(webfilepath));
                } else {
                    CmsPageData data = metadataComponent.getTemplateData(filepath);
                    templateComponent.printPlace(handler.getWriter(), site, path, metadata, data);
                }
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

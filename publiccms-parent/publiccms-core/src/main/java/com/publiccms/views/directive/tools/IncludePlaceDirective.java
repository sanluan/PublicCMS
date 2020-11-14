package com.publiccms.views.directive.tools;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.publiccms.common.base.AbstractTemplateDirective;
import com.publiccms.common.handler.RenderHandler;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.entities.sys.SysSite;
import com.publiccms.logic.component.template.MetadataComponent;
import com.publiccms.logic.component.template.TemplateComponent;
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
        String[] paths = handler.getStringArray("paths");
        if (CommonUtils.notEmpty(path)) {
            SysSite site = getSite(handler);
            CmsPlaceMetadata metadata = metadataComponent
                    .getPlaceMetadata(siteComponent.getWebTemplateFilePath(site, TemplateComponent.INCLUDE_DIRECTORY + path));
            if (site.isUseSsi()) {
                handler.print(new StringBuilder("<!--#include virtual=\"/").append(TemplateComponent.INCLUDE_DIRECTORY)
                        .append(path).append("\"-->").toString());
            } else {
                templateComponent.printPlace(handler.getWriter(), site, path, metadata);
            }
        } else if (CommonUtils.notEmpty(paths)) {
            SysSite site = getSite(handler);
            Map<String, String> map = new LinkedHashMap<>();
            if (site.isUseSsi()) {
                for (String p : paths) {
                    map.put(p, new StringBuilder("<!--#include virtual=\"/").append(TemplateComponent.INCLUDE_DIRECTORY).append(p)
                            .append("\"-->").toString());
                }
                handler.put("map", map).render();
            } else {
                for (String p : paths) {
                    if (!map.containsKey(path)) {
                        CmsPlaceMetadata metadata = metadataComponent.getPlaceMetadata(
                                siteComponent.getWebTemplateFilePath(site, TemplateComponent.INCLUDE_DIRECTORY + p));
                        map.put(path, templateComponent.printPlace(site, path, metadata));
                    }
                }
                handler.put("map", map).render();
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

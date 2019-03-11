package com.publiccms.views.directive.task;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.publiccms.common.base.AbstractTaskDirective;
import com.publiccms.common.constants.CommonConstants;
import com.publiccms.common.handler.RenderHandler;
import com.publiccms.common.tools.CmsFileUtils;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.common.tools.CmsFileUtils.FileInfo;
import com.publiccms.entities.sys.SysSite;
import com.publiccms.logic.component.site.SiteComponent;
import com.publiccms.logic.component.template.MetadataComponent;
import com.publiccms.logic.component.template.TemplateComponent;
import com.publiccms.views.pojo.entities.CmsPageData;
import com.publiccms.views.pojo.entities.CmsPageMetadata;

import freemarker.template.TemplateException;

/**
 *
 * PublishPageDirective
 *
 */
@Component
public class PublishPageDirective extends AbstractTaskDirective {

    @Override
    public void execute(RenderHandler handler) throws IOException, Exception {
        String path = handler.getString("path", CommonConstants.SEPARATOR);
        SysSite site = getSite(handler);
        String filePath = siteComponent.getWebTemplateFilePath(site, path);
        if (CmsFileUtils.isFile(filePath)) {
            Map<String, Boolean> map = new LinkedHashMap<>();
            CmsPageMetadata metadata = metadataComponent.getTemplateMetadata(filePath);
            if (CommonUtils.notEmpty(metadata.getPublishPath())) {
                try {
                    CmsPageData data = metadataComponent
                            .getTemplateData(siteComponent.getCurrentSiteWebTemplateFilePath(site, filePath));
                    templateComponent.createStaticFile(site, SiteComponent.getFullTemplatePath(site, path),
                            metadata.getPublishPath(), null, metadata.getAsMap(data), null);
                    map.put(path, true);
                } catch (IOException | TemplateException e) {
                    map.put(path, false);
                }
                handler.put("map", map).render();
            }
        } else if (CmsFileUtils.isDirectory(filePath)) {
            handler.put("map", deal(site, path)).render();
        }
    }

    private Map<String, Boolean> deal(SysSite site, String path) {
        path = path.replace("\\", CommonConstants.SEPARATOR).replace("//", CommonConstants.SEPARATOR);
        Map<String, Boolean> map = new LinkedHashMap<>();
        List<FileInfo> list = CmsFileUtils.getFileList(siteComponent.getWebTemplateFilePath(site, path), null);
        for (FileInfo fileInfo : list) {
            String filePath = path + fileInfo.getFileName();
            if (fileInfo.isDirectory()) {
                map.putAll(deal(site, filePath + CommonConstants.SEPARATOR));
            } else {
                CmsPageMetadata metadata = metadataComponent
                        .getTemplateMetadata(siteComponent.getWebTemplateFilePath(site, filePath));
                if (null != metadata && CommonUtils.notEmpty(metadata.getPublishPath())) {
                    try {
                        String templatePath = SiteComponent.getFullTemplatePath(site, filePath);
                        CmsPageData data = metadataComponent
                                .getTemplateData(siteComponent.getCurrentSiteWebTemplateFilePath(site, filePath));
                        templateComponent.createStaticFile(site, templatePath, metadata.getPublishPath(), null,
                                metadata.getAsMap(data), null);
                        map.put(filePath, true);
                    } catch (IOException | TemplateException e) {
                        map.put(filePath, false);
                    }
                }
            }
        }
        return map;
    }

    @Autowired
    private TemplateComponent templateComponent;
    @Autowired
    private MetadataComponent metadataComponent;

}

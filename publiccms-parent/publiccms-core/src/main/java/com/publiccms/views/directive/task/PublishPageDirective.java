package com.publiccms.views.directive.task;

import java.io.File;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.publiccms.common.base.AbstractTaskDirective;
import com.publiccms.common.handler.RenderHandler;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.entities.sys.SysSite;
import com.publiccms.logic.component.site.FileComponent;
import com.publiccms.logic.component.site.FileComponent.FileInfo;
import com.publiccms.logic.component.site.SiteComponent;
import com.publiccms.logic.component.template.MetadataComponent;
import com.publiccms.logic.component.template.TemplateComponent;
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
        String path = handler.getString("path", SEPARATOR);
        SysSite site = getSite(handler);
        String fullPath = siteComponent.getWebTemplateFilePath(site, path);
        File file = new File(fullPath);
        if (file.isFile()) {
            Map<String, Boolean> map = new LinkedHashMap<>();
            CmsPageMetadata metadata = metadataComponent.getTemplateMetadata(fullPath);
            if (null != metadata && CommonUtils.notEmpty(metadata.getPublishPath())) {
                try {
                    templateComponent.createStaticFile(site, SiteComponent.getFullFileName(site, path), metadata.getPublishPath(),
                            null, metadata, null);
                    map.put(path, true);
                } catch (IOException | TemplateException e) {
                    map.put(path, false);
                }
                handler.put("map", map).render();
            }
        } else if (file.isDirectory()) {
            handler.put("map", deal(site, path)).render();
        }
    }

    private Map<String, Boolean> deal(SysSite site, String path) {
        path = path.replace("\\", SEPARATOR).replace("//", SEPARATOR);
        Map<String, Boolean> map = new LinkedHashMap<>();
        Map<String, CmsPageMetadata> metadataMap = metadataComponent
                .getTemplateMetadataMap(siteComponent.getWebTemplateFilePath(site, path));
        List<FileInfo> list = fileComponent.getFileList(siteComponent.getWebTemplateFilePath(site, path));
        for (FileInfo fileInfo : list) {
            String filePath = path + fileInfo.getFileName();
            if (fileInfo.isDirectory()) {
                map.putAll(deal(site, filePath + SEPARATOR));
            } else {
                CmsPageMetadata metadata = metadataMap.get(fileInfo.getFileName());
                if (null != metadata && CommonUtils.notEmpty(metadata.getPublishPath())) {
                    try {
                        templateComponent.createStaticFile(site, SiteComponent.getFullFileName(site, filePath), metadata.getPublishPath(), null,
                                metadata, null);
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
    private FileComponent fileComponent;
    @Autowired
    private TemplateComponent templateComponent;
    @Autowired
    private MetadataComponent metadataComponent;

}

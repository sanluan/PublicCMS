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
import com.publiccms.entities.sys.SysSite;
import com.publiccms.logic.component.site.FileComponent;
import com.publiccms.logic.component.site.FileComponent.FileInfo;
import com.publiccms.logic.component.template.MetadataComponent;
import com.publiccms.logic.component.template.TemplateComponent;
import com.publiccms.views.pojo.entities.CmsPlaceMetadata;

import freemarker.template.TemplateException;

/**
 *
 * PublishPageDirective
 *
 */
@Component
public class PublishPlaceDirective extends AbstractTaskDirective {

    @Override
    public void execute(RenderHandler handler) throws IOException, Exception {
        String path = handler.getString("path", SEPARATOR);
        SysSite site = getSite(handler);
        if (site.isUseSsi()) {
            String fullPath = siteComponent.getWebTemplateFilePath(site, TemplateComponent.INCLUDE_DIRECTORY + SEPARATOR + path);
            File file = new File(fullPath);
            if (file.isFile()) {
                Map<String, Boolean> map = new LinkedHashMap<>();
                CmsPlaceMetadata metadata = metadataComponent
                        .getPlaceMetadata(fullPath);
                try {
                    if (null == metadata) {
                        metadata = new CmsPlaceMetadata();
                    }
                    templateComponent.staticPlace(site, path, metadata);
                    map.put(path, true);
                } catch (IOException | TemplateException e) {
                    map.put(path, false);
                }
                handler.put("map", map).render();
            } else if (file.isDirectory()) {
                handler.put("map", deal(site, path)).render();
            }
        }
    }

    private Map<String, Boolean> deal(SysSite site, String path) {
        path = path.replace("\\", SEPARATOR).replace("//", SEPARATOR);
        Map<String, Boolean> map = new LinkedHashMap<>();
        List<FileInfo> list = fileComponent
                .getFileList(siteComponent.getWebTemplateFilePath(site, TemplateComponent.INCLUDE_DIRECTORY + SEPARATOR + path));
        Map<String, CmsPlaceMetadata> metadataMap = metadataComponent
                .getPlaceMetadataMap(siteComponent.getWebTemplateFilePath(site, path));
        for (FileInfo fileInfo : list) {
            String filePath = path + fileInfo.getFileName();
            if (fileInfo.isDirectory()) {
                map.putAll(deal(site, filePath + SEPARATOR));
            } else {
                try {
                    CmsPlaceMetadata metadata = metadataMap.get(fileInfo.getFileName());
                    if (null == metadata) {
                        metadata = new CmsPlaceMetadata();
                    }
                    templateComponent.staticPlace(site, filePath, metadata);
                    map.put(filePath, true);
                } catch (IOException | TemplateException e) {
                    map.put(filePath, false);
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

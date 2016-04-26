package com.publiccms.views.directive.task;

import static com.publiccms.logic.component.SiteComponent.getFullFileName;
import static com.publiccms.logic.component.TemplateComponent.INCLUDE_DIRECTORY;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.publiccms.common.base.AbstractTaskDirective;
import com.publiccms.entities.sys.SysSite;
import com.publiccms.logic.component.FileComponent;
import com.publiccms.logic.component.FileComponent.FileInfo;
import com.publiccms.logic.component.MetadataComponent;
import com.publiccms.logic.component.TemplateComponent;
import com.publiccms.views.pojo.CmsPageMetadata;
import com.publiccms.views.pojo.CmsPlaceMetadata;
import com.sanluan.common.handler.RenderHandler;

import freemarker.template.TemplateException;

@Component
public class PublishPageDirective extends AbstractTaskDirective {
    @Override
    public void execute(RenderHandler handler) throws IOException, Exception {
        String path = handler.getString("path", SEPARATOR);
        List<FileInfo> list = fileComponent.getFileList(siteComponent.getWebTemplateFilePath(getSite(handler), path));
        handler.put("map", deal(getSite(handler), path, list)).render();
    }

    private Map<String, Boolean> deal(SysSite site, String path, List<FileInfo> list) {
        path = path.replace("\\", SEPARATOR).replace("//", SEPARATOR);
        Map<String, CmsPageMetadata> metadataMap = metadataComponent.getTemplateMetadataMap(siteComponent.getWebTemplateFilePath(
                site, path));
        Map<String, Boolean> map = new LinkedHashMap<String, Boolean>();
        for (FileInfo fileInfo : list) {
            String filePath = path + fileInfo.getFileName();
            if (fileInfo.isDirectory()) {
                map.putAll(deal(site, filePath + SEPARATOR,
                        fileComponent.getFileList(siteComponent.getWebTemplateFilePath(site, filePath))));
            } else {
                if (site.isUseSsi()) {
                    String placesPath = INCLUDE_DIRECTORY + filePath + SEPARATOR;
                    map.putAll(dealPlace(site, filePath + SEPARATOR,
                            metadataComponent.getPlaceMetadataMap(siteComponent.getWebTemplateFilePath(site, placesPath)),
                            fileComponent.getFileList(siteComponent.getWebTemplateFilePath(site, placesPath))));
                }
                CmsPageMetadata metadata = metadataMap.get(fileInfo.getFileName());
                if (notEmpty(metadata) && notEmpty(metadata.getPublishPath())) {
                    try {
                        templateComponent.createStaticFile(site, getFullFileName(site, filePath), metadata.getPublishPath(),
                                null, metadata, null);
                        map.put(filePath, true);
                    } catch (IOException | TemplateException e) {
                        map.put(filePath, false);
                    }
                }
            }
        }
        return map;
    }

    private Map<String, Boolean> dealPlace(SysSite site, String path, Map<String, CmsPlaceMetadata> metadataMap,
            List<FileInfo> fileList) {
        Map<String, Boolean> map = new LinkedHashMap<String, Boolean>();
        for (FileInfo fileInfo : fileList) {
            String filePath = path + fileInfo.getFileName();
            try {
                CmsPlaceMetadata metadata = metadataMap.get(fileInfo.getFileName());
                if (empty(metadata)) {
                    metadata = new CmsPlaceMetadata();
                }
                templateComponent.staticPlace(site, filePath, metadata);
                map.put(filePath, true);
            } catch (IOException | TemplateException e) {
                map.put(filePath, false);
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

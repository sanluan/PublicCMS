package com.publiccms.views.directive.task;

import java.io.File;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.publiccms.common.base.AbstractTaskDirective;
import com.publiccms.common.constants.CommonConstants;
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
        String path = handler.getString("path", CommonConstants.SEPARATOR);
        SysSite site = getSite(handler);
        if (site.isUseSsi()) {
            String fullPath = siteComponent.getWebTemplateFilePath(site,
                    TemplateComponent.INCLUDE_DIRECTORY + CommonConstants.SEPARATOR + path);
            File file = new File(fullPath);
            if (file.isFile()) {
                Map<String, Boolean> map = new LinkedHashMap<>();
                CmsPlaceMetadata metadata = metadataComponent.getPlaceMetadata(file.getName());
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
        path = path.replace("\\", CommonConstants.SEPARATOR).replace("//", CommonConstants.SEPARATOR);
        Map<String, Boolean> map = new LinkedHashMap<>();
        String realPath = siteComponent.getWebTemplateFilePath(site,
                TemplateComponent.INCLUDE_DIRECTORY + CommonConstants.SEPARATOR + path);
        List<FileInfo> list = fileComponent.getFileList(realPath);
        Map<String, CmsPlaceMetadata> metadataMap = metadataComponent.getPlaceMetadataMap(realPath);
        for (FileInfo fileInfo : list) {
            String filePath = path + fileInfo.getFileName();
            if (fileInfo.isDirectory()) {
                map.putAll(deal(site, filePath + CommonConstants.SEPARATOR));
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

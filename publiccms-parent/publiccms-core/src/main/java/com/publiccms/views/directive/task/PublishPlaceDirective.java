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
import com.publiccms.common.tools.CmsFileUtils.FileInfo;
import com.publiccms.entities.sys.SysSite;
import com.publiccms.logic.component.template.MetadataComponent;
import com.publiccms.logic.component.template.TemplateComponent;
import com.publiccms.views.pojo.entities.CmsPageData;
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
        String filePath = siteComponent.getWebTemplateFilePath(site, TemplateComponent.INCLUDE_DIRECTORY + path);
        if (CmsFileUtils.isFile(filePath)) {
            Map<String, Boolean> map = new LinkedHashMap<>();
            try {
                CmsPlaceMetadata metadata = metadataComponent.getPlaceMetadata(filePath);
                CmsPageData data = metadataComponent.getTemplateData(filePath);
                templateComponent.staticPlace(site, path, metadata, data);
                map.put(path, true);
            } catch (IOException | TemplateException e) {
                map.put(path, false);
            }
            handler.put("map", map).render();
        } else if (CmsFileUtils.isDirectory(filePath)) {
            handler.put("map", dealDir(site, path)).render();
        }
    }

    private Map<String, Boolean> dealDir(SysSite site, String path) {
        path = path.replace("\\", CommonConstants.SEPARATOR).replace("//", CommonConstants.SEPARATOR);
        Map<String, Boolean> map = new LinkedHashMap<>();
        String realPath = siteComponent.getWebTemplateFilePath(site, TemplateComponent.INCLUDE_DIRECTORY + path);
        List<FileInfo> list = CmsFileUtils.getFileList(realPath, null);
        for (FileInfo fileInfo : list) {
            String filePath = path + fileInfo.getFileName();
            if (fileInfo.isDirectory()) {
                map.putAll(dealDir(site, filePath + CommonConstants.SEPARATOR));
            } else {
                try {
                    String realfilePath = siteComponent.getWebTemplateFilePath(site, TemplateComponent.INCLUDE_DIRECTORY + filePath);
                    CmsPlaceMetadata metadata = metadataComponent.getPlaceMetadata(realfilePath);
                    CmsPageData data = metadataComponent.getTemplateData(realfilePath);
                    templateComponent.staticPlace(site, filePath, metadata, data);
                    map.put(filePath, true);
                } catch (IOException | TemplateException e) {
                    map.put(filePath, false);
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

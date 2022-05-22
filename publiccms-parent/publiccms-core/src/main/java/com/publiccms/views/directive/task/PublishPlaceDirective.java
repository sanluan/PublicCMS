package com.publiccms.views.directive.task;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import jakarta.annotation.Resource;
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
        String filepath = siteComponent.getWebTemplateFilePath(site, TemplateComponent.INCLUDE_DIRECTORY + path);
        if (CmsFileUtils.isFile(filepath)) {
            Map<String, Boolean> map = new LinkedHashMap<>();
            try {
                CmsPlaceMetadata metadata = metadataComponent.getPlaceMetadata(filepath);
                CmsPageData data = metadataComponent.getTemplateData(filepath);
                templateComponent.staticPlace(site, path, metadata, data);
                map.put(path, true);
            } catch (IOException | TemplateException e) {
                map.put(path, false);
            }
            handler.put("map", map).render();
        } else if (CmsFileUtils.isDirectory(filepath)) {
            handler.put("map", dealDir(site, path)).render();
        }
    }

    private Map<String, Boolean> dealDir(SysSite site, String path) {
        path = path.replace("\\", CommonConstants.SEPARATOR).replace("//", CommonConstants.SEPARATOR);
        Map<String, Boolean> map = new LinkedHashMap<>();
        String realPath = siteComponent.getWebTemplateFilePath(site, TemplateComponent.INCLUDE_DIRECTORY + path);
        List<FileInfo> list = CmsFileUtils.getFileList(realPath, null);
        for (FileInfo fileInfo : list) {
            String filepath = path + fileInfo.getFileName();
            if (fileInfo.isDirectory()) {
                map.putAll(dealDir(site, filepath + CommonConstants.SEPARATOR));
            } else {
                try {
                    String realfilepath = siteComponent.getWebTemplateFilePath(site, TemplateComponent.INCLUDE_DIRECTORY + filepath);
                    CmsPlaceMetadata metadata = metadataComponent.getPlaceMetadata(realfilepath);
                    CmsPageData data = metadataComponent.getTemplateData(realfilepath);
                    templateComponent.staticPlace(site, filepath, metadata, data);
                    map.put(filepath, true);
                } catch (IOException | TemplateException e) {
                    map.put(filepath, false);
                }
            }
        }
        return map;
    }

    @Resource
    private TemplateComponent templateComponent;
    @Resource
    private MetadataComponent metadataComponent;

}

package com.publiccms.views.method;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import net.coobird.thumbnailator.Thumbnails;

import com.publiccms.logic.component.FileComponent;
import com.sanluan.common.base.BaseMethod;

import freemarker.template.TemplateModelException;

@Component
public class GetThumbMethod extends BaseMethod {

    @SuppressWarnings("unchecked")
    @Override
    public Object exec(@SuppressWarnings("rawtypes") List arguments) throws TemplateModelException {
        if (3 <= arguments.size()) {
            String imagePath = getString(0, arguments);
            Integer width = getInteger(1, arguments);
            Integer height = getInteger(2, arguments);
            if (null != imagePath && null != width && null != height) {
                String thumbPath = imagePath.substring(0, imagePath.lastIndexOf(".")) + "_" + width + "_" + height
                        + fileComponent.getSuffix(imagePath);
                String thumbRealPath = fileComponent.getUploadFilePath(thumbPath);
                if ((new File(thumbRealPath)).exists()) {
                    return thumbPath;
                } else {
                    try {
                        Thumbnails.of(fileComponent.getUploadFilePath(imagePath)).size(width, height).toFile(thumbRealPath);
                        return thumbPath;
                    } catch (IOException e) {
                        log.debug(e.getMessage());
                    }
                }
            }
        }
        return null;
    }

    @Autowired
    private FileComponent fileComponent;
}

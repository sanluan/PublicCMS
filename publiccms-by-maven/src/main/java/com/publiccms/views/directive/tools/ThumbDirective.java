package com.publiccms.views.directive.tools;

import java.io.File;
import java.io.IOException;

import net.coobird.thumbnailator.Thumbnails;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.publiccms.entities.sys.SysSite;
import com.publiccms.logic.component.FileComponent;
import com.publiccms.logic.service.sys.SysSiteService;
import com.publiccms.common.base.AbstractTemplateDirective;
import com.sanluan.common.handler.RenderHandler;

@Component
public class ThumbDirective extends AbstractTemplateDirective {

    @Override
    public void execute(RenderHandler handler) throws IOException, Exception {
        String path = handler.getString("path");
        Integer width = handler.getInteger("width");
        Integer height = handler.getInteger("height");
        SysSite site = getSite(handler);
        if (notEmpty(path) && notEmpty(width) && notEmpty(height)) {
            String thumbPath = path.substring(0, path.lastIndexOf(".")) + "_" + width + "_" + height
                    + fileComponent.getSuffix(path);
            String fulleThumbPath = siteComponent.getResourceFilePath(site, thumbPath);
            thumbPath = site.getResourcePath() + thumbPath;
            if ((new File(fulleThumbPath)).exists()) {
                handler.print(thumbPath);
            } else {
                try {
                    Thumbnails.of(siteComponent.getResourceFilePath(site, path)).size(width, height).toFile(fulleThumbPath);
                    handler.print(thumbPath);
                } catch (IOException e) {
                    handler.print(site.getResourcePath() + path);
                    log.error(e.getMessage());
                }
            }
        }
    }

    @Autowired
    private SysSiteService sysSiteService;
    @Autowired
    private FileComponent fileComponent;
}

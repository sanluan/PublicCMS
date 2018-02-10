package com.publiccms.views.directive.tools;

import java.io.File;
import java.io.IOException;

import com.publiccms.common.base.AbstractTemplateDirective;
import org.springframework.stereotype.Component;

import com.publiccms.common.handler.RenderHandler;

/**
 * 
 * DiskDirective PublicCMS磁盘空间与路径指令
 *
 */
@Component
public class DiskDirective extends AbstractTemplateDirective {

    @Override
    public void execute(RenderHandler handler) throws IOException, Exception {
        File root = new File(siteComponent.getRootPath());
        handler.put("freeSpace", root.getFreeSpace());
        handler.put("totalSpace", root.getTotalSpace());
        handler.put("usableSpace", root.getUsableSpace());
        handler.put("rootPath", root.getAbsolutePath());
        handler.render();
    }

}

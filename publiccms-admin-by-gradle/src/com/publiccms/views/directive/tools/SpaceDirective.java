package com.publiccms.views.directive.tools;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.publiccms.logic.component.FileComponent;
import com.sanluan.common.base.BaseDirective;
import com.sanluan.common.handler.RenderHandler;

/**
 * 
 * SpaceDirective 
 *
 */
@Component
public class SpaceDirective extends BaseDirective {
    @Autowired
    private FileComponent fileComponent;

    @Override
    public void execute(RenderHandler handler) throws IOException, Exception {
        Map<String, Long> map = new HashMap<String, Long>();
        File uploadFile = new File(fileComponent.getUploadFilePath(""));
        map.put("free", uploadFile.getFreeSpace());
        map.put("total", uploadFile.getTotalSpace());
        map.put("usable", uploadFile.getUsableSpace());
        handler.put("object", map).render();
    }
}

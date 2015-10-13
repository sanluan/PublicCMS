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
 * FilePathDirective 文件路径指令
 *
 */
@Component
public class FilePathDirective extends BaseDirective {
    @Autowired
    private FileComponent fileComponent;

    @Override
    public void execute(RenderHandler handler) throws IOException, Exception {
        Map<String, String> map = new HashMap<String, String>();
        map.put("uploadFilePath", new File(fileComponent.getUploadFilePath("")).getAbsolutePath());
        map.put("staticFilePath", new File(fileComponent.getStaticFilePath("")).getAbsolutePath());
        map.put("templateFilePath", new File(fileComponent.getTemplateFilePath("")).getAbsolutePath());
        handler.put("object", map).render();
    }
}

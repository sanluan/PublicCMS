package com.publiccms.views.directive.system;

// Generated 2015-5-10 17:54:56 by SourceMaker

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.publiccms.logic.component.FileComponent;
import com.publiccms.logic.component.FileComponent.FileInfo;
import com.sanluan.common.base.BaseDirective;
import com.sanluan.common.handler.RenderHandler;

@Component
public class TemplateListDirective extends BaseDirective {
    @Autowired
    private FileComponent fileComponent;

    @Override
    public void execute(RenderHandler handler) throws IOException, Exception {
        List<FileInfo> list = fileComponent.getFileList(handler.getString("path", "/"), handler.getBoolean("exclude", false));
        handler.put("list", list).render();
    }
}
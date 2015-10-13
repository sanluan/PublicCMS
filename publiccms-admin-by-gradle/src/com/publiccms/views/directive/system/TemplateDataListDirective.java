package com.publiccms.views.directive.system;

// Generated 2015-5-10 17:54:56 by SourceMaker

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.publiccms.logic.component.FileComponent;
import com.sanluan.common.base.BaseDirective;
import com.sanluan.common.handler.RenderHandler;

@Component
public class TemplateDataListDirective extends BaseDirective {
    @Autowired
    private FileComponent fileComponent;

    @Override
    public void execute(RenderHandler handler) throws IOException, Exception {
        List<Map<String, Object>> list = fileComponent.getListData(handler.getString("path", "/"));
        handler.put("list", list).render();
    }
}
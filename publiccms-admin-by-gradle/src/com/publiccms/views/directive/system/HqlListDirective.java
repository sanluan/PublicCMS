package com.publiccms.views.directive.system;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.publiccms.logic.service.system.HqlService;
import com.sanluan.common.base.BaseTemplateDirective;
import com.sanluan.common.handler.PageHandler;
import com.sanluan.common.handler.RenderHandler;

@Component
public class HqlListDirective extends BaseTemplateDirective {

    @Override
    public void execute(RenderHandler handler) throws IOException, Exception {
        PageHandler page = service.getPage(handler.getString("hql"), null, handler.getInteger("pageIndex", 1),
                handler.getInteger("count", 30));
        handler.put("page", page).render();
    }

    @Autowired
    private HqlService service;

}

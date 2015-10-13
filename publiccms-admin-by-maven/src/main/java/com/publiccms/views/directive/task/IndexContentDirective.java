package com.publiccms.views.directive.task;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.publiccms.logic.service.cms.CmsContentService;
import com.sanluan.common.base.BaseTemplateDirective;
import com.sanluan.common.handler.RenderHandler;

@Component
public class IndexContentDirective extends BaseTemplateDirective {
    @Autowired
    private CmsContentService service;

    @Override
    public void execute(RenderHandler handler) throws IOException, Exception {
        Integer id = handler.getInteger(ID);
        Integer[] ids = handler.getIntegerArray("ids");
        List<String> messageList = new ArrayList<String>();
        if (null != ids) {
            service.index(ids);
        } else if (null != id) {
            service.index(new Integer[] { id });
        } else {
            service.reCreateIndex();
            messageList.add("re create all index");
        }
        handler.put("messageList", messageList).render();
        handler.render();
    }
}

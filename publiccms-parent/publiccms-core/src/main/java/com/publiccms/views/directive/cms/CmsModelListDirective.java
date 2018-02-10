package com.publiccms.views.directive.cms;

// Generated 2015-5-10 17:54:56 by com.publiccms.common.source.SourceGenerator

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.publiccms.common.base.AbstractTemplateDirective;
import com.publiccms.common.handler.PageHandler;
import com.publiccms.common.handler.RenderHandler;
import com.publiccms.logic.component.template.ModelComponent;

/**
 *
 * CmsModelListDirective
 * 
 */
@Component
public class CmsModelListDirective extends AbstractTemplateDirective {

    @Override
    public void execute(RenderHandler handler) throws IOException, Exception {
        PageHandler page = new PageHandler(null, null, 0, null);
        page.setList(modelComponent.getList(getSite(handler), handler.getString("parentId"),
                handler.getBoolean("hasChild"), handler.getBoolean("onlyUrl"), handler.getBoolean("hasImages"),
                handler.getBoolean("hasFiles")));
        handler.put("page", page).render();
    }

    @Autowired
    private ModelComponent modelComponent;
}
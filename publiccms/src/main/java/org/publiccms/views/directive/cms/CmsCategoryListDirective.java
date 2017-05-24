package org.publiccms.views.directive.cms;

// Generated 2015-5-10 17:54:56 by com.publiccms.common.source.SourceGenerator

import java.io.IOException;

import org.publiccms.common.base.AbstractTemplateDirective;
import org.publiccms.logic.service.cms.CmsCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.publiccms.common.handler.PageHandler;
import com.publiccms.common.handler.RenderHandler;

/**
 *
 * CmsCategoryListDirective
 * 
 */
@Component
public class CmsCategoryListDirective extends AbstractTemplateDirective {

    @Override
    public void execute(RenderHandler handler) throws IOException, Exception {
        Boolean disabled = false;
        Boolean hidden = false;
        Boolean queryAll = handler.getBoolean("queryAll");
        if (handler.getBoolean("advanced", false)) {
            disabled = handler.getBoolean("disabled", false);
            hidden = handler.getBoolean("hidden");
        }
        PageHandler page = service.getPage(getSite(handler).getId(), handler.getInteger("parentId"), queryAll,
                handler.getInteger("typeId"), handler.getBoolean("allowContribute"), hidden, disabled,
                handler.getInteger("pageIndex", 1), handler.getInteger("count", 30));
        handler.put("page", page).render();
    }

    @Autowired
    private CmsCategoryService service;

}
package com.publiccms.views.directive.cms;

// Generated 2016-2-26 15:57:04 by com.publiccms.common.source.SourceGenerator

import java.io.IOException;

import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

import com.publiccms.common.base.AbstractTemplateDirective;
import com.publiccms.common.handler.PageHandler;
import com.publiccms.common.handler.RenderHandler;
import com.publiccms.logic.component.template.ModelComponent;

/**
 *
 * CmsCategoryTypeListDirective
 * 
 */
@Component
public class CmsCategoryTypeListDirective extends AbstractTemplateDirective {

    @Override
    public void execute(RenderHandler handler) throws IOException, Exception {
        PageHandler page = new PageHandler(null, null);
        page.setList(modelComponent.getCategoryTypeList(getSite(handler)));
        handler.put("page", page).render();
    }

    @Resource
    private ModelComponent modelComponent;

}
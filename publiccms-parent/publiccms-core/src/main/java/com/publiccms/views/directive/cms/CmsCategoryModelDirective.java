package com.publiccms.views.directive.cms;

// Generated 2015-5-10 17:54:56 by com.publiccms.common.source.SourceGenerator

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.publiccms.common.base.AbstractTemplateDirective;
import com.publiccms.common.handler.RenderHandler;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.entities.cms.CmsCategoryModel;
import com.publiccms.logic.service.cms.CmsCategoryModelService;

/**
 *
 * CmsCategoryModelListDirective
 * 
 */
@Component
public class CmsCategoryModelDirective extends AbstractTemplateDirective {

    @Override
    public void execute(RenderHandler handler) throws IOException, Exception {
        String modelId = handler.getString("modelId");
        Integer categoryId = handler.getInteger("categoryId");
        if (CommonUtils.notEmpty(modelId) && null != categoryId) {
            CmsCategoryModel entity = service.getEntity(modelId, categoryId);
            handler.put("object", entity).render();
        }
    }

    @Autowired
    private CmsCategoryModelService service;

}
package com.publiccms.views.directive.cms;

// Generated 2015-5-10 17:54:56 by com.publiccms.common.source.SourceGenerator

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.publiccms.common.base.AbstractTemplateDirective;
import com.publiccms.common.handler.RenderHandler;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.entities.cms.CmsCategoryModel;
import com.publiccms.entities.cms.CmsCategoryModelId;
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
        Integer categoryId = handler.getInteger("categoryId");
        if (null != categoryId) {
            String modelId = handler.getString("modelId");
            if (CommonUtils.notEmpty(modelId)) {
                CmsCategoryModelId id = new CmsCategoryModelId(categoryId, modelId);
                CmsCategoryModel entity = service.getEntity(id);
                if (null != entity) {
                    handler.put("object", entity).render();
                }
            } else {
                String[] modelIds = handler.getStringArray("modelIds");
                if (CommonUtils.notEmpty(modelIds)) {
                    CmsCategoryModelId[] entityIds = new CmsCategoryModelId[modelIds.length];
                    for (int i = 0; i < modelIds.length; i++) {
                        entityIds[i] = new CmsCategoryModelId(categoryId, modelIds[i]);
                    }
                    List<CmsCategoryModel> entityList = service.getEntitys(entityIds);
                    Map<String, CmsCategoryModel> map = CommonUtils.listToMap(entityList, k -> k.getId().getModelId(),
                            null, null);
                    handler.put("map", map).render();
                }
            }
        }
    }

    @Autowired
    private CmsCategoryModelService service;

}
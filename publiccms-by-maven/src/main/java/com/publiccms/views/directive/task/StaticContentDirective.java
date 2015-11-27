package com.publiccms.views.directive.task;

import static org.apache.commons.lang3.ArrayUtils.isNotEmpty;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.publiccms.entities.cms.CmsCategory;
import com.publiccms.entities.cms.CmsCategoryModel;
import com.publiccms.entities.cms.CmsContent;
import com.publiccms.entities.cms.CmsModel;
import com.publiccms.logic.component.FileComponent;
import com.publiccms.logic.component.FileComponent.StaticResult;
import com.publiccms.logic.service.cms.CmsCategoryModelService;
import com.publiccms.logic.service.cms.CmsCategoryService;
import com.publiccms.logic.service.cms.CmsContentService;
import com.publiccms.logic.service.cms.CmsModelService;
import com.sanluan.common.base.BaseTemplateDirective;
import com.sanluan.common.handler.RenderHandler;

@Component
public class StaticContentDirective extends BaseTemplateDirective {
    @Autowired
    private FileComponent fileComponent;
    @Autowired
    private CmsContentService service;
    @Autowired
    private CmsCategoryModelService categoryModelService;
    @Autowired
    private CmsCategoryService categoryService;
    @Autowired
    private CmsModelService modelService;

    @Override
    public void execute(RenderHandler handler) throws IOException, Exception {
        Integer id = handler.getInteger(ID);
        if (null != id) {
            CmsContent entity = service.getEntity(id);
            if (!deal(entity)) {
                List<String> messageList = new ArrayList<String>();
                messageList.add(entity.getId().toString());
                handler.put("messageList", messageList);
            }
            handler.render();
        } else {
            Integer[] ids = handler.getIntegerArray("ids");
            if (isNotEmpty(ids)) {
                List<CmsContent> entityList = service.getEntitys(ids);
                List<String> messageList = new ArrayList<String>();
                for (CmsContent entity : entityList) {
                    if (!deal(entity)) {
                        messageList.add(entity.getId().toString());
                    }
                }
                handler.put("messageList", messageList);
                handler.render();
            }
        }
    }

    private boolean deal(CmsContent entity) {
        if (null != entity) {
            CmsCategoryModel categoryModel = categoryModelService.getEntity(entity.getModelId(), entity.getCategoryId());
            CmsModel cmsModel = modelService.getEntity(entity.getModelId());
            CmsCategory category = categoryService.getEntity(entity.getCategoryId());
            if (null != cmsModel && null != categoryModel && null != category && !cmsModel.isIsUrl()) {
                StaticResult result = fileComponent
                        .createContentHtml(entity, category, cmsModel, categoryModel.getTemplatePath());
                if (result.getResult()) {
                    entity = service.updateUrl(entity.getId(), result.getFilePath());
                    return true;
                }
            }
        }
        return false;
    }
}

package com.publiccms.views.directive.task;

import static org.apache.commons.lang3.ArrayUtils.isNotEmpty;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.publiccms.entities.cms.CmsCategory;
import com.publiccms.logic.component.FileComponent;
import com.publiccms.logic.component.FileComponent.StaticResult;
import com.publiccms.logic.service.cms.CmsCategoryService;
import com.sanluan.common.base.BaseTemplateDirective;
import com.sanluan.common.handler.RenderHandler;

@Component
public class StaticCategoryDirective extends BaseTemplateDirective {
    @Autowired
    private FileComponent fileComponent;
    @Autowired
    private CmsCategoryService service;

    @Override
    public void execute(RenderHandler handler) throws IOException, Exception {
        Integer id = handler.getInteger(ID);
        Integer totalPage = handler.getInteger("totalPage");
        if (null != id) {
            CmsCategory entity = service.getEntity(id);
            if (!deal(entity, totalPage)) {
                List<String> messageList = new ArrayList<String>();
                messageList.add(entity.getId().toString());
                handler.put("messageList", messageList);
            }
            handler.render();
        } else {
            Integer[] ids = handler.getIntegerArray("ids");
            if (isNotEmpty(ids)) {
                List<CmsCategory> entityList = service.getEntitys(ids);
                List<String> messageList = new ArrayList<String>();
                for (CmsCategory entity : entityList) {
                    if (!deal(entity, totalPage)) {
                        messageList.add(entity.getId().toString());
                    }
                }
                handler.put("messageList", messageList);
                handler.render();
            }
        }
    }

    private boolean deal(CmsCategory entity, Integer totalPage) {
        if (null != entity) {
            if (isNotBlank(entity.getPath())) {
                StaticResult result = fileComponent.createCategoryHtml(entity, entity.getTemplatePath(), entity.getPath(),
                        totalPage);
                if (result.getResult()) {
                    entity = service.updateUrl(entity.getId(), result.getFilePath());
                }
            }
        }
        return false;
    }
}

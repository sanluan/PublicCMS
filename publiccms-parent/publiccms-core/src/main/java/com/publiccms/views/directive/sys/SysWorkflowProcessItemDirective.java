package com.publiccms.views.directive.sys;

// Generated 2023-8-16 by com.publiccms.common.generator.SourceGenerator

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.publiccms.common.base.AbstractTemplateDirective;
import com.publiccms.common.handler.RenderHandler;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.entities.sys.SysWorkflowProcessItem;
import com.publiccms.entities.sys.SysWorkflowProcessItemId;
import com.publiccms.logic.service.sys.SysWorkflowProcessItemService;
import com.publiccms.logic.service.sys.SysWorkflowProcessService;

import freemarker.template.TemplateException;
import jakarta.annotation.Resource;

/**
 *
 * SysWorkflowProcessDirective
 * 
 */
@Component
public class SysWorkflowProcessItemDirective extends AbstractTemplateDirective {

    @Override
    public void execute(RenderHandler handler) throws IOException, TemplateException {
        String itemType = handler.getString("itemType");

        if (CommonUtils.notEmpty(itemType)) {
            String itemId = handler.getString("itemId");
            if (CommonUtils.notEmpty(itemId)) {
                SysWorkflowProcessItem entity = workflowProcessItemService
                        .getEntity(new SysWorkflowProcessItemId(itemType, itemId));
                if (null != entity) {
                    handler.put("object", entity).render();
                }
            } else {
                String[] itemIds = handler.getStringArray("itemIds");
                if (CommonUtils.notEmpty(itemIds)) {
                    SysWorkflowProcessItemId[] entityIds = new SysWorkflowProcessItemId[itemIds.length];
                    for (int i = 0; i < itemIds.length; i++) {
                        entityIds[i] = new SysWorkflowProcessItemId(itemType, itemIds[i]);
                    }
                    List<SysWorkflowProcessItem> entityList = workflowProcessItemService.getEntitys(entityIds);
                    Map<String, SysWorkflowProcessItem> map = CommonUtils.listToMapSorted(entityList,
                            k -> String.valueOf(k.getId().getItemId()), itemIds);
                    handler.put("map", map).render();
                }
            }
        }
    }

    @Override
    public boolean needAppToken() {
        return true;
    }

    @Resource
    private SysWorkflowProcessItemService workflowProcessItemService;
    @Resource
    private SysWorkflowProcessService service;

}

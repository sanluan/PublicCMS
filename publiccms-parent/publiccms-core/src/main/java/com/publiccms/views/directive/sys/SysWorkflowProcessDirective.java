package com.publiccms.views.directive.sys;

// Generated 2023-8-16 by com.publiccms.common.generator.SourceGenerator

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.publiccms.common.base.AbstractTemplateDirective;
import com.publiccms.common.handler.RenderHandler;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.entities.sys.SysSite;
import com.publiccms.entities.sys.SysWorkflowProcess;
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
public class SysWorkflowProcessDirective extends AbstractTemplateDirective {

    @Override
    public void execute(RenderHandler handler) throws IOException, TemplateException {
        Long id = handler.getLong("id");
        String itemType = handler.getString("itemType");

        SysSite site = getSite(handler);
        if (CommonUtils.notEmpty(id)) {
            SysWorkflowProcess entity = service.getEntity(id);
            if (null != entity) {
                handler.put("object", entity).render();
            }
        } else if (CommonUtils.notEmpty(itemType)) {
            String itemId = handler.getString("itemId");
            if (CommonUtils.notEmpty(itemId)) {
                SysWorkflowProcessItem item = workflowProcessItemService
                        .getEntity(new SysWorkflowProcessItemId(itemType, itemId));
                if (null != item) {
                    SysWorkflowProcess entity = service.getEntity(item.getProcessId());
                    if (null != entity) {
                        handler.put("object", entity).render();
                    }
                }
            } else {
                String[] itemIds = handler.getStringArray("itemIds");
                if (CommonUtils.notEmpty(itemIds)) {
                    SysWorkflowProcessItemId[] entityIds = new SysWorkflowProcessItemId[itemIds.length];
                    for (int i = 0; i < itemIds.length; i++) {
                        entityIds[i] = new SysWorkflowProcessItemId(itemType, itemIds[i]);
                    }
                    List<SysWorkflowProcessItem> itemList = workflowProcessItemService.getEntitys(entityIds);
                    Long[] ids = new Long[itemList.size()];
                    for (int i = 0; i < ids.length; i++) {
                        ids[i] = itemList.get(i).getProcessId();
                    }
                    List<SysWorkflowProcess> entityList = service.getEntitys(ids);

                    Map<String, SysWorkflowProcess> map = CommonUtils.listToMapSorted(entityList,
                            k -> String.valueOf(k.getItemId()), itemIds);
                    handler.put("map", map).render();
                }
            }
        } else {
            Long[] ids = handler.getLongArray("ids");
            if (CommonUtils.notEmpty(ids)) {
                List<SysWorkflowProcess> entityList = service.getEntitys(ids);
                Map<String, SysWorkflowProcess> map = CommonUtils.listToMapSorted(entityList, k -> k.getId().toString(), null,
                        ids, entity -> site.getId() == entity.getSiteId());
                handler.put("map", map).render();
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

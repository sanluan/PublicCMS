package com.publiccms.views.directive.sys;

// Generated 2023-8-16 by com.publiccms.common.generator.SourceGenerator

import java.io.IOException;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.publiccms.entities.sys.SysWorkflow;
import com.publiccms.logic.service.sys.SysWorkflowService;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.common.base.AbstractTemplateDirective;
import com.publiccms.common.handler.RenderHandler;

import freemarker.template.TemplateException;
/**
 *
 * SysWorkflowDirective
 * 
 */
@Component
public class SysWorkflowDirective extends AbstractTemplateDirective {

    @Override
    public void execute(RenderHandler handler) throws IOException, TemplateException {
        Integer id = handler.getInteger("id");
        if (CommonUtils.notEmpty(id)) {
            SysWorkflow entity = service.getEntity(id);
            if (null != entity) {
                handler.put("object", entity).render();
            }
        } else {
            Integer[] ids = handler.getIntegerArray("ids");
            if (CommonUtils.notEmpty(ids)) {
                List<SysWorkflow> entityList = service.getEntitys(ids);
                Map<String, SysWorkflow> map = CommonUtils.listToMap(entityList, k -> k.getId().toString(), null, null);
                handler.put("map", map).render();
            }
        }
    }

    @Resource
    private SysWorkflowService service;

}

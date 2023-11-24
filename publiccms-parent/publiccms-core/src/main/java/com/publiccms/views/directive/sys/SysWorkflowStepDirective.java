package com.publiccms.views.directive.sys;

// Generated 2023-8-16 by com.publiccms.common.generator.SourceGenerator

import java.io.IOException;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.publiccms.entities.sys.SysWorkflowStep;
import com.publiccms.logic.service.sys.SysWorkflowStepService;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.common.base.AbstractTemplateDirective;
import com.publiccms.common.handler.RenderHandler;

import freemarker.template.TemplateException;
/**
 *
 * SysWorkflowStepDirective
 * 
 */
@Component
public class SysWorkflowStepDirective extends AbstractTemplateDirective {

    @Override
    public void execute(RenderHandler handler) throws IOException, TemplateException {
        Long id = handler.getLong("id");
        if (CommonUtils.notEmpty(id)) {
            SysWorkflowStep entity = service.getEntity(id);
            if (null != entity) {
                handler.put("object", entity).render();
            }
        } else {
            Long[] ids = handler.getLongArray("ids");
            if (CommonUtils.notEmpty(ids)) {
                List<SysWorkflowStep> entityList = service.getEntitys(ids);
                Map<String, SysWorkflowStep> map = CommonUtils.listToMap(entityList, k -> k.getId().toString(), null, null);
                handler.put("map", map).render();
            }
        }
    }

    @Resource
    private SysWorkflowStepService service;

}

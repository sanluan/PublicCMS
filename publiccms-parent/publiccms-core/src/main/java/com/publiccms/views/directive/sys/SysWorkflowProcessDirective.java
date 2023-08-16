package com.publiccms.views.directive.sys;

// Generated 2023-8-16 by com.publiccms.common.generator.SourceGenerator

import java.io.IOException;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.publiccms.entities.sys.SysWorkflowProcess;
import com.publiccms.logic.service.sys.SysWorkflowProcessService;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.common.base.AbstractTemplateDirective;
import com.publiccms.common.handler.RenderHandler;

import freemarker.template.TemplateException;
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
        if (CommonUtils.notEmpty(id)) {
            SysWorkflowProcess entity = service.getEntity(id);
            if (null != entity) {
                handler.put("object", entity).render();
            }
        } else {
            Long[] ids = handler.getLongArray("ids");
            if (CommonUtils.notEmpty(ids)) {
                List<SysWorkflowProcess> entityList = service.getEntitys(ids);
                Map<String, SysWorkflowProcess> map = CommonUtils.listToMap(entityList, k -> k.getId().toString(), null, null);
                handler.put("map", map).render();
            }
        }
    }

    @Resource
    private SysWorkflowProcessService service;

}

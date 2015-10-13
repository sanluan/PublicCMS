package com.publiccms.views.directive.system;

// Generated 2015-7-3 16:23:02 by SourceMaker

import static org.apache.commons.lang3.ArrayUtils.isNotEmpty;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.publiccms.entities.system.SystemTask;
import com.publiccms.logic.service.system.SystemTaskService;
import com.sanluan.common.base.BaseDirective;
import com.sanluan.common.handler.RenderHandler;

@Component
public class SystemTaskDirective extends BaseDirective {

    @Override
    public void execute(RenderHandler handler) throws IOException, Exception {
        Integer id = handler.getInteger(ID);
        if (null != id) {
            SystemTask entity= service.getEntity(id);
            handler.put("object", entity).renderIfNotNull(entity);
        } else {
            Integer[] ids = handler.getIntegerArray("ids");
            if (isNotEmpty(ids)) {
                List<SystemTask> entityList = service.getEntitys(ids);
                Map<String, SystemTask> map = new HashMap<String, SystemTask>();
                for (SystemTask entity: entityList) {
                    map.put(String.valueOf(entity.getId()), entity);
                }
                handler.put("map", map).render();
            }
        }
    }

    @Autowired
    private SystemTaskService service;

}

package com.publiccms.views.directive.system;

// Generated 2015-7-20 11:46:39 by SourceMaker

import static org.apache.commons.lang3.ArrayUtils.isNotEmpty;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.publiccms.entities.system.SystemRole;
import com.publiccms.logic.service.system.SystemRoleService;
import com.sanluan.common.base.BaseDirective;
import com.sanluan.common.handler.RenderHandler;

@Component
public class SystemRoleDirective extends BaseDirective {

    @Override
    public void execute(RenderHandler handler) throws IOException, Exception {
        Integer id = handler.getInteger(ID);
        if (null != id) {
            SystemRole entity = service.getEntity(id);
            handler.put("object", entity).renderIfNotNull(entity);
        } else {
            Integer[] ids = handler.getIntegerArray("ids");
            if (isNotEmpty(ids)) {
                List<SystemRole> entityList = service.getEntitys(ids);
                Map<String, SystemRole> map = new HashMap<String, SystemRole>();
                for (SystemRole entity : entityList) {
                    map.put(String.valueOf(entity.getId()), entity);
                }
                handler.put("map", map).render();
            }
        }
    }

    @Autowired
    private SystemRoleService service;

}

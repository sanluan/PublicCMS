package com.publiccms.views.directive.sys;

// Generated 2015-7-20 11:46:39 by SourceMaker

import static org.apache.commons.lang3.ArrayUtils.isNotEmpty;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.publiccms.entities.sys.SysRole;
import com.publiccms.logic.service.sys.SysRoleService;
import com.sanluan.common.base.BaseDirective;
import com.sanluan.common.handler.RenderHandler;

@Component
public class SysRoleDirective extends BaseDirective {

    @Override
    public void execute(RenderHandler handler) throws IOException, Exception {
        Integer id = handler.getInteger(ID);
        if (null != id) {
            SysRole entity = service.getEntity(id);
            handler.put("object", entity).renderIfNotNull(entity);
        } else {
            Integer[] ids = handler.getIntegerArray("ids");
            if (isNotEmpty(ids)) {
                List<SysRole> entityList = service.getEntitys(ids);
                Map<String, SysRole> map = new HashMap<String, SysRole>();
                for (SysRole entity : entityList) {
                    map.put(String.valueOf(entity.getId()), entity);
                }
                handler.put("map", map).render();
            }
        }
    }

    @Autowired
    private SysRoleService service;

}

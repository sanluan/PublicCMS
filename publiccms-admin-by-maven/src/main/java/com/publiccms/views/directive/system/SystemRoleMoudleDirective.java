package com.publiccms.views.directive.system;

// Generated 2015-7-22 13:48:39 by SourceMaker

import static org.apache.commons.lang3.ArrayUtils.isNotEmpty;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.publiccms.entities.system.SystemRoleMoudle;
import com.publiccms.logic.service.system.SystemRoleMoudleService;
import com.publiccms.logic.service.system.SystemRoleService;
import com.sanluan.common.base.BaseDirective;
import com.sanluan.common.handler.RenderHandler;

@Component
public class SystemRoleMoudleDirective extends BaseDirective {

    @Override
    public void execute(RenderHandler handler) throws IOException, Exception {
        Integer[] roleIds = handler.getIntegerArray("roleIds");
        Integer moudleId = handler.getInteger("moudleId");
        if (null != roleIds) {
            if (null != moudleId) {
                SystemRoleMoudle entity = service.getEntity(roleIds, moudleId);
                handler.put("object", entity).renderIfNotNull(entity);
            } else {
                Integer[] moudleIds = handler.getIntegerArray("moudleIds");
                if (isNotEmpty(moudleIds)) {
                    Map<String, Boolean> map = new HashMap<String, Boolean>();
                    if (systemRoleService.ownsAllRight(roleIds)) {
                        for (Integer id : moudleIds) {
                            map.put(String.valueOf(id), true);
                        }
                    } else {
                        List<SystemRoleMoudle> entityList = service.getEntitys(roleIds, moudleIds);
                        for (SystemRoleMoudle entity : entityList) {
                            map.put(String.valueOf(entity.getMoudleId()), true);
                        }
                    }
                    handler.put("map", map).render();
                }
            }
        }
    }

    @Autowired
    private SystemRoleService systemRoleService;
    @Autowired
    private SystemRoleMoudleService service;

}
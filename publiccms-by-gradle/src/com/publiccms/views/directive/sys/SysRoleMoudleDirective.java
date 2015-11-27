package com.publiccms.views.directive.sys;

// Generated 2015-7-22 13:48:39 by SourceMaker

import static org.apache.commons.lang3.ArrayUtils.isNotEmpty;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.publiccms.entities.sys.SysRoleMoudle;
import com.publiccms.logic.service.sys.SysRoleMoudleService;
import com.publiccms.logic.service.sys.SysRoleService;
import com.sanluan.common.base.BaseDirective;
import com.sanluan.common.handler.RenderHandler;

@Component
public class SysRoleMoudleDirective extends BaseDirective {

    @Override
    public void execute(RenderHandler handler) throws IOException, Exception {
        Integer[] roleIds = handler.getIntegerArray("roleIds");
        Integer moudleId = handler.getInteger("moudleId");
        if (null != roleIds) {
            if (null != moudleId) {
                SysRoleMoudle entity = service.getEntity(roleIds, moudleId);
                handler.put("object", entity).renderIfNotNull(entity);
            } else {
                Integer[] moudleIds = handler.getIntegerArray("moudleIds");
                if (isNotEmpty(moudleIds)) {
                    Map<String, Boolean> map = new HashMap<String, Boolean>();
                    if (sysRoleService.ownsAllRight(roleIds)) {
                        for (Integer id : moudleIds) {
                            map.put(String.valueOf(id), true);
                        }
                    } else {
                        List<SysRoleMoudle> entityList = service.getEntitys(roleIds, moudleIds);
                        for (SysRoleMoudle entity : entityList) {
                            map.put(String.valueOf(entity.getMoudleId()), true);
                        }
                    }
                    handler.put("map", map).render();
                }
            }
        }
    }

    @Autowired
    private SysRoleService sysRoleService;
    @Autowired
    private SysRoleMoudleService service;

}
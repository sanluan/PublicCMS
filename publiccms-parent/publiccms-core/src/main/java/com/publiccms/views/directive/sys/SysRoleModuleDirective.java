package com.publiccms.views.directive.sys;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.publiccms.common.base.AbstractTemplateDirective;
import com.publiccms.common.handler.RenderHandler;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.entities.sys.SysRoleModule;
import com.publiccms.logic.service.sys.SysRoleModuleService;
import com.publiccms.logic.service.sys.SysRoleService;

/**
 *
 * SysRoleModuleDirective
 * 
 */
@Component
public class SysRoleModuleDirective extends AbstractTemplateDirective {

    @Override
    public void execute(RenderHandler handler) throws IOException, Exception {
        Integer[] roleIds = handler.getIntegerArray("roleIds");
        Integer moduleId = handler.getInteger("moduleId");
        if (CommonUtils.notEmpty(roleIds)) {
            if (CommonUtils.notEmpty(moduleId)) {
                SysRoleModule entity = service.getEntity(roleIds, moduleId);
                handler.put("object", entity).render();
            } else {
                Integer[] moduleIds = handler.getIntegerArray("moduleIds");
                if (CommonUtils.notEmpty(moduleIds)) {
                    Map<String, Boolean> map = new LinkedHashMap<>();
                    if (sysRoleService.showAllModule(roleIds)) {
                        for (Integer id : moduleIds) {
                            map.put(String.valueOf(id), true);
                        }
                    } else {
                        for (SysRoleModule entity : service.getEntitys(roleIds, moduleIds)) {
                            map.put(String.valueOf(entity.getId().getModuleId()), true);
                        }
                    }
                    handler.put("map", map).render();
                }
            }
        }
    }

    @Override
    public boolean needAppToken() {
        return true;
    }

    @Autowired
    private SysRoleService sysRoleService;
    @Autowired
    private SysRoleModuleService service;

}
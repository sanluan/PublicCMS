package org.publiccms.views.directive.sys;

// Generated 2015-7-22 13:48:39 by com.publiccms.common.source.SourceGenerator
import static com.publiccms.common.tools.CommonUtils.notEmpty;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

import org.publiccms.common.base.AbstractTemplateDirective;
import org.publiccms.entities.sys.SysRoleMoudle;
import org.publiccms.logic.service.sys.SysRoleMoudleService;
import org.publiccms.logic.service.sys.SysRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.publiccms.common.handler.RenderHandler;

/**
 *
 * SysRoleMoudleDirective
 * 
 */
@Component
public class SysRoleMoudleDirective extends AbstractTemplateDirective {

    @Override
    public void execute(RenderHandler handler) throws IOException, Exception {
        Integer[] roleIds = handler.getIntegerArray("roleIds");
        Integer moudleId = handler.getInteger("moudleId");
        if (notEmpty(roleIds)) {
            if (notEmpty(moudleId)) {
                SysRoleMoudle entity = service.getEntity(roleIds, moudleId);
                handler.put("object", entity).render();
            } else {
                Integer[] moudleIds = handler.getIntegerArray("moudleIds");
                if (notEmpty(moudleIds)) {
                    Map<String, Boolean> map = new LinkedHashMap<>();
                    if (sysRoleService.showAllMoudle(roleIds)) {
                        for (Integer id : moudleIds) {
                            map.put(String.valueOf(id), true);
                        }
                    } else {
                        for (SysRoleMoudle entity : service.getEntitys(roleIds, moudleIds)) {
                            map.put(String.valueOf(entity.getId().getMoudleId()), true);
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
    private SysRoleMoudleService service;

}
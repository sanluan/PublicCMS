package com.publiccms.views.directive.sys;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.publiccms.common.base.AbstractTemplateDirective;
import com.publiccms.entities.sys.SysSite;
import com.publiccms.entities.sys.SysUser;
import com.publiccms.logic.service.sys.SysUserService;
import com.sanluan.common.handler.RenderHandler;

@Component
public class SysUserDirective extends AbstractTemplateDirective {

    @Override
    public void execute(RenderHandler handler) throws IOException, Exception {
        Long id = handler.getLong("id");
        SysSite site = getSite(handler);
        if (notEmpty(id)) {
            SysUser entity = service.getEntity(id);
            entity.setPassword(null);
            if (notEmpty(entity) && site.getId() == entity.getSiteId()) {
                entity.setPassword(null);
                handler.put("object", entity).render();
            }
        } else {
            Long[] ids = handler.getLongArray("ids");
            if (notEmpty(ids)) {
                List<SysUser> entityList = service.getEntitys(ids);
                Map<String, SysUser> map = new LinkedHashMap<String, SysUser>();
                for (SysUser entity : entityList) {
                    if (site.getId() == entity.getSiteId()) {
                        entity.setPassword(null);
                        map.put(String.valueOf(entity.getId()), entity);
                    }
                }
                handler.put("map", map).render();
            }
        }
    }

    @Autowired
    private SysUserService service;

}

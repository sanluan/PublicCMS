package com.publiccms.views.directive.sys;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.publiccms.common.base.AbstractTemplateDirective;
import com.publiccms.common.handler.RenderHandler;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.entities.sys.SysRoleAuthorized;
import com.publiccms.entities.sys.SysRoleAuthorizedId;
import com.publiccms.logic.service.sys.SysRoleAuthorizedService;
import com.publiccms.logic.service.sys.SysRoleService;

/**
 *
 * SysAuthorizedDirective
 * 
 */
@Component
public class SysAuthorizedDirective extends AbstractTemplateDirective {

    @Override
    public void execute(RenderHandler handler) throws IOException, Exception {
        Integer[] roleIds = handler.getIntegerArray("roleIds");
        String url = handler.getString("url");
        String[] urls = handler.getStringArray("urls");
        if (CommonUtils.notEmpty(roleIds)) {
            if (CommonUtils.notEmpty(url) && sysRoleService.showAllModule(roleIds)) {
                handler.put("object", true).render();
            } else if (CommonUtils.notEmpty(url)) {
                SysRoleAuthorizedId[] ids = new SysRoleAuthorizedId[roleIds.length];
                for (int i = 0; i < roleIds.length; i++) {
                    ids[i] = new SysRoleAuthorizedId(roleIds[i], url);
                }
                if (CommonUtils.notEmpty(service.getEntitys(ids))) {
                    handler.put("object", true).render();
                }
            } else if (CommonUtils.notEmpty(urls)) {
                Map<String, Boolean> map = new LinkedHashMap<>();
                if (sysRoleService.showAllModule(roleIds)) {
                    for (String u : urls) {
                        map.put(u, true);
                    }
                } else {
                    SysRoleAuthorizedId[] ids = new SysRoleAuthorizedId[urls.length * roleIds.length];
                    int n = 0;
                    for (int i = 0; i < urls.length; i++) {
                        map.put(urls[i], false);
                        for (int j = 0; j < roleIds.length; j++) {
                            ids[n++] = new SysRoleAuthorizedId(roleIds[j], urls[i]);
                        }
                    }
                    List<SysRoleAuthorized> entityList = service.getEntitys(ids);
                    for (SysRoleAuthorized entity : entityList) {
                        map.put(entity.getId().getUrl(), true);
                    }
                }
                handler.put("map", map).render();
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
    private SysRoleAuthorizedService service;

}
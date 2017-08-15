package org.publiccms.views.directive.sys;

// Generated 2015-7-22 13:48:39 by com.publiccms.common.source.SourceGenerator
import static com.publiccms.common.tools.CommonUtils.notEmpty;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

import org.publiccms.common.base.AbstractTemplateDirective;
import org.publiccms.entities.sys.SysRoleAuthorized;
import org.publiccms.entities.sys.SysRoleAuthorizedId;
import org.publiccms.logic.service.sys.SysRoleAuthorizedService;
import org.publiccms.logic.service.sys.SysRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.publiccms.common.handler.RenderHandler;

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
        if (notEmpty(roleIds)) {
            if (notEmpty(url) && sysRoleService.showAllMoudle(roleIds)) {
                handler.put("object", true).render();
            } else if (notEmpty(url)) {
                SysRoleAuthorizedId[] ids = new SysRoleAuthorizedId[roleIds.length];
                for (int i = 0; i < roleIds.length; i++) {
                    ids[i] = new SysRoleAuthorizedId(roleIds[i], url);
                }
                if (notEmpty(service.getEntitys(ids))) {
                    handler.put("object", true).render();
                }
            } else if (notEmpty(urls)) {
                Map<String, Boolean> map = new LinkedHashMap<>();
                if (sysRoleService.showAllMoudle(roleIds)) {
                    for (String u : urls) {
                        map.put(u, true);
                    }
                } else {
                    SysRoleAuthorizedId[] ids = new SysRoleAuthorizedId[urls.length * roleIds.length];
                    int n=0;
                    for (int i = 0; i < urls.length; i++) {
                        map.put(urls[i], false);
                        for (int j = 0; j < roleIds.length; j++) {
                            ids[n++] = new SysRoleAuthorizedId(roleIds[j], urls[i]);
                        }
                    }
                    for (SysRoleAuthorized entity : service.getEntitys(ids)) {
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
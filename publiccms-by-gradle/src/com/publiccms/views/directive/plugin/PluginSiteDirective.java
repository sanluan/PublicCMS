package com.publiccms.views.directive.plugin;

// Generated 2016-3-1 17:24:24 by com.sanluan.common.source.SourceMaker

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.publiccms.common.base.AbstractTemplateDirective;
import com.publiccms.entities.plugin.PluginSite;
import com.publiccms.entities.sys.SysSite;
import com.publiccms.logic.service.plugin.PluginSiteService;
import com.sanluan.common.handler.RenderHandler;

@Component
public class PluginSiteDirective extends AbstractTemplateDirective {

    @Override
    public void execute(RenderHandler handler) throws IOException, Exception {
        String pluginCode = handler.getString("pluginCode");
        SysSite site = getSite(handler);
        if (notEmpty(pluginCode)) {
            handler.put("object", service.getEntity(site.getId(), pluginCode)).render();
        } else {
            String[] pluginCodes = handler.getStringArray("pluginCodes");
            if (notEmpty(pluginCodes)) {
                List<PluginSite> entityList = service.getEntitys(site.getId(), pluginCodes);
                Map<String, PluginSite> map = new LinkedHashMap<String, PluginSite>();
                for (PluginSite entity : entityList) {
                    map.put(entity.getPluginCode(), entity);
                }
                handler.put("map", map).render();
            }
        }
    }

    @Autowired
    private PluginSiteService service;

}

package com.publiccms.views.directive.plugin;

// Generated 2016-3-3 17:43:34 by com.sanluan.common.source.SourceMaker

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.publiccms.entities.plugin.PluginVoteItem;
import com.publiccms.logic.service.plugin.PluginVoteItemService;
import com.publiccms.common.base.AbstractTemplateDirective;
import com.sanluan.common.handler.RenderHandler;

@Component
public class PluginVoteItemDirective extends AbstractTemplateDirective {

    @Override
    public void execute(RenderHandler handler) throws IOException, Exception {
        Long id = handler.getLong("id");
        if (notEmpty(id)) {
            handler.put("object", service.getEntity(id)).render();
        } else {
            Long[] ids = handler.getLongArray("ids");
            if (notEmpty(ids)) {
                List<PluginVoteItem> entityList = service.getEntitys(ids);
                Map<String, PluginVoteItem> map = new LinkedHashMap<String, PluginVoteItem>();
                for (PluginVoteItem entity : entityList) {
                    map.put(String.valueOf(entity.getId()), entity);
                }
                handler.put("map", map).render();
            }
        }
    }

    @Autowired
    private PluginVoteItemService service;

}

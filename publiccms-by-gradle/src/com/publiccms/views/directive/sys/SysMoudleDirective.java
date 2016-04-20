package com.publiccms.views.directive.sys;

// Generated 2015-7-22 13:48:39 by com.sanluan.common.source.SourceMaker

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.publiccms.entities.sys.SysMoudle;
import com.publiccms.logic.service.sys.SysMoudleService;
import com.publiccms.common.base.AbstractTemplateDirective;
import com.sanluan.common.handler.RenderHandler;

@Component
public class SysMoudleDirective extends AbstractTemplateDirective {

    @Override
    public void execute(RenderHandler handler) throws IOException, Exception {
        Integer id = handler.getInteger("id");
        if (notEmpty(id)) {
            SysMoudle entity = service.getEntity(id);
            if (notEmpty(entity)) {
                handler.put("object", entity).render();
            }
        } else {
            Integer[] ids = handler.getIntegerArray("ids");
            if (notEmpty(ids)) {
                List<SysMoudle> entityList = service.getEntitys(ids);
                Map<String, SysMoudle> map = new LinkedHashMap<String, SysMoudle>();
                for (SysMoudle entity : entityList) {
                    map.put(String.valueOf(entity.getId()), entity);
                }
                handler.put("map", map).render();
            }
        }
    }

    @Autowired
    private SysMoudleService service;

}

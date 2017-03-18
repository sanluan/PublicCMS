package com.publiccms.views.directive.cms;

// Generated 2016-11-20 14:50:37 by com.sanluan.common.source.SourceGenerator

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.publiccms.entities.cms.CmsDictionary;
import com.publiccms.logic.service.cms.CmsDictionaryService;
import com.publiccms.common.base.AbstractTemplateDirective;
import com.sanluan.common.handler.RenderHandler;

@Component
public class CmsDictionaryDirective extends AbstractTemplateDirective {

    @Override
    public void execute(RenderHandler handler) throws IOException, Exception {
        Integer id = handler.getInteger("id");
        if (notEmpty(id)) {
            handler.put("object", service.getEntity(id)).render();
        } else {
            Integer[] ids = handler.getIntegerArray("ids");
            if (notEmpty(ids)) {
                List<CmsDictionary> entityList = service.getEntitys(ids);
                Map<String, CmsDictionary> map = new LinkedHashMap<String, CmsDictionary>();
                for (CmsDictionary entity : entityList) {
                    map.put(String.valueOf(entity.getId()), entity);
                }
                handler.put("map", map).render();
            }
        }
    }

    @Autowired
    private CmsDictionaryService service;

}

package com.publiccms.views.directive.cms;

// Generated 2016-11-20 14:50:55 by com.sanluan.common.source.SourceGenerator

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.publiccms.entities.cms.CmsDictionaryData;
import com.publiccms.logic.service.cms.CmsDictionaryDataService;
import com.publiccms.common.base.AbstractTemplateDirective;
import com.sanluan.common.handler.RenderHandler;

@Component
public class CmsDictionaryDataDirective extends AbstractTemplateDirective {

    @Override
    public void execute(RenderHandler handler) throws IOException, Exception {
        Integer id = handler.getInteger("id");
        if (notEmpty(id)) {
            handler.put("object", service.getEntity(id)).render();
        } else {
            Integer[] ids = handler.getIntegerArray("ids");
            if (notEmpty(ids)) {
                List<CmsDictionaryData> entityList = service.getEntitys(ids);
                Map<String, CmsDictionaryData> map = new LinkedHashMap<String, CmsDictionaryData>();
                for (CmsDictionaryData entity : entityList) {
                    map.put(String.valueOf(entity.getId()), entity);
                }
                handler.put("map", map).render();
            }
        }
    }

    @Autowired
    private CmsDictionaryDataService service;

}

package com.publiccms.views.directive.home;

// Generated 2016-11-13 11:38:14 by com.sanluan.common.source.SourceGenerator

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.publiccms.entities.home.HomeArticle;
import com.publiccms.logic.service.home.HomeArticleService;
import com.publiccms.common.base.AbstractTemplateDirective;
import com.sanluan.common.handler.RenderHandler;

@Component
public class HomeArticleDirective extends AbstractTemplateDirective {

    @Override
    public void execute(RenderHandler handler) throws IOException, Exception {
        Integer id = handler.getInteger("id");
        if (notEmpty(id)) {
            handler.put("object", service.getEntity(id)).render();
        } else {
            Integer[] ids = handler.getIntegerArray("ids");
            if (notEmpty(ids)) {
                List<HomeArticle> entityList = service.getEntitys(ids);
                Map<String, HomeArticle> map = new LinkedHashMap<String, HomeArticle>();
                for (HomeArticle entity : entityList) {
                    map.put(String.valueOf(entity.getId()), entity);
                }
                handler.put("map", map).render();
            }
        }
    }

    @Autowired
    private HomeArticleService service;

}

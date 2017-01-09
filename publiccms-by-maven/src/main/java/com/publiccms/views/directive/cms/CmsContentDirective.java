package com.publiccms.views.directive.cms;

// Generated 2015-5-10 17:54:56 by com.sanluan.common.source.SourceMaker

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.publiccms.entities.cms.CmsContent;
import com.publiccms.entities.sys.SysSite;
import com.publiccms.logic.service.cms.CmsContentService;
import com.publiccms.common.base.AbstractTemplateDirective;
import com.sanluan.common.handler.RenderHandler;

@Component
public class CmsContentDirective extends AbstractTemplateDirective {

    @Override
    public void execute(RenderHandler handler) throws IOException, Exception {
        Long id = handler.getLong("id");
        Integer categoryId = handler.getInteger("categoryId");

        CmsContent entity = null;

        SysSite site = getSite(handler);
        if (notEmpty(id)) {
              entity = service.getEntity(id);

        }else if(notEmpty(categoryId)){
            entity = service.getLastEntity(categoryId);

        } else {
            Long[] ids = handler.getLongArray("ids");
            if (notEmpty(ids)) {
                List<CmsContent> entityList = service.getEntitys(ids);
                Map<String, CmsContent> map = new LinkedHashMap<String, CmsContent>();
                for (CmsContent _entity : entityList) {
                    if (site.getId() == _entity.getSiteId()) {
                        map.put(String.valueOf(_entity.getId()), _entity);
                    }
                }
                handler.put("map", map).render();
            }
        }


        if (notEmpty(entity) && site.getId() == entity.getSiteId()) {
            handler.put("object", entity).render();
        }
    }

    @Autowired
    private CmsContentService service;

}

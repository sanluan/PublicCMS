package com.publiccms.views.directive.cms;

// Generated 2015-7-10 16:36:23 by SourceMaker

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.publiccms.entities.cms.CmsTag;
import com.publiccms.logic.service.cms.CmsTagService;
import com.sanluan.common.base.BaseDirective;
import com.sanluan.common.handler.RenderHandler;

@Component
public class CmsTagDirective extends BaseDirective {

    @Override
    public void execute(RenderHandler handler) throws IOException, Exception {
        Integer id = handler.getInteger(ID);
        if (null != id) {
            CmsTag entity = service.getEntity(id);
            handler.put("object", entity).renderIfNotNull(entity);
        } else {
            Integer[] ids = handler.getIntegerArray("ids");
            if (ArrayUtils.isNotEmpty(ids)) {
                List<CmsTag> entityList = service.getEntitys(ids);
                Map<String, CmsTag> map = new HashMap<String, CmsTag>();
                for (CmsTag entity : entityList) {
                    map.put(String.valueOf(entity.getId()), entity);
                }
                handler.put("map", map).render();
            }
        }
    }

    @Autowired
    private CmsTagService service;

}

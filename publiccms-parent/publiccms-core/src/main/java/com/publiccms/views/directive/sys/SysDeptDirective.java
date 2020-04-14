package com.publiccms.views.directive.sys;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.publiccms.common.base.AbstractTemplateDirective;
import com.publiccms.common.handler.RenderHandler;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.entities.sys.SysDept;
import com.publiccms.entities.sys.SysSite;
import com.publiccms.logic.service.sys.SysDeptService;

/**
 *
 * SysDeptDirective
 * 
 */
@Component
public class SysDeptDirective extends AbstractTemplateDirective {

    @Override
    public void execute(RenderHandler handler) throws IOException, Exception {
        Integer id = handler.getInteger("id");
        SysSite site = getSite(handler);
        if (CommonUtils.notEmpty(id)) {
            SysDept entity = service.getEntity(id);
            if (null != entity && site.getId() == entity.getSiteId()) {
                handler.put("object", entity).render();
            }
        } else {
            Integer[] ids = handler.getIntegerArray("ids");
            if (CommonUtils.notEmpty(ids)) {
                List<SysDept> entityList = service.getEntitys(ids);
                Map<String, SysDept> map = CommonUtils.listToMap(entityList, k -> k.getId().toString(), null,
                        entity -> site.getId() == entity.getSiteId());
                handler.put("map", map).render();
            }
        }
    }

    @Override
    public boolean needAppToken() {
        return true;
    }
    
    @Autowired
    private SysDeptService service;

}

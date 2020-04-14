package com.publiccms.views.directive.sys;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.publiccms.common.base.AbstractTemplateDirective;
import com.publiccms.common.handler.RenderHandler;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.entities.sys.SysSite;
import com.publiccms.logic.service.sys.SysSiteService;

/**
 *
 * SysSiteDirective
 * 
 */
@Component
public class SysSiteDirective extends AbstractTemplateDirective {

    @Override
    public void execute(RenderHandler handler) throws IOException, Exception {
        Short id = handler.getShort("id");
        if (CommonUtils.notEmpty(id)) {
            SysSite entity = service.getEntity(id);
            if (null != entity) {
                handler.put("object", entity).render();
            }
        } else {
            Short[] ids = handler.getShortArray("ids");
            if (CommonUtils.notEmpty(ids)) {
                List<SysSite> entityList = service.getEntitys(ids);
                Map<String, SysSite> map = CommonUtils.listToMap(entityList, k -> k.getId().toString(), null, null);
                handler.put("map", map).render();
            }
        }
    }

    @Override
    public boolean needAppToken() {
        return true;
    }

    @Autowired
    private SysSiteService service;

}

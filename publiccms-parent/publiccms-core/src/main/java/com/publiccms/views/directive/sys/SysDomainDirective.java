package com.publiccms.views.directive.sys;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.publiccms.common.base.AbstractTemplateDirective;
import com.publiccms.common.handler.RenderHandler;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.entities.sys.SysDomain;
import com.publiccms.logic.service.sys.SysDomainService;

/**
 *
 * SysDomainDirective
 * 
 */
@Component
public class SysDomainDirective extends AbstractTemplateDirective {

    @Override
    public void execute(RenderHandler handler) throws IOException, Exception {
        String id = handler.getString("id");
        if (CommonUtils.notEmpty(id)) {
            SysDomain entity = service.getEntity(id);
            if (null != entity) {
                handler.put("object", entity).render();
            }
        } else {
            String[] ids = handler.getStringArray("ids");
            if (CommonUtils.notEmpty(ids)) {
                List<SysDomain> entityList = service.getEntitys(ids);
                Map<String, SysDomain> map = CommonUtils.listToMap(entityList, k -> k.getName(), null, null);
                handler.put("map", map).render();
            }
        }
    }

    @Override
    public boolean needAppToken() {
        return true;
    }

    @Autowired
    private SysDomainService service;

}

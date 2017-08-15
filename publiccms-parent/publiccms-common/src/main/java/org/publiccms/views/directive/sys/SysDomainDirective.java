package org.publiccms.views.directive.sys;

// Generated 2016-1-20 11:19:18 by com.publiccms.common.source.SourceGenerator
import static com.publiccms.common.tools.CommonUtils.notEmpty;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.publiccms.common.base.AbstractTemplateDirective;
import org.publiccms.entities.sys.SysDomain;
import org.publiccms.logic.service.sys.SysDomainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.publiccms.common.handler.RenderHandler;

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
        if (notEmpty(id)) {
            SysDomain entity = service.getEntity(id);
            if (null != entity) {
                handler.put("object", entity).render();
            }
        } else {
            String[] ids = handler.getStringArray("ids");
            if (notEmpty(ids)) {
                List<SysDomain> entityList = service.getEntitys(ids);
                Map<String, SysDomain> map = new LinkedHashMap<>();
                for (SysDomain entity : entityList) {
                    map.put(String.valueOf(entity.getName()), entity);
                }
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

package org.publiccms.views.directive.sys;

// Generated 2015-7-22 13:48:39 by com.publiccms.common.source.SourceGenerator
import static com.publiccms.common.tools.CommonUtils.notEmpty;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.publiccms.common.base.AbstractTemplateDirective;
import org.publiccms.entities.sys.SysMoudle;
import org.publiccms.logic.service.sys.SysMoudleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.publiccms.common.handler.RenderHandler;

/**
 *
 * SysMoudleDirective
 * 
 */
@Component
public class SysMoudleDirective extends AbstractTemplateDirective {

    @Override
    public void execute(RenderHandler handler) throws IOException, Exception {
        Integer id = handler.getInteger("id");
        if (notEmpty(id)) {
            SysMoudle entity = service.getEntity(id);
            if (null != entity) {
                handler.put("object", entity).render();
            }
        } else {
            Integer[] ids = handler.getIntegerArray("ids");
            if (notEmpty(ids)) {
                List<SysMoudle> entityList = service.getEntitys(ids);
                Map<String, SysMoudle> map = new LinkedHashMap<>();
                for (SysMoudle entity : entityList) {
                    map.put(String.valueOf(entity.getId()), entity);
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
    private SysMoudleService service;

}

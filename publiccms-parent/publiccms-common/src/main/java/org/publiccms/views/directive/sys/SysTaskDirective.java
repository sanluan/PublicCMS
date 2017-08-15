package org.publiccms.views.directive.sys;

// Generated 2015-7-3 16:23:02 by com.publiccms.common.source.SourceGenerator
import static com.publiccms.common.tools.CommonUtils.notEmpty;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.publiccms.common.base.AbstractTemplateDirective;
import org.publiccms.entities.sys.SysSite;
import org.publiccms.entities.sys.SysTask;
import org.publiccms.logic.service.sys.SysTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.publiccms.common.handler.RenderHandler;

/**
 *
 * SysTaskDirective
 * 
 */
@Component
public class SysTaskDirective extends AbstractTemplateDirective {

    @Override
    public void execute(RenderHandler handler) throws IOException, Exception {
        Integer id = handler.getInteger("id");
        SysSite site = getSite(handler);
        if (notEmpty(id)) {
            SysTask entity = service.getEntity(id);
            if (null != entity && site.getId() == entity.getSiteId()) {
                handler.put("object", entity).render();
            }
        } else {
            Integer[] ids = handler.getIntegerArray("ids");
            if (notEmpty(ids)) {
                List<SysTask> entityList = service.getEntitys(ids);
                Map<String, SysTask> map = new LinkedHashMap<>();
                for (SysTask entity : entityList) {
                    if(site.getId() == entity.getSiteId()){
                        map.put(String.valueOf(entity.getId()), entity);
                    }
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
    private SysTaskService service;

}

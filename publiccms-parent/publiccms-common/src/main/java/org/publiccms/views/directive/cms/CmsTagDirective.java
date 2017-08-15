package org.publiccms.views.directive.cms;

// Generated 2015-7-10 16:36:23 by com.publiccms.common.source.SourceGenerator
import static com.publiccms.common.tools.CommonUtils.notEmpty;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.publiccms.common.base.AbstractTemplateDirective;
import org.publiccms.entities.cms.CmsTag;
import org.publiccms.entities.sys.SysSite;
import org.publiccms.logic.service.cms.CmsTagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.publiccms.common.handler.RenderHandler;

/**
 *
 * CmsTagDirective
 * 
 */
@Component
public class CmsTagDirective extends AbstractTemplateDirective {

    @Override
    public void execute(RenderHandler handler) throws IOException, Exception {
        Long id = handler.getLong("id");
        SysSite site = getSite(handler);
        if (notEmpty(id)) {
            CmsTag entity = service.getEntity(id);
            if (null != entity && site.getId() == entity.getSiteId()) {
                handler.put("object", entity).render();
            }
        } else {
            Long[] ids = handler.getLongArray("ids");
            if (notEmpty(ids)) {
                List<CmsTag> entityList = service.getEntitys(ids);
                Map<String, CmsTag> map = new LinkedHashMap<>();
                for (CmsTag entity : entityList) {
                    if (site.getId() == entity.getSiteId()) {
                        map.put(String.valueOf(entity.getId()), entity);
                    }
                }
                handler.put("map", map).render();
            }
        }
    }

    @Autowired
    private CmsTagService service;

}

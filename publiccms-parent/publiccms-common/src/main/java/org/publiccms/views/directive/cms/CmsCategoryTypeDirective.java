package org.publiccms.views.directive.cms;

// Generated 2016-2-26 15:57:04 by com.publiccms.common.source.SourceGenerator
import static com.publiccms.common.tools.CommonUtils.notEmpty;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.publiccms.common.base.AbstractTemplateDirective;
import org.publiccms.entities.cms.CmsCategoryType;
import org.publiccms.entities.sys.SysSite;
import org.publiccms.logic.service.cms.CmsCategoryTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.publiccms.common.handler.RenderHandler;

/**
 *
 * CmsCategoryTypeDirective
 * 
 */
@Component
public class CmsCategoryTypeDirective extends AbstractTemplateDirective {

    @Override
    public void execute(RenderHandler handler) throws IOException, Exception {
        Integer id = handler.getInteger("id");
        SysSite site = getSite(handler);
        if (notEmpty(id)) {
            CmsCategoryType entity = service.getEntity(id);
            if (null != entity && site.getId() == entity.getSiteId()) {
                handler.put("object", entity).render();
            }
        } else {
            Integer[] ids = handler.getIntegerArray("ids");
            if (notEmpty(ids)) {
                List<CmsCategoryType> entityList = service.getEntitys(ids);
                Map<String, CmsCategoryType> map = new LinkedHashMap<>();
                for (CmsCategoryType entity : entityList) {
                    if(site.getId() == entity.getSiteId()){
                        map.put(String.valueOf(entity.getId()), entity);
                    }
                }
                handler.put("map", map).render();
            }
        }
    }

    @Autowired
    private CmsCategoryTypeService service;

}

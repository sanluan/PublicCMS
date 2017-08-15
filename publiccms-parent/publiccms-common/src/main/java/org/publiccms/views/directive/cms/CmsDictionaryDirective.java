package org.publiccms.views.directive.cms;

// Generated 2016-11-20 14:50:37 by com.publiccms.common.source.SourceGenerator
import static com.publiccms.common.tools.CommonUtils.notEmpty;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.publiccms.common.base.AbstractTemplateDirective;
import org.publiccms.entities.cms.CmsDictionary;
import org.publiccms.logic.service.cms.CmsDictionaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.publiccms.common.handler.RenderHandler;

/**
 *
 * CmsDictionaryDirective
 * 
 */
@Component
public class CmsDictionaryDirective extends AbstractTemplateDirective {

    @Override
    public void execute(RenderHandler handler) throws IOException, Exception {
        Long id = handler.getLong("id");
        if (notEmpty(id)) {
            handler.put("object", service.getEntity(id)).render();
        } else {
            Long[] ids = handler.getLongArray("ids");
            if (notEmpty(ids)) {
                List<CmsDictionary> entityList = service.getEntitys(ids);
                Map<String, CmsDictionary> map = new LinkedHashMap<>();
                for (CmsDictionary entity : entityList) {
                    map.put(String.valueOf(entity.getId()), entity);
                }
                handler.put("map", map).render();
            }
        }
    }

    @Autowired
    private CmsDictionaryService service;

}

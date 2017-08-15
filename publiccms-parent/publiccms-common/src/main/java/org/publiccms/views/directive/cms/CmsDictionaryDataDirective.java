package org.publiccms.views.directive.cms;

// Generated 2016-11-20 14:50:55 by com.publiccms.common.source.SourceGenerator
import static com.publiccms.common.tools.CommonUtils.notEmpty;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

import org.publiccms.common.base.AbstractTemplateDirective;
import org.publiccms.entities.cms.CmsDictionaryData;
import org.publiccms.entities.cms.CmsDictionaryDataId;
import org.publiccms.logic.service.cms.CmsDictionaryDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.publiccms.common.handler.RenderHandler;

/**
 *
 * CmsDictionaryDataDirective
 * 
 */
@Component
public class CmsDictionaryDataDirective extends AbstractTemplateDirective {

    @Override
    public void execute(RenderHandler handler) throws IOException, Exception {
        Long dictionaryId = handler.getLong("dictionaryId");
        String value = handler.getString("value");
        if (notEmpty(dictionaryId)) {
            if (notEmpty(value)) {
                CmsDictionaryData entity = service.getEntity(new CmsDictionaryDataId(dictionaryId, value));
                handler.put("object", entity).render();
            } else {
                String[] values = handler.getStringArray("values");
                if (notEmpty(values)) {
                    Map<String, CmsDictionaryData> map = new LinkedHashMap<>();
                    CmsDictionaryDataId[] ids = new CmsDictionaryDataId[values.length];
                    for (int i = 0; i < values.length; i++) {
                        ids[i] = new CmsDictionaryDataId(dictionaryId, values[i]);
                    }
                    for (CmsDictionaryData entity : service.getEntitys(ids)) {
                        map.put(entity.getId().getValue(), entity);
                    }
                    handler.put("map", map).render();
                }
            }
        }
    }

    @Autowired
    private CmsDictionaryDataService service;

}

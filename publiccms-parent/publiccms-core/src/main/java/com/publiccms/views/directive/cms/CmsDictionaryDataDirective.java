package com.publiccms.views.directive.cms;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.publiccms.common.base.AbstractTemplateDirective;
import com.publiccms.common.handler.RenderHandler;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.entities.cms.CmsDictionaryData;
import com.publiccms.entities.cms.CmsDictionaryDataId;
import com.publiccms.entities.sys.SysSite;
import com.publiccms.logic.service.cms.CmsDictionaryDataService;

/**
 *
 * CmsDictionaryDataDirective
 * 
 */
@Component
public class CmsDictionaryDataDirective extends AbstractTemplateDirective {

    @Override
    public void execute(RenderHandler handler) throws IOException, Exception {
        String dictionaryId = handler.getString("dictionaryId");
        String value = handler.getString("value");
        if (CommonUtils.notEmpty(dictionaryId)) {
            SysSite site = getSite(handler);
            if (CommonUtils.notEmpty(value)) {
                CmsDictionaryData entity = service.getEntity(new CmsDictionaryDataId(dictionaryId, site.getId(), value));
                handler.put("object", entity).render();
            } else {
                String[] values = handler.getStringArray("values");
                if (CommonUtils.notEmpty(values)) {

                    CmsDictionaryDataId[] ids = new CmsDictionaryDataId[values.length];
                    for (int i = 0; i < values.length; i++) {
                        ids[i] = new CmsDictionaryDataId(dictionaryId, site.getId(), values[i]);
                    }
                    List<CmsDictionaryData> entityList = service.getEntitys(ids);
                    Map<String, CmsDictionaryData> map = CommonUtils.listToMap(entityList, k -> k.getId().getValue(), null, null);
                    handler.put("map", map).render();
                }
            }
        }
    }

    @Autowired
    private CmsDictionaryDataService service;

}

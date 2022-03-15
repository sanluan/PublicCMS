package com.publiccms.views.directive.cms;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.publiccms.common.base.AbstractTemplateDirective;
import com.publiccms.common.handler.RenderHandler;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.entities.cms.CmsDictionaryExcludeValue;
import com.publiccms.entities.cms.CmsDictionaryExcludeValueId;
import com.publiccms.entities.sys.SysSite;
import com.publiccms.logic.service.cms.CmsDictionaryExcludeValueService;

/**
 *
 * CmsDictionaryExcludeValueDirective
 * 
 */
@Component
public class CmsDictionaryExcludeValueDirective extends AbstractTemplateDirective {

    @Override
    public void execute(RenderHandler handler) throws IOException, Exception {
        String dictionaryId = handler.getString("dictionaryId");
        String excludeDictionaryId = handler.getString("excludeDictionaryId");
        String value = handler.getString("value");
        if (CommonUtils.notEmpty(dictionaryId) && CommonUtils.notEmpty(excludeDictionaryId)) {
            SysSite site = getSite(handler);
            if (CommonUtils.notEmpty(value)) {
                CmsDictionaryExcludeValue entity = service
                        .getEntity(new CmsDictionaryExcludeValueId(dictionaryId, site.getId(), excludeDictionaryId, value));
                if (null != entity) {
                    handler.put("object", entity).render();
                }
            } else {
                String[] values = handler.getStringArray("values");
                if (CommonUtils.notEmpty(values)) {
                    CmsDictionaryExcludeValueId[] ids = new CmsDictionaryExcludeValueId[values.length];
                    for (int i = 0; i < values.length; i++) {
                        ids[i] = new CmsDictionaryExcludeValueId(dictionaryId, site.getId(), excludeDictionaryId, values[i]);
                    }
                    List<CmsDictionaryExcludeValue> entityList = service.getEntitys(ids);
                    Map<String, CmsDictionaryExcludeValue> map = CommonUtils.listToMap(entityList, k -> k.getId().getValue(),
                            null, null);
                    handler.put("map", map).render();
                }
            }
        }
    }

    @Autowired
    private CmsDictionaryExcludeValueService service;

}

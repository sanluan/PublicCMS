package com.publiccms.views.directive.cms;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

import com.publiccms.common.base.AbstractTemplateDirective;
import com.publiccms.common.handler.RenderHandler;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.entities.cms.CmsDictionaryExclude;
import com.publiccms.entities.cms.CmsDictionaryExcludeId;
import com.publiccms.entities.sys.SysSite;
import com.publiccms.logic.service.cms.CmsDictionaryExcludeService;

/**
 *
 * CmsDictionaryExcludeDirective
 * 
 */
@Component
public class CmsDictionaryExcludeDirective extends AbstractTemplateDirective {

    @Override
    public void execute(RenderHandler handler) throws IOException, Exception {
        String dictionaryId = handler.getString("dictionaryId");
        String excludeDictionaryId = handler.getString("excludeDictionaryId");
        if (CommonUtils.notEmpty(dictionaryId)) {
            SysSite site = getSite(handler);
            if (CommonUtils.notEmpty(excludeDictionaryId)) {
                CmsDictionaryExclude entity = service
                        .getEntity(new CmsDictionaryExcludeId(dictionaryId, site.getId(), excludeDictionaryId));
                if (null != entity) {
                    handler.put("object", entity).render();
                }
            } else {
                String[] excludeDictionaryIds = handler.getStringArray("excludeDictionaryIds");
                if (CommonUtils.notEmpty(excludeDictionaryIds)) {
                    CmsDictionaryExcludeId[] ids = new CmsDictionaryExcludeId[excludeDictionaryIds.length];
                    for (int i = 0; i < excludeDictionaryIds.length; i++) {
                        ids[i] = new CmsDictionaryExcludeId(dictionaryId, site.getId(), excludeDictionaryIds[i]);
                    }
                    List<CmsDictionaryExclude> entityList = service.getEntitys(ids);
                    Map<String, CmsDictionaryExclude> map = CommonUtils.listToMap(entityList,
                            k -> k.getId().getExcludeDictionaryId(), null, null);
                    handler.put("map", map).render();
                }
            }
        }
    }

    @Resource
    private CmsDictionaryExcludeService service;

}

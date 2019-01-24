package com.publiccms.views.directive.cms;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.publiccms.common.base.AbstractTemplateDirective;
import com.publiccms.common.constants.CommonConstants;
import com.publiccms.common.handler.RenderHandler;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.entities.cms.CmsDictionary;
import com.publiccms.entities.cms.CmsDictionaryId;
import com.publiccms.entities.sys.SysSite;
import com.publiccms.logic.service.cms.CmsDictionaryService;

/**
 *
 * CmsDictionaryDirective
 * 
 */
@Component
public class CmsDictionaryDirective extends AbstractTemplateDirective {

    @Override
    public void execute(RenderHandler handler) throws IOException, Exception {
        Short siteId = handler.getShort("siteId");
        SysSite site = getSite(handler);
        if (null == siteId || !siteId.equals(site.getId()) || !siteId.equals(site.getParentId())) {
            siteId = site.getId();
        }
        String id = handler.getString("id");
        if (CommonUtils.notEmpty(id)) {
            CmsDictionaryId entityId = new CmsDictionaryId(id, siteId);
            CmsDictionary entity = service.getEntity(entityId);
            if (null != entity) {
                handler.put("object", entity).render();
            }
        } else {
            String[] ids = handler.getStringArray("ids");
            if (CommonUtils.notEmpty(ids)) {
                CmsDictionaryId[] entityIds = new CmsDictionaryId[ids.length];
                for (int i = 0; i < ids.length; i++) {
                    entityIds[i] = new CmsDictionaryId(ids[i], siteId);
                }
                List<CmsDictionary> entityList = service.getEntitys(entityIds);
                Map<String, CmsDictionary> map = entityList.stream().collect(Collectors.toMap(k -> k.getId().toString(),
                        Function.identity(), CommonConstants.defaultMegerFunction(), LinkedHashMap::new));
                handler.put("map", map).render();
            }
        }
    }

    @Autowired
    private CmsDictionaryService service;

}

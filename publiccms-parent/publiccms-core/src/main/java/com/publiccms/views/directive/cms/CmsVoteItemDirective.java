package com.publiccms.views.directive.cms;

// Generated 2020-3-26 11:46:48 by com.publiccms.common.generator.SourceGenerator

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.publiccms.common.base.AbstractTemplateDirective;
import com.publiccms.common.handler.RenderHandler;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.entities.cms.CmsVoteItem;
import com.publiccms.logic.service.cms.CmsVoteItemService;

/**
 *
 * CmsVoteItemDirective
 * 
 */
@Component
public class CmsVoteItemDirective extends AbstractTemplateDirective {

    @Override
    public void execute(RenderHandler handler) throws IOException, Exception {
        Long id = handler.getLong("id");
        if (CommonUtils.notEmpty(id)) {
            CmsVoteItem entity = service.getEntity(id);
            if (null != entity) {
                handler.put("object", entity).render();
            }
        } else {
            Long[] ids = handler.getLongArray("ids");
            if (CommonUtils.notEmpty(ids)) {
                List<CmsVoteItem> entityList = service.getEntitys(ids);
                Map<String, CmsVoteItem> map = CommonUtils.listToMap(entityList, k -> k.getId().toString(), null, null);
                handler.put("map", map).render();
            }
        }
    }

    @Autowired
    private CmsVoteItemService service;

}

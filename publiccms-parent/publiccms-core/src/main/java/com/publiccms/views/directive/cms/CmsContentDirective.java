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
import com.publiccms.entities.cms.CmsContent;
import com.publiccms.entities.sys.SysSite;
import com.publiccms.logic.component.site.StatisticsComponent;
import com.publiccms.logic.service.cms.CmsContentService;

/**
 *
 * CmsContentDirective
 * 
 */
@Component
public class CmsContentDirective extends AbstractTemplateDirective {

    @Override
    public void execute(RenderHandler handler) throws IOException, Exception {
        Long id = handler.getLong("id");
        SysSite site = getSite(handler);
        if (CommonUtils.notEmpty(id)) {
            CmsContent entity = service.getEntity(id);
            if (null != entity && site.getId() == entity.getSiteId()) {
                Integer clicks = statisticsComponent.getContentClicks(entity.getId());
                if (null != clicks) {
                    entity.setClicks(entity.getClicks() + clicks);
                }
                handler.put("object", entity).render();
            }
        } else {
            Long[] ids = handler.getLongArray("ids");
            if (CommonUtils.notEmpty(ids)) {
                List<CmsContent> entityList = service.getEntitys(ids);
                entityList.forEach(e -> {
                    Integer clicks = statisticsComponent.getContentClicks(e.getId());
                    if (null != clicks) {
                        e.setClicks(e.getClicks() + clicks);
                    }
                });
                Map<String, CmsContent> map = entityList.stream().filter(entity -> site.getId() == entity.getSiteId())
                        .collect(Collectors.toMap(k -> k.getId().toString(), Function.identity(),
                                CommonConstants.defaultMegerFunction(), LinkedHashMap::new));
                handler.put("map", map).render();
            }
        }
    }

    @Autowired
    private CmsContentService service;
    @Autowired
    private StatisticsComponent statisticsComponent;
}

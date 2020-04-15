package com.publiccms.views.directive.cms;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.publiccms.common.base.AbstractTemplateDirective;
import com.publiccms.common.handler.RenderHandler;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.common.tools.ExtendUtils;
import com.publiccms.entities.cms.CmsPlace;
import com.publiccms.entities.cms.CmsPlaceAttribute;
import com.publiccms.entities.sys.SysSite;
import com.publiccms.logic.component.site.StatisticsComponent;
import com.publiccms.logic.component.template.TemplateComponent;
import com.publiccms.logic.service.cms.CmsPlaceAttributeService;
import com.publiccms.logic.service.cms.CmsPlaceService;

/**
 *
 * CmsPlaceDirective
 * 
 */
@Component
public class CmsPlaceDirective extends AbstractTemplateDirective {

    @Override
    public void execute(RenderHandler handler) throws IOException, Exception {
        Long id = handler.getLong("id");
        boolean absoluteURL = handler.getBoolean("absoluteURL", true);
        SysSite site = getSite(handler);
        if (CommonUtils.notEmpty(id)) {
            CmsPlace entity = service.getEntity(id);
            if (null != entity && site.getId() == entity.getSiteId()) {
                if (absoluteURL) {
                    templateComponent.initPlaceCover(site, entity);
                }
                handler.put("object", entity);
                if (handler.getBoolean("containsAttribute", false)) {
                    CmsPlaceAttribute attribute = attributeService.getEntity(id);
                    if (null != attribute) {
                        handler.put("attribute", ExtendUtils.getExtendMap(attribute.getData()));
                    }
                }
                handler.render();
            }
        } else {
            Long[] ids = handler.getLongArray("ids");
            if (CommonUtils.notEmpty(ids)) {
                List<CmsPlace> entityList = service.getEntitys(ids);
                Consumer<CmsPlace> consumer;
                if (absoluteURL) {
                    consumer = e -> {
                        Integer clicks = statisticsComponent.getPlaceClicks(e.getId());
                        if (null != clicks) {
                            e.setClicks(e.getClicks() + clicks);
                        }
                        templateComponent.initPlaceCover(site, e);
                    };
                } else {
                    consumer = e -> {
                        Integer clicks = statisticsComponent.getPlaceClicks(e.getId());
                        if (null != clicks) {
                            e.setClicks(e.getClicks() + clicks);
                        }
                    };
                }
                Map<String, CmsPlace> map = CommonUtils.listToMap(entityList, k -> k.getId().toString(), consumer,
                        entity -> site.getId() == entity.getSiteId());
                handler.put("map", map).render();
            }
        }
    }

    @Override
    public boolean needAppToken() {
        return true;
    }

    @Autowired
    private CmsPlaceService service;
    @Autowired
    private CmsPlaceAttributeService attributeService;
    @Autowired
    private TemplateComponent templateComponent;
    @Autowired
    private StatisticsComponent statisticsComponent;
}

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
import com.publiccms.entities.cms.CmsContent;
import com.publiccms.entities.cms.CmsContentAttribute;
import com.publiccms.entities.sys.SysSite;
import com.publiccms.logic.component.site.StatisticsComponent;
import com.publiccms.logic.component.template.TemplateComponent;
import com.publiccms.logic.service.cms.CmsContentAttributeService;
import com.publiccms.logic.service.cms.CmsContentService;
import com.publiccms.views.pojo.entities.ClickStatistics;

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
        boolean absoluteURL = handler.getBoolean("absoluteURL", true);
        boolean absoluteId = handler.getBoolean("absoluteId", true);
        SysSite site = getSite(handler);
        if (CommonUtils.notEmpty(id)) {
            CmsContent entity = service.getEntity(id);
            if (null != entity && site.getId() == entity.getSiteId()) {
                ClickStatistics statistics = statisticsComponent.getContentStatistics(entity.getId());
                if (null != statistics) {
                    entity.setClicks(entity.getClicks() + statistics.getClicks());
                }
                if (absoluteId && null != entity.getQuoteContentId()) {
                    entity.setId(entity.getQuoteContentId());
                }
                if (absoluteURL) {
                    TemplateComponent.initContentUrl(site, entity);
                    TemplateComponent.initContentCover(site, entity);
                }
                handler.put("object", entity);
                if (handler.getBoolean("containsAttribute", false)) {
                    CmsContentAttribute attribute = attributeService.getEntity(id);
                    if (null != attribute) {
                        Map<String, String> map = ExtendUtils.getExtendMap(attribute.getData());
                        map.put("text", attribute.getText());
                        map.put("source", attribute.getSource());
                        map.put("sourceUrl", attribute.getSourceUrl());
                        map.put("wordCount", String.valueOf(attribute.getWordCount()));
                        handler.put("attribute", map);
                    }
                }
                handler.render();
            }
        } else {
            Long[] ids = handler.getLongArray("ids");
            if (CommonUtils.notEmpty(ids)) {
                List<CmsContent> entityList = service.getEntitys(ids);
                Consumer<CmsContent> consumer;
                if (absoluteURL) {
                    consumer = e -> {
                        ClickStatistics statistics = statisticsComponent.getContentStatistics(e.getId());
                        if (null != statistics) {
                            e.setClicks(e.getClicks() + statistics.getClicks());
                        }
                        if (absoluteId && null != e.getQuoteContentId()) {
                            e.setId(e.getQuoteContentId());
                        }
                        TemplateComponent.initContentUrl(site, e);
                        TemplateComponent.initContentCover(site, e);
                    };
                } else {
                    consumer = e -> {
                        ClickStatistics statistics = statisticsComponent.getContentStatistics(e.getId());
                        if (absoluteId && null != e.getQuoteContentId()) {
                            e.setId(e.getQuoteContentId());
                        }
                        if (null != statistics) {
                            e.setClicks(e.getClicks() + statistics.getClicks());
                        }
                    };
                }
                Map<String, CmsContent> map = CommonUtils.listToMap(entityList, k -> k.getId().toString(), consumer,
                        entity -> site.getId() == entity.getSiteId());
                handler.put("map", map).render();
            }
        }
    }

    @Autowired
    private CmsContentService service;
    @Autowired
    private CmsContentAttributeService attributeService;
    @Autowired
    private StatisticsComponent statisticsComponent;
}

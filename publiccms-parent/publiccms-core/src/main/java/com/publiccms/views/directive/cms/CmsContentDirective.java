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
import com.publiccms.common.tools.ExtendUtils;
import com.publiccms.entities.cms.CmsContent;
import com.publiccms.entities.cms.CmsContentAttribute;
import com.publiccms.entities.sys.SysSite;
import com.publiccms.logic.component.site.StatisticsComponent;
import com.publiccms.logic.component.template.TemplateComponent;
import com.publiccms.logic.service.cms.CmsContentAttributeService;
import com.publiccms.logic.service.cms.CmsContentService;
import com.publiccms.views.pojo.entities.CmsContentStatistics;

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
                CmsContentStatistics statistics = statisticsComponent.getContentStatistics(entity.getId());
                if (null != statistics) {
                    entity.setClicks(entity.getClicks() + statistics.getClicks());
                    entity.setScores(entity.getScores() + statistics.getScores());
                }
                if (handler.getBoolean("absoluteURL", false)) {
                    templateComponent.initContentUrl(site, entity);
                    templateComponent.initContentCover(site, entity);
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
                boolean absoluteURL = handler.getBoolean("absoluteURL", true);
                entityList.forEach(e -> {
                    CmsContentStatistics statistics = statisticsComponent.getContentStatistics(e.getId());
                    if (null != statistics) {
                        e.setClicks(e.getClicks() + statistics.getClicks());
                        e.setScores(e.getScores() + statistics.getScores());
                    }
                    if (absoluteURL) {
                        templateComponent.initContentUrl(site, e);
                        templateComponent.initContentCover(site, e);
                    }
                });
                Map<String, CmsContent> map = entityList.stream().filter(entity -> site.getId() == entity.getSiteId())
                        .collect(Collectors.toMap(k -> k.getId().toString(), Function.identity(),
                                CommonConstants.defaultMegerFunction(), LinkedHashMap::new));
                handler.put("map", map).render();
            }
        }
    }

    @Override
    public boolean needAppToken() {
        return true;
    }

    @Autowired
    private CmsContentService service;
    @Autowired
    private TemplateComponent templateComponent;
    @Autowired
    private CmsContentAttributeService attributeService;
    @Autowired
    private StatisticsComponent statisticsComponent;
}

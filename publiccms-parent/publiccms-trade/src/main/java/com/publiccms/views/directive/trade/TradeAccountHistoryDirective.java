package com.publiccms.views.directive.trade;

// Generated 2019-6-16 9:47:27 by com.publiccms.common.generator.SourceGenerator

import java.io.IOException;
import java.util.List;
import java.util.Map;

import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

import com.publiccms.common.base.AbstractTemplateDirective;
import com.publiccms.common.handler.RenderHandler;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.entities.sys.SysSite;
import com.publiccms.entities.trade.TradeAccountHistory;
import com.publiccms.logic.service.trade.TradeAccountHistoryService;

/**
 *
 * TradeAccountHistoryDirective
 * 
 */
@Component
public class TradeAccountHistoryDirective extends AbstractTemplateDirective {

    @Override
    public void execute(RenderHandler handler) throws IOException, Exception {
        Long id = handler.getLong("id");
        SysSite site = getSite(handler);
        if (CommonUtils.notEmpty(id)) {
            TradeAccountHistory entity = service.getEntity(id);
            if (null != entity && site.getId() == entity.getSiteId()) {
                handler.put("object", entity).render();
            }
        } else {
            Long[] ids = handler.getLongArray("ids");
            if (CommonUtils.notEmpty(ids)) {
                List<TradeAccountHistory> entityList = service.getEntitys(ids);
                Map<String, TradeAccountHistory> map = CommonUtils.listToMap(entityList, k -> k.getId().toString(), null, entity -> site.getId() == entity.getSiteId());
                handler.put("map", map).render();
            }
        }
    }
    
    @Override
    public boolean needAppToken() {
        return true;
    }

    @Resource
    private TradeAccountHistoryService service;

}

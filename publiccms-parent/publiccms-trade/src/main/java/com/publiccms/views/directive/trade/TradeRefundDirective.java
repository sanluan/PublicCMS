package com.publiccms.views.directive.trade;

// Generated 2019-6-15 20:08:45 by com.publiccms.common.generator.SourceGenerator

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.publiccms.common.base.AbstractTemplateDirective;
import com.publiccms.common.handler.RenderHandler;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.entities.trade.TradeRefund;
import com.publiccms.logic.service.trade.TradeRefundService;

/**
 *
 * TradeRefundDirective
 * 
 */
@Component
public class TradeRefundDirective extends AbstractTemplateDirective {

    @Override
    public void execute(RenderHandler handler) throws IOException, Exception {
        Long id = handler.getLong("id");
        if (CommonUtils.notEmpty(id)) {
            TradeRefund entity = service.getEntity(id);
            if (null != entity) {
                handler.put("object", entity).render();
            }
        } else {
            Long[] ids = handler.getLongArray("ids");
            if (CommonUtils.notEmpty(ids)) {
                List<TradeRefund> entityList = service.getEntitys(ids);
                Map<String, TradeRefund> map = new LinkedHashMap<>();
                for (TradeRefund entity : entityList) {
                    map.put(String.valueOf(entity.getId()), entity);
                }
                handler.put("map", map).render();
            }
        }
    }

    @Override
    public boolean needAppToken() {
        return true;
    }

    @Autowired
    private TradeRefundService service;

}

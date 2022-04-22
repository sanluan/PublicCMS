package com.publiccms.views.directive.trade;

// Generated 2021-6-26 20:16:25 by com.publiccms.common.generator.SourceGenerator
import java.io.IOException;

import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

import com.publiccms.logic.service.trade.TradeOrderService;
import com.publiccms.common.base.AbstractTemplateDirective;
import com.publiccms.common.handler.RenderHandler;
import com.publiccms.common.handler.PageHandler;

/**
 *
 * TradeOrderListDirective
 * 
 */
@Component
public class TradeOrderListDirective extends AbstractTemplateDirective {

    @Override
    public void execute(RenderHandler handler) throws IOException, Exception {
        PageHandler page = service.getPage(getSite(handler).getId(), getUserId(handler, "userId"), handler.getLong("paymentId"),
                handler.getIntegerArray("status"), handler.getBoolean("processed"), handler.getDate("startCreateDate"),
                handler.getDate("endCreateDate"), handler.getString("orderType"), handler.getInteger("pageIndex", 1),
                handler.getInteger("pageSize", 30));
        handler.put("page", page).render();
    }

    @Override
    public boolean needUserToken() {
        return true;
    }

    @Resource
    private TradeOrderService service;

}
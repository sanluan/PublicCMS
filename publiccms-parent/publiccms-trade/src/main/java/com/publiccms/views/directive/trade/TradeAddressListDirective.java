package com.publiccms.views.directive.trade;

// Generated 2023-8-16 by com.publiccms.common.generator.SourceGenerator

import java.io.IOException;
import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.publiccms.logic.service.trade.TradeAddressService;
import com.publiccms.common.base.AbstractTemplateDirective;
import com.publiccms.common.handler.RenderHandler;
import com.publiccms.common.handler.PageHandler;

import freemarker.template.TemplateException;
/**
 *
 * TradeAddressListDirective
 * 
 */
@Component
public class TradeAddressListDirective extends AbstractTemplateDirective {

    @Override
    public void execute(RenderHandler handler) throws IOException, TemplateException {
        PageHandler page = service.getPage(getSite(handler).getId(),handler.getLong("userId"), 
                handler.getInteger("pageIndex",1), handler.getInteger("pageSize",30));
        handler.put("page", page).render();
    }

    @Resource
    private TradeAddressService service;

}
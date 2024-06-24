package com.publiccms.views.directive.trade;

// Generated 2023-8-16 by com.publiccms.common.generator.SourceGenerator

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.publiccms.common.base.AbstractTemplateDirective;
import com.publiccms.common.handler.RenderHandler;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.entities.trade.TradeAddress;
import com.publiccms.logic.service.trade.TradeAddressService;

import freemarker.template.TemplateException;
import jakarta.annotation.Resource;
/**
 *
 * TradeAddressDirective
 * 
 */
@Component
public class TradeAddressDirective extends AbstractTemplateDirective {

    @Override
    public void execute(RenderHandler handler) throws IOException, TemplateException {
        Long id = handler.getLong("id");
        if (CommonUtils.notEmpty(id)) {
            TradeAddress entity = service.getEntity(id);
            if (null != entity) {
                handler.put("object", entity).render();
            }
        } else {
            Long[] ids = handler.getLongArray("ids");
            if (CommonUtils.notEmpty(ids)) {
                List<TradeAddress> entityList = service.getEntitys(ids);
                Map<String, TradeAddress> map = CommonUtils.listToMap(entityList, k -> k.getId().toString(), null, null);
                handler.put("map", map).render();
            }
        }
    }

    @Resource
    private TradeAddressService service;

}

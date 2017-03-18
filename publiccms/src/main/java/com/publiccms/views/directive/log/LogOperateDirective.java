package com.publiccms.views.directive.log;

// Generated 2015-5-12 12:57:43 by com.sanluan.common.source.SourceGenerator

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.publiccms.common.base.AbstractTemplateDirective;
import com.publiccms.entities.log.LogOperate;
import com.publiccms.entities.sys.SysSite;
import com.publiccms.logic.service.log.LogOperateService;
import com.sanluan.common.handler.RenderHandler;

@Component
public class LogOperateDirective extends AbstractTemplateDirective {

    @Override
    public void execute(RenderHandler handler) throws IOException, Exception {
        Long id = handler.getLong("id");
        SysSite site = getSite(handler);
        if (notEmpty(id)) {
            LogOperate entity = service.getEntity(id);
            if (null != entity && site.getId() == entity.getSiteId()) {
                handler.put("object", entity).render();
            }
        } else {
            Long[] ids = handler.getLongArray("ids");
            if (notEmpty(ids)) {
                List<LogOperate> entityList = service.getEntitys(ids);
                Map<String, LogOperate> map = new LinkedHashMap<String, LogOperate>();
                for (LogOperate entity : entityList) {
                    if (site.getId() == entity.getSiteId()) {
                        map.put(String.valueOf(entity.getId()), entity);
                    }
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
    private LogOperateService service;

}
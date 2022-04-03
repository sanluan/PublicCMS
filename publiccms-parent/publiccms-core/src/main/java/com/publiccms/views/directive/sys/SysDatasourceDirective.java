package com.publiccms.views.directive.sys;

// Generated 2021-8-2 11:31:34 by com.publiccms.common.generator.SourceGenerator

import java.io.IOException;
import java.io.StringReader;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

import com.publiccms.common.base.AbstractTemplateDirective;
import com.publiccms.common.handler.RenderHandler;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.entities.sys.SysDatasource;
import com.publiccms.logic.service.sys.SysDatasourceService;

/**
 *
 * SysDatasourceDirective
 * 
 */
@Component
public class SysDatasourceDirective extends AbstractTemplateDirective {

    @Override
    public void execute(RenderHandler handler) throws IOException, Exception {
        String id = handler.getString("id");
        if (CommonUtils.notEmpty(id)) {
            SysDatasource entity = service.getEntity(id);
            if (null != entity) {
                Properties properties = new Properties();
                properties.load(new StringReader(entity.getConfig()));
                handler.put("object", entity).put("properties", properties).render();
            }
        } else {
            Long[] ids = handler.getLongArray("ids");
            if (CommonUtils.notEmpty(ids)) {
                List<SysDatasource> entityList = service.getEntitys(ids);
                Map<String, SysDatasource> map = CommonUtils.listToMap(entityList, k -> k.getName(), null, null);
                handler.put("map", map).render();
            }
        }
    }

    @Resource
    private SysDatasourceService service;

}

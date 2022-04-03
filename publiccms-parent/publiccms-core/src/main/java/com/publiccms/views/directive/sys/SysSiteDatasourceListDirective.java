package com.publiccms.views.directive.sys;

// Generated 2015-7-22 13:48:39 by com.publiccms.common.source.SourceGenerator

import java.io.IOException;
import java.util.List;

import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

import com.publiccms.common.base.AbstractTemplateDirective;
import com.publiccms.common.handler.RenderHandler;
import com.publiccms.entities.sys.SysSiteDatasource;
import com.publiccms.logic.service.sys.SysSiteDatasourceService;

/**
 *
 * SysRoleModuleListDirective
 * 
 */
@Component
public class SysSiteDatasourceListDirective extends AbstractTemplateDirective {

    @Override
    public void execute(RenderHandler handler) throws IOException, Exception {
        List<SysSiteDatasource> list = service.getList(handler.getShort("siteId"), handler.getString("datasource"));
        handler.put("list", list).render();
    }

    @Override
    public boolean needAppToken() {
        return true;
    }

    @Resource
    private SysSiteDatasourceService service;

}
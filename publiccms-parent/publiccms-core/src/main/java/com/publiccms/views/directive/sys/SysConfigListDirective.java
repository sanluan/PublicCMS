package com.publiccms.views.directive.sys;

// Generated 2015-5-10 17:54:56 by com.publiccms.common.source.SourceGenerator

import java.io.IOException;
import java.util.Collection;

import com.publiccms.common.base.AbstractTemplateDirective;
import com.publiccms.logic.component.config.ConfigComponent;
import com.publiccms.logic.component.config.ConfigComponent.ConfigInfo;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

import com.publiccms.common.handler.RenderHandler;

/**
 *
 * SysConfigListDirective
 * 
 */
@Component
public class SysConfigListDirective extends AbstractTemplateDirective {

    @Override
    public void execute(RenderHandler handler) throws IOException, Exception {
        Collection<ConfigInfo> list = configComponent.getConfigList(getSite(handler), handler.getLocale(),
                handler.getBoolean("advanced", false));
        handler.put("list", list).render();
    }

    @Override
    public boolean needAppToken() {
        return true;
    }

    @Resource
    private ConfigComponent configComponent;
}
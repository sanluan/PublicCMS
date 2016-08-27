package com.publiccms.views.directive.sys;

// Generated 2016-7-16 11:54:15 by com.sanluan.common.source.SourceMaker

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.publiccms.common.base.AbstractTemplateDirective;
import com.publiccms.entities.sys.SysSite;
import com.publiccms.logic.component.ConfigComponent;
import com.sanluan.common.handler.RenderHandler;

@Component
public class SysConfigItemListDirective extends AbstractTemplateDirective {

    @Override
    public void execute(RenderHandler handler) throws IOException, Exception {
        String code = handler.getString("code");
        SysSite site = getSite(handler);
        if (notEmpty(code)) {
            handler.put("list", configComponent.getConfigItemList(site.getId(), code, handler.getLocale())).render();
        }
    }

    @Autowired
    private ConfigComponent configComponent;

}

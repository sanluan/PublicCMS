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
public class SysConfigExtendFieldListDirective extends AbstractTemplateDirective {

    @Override
    public void execute(RenderHandler handler) throws IOException, Exception {
        String code = handler.getString("code");
        String subcode = handler.getString("subcode");
        SysSite site = getSite(handler);
        if (notEmpty(code) && notEmpty(subcode)) {
            handler.put("list", configComponent.getExtendFieldList(site.getId(), code, subcode, handler.getLocale())).render();
        }
    }

    @Autowired
    private ConfigComponent configComponent;

}

package com.publiccms.views.directive.sys;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.publiccms.common.base.AbstractTemplateDirective;
import com.publiccms.common.handler.RenderHandler;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.logic.component.config.ConfigComponent;

/**
 *
 * SysConfigFieldListDirective
 * 
 */
@Component
public class SysConfigFieldListDirective extends AbstractTemplateDirective {

    @Override
    public void execute(RenderHandler handler) throws IOException, Exception {
        String code = handler.getString("code");
        if (CommonUtils.notEmpty(code)) {
            handler.put("list",
                    configComponent.getFieldList(getSite(handler), code, handler.getBoolean("customed"), handler.getLocale()))
                    .render();
        }
    }

    @Override
    public boolean needAppToken() {
        return true;
    }

    @Autowired
    private ConfigComponent configComponent;

}

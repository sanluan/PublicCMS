package org.publiccms.views.directive.sys;

// Generated 2016-7-16 11:54:15 by com.publiccms.common.source.SourceGenerator
import static com.publiccms.common.tools.CommonUtils.notEmpty;

import java.io.IOException;

import org.publiccms.common.base.AbstractTemplateDirective;
import org.publiccms.logic.component.config.ConfigComponent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.publiccms.common.handler.RenderHandler;

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
        if (notEmpty(code)) {
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

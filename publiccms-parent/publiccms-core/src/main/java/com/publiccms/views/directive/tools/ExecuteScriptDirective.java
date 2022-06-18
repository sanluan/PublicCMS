package com.publiccms.views.directive.tools;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.publiccms.common.base.AbstractTemplateDirective;
import com.publiccms.common.constants.CommonConstants;
import com.publiccms.common.handler.RenderHandler;
import com.publiccms.common.tools.LanguagesUtils;
import com.publiccms.logic.component.site.ScriptComponent;

/**
 *
 * ExecuteScriptDirective
 * 
 */
@Component
public class ExecuteScriptDirective extends AbstractTemplateDirective {

    @Override
    public void execute(RenderHandler handler) throws IOException, Exception {
        String command = handler.getString("command");
        String[] parameters = handler.getStringArray("parameters");
        if (siteComponent.isMaster(getSite(handler).getId())) {
            try {
                handler.print(scriptComponent.execute(command, parameters, handler.getLong("timeout", 1l)));
            } catch (IOException | InterruptedException e) {
                handler.print(e.getMessage());
            }
        } else {
            handler.print(
                    LanguagesUtils.getMessage(CommonConstants.applicationContext, handler.getLocale(), "verify.custom.noright"));
        }
        handler.render();
    }

    @Override
    public boolean needAppToken() {
        return true;
    }

    @Autowired
    protected ScriptComponent scriptComponent;
}

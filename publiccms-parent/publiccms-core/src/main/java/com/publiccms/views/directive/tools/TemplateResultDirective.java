package com.publiccms.views.directive.tools;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.publiccms.common.base.AbstractTemplateDirective;
import com.publiccms.common.handler.RenderHandler;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.common.tools.FreeMarkerUtils;
import com.publiccms.logic.component.template.TemplateComponent;

import freemarker.template.TemplateException;

/**
 *
 * TemplateResultDirective
 * 
 */
@Component
public class TemplateResultDirective extends AbstractTemplateDirective {

    @Override
    public void execute(RenderHandler handler) throws IOException, Exception {
        String content = handler.getString("templateContent");
        if (CommonUtils.notEmpty(content)) {
            try {
                content = "<#attempt>" + content + "<#recover>${.error!}</#attempt>";
                Map<String, Object> model = new HashMap<>();
                expose(handler, model);
                model.remove("templateContent");
                handler.print(FreeMarkerUtils.generateStringByString(content, templateComponent.getWebConfiguration(), model));
            } catch (IOException | TemplateException e) {
                handler.print(e.getMessage());
            }
        }
    }

    @Override
    public boolean needAppToken() {
        return true;
    }

    @Autowired
    private TemplateComponent templateComponent;

}

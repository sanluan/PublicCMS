package com.publiccms.views.directive.tools;

import java.io.IOException;
import java.util.Map;
import java.util.regex.Matcher;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.publiccms.common.base.AbstractTemplateDirective;
import com.publiccms.common.handler.RenderHandler;
import com.publiccms.common.handler.TemplateDirectiveHandler;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.logic.component.template.DiyComponent;
import com.publiccms.views.pojo.diy.CmsLayout;

import freemarker.core.Environment;
import freemarker.template.Template;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;

/**
 *
 * MetadataDirective
 * 
 */
@Component
public class DiyLayoutDirective extends AbstractTemplateDirective {

    @SuppressWarnings("unchecked")
    @Override
    public void execute(Environment environment, @SuppressWarnings("rawtypes") Map parameters, TemplateModel[] loopVars,
            TemplateDirectiveBody templateDirectiveBody) throws TemplateException, IOException {
        try {
            RenderHandler handler = new TemplateDirectiveHandler(parameters, loopVars, environment, templateDirectiveBody);
            String id = handler.getString("id");
            if (CommonUtils.notEmpty(id)) {
                CmsLayout layout = diyComponent.getLayout(getSite(handler), id);
                String template = layout.getTemplate();
                Matcher matcher = CmsLayout.PLACE_PATTERN.matcher(template);
                StringBuffer sb = new StringBuffer();
                int index = 0, end = 0;
                while (matcher.find()) {
                    sb.append(template.substring(end, matcher.start()));
                    environment.include(new Template(layout.getName(), sb.toString(), environment.getConfiguration()));
                    sb.setLength(0);
                    handler.put("index", index++).render();
                    end = matcher.end();
                }
                if (end < template.length()) {
                    sb.append(template.substring(end, template.length()));
                    environment.include(new Template(layout.getName(), sb.toString(), environment.getConfiguration()));
                }
            }
        } catch (IOException e) {
            throw e;
        } catch (Exception e) {
            throw new TemplateException(e, environment);
        }
    }

    @Override
    public boolean needAppToken() {
        return true;
    }

    @Autowired
    private DiyComponent diyComponent;

    @Override
    public void execute(RenderHandler handler) throws IOException, Exception {
        // nouse
    }
}

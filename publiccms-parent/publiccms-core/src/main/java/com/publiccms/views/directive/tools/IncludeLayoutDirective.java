package com.publiccms.views.directive.tools;

import java.io.IOException;
import java.util.Map;
import java.util.regex.Matcher;

import javax.annotation.Resource;
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
 * includeLayout 包含diy布局指令
 * <p>
 * 参数列表
 * <ul>
 * <li><code>id</code>:布局id
 * </ul>
 * <p>
 * 打印包含结果
 * <p>
 * 使用示例
 * <p>
 * &lt;@tools.includeLayout
 * id='00000000-0000-0000-0000-000000000000'&gt;${index},&lt;/@tools.includeLayout&gt;
 * 
 */
@Component
public class IncludeLayoutDirective extends AbstractTemplateDirective {

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
    public boolean httpEnabled() {
        return false;
    }

    @Resource
    private DiyComponent diyComponent;

    @Override
    public void execute(RenderHandler handler) throws IOException, Exception {
        // nouse
    }
}

package com.publiccms.views.directive.tools;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

import com.publiccms.common.base.AbstractTemplateDirective;
import com.publiccms.common.handler.RenderHandler;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.common.tools.FreeMarkerUtils;
import com.publiccms.logic.component.template.TemplateComponent;

import freemarker.template.TemplateException;

/**
 * templateResult 模板渲染结果指令
 * <p>
 * 参数列表
 * <ul>
 * <li><code>parameters</code> 参数map
 * <li><code>templateContent</code> 模板内容
 * </ul>
 * 打印渲染结果
 * <p>
 * 使用示例
 * <p>
 * &lt;@tools.templateResult templateContent='${name}' parameters={'name':'value'}/&gt;
 * 
 * <pre>
&lt;script&gt;
 $.getJSON('${site.dynamicPath}api/directive/tools/templateResult?path=$%7Bname%7D&amp;parameters_name=value&amp;appToken=接口访问授权Token', function(data){    
   console.log(data);
 });
 &lt;/script&gt;
 * </pre>
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
                model.putAll(handler.getMap("parameters"));
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

    @Resource
    private TemplateComponent templateComponent;

}

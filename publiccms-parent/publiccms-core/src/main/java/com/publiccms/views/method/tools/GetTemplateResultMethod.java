package com.publiccms.views.method.tools;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.publiccms.common.base.BaseMethod;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.common.tools.FreeMarkerUtils;

import freemarker.cache.StringTemplateLoader;
import freemarker.core.TemplateClassResolver;
import freemarker.template.Configuration;
import freemarker.template.SimpleHash;
import freemarker.template.TemplateExceptionHandler;
import freemarker.template.TemplateModel;
import freemarker.template.TemplateModelException;

/**
 *
 * getTemplateResult 获取模板解析结果
 * <p>
 * 参数列表
 * <ol>
 * <li>模板内容
 * </ol>
 * <p>
 * 返回结果
 * <ul>
 * <li><code>string</code>:解析结果
 * </ul>
 * 使用示例
 * <p>
 * ${getTemplateResult('${.now}')}
 * <p>
 *
 * <pre>
&lt;script&gt;
$.getJSON('${site.dynamicPath}api/method/getDate?appToken=接口访问授权Token&amp;parameters=${.now}', function(data){
console.log(data);
});
&lt;/script&gt;
 * </pre>
 */
@Component
public class GetTemplateResultMethod extends BaseMethod {
    private Configuration configuration;

    /**
     *
     */
    public GetTemplateResultMethod() {
        configuration = new Configuration(Configuration.DEFAULT_INCOMPATIBLE_IMPROVEMENTS);
        configuration.setDefaultEncoding(StandardCharsets.UTF_8.name());
        configuration.setURLEscapingCharset(StandardCharsets.UTF_8.name());
        configuration.setTemplateUpdateDelayMilliseconds(0);
        configuration.setAPIBuiltinEnabled(false);
        configuration.setNewBuiltinClassResolver(TemplateClassResolver.ALLOWS_NOTHING_RESOLVER);
        configuration.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
        configuration.setLogTemplateExceptions(false);
        configuration.setBooleanFormat("c");
        configuration.setTemplateLoader(new StringTemplateLoader());
        Map<String, Object> freemarkerVariables = new HashMap<>();
        try {
            configuration.setAllSharedVariables(new SimpleHash(freemarkerVariables, configuration.getObjectWrapper()));
        } catch (TemplateModelException e) {
        }
    }

    @Override
    public Object execute(List<TemplateModel> arguments) throws TemplateModelException {
        String template = getString(0, arguments);
        if (CommonUtils.notEmpty(template)) {
            try {
                return FreeMarkerUtils.generateStringByString(template, configuration, null);
            } catch (Exception e) {
                return e.getMessage();
            }
        }
        return null;
    }

    @Override
    public boolean needAppToken() {
        return true;
    }

    @Override
    public int minParametersNumber() {
        return 1;
    }
}

package com.publiccms.views.method.tools;

import java.util.List;

import org.springframework.stereotype.Component;

import com.publiccms.common.base.BaseMethod;
import com.publiccms.logic.component.template.TemplateCacheComponent;

import freemarker.template.TemplateModel;
import freemarker.template.TemplateModelException;
import jakarta.annotation.Resource;

/**
 *
 * getParameterTypeList 获取参数类型list
 * <p lang="zh">返回结果
 * <p lang="en">return result
 * <p lang="ja">戻り値
 * <ul>
 * <li><code>list</code>:参数类型名称
 * </ul>
 * <p lang="zh">使用示例
 * <p lang="en">usage example
 * <p lang="ja">使用例
 * <p>
 * &lt;#list getParameterTypeList() as a&gt;${a}&lt;#sep&gt;,&lt;/#list&gt;
 * <p>
 *
 * <pre>
&lt;script&gt;
$.getJSON('${site.dynamicPath}api/method/getParameterTypeList?appToken=接口访问授权Token', function(data){
console.log(data);
});
&lt;/script&gt;
 * </pre>
 */
@Component
public class GetParameterTypeListMethod extends BaseMethod {
    @Resource
    private TemplateCacheComponent templateCacheComponent;

    @Override
    public Object execute(List<TemplateModel> arguments) throws TemplateModelException {
        return templateCacheComponent.getParameterTypeHandlerMap().keySet();
    }

    @Override
    public boolean needAppToken() {
        return true;
    }

    @Override
    public int minParametersNumber() {
        return 0;
    }
}

package com.publiccms.views.method.tools;

import java.util.List;

import org.springframework.stereotype.Component;

import com.publiccms.common.base.BaseMethod;
import com.publiccms.logic.component.workflow.ProcessComponent;

import freemarker.template.TemplateModel;
import freemarker.template.TemplateModelException;
import jakarta.annotation.Resource;

/**
 *
 * getProcessTypeList 获取参数类型list
 * <p>返回结果
 * <ul>
 * <li><code>list</code>:参数类型名称
 * </ul>
 * <p>使用示例
 * <p>
 * &lt;#list getProcessTypeList() as a&gt;${a}&lt;#sep&gt;,&lt;/#list&gt;
 * <p>
 *
 * <pre>
&lt;script&gt;
$.getJSON('${site.dynamicPath}api/method/getProcessTypeList?appToken=接口访问授权Token', function(data){
console.log(data);
});
&lt;/script&gt;
 * </pre>
 */
@Component
public class GetProcessTypeListMethod extends BaseMethod {
    @Resource
    private ProcessComponent processComponent;

    @Override
    public Object execute(List<TemplateModel> arguments) throws TemplateModelException {
        return processComponent.getWorkflowHandlerMap().keySet();
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

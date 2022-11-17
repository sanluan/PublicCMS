package com.publiccms.views.method.tools;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Component;

import com.publiccms.common.base.BaseMethod;

import freemarker.template.TemplateModel;
import freemarker.template.TemplateModelException;

/**
*
* getUUID 获取UUID
* <p>
* <p>
* 返回结果
* <ul>
* <li><code>string</code> 随机uuid
* </ul>
* 使用示例
* <p>
* ${getUUID()}
* <p>
* 
* <pre>
&lt;script&gt;
$.getJSON('${site.dynamicPath}api/method/getUUID', function(data){
console.log(data);
});
&lt;/script&gt;
* </pre>
*/
@Component
public class GetUUIDMethod extends BaseMethod {

    @Override
    public Object execute(List<TemplateModel> arguments) throws TemplateModelException {
        return UUID.randomUUID();
    }
    
    @Override
    public boolean needAppToken() {
        return false;
    }

    @Override
    public int minParametersNumber() {
        return 0;
    }
}

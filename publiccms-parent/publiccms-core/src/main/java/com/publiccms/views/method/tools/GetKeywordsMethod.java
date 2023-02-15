package com.publiccms.views.method.tools;

import java.util.List;

import com.publiccms.logic.service.tools.HqlService;
import javax.annotation.Resource;
import org.springframework.stereotype.Component;

import com.publiccms.common.base.BaseMethod;

import freemarker.template.TemplateModel;
import freemarker.template.TemplateModelException;

/**
*
* getKeywords 获取分词结果
* <p>
* 参数列表
* <ol>
* <li>完整语句
* </ol>
* <p>
* 返回结果
* <ul>
* <li><code>list</code>(<code>string</code>):分词结果set
* </ul>
* 使用示例
* <p>
* &lt;#list getKeywords('这是一段话') as a&gt;${a}&lt;#sep&gt;,&lt;/#list&gt;&lt;
* <p>
* 
* <pre>
&lt;script&gt;
$.getJSON('${site.dynamicPath}api/method/getKeywords?parameters=这是一段话', function(data){
console.log(data);
});
&lt;/script&gt;
* </pre>
*/
@Component
public class GetKeywordsMethod extends BaseMethod {

    @Override
    public Object execute(List<TemplateModel> arguments) throws TemplateModelException {
        return service.getToken(getString(0, arguments));
    }

    @Override
    public boolean needAppToken() {
        return false;
    }

    @Override
    public int minParametersNumber() {
        return 1;
    }

    @Resource
    private HqlService service;
}

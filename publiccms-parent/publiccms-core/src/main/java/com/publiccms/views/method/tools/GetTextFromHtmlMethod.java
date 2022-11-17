package com.publiccms.views.method.tools;

import java.util.List;

import org.springframework.stereotype.Component;

import com.publiccms.common.base.BaseMethod;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.common.tools.HtmlUtils;

import freemarker.template.TemplateModel;
import freemarker.template.TemplateModelException;

/**
*
* getTextFromHtml 获取html中的文本
* <p>
* 参数列表
* <ol>
* <li>html内容
* </ol>
* <p>
* 返回结果
* <ul>
* <li><code>string</code> 文本结果
* </ul>
* 使用示例
* <p>
* ${getTextFromHtml('&lt;a href="http://www.publiccms.com/"&gt;publiccms&lt;/a&gt;')}
* <p>
* 
* <pre>
&lt;script&gt;
$.getJSON('${site.dynamicPath}api/method/getTextFromHtml?parameters=&lt;a href="http://www.publiccms.com/"&gt;publiccms&lt;/a&gt;', function(data){
console.log(data);
});
&lt;/script&gt;
* </pre>
*/
@Component
public class GetTextFromHtmlMethod extends BaseMethod {

    @Override
    public Object execute(List<TemplateModel> arguments) throws TemplateModelException {
        String html = getString(0, arguments);
        if (CommonUtils.notEmpty(html)) {
            return HtmlUtils.removeHtmlTag(html);
        }
        return null;
    }
    
    @Override
    public boolean needAppToken() {
        return false;
    }

    @Override
    public int minParametersNumber() {
        return 1;
    }
}

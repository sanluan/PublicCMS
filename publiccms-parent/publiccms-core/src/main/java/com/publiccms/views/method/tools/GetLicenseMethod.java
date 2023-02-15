package com.publiccms.views.method.tools;

import java.util.List;

import com.publiccms.common.constants.CmsVersion;
import org.springframework.stereotype.Component;

import com.publiccms.common.base.BaseMethod;

import freemarker.template.TemplateModel;
import freemarker.template.TemplateModelException;

/**
*
* getLicense 获取授权
* <p>
* 返回结果
* <ul>
* <li><code>license</code>:授权数据{@link com.publiccms.common.copyright.License}
* </ul>
* 使用示例
* <p>
* ${getLicense().domain}
* <p>
* 
* <pre>
&lt;script&gt;
$.getJSON('${site.dynamicPath}api/method/getLicense?appToken=接口访问授权Token, function(data){
console.log(data);
});
&lt;/script&gt;
* </pre>
*/
@Component
public class GetLicenseMethod extends BaseMethod {

    @Override
    public Object execute(List<TemplateModel> arguments) throws TemplateModelException {
        return CmsVersion.getLicense();
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

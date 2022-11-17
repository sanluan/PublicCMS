package com.publiccms.views.method.tools;

import java.util.List;

import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

import com.publiccms.common.base.BaseMethod;
import com.publiccms.logic.component.site.SiteComponent;

import freemarker.template.TemplateModel;
import freemarker.template.TemplateModelException;

/**
*
* getDictEnable 获取词典是否启用
* <p>
* 返回结果
* <ul>
* <li><code>boolean</code> 词典是否启用
* </ul>
* 使用示例
* <p>
* ${getDictEnable()}
* <p>
* 
* <pre>
&lt;script&gt;
$.getJSON('${site.dynamicPath}api/method/getDictEnable', function(data){
console.log(data);
});
&lt;/script&gt;
* </pre>
*/
@Component
public class GetDictEnableMethod extends BaseMethod {
    @Resource
    private SiteComponent siteComponent;

    @Override
    public Object execute(List<TemplateModel> arguments) throws TemplateModelException {
        return siteComponent.isDictEnable();
    }

    @Override
    public int minParametersNumber() {
        return 0;
    }

    @Override
    public boolean needAppToken() {
        return false;
    }
}

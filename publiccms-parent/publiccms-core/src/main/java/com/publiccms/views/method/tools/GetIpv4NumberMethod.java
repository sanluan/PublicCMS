package com.publiccms.views.method.tools;

import java.util.List;

import org.springframework.stereotype.Component;

import com.publiccms.common.base.BaseMethod;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.common.tools.IpUtils;

import freemarker.template.TemplateModel;
import freemarker.template.TemplateModelException;

/**
 *
 * getIpv4Number 获取ip的数字值
 * <p>
 * 参数列表
 * <ol>
 * <li>ip
 * </ol>
 * <p>
 * 返回结果
 * <ul>
 * <li><code>number</code>:数字
 * </ul>
 * 使用示例
 * <p>
 * ${getIpv4Number('127.0.0.1')}
 * <p>
 * 
 * <pre>
&lt;script&gt;
$.getJSON('${site.dynamicPath}api/method/getIpv4Number?parameters=127.0.0.1', function(data){
console.log(data);
});
&lt;/script&gt;
 * </pre>
 */
@Component
public class GetIpv4NumberMethod extends BaseMethod {

    @Override
    public Object execute(List<TemplateModel> arguments) throws TemplateModelException {
        String string = getString(0, arguments);
        if (CommonUtils.notEmpty(string)) {
            return String.valueOf(IpUtils.getIpv4Number(string));
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

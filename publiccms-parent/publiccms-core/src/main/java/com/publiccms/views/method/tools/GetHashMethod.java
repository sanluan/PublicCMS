package com.publiccms.views.method.tools;

import java.util.List;

import org.springframework.stereotype.Component;

import com.publiccms.common.base.BaseMethod;
import com.publiccms.common.tools.CommonUtils;

import freemarker.template.TemplateModel;
import freemarker.template.TemplateModelException;

/**
 *
 * getHash 获取哈希值
 * <p>
 * 参数列表
 * <ol>
 * <li>字符串
 * </ol>
 * <p>
 * 返回结果
 * <ul>
 * <li><code>string</code> 哈希值
 * </ul>
 * 使用示例
 * <p>
 * ${getHash('aaa')}
 * <p>
 * 
 * <pre>
&lt;script&gt;
$.getJSON('//cms.publiccms.com/api/method/getHash?parameters=aaa', function(data){
console.log(data);
});
&lt;/script&gt;
 * </pre>
 */
@Component
public class GetHashMethod extends BaseMethod {

    @Override
    public Object execute(List<TemplateModel> arguments) throws TemplateModelException {
        String string = getString(0, arguments);
        if (CommonUtils.notEmpty(string)) {
            return string.hashCode();
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

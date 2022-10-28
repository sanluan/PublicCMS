package com.publiccms.views.method.tools;

import java.util.List;

import org.springframework.stereotype.Component;

import com.publiccms.common.base.BaseMethod;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.common.tools.VerificationUtils;

import freemarker.template.TemplateModel;
import freemarker.template.TemplateModelException;

/**
 *
 * getMd5 获取md5值
 * <p>
 * 参数列表
 * <ol>
 * <li>字符串
 * </ol>
 * <p>
 * 返回结果
 * <ul>
 * <li><code>string</code> md5值
 * </ul>
 * 使用示例
 * <p>
 * ${getMd5('aaa')}
 * <p>
 * 
 * <pre>
&lt;script&gt;
$.getJSON('//cms.publiccms.com/api/method/getMd5?parameters=aaa', function(data){
console.log(data);
});
&lt;/script&gt;
 * </pre>
 */
@Component
public class GetMd5Method extends BaseMethod {

    @Override
    public Object execute(List<TemplateModel> arguments) throws TemplateModelException {
        String string = getString(0, arguments);
        if (CommonUtils.notEmpty(string)) {
            return VerificationUtils.md5Encode(string);
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

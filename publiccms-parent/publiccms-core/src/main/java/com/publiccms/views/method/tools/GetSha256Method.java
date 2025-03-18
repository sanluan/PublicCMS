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
 * getSha256 获取sha256
 * <p lang="zh">参数列表
 * <p lang="en">parameter list
 * <p lang="ja">パラメータリスト
 * <ol>
 * <li>字符串
 * </ol>
 * <p lang="zh">返回结果
 * <p lang="en">return result
 * <p lang="ja">戻り値
 * <ul>
 * <li><code>string</code>:sha256值
 * </ul>
 * <p lang="zh">使用示例
 * <p lang="en">usage example
 * <p lang="ja">使用例
 * <p>
 * ${getSha256('aaa')}
 * <p>
 *
 * <pre>
&lt;script&gt;
$.getJSON('${site.dynamicPath}api/method/getSha256?parameters=aaa', function(data){
console.log(data);
});
&lt;/script&gt;
 * </pre>
 */
@Component
public class GetSha256Method extends BaseMethod {

    @Override
    public Object execute(List<TemplateModel> arguments) throws TemplateModelException {
        String string = getString(0, arguments);
        if (CommonUtils.notEmpty(string)) {
            return VerificationUtils.sha256Encode(string);
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

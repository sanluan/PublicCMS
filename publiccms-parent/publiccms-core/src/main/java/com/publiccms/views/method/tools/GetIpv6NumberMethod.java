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
 * getIpv6Number 获取ip的数字值
 * <p lang="zh">参数列表
 * <p lang="en">parameter list
 * <p lang="ja">パラメータリスト
 * <ol>
 * <li>ip
 * </ol>
 * <p lang="zh">返回结果
 * <p lang="en">return result
 * <p lang="ja">戻り値
 * <ul>
 * <li><code>number string</code>:字符格式数字
 * </ul>
 * <p lang="zh">使用示例
 * <p lang="en">usage example
 * <p lang="ja">使用例
 * <p>
 * ${getIpv6Number('FF01:0:0:0:0:0:0:1101')}
 * <p>
 *
 * <pre>
&lt;script&gt;
$.getJSON('${site.dynamicPath}api/method/getIpv4Number?parameters=FF01:0:0:0:0:0:0:1101', function(data){
console.log(data);
});
&lt;/script&gt;
 * </pre>
 */
@Component
public class GetIpv6NumberMethod extends BaseMethod {

    @Override
    public Object execute(List<TemplateModel> arguments) throws TemplateModelException {
        String string = getString(0, arguments);
        if (CommonUtils.notEmpty(string)) {
            return IpUtils.getIpv6Number(string);
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

package com.publiccms.views.method.tools;

import java.util.List;

import org.springframework.stereotype.Component;

import com.publiccms.common.base.BaseMethod;
import com.publiccms.common.constants.CommonConstants;
import com.publiccms.common.tools.CommonUtils;

import freemarker.template.TemplateModel;
import freemarker.template.TemplateModelException;

/**
 *
 * getRandom 获取随机数字
 * <p>
 * 参数列表
 * <ol>
 * <li><code>number</code>最大数字,可以为空
 * </ol>
 * <p>
 * 返回结果
 * <ul>
 * <li><code>number</code> 随机数字
 * </ul>
 * 使用示例
 * <p>
 * ${getRandom()}
 * <p>
 * ${getRandom(100)}
 * <p>
 * 
 * <pre>
&lt;script&gt;
$.getJSON('${site.dynamicPath}api/method/getRandom?parameters=100', function(data){
console.log(data);
});
&lt;/script&gt;
 * </pre>
 */
@Component
public class GetRandomMethod extends BaseMethod {

    @Override
    public Object execute(List<TemplateModel> arguments) throws TemplateModelException {
        Integer max = getInteger(0, arguments);
        if (CommonUtils.notEmpty(max)) {
            return CommonConstants.random.nextInt(max);
        }
        return CommonConstants.random.nextInt();
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

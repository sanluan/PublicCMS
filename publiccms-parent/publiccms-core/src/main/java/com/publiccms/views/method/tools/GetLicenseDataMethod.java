package com.publiccms.views.method.tools;

import java.util.List;

import org.springframework.stereotype.Component;

import com.publiccms.common.base.BaseMethod;
import com.publiccms.common.constants.CmsVersion;
import com.publiccms.common.tools.LicenseUtils;
import com.publiccms.common.tools.VerificationUtils;

import freemarker.template.TemplateModel;
import freemarker.template.TemplateModelException;

/**
*
* getLicenseData 获取授权数据
* <p>
* 返回结果
* <ul>
* <li><code>string</code> base64编码的授权数据
* </ul>
* 使用示例
* <p>
* ${getLicenseData()}
* <p>
* 
* <pre>
&lt;script&gt;
$.getJSON('//cms.publiccms.com/api/method/getLicenseData, function(data){
console.log(data);
});
&lt;/script&gt;
* </pre>
*/
@Component
public class GetLicenseDataMethod extends BaseMethod {

    @Override
    public Object execute(List<TemplateModel> arguments) throws TemplateModelException {
        return VerificationUtils.base64Encode(LicenseUtils.getLicenseDate(CmsVersion.getLicense()));
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

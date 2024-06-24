package com.publiccms.views.directive.tools;

import java.io.IOException;

import org.springframework.stereotype.Component;

import com.publiccms.common.base.AbstractTemplateDirective;
import com.publiccms.common.copyright.License;
import com.publiccms.common.handler.RenderHandler;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.common.tools.LicenseUtils;
import com.publiccms.common.tools.VerificationUtils;

import freemarker.template.TemplateException;

/**
*
* licenseVerify 授权文件验证指令
* <p>
* 参数列表
* <ul>
* <li><code>licenseData</code>:授权文件数据
* </ul>
* <p>
* 返回结果
* <ul>
* <li><code>result</code>:验证结果,【true:验证成功,false:验证失败】
* </ul>
* 使用示例
* <p>
* &lt;@tools.licenseVerify licenseData='content'&gt;${url}&lt;/@tools.licenseVerify&gt;
* 
* <pre>
&lt;script&gt;
$.getJSON('${site.dynamicPath}api/directive/tools/licenseVerify?licenseData=content', function(data){    
 console.log(data);
});
&lt;/script&gt;
* </pre>
*/
@Component
public class LicenseVerifyDirective extends AbstractTemplateDirective {

    @Override
    public void execute(RenderHandler handler) throws IOException, TemplateException {
        String licenseData = handler.getString("licenseData");
        handler.put("result", false);
        if (CommonUtils.notEmpty(licenseData)) {
            License license = LicenseUtils.readLicense(VerificationUtils.base64Decode(licenseData));
            handler.put("result", LicenseUtils.verifyLicenseDate(license));
        }
        handler.render();
    }

}

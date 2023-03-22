package com.publiccms.views.method.tools;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.publiccms.common.base.BaseMethod;
import com.publiccms.common.constants.CmsVersion;
import com.publiccms.common.constants.CommonConstants;
import com.publiccms.common.tools.CmsFileUtils;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.common.tools.TemplateModelUtils;
import com.publiccms.common.tools.VerificationUtils;
import com.publiccms.entities.sys.SysSite;
import com.publiccms.logic.component.config.ConfigComponent;
import com.publiccms.logic.component.config.SafeConfigComponent;

import freemarker.core.Environment;
import freemarker.template.TemplateModel;
import freemarker.template.TemplateModelException;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;

/**
 *
 * getPrivateUrl 获取私有文件绝对路径
 * <p>
 * 参数列表
 * <ol>
 * <li><code>url</code>,文件url
 * <li><code>string</code>,文件名,可以为空
 * </ol>
 * <p>
 * 返回结果
 * <ul>
 * <li><code>url</code>:绝对路径的url
 * </ul>
 * 使用示例
 * <p>
 * ${getUrl('index.html')}
 * <p>
 * 
 * <pre>
&lt;script&gt;
$.getJSON('${site.dynamicPath}api/method/getPrivateUrl?parameters=index.html', function(data){
console.log(data);
});
&lt;/script&gt;
 * </pre>
 */
@Component
public class GetPrivateUrlMethod extends BaseMethod {
    @Resource
    private ConfigComponent configComponent;

    @Override
    public Object execute(HttpServletRequest request, List<TemplateModel> arguments) throws TemplateModelException {
        SysSite site = (SysSite) request.getAttribute("site");
        return execute(site, arguments);
    }

    @Override
    public Object execute(List<TemplateModel> arguments) throws TemplateModelException {
        TemplateModel model = Environment.getCurrentEnvironment().getGlobalVariable(CommonConstants.getAttributeSite());
        SysSite site = null;
        if (null != model) {
            site = (SysSite) TemplateModelUtils.converBean(model);
        }
        return execute(site, arguments);
    }

    public Object execute(SysSite site, List<TemplateModel> arguments) throws TemplateModelException {
        String url = getString(0, arguments);
        String filename = getString(1, arguments);
        if (CommonUtils.notEmpty(url) && null != site) {
            Map<String, String> config = configComponent.getConfigData(site.getId(), SafeConfigComponent.CONFIG_CODE);
            String signKey = config.get(SafeConfigComponent.CONFIG_PRIVATEFILE_KEY);
            if (null == signKey) {
                signKey = CmsVersion.getClusterId();
            }
            int expiryMinutes = ConfigComponent.getInt(config.get(SafeConfigComponent.CONFIG_EXPIRY_MINUTES_SIGN),
                    SafeConfigComponent.DEFAULT_EXPIRY_MINUTES_SIGN);
            long expiry = System.currentTimeMillis() + expiryMinutes * 60 * 1000;
            String string = CmsFileUtils.getPrivateFileSignString(expiry, url);
            String sign = VerificationUtils.base64Encode(VerificationUtils.encryptAES(string, signKey));
            try {
                if (CommonUtils.notEmpty(filename)) {
                    return CommonUtils.joinString(site.getDynamicPath(), "file/private?expiry=", expiry, "&sign=",
                            URLEncoder.encode(sign, CommonConstants.DEFAULT_CHARSET_NAME), "&filePath=",
                            URLEncoder.encode(url, CommonConstants.DEFAULT_CHARSET_NAME), "&filename=",
                            URLEncoder.encode(filename, CommonConstants.DEFAULT_CHARSET_NAME));
                } else {
                    return CommonUtils.joinString(site.getDynamicPath(), "file/private?expiry=", expiry, "&sign=",
                            URLEncoder.encode(sign, CommonConstants.DEFAULT_CHARSET_NAME), "&filePath=",
                            URLEncoder.encode(url, CommonConstants.DEFAULT_CHARSET_NAME));
                }
            } catch (UnsupportedEncodingException e) {
            }
        }
        return url;
    }

    @Override
    public boolean needAppToken() {
        return true;
    }

    @Override
    public int minParametersNumber() {
        return 1;
    }
}

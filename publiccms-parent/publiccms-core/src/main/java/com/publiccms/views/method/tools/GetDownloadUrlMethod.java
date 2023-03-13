package com.publiccms.views.method.tools;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Component;

import com.publiccms.common.base.BaseMethod;
import com.publiccms.common.constants.CommonConstants;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.common.tools.TemplateModelUtils;
import com.publiccms.entities.sys.SysSite;

import freemarker.core.Environment;
import freemarker.template.TemplateModel;
import freemarker.template.TemplateModelException;

/**
 *
 * getDownloadUrl 获取下载url绝对路径
 * <p>
 * 参数列表
 * <ol>
 * <li><code>url</code>,文件路径
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
$.getJSON('${site.dynamicPath}api/method/getDownloadUrl?parameters=index.html', function(data){
console.log(data);
});
&lt;/script&gt;
 * </pre>
 */
@Component
public class GetDownloadUrlMethod extends BaseMethod {

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
        if (CommonUtils.notEmpty(url) && null != site) {
            try {
                return CommonUtils.joinString(site.getDynamicPath(), "file/download?filePath=",
                        URLEncoder.encode(url, CommonConstants.DEFAULT_CHARSET_NAME));
            } catch (UnsupportedEncodingException e) {
            }
        }
        return url;
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

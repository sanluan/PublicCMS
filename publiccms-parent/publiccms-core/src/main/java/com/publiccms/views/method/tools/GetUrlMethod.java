package com.publiccms.views.method.tools;

import java.util.List;

import org.springframework.stereotype.Component;

import com.publiccms.common.base.BaseMethod;
import com.publiccms.common.constants.CommonConstants;
import com.publiccms.common.tools.CmsUrlUtils;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.common.tools.TemplateModelUtils;
import com.publiccms.entities.sys.SysSite;
import com.publiccms.logic.component.site.FileUploadComponent;

import freemarker.core.Environment;
import freemarker.template.TemplateModel;
import freemarker.template.TemplateModelException;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;

/**
 *
 * getUrl 获取url绝对路径
 * <p>
 * 参数列表
 * <ol>
 * <li>url前缀
 * <li><code>url</code>,当第二个为空时,将第一个参数当作url
 * </ol>
 * <p>
 * 返回结果
 * <ul>
 * <li><code>url</code>:绝对路径的url
 * </ul>
 * 使用示例
 * <p>
 * ${getUrl(site.sitePath,'index.html')} ${getUrl('index.html')}
 * <p>
 * 
 * <pre>
&lt;script&gt;
$.getJSON('${site.dynamicPath}api/method/getUrl?parameters=index.html', function(data){
console.log(data);
});
&lt;/script&gt;
 * </pre>
 */
@Component
public class GetUrlMethod extends BaseMethod {
    @Resource
    protected FileUploadComponent fileUploadComponent;

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
        String sitePath = getString(0, arguments);
        String url = getString(1, arguments);
        if (CommonUtils.notEmpty(sitePath) && CommonUtils.notEmpty(url)) {
            return CmsUrlUtils.getUrl(sitePath, url);
        } else if (CommonUtils.notEmpty(sitePath) && null != site) {
            return CmsUrlUtils.getUrl(fileUploadComponent.getPrefix(site, false), sitePath);
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

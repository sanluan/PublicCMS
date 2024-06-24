package com.publiccms.views.method.tools;

import java.util.List;

import org.springframework.stereotype.Component;

import com.publiccms.common.base.BaseMethod;
import com.publiccms.common.constants.CommonConstants;
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
 * getFileUploadPrefix 获取
 * <p>
 * 参数列表
 * <ol>
 * </ol>
 * <p>
 * 返回结果
 * <ul>
 * <li><code>url</code>:文件上传前缀
 * </ul>
 * 使用示例
 * <p>
 * ${getFileUploadPrefix()}
 * <p>
 * 
 * <pre>
&lt;script&gt;
$.getJSON('${site.dynamicPath}api/method/getFileUploadPrefix', function(data){
console.log(data);
});
&lt;/script&gt;
 * </pre>
 */
@Component
public class GetFileUploadPrefixMethod extends BaseMethod {
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
        return fileUploadComponent.getPrefix(site, !arguments.isEmpty() && TemplateModelUtils.converBoolean(arguments.get(0)));
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

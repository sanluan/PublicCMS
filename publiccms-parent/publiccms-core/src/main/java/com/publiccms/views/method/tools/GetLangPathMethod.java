package com.publiccms.views.method.tools;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Component;

import com.publiccms.common.base.BaseMethod;
import com.publiccms.common.constants.CommonConstants;
import com.publiccms.common.constants.Constants;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.common.tools.TemplateModelUtils;
import com.publiccms.entities.sys.SysSite;
import com.publiccms.logic.component.config.SiteAttributeComponent;
import com.publiccms.logic.component.site.FileUploadComponent;

import freemarker.core.Environment;
import freemarker.template.TemplateModel;
import freemarker.template.TemplateModelException;

/**
 *
 * getLangPath 获取站点多语言地址
 * <p>
 * 参数列表
 * <ol>
 * <li><code>lang</code>,语言
 * </ol>
 * <p>
 * 返回结果
 * <ul>
 * <li><code>lang path</code>:站点多语言地址
 * </ul>
 * <p>
 * 使用示例
 * <p>
 * ${getLangPath('cn')}
 * <p>
 *
 * <pre>
&lt;script&gt;
fetch('${site.dynamicPath}api/method/getLangPath?parameters=cn').then(res => res.json()).then(data=>{
console.log(data);
});
&lt;/script&gt;
 * </pre>
 */
@Component
public class GetLangPathMethod extends BaseMethod {
    @Resource
    protected FileUploadComponent fileUploadComponent;
    @Resource
    protected SiteAttributeComponent siteAttributeComponent;

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
        String lang = getString(0, arguments);
        if (site.isUseStatic()) {
            if (siteAttributeComponent.enableMultilingual(site.getId())) {
                String defaultLang = siteAttributeComponent.getDefaultLanguage(site.getId());
                if (CommonUtils.notEmpty(lang) && !lang.equalsIgnoreCase(defaultLang)) {
                    return CommonUtils.joinString(site.getSitePath(), lang, Constants.SEPARATOR);
                } else {
                    return site.getSitePath();
                }
            } else {
                return site.getSitePath();
            }
        } else {
            return site.getDynamicPath();
        }
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

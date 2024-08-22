package com.publiccms.views.method.cms;

import java.util.Collections;
import java.util.List;

import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Component;

import com.publiccms.common.base.BaseMethod;
import com.publiccms.common.constants.CommonConstants;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.common.tools.ExtendUtils;
import com.publiccms.common.tools.TemplateModelUtils;
import com.publiccms.entities.sys.SysSite;
import com.publiccms.logic.component.config.ContentConfigComponent;
import com.publiccms.logic.service.cms.CmsContentAttributeService;

import freemarker.core.Environment;
import freemarker.template.TemplateModel;
import freemarker.template.TemplateModelException;

/**
 *
 * getContentAttribute 获取内容扩展数据
 * <p>
 * 参数列表
 * <ol>
 * <li>内容id
 * </ol>
 * <p>
 * 返回结果
 * <ul>
 * <li><code>attribute</code>:内容扩展数据(字段编码,<code>value</code>)
 * </ul>
 * 使用示例
 * <p>
 * &lt;#assign attribute=getContentAttribute(1)/&lt;
 * <p>
 * ${(attribute.text?no_esc)!}
 * <p>
 * 
 * <pre>
&lt;script&gt;
$.getJSON('${site.dynamicPath}api/method/getContentAttribute?appToken=接口访问授权Token&amp;parameters=1', function(data){
console.log(data.text);
});
&lt;/script&gt;
 * </pre>
 */
@Component
public class GetContentAttributeMethod extends BaseMethod {
    @Resource
    protected ContentConfigComponent contentConfigComponent;

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
        Long id = getLong(0, arguments);
        Boolean replaceKeywords = getBoolean(1, arguments);
        if (CommonUtils.notEmpty(id) && null != site) {
            return ExtendUtils.getAttributeMap(service.getEntity(id),
                    null == replaceKeywords || Boolean.TRUE.equals(replaceKeywords)
                            ? contentConfigComponent.getKeywordsConfig(site.getId())
                            : null);
        }
        return Collections.emptyMap();
    }

    @Override
    public boolean needAppToken() {
        return true;
    }

    @Override
    public int minParametersNumber() {
        return 1;
    }

    @Resource
    private CmsContentAttributeService service;

}

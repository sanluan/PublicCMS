package com.publiccms.views.method.cms;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.stream.Collectors;

import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Component;

import com.publiccms.common.base.BaseMethod;
import com.publiccms.common.constants.CommonConstants;
import com.publiccms.common.constants.Constants;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.common.tools.ExtendUtils;
import com.publiccms.common.tools.TemplateModelUtils;
import com.publiccms.entities.sys.SysSite;
import com.publiccms.logic.component.config.ContentConfigComponent;
import com.publiccms.logic.component.config.ContentConfigComponent.KeywordsConfig;
import com.publiccms.logic.service.cms.CmsContentAttributeService;

import freemarker.core.Environment;
import freemarker.template.TemplateModel;
import freemarker.template.TemplateModelException;

/**
 *
 * getContentAttributes 获取多个内容扩展数据
 * <p>
 * 参数列表
 * <ol>
 * <li>多个内容id
 * </ol>
 * <p>
 * 返回结果
 * <ul>
 * <li><code>map</code>(id,<code>attribute</code>:内容扩展数据(字段编码,<code>value</code>))
 * </ul>
 * 使用示例
 * <p>
 * &lt;#assign attributeMap=getContentAttributes('1,2,3,4')/&lt;
 * <p>
 * ${attributeMap['1'].text?no_esc!}
 * <p>
 * 
 * <pre>
&lt;script&gt;
$.getJSON('${site.dynamicPath}api/method/getContentAttributes?appToken=接口访问授权Token&amp;parameters=1,2,3,4', function(data){
console.log(data);
});
&lt;/script&gt;
 * </pre>
 */
@Component
public class GetContentAttributesMethod extends BaseMethod {
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
        Long[] ids = getLongArray(0, arguments);
        if (CommonUtils.notEmpty(ids) && null != site) {
            KeywordsConfig config = contentConfigComponent.getKeywordsConfig(site.getId());
            return service.getEntitys(ids).stream().collect(Collectors.toMap(k -> String.valueOf(k.getContentId()),
                    v -> ExtendUtils.getAttributeMap(v, config), Constants.defaultMegerFunction(), LinkedHashMap::new));
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

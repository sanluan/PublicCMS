package com.publiccms.views.method.cms;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.publiccms.common.base.BaseMethod;
import com.publiccms.common.constants.Constants;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.common.tools.ExtendUtils;
import com.publiccms.logic.service.cms.CmsContentAttributeService;

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

    @Override
    public Object execute(List<TemplateModel> arguments) throws TemplateModelException {
        Long[] ids = getLongArray(0, arguments);
        if (CommonUtils.notEmpty(ids)) {
            return service.getEntitys(ids).stream().collect(Collectors.toMap(k -> String.valueOf(k.getContentId()),
                    ExtendUtils::getAttributeMap, Constants.defaultMegerFunction(), LinkedHashMap::new));
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

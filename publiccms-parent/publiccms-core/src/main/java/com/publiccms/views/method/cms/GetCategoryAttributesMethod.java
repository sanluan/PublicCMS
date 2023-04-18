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
import com.publiccms.logic.service.cms.CmsCategoryAttributeService;

import freemarker.template.TemplateModel;
import freemarker.template.TemplateModelException;

/**
 *
 * getCategoryAttributes 获取多个分类扩展数据
 * <p>
 * 参数列表
 * <ol>
 * <li>多个分类id
 * </ol>
 * <p>
 * 返回结果
 * <ul>
 * <li><code>map</code>(id,<code>attribute</code>:分类扩展数据(字段编码,<code>value</code>))
 * </ul>
 * 使用示例
 * <p>
 * &lt;#assign attributeMap=getCategoryAttributes('1,2,3,4')/&lt;
 * <p>
 * ${attributeMap['1'].title!}
 * <p>
 * 
 * <pre>
&lt;script&gt;
$.getJSON('${site.dynamicPath}api/method/getCategoryAttributes?parameters=1,2,3,4', function(data){
console.log(data);
});
&lt;/script&gt;
 * </pre>
 */
@Component
public class GetCategoryAttributesMethod extends BaseMethod {

    @Override
    public Object execute(List<TemplateModel> arguments) throws TemplateModelException {
        Integer[] ids = getIntegerArray(0, arguments);
        if (CommonUtils.notEmpty(ids)) {
            return service.getEntitys(ids).stream().collect(Collectors.toMap(k -> String.valueOf(k.getCategoryId()),
                    ExtendUtils::getAttributeMap, Constants.defaultMegerFunction(), LinkedHashMap::new));
        }
        return Collections.emptyMap();
    }

    @Override
    public boolean needAppToken() {
        return false;
    }

    @Override
    public int minParametersNumber() {
        return 1;
    }

    @Resource
    private CmsCategoryAttributeService service;
}

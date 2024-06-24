package com.publiccms.views.method.cms;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.stream.Collectors;

import jakarta.annotation.Resource;

import org.springframework.stereotype.Component;

import com.publiccms.common.base.BaseMethod;
import com.publiccms.common.constants.Constants;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.common.tools.ExtendUtils;
import com.publiccms.logic.service.cms.CmsPlaceAttributeService;

import freemarker.template.TemplateModel;
import freemarker.template.TemplateModelException;

/**
 *
 * getPlaceAttributes 获取多个推荐位数据扩展数据
 * <p>
 * 参数列表
 * <ol>
 * <li>多个荐位数据id
 * </ol>
 * <p>
 * 返回结果
 * <ul>
 * <li><code>map</code>(id,<code>attribute</code>:推荐位数据扩展数据(字段编码,<code>value</code>))
 * </ul>
 * 使用示例
 * <p>
 * &lt;#assign attributeMap=getPlaceAttributes('1,2,3,4')/&lt;
 * <p>
 * ${attributeMap['1'].description!}
 * <p>
 * 
 * <pre>
&lt;script&gt;
$.getJSON('${site.dynamicPath}api/method/getPlaceAttributes?parameters=1,2,3,4', function(data){
console.log(data);
});
&lt;/script&gt;
 * </pre>
 */
@Component
public class GetPlaceAttributesMethod extends BaseMethod {

    @Override
    public Object execute(List<TemplateModel> arguments) throws TemplateModelException {
        Long[] ids = getLongArray(0, arguments);
        if (CommonUtils.notEmpty(ids)) {
            return service.getEntitys(ids).stream().collect(Collectors.toMap(k -> String.valueOf(k.getPlaceId()),
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
    private CmsPlaceAttributeService service;

}

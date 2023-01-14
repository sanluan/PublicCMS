package com.publiccms.views.method.cms;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.publiccms.common.base.BaseMethod;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.common.tools.ExtendUtils;
import com.publiccms.entities.cms.CmsCategoryAttribute;
import com.publiccms.logic.service.cms.CmsCategoryAttributeService;

import freemarker.template.TemplateModel;
import freemarker.template.TemplateModelException;

/**
 *
 * getCategoryAttribute 获取分类扩展数据
 * <p>
 * 参数列表
 * <ol>
 * <li>分类id
 * </ol>
 * <p>
 * 返回结果
 * <ul>
 * <li><code>attribute</code>分类扩展数据(字段编码,<code>value</code>)
 * </ul>
 * 使用示例
 * <p>
 * &lt;#assign attribute=getCategoryAttribute(1)/&lt;
 * <p>
 * ${attribute.title!}
 * <p>
 * 
 * <pre>
&lt;script&gt;
$.getJSON('${site.dynamicPath}api/method/getCategoryAttribute?parameters=1', function(data){
console.log(data.title);
});
&lt;/script&gt;
 * </pre>
 */
@Component
public class GetCategoryAttributeMethod extends BaseMethod {

    @Override
    public Object execute(List<TemplateModel> arguments) throws TemplateModelException {
        Integer id = getInteger(0, arguments);
        if (CommonUtils.notEmpty(id)) {
            CmsCategoryAttribute entity = service.getEntity(id);
            if (null != entity) {
                Map<String, String> map = ExtendUtils.getExtendMap(entity.getData());
                map.put("title", entity.getTitle());
                map.put("keywords", entity.getKeywords());
                map.put("description", entity.getDescription());
                return map;
            }
        }
        return Collections.EMPTY_MAP;
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

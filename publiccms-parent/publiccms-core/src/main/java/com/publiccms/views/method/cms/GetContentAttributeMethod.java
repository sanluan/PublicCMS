package com.publiccms.views.method.cms;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.publiccms.common.base.BaseMethod;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.common.tools.ExtendUtils;
import com.publiccms.entities.cms.CmsContentAttribute;
import com.publiccms.logic.service.cms.CmsContentAttributeService;

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
* <li><code>attribute</code>内容扩展数据(字段编码,<code>value</code>)
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

    @Override
    public Object execute(List<TemplateModel> arguments) throws TemplateModelException {
        Long id = getLong(0, arguments);
        if (CommonUtils.notEmpty(id)) {
            CmsContentAttribute entity = service.getEntity(id);
            if (null != entity) {
                Map<String, String> map = ExtendUtils.getExtendMap(entity.getData());
                map.put("text", entity.getText());
                map.put("source", entity.getSource());
                map.put("sourceUrl", entity.getSourceUrl());
                map.put("wordCount", String.valueOf(entity.getWordCount()));
                map.put("minPrice", String.valueOf(entity.getMinPrice()));
                map.put("maxPrice", String.valueOf(entity.getMaxPrice()));
                return map;
            }
        }
        return null;
    }
    
    @Override
    public boolean needAppToken() {
        return true;
    }

    @Override
    public int minParametersNumber() {
        return 1;
    }

    @Autowired
    private CmsContentAttributeService service;
    
}

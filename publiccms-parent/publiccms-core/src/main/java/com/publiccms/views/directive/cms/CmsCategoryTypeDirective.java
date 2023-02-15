package com.publiccms.views.directive.cms;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

import com.publiccms.common.base.AbstractTemplateDirective;
import com.publiccms.common.handler.RenderHandler;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.entities.sys.SysSite;
import com.publiccms.logic.component.template.ModelComponent;
import com.publiccms.views.pojo.entities.CmsCategoryType;

/**
 *
 * categoryType 分类类型查询指令
 * <p>
 * 参数列表
 * <ul>
 * <li><code>id</code>:分类id,结果返回<code>object</code>
 * {@link com.publiccms.views.pojo.entities.CmsCategoryType}
 * <li><code>ids</code>:
 * 多个分类id,逗号或空格间隔,当id或code为空时生效,结果返回<code>map</code>(id,<code>object</code>)
 * </ul>
 * 使用示例
 * <p>
 * &lt;@cms.categoryType id=1&gt;${object.name}&lt;/@cms.categoryType&gt;
 * <p>
 * &lt;@cms.categoryType ids=1,2,3&gt;&lt;#list map as
 * k,v&gt;${k}:${v.name}&lt;#sep&gt;,&lt;/#list&gt;&lt;/@cms.categoryType&gt;
 * 
 * <pre>
  &lt;script&gt;
   $.getJSON('${site.dynamicPath}api/directive/cms/categoryType?id=banner', function(data){    
     console.log(data.name);
   });
   &lt;/script&gt;
 * </pre>
 */
@Component
public class CmsCategoryTypeDirective extends AbstractTemplateDirective {

    @Override
    public void execute(RenderHandler handler) throws IOException, Exception {
        String id = handler.getString("id");
        SysSite site = getSite(handler);
        if (CommonUtils.notEmpty(id)) {
            CmsCategoryType entity = modelComponent.getCategoryType(site.getId(), id);
            if (null != entity) {
                handler.put("object", entity).render();
            }
        } else {
            String[] ids = handler.getStringArray("ids");
            if (CommonUtils.notEmpty(ids)) {
                Map<String, CmsCategoryType> typeMap = modelComponent.getCategoryTypeMap(site.getId());
                Map<String, CmsCategoryType> map = new LinkedHashMap<>();
                for (String typeId : ids) {
                    map.put(typeId, typeMap.get(typeId));
                }
                handler.put("map", map).render();
            }
        }
    }

    @Resource
    private ModelComponent modelComponent;

}

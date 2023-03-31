package com.publiccms.views.directive.cms;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.annotation.Resource;
import org.springframework.stereotype.Component;

import com.publiccms.common.base.AbstractTemplateDirective;
import com.publiccms.common.handler.RenderHandler;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.entities.sys.SysSite;
import com.publiccms.logic.component.template.ModelComponent;
import com.publiccms.views.pojo.entities.CmsModel;

import freemarker.template.TemplateException;

/**
 *
 * model 页面片段数据查询指令
 * <p>
 * 参数列表
 * <ul>
 * <li><code>id</code>:内容模型id,结果返回<code>object</code>
 * {@link com.publiccms.views.pojo.entities.CmsModel}
 * <li><code>ids</code>:
 * 多个内容模型id,逗号或空格间隔,当id为空时生效,结果返回<code>map</code>(id,<code>object</code>)
 * </ul>
 * 使用示例
 * <p>
 * &lt;@cms.model id='article'&gt;${object.name}&lt;/@cms.model&gt;
 * <p>
 * &lt;@cms.model ids='article,link,picture'&gt;&lt;#list map as
 * k,v&gt;${k}:${v.name}&lt;#sep&gt;,&lt;/#list&gt;&lt;/@cms.model&gt;
 * 
 * <pre>
  &lt;script&gt;
   $.getJSON('${site.dynamicPath}api/directive/cms/model?id=article', function(data){    
     console.log(data.name);
   });
   &lt;/script&gt;
 * </pre>
 */
@Component
public class CmsModelDirective extends AbstractTemplateDirective {

    @Override
    public void execute(RenderHandler handler) throws IOException, TemplateException {
        String id = handler.getString("id");
        SysSite site = getSite(handler);
        if (CommonUtils.notEmpty(id)) {
            CmsModel entity = modelComponent.getModel(site, id);
            if (null != entity) {
                handler.put("object", entity).render();
            }
        } else {
            String[] ids = handler.getStringArray("ids");
            if (CommonUtils.notEmpty(ids)) {
                Map<String, CmsModel> modelMap = modelComponent.getModelMap(site);
                Map<String, CmsModel> map = new LinkedHashMap<>();
                for (String modelId : ids) {
                    map.put(modelId, modelMap.get(modelId));
                }
                handler.put("map", map).render();
            }
        }
    }

    @Resource
    private ModelComponent modelComponent;

}

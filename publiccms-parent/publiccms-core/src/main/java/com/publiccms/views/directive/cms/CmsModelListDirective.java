package com.publiccms.views.directive.cms;

// Generated 2015-5-10 17:54:56 by com.publiccms.common.source.SourceGenerator

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.publiccms.common.base.AbstractTemplateDirective;
import com.publiccms.common.handler.PageHandler;
import com.publiccms.common.handler.RenderHandler;
import com.publiccms.logic.component.template.ModelComponent;

/**
 *
 * modelList 内容模型列表查询指令
 * <p>
 * 参数列表
 * <ul>
 * <li><code>parentId</code> 父内容模型id
 * <li><code>hasChild</code> 拥有子模型，【true,false】
 * <li><code>onlyUrl</code> 外链,【true,false】
 * <li><code>hasImages</code> 拥有图片列表，【true,false】
 * <li><code>hasFiles</code> 拥有文件列表，【true,false】
 * </ul>
 * <p>
 * 返回结果
 * <ul>
 * <li><code>page</code> {@link com.publiccms.common.handler.PageHandler}
 * <li><code>page.list</code> List类型 查询结果实体列表
 * {@link com.publiccms.views.pojo.entities.CmsModel}
 * </ul>
 * 使用示例
 * <p>
 * &lt;@_modelList&gt;&lt;#list page.list as
 * a&gt;${a.name}&lt;#sep&gt;,&lt;/#list&gt;&lt;/@_modelList&gt;
 * 
 * <pre>
 &lt;script&gt;
  $.getJSON('//cms.publiccms.com/api/directive/modelList', function(data){    
    console.log(data.totalCount);
  });
  &lt;/script&gt;
 * </pre>
 */
@Component
public class CmsModelListDirective extends AbstractTemplateDirective {

    @Override
    public void execute(RenderHandler handler) throws IOException, Exception {
        PageHandler page = new PageHandler(null, null);
        page.setList(modelComponent.getModelList(getSite(handler), handler.getString("parentId"), handler.getBoolean("hasChild"),
                handler.getBoolean("onlyUrl"), handler.getBoolean("hasImages"), handler.getBoolean("hasFiles")));
        handler.put("page", page).render();
    }

    @Autowired
    private ModelComponent modelComponent;
}
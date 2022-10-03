package com.publiccms.views.directive.cms;

// Generated 2016-2-26 15:57:04 by com.publiccms.common.generator.SourceGenerator

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.publiccms.common.base.AbstractTemplateDirective;
import com.publiccms.common.handler.PageHandler;
import com.publiccms.common.handler.RenderHandler;
import com.publiccms.logic.component.template.ModelComponent;

/**
 *
 * categoryTypeList 分类类型列表查询指令
 * <p>
 * 没有可用于查询得参数,该指令固定返回所有结果,不可分页查询
 * <p>
 * 返回结果
 * <ul>
 * <li><code>page</code> {@link com.publiccms.common.handler.PageHandler}
 * <li><code>page.list</code> List类型 查询结果实体列表
 * {@link com.publiccms.views.pojo.entities.CmsCategoryType}
 * </ul>
 * 使用示例
 * <p>
 * &lt;@cms.categoryTypeList&gt;&lt;#list page.list as
 * a&gt;${a.name}&lt;#sep&gt;,&lt;/#list&gt;&lt;/@cms.categoryTypeList&gt;
 * 
 * <pre>
  &lt;script&gt;
   $.getJSON('//cms.publiccms.com/api/directive/cms/categoryTypeList', function(data){    
     console.log(data.totalCount);
   });
   &lt;/script&gt;
 * </pre>
 */
@Component
public class CmsCategoryTypeListDirective extends AbstractTemplateDirective {

    @Override
    public void execute(RenderHandler handler) throws IOException, Exception {
        PageHandler page = new PageHandler(null, null);
        page.setList(modelComponent.getCategoryTypeList(getSite(handler)));
        handler.put("page", page).render();
    }

    @Autowired
    private ModelComponent modelComponent;

}
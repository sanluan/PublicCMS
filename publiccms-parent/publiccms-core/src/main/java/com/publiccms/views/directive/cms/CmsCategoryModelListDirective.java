package com.publiccms.views.directive.cms;

// Generated 2015-5-10 17:54:56 by com.publiccms.common.generator.SourceGenerator

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.publiccms.common.base.AbstractTemplateDirective;
import com.publiccms.common.handler.RenderHandler;
import com.publiccms.entities.cms.CmsCategoryModel;
import com.publiccms.logic.service.cms.CmsCategoryModelService;

/**
 *
 * categoryModelList 分类模型映射列表查询指令
 * <p>
 * 参数列表
 * <ul>
 * <li><code>modelId</code> 内容模型id
 * <li><code>categoryId</code> 分类id
 * </ul>
 * <p>
 * 返回结果
 * <ul>
 * <li><code>list</code> List类型 查询结果实体列表
 * {@link com.publiccms.entities.cms.CmsCategoryModel}
 * </ul>
 * 使用示例
 * <p>
 * &lt;@cms.categoryModelList modelId='article'&gt;&lt;#list page.list as
 * a&gt;${a.templatePath}&lt;#sep&gt;,&lt;/#list&gt;&lt;/@cms.categoryModelList&gt;
 * 
 * <pre>
  &lt;script&gt;
   $.getJSON('//cms.publiccms.com/api/directive/cms/categoryModelList?modelId=article', function(data){    
     console.log(data[0].totalCount);
   });
   &lt;/script&gt;
 * </pre>
 */
@Component
public class CmsCategoryModelListDirective extends AbstractTemplateDirective {

    @Override
    public void execute(RenderHandler handler) throws IOException, Exception {
        List<CmsCategoryModel> list = service.getList(handler.getString("modelId"), handler.getInteger("categoryId"));
        handler.put("list", list).render();
    }

    @Autowired
    private CmsCategoryModelService service;

}
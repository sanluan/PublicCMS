package com.publiccms.views.directive.cms;

// Generated 2016-2-26 15:57:04 by com.publiccms.common.source.SourceGenerator

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
 * 没有可用于查询得参数，该指令固定返回所有结果，不可分页查询
 * <p>
 * 返回结果page子属性:
 * <ul>
 * <li><code>totalCount</code> int类型 数据总数
 * <li><code>pageIndex</code> int类型 当前页码
 * <li><code>list</code> List类型 查询结果实体列表
 * {@link com.publiccms.views.pojo.entities.CmsCategoryType}
 * <li><code>totalPage</code> int类型 总页数
 * <li><code>firstResult</code> int类型 第一条序号
 * <li><code>firstPage</code> boolean类型 是否第一页
 * <li><code>lastPage</code> boolean类型 是否最后一页
 * <li><code>nextPage</code> int类型 下一页页码
 * <li><code>prePage</code> int类型 上一页页码
 * </ul>
 * 使用示例
 * <p>
 * &lt;@_categoryList&gt;&lt;#list page.list as
 * a&gt;${a.name}&lt;#sep&gt;,&lt;/#list&gt;&lt;/@_categoryList&gt;
 * 
 * <pre>
  &lt;script&gt;
   $.getJSON('//cms.publiccms.com/api/directive/categoryTypeList', function(data){    
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
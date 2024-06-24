package com.publiccms.views.directive.sys;

// Generated 2016-1-19 11:41:45 by com.publiccms.common.generator.SourceGenerator

import java.io.IOException;

import org.springframework.stereotype.Component;

import com.publiccms.common.base.AbstractTemplateDirective;
import com.publiccms.common.handler.PageHandler;
import com.publiccms.common.handler.RenderHandler;
import com.publiccms.logic.service.sys.SysDeptItemService;

import jakarta.annotation.Resource;
import freemarker.template.TemplateException;

/**
 *
 * sysDeptItemList 部门数据授权列表查询指令
 * <p>
 * 参数列表
 * <ul>
 * <li><code>deptId</code>:部门id
 * <li><code>itemType</code>:项目类型
 * <li><code>itemId</code>:项目id
 * <li><code>pageIndex</code>:页码
 * <li><code>pageSize</code>:每页条数
 * </ul>
 * <p>
 * 返回结果
 * <ul>
 * <li><code>page</code>:{@link com.publiccms.common.handler.PageHandler}
 * <li><code>page.list</code>:List类型 查询结果实体列表
 * {@link com.publiccms.entities.sys.SysDeptItem}
 * </ul>
 * 使用示例
 * <p>
 * &lt;@sys.deptCategoryList deptId=1 pageSize=10&gt;&lt;#list page.list as
 * a&gt;${a.id.deptId}&lt;#sep&gt;,&lt;/#list&gt;&lt;/@sys.deptCategoryList&gt;
 *
 * <pre>
 &lt;script&gt;
  $.getJSON('${site.dynamicPath}api/directive/sys/deptCategoryList?deptId=1&amp;pageSize=10&amp;appToken=接口访问授权Token', function(data){
    console.log(data.page.totalCount);
  });
  &lt;/script&gt;
 * </pre>
 */
@Component
public class SysDeptItemListDirective extends AbstractTemplateDirective {

    @Override
    public void execute(RenderHandler handler) throws IOException, TemplateException {
        PageHandler page = service.getPage(handler.getInteger("deptId"), handler.getString("itemType"),
                handler.getString("itemId"), handler.getInteger("pageIndex", 1), handler.getInteger("pageSize"));
        handler.put("page", page).render();
    }

    @Override
    public boolean needAppToken() {
        return true;
    }

    @Resource
    private SysDeptItemService service;

}
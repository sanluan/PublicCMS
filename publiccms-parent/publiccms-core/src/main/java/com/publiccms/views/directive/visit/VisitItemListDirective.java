package com.publiccms.views.directive.visit;

// Generated 2021-1-14 22:44:12 by com.publiccms.common.generator.SourceGenerator
import java.io.IOException;

import javax.annotation.Resource;
import org.springframework.stereotype.Component;

import com.publiccms.common.base.AbstractTemplateDirective;
import com.publiccms.common.handler.PageHandler;
import com.publiccms.common.handler.RenderHandler;
import com.publiccms.logic.service.visit.VisitItemService;

import freemarker.template.TemplateException;

/**
 *
 * visitItemList 访问项目列表查询指令
 * <p>
 * 参数列表
 * <ul>
 * <li><code>startVisitDate</code>:起始访问日期,【2020-01-01 23:59:59】,【2020-01-01】
 * <li><code>endVisitDate</code>:终止访问日期,【2020-01-01 23:59:59】,【2020-01-01】
 * <li><code>itemType</code>:项目类型,【category,content,user等页面统计时中的itemType】
 * <li><code>itemId</code>:项目id
 * <li><code>pageIndex</code>:页码
 * <li><code>pageSize</code>:每页条数
 * </ul>
 * <p>
 * 返回结果
 * <ul>
 * <li><code>page</code>:{@link com.publiccms.common.handler.PageHandler}
 * <li><code>page.list</code>:List类型 查询结果实体列表
 * {@link com.publiccms.entities.visit.VisitItem}
 * </ul>
 * 使用示例
 * <p>
 * &lt;@visit.itemList itemType='content'&gt;&lt;#list page.list as
 * a&gt;${a.pv}&lt;#sep&gt;,&lt;/#list&gt;&lt;/@visit.itemList&gt;
 * 
 * <pre>
&lt;script&gt;
$.getJSON('${site.dynamicPath}api/directive/visit/itemList?itemType=content&amp;appToken=接口访问授权Token', function(data){    
  console.log(data.page.totalCount);
});
&lt;/script&gt;
 * </pre>
 */
@Component
public class VisitItemListDirective extends AbstractTemplateDirective {

    @Override
    public void execute(RenderHandler handler) throws IOException, TemplateException {
        PageHandler page = service.getPage(getSite(handler).getId(), handler.getDate("startVisitDate"),
                handler.getDate("endVisitDate"), handler.getString("itemType"), handler.getString("itemId"),
                handler.getInteger("pageIndex", 1), handler.getInteger("pageSize", 30));
        handler.put("page", page).render();
    }

    @Override
    public boolean needAppToken() {
        return true;
    }

    @Resource
    private VisitItemService service;

}
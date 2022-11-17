package com.publiccms.views.directive.visit;

// Generated 2021-1-14 22:44:12 by com.publiccms.common.generator.SourceGenerator
import java.io.IOException;

import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

import com.publiccms.common.base.AbstractTemplateDirective;
import com.publiccms.common.handler.PageHandler;
import com.publiccms.common.handler.RenderHandler;
import com.publiccms.logic.service.visit.VisitDayService;

/**
 *
 * visitDayList 访问日报表列表查询指令
 * <p>
 * 参数列表
 * <ul>
 * <li><code>startVisitDate</code> 起始访问日期,【2020-01-01 23:59:59】,【2020-01-01】
 * <li><code>endVisitDate</code> 终止访问日期,【2020-01-01 23:59:59】,【2020-01-01】
 * <li><code>hourAnalytics</code> 小时统计,【true,false】,默认false
 * <li><code>pageIndex</code> 页码
 * <li><code>pageSize</code> 每页条数
 * </ul>
 * <p>
 * 返回结果
 * <ul>
 * <li><code>page</code> {@link com.publiccms.common.handler.PageHandler}
 * <li><code>page.list</code> List类型 查询结果实体列表
 * {@link com.publiccms.entities.visit.VisitDay}
 * </ul>
 * 使用示例
 * <p>
 * &lt;@visit.dayList hourAnalytics=false&gt;&lt;#list page.list as
 * a&gt;${a.pv}&lt;#sep&gt;,&lt;/#list&gt;&lt;/@visit.dayList&gt;
 * 
 * <pre>
&lt;script&gt;
 $.getJSON('${site.dynamicPath}api/directive/visit/dayList?hourAnalytics=false&amp;appToken=接口访问授权Token', function(data){    
   console.log(data.totalCount);
 });
 &lt;/script&gt;
 * </pre>
 */
@Component
public class VisitDayListDirective extends AbstractTemplateDirective {

    @Override
    public void execute(RenderHandler handler) throws IOException, Exception {
        PageHandler page = service.getPage(getSite(handler).getId(), handler.getDate("startVisitDate"),
                handler.getDate("endVisitDate"), handler.getBoolean("hourAnalytics", false), handler.getInteger("pageIndex", 1),
                handler.getInteger("pageSize", 30));
        handler.put("page", page).render();
    }

    @Override
    public boolean needAppToken() {
        return true;
    }

    @Resource
    private VisitDayService service;

}
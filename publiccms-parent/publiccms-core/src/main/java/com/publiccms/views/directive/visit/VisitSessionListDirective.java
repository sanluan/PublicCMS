package com.publiccms.views.directive.visit;

// Generated 2021-1-14 22:44:06 by com.publiccms.common.generator.SourceGenerator
import java.io.IOException;

import javax.annotation.Resource;
import org.springframework.stereotype.Component;

import com.publiccms.common.base.AbstractTemplateDirective;
import com.publiccms.common.handler.RenderHandler;
import com.publiccms.logic.service.visit.VisitSessionService;
import com.publiccms.common.handler.PageHandler;

/**
 *
 * visitSessionList 访问会话报表列表查询指令
 * <p>
 * 参数列表
 * <ul>
 * <li><code>sessionId</code>:会话id
 * <li><code>ip</code>:ip
 * <li><code>startVisitDate</code>:起始访问日期,【2020-01-01 23:59:59】,【2020-01-01】
 * <li><code>endVisitDate</code>:终止访问日期,【2020-01-01 23:59:59】,【2020-01-01】
 * <li><code>orderType</code>:排序类型,【asc:正序,desc:倒序】,默认为创建日期倒序
 * <li><code>pageIndex</code>:页码
 * <li><code>pageSize</code>:每页条数
 * </ul>
 * <p>
 * 返回结果
 * <ul>
 * <li><code>page</code>:{@link com.publiccms.common.handler.PageHandler}
 * <li><code>page.list</code>:List类型 查询结果实体列表
 * {@link com.publiccms.entities.visit.VisitSession}
 * </ul>
 * 使用示例
 * <p>
 * &lt;@visit.sessionList&gt;&lt;#list page.list as
 * a&gt;${a.pv}&lt;#sep&gt;,&lt;/#list&gt;&lt;/@visit.sessionList&gt;
 * 
 * <pre>
&lt;script&gt;
$.getJSON('${site.dynamicPath}api/directive/visit/sessionList?appToken=接口访问授权Token', function(data){    
  console.log(data.totalCount);
});
&lt;/script&gt;
 * </pre>
 */
@Component
public class VisitSessionListDirective extends AbstractTemplateDirective {

    @Override
    public void execute(RenderHandler handler) throws IOException, Exception {
        PageHandler page = service.getPage(getSite(handler).getId(), handler.getString("sessionId"), handler.getString("ip"),
                handler.getDate("startVisitDate"), handler.getDate("endVisitDate"), handler.getString("orderType"),
                handler.getInteger("pageIndex", 1), handler.getInteger("pageSize", 30));
        handler.put("page", page).render();
    }

    @Override
    public boolean needAppToken() {
        return true;
    }

    @Resource
    private VisitSessionService service;

}
package com.publiccms.views.directive.cms;

// Generated 2020-3-26 12:04:23 by com.publiccms.common.generator.SourceGenerator
import java.io.IOException;

import javax.annotation.Resource;
import org.springframework.stereotype.Component;

import com.publiccms.logic.service.cms.CmsVoteService;
import com.publiccms.common.base.AbstractTemplateDirective;
import com.publiccms.common.handler.RenderHandler;
import com.publiccms.common.handler.PageHandler;

/**
 *
 * voteList 投票列表查询指令
 * <p>
 * 参数列表
 * <ul>
 * <li><code>startStartDate</code> 起始开始日期,【2020-01-01 23:59:59】,【2020-01-01】
 * <li><code>endStartDate</code> 终止开始日期,【2020-01-01 23:59:59】,【2020-01-01】
 * <li><code>startEndDate</code> 起始结束日期,【2020-01-01 23:59:59】,【2020-01-01】
 * <li><code>endEndDate</code> 终止结束日期,【2020-01-01 23:59:59】,【2020-01-01】
 * <li><code>advanced</code> 开启高级选项, 默认为<code>false</code>
 * <li><code>disabled</code> 高级选项:禁用状态,默认为<code>false</code>
 * <li><code>title</code> 高级选项:标题
 * <li><code>orderField</code> 排序字段,【startDate:开始日期,endDate:结束,votes:投票人数,createDate:创建日期】,默认id按orderType排序
 * <li><code>orderType</code> 排序类型,【asc:正序,desc:倒序】,默认为倒序
 * <li><code>pageIndex</code> 页码
 * <li><code>pageSize</code> 每页条数
 * </ul>
 * <p>
 * 返回结果
 * <ul>
 * <li><code>page</code> {@link com.publiccms.common.handler.PageHandler}
 * <li><code>page.list</code> List类型 查询结果实体列表
 * {@link com.publiccms.entities.cms.CmsVote}
 * </ul>
 * 使用示例
 * <p>
 * &lt;@cms.voteList pageSize=10&gt;&lt;#list page.list as
 * a&gt;${a.title}&lt;#sep&gt;,&lt;/#list&gt;&lt;/@cms.voteList&gt;
 * 
 * <pre>
&lt;script&gt;
$.getJSON('${site.dynamicPath}api/directive/cms/voteList?pageSize=10', function(data){    
console.log(data.totalCount);
});
&lt;/script&gt;
 * </pre>
 */
@Component
public class CmsVoteListDirective extends AbstractTemplateDirective {

    @Override
    public void execute(RenderHandler handler) throws IOException, Exception {
        Boolean disabled = false;
        String title = null;
        if (getAdvanced(handler)) {
            disabled = handler.getBoolean("disabled", false);
            title = handler.getString("title");
        }
        PageHandler page = service.getPage(getSite(handler).getId(), handler.getDate("startStartDate"),
                handler.getDate("endStartDate"), handler.getDate("startEndDate"), handler.getDate("endEndDate"), title, disabled,
                handler.getString("orderField"), handler.getString("orderType"), handler.getInteger("pageIndex", 1),
                handler.getInteger("pageSize", 30));
        handler.put("page", page).render();
    }

    @Override
    public boolean supportAdvanced() {
        return true;
    }

    @Resource
    private CmsVoteService service;

}
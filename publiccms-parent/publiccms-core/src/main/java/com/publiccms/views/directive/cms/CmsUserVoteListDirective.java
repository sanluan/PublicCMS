package com.publiccms.views.directive.cms;

// Generated 2020-3-26 11:46:48 by com.publiccms.common.generator.SourceGenerator
import java.io.IOException;

import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

import com.publiccms.logic.service.cms.CmsUserVoteService;
import com.publiccms.common.base.AbstractTemplateDirective;
import com.publiccms.common.handler.RenderHandler;
import com.publiccms.common.handler.PageHandler;

/**
 *
 * userVoteList 用户投票列表查询指令
 * <p>
 * 参数列表
 * <ul>
 * <li><code>userId</code> 用户id
 * <li><code>voteId</code> 投票id
 * <li><code>orderType</code> 排序类型,【asc:正序,desc:倒叙】，默认为创建日期倒叙
 * <li><code>pageIndex</code> 页码
 * <li><code>pageSize</code> 每页条数
 * </ul>
 * <p>
 * 返回结果
 * <ul>
 * <li><code>page</code> {@link com.publiccms.common.handler.PageHandler}
 * <li><code>page.list</code> List类型 查询结果实体列表
 * {@link com.publiccms.entities.cms.CmsUserVote}
 * </ul>
 * 使用示例
 * <p>
 * &lt;@cms.userVoteList userId=1 pageSize=10&gt;&lt;#list page.list as
 * a&gt;${a.ip}&lt;#sep&gt;,&lt;/#list&gt;&lt;/@cms.userVoteList&gt;
 * 
 * <pre>
&lt;script&gt;
$.getJSON('//cms.publiccms.com/api/directive/cms/userVoteList?userId=1&amp;pageSize=10&amp;appToken=接口访问授权Token', function(data){    
 console.log(data.totalCount);
});
&lt;/script&gt;
 * </pre>
 */
@Component
public class CmsUserVoteListDirective extends AbstractTemplateDirective {

    @Override
    public void execute(RenderHandler handler) throws IOException, Exception {
        PageHandler page = service.getPage(handler.getLong("userId"), handler.getLong("voteId"), handler.getString("orderType"),
                handler.getInteger("pageIndex", 1), handler.getInteger("pageSize", handler.getInteger("count", 30)));
        handler.put("page", page).render();
    }

    @Resource
    private CmsUserVoteService service;

}
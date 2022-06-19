package com.publiccms.views.directive.cms;

// Generated 2020-3-26 11:46:48 by com.publiccms.common.generator.SourceGenerator
import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.publiccms.logic.service.cms.CmsVoteItemService;
import com.publiccms.common.base.AbstractTemplateDirective;
import com.publiccms.common.handler.RenderHandler;
import com.publiccms.common.handler.PageHandler;

/**
*
* voteItemList 投票选项列表查询指令
* <p>
* 参数列表
* <ul>
* <li><code>voteId</code> 投票id
* <li><code>orderField</code> 排序字段,【scores:分数,sort:排序正序】,默认sort按正序
* <li><code>orderType</code> 排序类型,【asc:正序,desc:倒叙】，默认为倒叙
* <li><code>pageIndex</code> 页码
* <li><code>pageSize</code> 每页条数
* </ul>
* <p>
* 返回结果
* <ul>
* <li><code>page</code> {@link com.publiccms.common.handler.PageHandler}
* <li><code>page.list</code> List类型 查询结果实体列表
* {@link com.publiccms.entities.cms.CmsVoteItem}
* </ul>
* 使用示例
* <p>
* &lt;@cms.voteItemList voteId=1 pageSize=10&gt;&lt;#list page.list as
* a&gt;${a.title}&lt;#sep&gt;,&lt;/#list&gt;&lt;/@cms.voteItemList&gt;
* 
* <pre>
&lt;script&gt;
$.getJSON('//cms.publiccms.com/api/directive/cms/voteItemList?voteId=1&amp;pageSize=10', function(data){    
console.log(data.totalCount);
});
&lt;/script&gt;
* </pre>
*/
@Component
public class CmsVoteItemListDirective extends AbstractTemplateDirective {

    @Override
    public void execute(RenderHandler handler) throws IOException, Exception {
        PageHandler page = service.getPage(handler.getLong("voteId"), handler.getString("orderField"),
                handler.getString("orderType"), handler.getInteger("pageIndex", 1), handler.getInteger("pageSize", handler.getInteger("count", 30)));
        handler.put("page", page).render();
    }

    @Autowired
    private CmsVoteItemService service;

}
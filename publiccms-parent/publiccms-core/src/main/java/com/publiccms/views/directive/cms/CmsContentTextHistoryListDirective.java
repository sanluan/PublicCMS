package com.publiccms.views.directive.cms;

// Generated 2022-5-10 by com.publiccms.common.generator.SourceGenerator

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.publiccms.logic.service.cms.CmsContentTextHistoryService;
import com.publiccms.common.base.AbstractTemplateDirective;
import com.publiccms.common.handler.RenderHandler;
import com.publiccms.common.handler.PageHandler;

/**
 *
 * contentTextHistoryList 内容正文历史列表查询指令
 * <p>
 * 参数列表
 * <ul>
 * <li><code>contentId</code> 内容id
 * <li><code>fieldName</code> 字段名
 * <li><code>userId</code> 用户id
 * <li><code>orderType</code> 排序类型,【asc:正序,desc:倒叙】,默认为发布日期倒叙
 * <li><code>pageIndex</code> 页码
 * <li><code>pageSize</code> 每页条数
 * </ul>
 * <p>
 * 返回结果page子属性:
 * <ul>
 * <li><code>page</code> {@link com.publiccms.common.handler.PageHandler}
 * <li><code>page.list</code> List类型 查询结果实体列表
 * {@link com.publiccms.entities.cms.CmsContentTextHistory}
 * </ul>
 * 使用示例
 * <p>
 * &lt;@cms.contentTextHistoryList contentId=1 fieldName='text' pageSize=10&gt;&lt;#list page.list as
 * a&gt;${a.text}&lt;#sep&gt;,&lt;/#list&gt;&lt;/@cms.contentTextHistoryList&gt;
 * 
 * <pre>
 &lt;script&gt;
  $.getJSON('//cms.publiccms.com/api/directive/cms/contentTextHistoryList?contentId=1&amp;fieldName='text'&amp;pageSize=10&amp;appToken=接口访问授权Token', function(data){    
    console.log(data.totalCount);
  });
  &lt;/script&gt;
 * </pre>
 */
@Component
public class CmsContentTextHistoryListDirective extends AbstractTemplateDirective {

    @Override
    public void execute(RenderHandler handler) throws IOException, Exception {
        PageHandler page = service.getPage(handler.getLong("contentId"), handler.getString("fieldName"),
                handler.getLong("userId"), handler.getString("orderType"), handler.getInteger("pageIndex", 1),
                handler.getInteger("pageSize", 30));
        handler.put("page", page).render();
    }

    @Override
    public boolean needAppToken() {
        return true;
    }

    @Autowired
    private CmsContentTextHistoryService service;

}
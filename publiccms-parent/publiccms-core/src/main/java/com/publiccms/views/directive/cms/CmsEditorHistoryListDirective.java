package com.publiccms.views.directive.cms;

// Generated 2022-5-10 by com.publiccms.common.generator.SourceGenerator

import java.io.IOException;

import org.springframework.stereotype.Component;

import com.publiccms.common.base.AbstractTemplateDirective;
import com.publiccms.common.handler.PageHandler;
import com.publiccms.common.handler.RenderHandler;
import com.publiccms.logic.service.cms.CmsEditorHistoryService;

import jakarta.annotation.Resource;

/**
 *
 * editorHistoryList 内容正文历史列表查询指令
 * <p>
 * 参数列表
 * <ul>
 * <li><code>itemType</code> 项目类型
 * <li><code>itemId</code> 项目id
 * <li><code>fieldName</code> 字段名
 * <li><code>userId</code> 用户id
 * <li><code>orderType</code> 排序类型,【asc:正序,desc:倒序】,默认为发布日期倒序
 * <li><code>pageIndex</code> 页码
 * <li><code>pageSize</code> 每页条数
 * </ul>
 * <p>
 * 返回结果page子属性:
 * <ul>
 * <li><code>page</code> {@link com.publiccms.common.handler.PageHandler}
 * <li><code>page.list</code> List类型 查询结果实体列表
 * {@link com.publiccms.entities.cms.CmsEditorHistory}
 * </ul>
 * 使用示例
 * <p>
 * &lt;@cms.editorHistoryList contentId=1 fieldName='text'
 * pageSize=10&gt;&lt;#list page.list as
 * a&gt;${a.text}&lt;#sep&gt;,&lt;/#list&gt;&lt;/@cms.editorHistoryList&gt;
 * 
 * <pre>
 &lt;script&gt;
  $.getJSON('${site.dynamicPath}api/directive/cms/editorHistoryList?contentId=1&amp;fieldName='text'&amp;pageSize=10&amp;appToken=接口访问授权Token', function(data){    
    console.log(data.totalCount);
  });
  &lt;/script&gt;
 * </pre>
 */
@Component
public class CmsEditorHistoryListDirective extends AbstractTemplateDirective {

    @Override
    public void execute(RenderHandler handler) throws IOException, Exception {
        PageHandler page = service.getPage(handler.getString("itemType"), handler.getString("itemId"),
                handler.getString("fieldName"), handler.getLong("userId"), handler.getString("orderType"),
                handler.getInteger("pageIndex", 1), handler.getInteger("pageSize", 30));
        handler.put("page", page).render();
    }

    @Override
    public boolean needAppToken() {
        return true;
    }

    @Resource
    private CmsEditorHistoryService service;

}
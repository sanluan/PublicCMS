package com.publiccms.views.directive.cms;

// Generated 2016-1-25 10:53:21 by com.publiccms.common.generator.SourceGenerator

import java.io.IOException;

import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

import com.publiccms.common.base.AbstractTemplateDirective;
import com.publiccms.common.handler.PageHandler;
import com.publiccms.common.handler.RenderHandler;
import com.publiccms.logic.service.cms.CmsContentRelatedService;

/**
 *
 * contentRelatedList 内容推荐列表查询指令
 * <p>
 * 参数列表
 * <ul>
 * <li><code>contentId</code> 内容id
 * <li><code>relatedContentId</code> 被推荐内容id
 * <li><code>relationType</code> 关系类型
 * <li><code>relation</code> 关系
 * <li><code>orderField</code> 排序字段,【clicks:点击数】,默认排序正序、id正序
 * <li><code>orderType</code> 排序类型,【asc:正序,desc:倒叙】,默认为倒叙
 * <li><code>pageIndex</code> 页码
 * <li><code>pageSize</code> 每页条数
 * </ul>
 * <p>
 * 返回结果
 * <ul>
 * <li><code>page</code> {@link com.publiccms.common.handler.PageHandler}
 * <li><code>page.list</code> List类型 查询结果实体列表
 * {@link com.publiccms.entities.cms.CmsContentRelated}
 * </ul>
 * 使用示例
 * <p>
 * &lt;@cms.contentRelatedList contentId=1 pageSize=10&gt;&lt;#list page.list as
 * a&gt;${a.title}&lt;#sep&gt;,&lt;/#list&gt;&lt;/@cms.contentRelatedList&gt;
 * 
 * <pre>
 &lt;script&gt;
  $.getJSON('${site.dynamicPath}api/directive/cms/contentRelatedList?contentId=1&amp;pageSize=10', function(data){    
    console.log(data.totalCount);
  });
  &lt;/script&gt;
 * </pre>
 */
@Component
public class CmsContentRelatedListDirective extends AbstractTemplateDirective {

    @Override
    public void execute(RenderHandler handler) throws IOException, Exception {
        PageHandler page = service.getPage(handler.getLong("contentId"), handler.getLong("relatedContentId"),
                handler.getString("relationType"), handler.getString("relation"), handler.getString("orderField"),
                handler.getString("orderType"), handler.getInteger("pageIndex", 1),
                handler.getInteger("pageSize", handler.getInteger("count", 30)));
        handler.put("page", page).render();
    }

    @Resource
    private CmsContentRelatedService service;

}
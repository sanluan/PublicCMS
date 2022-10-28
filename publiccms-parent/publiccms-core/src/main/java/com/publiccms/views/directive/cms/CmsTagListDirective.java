package com.publiccms.views.directive.cms;

// Generated 2015-7-10 16:36:23 by com.publiccms.common.generator.SourceGenerator

import java.io.IOException;

import com.publiccms.common.base.AbstractTemplateDirective;
import com.publiccms.logic.service.cms.CmsTagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.publiccms.common.handler.PageHandler;
import com.publiccms.common.handler.RenderHandler;

/**
 *
 * tagList 标签列表查询指令
 * <p>
 * 参数列表
 * <ul>
 * <li><code>typeId</code> 标签类型id
 * <li><code>name</code> 标签名
 * <li><code>advanced</code> 开启高级选项, 默认为<code>false</code>
 * <li><code>orderField</code> 高级选项:排序字段,【searchCount:搜索次数】,默认searchCount按orderType排序
 * <li><code>orderType</code> 排序类型,【asc:正序,desc:倒叙】,默认为倒叙
 * <li><code>pageIndex</code> 页码
 * <li><code>pageSize</code> 每页条数
 * </ul>
 * <p>
 * 返回结果
 * <ul>
 * <li><code>page</code> {@link com.publiccms.common.handler.PageHandler}
 * <li><code>page.list</code> List类型 查询结果实体列表
 * {@link com.publiccms.entities.cms.CmsTag}
 * </ul>
 * 使用示例
 * <p>
 * &lt;@cms.tagList pageSize=10&gt;&lt;#list page.list as
 * a&gt;${a.name}&lt;#sep&gt;,&lt;/#list&gt;&lt;/@cms.tagList&gt;
 * 
 * <pre>
&lt;script&gt;
$.getJSON('${site.dynamicPath}api/directive/cms/tagList?pageSize=10', function(data){    
  console.log(data.totalCount);
});
&lt;/script&gt;
 * </pre>
 */
@Component
public class CmsTagListDirective extends AbstractTemplateDirective {

    @Override
    public void execute(RenderHandler handler) throws IOException, Exception {
        String orderField = "searchCount";
        if (getAdvanced(handler)) {
            orderField = handler.getString("orderField");
        }
        PageHandler page = service.getPage(getSite(handler).getId(), handler.getInteger("typeId"), handler.getString("name"),
                orderField, handler.getString("orderType"), handler.getInteger("pageIndex", 1),
                handler.getInteger("pageSize", handler.getInteger("count", 30)));
        handler.put("page", page).render();
    }

    @Override
    public boolean supportAdvanced() {
        return true;
    }

    @Autowired
    private CmsTagService service;

}
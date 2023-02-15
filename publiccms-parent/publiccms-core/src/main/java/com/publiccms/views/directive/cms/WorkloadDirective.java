package com.publiccms.views.directive.cms;

// Generated 2015-5-12 12:57:43 by com.publiccms.common.generator.SourceGenerator

import java.io.IOException;

import org.springframework.stereotype.Component;

import com.publiccms.common.base.AbstractTemplateDirective;
import com.publiccms.common.handler.PageHandler;
import com.publiccms.common.handler.RenderHandler;
import com.publiccms.logic.service.cms.CmsContentService;

import jakarta.annotation.Resource;

/**
 *
 * wordload 工作量查询指令
 * <p>
 * 参数列表
 * <ul>
 * <li><code>status</code>:多个内容状态,【0:草稿,1:已发布,2:待审核,3:驳回】
 * <li><code>startCreateDate</code>:起始创建日期,【2020-01-01 23:59:59】,【2020-01-01】
 * <li><code>endCreateDate</code>:终止创建日期,【2020-01-01 23:59:59】,【2020-01-01】
 * <li><code>workloadType</code>:工作量类型【dept:部门,user:用户】,默认部门
 * <li><code>dateField</code>:日期字段【createDate:创建日期,publishDate:发布日期】,默认创建日期
 * <li><code>pageIndex</code>:页码
 * <li><code>pageSize</code>:每页条数
 * </ul>
 * <p>
 * 返回结果
 * <ul>
 * <li><code>page</code>:{@link com.publiccms.common.handler.PageHandler}
 * <li><code>page.list</code>:List类型 查询结果实体列表
 * {@link com.publiccms.views.pojo.entities.Workload}
 * </ul>
 * 使用示例
 * <p>
 * &lt;@cms.wordload pageSize=10&gt;&lt;#list page.list as
 * a&gt;${a.name}&lt;#sep&gt;,&lt;/#list&gt;&lt;/@cms.wordload&gt;
 * 
 * <pre>
 &lt;script&gt;
  $.getJSON('${site.dynamicPath}api/directive/cms/wordload?pageSize=10&amp;appToken=接口访问授权Token', function(data){    
    console.log(data.totalCount);
  });
  &lt;/script&gt;
 * </pre>
 */
@Component
public class WorkloadDirective extends AbstractTemplateDirective {

    @Override
    public void execute(RenderHandler handler) throws IOException, Exception {
        PageHandler page = service.getWorkLoadPage(getSite(handler).getId(), handler.getIntegerArray("status"),
                handler.getDate("startCreateDate"), handler.getDate("endCreateDate"), handler.getString("workloadType"),
                handler.getString("dateField"), handler.getInteger("pageIndex", 1), handler.getInteger("pageSize", 30));
        handler.put("page", page).render();
    }

    @Override
    public boolean needAppToken() {
        return true;
    }

    @Resource
    private CmsContentService service;

}
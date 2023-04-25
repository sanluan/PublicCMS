package com.publiccms.views.directive.cms;

// Generated 2020-7-1 21:06:19 by com.publiccms.common.generator.SourceGenerator
import java.io.IOException;

import javax.annotation.Resource;
import org.springframework.stereotype.Component;

import com.publiccms.logic.service.cms.CmsSurveyService;

import freemarker.template.TemplateException;

import com.publiccms.common.base.AbstractTemplateDirective;
import com.publiccms.common.handler.RenderHandler;
import com.publiccms.common.handler.PageHandler;

/**
 *
 * surveyList 问卷调查列表查询指令
 * <p>
 * 参数列表
 * <ul>
 * <li><code>userId</code>:发布用户id
 * <li><code>surveyType</code>:问卷类型,【exam:考试,survey:问卷调查】
 * <li><code>startStartDate</code>:起始开始日期,【2020-01-01 23:59:59】,【2020-01-01】
 * <li><code>endStartDate</code>:终止开始日期,【2020-01-01 23:59:59】,【2020-01-01】
 * <li><code>startEndDate</code>:起始结束日期,【2020-01-01 23:59:59】,【2020-01-01】
 * <li><code>endEndDate</code>:终止结束日期,【2020-01-01 23:59:59】,【2020-01-01】
 * <li><code>title</code>:标题
 * <li><code>disabled</code>:已禁用,默认为<code>false</code>
 * <li><code>orderField</code>
 * 排序字段,【votes:参与人数,startDate:开始日期,endDate:结束日期,createTime:创建日期】,默认id按orderType排序
 * <li><code>orderType</code>:排序类型,【asc:正序,desc:倒序】,默认为倒序
 * <li><code>pageIndex</code>:页码
 * <li><code>pageSize</code>:每页条数
 * </ul>
 * <p>
 * 返回结果
 * <ul>
 * <li><code>page</code>:{@link com.publiccms.common.handler.PageHandler}
 * <li><code>page.list</code>:List类型 查询结果实体列表
 * {@link com.publiccms.entities.cms.CmsSurvey}
 * </ul>
 * 使用示例
 * <p>
 * &lt;@cms.surveyList pageSize=10&gt;&lt;#list page.list as
 * a&gt;${a.title}&lt;#sep&gt;,&lt;/#list&gt;&lt;/@cms.surveyList&gt;
 * 
 * <pre>
&lt;script&gt;
 $.getJSON('${site.dynamicPath}api/directive/cms/surveyList?pageSize=10', function(data){    
   console.log(data.page.totalCount);
 });
 &lt;/script&gt;
 * </pre>
 */
@Component
public class CmsSurveyListDirective extends AbstractTemplateDirective {

    @Override
    public void execute(RenderHandler handler) throws IOException, TemplateException {
        PageHandler page = service.getPage(getSite(handler).getId(), handler.getLong("userId"), handler.getString("surveyType"),
                handler.getDate("startStartDate"), handler.getDate("endStartDate"), handler.getDate("startEndDate"),
                handler.getDate("endEndDate"), handler.getString("title"), handler.getBoolean("disabled", false),
                handler.getString("orderField"), handler.getString("orderType"), handler.getInteger("pageIndex", 1),
                handler.getInteger("pageSize", 30));
        handler.put("page", page).render();
    }

    @Resource
    private CmsSurveyService service;

}
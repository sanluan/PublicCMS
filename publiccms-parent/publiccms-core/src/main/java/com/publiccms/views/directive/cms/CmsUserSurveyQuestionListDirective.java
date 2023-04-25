package com.publiccms.views.directive.cms;

// Generated 2021-09-23 16:55:08 by com.publiccms.common.generator.SourceGenerator
import java.io.IOException;

import javax.annotation.Resource;
import org.springframework.stereotype.Component;

import com.publiccms.common.base.AbstractTemplateDirective;
import com.publiccms.common.handler.PageHandler;
import com.publiccms.common.handler.RenderHandler;
import com.publiccms.logic.service.cms.CmsUserSurveyQuestionService;

import freemarker.template.TemplateException;

/**
*
* userSurveyQuestionList 用户问卷问题解答列表查询指令
* <p>
* 参数列表
* <ul>
* <li><code>userId</code>:用户id
* <li><code>questionId</code>:问题id
* <li><code>surveyId</code>:问卷id
* <li><code>orderField</code>:排序字段,【score:评分,createDate:创建日期】,默认createDate按orderType排序
* <li><code>orderType</code>:排序类型,【asc:正序,desc:倒序】,默认为倒序
* <li><code>pageIndex</code>:页码
* <li><code>pageSize</code>:每页条数
* </ul>
* <p>
* 返回结果
* <ul>
* <li><code>page</code>:{@link com.publiccms.common.handler.PageHandler}
* <li><code>page.list</code>:List类型 查询结果实体列表
* {@link com.publiccms.entities.cms.CmsUserSurveyQuestion}
* </ul>
* 使用示例
* <p>
* &lt;@cms.userSurveyQuestionList userId=1 pageSize=10&gt;&lt;#list page.list as
* a&gt;${a.answer}&lt;#sep&gt;,&lt;/#list&gt;&lt;/@cms.userSurveyQuestionList&gt;
* 
* <pre>
&lt;script&gt;
$.getJSON('${site.dynamicPath}api/directive/cms/userSurveyQuestionList?userId=1&amp;pageSize=10&amp;appToken=接口访问授权Token', function(data){    
 console.log(data.page.totalCount);
});
&lt;/script&gt;
* </pre>
*/
@Component
public class CmsUserSurveyQuestionListDirective extends AbstractTemplateDirective {

    @Override
    public void execute(RenderHandler handler) throws IOException, TemplateException {
        PageHandler page = service.getPage(getSite(handler).getId(), handler.getLong("userId"), handler.getLong("questionId"),
                handler.getLong("surveyId"), handler.getString("orderField"), handler.getString("orderType"),
                handler.getInteger("pageIndex", 1), handler.getInteger("pageSize", 30));
        handler.put("page", page).render();
    }

    @Override
    public boolean needAppToken() {
        return true;
    }

    @Resource
    private CmsUserSurveyQuestionService service;

}
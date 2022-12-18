package com.publiccms.views.directive.cms;

// Generated 2020-7-1 21:06:19 by com.publiccms.common.generator.SourceGenerator
import java.io.IOException;

import javax.annotation.Resource;
import org.springframework.stereotype.Component;

import com.publiccms.logic.service.cms.CmsSurveyQuestionItemService;
import com.publiccms.common.base.AbstractTemplateDirective;
import com.publiccms.common.handler.RenderHandler;
import com.publiccms.common.handler.PageHandler;

/**
 *
 * surveyQuestionItemList 问卷调查问题选项列表查询指令
 * <p>
 * 参数列表
 * <ul>
 * <li><code>questionId</code> 问题id
 * <li><code>orderField</code> 排序字段,【votes:得票数量】,默认sort按顺序
 * <li><code>orderType</code> 排序类型,【asc:正序,desc:倒叙】,默认为倒叙
 * <li><code>pageIndex</code> 页码
 * <li><code>pageSize</code> 每页条数
 * </ul>
 * <p>
 * 返回结果
 * <ul>
 * <li><code>page</code> {@link com.publiccms.common.handler.PageHandler}
 * <li><code>page.list</code> List类型 查询结果实体列表
 * {@link com.publiccms.entities.cms.CmsSurveyQuestionItem}
 * </ul>
 * 使用示例
 * <p>
 * &lt;@cms.surveyQuestionItemList questionId=1 pageSize=10&gt;&lt;#list
 * page.list as
 * a&gt;${a.title}&lt;#sep&gt;,&lt;/#list&gt;&lt;/@cms.surveyQuestionItemList&gt;
 * 
 * <pre>
&lt;script&gt;
$.getJSON('${site.dynamicPath}api/directive/cms/surveyQuestionItemList?questionId=1&amp;pageSize=10', function(data){    
  console.log(data.totalCount);
});
&lt;/script&gt;
 * </pre>
 */
@Component
public class CmsSurveyQuestionItemListDirective extends AbstractTemplateDirective {

    @Override
    public void execute(RenderHandler handler) throws IOException, Exception {
        PageHandler page = service.getPage(handler.getLong("questionId"), handler.getString("orderField"),
                handler.getString("orderType"), handler.getInteger("pageIndex", 1), handler.getInteger("pageSize", 30));
        handler.put("page", page).render();
    }

    @Resource
    private CmsSurveyQuestionItemService service;

}
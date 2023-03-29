package com.publiccms.views.directive.cms;

// Generated 2020-7-1 21:06:19 by com.publiccms.common.generator.SourceGenerator
import java.io.IOException;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.publiccms.common.base.AbstractTemplateDirective;
import com.publiccms.common.handler.PageHandler;
import com.publiccms.common.handler.RenderHandler;
import com.publiccms.common.tools.CmsUrlUtils;
import com.publiccms.entities.cms.CmsSurveyQuestion;
import com.publiccms.entities.sys.SysSite;
import com.publiccms.logic.service.cms.CmsSurveyQuestionService;

/**
 *
 * surveyQuestionList 问卷调查问题列表查询指令
 * <p>
 * 参数列表
 * <ul>
 * <li><code>surveyId</code>:问卷调查id
 * <li><code>questionTypes</code>
 * 多个问题类型,【radio:单选,select:下拉选择,checkbox:复选框,text:文字,file:文件,picture:图片】
 * <li><code>absoluteURL</code>:封面图处理为绝对路径 默认为<code>true</code>
 * <li><code>advanced</code>:开启高级选项, 默认为<code>false</code>
 * <li><code>orderType</code>:排序类型,【asc:正序,desc:倒序】,默认为排序顺序
 * <li><code>pageIndex</code>:页码
 * <li><code>pageSize</code>:每页条数
 * </ul>
 * <p>
 * 返回结果
 * <ul>
 * <li><code>page</code>:{@link com.publiccms.common.handler.PageHandler}
 * <li><code>page.list</code>:List类型 查询结果实体列表
 * {@link com.publiccms.entities.cms.CmsSurveyQuestion}
 * </ul>
 * 使用示例
 * <p>
 * &lt;@cms.surveyQuestionList surveyId=1 pageSize=10&gt;&lt;#list page.list as
 * a&gt;${a.title}&lt;#sep&gt;,&lt;/#list&gt;&lt;/@cms.surveyQuestionList&gt;
 * 
 * <pre>
&lt;script&gt;
$.getJSON('${site.dynamicPath}api/directive/cms/surveyQuestionList?surveyId=1&amp;pageSize=10', function(data){    
  console.log(data.totalCount);
});
&lt;/script&gt;
 * </pre>
 */
@Component
public class CmsSurveyQuestionListDirective extends AbstractTemplateDirective {

    @Override
    public void execute(RenderHandler handler) throws IOException, Exception {
        boolean absoluteURL = handler.getBoolean("absoluteURL", true);
        boolean advanced = getAdvanced(handler);
        PageHandler page = service.getPage(handler.getLong("surveyId"), handler.getStringArray("questionTypes"),
                handler.getString("orderType"), handler.getInteger("pageIndex", 1), handler.getInteger("pageSize", 30));
        SysSite site = getSite(handler);
        @SuppressWarnings("unchecked")
        List<CmsSurveyQuestion> list = (List<CmsSurveyQuestion>) page.getList();
        if (null != list) {
            list.forEach(e -> {
                if (!advanced) {
                    e.setAnswer(null);
                }
                if (absoluteURL) {
                    e.setCover(CmsUrlUtils.getUrl(site.getSitePath(), e.getCover()));
                }
            });
        }
        handler.put("page", page).render();
    }

    @Override
    public boolean supportAdvanced() {
        return true;
    }

    @Resource
    private CmsSurveyQuestionService service;

}
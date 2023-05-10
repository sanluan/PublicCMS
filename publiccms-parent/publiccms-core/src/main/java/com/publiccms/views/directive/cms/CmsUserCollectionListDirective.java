package com.publiccms.views.directive.cms;

// Generated 2020-3-26 11:46:48 by com.publiccms.common.generator.SourceGenerator
import java.io.IOException;

import javax.annotation.Resource;
import org.springframework.stereotype.Component;

import com.publiccms.logic.service.cms.CmsUserCollectionService;

import freemarker.template.TemplateException;

import com.publiccms.common.base.AbstractTemplateDirective;
import com.publiccms.common.handler.RenderHandler;
import com.publiccms.common.handler.PageHandler;

/**
 *
 * userCollectionList 用户评分列表查询指令
 * <p>
 * 参数列表
 * <ul>
 * <li><code>userId</code>:用户id
 * <li><code>contentId</code>:内容id
 * <li><code>pageIndex</code>:页码
 * <li><code>pageSize</code>:每页条数
 * </ul>
 * <p>
 * 返回结果
 * <ul>
 * <li><code>page</code>:{@link com.publiccms.common.handler.PageHandler}
 * <li><code>page.list</code>:List类型 查询结果实体列表
 * {@link com.publiccms.entities.cms.CmsUserCollection}
 * </ul>
 * 使用示例
 * <p>
 * &lt;@cms.userCollectionList userId=1 pageSize=10&gt;&lt;#list page.list as
 * a&gt;${a.scores}&lt;#sep&gt;,&lt;/#list&gt;&lt;/@cms.userCollectionList&gt;
 * 
 * <pre>
&lt;script&gt;
$.getJSON('${site.dynamicPath}api/directive/cms/userCollectionList?userId=1&amp;pageSize=10', function(data){    
 console.log(data.page.totalCount);
});
&lt;/script&gt;
 * </pre>
 */
@Component
public class CmsUserCollectionListDirective extends AbstractTemplateDirective {

    @Override
    public void execute(RenderHandler handler) throws IOException, TemplateException {
        PageHandler page = service.getPage(handler.getLong("userId"), handler.getLong("contentId"),
                handler.getInteger("pageIndex", 1), handler.getInteger("pageSize", 30));
        handler.put("page", page).render();
    }

    @Resource
    private CmsUserCollectionService service;

}
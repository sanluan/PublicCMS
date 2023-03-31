package com.publiccms.views.directive.cms;

// Generated 2016-11-20 14:50:37 by com.publiccms.common.generator.SourceGenerator

import java.io.IOException;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.publiccms.common.base.AbstractTemplateDirective;
import com.publiccms.common.handler.PageHandler;
import com.publiccms.common.handler.RenderHandler;
import com.publiccms.entities.sys.SysSite;
import com.publiccms.logic.service.cms.CmsDictionaryService;

import freemarker.template.TemplateException;

/**
 *
 * dictionaryList 数据字典列表
 * <p>
 * 参数列表
 * <ul>
 * <li><code>name</code>:字典名称
 * <li><code>pageIndex</code>:页码
 * <li><code>pageSize</code>:每页条数
 * </ul>
 * <p>
 * 返回结果
 * <ul>
 * <li><code>page</code>:{@link com.publiccms.common.handler.PageHandler}
 * <li><code>page.list</code>:List类型 查询结果实体列表
 * {@link com.publiccms.entities.cms.CmsDictionary}
 * </ul>
 * 使用示例
 * <p>
 * &lt;@cms.dictionaryList name='data'&gt;&lt;#list page.list as
 * a&gt;${a.name}&lt;#sep&gt;,&lt;/#list&gt;&lt;/@cms.dictionaryList&gt;
 * 
 * <pre>
&lt;script&gt;
 $.getJSON('${site.dynamicPath}api/directive/cms/dictionaryList?name=data', function(data){    
   console.log(data.totalCount);
 });
 &lt;/script&gt;
 * </pre>
 */
@Component
public class CmsDictionaryListDirective extends AbstractTemplateDirective {

    @Override
    public void execute(RenderHandler handler) throws IOException, TemplateException {
        SysSite site = getSite(handler);
        short siteId = null == site.getParentId() ? site.getId() : site.getParentId();
        PageHandler page = service.getPage(siteId, handler.getString("name"), handler.getInteger("pageIndex", 1),
                handler.getInteger("pageSize", handler.getInteger("count", 30)));
        handler.put("page", page).render();
    }

    @Resource
    private CmsDictionaryService service;

}
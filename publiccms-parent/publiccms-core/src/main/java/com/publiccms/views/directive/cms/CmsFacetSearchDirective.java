package com.publiccms.views.directive.cms;

// Generated 2015-5-10 17:54:56 by com.publiccms.common.generator.SourceGenerator

import java.io.IOException;

import org.springframework.stereotype.Component;

import com.publiccms.common.handler.RenderHandler;

import freemarker.template.TemplateException;

/**
 *
 * facetSearch 内容列表查询指令
 * <p>
 * 参数列表{@link com.publiccms.views.directive.cms.CmsSearchDirective}
 * <p>
 * 返回结果
 * <ul>
 * <li><code>page</code>
 * 分面搜索结果{@link com.publiccms.common.handler.FacetPageHandler}
 * <li><code>page.list</code>:List类型 查询结果实体列表
 * {@link com.publiccms.entities.cms.CmsContent}
 * </ul>
 * 使用示例
 * 
 * <pre>
  &lt;@cms.facetSearch word='cms' pageSize=10&gt; 
  &lt;p&gt; category: &lt;#list page.facetMap.categoryId as k,v&gt;&lt;@cms.category id=k&gt;${object.name}&lt;/@cms.category&gt;(${v})&lt;/#list&gt;&lt;/p&gt;
  &lt;p&gt; model: &lt;#list page.facetMap.modelId as k,v&gt;&lt;@cms.model id=k&gt;${object.name}&lt;/@cms.model&gt;(${v})&lt;/#list&gt;&lt;/p&gt;
  &lt;#list page.list as a&gt;&lt;p&gt;${a.title}&lt;/p&gt;&lt;/#list&gt;
  &lt;/@cms.facetSearch&gt;
 * </pre>
 * 
 * <pre>
  &lt;script&gt;
   $.getJSON('${site.dynamicPath}api/directive/cms/facetSearch?word=cms&amp;pageSize=10', function(data){    
     console.log(data.page.totalCount);
   });
   &lt;/script&gt;
 * </pre>
 * 
 */
@Component
public class CmsFacetSearchDirective extends CmsSearchDirective {

    @Override
    public void execute(RenderHandler handler) throws IOException, TemplateException {
        handler.put("page", query(handler, true)).render();
    }

}
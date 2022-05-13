package com.publiccms.views.directive.cms;

// Generated 2016-2-18 23:41:56 by com.publiccms.common.source.SourceGenerator

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.publiccms.common.base.AbstractTemplateDirective;
import com.publiccms.common.database.CmsDataSource;
import com.publiccms.common.handler.RenderHandler;
import com.publiccms.entities.cms.CmsContent;
import com.publiccms.entities.sys.SysSite;
import com.publiccms.logic.component.site.DatasourceComponent;
import com.publiccms.logic.service.cms.CmsContentService;

/**
*
* contentQuoteList 分类列表查询指令
* <p>
* 参数列表
* <ul>
* <li><code>quoteId</code> 发布评论用户id
* </ul>
* <p>
* 返回结果:
* <ul>
* <li><code>list</code> List类型 查询结果实体列表
* {@link com.publiccms.entities.cms.CmsContent}
* </ul>
* 使用示例
* <p>
* &lt;@_contentQuoteList contentId=1 pageSize=10&gt;&lt;#list list as
* a&gt;${a.title}&lt;#sep&gt;,&lt;/#list&gt;&lt;/@_contentQuoteList&gt;
* 
* <pre>
 &lt;script&gt;
  $.getJSON('//cms.publiccms.com/api/directive/contentQuoteList?contentId=1', function(data){    
    console.log(data);
  });
  &lt;/script&gt;
* </pre>
*/
@Component
public class CmsContentQuoteListDirective extends AbstractTemplateDirective {

    @Override
    public void execute(RenderHandler handler) throws IOException, Exception {
        SysSite site = getSite(handler);
        try {
            CmsDataSource.setDataSourceName(datasourceComponent.getRandomDatasource(site.getId()));
            List<CmsContent> list = service.getListByQuoteId(site.getId(), handler.getLong("quoteId"));
            handler.put("list", list).render();
        } finally {
            CmsDataSource.resetDataSourceName();
        }
    }

    @Autowired
    private CmsContentService service;
    @Autowired
    private DatasourceComponent datasourceComponent;
}
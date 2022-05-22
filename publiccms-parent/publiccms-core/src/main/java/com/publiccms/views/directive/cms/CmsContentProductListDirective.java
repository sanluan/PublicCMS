package com.publiccms.views.directive.cms;

// Generated 2021-6-26 17:53:08 by com.publiccms.common.generator.SourceGenerator
import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.publiccms.common.base.AbstractTemplateDirective;
import com.publiccms.common.handler.PageHandler;
import com.publiccms.common.handler.RenderHandler;
import com.publiccms.entities.cms.CmsContentProduct;
import com.publiccms.entities.sys.SysSite;
import com.publiccms.logic.component.template.TemplateComponent;
import com.publiccms.logic.service.cms.CmsContentProductService;

/**
*
* contentProductList 产品列表查询指令
* <p>
* 参数列表
* <ul>
* <li><code>contentId</code> 内容id
* <li><code>userId</code> 发布产品用户id
* <li><code>title</code> 标题
* <li><code>startPrice</code> 开始价格
* <li><code>endPrice</code> 结束价格
* <li><code>absoluteURL</code> 封面图处理为绝对路径 默认为<code>true</code>
* <li><code>orderField</code>
* 排序字段,【price:价格,inventory:库存,sales:销量】,默认置顶id按orderType排序
* <li><code>orderType</code> 排序类型,【asc:正序,desc:倒叙】，默认为倒叙
* <li><code>pageIndex</code> 页码
* <li><code>pageSize</code> 每页条数
* </ul>
* <p>
* 返回结果
 * <ul>
 * <li><code>page</code> {@link com.publiccms.common.handler.PageHandler}
 * <li><code>page.list</code> List类型 查询结果实体列表
* {@link com.publiccms.entities.cms.CmsContentProduct}
* </ul>
* 使用示例
* <p>
* &lt;@cms.contentProductList contentId=1 pageSize=10&gt;&lt;#list page.list as
* a&gt;${a.title}&lt;#sep&gt;,&lt;/#list&gt;&lt;/@cms.contentProductList&gt;
* 
* <pre>
 &lt;script&gt;
  $.getJSON('//cms.publiccms.com/api/directive/cms/contentProductList?contentId=1&amp;pageSize=10', function(data){    
    console.log(data.totalCount);
  });
  &lt;/script&gt;
* </pre>
*/
@Component
public class CmsContentProductListDirective extends AbstractTemplateDirective {

    @Override
    public void execute(RenderHandler handler) throws IOException, Exception {
        PageHandler page = service.getPage(getSite(handler).getId(), handler.getLong("contentId"), handler.getLong("userId"),
                handler.getString("title"), handler.getBigDecimal("startPrice"), handler.getBigDecimal("endPrice"),
                handler.getString("orderField"), handler.getString("orderType"), handler.getInteger("pageIndex", 1),
                handler.getInteger("pageSize", 30));
        @SuppressWarnings("unchecked")
        List<CmsContentProduct> list = (List<CmsContentProduct>) page.getList();
        if (null != list) {
            boolean absoluteURL = handler.getBoolean("absoluteURL", true);
            SysSite site = getSite(handler);
            list.forEach(e -> {
                if (absoluteURL) {
                    e.setCover(TemplateComponent.getUrl(site, true, e.getCover()));
                }
            });
        }
        handler.put("page", page).render();
    }

    @Autowired
    private CmsContentProductService service;

}
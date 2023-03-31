package com.publiccms.views.directive.trade;

// Generated 2021-6-26 22:16:13 by com.publiccms.common.generator.SourceGenerator
import java.io.IOException;

import javax.annotation.Resource;
import org.springframework.stereotype.Component;

import com.publiccms.logic.service.trade.TradeOrderProductService;

import freemarker.template.TemplateException;

import com.publiccms.common.base.AbstractTemplateDirective;
import com.publiccms.common.handler.RenderHandler;
import com.publiccms.common.handler.PageHandler;

/**
*
* tradeOrderProductList 订单产品列表查询指令
* <p>
* 参数列表
* <ul>
* <li><code>orderId</code> 订单id
* <li><code>pageIndex</code> 页码
* <li><code>pageSize</code> 每页条数
* </ul>
* <p>
* 返回结果
* <ul>
* <li><code>page</code> {@link com.publiccms.common.handler.PageHandler}
* <li><code>page.list</code> List类型 查询结果实体列表
* {@link com.publiccms.entities.trade.TradeOrderProduct}
* </ul>
* 使用示例
* <p>
* &lt;@trade.orderProductList orderId=1 pageSize=10&gt;&lt;#list page.list as
* a&gt;${a.productId}&lt;#sep&gt;,&lt;/#list&gt;&lt;/@trade.orderProductList&gt;
* 
* <pre>
&lt;script&gt;
$.getJSON('${site.dynamicPath}api/directive/trade/orderProductList?orderId=1&amp;pageSize=10&amp;appToken=接口访问授权Token', function(data){    
 console.log(data.totalCount);
});
&lt;/script&gt;
* </pre>
*/
@Component
public class TradeOrderProductListDirective extends AbstractTemplateDirective {

    @Override
    public void execute(RenderHandler handler) throws IOException, TemplateException{
        PageHandler page = service.getPage(getSite(handler).getId(), handler.getLong("orderId"),
                handler.getInteger("pageIndex", 1), handler.getInteger("pageSize", 30));
        handler.put("page", page).render();
    }

    @Override
    public boolean needAppToken() {
        return true;
    }

    @Resource
    private TradeOrderProductService service;

}
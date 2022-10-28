package com.publiccms.views.directive.trade;

// Generated 2019-6-16 9:47:27 by com.publiccms.common.generator.SourceGenerator
import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.publiccms.common.base.AbstractTemplateDirective;
import com.publiccms.common.handler.PageHandler;
import com.publiccms.common.handler.RenderHandler;
import com.publiccms.logic.service.trade.TradeOrderHistoryService;

/**
 *
 * tradeOrderHistoryList 订单历史列表查询指令
 * <p>
 * 参数列表
 * <ul>
 * <li><code>orderId</code> 订单id
 * <li><code>startCreateDate</code> 起始创建日期,【2020-01-01 23:59:59】,【2020-01-01】
 * <li><code>endCreateDate</code> 终止创建日期,【2020-01-01 23:59:59】,【2020-01-01】
 * <li><code>orderType</code> 排序类型,【asc:正序,desc:倒叙】,默认为创建日期倒叙
 * <li><code>pageIndex</code> 页码
 * <li><code>pageSize</code> 每页条数
 * </ul>
 * <p>
 * 返回结果
 * <ul>
 * <li><code>page</code> {@link com.publiccms.common.handler.PageHandler}
 * <li><code>page.list</code> List类型 查询结果实体列表
 * {@link com.publiccms.entities.trade.TradeOrderHistory}
 * </ul>
 * 使用示例
 * <p>
 * &lt;@trade.orderHistoryList orderId=1 pageSize=10&gt;&lt;#list page.list as
 * a&gt;${a.content}&lt;#sep&gt;,&lt;/#list&gt;&lt;/@trade.orderHistoryList&gt;
 * 
 * <pre>
&lt;script&gt;
$.getJSON('${site.dynamicPath}api/directive/trade/orderHistoryList?orderId=1&amp;pageSize=10&amp;appToken=接口访问授权Token', function(data){    
 console.log(data.totalCount);
});
&lt;/script&gt;
 * </pre>
 */
@Component
public class TradeOrderHistoryListDirective extends AbstractTemplateDirective {

    @Override
    public void execute(RenderHandler handler) throws IOException, Exception {
        PageHandler page = service.getPage(getSite(handler).getId(), handler.getLong("orderId"),
                handler.getDate("startCreateDate"), handler.getDate("endCreateDate"), handler.getString("orderType"),
                handler.getInteger("pageIndex", 1), handler.getInteger("pageSize", 30));
        handler.put("page", page).render();
    }

    @Override
    public boolean needAppToken() {
        return true;
    }

    @Autowired
    private TradeOrderHistoryService service;

}
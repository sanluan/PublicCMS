package com.publiccms.views.directive.trade;

// Generated 2019-6-15 18:52:24 by com.publiccms.common.generator.SourceGenerator
import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.publiccms.logic.service.trade.TradePaymentService;
import com.publiccms.common.base.AbstractTemplateDirective;
import com.publiccms.common.handler.RenderHandler;
import com.publiccms.common.handler.PageHandler;

/**
 *
 * tradePaymentList 支付列表查询指令
 * <p>
 * 参数列表
 * <ul>
 * <li><code>userId</code> 用户id
 * <li><code>tradeType</code> 支付类型,【recharge:充值,product:产品】
 * <li><code>serialNumber</code> 流水号
 * <li><code>accountType</code> 账户类型,【account:账户,alipay:支付宝,wechat:微信】
 * <li><code>accountSerialNumber</code> 账号流水号
 * <li><code>status</code> 状态,【0:待支付,1:已支付,2:待退款,3:已退款,4:已关闭】
 * <li><code>startCreateDate</code> 起始创建日期,【2020-01-01 23:59:59】,【2020-01-01】
 * <li><code>endCreateDate</code> 终止创建日期,【2020-01-01 23:59:59】,【2020-01-01】
 * <li><code>orderType</code> 排序类型,【asc:正序,desc:倒叙】,默认为发布日期倒叙
 * <li><code>pageIndex</code> 页码
 * <li><code>pageSize</code> 每页条数
 * </ul>
 * <p>
 * 返回结果
 * <ul>
 * <li><code>page</code> {@link com.publiccms.common.handler.PageHandler}
 * <li><code>page.list</code> List类型 查询结果实体列表
 * {@link com.publiccms.entities.trade.TradePayment}
 * </ul>
 * 使用示例
 * <p>
 * &lt;@trade.paymentList pageSize=10&gt;&lt;#list page.list as
 * a&gt;${a.amount}&lt;#sep&gt;,&lt;/#list&gt;&lt;/@trade.paymentList&gt;
 * 
 * <pre>
&lt;script&gt;
$.getJSON('//cms.publiccms.com/api/directive/trade/paymentList?pageSize=10&amp;authToken=用户登录Token&amp;authUserId=用户id', function(data){    
 console.log(data.totalCount);
});
&lt;/script&gt;
 * </pre>
 */
@Component
public class TradePaymentListDirective extends AbstractTemplateDirective {

    @Override
    public void execute(RenderHandler handler) throws IOException, Exception {
        PageHandler page = service.getPage(getSite(handler).getId(), getUserId(handler, "userId"), handler.getString("tradeType"),
                handler.getString("serialNumber"), handler.getString("accountType"), handler.getString("accountSerialNumber"),
                handler.getIntegerArray("status"), handler.getDate("startCreateDate"), handler.getDate("endCreateDate"),
                handler.getString("paymentType"), handler.getInteger("pageIndex", 1), handler.getInteger("pageSize", 30));
        handler.put("page", page).render();
    }

    @Override
    public boolean needUserToken() {
        return true;
    }

    @Autowired
    private TradePaymentService service;

}
package com.publiccms.views.directive.trade;

// Generated 2021-6-26 22:16:13 by com.publiccms.common.generator.SourceGenerator

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.publiccms.entities.sys.SysSite;
import com.publiccms.entities.trade.TradeOrderProduct;
import com.publiccms.logic.service.trade.TradeOrderProductService;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.common.base.AbstractTemplateDirective;
import com.publiccms.common.handler.RenderHandler;

/**
*
* tradeOrderProduct 订单产品查询指令
* <p>
* 参数列表
* <ul>
* <li><code>id</code> 订单产品id，结果返回<code>object</code>
* {@link com.publiccms.entities.trade.TradeOrderProduct}
* <li><code>ids</code> 多个订单产品id，逗号或空格间隔，当id为空时生效，结果返回<code>map</code>(id,<code>object</code>)
* </ul>
* 使用示例
* <p>
* &lt;@trade.orderProduct id=1&gt;${object.quantity}&lt;/@trade.orderProduct&gt;
* <p>
* &lt;@trade.orderProduct ids=1,2,3&gt;&lt;#list map as
* k,v&gt;${v.quantity}&lt;#sep&gt;,&lt;/#list&gt;&lt;/@trade.orderProduct&gt;
* 
* <pre>
 &lt;script&gt;
  $.getJSON('//cms.publiccms.com/api/directive/trade/orderProduct?id=1&amp;appToken=接口访问授权Token', function(data){    
    console.log(data.quantity);
  });
  &lt;/script&gt;
* </pre>
*/
@Component
public class TradeOrderProductDirective extends AbstractTemplateDirective {

    @Override
    public void execute(RenderHandler handler) throws IOException, Exception {
        Long id = handler.getLong("id");
        SysSite site = getSite(handler);
        if (CommonUtils.notEmpty(id)) {
            TradeOrderProduct entity = service.getEntity(id);
            if (null != entity && site.getId() == entity.getSiteId()) {
                handler.put("object", entity).render();
            }
        } else {
            Long[] ids = handler.getLongArray("ids");
            if (CommonUtils.notEmpty(ids)) {
                List<TradeOrderProduct> entityList = service.getEntitys(ids);
                Map<String, TradeOrderProduct> map = CommonUtils.listToMap(entityList, k -> k.getId().toString(), null,
                        entity -> site.getId() == entity.getSiteId());
                handler.put("map", map).render();
            }
        }
    }

    @Override
    public boolean needAppToken() {
        return true;
    }

    @Autowired
    private TradeOrderProductService service;

}

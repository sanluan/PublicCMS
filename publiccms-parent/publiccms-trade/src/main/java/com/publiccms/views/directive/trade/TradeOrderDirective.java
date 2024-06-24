package com.publiccms.views.directive.trade;

// Generated 2021-6-26 20:16:25 by com.publiccms.common.generator.SourceGenerator

import java.io.IOException;
import java.util.List;
import java.util.Map;

import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

import com.publiccms.entities.sys.SysSite;
import com.publiccms.entities.trade.TradeOrder;
import com.publiccms.logic.service.trade.TradeOrderService;

import freemarker.template.TemplateException;

import com.publiccms.common.tools.CommonUtils;
import com.publiccms.common.base.AbstractTemplateDirective;
import com.publiccms.common.handler.RenderHandler;

/**
*
* tradeOrder 订单查询指令
* <p>
* 参数列表
* <ul>
* <li><code>id</code> 订单id，结果返回<code>object</code>
* {@link com.publiccms.entities.trade.TradeOrder}
* <li><code>ids</code> 多个订单id，逗号或空格间隔，当id为空时生效，结果返回<code>map</code>(id,<code>object</code>)
* </ul>
* 使用示例
* <p>
* &lt;@trade.order id=1&gt;${object.title}&lt;/@trade.order&gt;
* <p>
* &lt;@trade.order ids=1,2,3&gt;&lt;#list map as
* k,v&gt;${k}:${v.title}&lt;#sep&gt;,&lt;/#list&gt;&lt;/@trade.order&gt;
* 
* <pre>
 &lt;script&gt;
  $.getJSON('${site.dynamicPath}api/directive/trade/order?id=1&amp;appToken=接口访问授权Token', function(data){    
    console.log(data.title);
  });
  &lt;/script&gt;
* </pre>
*/
@Component
public class TradeOrderDirective extends AbstractTemplateDirective {

    @Override
    public void execute(RenderHandler handler) throws IOException, TemplateException {
        Long id = handler.getLong("id");
        SysSite site = getSite(handler);
        if (CommonUtils.notEmpty(id)) {
            TradeOrder entity = service.getEntity(id);
            if (null != entity && site.getId() == entity.getSiteId()) {
                handler.put("object", entity).render();
            }
        } else {
            Long[] ids = handler.getLongArray("ids");
            if (CommonUtils.notEmpty(ids)) {
                List<TradeOrder> entityList = service.getEntitys(ids);
                Map<String, TradeOrder> map = CommonUtils.listToMap(entityList, k -> k.getId().toString(), null,
                        entity -> site.getId() == entity.getSiteId());
                handler.put("map", map).render();
            }
        }
    }

    @Override
    public boolean needAppToken() {
        return true;
    }

    @Resource
    private TradeOrderService service;

}

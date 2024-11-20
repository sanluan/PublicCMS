package com.publiccms.views.directive.trade;

// Generated 2019-6-15 20:08:45 by com.publiccms.common.generator.SourceGenerator

import java.io.IOException;
import java.util.List;
import java.util.Map;

import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

import com.publiccms.common.base.AbstractTemplateDirective;
import com.publiccms.common.handler.RenderHandler;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.entities.sys.SysSite;
import com.publiccms.entities.trade.TradeRefund;
import com.publiccms.logic.service.trade.TradeRefundService;

import freemarker.template.TemplateException;

/**
*
* tradeRefund 退款查询指令
* <p>
* 参数列表
* <ul>
* <li><code>id</code> 退款id，结果返回<code>object</code>
* {@link com.publiccms.entities.trade.TradeRefund}
* <li><code>ids</code> 多个退款id，逗号或空格间隔，当id为空时生效，结果返回<code>map</code>(id,<code>object</code>)
* </ul>
* 使用示例
* <p>
* &lt;@trade.refund id=1&gt;${object.amount}&lt;/@trade.refund&gt;
* <p>
* &lt;@trade.refund ids=1,2,3&gt;&lt;#list map as
* k,v&gt;${k}:${v.amount}&lt;#sep&gt;,&lt;/#list&gt;&lt;/@trade.refund&gt;
* 
* <pre>
 &lt;script&gt;
  $.getJSON('${site.dynamicPath}api/directive/trade/refund?id=1&amp;appToken=接口访问授权Token', function(data){    
    console.log(data.amount);
  });
  &lt;/script&gt;
* </pre>
*/
@Component
public class TradeRefundDirective extends AbstractTemplateDirective {

    @Override
    public void execute(RenderHandler handler) throws IOException, TemplateException{
        Long id = handler.getLong("id");
        SysSite site = getSite(handler);
        if (CommonUtils.notEmpty(id)) {
            TradeRefund entity = service.getEntity(id);
            if (null != entity && site.getId() == entity.getSiteId()) {
                handler.put("object", entity).render();
            }
        } else {
            Long[] ids = handler.getLongArray("ids");
            if (CommonUtils.notEmpty(ids)) {
                List<TradeRefund> entityList = service.getEntitys(ids);
                Map<String, TradeRefund> map = CommonUtils.listToMapSorted(entityList, k -> k.getId().toString(), null, ids, entity -> site.getId() == entity.getSiteId());
                handler.put("map", map).render();
            }
        }
    }

    @Override
    public boolean needAppToken() {
        return true;
    }

    @Resource
    private TradeRefundService service;

}

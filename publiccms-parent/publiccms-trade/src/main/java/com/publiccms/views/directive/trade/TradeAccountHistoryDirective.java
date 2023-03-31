package com.publiccms.views.directive.trade;

// Generated 2019-6-16 9:47:27 by com.publiccms.common.generator.SourceGenerator

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import org.springframework.stereotype.Component;

import com.publiccms.common.base.AbstractTemplateDirective;
import com.publiccms.common.handler.RenderHandler;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.entities.sys.SysSite;
import com.publiccms.entities.trade.TradeAccountHistory;
import com.publiccms.logic.service.trade.TradeAccountHistoryService;

import freemarker.template.TemplateException;

/**
*
* tradeAccountHistory 账户历史查询指令
* <p>
* 参数列表
* <ul>
* <li><code>id</code> 账户历史id，结果返回<code>object</code>
* {@link com.publiccms.entities.trade.TradeAccountHistory}
* <li><code>ids</code> 多个账户历史id，逗号或空格间隔，当id为空时生效，结果返回<code>map</code>(id,<code>object</code>)
* </ul>
* 使用示例
* <p>
* &lt;@trade.accountHistory id=1&gt;${object.balance}&lt;/@trade.accountHistory&gt;
* <p>
* &lt;@trade.accountHistory ids=1,2,3&gt;&lt;#list map as
* k,v&gt;${k}:${v.balance}&lt;#sep&gt;,&lt;/#list&gt;&lt;/@trade.accountHistory&gt;
* 
* <pre>
 &lt;script&gt;
  $.getJSON('${site.dynamicPath}api/directive/trade/accountHistory?id=1&amp;appToken=接口访问授权Token', function(data){    
    console.log(data.balance);
  });
  &lt;/script&gt;
* </pre>
*/
@Component
public class TradeAccountHistoryDirective extends AbstractTemplateDirective {

    @Override
    public void execute(RenderHandler handler) throws IOException, TemplateException  {
        Long id = handler.getLong("id");
        SysSite site = getSite(handler);
        if (CommonUtils.notEmpty(id)) {
            TradeAccountHistory entity = service.getEntity(id);
            if (null != entity && site.getId() == entity.getSiteId()) {
                handler.put("object", entity).render();
            }
        } else {
            Long[] ids = handler.getLongArray("ids");
            if (CommonUtils.notEmpty(ids)) {
                List<TradeAccountHistory> entityList = service.getEntitys(ids);
                Map<String, TradeAccountHistory> map = CommonUtils.listToMap(entityList, k -> k.getId().toString(), null, entity -> site.getId() == entity.getSiteId());
                handler.put("map", map).render();
            }
        }
    }
    
    @Override
    public boolean needAppToken() {
        return true;
    }

    @Resource
    private TradeAccountHistoryService service;

}

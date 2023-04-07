package com.publiccms.views.directive.visit;

// Generated 2021-1-14 22:44:12 by com.publiccms.common.generator.SourceGenerator

import java.io.IOException;
import java.util.Date;

import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

import com.publiccms.common.base.AbstractTemplateDirective;
import com.publiccms.common.handler.RenderHandler;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.entities.visit.VisitItem;
import com.publiccms.entities.visit.VisitItemId;
import com.publiccms.logic.service.visit.VisitItemService;

import freemarker.template.TemplateException;

/**
*
* visitItem 访问项目报表查询指令
* <p>
* 参数列表
* <ul>
* <li><code>visitDate</code>:访问日期,【2020-01-01】
* <li><code>itemType</code>:访问项目类型,【category,content,user等页面统计时中的itemType】
* <li><code>itemId</code>:访问项目id,三个参数都不为空时,结果返回<code>object</code>
 * {@link com.publiccms.entities.visit.VisitItem}
* </ul>
* 使用示例
* <p>
* &lt;@visit.item visitDate='2020-01-01' visitHour=9&gt;${object.pv}&lt;/@visit.item&gt;
* <pre>
 &lt;script&gt;
  $.getJSON('${site.dynamicPath}api/directive/visit/item?visitDate=2020-01-01&amp;itemType=content&amp;itemId=1&amp;appToken=接口访问授权Token', function(data){    
    console.log(data.pv);
  });
  &lt;/script&gt;
* </pre>
*/
@Component
public class VisitItemDirective extends AbstractTemplateDirective {

    @Override
    public void execute(RenderHandler handler) throws IOException, TemplateException {
        Date visitDate = handler.getDate("visitDate");
        String itemType = handler.getString("itemType");
        String itemId = handler.getString("itemId");
        if (null != visitDate && CommonUtils.notEmpty(itemType) && CommonUtils.notEmpty(itemId)) {
            VisitItem entity = service.getEntity(new VisitItemId(getSite(handler).getId(), visitDate, itemType, itemId));
            if (null != entity) {
                handler.put("object", entity).render();
            }
        }
    }

    @Override
    public boolean needAppToken() {
        return true;
    }

    @Resource
    private VisitItemService service;

}

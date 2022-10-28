package com.publiccms.views.directive.visit;

// Generated 2021-1-14 22:44:12 by com.publiccms.common.generator.SourceGenerator

import java.io.IOException;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.publiccms.common.base.AbstractTemplateDirective;
import com.publiccms.common.handler.RenderHandler;
import com.publiccms.entities.visit.VisitDay;
import com.publiccms.entities.visit.VisitDayId;
import com.publiccms.logic.service.visit.VisitDayService;

/**
*
* visitDay 访问日报表查询指令
* <p>
* 参数列表
* <ul>
* <li><code>visitDate</code> 访问日期,【2020-01-01】
* <li><code>visitHour</code> 访问小时,【0-23】,两个参数都不为空时,结果返回<code>object</code>
 * {@link com.publiccms.entities.visit.VisitDay}
* </ul>
* 使用示例
* <p>
* &lt;@visit.day visitDate='2020-01-01' visitHour=9&gt;${object.pv}&lt;/@visit.day&gt;
* <pre>
 &lt;script&gt;
  $.getJSON('${site.dynamicPath}api/directive/visit/day?visitDate=2020-01-01&amp;visitHour=9&amp;appToken=接口访问授权Token', function(data){    
    console.log(data.pv);
  });
  &lt;/script&gt;
* </pre>
*/
@Component
public class VisitDayDirective extends AbstractTemplateDirective {

    @Override
    public void execute(RenderHandler handler) throws IOException, Exception {
        Date visitDate = handler.getDate("visitDate");
        Byte visitHour = handler.getByte("visitHour");
        if (null != visitDate && null != visitHour) {
            VisitDay entity = service.getEntity(new VisitDayId(getSite(handler).getId(), visitDate, visitHour));
            if (null != entity) {
                handler.put("object", entity).render();
            }
        }
    }

    @Override
    public boolean needAppToken() {
        return true;
    }

    @Autowired
    private VisitDayService service;

}

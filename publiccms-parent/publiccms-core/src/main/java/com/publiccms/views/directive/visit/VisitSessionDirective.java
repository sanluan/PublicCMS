package com.publiccms.views.directive.visit;

// Generated 2021-1-14 22:44:06 by com.publiccms.common.generator.SourceGenerator

import java.io.IOException;
import java.util.Date;

import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

import com.publiccms.common.base.AbstractTemplateDirective;
import com.publiccms.common.handler.RenderHandler;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.entities.visit.VisitSession;
import com.publiccms.entities.visit.VisitSessionId;
import com.publiccms.logic.service.visit.VisitSessionService;

/**
*
* visitSession 访问会话报表查询指令
* <p>
* 参数列表
* <ul>
* <li><code>visitDate</code>:访问日期,【2020-01-01】
* <li><code>sessionId</code>:会话id,两个参数都不为空时,结果返回<code>object</code>
 * {@link com.publiccms.entities.visit.VisitSession}
* </ul>
* 使用示例
* <p>
* &lt;@visit.session visitDate='2020-01-01' sessionId='xxxx-xxxx-xxxx'&gt;${object.pv}&lt;/@visit.session&gt;
* <pre>
 &lt;script&gt;
  $.getJSON('${site.dynamicPath}api/directive/visit/session?visitDate=2020-01-01&amp;sessionId=xxxx-xxxx-xxxx&amp;appToken=接口访问授权Token', function(data){    
    console.log(data.pv);
  });
  &lt;/script&gt;
* </pre>
*/
@Component
public class VisitSessionDirective extends AbstractTemplateDirective {

    @Override
    public void execute(RenderHandler handler) throws IOException, Exception {
        String sessionId = handler.getString("sessionId");
        Date visitDate = handler.getDate("visitDate");
        if (CommonUtils.notEmpty(sessionId) && null != visitDate) {
            VisitSession entity = service.getEntity(new VisitSessionId(getSite(handler).getId(), sessionId, visitDate));
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
    private VisitSessionService service;

}

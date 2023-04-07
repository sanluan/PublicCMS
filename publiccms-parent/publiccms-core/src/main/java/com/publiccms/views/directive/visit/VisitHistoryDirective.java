package com.publiccms.views.directive.visit;

// Generated 2021-1-14 22:43:59 by com.publiccms.common.generator.SourceGenerator

import java.io.IOException;
import java.util.List;
import java.util.Map;

import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

import com.publiccms.common.base.AbstractTemplateDirective;
import com.publiccms.common.handler.RenderHandler;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.entities.visit.VisitHistory;
import com.publiccms.logic.service.visit.VisitHistoryService;

import freemarker.template.TemplateException;

/**
 *
 * visitHistory 访问记录查询指令
 * <p>
 * 参数列表
 * <ul>
 * <li><code>id</code>:记录id,结果返回<code>object</code>
 * {@link com.publiccms.entities.visit.VisitHistory}
 * <li><code>ids</code>:
 * 多个记录id,逗号或空格间隔,当id为空时生效,结果返回<code>map</code>(id,<code>object</code>)
 * </ul>
 * 使用示例
 * <p>
 * &lt;@visit.history id=1&gt;${object.title}&lt;/@visit.history&gt;
 * <p>
 * &lt;@visit.history ids='1,2,3'&gt;&lt;#list map as
 * k,v&gt;${k}:${v.title}&lt;#sep&gt;,&lt;/#list&gt;&lt;/@visit.history&gt;
 * 
 * <pre>
 &lt;script&gt;
  $.getJSON('${site.dynamicPath}api/directive/visit/history?id=1&amp;appToken=接口访问授权Token', function(data){    
    console.log(data.title);
  });
  &lt;/script&gt;
 * </pre>
 */
@Component
public class VisitHistoryDirective extends AbstractTemplateDirective {

    @Override
    public void execute(RenderHandler handler) throws IOException, TemplateException {
        Long id = handler.getLong("id");
        if (CommonUtils.notEmpty(id)) {
            VisitHistory entity = service.getEntity(id);
            if (null != entity) {
                handler.put("object", entity).render();
            }
        } else {
            Long[] ids = handler.getLongArray("ids");
            if (CommonUtils.notEmpty(ids)) {
                List<VisitHistory> entityList = service.getEntitys(ids);
                Map<String, VisitHistory> map = CommonUtils.listToMap(entityList, k -> k.getId().toString(), null, null);
                handler.put("map", map).render();
            }
        }
    }

    @Override
    public boolean needAppToken() {
        return true;
    }

    @Resource
    private VisitHistoryService service;

}

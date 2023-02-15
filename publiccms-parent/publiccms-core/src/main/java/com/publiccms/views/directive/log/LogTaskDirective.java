package com.publiccms.views.directive.log;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

import com.publiccms.common.base.AbstractTemplateDirective;
import com.publiccms.common.handler.RenderHandler;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.entities.log.LogTask;
import com.publiccms.entities.sys.SysSite;
import com.publiccms.logic.service.log.LogTaskService;

/**
*
* logTask 任务计划日志查询指令
* <p>
* 参数列表
* <ul>
* <li><code>id</code>:日志id,结果返回<code>object</code>
* {@link com.publiccms.entities.log.LogTask}
* <li><code>ids</code>:
* 多个日志id,逗号或空格间隔,当id为空时生效,结果返回<code>map</code>(id,<code>object</code>)
* </ul>
* 使用示例
* <p>
* &lt;@log.task id=1&gt;${object.result}&lt;/@log.task&gt;
* <p>
* &lt;@log.task ids='1,2,3'&gt;&lt;#list map as
* k,v&gt;${k}:${v.result}&lt;#sep&gt;,&lt;/#list&gt;&lt;/@log.task&gt;
* 
* <pre>
&lt;script&gt;
$.getJSON('${site.dynamicPath}api/directive/log/task?id=1&amp;appToken=接口访问授权Token', function(data){    
  console.log(data.result);
});
&lt;/script&gt;
* </pre>
*/
@Component
public class LogTaskDirective extends AbstractTemplateDirective {

    @Override
    public void execute(RenderHandler handler) throws IOException, Exception {
        Long id = handler.getLong("id");
        SysSite site = getSite(handler);
        if (CommonUtils.notEmpty(id)) {
            LogTask entity = service.getEntity(id);
            if (null != entity && site.getId() == entity.getSiteId()) {
                handler.put("object", entity).render();
            }
        } else {
            Long[] ids = handler.getLongArray("ids");
            if (CommonUtils.notEmpty(ids)) {
                List<LogTask> entityList = service.getEntitys(ids);
                Map<String, LogTask> map = CommonUtils.listToMap(entityList, k -> k.getId().toString(), null,
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
    private LogTaskService service;

}
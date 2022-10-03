package com.publiccms.views.directive.log;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.publiccms.common.base.AbstractTemplateDirective;
import com.publiccms.common.handler.RenderHandler;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.entities.log.LogOperate;
import com.publiccms.entities.sys.SysSite;
import com.publiccms.logic.service.log.LogOperateService;

/**
*
* logOperate 操作日志查询指令
* <p>
* 参数列表
* <ul>
* <li><code>id</code> 日志id,结果返回<code>object</code>
* {@link com.publiccms.entities.log.LogOperate}
* <li><code>ids</code>
* 多个日志id,逗号或空格间隔,当id为空时生效,结果返回<code>map</code>(id,<code>object</code>)
* </ul>
* 使用示例
* <p>
* &lt;@log.operate id=1&gt;${object.content}&lt;/@log.operate&gt;
* <p>
* &lt;@log.operate ids='1,2,3'&gt;&lt;#list map as
* k,v&gt;${k}:${v.content}&lt;#sep&gt;,&lt;/#list&gt;&lt;/@log.operate&gt;
* 
* <pre>
&lt;script&gt;
$.getJSON('//cms.publiccms.com/api/directive/log/operate?id=1&amp;appToken=接口访问授权Token', function(data){    
  console.log(data.content);
});
&lt;/script&gt;
* </pre>
*/
@Component
public class LogOperateDirective extends AbstractTemplateDirective {

    @Override
    public void execute(RenderHandler handler) throws IOException, Exception {
        Long id = handler.getLong("id");
        SysSite site = getSite(handler);
        if (CommonUtils.notEmpty(id)) {
            LogOperate entity = service.getEntity(id);
            if (null != entity && site.getId() == entity.getSiteId()) {
                handler.put("object", entity).render();
            }
        } else {
            Long[] ids = handler.getLongArray("ids");
            if (CommonUtils.notEmpty(ids)) {
                List<LogOperate> entityList = service.getEntitys(ids);
                Map<String, LogOperate> map = CommonUtils.listToMap(entityList, k -> k.getId().toString(), null,
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
    private LogOperateService service;

}
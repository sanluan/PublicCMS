package com.publiccms.views.directive.sys;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.springframework.stereotype.Component;

import com.publiccms.common.base.AbstractTemplateDirective;
import com.publiccms.common.handler.RenderHandler;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.entities.sys.SysApp;
import com.publiccms.entities.sys.SysSite;
import com.publiccms.logic.service.sys.SysAppService;

/**
*
* sysApp 应用查询指令
* <p>
* 参数列表
* <ul>
* <li><code>id</code> 应用id,结果返回<code>object</code>
* {@link com.publiccms.entities.sys.SysApp}
* <li><code>ids</code>
* 多个应用id,逗号或空格间隔,当id为空时生效,结果返回<code>map</code>(id,<code>object</code>)
* </ul>
* 使用示例
* <p>
* &lt;@sys.app id=1&gt;${object.channel}&lt;/@sys.app&gt;
* <p>
* &lt;@sys.app ids='1,2,3'&gt;&lt;#list map as
* k,v&gt;${k}:${v.channel}&lt;#sep&gt;,&lt;/#list&gt;&lt;/@sys.app&gt;
* 
* <pre>
&lt;script&gt;
$.getJSON('${site.dynamicPath}api/directive/sys/app?id=1&amp;appToken=接口访问授权Token', function(data){    
  console.log(data.channel);
});
&lt;/script&gt;
* </pre>
*/
@Component
public class SysAppDirective extends AbstractTemplateDirective {

    @Override
    public void execute(RenderHandler handler) throws IOException, Exception {
        Integer id = handler.getInteger("id");
        SysSite site = getSite(handler);
        if (CommonUtils.notEmpty(id)) {
            SysApp entity = service.getEntity(id);
            if (null != entity && site.getId() == entity.getSiteId()) {
                handler.put("object", entity).render();
            }
        } else {
            Integer[] ids = handler.getIntegerArray("ids");
            if (CommonUtils.notEmpty(ids)) {
                List<SysApp> entityList = service.getEntitys(ids);
                Map<String, SysApp> map = CommonUtils.listToMap(entityList, k -> k.getId().toString(), null,
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
    private SysAppService service;

}

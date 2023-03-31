package com.publiccms.views.directive.sys;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.springframework.stereotype.Component;

import com.publiccms.common.base.AbstractTemplateDirective;
import com.publiccms.common.handler.RenderHandler;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.entities.sys.SysRole;
import com.publiccms.entities.sys.SysSite;
import com.publiccms.logic.service.sys.SysRoleService;

import freemarker.template.TemplateException;

/**
*
* sysRole 角色查询指令
* <p>
* 参数列表
* <ul>
* <li><code>id</code>:角色id,结果返回<code>object</code>
* {@link com.publiccms.entities.sys.SysRole}
* <li><code>ids</code>:
* 多个角色id,逗号或空格间隔,当id为空时生效,结果返回<code>map</code>(id,<code>object</code>)
* </ul>
* 使用示例
* <p>
* &lt;@sys.role id=1&gt;${object.name}&lt;/@sys.role&gt;
* <p>
* &lt;@sys.role ids='1,2,3'&gt;&lt;#list map as
* k,v&gt;${k}:${v.name}&lt;#sep&gt;,&lt;/#list&gt;&lt;/@sys.role&gt;
* 
* <pre>
&lt;script&gt;
$.getJSON('//sys.publicsys.com/api/directive/sys/role?id=1&amp;appToken=接口访问授权Token', function(data){    
  console.log(data.name);
});
&lt;/script&gt;
* </pre>
*/
@Component
public class SysRoleDirective extends AbstractTemplateDirective {

    @Override
    public void execute(RenderHandler handler) throws IOException, TemplateException {
        Integer id = handler.getInteger("id");
        SysSite site = getSite(handler);
        if (CommonUtils.notEmpty(id)) {
            SysRole entity = service.getEntity(id);
            if (null != entity && site.getId() == entity.getSiteId()) {
                handler.put("object", entity).render();
            }
        } else {
            Integer[] ids = handler.getIntegerArray("ids");
            if (CommonUtils.notEmpty(ids)) {
                List<SysRole> entityList = service.getEntitys(ids);
                Map<String, SysRole> map = CommonUtils.listToMap(entityList, k -> k.getId().toString(), null,
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
    private SysRoleService service;

}

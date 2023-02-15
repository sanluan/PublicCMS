package com.publiccms.views.directive.sys;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

import com.publiccms.common.base.AbstractTemplateDirective;
import com.publiccms.common.handler.RenderHandler;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.entities.sys.SysSite;
import com.publiccms.entities.sys.SysUser;
import com.publiccms.logic.component.template.TemplateComponent;
import com.publiccms.logic.service.sys.SysUserService;

/**
 *
 * sysUser 用户查询指令
 * <p>
 * 参数列表
 * <ul>
 * <li><code>id</code>:用户id,结果返回<code>object</code>
 * {@link com.publiccms.entities.sys.SysUser}
 * <li><code>ids</code>:
 * 多个用户id,逗号或空格间隔,当id为空时生效,结果返回<code>map</code>(id,<code>object</code>)
 * <li><code>absoluteURL</code>:cover处理为绝对路径 默认为<code> true</code>
 * </ul>
 * 使用示例
 * <p>
 * &lt;@sys.user id=1&gt;${object.name}&lt;/@sys.user&gt;
 * <p>
 * &lt;@sys.user ids='1,2,3'&gt;&lt;#list map as
 * k,v&gt;${k}:${v.name}&lt;#sep&gt;,&lt;/#list&gt;&lt;/@sys.user&gt;
 * 
 * <pre>
&lt;script&gt;
$.getJSON('//sys.publicsys.com/api/directive/sys/user?id=1&amp;appToken=接口访问授权Token', function(data){    
  console.log(data.name);
});
&lt;/script&gt;
 * </pre>
 */
@Component
public class SysUserDirective extends AbstractTemplateDirective {

    @Override
    public void execute(RenderHandler handler) throws IOException, Exception {
        Long id = handler.getLong("id");
        boolean absoluteURL = handler.getBoolean("absoluteURL", true);
        SysSite site = getSite(handler);
        if (CommonUtils.notEmpty(id)) {
            SysUser entity = service.getEntity(id);
            if (null != entity && site.getId() == entity.getSiteId()) {
                if (absoluteURL) {
                    entity.setCover(TemplateComponent.getUrl(site.getSitePath(), entity.getCover()));
                }
                entity.setPassword(null);
                handler.put("object", entity).render();
            }
        } else {
            Long[] ids = handler.getLongArray("ids");
            if (CommonUtils.notEmpty(ids)) {
                List<SysUser> entityList = service.getEntitys(ids);
                Consumer<SysUser> consumer = null;
                if (absoluteURL) {
                    consumer = e -> {
                        e.setCover(TemplateComponent.getUrl(site.getSitePath(), e.getCover()));
                    };
                }
                Map<String, SysUser> map = CommonUtils.listToMap(entityList, k -> k.getId().toString(), consumer,
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
    private SysUserService service;

}

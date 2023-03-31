package com.publiccms.views.directive.sys;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.springframework.stereotype.Component;

import com.publiccms.common.base.AbstractTemplateDirective;
import com.publiccms.common.handler.RenderHandler;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.entities.sys.SysModule;
import com.publiccms.logic.service.sys.SysModuleService;

import freemarker.template.TemplateException;

/**
 *
 * sysModule 模块查询指令
 * <p>
 * 参数列表
 * <ul>
 * <li><code>id</code>:模块id,结果返回<code>object</code>
 * {@link com.publiccms.entities.sys.SysModule}
 * <li><code>ids</code>:
 * 多个模块id,逗号或空格间隔,当id为空时生效,结果返回<code>map</code>(id,<code>object</code>)
 * </ul>
 * 使用示例
 * <p>
 * &lt;@sys.module id='page'&gt;${object.url}&lt;/@sys.module&gt;
 * <p>
 * &lt;@sys.module ids='page,develop'&gt;&lt;#list map as
 * k,v&gt;${k}:${v.url}&lt;#sep&gt;,&lt;/#list&gt;&lt;/@sys.module&gt;
 * 
 * <pre>
&lt;script&gt;
$.getJSON('//sys.publicsys.com/api/directive/sys/module?id=page&amp;appToken=接口访问授权Token', function(data){    
  console.log(data.url);
});
&lt;/script&gt;
 * </pre>
 */
@Component
public class SysModuleDirective extends AbstractTemplateDirective {

    @Override
    public void execute(RenderHandler handler) throws IOException, TemplateException {
        String id = handler.getString("id");
        if (CommonUtils.notEmpty(id)) {
            SysModule entity = service.getEntity(id);
            if (null != entity) {
                handler.put("object", entity).render();
            }
        } else {
            String[] ids = handler.getStringArray("ids");
            if (CommonUtils.notEmpty(ids)) {
                List<SysModule> entityList = service.getEntitys(ids);
                Map<String, SysModule> map = CommonUtils.listToMap(entityList, k -> k.getId(), null, null);
                handler.put("map", map).render();
            }
        }
    }

    @Override
    public boolean needAppToken() {
        return true;
    }

    @Resource
    private SysModuleService service;

}

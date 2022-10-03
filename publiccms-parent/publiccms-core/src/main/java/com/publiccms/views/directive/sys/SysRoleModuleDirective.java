package com.publiccms.views.directive.sys;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.publiccms.common.base.AbstractTemplateDirective;
import com.publiccms.common.handler.RenderHandler;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.entities.sys.SysRoleModule;
import com.publiccms.logic.service.sys.SysRoleModuleService;
import com.publiccms.logic.service.sys.SysRoleService;

/**
 *
 * sysRoleModule 部门页面授权查询指令
 * <p>
 * 参数列表
 * <ul>
 * <li><code>roleIds</code> 多个角色id
 * <li><code>moduleId</code>
 * 模块id,roleIds、moduleId都存在时,结果返回<code>true</code>或<code>false</code>,表示这些角色是否拥有该模块的数据权限
 * <li><code>moduleIds</code>
 * 多个模块id,当roleIds存在,且moduleId为空时生效,结果返回<code>map</code>(模块id,<code>true</code>或<code>false</code>)
 * </ul>
 * 使用示例
 * <p>
 * &lt;@sys.roleModule roleIds='1,2,3'
 * modelId='page'&gt;${object}&lt;/@sys.roleModule&gt;
 * <p>
 * &lt;@sys.roleModule deptId=1 pages='/index.html,/search'&gt;&lt;#list map as
 * k,v&gt;${k}:${v}&lt;#sep&gt;,&lt;/#list&gt;&lt;/@sys.roleModule&gt;
 * 
 * <pre>
&lt;script&gt;
$.getJSON('//cms.publiccms.com/api/directive/sys/roleModule?roleIds=1,2,3&amp;modelId=page&amp;appToken=接口访问授权Token', function(data){    
  console.log(data);
});
&lt;/script&gt;
 * </pre>
 */
@Component
public class SysRoleModuleDirective extends AbstractTemplateDirective {

    @Override
    public void execute(RenderHandler handler) throws IOException, Exception {
        Integer[] roleIds = handler.getIntegerArray("roleIds");
        String moduleId = handler.getString("moduleId");
        if (CommonUtils.notEmpty(roleIds)) {
            if (CommonUtils.notEmpty(moduleId)) {
                handler.put("object", sysRoleService.showAllModule(roleIds) || null != service.getEntity(roleIds, moduleId))
                        .render();
            } else {
                String[] moduleIds = handler.getStringArray("moduleIds");
                if (CommonUtils.notEmpty(moduleIds)) {
                    Map<String, Boolean> map = new LinkedHashMap<>();
                    if (sysRoleService.showAllModule(roleIds)) {
                        for (String id : moduleIds) {
                            map.put(id, true);
                        }
                    } else {
                        for (SysRoleModule entity : service.getEntitys(roleIds, moduleIds)) {
                            map.put(entity.getId().getModuleId(), true);
                        }
                    }
                    handler.put("map", map).render();
                }
            }
        }
    }

    @Override
    public boolean needAppToken() {
        return true;
    }

    @Autowired
    private SysRoleService sysRoleService;
    @Autowired
    private SysRoleModuleService service;

}
package com.publiccms.views.directive.sys;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

import jakarta.annotation.Resource;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import com.publiccms.common.api.Config;
import com.publiccms.common.base.AbstractTemplateDirective;
import com.publiccms.common.constants.CommonConstants;
import com.publiccms.common.handler.RenderHandler;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.entities.sys.SysRoleModule;
import com.publiccms.entities.sys.SysSite;
import com.publiccms.logic.component.config.ConfigComponent;
import com.publiccms.logic.component.config.SiteConfigComponent;
import com.publiccms.logic.service.sys.SysRoleModuleService;
import com.publiccms.logic.service.sys.SysRoleService;

/**
 *
 * sysRoleModule 角色模块授权查询指令
 * <p>
 * 参数列表
 * <ul>
 * <li><code>roleIds</code>:多个角色id
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
 * &lt;@sys.roleModule roleIds='1,2,3' modelIds='page,content'&gt;&lt;#list map as
 * k,v&gt;${k}:${v}&lt;#sep&gt;,&lt;/#list&gt;&lt;/@sys.roleModule&gt;
 *
 * <pre>
&lt;script&gt;
$.getJSON('${site.dynamicPath}api/directive/sys/roleModule?roleIds=1,2,3&amp;modelId=page&amp;appToken=接口访问授权Token', function(data){
  console.log(data);
});
&lt;/script&gt;
 * </pre>
 */
@Component
public class SysRoleModuleDirective extends AbstractTemplateDirective {
    @Resource
    private SysRoleService sysRoleService;
    @Resource
    protected ConfigComponent configComponent;

    @Override
    public void execute(RenderHandler handler) throws IOException, Exception {
        Integer[] roleIds = handler.getIntegerArray("roleIds");
        String moduleId = handler.getString("moduleId");
        if (CommonUtils.notEmpty(roleIds)) {
            SysSite site = getSite(handler);
            Map<String, String> config = configComponent.getConfigData(site.getId(), Config.CONFIG_CODE_SITE);
            String[] excludeModules = StringUtils.split(config.get(SiteConfigComponent.CONFIG_SITE_EXCLUDE_MODULE),
                    CommonConstants.COMMA);
            if (CommonUtils.notEmpty(moduleId)) {
                handler.put("object",
                        (CommonUtils.empty(excludeModules) || !ArrayUtils.contains(excludeModules, moduleId))
                                && (sysRoleService.showAllModule(roleIds) || null != service.getEntity(roleIds, moduleId)))
                        .render();
            } else {
                String[] moduleIds = handler.getStringArray("moduleIds");
                if (CommonUtils.notEmpty(moduleIds)) {
                    Map<String, Boolean> map = new LinkedHashMap<>();
                    if (sysRoleService.showAllModule(roleIds)) {
                        for (String id : moduleIds) {
                            map.put(id, CommonUtils.empty(excludeModules) || !ArrayUtils.contains(excludeModules, id));
                        }
                    } else {
                        for (SysRoleModule entity : service.getEntitys(roleIds, moduleIds)) {
                            map.put(entity.getId().getModuleId(), CommonUtils.empty(excludeModules)
                                    || !ArrayUtils.contains(excludeModules, entity.getId().getModuleId()));
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

    @Resource
    private SysRoleModuleService service;

}
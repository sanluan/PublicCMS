package com.publiccms.views.directive.sys;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

import com.publiccms.common.base.AbstractTemplateDirective;
import com.publiccms.common.handler.RenderHandler;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.entities.sys.SysRoleAuthorized;
import com.publiccms.entities.sys.SysRoleAuthorizedId;
import com.publiccms.logic.service.sys.SysRoleAuthorizedService;
import com.publiccms.logic.service.sys.SysRoleService;

/**
 *
 * sysAuthorized 角色url授权查询指令
 * <p>
 * 参数列表
 * <ul>
 * <li><code>roleIds</code> 多个角色id
 * <li><code>url</code>
 * url,roleIds、url都存在时,结果返回<code>true</code>或<code>false</code>,表示这些角色是否拥有该url的权限
 * <li><code>urls</code>
 * 多个url,当roleIds存在,且url为空时生效,结果返回<code>map</code>(url,<code>true</code>或<code>false</code>)
 * </ul>
 * 使用示例
 * <p>
 * &lt;@sys.authorized roleIds='1,2,3'
 * url='cmsContent/list'&gt;${object}&lt;/@sys.authorized&gt;
 * <p>
 * &lt;@sys.authorized roleIds='1,2,3'
 * urls='cmsContent/list,cmsCategory/list'&gt;&lt;#list map as
 * k,v&gt;${k}:${v}&lt;#sep&gt;,&lt;/#list&gt;&lt;/@sys.authorized&gt;
 * 
 * <pre>
&lt;script&gt;
 $.getJSON('${site.dynamicPath}api/directive/sys/authorized?roleIds=1,2,3&amp;url=cmsContent/list&amp;appToken=接口访问授权Token', function(data){    
   console.log(data);
 });
 &lt;/script&gt;
 * </pre>
 */
@Component
public class SysAuthorizedDirective extends AbstractTemplateDirective {

    @Override
    public void execute(RenderHandler handler) throws IOException, Exception {
        Integer[] roleIds = handler.getIntegerArray("roleIds");
        String url = handler.getString("url");
        String[] urls = handler.getStringArray("urls");
        if (CommonUtils.notEmpty(roleIds)) {
            if (CommonUtils.notEmpty(url) && sysRoleService.showAllModule(roleIds)) {
                handler.put("object", true).render();
            } else if (CommonUtils.notEmpty(url)) {
                SysRoleAuthorizedId[] ids = new SysRoleAuthorizedId[roleIds.length];
                for (int i = 0; i < roleIds.length; i++) {
                    ids[i] = new SysRoleAuthorizedId(roleIds[i], url);
                }
                if (CommonUtils.notEmpty(service.getEntitys(ids))) {
                    handler.put("object", true).render();
                }
            } else if (CommonUtils.notEmpty(urls)) {
                Map<String, Boolean> map = new LinkedHashMap<>();
                if (sysRoleService.showAllModule(roleIds)) {
                    for (String u : urls) {
                        map.put(u, true);
                    }
                } else {
                    SysRoleAuthorizedId[] ids = new SysRoleAuthorizedId[urls.length * roleIds.length];
                    int n = 0;
                    for (int i = 0; i < urls.length; i++) {
                        map.put(urls[i], false);
                        for (int j = 0; j < roleIds.length; j++) {
                            ids[n++] = new SysRoleAuthorizedId(roleIds[j], urls[i]);
                        }
                    }
                    List<SysRoleAuthorized> entityList = service.getEntitys(ids);
                    for (SysRoleAuthorized entity : entityList) {
                        map.put(entity.getId().getUrl(), true);
                    }
                }
                handler.put("map", map).render();
            }
        }
    }

    @Override
    public boolean needAppToken() {
        return true;
    }

    @Resource
    private SysRoleService sysRoleService;
    @Resource
    private SysRoleAuthorizedService service;

}
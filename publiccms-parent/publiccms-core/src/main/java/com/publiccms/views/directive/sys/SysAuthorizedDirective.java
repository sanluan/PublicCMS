package com.publiccms.views.directive.sys;

import java.io.IOException;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import com.publiccms.common.api.Config;
import com.publiccms.common.base.AbstractTemplateDirective;
import com.publiccms.common.constants.CommonConstants;
import com.publiccms.common.handler.RenderHandler;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.entities.sys.SysModule;
import com.publiccms.entities.sys.SysRoleAuthorized;
import com.publiccms.entities.sys.SysRoleAuthorizedId;
import com.publiccms.entities.sys.SysSite;
import com.publiccms.logic.component.config.ConfigComponent;
import com.publiccms.logic.component.config.SiteConfigComponent;
import com.publiccms.logic.service.sys.SysModuleService;
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
    @Resource
    private SysRoleService sysRoleService;
    @Resource
    protected ConfigComponent configComponent;
    @Resource
    private SysModuleService moduleService;

    @Override
    public void execute(RenderHandler handler) throws IOException, Exception {
        Integer[] roleIds = handler.getIntegerArray("roleIds");
        String url = handler.getString("url");
        String[] urls = handler.getStringArray("urls");
        if (CommonUtils.notEmpty(roleIds)) {
            SysSite site = getSite(handler);
            Map<String, String> config = configComponent.getConfigData(site.getId(), Config.CONFIG_CODE_SITE);
            String excludeModules = config.get(SiteConfigComponent.CONFIG_SITE_EXCLUDE_MODULE);
            Set<String> excludeUrls = null;
            if (CommonUtils.notEmpty(excludeModules)) {
                excludeUrls = new HashSet<>();
                for (SysModule module : moduleService.getEntitys(StringUtils.split(excludeModules, CommonConstants.COMMA))) {
                    if (CommonUtils.notEmpty(module.getUrl())) {
                        int index = module.getUrl().indexOf("?");
                        excludeUrls.add(module.getUrl().substring(0, 0 < index ? index : module.getUrl().length()));
                    }
                    if (CommonUtils.notEmpty(module.getAuthorizedUrl())) {
                        for (String tempUrl : StringUtils.split(module.getAuthorizedUrl(), CommonConstants.COMMA)) {
                            excludeUrls.add(tempUrl);
                        }
                    }
                }
            }
            if (CommonUtils.notEmpty(url) && sysRoleService.showAllModule(roleIds)
                    && (null == excludeUrls || !excludeUrls.contains(url))) {
                handler.put("object", true).render();
            } else if (CommonUtils.notEmpty(url) && (null == excludeUrls || !excludeUrls.contains(url))) {
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
                        map.put(u, null == excludeUrls || !excludeUrls.contains(url));
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
                        map.put(entity.getId().getUrl(), null == excludeUrls || !excludeUrls.contains(url));
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
    private SysRoleAuthorizedService service;

}
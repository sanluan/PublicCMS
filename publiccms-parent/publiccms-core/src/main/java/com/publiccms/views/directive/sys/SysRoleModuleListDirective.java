package com.publiccms.views.directive.sys;

// Generated 2015-7-22 13:48:39 by com.publiccms.common.generator.SourceGenerator

import java.io.IOException;

import com.publiccms.common.base.AbstractTemplateDirective;
import com.publiccms.logic.service.sys.SysRoleModuleService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

import com.publiccms.common.handler.PageHandler;
import com.publiccms.common.handler.RenderHandler;

/**
*
* sysRoleModuleList 角色模块映射列表查询指令
* <p>
* 参数列表
* <ul>
* <li><code>roleId</code>:角色id
* <li><code>moduleId</code>:模块id
* <li><code>pageIndex</code>:页码
* <li><code>pageSize</code>:每页条数
* </ul>
* <p>
* 返回结果
* <ul>
* <li><code>page</code>:{@link com.publiccms.common.handler.PageHandler}
* <li><code>page.list</code>:List类型 查询结果实体列表
* {@link com.publiccms.entities.sys.SysRoleModule}
* </ul>
* 使用示例
* <p>
* &lt;@sys.roleModuleList roleId=1 pageSize=10&gt;&lt;#list page.list as
* a&gt;${a.id.roleId}&lt;#sep&gt;,&lt;/#list&gt;&lt;/@sys.roleModuleList&gt;
* 
* <pre>
&lt;script&gt;
 $.getJSON('${site.dynamicPath}api/directive/sys/sysDeptCategoryList?roleId=1&amp;pageSize=10&amp;appToken=接口访问授权Token', function(data){    
   console.log(data.totalCount);
 });
 &lt;/script&gt;
* </pre>
*/
@Component
public class SysRoleModuleListDirective extends AbstractTemplateDirective {

    @Override
    public void execute(RenderHandler handler) throws IOException, Exception {
        PageHandler page = service.getPage(handler.getInteger("roleId"), handler.getString("moduleId"),
                handler.getInteger("pageIndex", 1), handler.getInteger("pageSize"));
        handler.put("page", page).render();
    }

    @Override
    public boolean needAppToken() {
        return true;
    }

    @Resource
    private SysRoleModuleService service;

}
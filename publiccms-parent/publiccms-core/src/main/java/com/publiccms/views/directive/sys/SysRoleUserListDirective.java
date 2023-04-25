package com.publiccms.views.directive.sys;

// Generated 2015-7-20 11:46:39 by com.publiccms.common.generator.SourceGenerator

import java.io.IOException;

import com.publiccms.common.base.AbstractTemplateDirective;
import com.publiccms.logic.service.sys.SysRoleUserService;

import freemarker.template.TemplateException;

import javax.annotation.Resource;
import org.springframework.stereotype.Component;

import com.publiccms.common.handler.PageHandler;
import com.publiccms.common.handler.RenderHandler;

/**
*
* sysRoleUserList 角色用户列表查询指令
* <p>
* 参数列表
* <ul>
* <li><code>roleId</code>:角色id
* <li><code>userId</code>:用户id
* <li><code>pageIndex</code>:页码
* <li><code>pageSize</code>:每页条数
* </ul>
* <p>
* 返回结果
* <ul>
* <li><code>page</code>:{@link com.publiccms.common.handler.PageHandler}
* <li><code>page.list</code>:List类型 查询结果实体列表
* {@link com.publiccms.entities.sys.SysRoleUser}
* </ul>
* 使用示例
* <p>
* &lt;@sys.roleUserList roleId=1 pageSize=10&gt;&lt;#list page.list as
* a&gt;${a.id.roleId}&lt;#sep&gt;,&lt;/#list&gt;&lt;/@sys.roleUserList&gt;
*
* <pre>
&lt;script&gt;
 $.getJSON('${site.dynamicPath}api/directive/sys/roleUserList?roleId=1&amp;pageSize=10&amp;appToken=接口访问授权Token', function(data){
   console.log(data.page.totalCount);
 });
 &lt;/script&gt;
* </pre>
*/
@Component
public class SysRoleUserListDirective extends AbstractTemplateDirective {

    @Override
    public void execute(RenderHandler handler) throws IOException, TemplateException {
        PageHandler page = service.getPage(handler.getInteger("roleId"), handler.getLong("userId"),
                handler.getInteger("pageIndex", 1), handler.getInteger("pageSize", 30));
        handler.put("page", page).render();
    }

    @Override
    public boolean needAppToken() {
        return true;
    }

    @Resource
    private SysRoleUserService service;

}
package com.publiccms.views.directive.sys;

// Generated 2015-7-22 13:48:39 by com.publiccms.common.generator.SourceGenerator

import java.io.IOException;

import com.publiccms.common.base.AbstractTemplateDirective;
import com.publiccms.logic.service.sys.SysModuleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.publiccms.common.handler.PageHandler;
import com.publiccms.common.handler.RenderHandler;

/**
*
* sysModuleList 模块列表查询指令
* <p>
* 参数列表
* <ul>
* <li><code>advanced</code> 开启高级选项, 默认为<code>false</code>
* <li><code>menu</code> 高级选项:是否菜单,【true,false】
* <li><code>parentId</code> 父模块id
* <li><code>pageIndex</code> 页码
* <li><code>pageSize</code> 每页条数
* </ul>
* <p>
* 返回结果
* <ul>
* <li><code>page</code> {@link com.publiccms.common.handler.PageHandler}
* <li><code>page.list</code> List类型 查询结果实体列表
* {@link com.publiccms.entities.sys.SysModule}
* </ul>
* 使用示例
* <p>
* &lt;@sys.moduleList parentId='page' pageSize=10&gt;&lt;#list page.list as
* a&gt;${springMacroRequestContext.getMessage('menu.'+a.id)}&lt;#sep&gt;,&lt;/#list&gt;&lt;/@sys.moduleList&gt;
* 
* <pre>
&lt;script&gt;
 $.getJSON('//cms.publiccms.com/api/directive/sys/moduleList?parentId=page&amp;pageSize=10&amp;appToken=接口访问授权Token', function(data){    
   console.log(data.totalCount);
 });
 &lt;/script&gt;
* </pre>
*/
@Component
public class SysModuleListDirective extends AbstractTemplateDirective {

    @Override
    public void execute(RenderHandler handler) throws IOException, Exception {
        Boolean menu = null;
        if (!getAdvanced(handler)) {
            menu = handler.getBoolean("menu", true);
        }
        PageHandler page = service.getPage(handler.getString("parentId"), menu, handler.getInteger("pageIndex", 1),
                handler.getInteger("pageSize", 30));
        handler.put("page", page).render();
    }

    @Override
    public boolean needAppToken() {
        return true;
    }

    @Override
    public boolean supportAdvanced() {
        return true;
    }

    @Autowired
    private SysModuleService service;

}
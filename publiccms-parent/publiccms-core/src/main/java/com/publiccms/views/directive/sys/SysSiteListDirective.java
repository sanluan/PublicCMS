package com.publiccms.views.directive.sys;

// Generated 2016-1-20 11:19:19 by com.publiccms.common.generator.SourceGenerator

import java.io.IOException;

import com.publiccms.common.base.AbstractTemplateDirective;
import com.publiccms.logic.service.sys.SysSiteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.publiccms.common.handler.PageHandler;
import com.publiccms.common.handler.RenderHandler;

/**
*
* sysSiteList 站点列表查询指令
* <p>
* 参数列表
* <ul>
* <li><code>advanced</code> 开启高级选项, 默认为<code>false</code>
* <li><code>disabled</code> 高级选项:已禁用,【true,false】
* <li><code>parentId</code> 父站点id
* <li><code>name</code> 名称
* <li><code>pageIndex</code> 页码
* <li><code>pageSize</code> 每页条数
* </ul>
* <p>
* 返回结果
* <ul>
* <li><code>page</code> {@link com.publiccms.common.handler.PageHandler}
* <li><code>page.list</code> List类型 查询结果实体列表
* {@link com.publiccms.entities.sys.SysSite}
* </ul>
* 使用示例
* <p>
* &lt;@sys.siteList pageSize=10&gt;&lt;#list page.list as
* a&gt;${a.name}&lt;#sep&gt;,&lt;/#list&gt;&lt;/@sys.siteList&gt;
* 
* <pre>
&lt;script&gt;
 $.getJSON('${site.dynamicPath}api/directive/sys/siteList?pageSize=10&amp;appToken=接口访问授权Token', function(data){    
   console.log(data.totalCount);
 });
 &lt;/script&gt;
* </pre>
*/
@Component
public class SysSiteListDirective extends AbstractTemplateDirective {

    @Override
    public void execute(RenderHandler handler) throws IOException, Exception {
        Boolean disabled = false;
        if (getAdvanced(handler)) {
            disabled = handler.getBoolean("disabled", false);
        }
        PageHandler page = service.getPage(disabled, handler.getShort("parentId"), handler.getString("name"),
                handler.getInteger("pageIndex", 1), handler.getInteger("pageSize", 30));
        handler.put("page", page).render();
    }

    @Override
    public boolean supportAdvanced() {
        return true;
    }

    @Override
    public boolean needAppToken() {
        return true;
    }

    @Autowired
    private SysSiteService service;

}
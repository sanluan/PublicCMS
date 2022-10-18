package com.publiccms.views.directive.sys;

// Generated 2016-3-1 17:24:12 by com.publiccms.common.generator.SourceGenerator

import java.io.IOException;

import com.publiccms.common.base.AbstractTemplateDirective;
import com.publiccms.logic.service.sys.SysAppTokenService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

import com.publiccms.common.handler.PageHandler;
import com.publiccms.common.handler.RenderHandler;

/**
*
* sysAppTokenList 应用授权列表查询指令
* <p>
* 参数列表
* <ul>
* <li><code>appId</code> 应用id
* <li><code>pageIndex</code> 页码
* <li><code>pageSize</code> 每页条数
* </ul>
* <p>
* 返回结果
* <ul>
* <li><code>page</code> {@link com.publiccms.common.handler.PageHandler}
* <li><code>page.list</code> List类型 查询结果实体列表
* {@link com.publiccms.entities.sys.SysAppToken}
* </ul>
* 使用示例
* <p>
* &lt;@sys.appTokenList pageSize=10&gt;&lt;#list page.list as
* a&gt;${a.authToken}&lt;#sep&gt;,&lt;/#list&gt;&lt;/@sys.appTokenList&gt;
* 
*/
@Component
public class SysAppTokenListDirective extends AbstractTemplateDirective {

    @Override
    public void execute(RenderHandler handler) throws IOException, Exception {
        PageHandler page = service.getPage(handler.getInteger("appId"), handler.getInteger("pageIndex", 1),
                handler.getInteger("pageSize", 30));
        handler.put("page", page).render();
    }

    @Override
    public boolean httpEnabled() {
        return false;
    }

    @Resource
    private SysAppTokenService service;

}
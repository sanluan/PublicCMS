package com.publiccms.views.directive.sys;

// Generated 2016-1-20 11:19:18 by com.publiccms.common.generator.SourceGenerator

import java.io.IOException;

import com.publiccms.common.base.AbstractTemplateDirective;
import com.publiccms.logic.service.sys.SysDomainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.publiccms.common.handler.PageHandler;
import com.publiccms.common.handler.RenderHandler;

/**
*
* sysDomainList 域名列表查询指令
* <p>
* 参数列表
* <ul>
* <li><code>advanced</code> 开启高级选项, 默认为<code>false</code>
* <li><code>siteId</code> 高级选项:站点id
* <li><code>wild</code> 高级选项:通配域名,【true,false】
* <li><code>pageIndex</code> 页码
* <li><code>pageSize</code> 每页条数
* </ul>
* <p>
* 返回结果
* <ul>
* <li><code>page</code> {@link com.publiccms.common.handler.PageHandler}
* <li><code>page.list</code> List类型 查询结果实体列表
* {@link com.publiccms.entities.sys.SysDomain}
* </ul>
* 使用示例
* <p>
* &lt;@sys.domainList deptId=1 pageSize=10&gt;&lt;#list page.list as
* a&gt;${a.name}&lt;#sep&gt;,&lt;/#list&gt;&lt;/@sys.domainList&gt;
* 
* <pre>
 &lt;script&gt;
  $.getJSON('//cms.publiccms.com/api/directive/sys/domainList?pageSize=10&amp;appToken=接口访问授权Token', function(data){    
    console.log(data.totalCount);
  });
  &lt;/script&gt;
* </pre>
*/
@Component
public class SysDomainListDirective extends AbstractTemplateDirective {

    @Override
    public void execute(RenderHandler handler) throws IOException, Exception {
        Short siteId = null;
        if (getAdvanced(handler)) {
            siteId = handler.getShort("siteId");
        } else {
            siteId = getSite(handler).getId();
        }
        PageHandler page = service.getPage(siteId, handler.getBoolean("wild"), handler.getInteger("pageIndex", 1),
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
    private SysDomainService service;

}
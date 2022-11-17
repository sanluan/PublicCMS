package com.publiccms.views.directive.sys;

// Generated 2015-7-3 16:18:22 by com.publiccms.common.generator.SourceGenerator

import java.io.IOException;

import com.publiccms.common.base.AbstractTemplateDirective;
import com.publiccms.logic.service.sys.SysTaskService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

import com.publiccms.common.handler.PageHandler;
import com.publiccms.common.handler.RenderHandler;

/**
*
* sysTaskList 任务计划列表查询指令
* <p>
* 参数列表
* <ul>
* <li><code>status</code> 状态【0:就绪,1:执行中,2:暂停,3:错误】
* <li><code>startUpdateDate</code> 起始更新日期,【2020-01-01 23:59:59】,【2020-01-01】
* <li><code>pageIndex</code> 页码
* <li><code>pageSize</code> 每页条数
* </ul>
* <p>
* 返回结果
* <ul>
* <li><code>page</code> {@link com.publiccms.common.handler.PageHandler}
* <li><code>page.list</code> List类型 查询结果实体列表
* {@link com.publiccms.entities.sys.SysTask}
* </ul>
* 使用示例
* <p>
* &lt;@sys.taskList deptId=1 pageSize=10&gt;&lt;#list page.list as
* a&gt;${a.id.deptId}&lt;#sep&gt;,&lt;/#list&gt;&lt;/@sys.taskList&gt;
* 
* <pre>
&lt;script&gt;
 $.getJSON('${site.dynamicPath}api/directive/sys/taskList?deptId=1&amp;pageSize=10&amp;appToken=接口访问授权Token', function(data){    
   console.log(data.totalCount);
 });
 &lt;/script&gt;
* </pre>
*/
@Component
public class SysTaskListDirective extends AbstractTemplateDirective {

    @Override
    public void execute(RenderHandler handler) throws IOException, Exception {
        PageHandler page = service.getPage(getSite(handler).getId(), handler.getInteger("status"),
                handler.getDate("startUpdateDate"), handler.getInteger("pageIndex", 1),
                handler.getInteger("pageSize", 30));
        handler.put("page", page).render();
    }
    
    @Override
    public boolean needAppToken() {
        return true;
    }

    @Resource
    private SysTaskService service;

}
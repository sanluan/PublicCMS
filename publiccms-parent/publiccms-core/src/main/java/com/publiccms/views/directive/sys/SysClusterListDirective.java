package com.publiccms.views.directive.sys;

// Generated 2016-7-16 11:56:50 by com.publiccms.common.generator.SourceGenerator

import java.io.IOException;

import com.publiccms.common.base.AbstractTemplateDirective;
import com.publiccms.logic.service.sys.SysClusterService;
import javax.annotation.Resource;
import org.springframework.stereotype.Component;

import com.publiccms.common.handler.PageHandler;
import com.publiccms.common.handler.RenderHandler;

/**
*
* sysClusterList 系统节点列表查询指令
* <p>
* 参数列表
* <ul>
* <li><code>startHeartbeatDate</code> 起始心跳日期,【2020-01-01 23:59:59】,【2020-01-01】
* <li><code>endHeartbeatDate</code> 终止心跳日期,【2020-01-01 23:59:59】,【2020-01-01】
* <li><code>master</code> 是否主节点,【true,false】,默认false
* <li><code>orderField</code> 排序字段,[createDate:上次登录日期,heartbeatDate:创建日期,id],默认id倒序
* <li><code>orderType</code> 排序类型,【asc:正序,desc:倒序】,默认为倒序
* <li><code>pageIndex</code> 页码
* <li><code>pageSize</code> 每页条数
* </ul>
* <p>
* 返回结果
* <ul>
* <li><code>page</code> {@link com.publiccms.common.handler.PageHandler}
* <li><code>page.list</code> List类型 查询结果实体列表
* {@link com.publiccms.entities.sys.SysCluster}
* </ul>
* 使用示例
* <p>
* &lt;@sys.clusterList pageSize=10&gt;&lt;#list page.list as
* a&gt;${a.uuid}&lt;#sep&gt;,&lt;/#list&gt;&lt;/@sys.clusterList&gt;
* 
* <pre>
 &lt;script&gt;
  $.getJSON('${site.dynamicPath}api/directive/sys/clusterList?pageSize=10', function(data){    
    console.log(data.totalCount);
  });
  &lt;/script&gt;
* </pre>
*/
@Component
public class SysClusterListDirective extends AbstractTemplateDirective {

    @Override
    public void execute(RenderHandler handler) throws IOException, Exception {
        PageHandler page = service.getPage(handler.getDate("startHeartbeatDate"), handler.getDate("endHeartbeatDate"),
                handler.getBoolean("master"), handler.getString("orderField"), handler.getString("orderType"),
                handler.getInteger("pageIndex", 1), handler.getInteger("pageSize", 30));
        handler.put("page", page).render();
    }

    @Override
    public boolean needAppToken() {
        return true;
    }

    @Resource
    private SysClusterService service;

}
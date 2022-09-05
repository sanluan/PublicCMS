package com.publiccms.views.directive.sys;

// Generated 2016-3-1 17:24:12 by com.publiccms.common.source.SourceGenerator

import java.io.IOException;

import com.publiccms.common.base.AbstractTemplateDirective;
import com.publiccms.logic.service.sys.SysAppClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.publiccms.common.handler.PageHandler;
import com.publiccms.common.handler.RenderHandler;

/**
*
* sysAppClientList 应用客户端列表查询指令
* <p>
* 参数列表
* <ul>
* <li><code>advanced</code> 开启高级选项， 默认为<code> false</code>
* <li><code>disabled</code> 高级选项:禁用状态,【true,false】,默认false
* <li><code>channel</code> 渠道
* <li><code>userId</code> 用户id
* <li><code>startLastLoginDate</code> 起始上次登录日期,【2020-01-01 23:59:59】,【2020-01-01】
* <li><code>endLastLoginDate</code> 终止上次登录日期,【2020-01-01 23:59:59】,【2020-01-01】
* <li><code>startCreateDate</code> 起始创建日期,【2020-01-01 23:59:59】,【2020-01-01】
* <li><code>endCreateDate</code> 终止创建日期,【2020-01-01 23:59:59】,【2020-01-01】
* <li><code>orderField</code> 排序字段,[lastLoginDate:上次登录日期,createDate:创建日期],默认创建日期倒叙
* <li><code>orderType</code> 排序类型,【asc:正序,desc:倒叙】,默认为倒叙
* <li><code>pageIndex</code> 页码
* <li><code>pageSize</code> 每页条数
* </ul>
* <p>
* 返回结果
* <ul>
* <li><code>page</code> {@link com.publiccms.common.handler.PageHandler}
* <li><code>page.list</code> List类型 查询结果实体列表
* {@link com.publiccms.entities.sys.SysAppClient}
* </ul>
* 使用示例
* <p>
* &lt;@sys.appClientList pageSize=10&gt;&lt;#list page.list as
* a&gt;${a.uuid}&lt;#sep&gt;,&lt;/#list&gt;&lt;/@sys.appClientList&gt;
* 
* <pre>
 &lt;script&gt;
  $.getJSON('//cms.publiccms.com/api/directive/sys/appClientList?pageSize=10&amp;appToken=接口访问授权Token', function(data){    
    console.log(data.totalCount);
  });
  &lt;/script&gt;
* </pre>
*/
@Component
public class SysAppClientListDirective extends AbstractTemplateDirective {

    @Override
    public void execute(RenderHandler handler) throws IOException, Exception {
        Boolean disabled = false;
        if (getAdvanced(handler)) {
            disabled = handler.getBoolean("disabled", false);
        }
        PageHandler page = service.getPage(getSite(handler).getId(), handler.getString("channel"), handler.getLong("userId"),
                handler.getDate("startLastLoginDate"), handler.getDate("endLastLoginDate"), handler.getDate("startCreateDate"),
                handler.getDate("endCreateDate"), disabled, handler.getString("orderField"), handler.getString("orderType"),
                handler.getInteger("pageIndex", 1), handler.getInteger("pageSize", 30));
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
    private SysAppClientService service;

}
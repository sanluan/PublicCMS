package com.publiccms.views.directive.sys;

// Generated 2016-1-20 11:19:18 by com.publiccms.common.generator.SourceGenerator

import java.io.IOException;

import com.publiccms.common.base.AbstractTemplateDirective;
import com.publiccms.logic.service.sys.SysUserTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.publiccms.common.handler.PageHandler;
import com.publiccms.common.handler.RenderHandler;

/**
*
* sysUserTokenList 用户登录授权列表查询指令
* <p>
* 参数列表
* <ul>
* <li><code>userId</code> 用户id
* <li><code>channel</code> 渠道
* <li><code>orderField</code> 排序字段,【expiryDate:过期日期,createDate:创建日期,】,默认创建日期按orderType排序
* <li><code>orderType</code> 排序类型,【asc:正序,desc:倒叙】,默认为倒叙
* <li><code>pageIndex</code> 页码
* <li><code>pageSize</code> 每页条数
* </ul>
* <p>
* 返回结果
* <ul>
* <li><code>page</code> {@link com.publiccms.common.handler.PageHandler}
* <li><code>page.list</code> List类型 查询结果实体列表
* {@link com.publiccms.entities.sys.SysUserToken}
* </ul>
* 使用示例
* <p>
* &lt;@sys.userTokenList userId=1 pageSize=10&gt;&lt;#list page.list as
* a&gt;${a.id.deptId}&lt;#sep&gt;,&lt;/#list&gt;&lt;/@sys.userTokenList&gt;
* 
* <pre>
&lt;script&gt;
 $.getJSON('//cms.publiccms.com/api/directive/sys/userTokenList?pageSize=10&amp;authToken=用户登录Token&amp;authUserId=用户id', function(data){    
   console.log(data.totalCount);
 });
 &lt;/script&gt;
* </pre>
*/
@Component
public class SysUserTokenListDirective extends AbstractTemplateDirective {

    @Override
    public void execute(RenderHandler handler) throws IOException, Exception {
        PageHandler page = service.getPage(getSite(handler).getId(), getUserId(handler, "userId"), handler.getString("channel"),
                handler.getString("orderField"), handler.getString("orderType"), handler.getInteger("pageIndex", 1),
                handler.getInteger("pageSize", 30));
        handler.put("page", page).render();
    }

    @Override
    public boolean needUserToken() {
        return true;
    }

    @Autowired
    private SysUserTokenService service;

}
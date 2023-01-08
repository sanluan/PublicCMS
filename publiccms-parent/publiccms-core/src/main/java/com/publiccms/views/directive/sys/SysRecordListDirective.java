package com.publiccms.views.directive.sys;

// Generated 2015-5-10 17:54:56 by com.publiccms.common.generator.SourceGenerator

import java.io.IOException;

import org.springframework.stereotype.Component;

import com.publiccms.common.base.AbstractTemplateDirective;
import com.publiccms.common.handler.PageHandler;
import com.publiccms.common.handler.RenderHandler;
import com.publiccms.logic.service.sys.SysRecordService;

import jakarta.annotation.Resource;

/**
 *
 * sysRecordList 自定义记录列表查询指令
 * <p>
 * <p>
 * 返回结果
 * <ul>
 * <li><code>list</code> List类型 查询结果实体列表
 * {@link com.publiccms.entities.sys.SysRecord}
 * </ul>
 * 使用示例
 * <p>
 * &lt;@sys.recordList&gt;&lt;#list list as
 * a&gt;${a.data}&lt;#sep&gt;,&lt;/#list&gt;&lt;/@sys.recordList&gt;
 * 
 * <pre>
 &lt;script&gt;
  $.getJSON('${site.dynamicPath}api/directive/sys/recordList?appToken=接口访问授权Token', function(data){    
    console.log(data);
  });
  &lt;/script&gt;
 * </pre>
 */
@Component
public class SysRecordListDirective extends AbstractTemplateDirective {

    @Override
    public void execute(RenderHandler handler) throws IOException, Exception {
        PageHandler page = service.getPage(getSite(handler).getId(), handler.getString("code"),
                handler.getDate("startCreateDate"), handler.getDate("endCreateDate"), handler.getString("orderType"),
                handler.getInteger("pageIndex", 1), handler.getInteger("pageSize", 30));
        handler.put("page", page).render();
    }

    @Override
    public boolean needAppToken() {
        return true;
    }

    @Resource
    private SysRecordService service;
}
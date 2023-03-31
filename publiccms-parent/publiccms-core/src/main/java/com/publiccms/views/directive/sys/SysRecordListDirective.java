package com.publiccms.views.directive.sys;

// Generated 2015-5-10 17:54:56 by com.publiccms.common.generator.SourceGenerator

import java.io.IOException;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.publiccms.common.base.AbstractTemplateDirective;
import com.publiccms.common.handler.PageHandler;
import com.publiccms.common.handler.RenderHandler;
import com.publiccms.logic.service.sys.SysRecordService;

import freemarker.template.TemplateException;

/**
 *
 * sysRecordList 自定义记录列表查询指令
 * <p>
 * 参数列表
 * <ul>
 * <li><code>code</code>:编码
 * <li><code>startCreateDate</code>:起始创建日期,【2020-01-01 23:59:59】,【2020-01-01】
 * <li><code>endCreateDate</code>:终止创建日期,高级选项禁用时不能超过现在,【2020-01-01
 * 23:59:59】,【2020-01-01】
 * <li><code>orderField</code>:排序字段,【updateDate:更新日期】默认创建日期倒序
 * <li><code>orderType</code>:排序类型,【asc:正序,desc:倒序】,默认为倒序
 * <li><code>pageIndex</code>:页码
 * <li><code>pageSize</code>:每页条数
 * </ul>
 * <p>
 * <p>
 * 返回结果
 * <ul>
 * <li><code>list</code>:List类型 查询结果实体列表
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
    public void execute(RenderHandler handler) throws IOException, TemplateException {
        PageHandler page = service.getPage(getSite(handler).getId(), handler.getString("code"),
                handler.getDate("startCreateDate"), handler.getDate("endCreateDate"), handler.getString("orderField"),
                handler.getString("orderType"), handler.getInteger("pageIndex", 1), handler.getInteger("pageSize", 30));
        handler.put("page", page).render();
    }

    @Override
    public boolean needAppToken() {
        return true;
    }

    @Resource
    private SysRecordService service;
}
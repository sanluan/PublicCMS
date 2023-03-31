package com.publiccms.views.directive.sys;

// Generated 2016-3-1 17:28:30 by com.publiccms.common.generator.SourceGenerator

import java.io.IOException;

import com.publiccms.common.base.AbstractTemplateDirective;
import com.publiccms.logic.service.sys.SysAppService;

import freemarker.template.TemplateException;

import javax.annotation.Resource;
import org.springframework.stereotype.Component;

import com.publiccms.common.handler.PageHandler;
import com.publiccms.common.handler.RenderHandler;

/**
 *
 * sysAppList 应用列表查询指令
 * <p>
 * 参数列表
 * <ul>
 * <li><code>channel</code>:渠道
 * <li><code>pageIndex</code>:页码
 * <li><code>pageSize</code>:每页条数
 * </ul>
 * <p>
 * 返回结果
 * <ul>
 * <li><code>page</code>:{@link com.publiccms.common.handler.PageHandler}
 * <li><code>page.list</code>:List类型 查询结果实体列表
 * {@link com.publiccms.entities.sys.SysApp}
 * </ul>
 * 使用示例
 * <p>
 * &lt;@sys.appList pageSize=10&gt;&lt;#list page.list as
 * a&gt;${a.channel}&lt;#sep&gt;,&lt;/#list&gt;&lt;/@sys.appList&gt;
 * 
 */
@Component
public class SysAppListDirective extends AbstractTemplateDirective {

    @Override
    public void execute(RenderHandler handler) throws IOException, TemplateException {
        PageHandler page = service.getPage(getSite(handler).getId(), handler.getString("channel"),
                handler.getInteger("pageIndex", 1), handler.getInteger("pageSize", 30));
        handler.put("page", page).render();
    }

    @Override
    public boolean httpEnabled() {
        return false;
    }

    @Resource
    private SysAppService service;

}
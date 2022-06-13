package com.publiccms.views.directive.sys;

import java.io.IOException;
import java.util.List;

import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

import com.publiccms.common.base.AbstractTemplateDirective;
import com.publiccms.common.handler.PageHandler;
import com.publiccms.common.handler.RenderHandler;
import com.publiccms.entities.sys.SysSite;
import com.publiccms.entities.sys.SysUser;
import com.publiccms.logic.component.template.TemplateComponent;
import com.publiccms.logic.service.sys.SysUserService;

/**
 *
 * SysUserListDirective
 * 
 */
@Component
public class SysUserListDirective extends AbstractTemplateDirective {

    @Override
    public void execute(RenderHandler handler) throws IOException, Exception {
        Boolean disabled = false;
        if (getAdvanced(handler)) {
            disabled = handler.getBoolean("disabled", false);
        }
        SysSite site = getSite(handler);
        PageHandler page = service.getPage(site.getId(), handler.getInteger("deptId"), handler.getDate("startRegisteredDate"),
                handler.getDate("endRegisteredDate"), handler.getDate("startLastLoginDate"), handler.getDate("endLastLoginDate"),
                handler.getBoolean("superuserAccess"), handler.getBoolean("emailChecked"), disabled, handler.getString("name"),
                handler.getString("orderField"), handler.getString("orderType"), handler.getInteger("pageIndex", 1),
                handler.getInteger("pageSize", handler.getInteger("count", 30)));
        @SuppressWarnings("unchecked")
        List<SysUser> list = (List<SysUser>) page.getList();
        if (null != list) {
            boolean absoluteURL = handler.getBoolean("absoluteURL", true);
            list.forEach(e -> {
                if (absoluteURL) {
                    e.setCover(TemplateComponent.getUrl(site.getSitePath(), e.getCover()));
                }
            });
        }
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

    @Resource
    private SysUserService service;

}

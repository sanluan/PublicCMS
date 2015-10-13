package com.publiccms.views.directive.system;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.publiccms.logic.service.system.SystemUserService;
import com.sanluan.common.base.BaseTemplateDirective;
import com.sanluan.common.handler.PageHandler;
import com.sanluan.common.handler.RenderHandler;

@Component
public class SystemUserListDirective extends BaseTemplateDirective {

    @Override
    public void execute(RenderHandler handler) throws IOException, Exception {
        PageHandler page = service.getPage(handler.getDate("startDateRegistered"), handler.getDate("endDateRegistered"),
                handler.getDate("startLastLoginDate"), handler.getDate("endLastLoginDate"),
                handler.getBoolean("superuserAccess"), handler.getBoolean("emailChecked"), handler.getInteger("deptId"),
                handler.getString("name"), handler.getBoolean("disabled", false), handler.getString("orderField"),
                handler.getString("orderType"), handler.getInteger("pageIndex", 1), handler.getInteger("count", 30));
        handler.put("page", page).render();
    }

    @Autowired
    private SystemUserService service;

}

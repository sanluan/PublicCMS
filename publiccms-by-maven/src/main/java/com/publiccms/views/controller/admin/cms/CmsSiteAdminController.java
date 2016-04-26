package com.publiccms.views.controller.admin.cms;

// Generated 2016-1-27 21:06:08 by com.sanluan.common.source.SourceMaker

import static com.sanluan.common.tools.RequestUtils.getIpAddress;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.publiccms.common.base.AbstractController;
import com.publiccms.entities.log.LogOperate;
import com.publiccms.entities.sys.SysSite;
import com.publiccms.logic.service.log.LogLoginService;
import com.publiccms.logic.service.sys.SysSiteService;

@Controller
@RequestMapping("cmsSite")
public class CmsSiteAdminController extends AbstractController {
    @Autowired
    private SysSiteService service;

    @RequestMapping("save")
    public String save(SysSite entity, HttpServletRequest request, HttpSession session) {
        SysSite site = getSite(request);
        entity = service.update(site.getId(), entity, new String[] { "id", "useStatic", "useSsi", "disabled" });
        logOperateService.save(new LogOperate(site.getId(), getAdminFromSession(session).getId(),
                LogLoginService.CHANNEL_WEB_MANAGER, "update.site", getIpAddress(request), getDate(), entity.getId() + ":"
                        + entity.getName()));
        siteComponent.clear();
        return TEMPLATE_DONE;
    }
}
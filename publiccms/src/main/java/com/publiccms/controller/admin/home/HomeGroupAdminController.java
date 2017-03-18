package com.publiccms.controller.admin.home;

// Generated 2016-11-19 11:25:19 by com.sanluan.common.source.SourceGenerator

import static com.sanluan.common.tools.RequestUtils.getIpAddress;
import static com.sanluan.common.tools.JsonUtils.getString;
import static org.apache.commons.lang3.StringUtils.join;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.publiccms.common.base.AbstractController;
import com.publiccms.entities.home.HomeGroup;
import com.publiccms.entities.log.LogOperate;
import com.publiccms.entities.sys.SysSite;
import com.publiccms.logic.service.home.HomeGroupService;
import com.publiccms.logic.service.log.LogLoginService;

@Controller
@RequestMapping("homeGroup")
public class HomeGroupAdminController extends AbstractController {

    private String[] ignoreProperties = new String[] { "id" };

    @RequestMapping("save")
    public String save(HomeGroup entity, HttpServletRequest request, HttpSession session) {
        SysSite site = getSite(request);
        if (null != entity.getId()) {
            entity = service.update(entity.getId(), entity, ignoreProperties);
            logOperateService
                    .save(new LogOperate(site.getId(), getAdminFromSession(session).getId(), LogLoginService.CHANNEL_WEB_MANAGER,
                            "update.homeGroup", getIpAddress(request), getDate(), getString(entity)));
        } else {
            service.save(entity);
            logOperateService
                    .save(new LogOperate(site.getId(), getAdminFromSession(session).getId(), LogLoginService.CHANNEL_WEB_MANAGER,
                            "save.homeGroup", getIpAddress(request), getDate(), getString(entity)));
        }
        return TEMPLATE_DONE;
    }

    @RequestMapping("delete")
    public String delete(Integer[] ids, HttpServletRequest request, HttpSession session) {
        SysSite site = getSite(request);
        if (notEmpty(ids)) {
            service.delete(ids);
            logOperateService.save(new LogOperate(site.getId(), getAdminFromSession(session).getId(),
                    LogLoginService.CHANNEL_WEB_MANAGER, "delete.homeGroup", getIpAddress(request), getDate(), join(ids, ',')));
        }
        return TEMPLATE_DONE;
    }

    @Autowired
    private HomeGroupService service;
}
package com.publiccms.controller.admin.home;

import static com.sanluan.common.tools.JsonUtils.getString;

// Generated 2016-11-19 11:25:19 by com.sanluan.common.source.SourceGenerator

import static com.sanluan.common.tools.RequestUtils.getIpAddress;
import static org.apache.commons.lang3.StringUtils.join;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.publiccms.common.base.AbstractController;
import com.publiccms.entities.home.HomeComment;
import com.publiccms.entities.log.LogOperate;
import com.publiccms.entities.sys.SysSite;
import com.publiccms.logic.service.home.HomeCommentService;
import com.publiccms.logic.service.log.LogLoginService;

@Controller
@RequestMapping("homeComment")
public class HomeCommentAdminController extends AbstractController {

    private String[] ignoreProperties = new String[] { "id" };

    @RequestMapping("save")
    public String save(HomeComment entity, HttpServletRequest request, HttpSession session) {
        SysSite site = getSite(request);
        if (null != entity.getId()) {
            entity = service.update(entity.getId(), entity, ignoreProperties);
            logOperateService.save(new LogOperate(site.getId(), getAdminFromSession(session).getId(),
                    LogLoginService.CHANNEL_WEB_MANAGER, "update.homeComment", getIpAddress(request), getDate(),
                    getString(entity)));
        } else {
            service.save(entity);
            logOperateService.save(new LogOperate(site.getId(), getAdminFromSession(session).getId(),
                    LogLoginService.CHANNEL_WEB_MANAGER, "save.homeComment", getIpAddress(request), getDate(),
                    getString(entity)));
        }
        return TEMPLATE_DONE;
    }

    @RequestMapping("delete")
    public String delete(Integer[] ids, HttpServletRequest request, HttpSession session) {
        SysSite site = getSite(request);
        if (notEmpty(ids)) {
            service.delete(ids);
            logOperateService.save(new LogOperate(site.getId(), getAdminFromSession(session).getId(),
                    LogLoginService.CHANNEL_WEB_MANAGER, "delete.homeComment", getIpAddress(request), getDate(), join(ids, ',')));
        }
        return TEMPLATE_DONE;
    }

    @Autowired
    private HomeCommentService service;
}
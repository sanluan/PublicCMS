package com.publiccms.views.controller.admin.sys;

// Generated 2016-3-1 17:24:12 by com.sanluan.common.source.SourceMaker

import static com.sanluan.common.tools.RequestUtils.getIpAddress;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.publiccms.common.base.AbstractController;
import com.publiccms.entities.log.LogOperate;
import com.publiccms.entities.sys.SysAppClient;
import com.publiccms.entities.sys.SysSite;
import com.publiccms.logic.service.log.LogLoginService;
import com.publiccms.logic.service.sys.SysAppClientService;

@Controller
@RequestMapping("sysAppClient")
public class SysAppClientAdminController extends AbstractController {
    @Autowired
    private SysAppClientService service;

    @RequestMapping(value = "enable", method = RequestMethod.POST)
    public String enable(Long id, HttpServletRequest request, HttpSession session, ModelMap model) {
        SysAppClient entity = service.getEntity(id);
        if (notEmpty(entity.getId())) {
            SysSite site = getSite(request);
            if (verifyNotEquals("siteId", site.getId(), entity.getSiteId(), model)) {
                return TEMPLATE_ERROR;
            }
            service.updateStatus(id, false);
            logOperateService.save(new LogOperate(site.getId(), getAdminFromSession(session).getId(),
                    LogLoginService.CHANNEL_WEB_MANAGER, "enable.appclient", getIpAddress(request), getDate(), id + ":"
                            + entity.getUuid()));
        }
        return TEMPLATE_DONE;
    }

    @RequestMapping(value = "disable", method = RequestMethod.POST)
    public String disable(Long id, HttpServletRequest request, HttpSession session, ModelMap model) {
        SysAppClient entity = service.getEntity(id);
        if (notEmpty(entity)) {
            SysSite site = getSite(request);
            if (verifyNotEquals("siteId", site.getId(), entity.getSiteId(), model)) {
                return TEMPLATE_ERROR;
            }
            service.updateStatus(id, true);
            logOperateService.save(new LogOperate(site.getId(), getAdminFromSession(session).getId(),
                    LogLoginService.CHANNEL_WEB_MANAGER, "disable.appclient", getIpAddress(request), getDate(), id + ":"
                            + entity.getUuid()));
        }
        return TEMPLATE_DONE;
    }
}
package com.publiccms.views.controller.admin.sys;

// Generated 2016-1-27 21:06:08 by com.sanluan.common.source.SourceMaker

import static com.sanluan.common.tools.RequestUtils.getIpAddress;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.publiccms.common.base.AbstractController;
import com.publiccms.entities.log.LogOperate;
import com.publiccms.entities.sys.SysDomain;
import com.publiccms.entities.sys.SysSite;
import com.publiccms.logic.service.log.LogLoginService;
import com.publiccms.logic.service.sys.SysDomainService;

@Controller
@RequestMapping("sysDomain")
public class SysDomainAdminController extends AbstractController {
    @Autowired
    private SysDomainService service;

    @RequestMapping("save")
    public String save(SysDomain entity, HttpServletRequest request, HttpSession session, ModelMap model) {
        SysSite site = getSite(request);
        if (notEmpty(entity.getId())) {
            if (!entity.getName().equals(service.getEntity(entity.getId()).getName())
                    && verifyHasExist("domain", service.getEntity(entity.getName()), model)) {
                return TEMPLATE_ERROR;
            }
            entity = service.update(entity.getId(), entity, new String[] { "id" });
            if (notEmpty(entity)) {
                logOperateService.save(
                        new LogOperate(site.getId(), getAdminFromSession(session).getId(), LogLoginService.CHANNEL_WEB_MANAGER,
                                "update.domain", getIpAddress(request), getDate(), entity.getId() + ":" + entity.getName()));
            }
        } else {
            if (verifyHasExist("domain", service.getEntity(entity.getName()), model)) {
                return TEMPLATE_ERROR;
            }
            if (0 == entity.getSiteId()) {
                entity.setSiteId(site.getId());
            }
            service.save(entity);
            logOperateService
                    .save(new LogOperate(site.getId(), getAdminFromSession(session).getId(), LogLoginService.CHANNEL_WEB_MANAGER,
                            "save.domain", getIpAddress(request), getDate(), entity.getId() + ":" + entity.getName()));
        }
        siteComponent.clear();
        return TEMPLATE_DONE;
    }

    @RequestMapping("delete")
    public String delete(Integer id, HttpServletRequest request, HttpSession session) {
        SysDomain entity = service.getEntity(id);
        if (notEmpty(entity)) {
            service.delete(id);
            SysSite site = getSite(request);
            logOperateService
                    .save(new LogOperate(site.getId(), getAdminFromSession(session).getId(), LogLoginService.CHANNEL_WEB_MANAGER,
                            "delete.domain", getIpAddress(request), getDate(), entity.getId() + ":" + entity.getName()));
        }
        siteComponent.clear();
        return TEMPLATE_DONE;
    }
}
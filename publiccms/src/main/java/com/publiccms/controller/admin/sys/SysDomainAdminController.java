package com.publiccms.controller.admin.sys;

import static com.sanluan.common.tools.JsonUtils.getString;

// Generated 2016-1-27 21:06:08 by com.sanluan.common.source.SourceGenerator

import static com.sanluan.common.tools.RequestUtils.getIpAddress;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

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
    public String save(SysDomain entity, String id, HttpServletRequest request, HttpSession session, ModelMap model) {
        SysSite site = getSite(request);
        if (verifyCustom("noright", !siteComponent.isMaster(site.getId()), model)) {
            return TEMPLATE_ERROR;
        }
        if (notEmpty(id)) {
            if (!entity.getName().equals(id) && verifyHasExist("domain", service.getEntity(entity.getName()), model)) {
                return TEMPLATE_ERROR;
            }
            entity = service.update(id, entity);
            if (null != entity) {
                logOperateService.save(
                        new LogOperate(site.getId(), getAdminFromSession(session).getId(), LogLoginService.CHANNEL_WEB_MANAGER,
                                "update.domain", getIpAddress(request), getDate(), getString(entity)));
            }
        } else {
            if (verifyHasExist("domain", service.getEntity(entity.getName()), model)) {
                return TEMPLATE_ERROR;
            }
            if (0 == entity.getSiteId()) {
                entity.setSiteId(site.getId());
            }
            service.save(entity);
            logOperateService.save(new LogOperate(site.getId(), getAdminFromSession(session).getId(),
                    LogLoginService.CHANNEL_WEB_MANAGER, "save.domain", getIpAddress(request), getDate(), getString(entity)));
        }
        siteComponent.clear();
        return TEMPLATE_DONE;
    }

    @RequestMapping("virify")
    @ResponseBody
    public boolean virify(String name, String domainName, String id, ModelMap model) {
        if (notEmpty(name)) {
            if (notEmpty(id) && !name.equals(service.getEntity(id).getName())
                    && verifyHasExist("domain", service.getEntity(name), model)
                    || empty(id) && verifyHasExist("domain", service.getEntity(name), model)) {
                return false;
            }
        }
        if (notEmpty(domainName)) {
            if (notEmpty(id) && !domainName.equals(service.getEntity(id).getName())
                    && verifyHasExist("domain", service.getEntity(domainName), model)
                    || empty(id) && verifyHasExist("domain", service.getEntity(domainName), model)) {
                return false;
            }
        }
        return true;
    }

    @RequestMapping("delete")
    public String delete(String id, HttpServletRequest request, HttpSession session, ModelMap model) {
        SysSite site = getSite(request);
        if (verifyCustom("noright", !siteComponent.isMaster(site.getId()), model)) {
            return TEMPLATE_ERROR;
        }
        SysDomain entity = service.getEntity(id);
        if (null != entity) {
            service.delete(id);
            logOperateService.save(new LogOperate(site.getId(), getAdminFromSession(session).getId(),
                    LogLoginService.CHANNEL_WEB_MANAGER, "delete.domain", getIpAddress(request), getDate(), getString(entity)));
        }
        siteComponent.clear();
        return TEMPLATE_DONE;
    }
}
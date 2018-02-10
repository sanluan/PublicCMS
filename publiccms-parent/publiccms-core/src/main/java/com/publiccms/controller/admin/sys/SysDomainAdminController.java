package com.publiccms.controller.admin.sys;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.publiccms.common.base.AbstractController;
import com.publiccms.common.constants.CmsVersion;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.common.tools.ControllerUtils;
import com.publiccms.common.tools.JsonUtils;
import com.publiccms.common.tools.RequestUtils;
import com.publiccms.entities.log.LogOperate;
import com.publiccms.entities.sys.SysDomain;
import com.publiccms.entities.sys.SysSite;
import com.publiccms.logic.service.log.LogLoginService;
import com.publiccms.logic.service.sys.SysDomainService;

/**
 *
 * SysDomainAdminController
 * 
 */
@Controller
@RequestMapping("sysDomain")
public class SysDomainAdminController extends AbstractController {
    @Autowired
    private SysDomainService service;
    private String[] ignoreProperties = new String[] { "siteId", "name", "wild" };

    /**
     * @param entity
     * @param id
     * @param request
     * @param session
     * @param model
     * @return view name
     */
    @RequestMapping("save")
    public String save(SysDomain entity, String id, HttpServletRequest request, HttpSession session, ModelMap model) {
        SysSite site = getSite(request);
        if (ControllerUtils.verifyCustom("noright", !siteComponent.isMaster(site.getId()), model)) {
            return TEMPLATE_ERROR;
        }
        if (ControllerUtils.verifyCustom("needAuthorizationEdition", !CmsVersion.isAuthorizationEdition(), model)
                || ControllerUtils.verifyCustom("unauthorizedDomain", !CmsVersion.verifyDomain(entity.getName()), model)) {
            return TEMPLATE_ERROR;
        }
        if (CommonUtils.notEmpty(id)) {
            if (!entity.getName().equals(id)
                    && ControllerUtils.verifyHasExist("domain", service.getEntity(entity.getName()), model)) {
                return TEMPLATE_ERROR;
            }
            entity = service.update(id, entity);
            if (null != entity) {
                logOperateService.save(new LogOperate(site.getId(), getAdminFromSession(session).getId(),
                        LogLoginService.CHANNEL_WEB_MANAGER, "update.domain", RequestUtils.getIpAddress(request),
                        CommonUtils.getDate(), JsonUtils.getString(entity)));
            }
        } else {
            if (ControllerUtils.verifyHasExist("domain", service.getEntity(entity.getName()), model)) {
                return TEMPLATE_ERROR;
            }
            if (0 == entity.getSiteId()) {
                entity.setSiteId(site.getId());
            }
            service.save(entity);
            logOperateService.save(new LogOperate(site.getId(), getAdminFromSession(session).getId(),
                    LogLoginService.CHANNEL_WEB_MANAGER, "save.domain", RequestUtils.getIpAddress(request), CommonUtils.getDate(),
                    JsonUtils.getString(entity)));
        }
        siteComponent.clear();
        return TEMPLATE_DONEANDREFRESH;
    }

    /**
     * @param entity
     * @param request
     * @param session
     * @param model
     * @return view name
     */
    @RequestMapping("saveConfig")
    public String saveConfig(SysDomain entity, HttpServletRequest request, HttpSession session, ModelMap model) {
        if (CommonUtils.notEmpty(entity.getName())) {
            SysSite site = getSite(request);
            SysDomain oldEntity = service.getEntity(entity.getName());
            if (null == oldEntity || ControllerUtils.verifyNotEquals("siteId", site.getId(), oldEntity.getSiteId(), model)) {
                return TEMPLATE_ERROR;
            }
            entity = service.update(entity.getName(), entity, ignoreProperties);
            if (null != entity) {
                logOperateService.save(new LogOperate(site.getId(), getAdminFromSession(session).getId(),
                        LogLoginService.CHANNEL_WEB_MANAGER, "update.domain", RequestUtils.getIpAddress(request),
                        CommonUtils.getDate(), JsonUtils.getString(entity)));
            }
            siteComponent.clear();
        }
        return TEMPLATE_DONE;
    }

    /**
     * @param name
     * @param domainName
     * @param id
     * @param model
     * @return view name
     */
    @RequestMapping("virify")
    @ResponseBody
    public boolean virify(String name, String domainName, String id, ModelMap model) {
        if (CommonUtils.notEmpty(name)) {
            if (CommonUtils.notEmpty(id) && !name.equals(service.getEntity(id).getName())
                    && ControllerUtils.verifyHasExist("domain", service.getEntity(name), model)
                    || CommonUtils.empty(id) && ControllerUtils.verifyHasExist("domain", service.getEntity(name), model)) {
                return false;
            }
        }
        if (CommonUtils.notEmpty(domainName)) {
            if (CommonUtils.notEmpty(id) && !domainName.equals(service.getEntity(id).getName())
                    && ControllerUtils.verifyHasExist("domain", service.getEntity(domainName), model)
                    || CommonUtils.empty(id) && ControllerUtils.verifyHasExist("domain", service.getEntity(domainName), model)) {
                return false;
            }
        }
        return true;
    }

    /**
     * @param id
     * @param request
     * @param session
     * @param model
     * @return view name
     */
    @RequestMapping("delete")
    public String delete(String id, HttpServletRequest request, HttpSession session, ModelMap model) {
        SysSite site = getSite(request);
        if (ControllerUtils.verifyCustom("noright", !siteComponent.isMaster(site.getId()), model)) {
            return TEMPLATE_ERROR;
        }
        SysDomain entity = service.getEntity(id);
        if (null != entity) {
            service.delete(id);
            logOperateService.save(new LogOperate(site.getId(), getAdminFromSession(session).getId(),
                    LogLoginService.CHANNEL_WEB_MANAGER, "delete.domain", RequestUtils.getIpAddress(request),
                    CommonUtils.getDate(), JsonUtils.getString(entity)));
        }
        siteComponent.clear();
        return TEMPLATE_DONE;
    }
}
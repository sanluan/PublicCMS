package org.publiccms.controller.admin.sys;

import static com.publiccms.common.tools.CommonUtils.empty;
import static com.publiccms.common.tools.CommonUtils.getDate;
import static com.publiccms.common.tools.CommonUtils.notEmpty;
import static com.publiccms.common.tools.ControllerUtils.verifyCustom;
import static com.publiccms.common.tools.ControllerUtils.verifyHasExist;
import static com.publiccms.common.tools.ControllerUtils.verifyNotEquals;
import static com.publiccms.common.tools.JsonUtils.getString;
import static com.publiccms.common.tools.RequestUtils.getIpAddress;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.publiccms.common.base.AbstractController;
import org.publiccms.entities.log.LogOperate;
import org.publiccms.entities.sys.SysDomain;
import org.publiccms.entities.sys.SysSite;
import org.publiccms.logic.service.log.LogLoginService;
import org.publiccms.logic.service.sys.SysDomainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

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
     * @return
     */
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

    /**
     * @param entity
     * @param request
     * @param session
     * @param model
     * @return
     */
    @RequestMapping("saveConfig")
    public String saveConfig(SysDomain entity, HttpServletRequest request, HttpSession session, ModelMap model) {
        if (notEmpty(entity.getName())) {
            SysSite site = getSite(request);
            SysDomain oldEntity = service.getEntity(entity.getName());
            if (null == oldEntity || verifyNotEquals("siteId", site.getId(), oldEntity.getSiteId(), model)) {
                return TEMPLATE_ERROR;
            }
            entity = service.update(entity.getName(), entity, ignoreProperties);
            if (null != entity) {
                logOperateService.save(
                        new LogOperate(site.getId(), getAdminFromSession(session).getId(), LogLoginService.CHANNEL_WEB_MANAGER,
                                "update.domain", getIpAddress(request), getDate(), getString(entity)));
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
     * @return
     */
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

    /**
     * @param id
     * @param request
     * @param session
     * @param model
     * @return
     */
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
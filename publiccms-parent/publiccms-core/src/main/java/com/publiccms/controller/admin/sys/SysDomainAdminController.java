package com.publiccms.controller.admin.sys;

import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttribute;

import com.publiccms.common.annotation.Csrf;
import com.publiccms.common.constants.CmsVersion;
import com.publiccms.common.constants.CommonConstants;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.common.tools.ControllerUtils;
import com.publiccms.common.tools.JsonUtils;
import com.publiccms.common.tools.RequestUtils;
import com.publiccms.entities.log.LogOperate;
import com.publiccms.entities.sys.SysDomain;
import com.publiccms.entities.sys.SysSite;
import com.publiccms.entities.sys.SysUser;
import com.publiccms.logic.component.site.SiteComponent;
import com.publiccms.logic.service.log.LogLoginService;
import com.publiccms.logic.service.log.LogOperateService;
import com.publiccms.logic.service.sys.SysDomainService;

/**
 *
 * SysDomainAdminController
 * 
 */
@Controller
@RequestMapping("sysDomain")
public class SysDomainAdminController {
    @Autowired
    private SysDomainService service;
    @Autowired
    protected LogOperateService logOperateService;
    @Autowired
    protected SiteComponent siteComponent;

    private String[] ignoreProperties = new String[] { "siteId", "name", "wild" };

    /**
     * @param site
     * @param admin
     * @param entity
     * @param oldName
     * @param request
     * @param model
     * @return view name
     */
    @RequestMapping("save")
    @Csrf
    public String save(@RequestAttribute SysSite site, @SessionAttribute SysUser admin, SysDomain entity, String oldName,
            HttpServletRequest request, ModelMap model) {
        if (ControllerUtils.verifyCustom("noright", !siteComponent.isMaster(site.getId()), model)
                || ControllerUtils.verifyCustom("needAuthorizationEdition", !CmsVersion.isAuthorizationEdition(), model)
                || ControllerUtils.verifyCustom("unauthorizedDomain", !CmsVersion.verifyDomain(entity.getName()), model)) {
            return CommonConstants.TEMPLATE_ERROR;
        }
        if (CommonUtils.notEmpty(oldName)) {
            if (!entity.getName().equals(oldName)
                    && ControllerUtils.verifyHasExist("domain", service.getEntity(entity.getName()), model)) {
                return CommonConstants.TEMPLATE_ERROR;
            }
            entity = service.update(oldName, entity);
            if (null != entity) {
                logOperateService.save(new LogOperate(site.getId(), admin.getId(), LogLoginService.CHANNEL_WEB_MANAGER,
                        "update.domain", RequestUtils.getIpAddress(request), CommonUtils.getDate(), JsonUtils.getString(entity)));
            }
        } else {
            if (ControllerUtils.verifyHasExist("domain", service.getEntity(entity.getName()), model)) {
                return CommonConstants.TEMPLATE_ERROR;
            }
            if (0 == entity.getSiteId()) {
                entity.setSiteId(site.getId());
            }
            service.save(entity);
            logOperateService.save(new LogOperate(site.getId(), admin.getId(), LogLoginService.CHANNEL_WEB_MANAGER, "save.domain",
                    RequestUtils.getIpAddress(request), CommonUtils.getDate(), JsonUtils.getString(entity)));
        }
        siteComponent.clear();
        if (!siteComponent.getSite(request.getServerName()).getId().equals(site.getId())) {
            return CommonConstants.TEMPLATE_DONEANDREFRESH;
        } else {
            return CommonConstants.TEMPLATE_DONE;
        }
    }

    /**
     * @param site
     * @param admin
     * @param entity
     * @param request
     * @param model
     * @return view name
     */
    @RequestMapping("saveConfig")
    @Csrf
    public String saveConfig(@RequestAttribute SysSite site, @SessionAttribute SysUser admin, SysDomain entity,
            HttpServletRequest request, ModelMap model) {
        if (CommonUtils.notEmpty(entity.getName())) {
            SysDomain oldEntity = service.getEntity(entity.getName());
            if (null == oldEntity || ControllerUtils.verifyNotEquals("siteId", site.getId(), oldEntity.getSiteId(), model)) {
                return CommonConstants.TEMPLATE_ERROR;
            }
            entity = service.update(entity.getName(), entity, ignoreProperties);
            if (null != entity) {
                logOperateService.save(new LogOperate(site.getId(), admin.getId(), LogLoginService.CHANNEL_WEB_MANAGER,
                        "update.domain", RequestUtils.getIpAddress(request), CommonUtils.getDate(), JsonUtils.getString(entity)));
            }
            siteComponent.clear();
        }
        return CommonConstants.TEMPLATE_DONE;
    }

    /**
     * @param name
     * @param domain
     * @param oldName
     * @return view name
     */
    @RequestMapping("virify")
    @ResponseBody
    @Csrf
    public boolean virify(String name, String domain, String oldName) {
        if (CommonUtils.notEmpty(name)) {
            if (CommonUtils.notEmpty(oldName) && !name.equals(oldName) && null != service.getEntity(name)
                    || CommonUtils.empty(oldName) && null != service.getEntity(name)) {
                return false;
            }
        }
        if (CommonUtils.notEmpty(domain) && null != service.getEntity(domain)) {
            return false;
        }
        return true;
    }

    /**
     * @param site
     * @param admin
     * @param id
     * @param request
     * @param model
     * @return view name
     */
    @RequestMapping("delete")
    public String delete(@RequestAttribute SysSite site, @SessionAttribute SysUser admin, String id, HttpServletRequest request,
            ModelMap model) {
        if (ControllerUtils.verifyCustom("noright", !siteComponent.isMaster(site.getId()), model)) {
            return CommonConstants.TEMPLATE_ERROR;
        }
        SysDomain entity = service.getEntity(id);
        if (null != entity) {
            service.delete(id);
            logOperateService.save(new LogOperate(site.getId(), admin.getId(), LogLoginService.CHANNEL_WEB_MANAGER,
                    "delete.domain", RequestUtils.getIpAddress(request), CommonUtils.getDate(), JsonUtils.getString(entity)));
        }
        siteComponent.clear();
        return CommonConstants.TEMPLATE_DONE;
    }
}
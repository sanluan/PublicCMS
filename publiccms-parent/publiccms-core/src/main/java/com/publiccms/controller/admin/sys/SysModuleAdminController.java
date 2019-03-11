package com.publiccms.controller.admin.sys;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttribute;

import com.publiccms.common.annotation.Csrf;
import com.publiccms.common.constants.CommonConstants;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.common.tools.ControllerUtils;
import com.publiccms.common.tools.JsonUtils;
import com.publiccms.common.tools.RequestUtils;
import com.publiccms.entities.log.LogOperate;
import com.publiccms.entities.sys.SysModule;
import com.publiccms.entities.sys.SysRole;
import com.publiccms.entities.sys.SysRoleModule;
import com.publiccms.entities.sys.SysSite;
import com.publiccms.entities.sys.SysUser;
import com.publiccms.logic.component.site.MenuMessageComponent;
import com.publiccms.logic.component.site.SiteComponent;
import com.publiccms.logic.service.log.LogLoginService;
import com.publiccms.logic.service.log.LogOperateService;
import com.publiccms.logic.service.sys.SysModuleLangService;
import com.publiccms.logic.service.sys.SysModuleService;
import com.publiccms.logic.service.sys.SysRoleAuthorizedService;
import com.publiccms.logic.service.sys.SysRoleModuleService;
import com.publiccms.logic.service.sys.SysRoleService;
import com.publiccms.views.pojo.model.SysModuleParameters;

/**
 *
 * SysModuleAdminController
 * 
 */
@Controller
@RequestMapping("sysModule")
public class SysModuleAdminController {
    @Autowired
    private SysModuleService service;
    @Autowired
    private SysModuleLangService sysModuleLangService;
    @Autowired
    private SysRoleService roleService;
    @Autowired
    private SysModuleService moduleService;
    @Autowired
    private SysRoleModuleService roleModuleService;
    @Autowired
    private SysRoleAuthorizedService roleAuthorizedService;
    @Autowired
    private MenuMessageComponent menuMessageComponent;
    @Autowired
    protected LogOperateService logOperateService;
    @Autowired
    protected SiteComponent siteComponent;

    /**
     * @param site
     * @param admin
     * @param entity
     * @param moduleParameters
     * @param oldId
     * @param request
     * @param session
     * @param model
     * @return view name
     */
    @RequestMapping("save")
    @Csrf
    public String save(@RequestAttribute SysSite site, @SessionAttribute SysUser admin, SysModule entity,
            @ModelAttribute SysModuleParameters moduleParameters, String oldId, HttpServletRequest request, HttpSession session,
            ModelMap model) {
        if (ControllerUtils.verifyCustom("noright", !siteComponent.isMaster(site.getId()), model)) {
            return CommonConstants.TEMPLATE_ERROR;
        }
        if (CommonUtils.notEmpty(oldId)) {
            if (!entity.getId().equals(oldId)
                    && ControllerUtils.verifyHasExist("module", service.getEntity(entity.getId()), model)) {
                return CommonConstants.TEMPLATE_ERROR;
            }
            entity = service.update(oldId, entity);
            if (!entity.getId().equals(oldId)) {
                service.updateParentId(oldId, entity.getId());
            }
            if (null != entity) {
                @SuppressWarnings("unchecked")
                List<SysRoleModule> roleModuleList = (List<SysRoleModule>) roleModuleService
                        .getPage(null, entity.getId(), null, null).getList();
                dealRoleAuthorized(roleModuleList);
                sysModuleLangService.save(oldId, entity.getId(), moduleParameters.getLangList());
                logOperateService.save(new LogOperate(site.getId(), admin.getId(), LogLoginService.CHANNEL_WEB_MANAGER,
                        "update.module", RequestUtils.getIpAddress(request), CommonUtils.getDate(), JsonUtils.getString(entity)));
            }
        } else {
            service.save(entity);
            sysModuleLangService.save(null, entity.getId(), moduleParameters.getLangList());
            logOperateService.save(new LogOperate(site.getId(), admin.getId(), LogLoginService.CHANNEL_WEB_MANAGER, "save.module",
                    RequestUtils.getIpAddress(request), CommonUtils.getDate(), JsonUtils.getString(entity)));
        }
        menuMessageComponent.clear();
        return CommonConstants.TEMPLATE_DONE;
    }

    /**
     * @param id
     * @param oldId
     * @return view name
     */
    @RequestMapping("virify")
    @ResponseBody
    @Csrf
    public boolean virify(String id, String oldId) {
        if (CommonUtils.notEmpty(id)) {
            if (CommonUtils.notEmpty(oldId) && !id.equals(oldId) && null != service.getEntity(id)
                    || CommonUtils.empty(oldId) && null != service.getEntity(id)) {
                return false;
            }
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
    @Csrf
    public String delete(@RequestAttribute SysSite site, @SessionAttribute SysUser admin, String id, HttpServletRequest request,
            ModelMap model) {
        if (ControllerUtils.verifyCustom("noright", !siteComponent.isMaster(site.getId()), model)) {
            return CommonConstants.TEMPLATE_ERROR;
        }
        SysModule entity = service.getEntity(id);
        if (null != entity) {
            service.delete(id);
            service.updateParentId(id, null);
            sysModuleLangService.delete(id);
            @SuppressWarnings("unchecked")
            List<SysRoleModule> roleModuleList = (List<SysRoleModule>) roleModuleService.getPage(null, id, null, null).getList();
            roleModuleService.deleteByModuleId(id);
            dealRoleAuthorized(roleModuleList);
            logOperateService.save(new LogOperate(siteComponent.getSite(request.getServerName()).getId(), admin.getId(),
                    LogLoginService.CHANNEL_WEB_MANAGER, "delete.module", RequestUtils.getIpAddress(request),
                    CommonUtils.getDate(), JsonUtils.getString(entity)));
        }
        menuMessageComponent.clear();
        return CommonConstants.TEMPLATE_DONE;
    }

    @SuppressWarnings("unchecked")
    private void dealRoleAuthorized(List<SysRoleModule> roleModuleList) {
        for (SysRoleModule roleModule : roleModuleList) {
            Set<String> moduleIds = new HashSet<>();
            for (SysRoleModule roleModule2 : (List<SysRoleModule>) roleModuleService
                    .getPage(roleModule.getId().getRoleId(), null, null, null).getList()) {
                moduleIds.add(roleModule2.getId().getModuleId());
            }
            SysRole role = roleService.getEntity(roleModule.getId().getRoleId());
            if (!moduleIds.isEmpty() && null != role && !role.isOwnsAllRight()) {
                roleAuthorizedService.dealRoleModules(roleModule.getId().getRoleId(), role.isShowAllModule(),
                        service.getEntitys(moduleIds.toArray(new String[moduleIds.size()])),
                        role.isShowAllModule() ? moduleService.getPageUrl(null) : null);
            }
        }
    }
}
package com.publiccms.controller.admin.sys;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.publiccms.common.base.AbstractController;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.common.tools.ControllerUtils;
import com.publiccms.common.tools.JsonUtils;
import com.publiccms.common.tools.RequestUtils;
import com.publiccms.entities.log.LogOperate;
import com.publiccms.entities.sys.SysModule;
import com.publiccms.entities.sys.SysRole;
import com.publiccms.entities.sys.SysRoleModule;
import com.publiccms.entities.sys.SysSite;
import com.publiccms.logic.service.log.LogLoginService;
import com.publiccms.logic.service.sys.SysModuleService;
import com.publiccms.logic.service.sys.SysRoleAuthorizedService;
import com.publiccms.logic.service.sys.SysRoleModuleService;
import com.publiccms.logic.service.sys.SysRoleService;

/**
 *
 * SysModuleAdminController
 * 
 */
@Controller
@RequestMapping("sysModule")
public class SysModuleAdminController extends AbstractController {
    @Autowired
    private SysModuleService service;
    @Autowired
    private SysRoleService roleService;
    @Autowired
    private SysModuleService moduleService;
    @Autowired
    private SysRoleModuleService roleModuleService;
    @Autowired
    private SysRoleAuthorizedService roleAuthorizedService;

    private String[] ignoreProperties = new String[] { "id" };

    /**
     * @param entity
     * @param request
     * @param session
     * @param model
     * @return view name
     */
    @RequestMapping("save")
    public String save(SysModule entity, HttpServletRequest request, HttpSession session, ModelMap model) {
        SysSite site = getSite(request);
        if (ControllerUtils.verifyCustom("noright", !siteComponent.isMaster(site.getId()), model)) {
            return TEMPLATE_ERROR;
        }
        if (null != entity.getId()) {
            entity = service.update(entity.getId(), entity, ignoreProperties);
            if (null != entity) {
                @SuppressWarnings("unchecked")
                List<SysRoleModule> roleModuleList = (List<SysRoleModule>) roleModuleService
                        .getPage(null, entity.getId(), null, null).getList();
                dealRoleAuthorized(roleModuleList);
                logOperateService.save(new LogOperate(site.getId(), getAdminFromSession(session).getId(),
                        LogLoginService.CHANNEL_WEB_MANAGER, "update.module", RequestUtils.getIpAddress(request),
                        CommonUtils.getDate(), JsonUtils.getString(entity)));
            }
        } else {
            service.save(entity);
            logOperateService.save(new LogOperate(site.getId(), getAdminFromSession(session).getId(),
                    LogLoginService.CHANNEL_WEB_MANAGER, "save.module", RequestUtils.getIpAddress(request), CommonUtils.getDate(),
                    JsonUtils.getString(entity)));
        }
        return TEMPLATE_DONE;
    }

    /**
     * @param id
     * @param request
     * @param session
     * @param model
     * @return view name
     */
    @SuppressWarnings("unchecked")
    @RequestMapping("delete")
    public String delete(Integer id, HttpServletRequest request, HttpSession session, ModelMap model) {
        SysSite site = getSite(request);
        if (ControllerUtils.verifyCustom("noright", !siteComponent.isMaster(site.getId()), model)) {
            return TEMPLATE_ERROR;
        }
        SysModule entity = service.getEntity(id);
        if (null != entity) {
            service.delete(id);
            List<SysRoleModule> roleModuleList = (List<SysRoleModule>) roleModuleService.getPage(null, id, null, null).getList();
            roleModuleService.deleteByModuleId(id);
            dealRoleAuthorized(roleModuleList);
            logOperateService.save(new LogOperate(getSite(request).getId(), getAdminFromSession(session).getId(),
                    LogLoginService.CHANNEL_WEB_MANAGER, "delete.module", RequestUtils.getIpAddress(request),
                    CommonUtils.getDate(), JsonUtils.getString(entity)));
        }
        return TEMPLATE_DONE;
    }

    @SuppressWarnings("unchecked")
    private void dealRoleAuthorized(List<SysRoleModule> roleModuleList) {
        Set<String> pageUrls = moduleService.getPageUrl(null);
        for (SysRoleModule roleModule : roleModuleList) {
            Set<Integer> moduleIds = new HashSet<Integer>();
            for (SysRoleModule roleModule2 : (List<SysRoleModule>) roleModuleService
                    .getPage(roleModule.getId().getRoleId(), null, null, null).getList()) {
                moduleIds.add(roleModule2.getId().getModuleId());
            }
            SysRole role = roleService.getEntity(roleModule.getId().getRoleId());
            if (!moduleIds.isEmpty() && null != role && !role.isOwnsAllRight()) {
                roleAuthorizedService.dealRoleModules(roleModule.getId().getRoleId(), role.isShowAllModule(),
                        service.getEntitys(moduleIds.toArray(new Integer[moduleIds.size()])), pageUrls);
            }
        }
    }
}
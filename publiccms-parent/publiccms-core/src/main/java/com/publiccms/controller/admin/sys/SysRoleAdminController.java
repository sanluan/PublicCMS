package com.publiccms.controller.admin.sys;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttribute;

import com.publiccms.common.annotation.Csrf;
import com.publiccms.common.constants.CommonConstants;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.common.tools.ControllerUtils;
import com.publiccms.common.tools.JsonUtils;
import com.publiccms.common.tools.RequestUtils;
import com.publiccms.entities.log.LogOperate;
import com.publiccms.entities.sys.SysRole;
import com.publiccms.entities.sys.SysRoleModule;
import com.publiccms.entities.sys.SysRoleModuleId;
import com.publiccms.entities.sys.SysRoleUser;
import com.publiccms.entities.sys.SysSite;
import com.publiccms.entities.sys.SysUser;
import com.publiccms.logic.component.site.SiteComponent;
import com.publiccms.logic.service.log.LogLoginService;
import com.publiccms.logic.service.log.LogOperateService;
import com.publiccms.logic.service.sys.SysModuleService;
import com.publiccms.logic.service.sys.SysRoleAuthorizedService;
import com.publiccms.logic.service.sys.SysRoleModuleService;
import com.publiccms.logic.service.sys.SysRoleService;
import com.publiccms.logic.service.sys.SysRoleUserService;
import com.publiccms.logic.service.sys.SysUserService;

/**
 *
 * SysRoleAdminController
 * 
 */
@Controller
@RequestMapping("sysRole")
public class SysRoleAdminController {
    @Autowired
    private SysRoleService service;
    @Autowired
    private SysRoleUserService roleUserService;
    @Autowired
    private SysRoleModuleService roleModuleService;
    @Autowired
    private SysModuleService moduleService;
    @Autowired
    private SysRoleAuthorizedService roleAuthorizedService;
    @Autowired
    private SysUserService userService;
    @Autowired
    protected LogOperateService logOperateService;
    @Autowired
    protected SiteComponent siteComponent;

    private String[] ignoreProperties = new String[] { "id", "siteId" };

    /**
     * @param site
     * @param admin
     * @param entity
     * @param moduleIds
     * @param request
     * @param model
     * @return view name
     */
    @RequestMapping("save")
    @Csrf
    public String save(@RequestAttribute SysSite site, @SessionAttribute SysUser admin, SysRole entity, String[] moduleIds,
            HttpServletRequest request, ModelMap model) {
        if (entity.isOwnsAllRight()) {
            moduleIds = null;
            entity.setShowAllModule(false);
        }
        if (null != entity.getId()) {
            SysRole oldEntity = service.getEntity(entity.getId());
            if (null == oldEntity || ControllerUtils.verifyNotEquals("siteId", site.getId(), oldEntity.getSiteId(), model)) {
                return CommonConstants.TEMPLATE_ERROR;
            }
            entity = service.update(entity.getId(), entity, ignoreProperties);
            roleModuleService.updateRoleModules(entity.getId(), moduleIds);
            if (null != entity) {
                logOperateService.save(new LogOperate(site.getId(), admin.getId(), LogLoginService.CHANNEL_WEB_MANAGER,
                        "update.role", RequestUtils.getIpAddress(request), CommonUtils.getDate(), JsonUtils.getString(entity)));
            }
        } else {
            entity.setSiteId(site.getId());
            service.save(entity);
            if (CommonUtils.notEmpty(moduleIds)) {
                List<SysRoleModule> list = new ArrayList<>();
                for (String moduleId : moduleIds) {
                    list.add(new SysRoleModule(new SysRoleModuleId(entity.getId(), moduleId)));
                }
                roleModuleService.save(list);
            }
            logOperateService.save(new LogOperate(site.getId(), admin.getId(), LogLoginService.CHANNEL_WEB_MANAGER, "save.role",
                    RequestUtils.getIpAddress(request), CommonUtils.getDate(), JsonUtils.getString(entity)));
        }
        roleAuthorizedService.dealRoleModules(entity.getId(), entity.isShowAllModule(), moduleService.getEntitys(moduleIds),
                entity.isShowAllModule() ? moduleService.getPageUrl(null) : null);
        return CommonConstants.TEMPLATE_DONE;
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
    public String delete(@RequestAttribute SysSite site, @SessionAttribute SysUser admin, Integer id, HttpServletRequest request,
            ModelMap model) {
        SysRole entity = service.getEntity(id);
        if (null != entity) {
            if (ControllerUtils.verifyNotEquals("siteId", site.getId(), entity.getSiteId(), model)) {
                return CommonConstants.TEMPLATE_ERROR;
            }
            service.delete(id);
            @SuppressWarnings("unchecked")
            List<SysRoleUser> roleUserList = (List<SysRoleUser>) roleUserService.getPage(id, null, null, null).getList();
            for (SysRoleUser roleUser : roleUserList) {
                userService.deleteRoleIds(roleUser.getId().getUserId(), id);
            }
            roleUserService.deleteByRoleId(id);
            roleModuleService.deleteByRoleId(id);
            roleAuthorizedService.deleteByRoleId(id);
            logOperateService.save(new LogOperate(site.getId(), admin.getId(), LogLoginService.CHANNEL_WEB_MANAGER, "delete.role",
                    RequestUtils.getIpAddress(request), CommonUtils.getDate(), JsonUtils.getString(entity)));
        }
        return CommonConstants.TEMPLATE_DONE;
    }
}
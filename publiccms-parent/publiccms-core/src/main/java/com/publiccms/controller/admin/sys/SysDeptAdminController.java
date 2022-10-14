package com.publiccms.controller.admin.sys;

import static org.springframework.util.StringUtils.arrayToCommaDelimitedString;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttribute;

import com.publiccms.common.annotation.Csrf;
import com.publiccms.common.constants.CommonConstants;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.common.tools.ControllerUtils;
import com.publiccms.common.tools.JsonUtils;
import com.publiccms.common.tools.RequestUtils;
import com.publiccms.common.tools.UserPasswordUtils;
import com.publiccms.entities.log.LogOperate;
import com.publiccms.entities.sys.SysDept;
import com.publiccms.entities.sys.SysRoleUser;
import com.publiccms.entities.sys.SysRoleUserId;
import com.publiccms.entities.sys.SysSite;
import com.publiccms.entities.sys.SysUser;
import com.publiccms.logic.component.site.SiteComponent;
import com.publiccms.logic.service.log.LogLoginService;
import com.publiccms.logic.service.log.LogOperateService;
import com.publiccms.logic.service.sys.SysDeptItemService;
import com.publiccms.logic.service.sys.SysDeptService;
import com.publiccms.logic.service.sys.SysRoleUserService;
import com.publiccms.logic.service.sys.SysUserService;

/**
 *
 * SysDeptAdminController
 * 
 */
@Controller
@RequestMapping("sysDept")
public class SysDeptAdminController {
    @Autowired
    private SysDeptService service;
    @Autowired
    private SysUserService userService;
    @Autowired
    private SysRoleUserService roleUserService;
    @Autowired
    private SysDeptItemService sysDeptItemService;
    @Autowired
    protected LogOperateService logOperateService;
    @Autowired
    protected SiteComponent siteComponent;

    private String[] ignoreProperties = new String[] { "id", "siteId" };
    private String[] userIgnoreProperties = new String[] { "id", "superuserAccess", "registeredDate", "siteId", "authToken",
            "lastLoginDate", "lastLoginIp", "loginCount", "disabled" };

    /**
     * @param site
     * @param admin
     * @param entity
     * @param categoryIds
     * @param pages
     * @param configs
     * @param request
     * @param model
     * @return view name
     */
    @RequestMapping("save")
    @Csrf
    public String save(@RequestAttribute SysSite site, @SessionAttribute SysUser admin, SysDept entity, String[] categoryIds,
            String[] pages, String[] configs, HttpServletRequest request, ModelMap model) {
        if (null != entity.getId()) {
            SysDept oldEntity = service.getEntity(entity.getId());
            if (null == oldEntity || ControllerUtils.errorNotEquals("siteId", site.getId(), oldEntity.getSiteId(), model)) {
                return CommonConstants.TEMPLATE_ERROR;
            }
            entity = service.update(entity.getId(), entity, ignoreProperties);
            if (null != entity) {
                logOperateService.save(new LogOperate(site.getId(), admin.getId(), admin.getDeptId(),
                        LogLoginService.CHANNEL_WEB_MANAGER, "update.dept", RequestUtils.getIpAddress(request),
                        CommonUtils.getDate(), JsonUtils.getString(entity)));
            }
            sysDeptItemService.update(entity.getId(), SysDeptItemService.ITEM_TYPE_CATEGORY, categoryIds);
            sysDeptItemService.update(entity.getId(), SysDeptItemService.ITEM_TYPE_PAGE, pages);
            sysDeptItemService.update(entity.getId(), SysDeptItemService.ITEM_TYPE_CONFIG, configs);
        } else {
            entity.setSiteId(site.getId());
            service.save(entity);
            logOperateService
                    .save(new LogOperate(site.getId(), admin.getId(), admin.getDeptId(), LogLoginService.CHANNEL_WEB_MANAGER,
                            "save.dept", RequestUtils.getIpAddress(request), CommonUtils.getDate(), JsonUtils.getString(entity)));
            sysDeptItemService.save(entity.getId(), SysDeptItemService.ITEM_TYPE_CATEGORY, categoryIds);
            sysDeptItemService.save(entity.getId(), SysDeptItemService.ITEM_TYPE_PAGE, pages);
            sysDeptItemService.save(entity.getId(), SysDeptItemService.ITEM_TYPE_CONFIG, configs);
        }
        return CommonConstants.TEMPLATE_DONE;
    }

    /**
     * @param site
     * @param admin
     * @param entity
     * @param repassword
     * @param encoding
     * @param roleIds
     * @param request
     * @param model
     * @return view name
     */
    @RequestMapping("saveUser")
    @Csrf
    public String saveUser(@RequestAttribute SysSite site, @SessionAttribute SysUser admin, SysUser entity, String repassword,
            String encoding, Integer[] roleIds, HttpServletRequest request, ModelMap model) {
        entity.setName(StringUtils.trim(entity.getName()));
        entity.setNickname(StringUtils.trim(entity.getNickname()));
        entity.setPassword(StringUtils.trim(entity.getPassword()));
        repassword = StringUtils.trim(repassword);
        SysDept dept = service.getEntity(entity.getDeptId());
        if (ControllerUtils.errorNotEmpty("username", entity.getName(), model)
                || ControllerUtils.errorNotEmpty("deptId", dept, model)
                || ControllerUtils.errorNotEquals("userId", dept.getUserId(), admin.getId(), model)
                || ControllerUtils.errorNotEquals("siteId", site.getId(), dept.getSiteId(), model)
                || ControllerUtils.errorNotEmpty("nickname", entity.getNickname(), model)
                || ControllerUtils.errorNotUserName("username", entity.getName(), model)
                || ControllerUtils.errorNotNickname("nickname", entity.getNickname(), model)) {
            return CommonConstants.TEMPLATE_ERROR;
        }
        entity.setSuperuser(true);
        entity.setRoles(arrayToCommaDelimitedString(roleIds));
        if (null != entity.getId()) {
            SysUser oldEntity = userService.getEntity(entity.getId());
            if (null == oldEntity || ControllerUtils.errorNotEquals("siteId", site.getId(), oldEntity.getSiteId(), model)) {
                return CommonConstants.TEMPLATE_ERROR;
            }
            if (SysUserService.CONTENT_PERMISSIONS_ALL == entity.getContentPermissions()
                    && SysUserService.CONTENT_PERMISSIONS_ALL != admin.getContentPermissions()
                    && entity.getContentPermissions() != oldEntity.getContentPermissions()) {
                entity.setContentPermissions(SysUserService.CONTENT_PERMISSIONS_DEPT);
            }
            SysUser user = userService.getEntity(entity.getId());
            if ((!user.getName().equals(entity.getName()) && ControllerUtils.errorHasExist("username",
                    userService.findByName(site.getId(), entity.getName()), model))) {
                return CommonConstants.TEMPLATE_ERROR;
            }
            if (CommonUtils.notEmpty(entity.getPassword())) {
                if (ControllerUtils.errorNotEquals("repassword", entity.getPassword(), repassword, model)) {
                    return CommonConstants.TEMPLATE_ERROR;
                }
                entity.setPassword(UserPasswordUtils.passwordEncode(entity.getPassword(), UserPasswordUtils.getSalt(), null, encoding));
            } else {
                entity.setPassword(user.getPassword());
                if (CommonUtils.empty(entity.getEmail()) || !entity.getEmail().equals(user.getEmail())) {
                    entity.setEmailChecked(false);
                }
            }
            entity = userService.update(entity.getId(), entity, userIgnoreProperties);
            if (null != entity) {
                roleUserService.dealRoleUsers(entity.getId(), roleIds);
                logOperateService.save(new LogOperate(site.getId(), admin.getId(), admin.getDeptId(),
                        LogLoginService.CHANNEL_WEB_MANAGER, "update.user", RequestUtils.getIpAddress(request),
                        CommonUtils.getDate(), JsonUtils.getString(entity)));
            }
        } else {
            if (ControllerUtils.errorNotEmpty("password", entity.getPassword(), model)
                    || ControllerUtils.errorNotEquals("repassword", entity.getPassword(), repassword, model)
                    || ControllerUtils.errorHasExist("username", userService.findByName(site.getId(), entity.getName()), model)) {
                return CommonConstants.TEMPLATE_ERROR;
            }
            if (SysUserService.CONTENT_PERMISSIONS_ALL != admin.getContentPermissions()
                    && SysUserService.CONTENT_PERMISSIONS_ALL == entity.getContentPermissions()) {
                entity.setContentPermissions(SysUserService.CONTENT_PERMISSIONS_DEPT);
            }
            entity.setDeptId(dept.getId());
            entity.setSiteId(site.getId());
            entity.setPassword(UserPasswordUtils.passwordEncode(entity.getPassword(), UserPasswordUtils.getSalt(), null, encoding));
            userService.save(entity);
            if (CommonUtils.notEmpty(roleIds)) {
                for (Integer roleId : roleIds) {
                    roleUserService.save(new SysRoleUser(new SysRoleUserId(roleId, entity.getId())));
                }
            }
            logOperateService
                    .save(new LogOperate(site.getId(), admin.getId(), admin.getDeptId(), LogLoginService.CHANNEL_WEB_MANAGER,
                            "save.user", RequestUtils.getIpAddress(request), CommonUtils.getDate(), JsonUtils.getString(entity)));
        }
        return CommonConstants.TEMPLATE_DONE;
    }

    /**
     * @param site
     * @param code
     * @param oldCode
     * @return view name
     */
    @RequestMapping("virify")
    @ResponseBody
    public boolean virify(@RequestAttribute SysSite site, String code, String oldCode) {
        if (CommonUtils.notEmpty(code)) {
            if (CommonUtils.notEmpty(oldCode) && !code.equals(oldCode) && null != service.getEntityByCode(site.getId(), code)
                    || CommonUtils.empty(oldCode) && null != service.getEntityByCode(site.getId(), code)) {
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
     * @return view name
     */
    @RequestMapping("delete")
    @Csrf
    public String delete(@RequestAttribute SysSite site, @SessionAttribute SysUser admin, Integer id,
            HttpServletRequest request) {
        SysDept entity = service.delete(site.getId(), id);
        if (null != entity) {
            sysDeptItemService.delete(entity.getId(), null, null);
            logOperateService.save(new LogOperate(site.getId(), admin.getId(), admin.getDeptId(),
                    LogLoginService.CHANNEL_WEB_MANAGER, "delete.dept", RequestUtils.getIpAddress(request), CommonUtils.getDate(),
                    JsonUtils.getString(entity)));
        }
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
    @RequestMapping(value = "enableUser", method = RequestMethod.POST)
    @Csrf
    public String enable(@RequestAttribute SysSite site, @SessionAttribute SysUser admin, Long id, HttpServletRequest request,
            ModelMap model) {
        if (ControllerUtils.errorEquals("admin.operate", admin.getId(), id, model)) {
            return CommonConstants.TEMPLATE_ERROR;
        }
        SysUser entity = userService.getEntity(id);
        if (null != entity) {
            SysDept dept = service.getEntity(entity.getDeptId());
            if (ControllerUtils.errorNotEquals("siteId", site.getId(), entity.getSiteId(), model)
                    || ControllerUtils.errorNotEmpty("deptId", dept, model)
                    || ControllerUtils.errorNotEquals("userId", dept.getUserId(), admin.getId(), model)) {
                return CommonConstants.TEMPLATE_ERROR;
            }
            userService.updateStatus(id, false);
            logOperateService.save(new LogOperate(site.getId(), admin.getId(), admin.getDeptId(),
                    LogLoginService.CHANNEL_WEB_MANAGER, "enable.user", RequestUtils.getIpAddress(request), CommonUtils.getDate(),
                    JsonUtils.getString(entity)));
        }
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
    @RequestMapping(value = "disableUser", method = RequestMethod.POST)
    @Csrf
    public String disable(@RequestAttribute SysSite site, @SessionAttribute SysUser admin, Long id, HttpServletRequest request,
            ModelMap model) {
        if (ControllerUtils.errorEquals("admin.operate", admin.getId(), id, model)) {
            return CommonConstants.TEMPLATE_ERROR;
        }
        SysUser entity = userService.getEntity(id);
        if (null != entity) {
            SysDept dept = service.getEntity(entity.getDeptId());
            if (ControllerUtils.errorNotEquals("siteId", site.getId(), entity.getSiteId(), model)
                    || ControllerUtils.errorNotEmpty("deptId", dept, model)
                    || ControllerUtils.errorNotEquals("userId", dept.getUserId(), admin.getId(), model)) {
                return CommonConstants.TEMPLATE_ERROR;
            }
            userService.updateStatus(id, true);
            logOperateService.save(new LogOperate(site.getId(), admin.getId(), admin.getDeptId(),
                    LogLoginService.CHANNEL_WEB_MANAGER, "disable.user", RequestUtils.getIpAddress(request),
                    CommonUtils.getDate(), JsonUtils.getString(entity)));
        }
        return CommonConstants.TEMPLATE_DONE;
    }
}
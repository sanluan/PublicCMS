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
import org.springframework.web.bind.annotation.SessionAttribute;

import com.publiccms.common.annotation.Csrf;
import com.publiccms.common.constants.CommonConstants;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.common.tools.ControllerUtils;
import com.publiccms.common.tools.JsonUtils;
import com.publiccms.common.tools.RequestUtils;
import com.publiccms.common.tools.UserPasswordUtils;
import com.publiccms.entities.log.LogOperate;
import com.publiccms.entities.sys.SysRoleUser;
import com.publiccms.entities.sys.SysRoleUserId;
import com.publiccms.entities.sys.SysSite;
import com.publiccms.entities.sys.SysUser;
import com.publiccms.logic.component.site.SiteComponent;
import com.publiccms.logic.service.log.LogLoginService;
import com.publiccms.logic.service.log.LogOperateService;
import com.publiccms.logic.service.sys.SysRoleUserService;
import com.publiccms.logic.service.sys.SysUserService;

/**
 *
 * SysUserAdminController
 * 
 */
@Controller
@RequestMapping("sysUser")
public class SysUserAdminController {
    @Autowired
    private SysUserService service;
    @Autowired
    private SysRoleUserService roleUserService;
    @Autowired
    protected LogOperateService logOperateService;
    @Autowired
    protected SiteComponent siteComponent;

    private String[] ignoreProperties = new String[] { "id", "registeredDate", "siteId", "salt", "password", "lastLoginDate",
            "lastLoginIp", "loginCount", "disabled" };

    /**
     * @param site
     * @param admin
     * @param entity
     * @param repassword
     * @param roleIds
     * @param request
     * @param model
     * @return view name
     */
    @RequestMapping("save")
    @Csrf
    public String save(@RequestAttribute SysSite site, @SessionAttribute SysUser admin, SysUser entity, String repassword,
            Integer[] roleIds, HttpServletRequest request, ModelMap model) {
        entity.setName(StringUtils.trim(entity.getName()));
        entity.setNickName(StringUtils.trim(entity.getNickName()));
        entity.setPassword(StringUtils.trim(entity.getPassword()));
        repassword = StringUtils.trim(repassword);
        if (ControllerUtils.verifyNotEmpty("username", entity.getName(), model)
                || ControllerUtils.verifyNotEmpty("nickname", entity.getNickName(), model)
                || ControllerUtils.verifyNotUserName("username", entity.getName(), model)
                || ControllerUtils.verifyNotNickName("nickname", entity.getNickName(), model)) {
            return CommonConstants.TEMPLATE_ERROR;
        }
        if (entity.isSuperuserAccess()) {
            entity.setRoles(arrayToCommaDelimitedString(roleIds));
        } else {
            roleIds = null;
            entity.setRoles(null);
            entity.setDeptId(null);
            entity.setOwnsAllContent(false);
        }
        if (null != entity.getId()) {
            SysUser oldEntity = service.getEntity(entity.getId());
            if (null == oldEntity || ControllerUtils.verifyNotEquals("siteId", site.getId(), oldEntity.getSiteId(), model)) {
                return CommonConstants.TEMPLATE_ERROR;
            }
            if ((!oldEntity.getName().equals(entity.getName())
                    && ControllerUtils.verifyHasExist("username", service.findByName(site.getId(), entity.getName()), model))
                    || (!oldEntity.getNickName().equals(entity.getNickName()) && ControllerUtils.verifyHasExist("nickname",
                            service.findByNickName(site.getId(), entity.getNickName()), model))) {
                return CommonConstants.TEMPLATE_ERROR;
            }
            if (CommonUtils.notEmpty(entity.getPassword())) {
                if (ControllerUtils.verifyNotEquals("repassword", entity.getPassword(), repassword, model)) {
                    return CommonConstants.TEMPLATE_ERROR;
                }
                String salt = UserPasswordUtils.getSalt();
                service.updatePassword(entity.getId(), UserPasswordUtils.passwordEncode(entity.getPassword(), salt), salt);
            }
            if (CommonUtils.empty(entity.getEmail()) || !entity.getEmail().equals(oldEntity.getEmail())) {
                entity.setEmailChecked(false);
            }
            entity = service.update(entity.getId(), entity, ignoreProperties);
            if (null != entity) {
                roleUserService.dealRoleUsers(entity.getId(), roleIds);
                logOperateService.save(new LogOperate(site.getId(), admin.getId(), LogLoginService.CHANNEL_WEB_MANAGER,
                        "update.user", RequestUtils.getIpAddress(request), CommonUtils.getDate(), JsonUtils.getString(entity)));
            }
        } else {
            if (ControllerUtils.verifyNotEmpty("password", entity.getPassword(), model)
                    || ControllerUtils.verifyNotEquals("repassword", entity.getPassword(), repassword, model)
                    || ControllerUtils.verifyHasExist("username", service.findByName(site.getId(), entity.getName()), model)
                    || ControllerUtils.verifyHasExist("nickname", service.findByNickName(site.getId(), entity.getNickName()),
                            model)) {
                return CommonConstants.TEMPLATE_ERROR;
            }
            entity.setSiteId(site.getId());
            entity.setSalt(UserPasswordUtils.getSalt());
            entity.setPassword(UserPasswordUtils.passwordEncode(entity.getPassword(), entity.getSalt()));
            entity.setWeakPassword(true);
            service.save(entity);
            if (CommonUtils.notEmpty(roleIds)) {
                for (Integer roleId : roleIds) {
                    roleUserService.save(new SysRoleUser(new SysRoleUserId(roleId, entity.getId())));
                }
            }
            logOperateService.save(new LogOperate(site.getId(), admin.getId(), LogLoginService.CHANNEL_WEB_MANAGER, "save.user",
                    RequestUtils.getIpAddress(request), CommonUtils.getDate(), JsonUtils.getString(entity)));
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
    @RequestMapping(value = "enable", method = RequestMethod.POST)
    @Csrf
    public String enable(@RequestAttribute SysSite site, @SessionAttribute SysUser admin, Long id, HttpServletRequest request,
            ModelMap model) {
        if (ControllerUtils.verifyEquals("admin.operate", admin.getId(), id, model)) {
            return CommonConstants.TEMPLATE_ERROR;
        }
        SysUser entity = service.getEntity(id);
        if (null != entity) {
            if (ControllerUtils.verifyNotEquals("siteId", site.getId(), entity.getSiteId(), model)) {
                return CommonConstants.TEMPLATE_ERROR;
            }
            service.updateStatus(id, false);
            logOperateService.save(new LogOperate(site.getId(), admin.getId(), LogLoginService.CHANNEL_WEB_MANAGER, "enable.user",
                    RequestUtils.getIpAddress(request), CommonUtils.getDate(), JsonUtils.getString(entity)));
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
    @RequestMapping(value = "disable", method = RequestMethod.POST)
    @Csrf
    public String disable(@RequestAttribute SysSite site, @SessionAttribute SysUser admin, Long id, HttpServletRequest request,
            ModelMap model) {
        if (ControllerUtils.verifyEquals("admin.operate", admin.getId(), id, model)) {
            return CommonConstants.TEMPLATE_ERROR;
        }
        SysUser entity = service.getEntity(id);
        if (null != entity) {
            if (ControllerUtils.verifyNotEquals("siteId", site.getId(), entity.getSiteId(), model)) {
                return CommonConstants.TEMPLATE_ERROR;
            }
            service.updateStatus(id, true);
            logOperateService.save(new LogOperate(site.getId(), admin.getId(), LogLoginService.CHANNEL_WEB_MANAGER,
                    "disable.user", RequestUtils.getIpAddress(request), CommonUtils.getDate(), JsonUtils.getString(entity)));
        }
        return CommonConstants.TEMPLATE_DONE;
    }
}

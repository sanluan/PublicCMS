package com.publiccms.controller.admin.sys;

import static org.springframework.util.StringUtils.arrayToCommaDelimitedString;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.publiccms.common.base.AbstractController;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.common.tools.ControllerUtils;
import com.publiccms.common.tools.JsonUtils;
import com.publiccms.common.tools.RequestUtils;
import com.publiccms.common.tools.VerificationUtils;
import com.publiccms.entities.log.LogOperate;
import com.publiccms.entities.sys.SysRoleUser;
import com.publiccms.entities.sys.SysRoleUserId;
import com.publiccms.entities.sys.SysSite;
import com.publiccms.entities.sys.SysUser;
import com.publiccms.logic.service.log.LogLoginService;
import com.publiccms.logic.service.sys.SysRoleUserService;
import com.publiccms.logic.service.sys.SysUserService;

/**
 *
 * SysUserAdminController
 * 
 */
@Controller
@RequestMapping("sysUser")
public class SysUserAdminController extends AbstractController {
    @Autowired
    private SysUserService service;
    @Autowired
    private SysRoleUserService roleUserService;

    private String[] ignoreProperties = new String[] { "id", "registeredDate", "siteId", "authToken", "lastLoginDate",
            "lastLoginIp", "loginCount", "disabled" };

    /**
     * @param entity
     * @param repassword
     * @param roleIds
     * @param request
     * @param session
     * @param model
     * @return view name
     */
    @RequestMapping("save")
    public String save(SysUser entity, String repassword, Integer[] roleIds, HttpServletRequest request, HttpSession session,
            ModelMap model) {
        SysSite site = getSite(request);
        entity.setName(StringUtils.trim(entity.getName()));
        entity.setNickName(StringUtils.trim(entity.getNickName()));
        entity.setPassword(StringUtils.trim(entity.getPassword()));
        repassword = StringUtils.trim(repassword);
        if (ControllerUtils.verifyNotEmpty("username", entity.getName(), model)
                || ControllerUtils.verifyNotEmpty("nickname", entity.getNickName(), model)
                || verifyNotUserName("username", entity.getName(), model)
                || verifyNotNickName("nickname", entity.getNickName(), model)) {
            return TEMPLATE_ERROR;
        }
        if (entity.isSuperuserAccess()) {
            entity.setRoles(arrayToCommaDelimitedString(roleIds));
        } else {
            roleIds = null;
            entity.setRoles(null);
            entity.setDeptId(null);
        }
        if (null != entity.getId()) {
            SysUser oldEntity = service.getEntity(entity.getId());
            if (null == oldEntity || ControllerUtils.verifyNotEquals("siteId", site.getId(), oldEntity.getSiteId(), model)) {
                return TEMPLATE_ERROR;
            }
            SysUser user = service.getEntity(entity.getId());
            if ((!user.getName().equals(entity.getName())
                    && ControllerUtils.verifyHasExist("username", service.findByName(site.getId(), entity.getName()), model))
                    || (!user.getNickName().equals(entity.getNickName()) && ControllerUtils.verifyHasExist("nickname",
                            service.findByNickName(site.getId(), entity.getNickName()), model))) {
                return TEMPLATE_ERROR;
            }
            if (CommonUtils.notEmpty(entity.getPassword())) {
                if (ControllerUtils.verifyNotEquals("repassword", entity.getPassword(), repassword, model)) {
                    return TEMPLATE_ERROR;
                }
                entity.setPassword(VerificationUtils.md5Encode(entity.getPassword()));
            } else {
                entity.setPassword(user.getPassword());
                if (CommonUtils.empty(entity.getEmail()) || !entity.getEmail().equals(user.getEmail())) {
                    entity.setEmailChecked(false);
                }
            }
            entity = service.update(entity.getId(), entity, ignoreProperties);
            if (null != entity) {
                roleUserService.dealRoleUsers(entity.getId(), roleIds);
                logOperateService.save(new LogOperate(site.getId(), getAdminFromSession(session).getId(),
                        LogLoginService.CHANNEL_WEB_MANAGER, "update.user", RequestUtils.getIpAddress(request),
                        CommonUtils.getDate(), JsonUtils.getString(entity)));
            }
        } else {
            if (ControllerUtils.verifyNotEmpty("password", entity.getPassword(), model)
                    || ControllerUtils.verifyNotEquals("repassword", entity.getPassword(), repassword, model)
                    || ControllerUtils.verifyHasExist("username", service.findByName(site.getId(), entity.getName()), model)) {
                return TEMPLATE_ERROR;
            }
            entity.setSiteId(site.getId());
            entity.setPassword(VerificationUtils.md5Encode(entity.getPassword()));
            service.save(entity);
            if (CommonUtils.notEmpty(roleIds)) {
                for (Integer roleId : roleIds) {
                    roleUserService.save(new SysRoleUser(new SysRoleUserId(roleId, entity.getId())));
                }
            }
            logOperateService
                    .save(new LogOperate(site.getId(), getAdminFromSession(session).getId(), LogLoginService.CHANNEL_WEB_MANAGER,
                            "save.user", RequestUtils.getIpAddress(request), CommonUtils.getDate(), JsonUtils.getString(entity)));
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
    @RequestMapping(value = "enable", method = RequestMethod.POST)
    public String enable(Long id, HttpServletRequest request, HttpSession session, ModelMap model) {
        if (ControllerUtils.verifyEquals("admin.operate", getAdminFromSession(session).getId(), id, model)) {
            return TEMPLATE_ERROR;
        }
        SysUser entity = service.getEntity(id);
        if (null != entity) {
            SysSite site = getSite(request);
            if (ControllerUtils.verifyNotEquals("siteId", site.getId(), entity.getSiteId(), model)) {
                return TEMPLATE_ERROR;
            }
            service.updateStatus(id, false);
            logOperateService.save(new LogOperate(site.getId(), getAdminFromSession(session).getId(),
                    LogLoginService.CHANNEL_WEB_MANAGER, "enable.user", RequestUtils.getIpAddress(request), CommonUtils.getDate(),
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
    @RequestMapping(value = "disable", method = RequestMethod.POST)
    public String disable(Long id, HttpServletRequest request, HttpSession session, ModelMap model) {
        if (ControllerUtils.verifyEquals("admin.operate", getAdminFromSession(session).getId(), id, model)) {
            return TEMPLATE_ERROR;
        }
        SysUser entity = service.getEntity(id);
        if (null != entity) {
            SysSite site = getSite(request);
            if (ControllerUtils.verifyNotEquals("siteId", site.getId(), entity.getSiteId(), model)) {
                return TEMPLATE_ERROR;
            }
            service.updateStatus(id, true);
            logOperateService.save(new LogOperate(site.getId(), getAdminFromSession(session).getId(),
                    LogLoginService.CHANNEL_WEB_MANAGER, "disable.user", RequestUtils.getIpAddress(request),
                    CommonUtils.getDate(), JsonUtils.getString(entity)));
        }
        return TEMPLATE_DONE;
    }
}

package org.publiccms.controller.admin.sys;

import static com.publiccms.common.tools.CommonUtils.empty;
import static com.publiccms.common.tools.CommonUtils.getDate;
import static com.publiccms.common.tools.CommonUtils.notEmpty;
import static com.publiccms.common.tools.ControllerUtils.verifyEquals;
import static com.publiccms.common.tools.ControllerUtils.verifyHasExist;
import static com.publiccms.common.tools.ControllerUtils.verifyNotEmpty;
import static com.publiccms.common.tools.ControllerUtils.verifyNotEquals;
import static com.publiccms.common.tools.JsonUtils.getString;
import static com.publiccms.common.tools.RequestUtils.getIpAddress;
import static com.publiccms.common.tools.VerificationUtils.encode;
import static org.apache.commons.lang3.StringUtils.trim;
import static org.springframework.util.StringUtils.arrayToCommaDelimitedString;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.publiccms.common.base.AbstractController;
import org.publiccms.entities.log.LogOperate;
import org.publiccms.entities.sys.SysRoleUser;
import org.publiccms.entities.sys.SysRoleUserId;
import org.publiccms.entities.sys.SysSite;
import org.publiccms.entities.sys.SysUser;
import org.publiccms.logic.service.log.LogLoginService;
import org.publiccms.logic.service.sys.SysRoleUserService;
import org.publiccms.logic.service.sys.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

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
     * @return
     */
    @RequestMapping("save")
    public String save(SysUser entity, String repassword, Integer[] roleIds, HttpServletRequest request, HttpSession session,
            ModelMap model) {
        SysSite site = getSite(request);
        entity.setName(trim(entity.getName()));
        entity.setNickName(trim(entity.getNickName()));
        entity.setPassword(trim(entity.getPassword()));
        repassword = trim(repassword);
        if (verifyNotEmpty("username", entity.getName(), model) || verifyNotEmpty("nickname", entity.getNickName(), model)
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
            if (null == oldEntity || verifyNotEquals("siteId", site.getId(), oldEntity.getSiteId(), model)) {
                return TEMPLATE_ERROR;
            }
            SysUser user = service.getEntity(entity.getId());
            if ((!user.getName().equals(entity.getName())
                    && verifyHasExist("username", service.findByName(site.getId(), entity.getName()), model))
                    || (!user.getNickName().equals(entity.getNickName())
                            && verifyHasExist("nickname", service.findByNickName(site.getId(), entity.getNickName()), model))) {
                return TEMPLATE_ERROR;
            }
            if (notEmpty(entity.getPassword())) {
                if (verifyNotEquals("repassword", entity.getPassword(), repassword, model)) {
                    return TEMPLATE_ERROR;
                }
                entity.setPassword(encode(entity.getPassword()));
            } else {
                entity.setPassword(user.getPassword());
                if (empty(entity.getEmail()) || !entity.getEmail().equals(user.getEmail())) {
                    entity.setEmailChecked(false);
                }
            }
            entity = service.update(entity.getId(), entity, ignoreProperties);
            if (null != entity) {
                roleUserService.dealRoleUsers(entity.getId(), roleIds);
                logOperateService.save(new LogOperate(site.getId(), getAdminFromSession(session).getId(),
                        LogLoginService.CHANNEL_WEB_MANAGER, "update.user", getIpAddress(request), getDate(), getString(entity)));
            }
        } else {
            if (verifyNotEmpty("password", entity.getPassword(), model)
                    || verifyNotEquals("repassword", entity.getPassword(), repassword, model)
                    || verifyHasExist("username", service.findByName(site.getId(), entity.getName()), model)) {
                return TEMPLATE_ERROR;
            }
            entity.setSiteId(site.getId());
            entity.setPassword(encode(entity.getPassword()));
            service.save(entity);
            if (notEmpty(roleIds)) {
                for (Integer roleId : roleIds) {
                    roleUserService.save(new SysRoleUser(new SysRoleUserId(roleId, entity.getId())));
                }
            }
            logOperateService.save(new LogOperate(site.getId(), getAdminFromSession(session).getId(),
                    LogLoginService.CHANNEL_WEB_MANAGER, "save.user", getIpAddress(request), getDate(), getString(entity)));
        }
        return TEMPLATE_DONE;
    }

    /**
     * @param id
     * @param request
     * @param session
     * @param model
     * @return
     */
    @RequestMapping(value = "enable", method = RequestMethod.POST)
    public String enable(Long id, HttpServletRequest request, HttpSession session, ModelMap model) {
        if (verifyEquals("admin.operate", getAdminFromSession(session).getId(), id, model)) {
            return TEMPLATE_ERROR;
        }
        SysUser entity = service.getEntity(id);
        if (null != entity) {
            SysSite site = getSite(request);
            if (verifyNotEquals("siteId", site.getId(), entity.getSiteId(), model)) {
                return TEMPLATE_ERROR;
            }
            service.updateStatus(id, false);
            logOperateService.save(new LogOperate(site.getId(), getAdminFromSession(session).getId(),
                    LogLoginService.CHANNEL_WEB_MANAGER, "enable.user", getIpAddress(request), getDate(), getString(entity)));
        }
        return TEMPLATE_DONE;
    }

    /**
     * @param id
     * @param request
     * @param session
     * @param model
     * @return
     */
    @RequestMapping(value = "disable", method = RequestMethod.POST)
    public String disable(Long id, HttpServletRequest request, HttpSession session, ModelMap model) {
        if (verifyEquals("admin.operate", getAdminFromSession(session).getId(), id, model)) {
            return TEMPLATE_ERROR;
        }
        SysUser entity = service.getEntity(id);
        if (null != entity) {
            SysSite site = getSite(request);
            if (verifyNotEquals("siteId", site.getId(), entity.getSiteId(), model)) {
                return TEMPLATE_ERROR;
            }
            service.updateStatus(id, true);
            logOperateService.save(new LogOperate(site.getId(), getAdminFromSession(session).getId(),
                    LogLoginService.CHANNEL_WEB_MANAGER, "disable.user", getIpAddress(request), getDate(), getString(entity)));
        }
        return TEMPLATE_DONE;
    }
}

package com.publiccms.views.controller.admin.sys;

import static com.sanluan.common.tools.RequestUtils.getIpAddress;
import static com.sanluan.common.tools.VerificationUtils.encode;
import static org.springframework.util.StringUtils.arrayToCommaDelimitedString;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.publiccms.common.base.AbstractController;
import com.publiccms.entities.log.LogOperate;
import com.publiccms.entities.sys.SysRoleUser;
import com.publiccms.entities.sys.SysSite;
import com.publiccms.entities.sys.SysUser;
import com.publiccms.logic.service.log.LogLoginService;
import com.publiccms.logic.service.sys.SysRoleUserService;
import com.publiccms.logic.service.sys.SysUserService;

@Controller
@RequestMapping("sysUser")
public class SysUserAdminController extends AbstractController {
    @Autowired
    private SysUserService service;
    @Autowired
    private SysRoleUserService roleUserService;

    @RequestMapping("save")
    public String save(SysUser entity, String repassword, Integer[] roleIds, HttpServletRequest request, HttpSession session,
            ModelMap model) {
        SysSite site = getSite(request);
        if (virifyNotEmpty("username", entity.getName(), model) || virifyNotEmpty("nickname", entity.getNickName(), model)
                || virifyNotUserName("username", entity.getName(), model)
                || virifyNotNickName("nickname", entity.getNickName(), model)) {
            return TEMPLATE_ERROR;
        }
        if (entity.isSuperuserAccess()) {
            entity.setRoles(arrayToCommaDelimitedString(roleIds));
        } else {
            roleIds = null;
            entity.setRoles(null);
            entity.setDeptId(null);
        }
        if (notEmpty(entity.getId())) {
            SysUser oldEntity = service.getEntity(entity.getId());
            if (empty(oldEntity) || virifyNotEquals("siteId", site.getId(), oldEntity.getSiteId(), model)) {
                return TEMPLATE_ERROR;
            }
            SysUser user = service.getEntity(entity.getId());
            if ((!user.getName().equals(entity.getName()) && virifyHasExist("username",
                    service.findByName(site.getId(), entity.getName()), model))
                    || (!user.getNickName().equals(entity.getNickName()) && virifyHasExist("nickname",
                            service.findByNickName(site.getId(), entity.getNickName()), model))) {
                return TEMPLATE_ERROR;
            }
            if (notEmpty(entity.getPassword())) {
                if (virifyNotEquals("repassword", entity.getPassword(), repassword, model)) {
                    return TEMPLATE_ERROR;
                }
                entity.setPassword(encode(entity.getPassword()));
            } else {
                entity.setPassword(user.getPassword());
                if (empty(entity.getEmail()) || !entity.getEmail().equals(user.getEmail())) {
                    entity.setEmailChecked(false);
                }
            }
            entity = service.update(entity.getId(), entity, new String[] { "id", "registeredDate", "siteId", "authToken", "lastLoginDate",
                    "lastLoginIp", "loginCount", "disabled" });
            if (notEmpty(entity)) {
                roleUserService.dealRoleUsers(entity.getId(), roleIds);
                logOperateService.save(new LogOperate(site.getId(), getAdminFromSession(session).getId(),
                        LogLoginService.CHANNEL_WEB_MANAGER, "update.user", getIpAddress(request), getDate(), entity.getId()
                                + ":" + entity.getName()));
            }
        } else {
            if (virifyNotEmpty("password", entity.getPassword(), model)
                    || virifyNotEquals("repassword", entity.getPassword(), repassword, model)
                    || virifyHasExist("username", service.findByName(site.getId(), entity.getName()), model)) {
                return TEMPLATE_ERROR;
            }
            entity.setSiteId(site.getId());
            entity.setPassword(encode(entity.getPassword()));
            service.save(entity);
            if (notEmpty(roleIds)) {
                for (Integer roleId : roleIds) {
                    roleUserService.save(new SysRoleUser(roleId, entity.getId()));
                }
            }
            logOperateService.save(new LogOperate(site.getId(), getAdminFromSession(session).getId(),
                    LogLoginService.CHANNEL_WEB_MANAGER, "save.user", getIpAddress(request), getDate(), entity.getId() + ":"
                            + entity.getName()));
        }
        return TEMPLATE_DONE;
    }

    @RequestMapping(value = "enable", method = RequestMethod.POST)
    public String enable(Integer id, HttpServletRequest request, HttpSession session, ModelMap model) {
        if (virifyEquals("admin.operate", getAdminFromSession(session).getId(), id, model)) {
            return TEMPLATE_ERROR;
        }
        SysUser entity = service.getEntity(id);
        if (notEmpty(entity)) {
            SysSite site = getSite(request);
            if (virifyNotEquals("siteId", site.getId(), entity.getSiteId(), model)) {
                return TEMPLATE_ERROR;
            }
            service.updateStatus(id, false);
            logOperateService.save(new LogOperate(site.getId(), getAdminFromSession(session).getId(),
                    LogLoginService.CHANNEL_WEB_MANAGER, "enable.user", getIpAddress(request), getDate(), id + ":"
                            + entity.getName()));
        }
        return TEMPLATE_DONE;
    }

    @RequestMapping(value = "disable", method = RequestMethod.POST)
    public String disable(Integer id, HttpServletRequest request, HttpSession session, ModelMap model) {
        if (virifyEquals("admin.operate", getAdminFromSession(session).getId(), id, model)) {
            return TEMPLATE_ERROR;
        }
        SysUser entity = service.getEntity(id);
        if (notEmpty(entity)) {
            SysSite site = getSite(request);
            if (virifyNotEquals("siteId", site.getId(), entity.getSiteId(), model)) {
                return TEMPLATE_ERROR;
            }
            service.updateStatus(id, true);
            logOperateService.save(new LogOperate(site.getId(), getAdminFromSession(session).getId(),
                    LogLoginService.CHANNEL_WEB_MANAGER, "disable.user", getIpAddress(request), getDate(), id + ":"
                            + entity.getName()));
        }
        return TEMPLATE_DONE;
    }

    protected boolean virifyEquals(String field, Integer value, Integer value2, ModelMap model) {
        if (notEmpty(value) && value.equals(value2)) {
            model.addAttribute(ERROR, "verify.equals." + field);
            return true;
        }
        return false;
    }

}

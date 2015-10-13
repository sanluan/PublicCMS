package com.publiccms.admin.views.controller.system;

import static org.springframework.util.StringUtils.arrayToCommaDelimitedString;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.publiccms.common.tools.UserUtils;
import com.publiccms.entities.log.LogOperate;
import com.publiccms.entities.system.SystemRoleUser;
import com.publiccms.entities.system.SystemUser;
import com.publiccms.logic.service.log.LogOperateService;
import com.publiccms.logic.service.system.SystemRoleUserService;
import com.publiccms.logic.service.system.SystemUserService;
import com.sanluan.common.base.BaseController;
import com.sanluan.common.tools.RequestUtils;
import com.sanluan.common.tools.VerificationUtils;

@Controller
@RequestMapping("systemUser")
public class SystemUserAdminController extends BaseController {
    @Autowired
    private SystemUserService service;
    @Autowired
    private SystemRoleUserService roleUserService;
    @Autowired
    private LogOperateService logOperateService;

    @RequestMapping(SAVE)
    public String save(SystemUser entity, String repassword, Integer[] roleIds, HttpServletRequest request, HttpSession session,
            ModelMap model) {
        if (virifyNotEmpty("username", entity.getName(), model) || virifyNotEmpty("nickname", entity.getNickName(), model)
                || virifyNotUserName("username", entity.getName(), model)
                || virifyNotNickName("nickname", entity.getNickName(), model)) {
            return TEMPLATE_ERROR;
        }
        if (entity.isSuperuserAccess()) {
            entity.setRoles(arrayToCommaDelimitedString(roleIds));
        } else {
            entity.setRoles(null);
            entity.setDeptId(null);
        }
        if (notEmpty(entity.getId())) {
            SystemUser user = service.getEntity(entity.getId());
            if ((!user.getName().equals(entity.getName()) && virifyHasExist("username", service.findByName(entity.getName()),
                    model))
                    || (!user.getNickName().equals(entity.getNickName()) && virifyHasExist("nickname",
                            service.findByNickName(entity.getNickName()), model))) {
                return TEMPLATE_ERROR;
            }
            if (notEmpty(entity.getPassword())) {
                if (virifyNotEquals("repassword", entity.getPassword(), repassword, model)) {
                    return TEMPLATE_ERROR;
                } else {
                    entity.setPassword(VerificationUtils.encode(entity.getPassword()));
                }
            } else {
                entity.setPassword(user.getPassword());
                if (!notEmpty(entity.getEmail()) || !entity.getEmail().equals(user.getEmail())) {
                    entity.setEmailChecked(false);
                }
            }
            entity = service.update(entity.getId(), entity, new String[] { ID, "dateRegistered", "authToken", "lastLoginDate",
                    "lastLoginIp", "loginCount", "disabled" });
            if (entity.isSuperuserAccess()) {
                roleUserService.dealRoleUsers(entity.getId(), roleIds);
            }
            if (notEmpty(entity)) {
                logOperateService.save(new LogOperate(UserUtils.getAdminFromSession(session).getId(), "update.user", RequestUtils
                        .getIp(request), getDate(), entity.getId() + ":" + entity.getName()));
            }
        } else {
            if (virifyNotEmpty("password", entity.getPassword(), model)
                    || virifyNotEquals("repassword", entity.getPassword(), repassword, model)
                    || virifyHasExist("username", service.findByName(entity.getName()), model)
                    || virifyHasExist("nickname", service.findByNickName(entity.getNickName()), model)) {
                return TEMPLATE_ERROR;
            }
            entity.setPassword(VerificationUtils.encode(entity.getPassword()));
            entity = service.save(entity);
            if (entity.isSuperuserAccess() && null != roleIds) {
                for (Integer roleId : roleIds) {
                    roleUserService.save(new SystemRoleUser(roleId, entity.getId()));
                }
            }
            if (notEmpty(entity)) {
                logOperateService.save(new LogOperate(UserUtils.getAdminFromSession(session).getId(), "save.user", RequestUtils
                        .getIp(request), getDate(), entity.getId() + ":" + entity.getName()));
            }
        }
        return TEMPLATE_DONE;
    }

    @RequestMapping(value = { "enable" }, method = RequestMethod.POST)
    public String enable(Integer id, String repassword, HttpServletRequest request, HttpSession session, ModelMap model) {
        if (virifyEquals("admin.operate", UserUtils.getAdminFromSession(session), id, model)) {
            return TEMPLATE_ERROR;
        }
        SystemUser entity = service.updateStatus(id, false);
        if (notEmpty(entity)) {
            logOperateService.save(new LogOperate(UserUtils.getAdminFromSession(session).getId(), "enable.user", RequestUtils
                    .getIp(request), getDate(), id + ":" + entity.getName()));
        }
        return TEMPLATE_DONE;
    }

    @RequestMapping(value = { "disable" }, method = RequestMethod.POST)
    public String disable(Integer id, String repassword, HttpServletRequest request, HttpSession session, ModelMap model) {
        if (virifyEquals("admin.operate", UserUtils.getAdminFromSession(session), id, model)) {
            return TEMPLATE_ERROR;
        }
        SystemUser entity = service.updateStatus(id, true);
        if (notEmpty(entity)) {
            logOperateService.save(new LogOperate(UserUtils.getAdminFromSession(session).getId(), "disable.user", RequestUtils
                    .getIp(request), getDate(), id + ":" + entity.getName()));
        }
        return TEMPLATE_DONE;
    }

    protected boolean virifyEquals(String field, SystemUser user, Integer value2, ModelMap model) {
        if (notEmpty(user) && user.getId().equals(value2)) {
            model.addAttribute(ERROR, "verify.equals." + field);
            return true;
        }
        return false;
    }

}

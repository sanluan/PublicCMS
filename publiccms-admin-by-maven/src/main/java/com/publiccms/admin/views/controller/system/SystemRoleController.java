package com.publiccms.admin.views.controller.system;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.publiccms.common.tools.UserUtils;
import com.publiccms.entities.log.LogOperate;
import com.publiccms.entities.system.SystemRole;
import com.publiccms.entities.system.SystemRoleUser;
import com.publiccms.logic.service.log.LogOperateService;
import com.publiccms.logic.service.system.SystemMoudleService;
import com.publiccms.logic.service.system.SystemRoleAuthorizedService;
import com.publiccms.logic.service.system.SystemRoleMoudleService;
import com.publiccms.logic.service.system.SystemRoleService;
import com.publiccms.logic.service.system.SystemRoleUserService;
import com.publiccms.logic.service.system.SystemUserService;
import com.sanluan.common.base.BaseController;
import com.sanluan.common.tools.RequestUtils;

@Controller
@RequestMapping("systemRole")
public class SystemRoleController extends BaseController {
    @Autowired
    private SystemRoleService service;
    @Autowired
    private SystemRoleUserService roleUserService;
    @Autowired
    private SystemRoleMoudleService roleMoudleService;
    @Autowired
    private SystemMoudleService moudleService;
    @Autowired
    private SystemRoleAuthorizedService roleAuthorizedService;
    @Autowired
    private SystemUserService userService;
    @Autowired
    private LogOperateService logOperateService;

    @RequestMapping(SAVE)
    public String save(SystemRole entity, Integer[] moudleIds, HttpServletRequest request, HttpSession session) {
        if (notEmpty(entity.getId())) {
            entity = service.update(entity.getId(), entity, new String[] { ID });
            if (notEmpty(entity)) {
                logOperateService.save(new LogOperate(UserUtils.getAdminFromSession(session).getId(), "update.role", RequestUtils
                        .getIp(request), getDate(), entity.getId() + ":" + entity.getName()));
            }
            roleMoudleService.updateRoleMoudles(entity.getId(), moudleIds);
        } else {
            entity = service.save(entity);
            if (notEmpty(entity)) {
                logOperateService.save(new LogOperate(UserUtils.getAdminFromSession(session).getId(), "save.role", RequestUtils
                        .getIp(request), getDate(), entity.getId() + ":" + entity.getName()));
            }
            roleMoudleService.saveRoleMoudles(entity.getId(), moudleIds);
        }
        roleAuthorizedService.dealRoleMoudles(entity.getId(), moudleService.getEntitys(moudleIds));
        return TEMPLATE_DONE;
    }

    @RequestMapping(DELETE)
    public String delete(Integer id, HttpServletRequest request, HttpSession session) {
        SystemRole entity = service.delete(id);
        if (notEmpty(entity)) {
            @SuppressWarnings("unchecked")
            List<SystemRoleUser> roleUserList = (List<SystemRoleUser>) roleUserService.getPage(id, null, null, null).getList();
            for (SystemRoleUser roleUser : roleUserList) {
                userService.deleteRoleIds(roleUser.getUserId(), id);
            }
            roleUserService.deleteByRole(id);
            roleMoudleService.deleteByRole(id);
            logOperateService.save(new LogOperate(UserUtils.getAdminFromSession(session).getId(), "delete.role", RequestUtils
                    .getIp(request), getDate(), id + ":" + entity.getName()));
        }
        return TEMPLATE_DONE;
    }
}
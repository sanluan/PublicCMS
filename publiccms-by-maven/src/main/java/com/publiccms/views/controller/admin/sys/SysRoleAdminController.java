package com.publiccms.views.controller.admin.sys;

import static com.sanluan.common.tools.RequestUtils.getIpAddress;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.publiccms.common.base.AbstractController;
import com.publiccms.entities.log.LogOperate;
import com.publiccms.entities.sys.SysRole;
import com.publiccms.entities.sys.SysRoleUser;
import com.publiccms.entities.sys.SysSite;
import com.publiccms.logic.service.log.LogLoginService;
import com.publiccms.logic.service.sys.SysMoudleService;
import com.publiccms.logic.service.sys.SysRoleAuthorizedService;
import com.publiccms.logic.service.sys.SysRoleMoudleService;
import com.publiccms.logic.service.sys.SysRoleService;
import com.publiccms.logic.service.sys.SysRoleUserService;
import com.publiccms.logic.service.sys.SysUserService;

@Controller
@RequestMapping("sysRole")
public class SysRoleAdminController extends AbstractController {
    @Autowired
    private SysRoleService service;
    @Autowired
    private SysRoleUserService roleUserService;
    @Autowired
    private SysRoleMoudleService roleMoudleService;
    @Autowired
    private SysMoudleService moudleService;
    @Autowired
    private SysRoleAuthorizedService roleAuthorizedService;
    @Autowired
    private SysUserService userService;

    @RequestMapping("save")
    public String save(SysRole entity, Integer[] moudleIds, HttpServletRequest request, HttpSession session, ModelMap model) {
        SysSite site = getSite(request);
        if (entity.isOwnsAllRight()) {
            moudleIds = null;
            entity.setShowAllMoudle(false);
        }
        if (notEmpty(entity.getId())) {
            SysRole oldEntity = service.getEntity(entity.getId());
            if (empty(oldEntity) || verifyNotEquals("siteId", site.getId(), oldEntity.getSiteId(), model)) {
                return TEMPLATE_ERROR;
            }
            entity = service.update(entity.getId(), entity, new String[] { "id", "siteId" });
            if (notEmpty(entity)) {
                logOperateService.save(new LogOperate(site.getId(), getAdminFromSession(session).getId(),
                        LogLoginService.CHANNEL_WEB_MANAGER, "update.role", getIpAddress(request), getDate(), entity.getId()
                                + ":" + entity.getName()));
            }
            roleMoudleService.updateRoleMoudles(entity.getId(), moudleIds);
        } else {
            entity.setSiteId(site.getId());
            service.save(entity);
            logOperateService.save(new LogOperate(site.getId(), getAdminFromSession(session).getId(),
                    LogLoginService.CHANNEL_WEB_MANAGER, "save.role", getIpAddress(request), getDate(), entity.getId() + ":"
                            + entity.getName()));
            roleMoudleService.saveRoleMoudles(entity.getId(), moudleIds);
        }
        roleAuthorizedService.dealRoleMoudles(entity.getId(), entity.isShowAllMoudle(), moudleService.getEntitys(moudleIds),
                moudleService.getPageUrl(null));
        return TEMPLATE_DONE;
    }

    @RequestMapping("delete")
    public String delete(Integer id, HttpServletRequest request, HttpSession session, ModelMap model) {
        SysRole entity = service.getEntity(id);
        SysSite site = getSite(request);
        if (notEmpty(entity)) {
            if (verifyNotEquals("siteId", site.getId(), entity.getSiteId(), model)) {
                return TEMPLATE_ERROR;
            }
            service.delete(id);
            @SuppressWarnings("unchecked")
            List<SysRoleUser> roleUserList = (List<SysRoleUser>) roleUserService.getPage(id, null, null, null).getList();
            for (SysRoleUser roleUser : roleUserList) {
                userService.deleteRoleIds(roleUser.getUserId(), id);
            }
            roleUserService.deleteByRoleId(id);
            roleMoudleService.deleteByRoleId(id);
            roleAuthorizedService.deleteByRoleId(id);
            logOperateService.save(new LogOperate(site.getId(), getAdminFromSession(session).getId(),
                    LogLoginService.CHANNEL_WEB_MANAGER, "delete.role", getIpAddress(request), getDate(), id + ":"
                            + entity.getName()));
        }
        return TEMPLATE_DONE;
    }
}
package org.publiccms.controller.admin.sys;

import static com.publiccms.common.tools.CommonUtils.getDate;
import static com.publiccms.common.tools.CommonUtils.notEmpty;
import static com.publiccms.common.tools.ControllerUtils.verifyNotEquals;
import static com.publiccms.common.tools.JsonUtils.getString;
import static com.publiccms.common.tools.RequestUtils.getIpAddress;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.publiccms.common.base.AbstractController;
import org.publiccms.entities.log.LogOperate;
import org.publiccms.entities.sys.SysRole;
import org.publiccms.entities.sys.SysRoleMoudle;
import org.publiccms.entities.sys.SysRoleMoudleId;
import org.publiccms.entities.sys.SysRoleUser;
import org.publiccms.entities.sys.SysSite;
import org.publiccms.logic.service.log.LogLoginService;
import org.publiccms.logic.service.sys.SysMoudleService;
import org.publiccms.logic.service.sys.SysRoleAuthorizedService;
import org.publiccms.logic.service.sys.SysRoleMoudleService;
import org.publiccms.logic.service.sys.SysRoleService;
import org.publiccms.logic.service.sys.SysRoleUserService;
import org.publiccms.logic.service.sys.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 *
 * SysRoleAdminController
 * 
 */
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

    private String[] ignoreProperties = new String[] { "id", "siteId" };

    /**
     * @param entity
     * @param moudleIds
     * @param request
     * @param session
     * @param model
     * @return
     */
    @RequestMapping("save")
    public String save(SysRole entity, Integer[] moudleIds, HttpServletRequest request, HttpSession session, ModelMap model) {
        SysSite site = getSite(request);
        if (entity.isOwnsAllRight()) {
            moudleIds = null;
            entity.setShowAllMoudle(false);
        }
        if (null != entity.getId()) {
            SysRole oldEntity = service.getEntity(entity.getId());
            if (null == oldEntity || verifyNotEquals("siteId", site.getId(), oldEntity.getSiteId(), model)) {
                return TEMPLATE_ERROR;
            }
            entity = service.update(entity.getId(), entity, ignoreProperties);
            roleMoudleService.updateRoleMoudles(entity.getId(), moudleIds);
            if (null != entity) {
                logOperateService.save(new LogOperate(site.getId(), getAdminFromSession(session).getId(),
                        LogLoginService.CHANNEL_WEB_MANAGER, "update.role", getIpAddress(request), getDate(), getString(entity)));
            }
        } else {
            entity.setSiteId(site.getId());
            service.save(entity);
            if (notEmpty(moudleIds)) {
                List<SysRoleMoudle> list = new ArrayList<>();
                for (int moudleId : moudleIds) {
                    list.add(new SysRoleMoudle(new SysRoleMoudleId(entity.getId(), moudleId)));
                }
                roleMoudleService.save(list);
            }
            logOperateService.save(new LogOperate(site.getId(), getAdminFromSession(session).getId(),
                    LogLoginService.CHANNEL_WEB_MANAGER, "save.role", getIpAddress(request), getDate(), getString(entity)));
        }
        roleAuthorizedService.dealRoleMoudles(entity.getId(), entity.isShowAllMoudle(), moudleService.getEntitys(moudleIds),
                moudleService.getPageUrl(null));
        return TEMPLATE_DONE;
    }

    /**
     * @param id
     * @param request
     * @param session
     * @param model
     * @return
     */
    @RequestMapping("delete")
    public String delete(Integer id, HttpServletRequest request, HttpSession session, ModelMap model) {
        SysRole entity = service.getEntity(id);
        SysSite site = getSite(request);
        if (null != entity) {
            if (verifyNotEquals("siteId", site.getId(), entity.getSiteId(), model)) {
                return TEMPLATE_ERROR;
            }
            service.delete(id);
            @SuppressWarnings("unchecked")
            List<SysRoleUser> roleUserList = (List<SysRoleUser>) roleUserService.getPage(id, null, null, null).getList();
            for (SysRoleUser roleUser : roleUserList) {
                userService.deleteRoleIds(roleUser.getId().getUserId(), id);
            }
            roleUserService.deleteByRoleId(id);
            roleMoudleService.deleteByRoleId(id);
            roleAuthorizedService.deleteByRoleId(id);
            logOperateService
                    .save(new LogOperate(site.getId(), getAdminFromSession(session).getId(), LogLoginService.CHANNEL_WEB_MANAGER,
                            "delete.role", getIpAddress(request), getDate(), getString(entity)));
        }
        return TEMPLATE_DONE;
    }
}
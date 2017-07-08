package org.publiccms.controller.admin.sys;

import static com.publiccms.common.tools.CommonUtils.getDate;
import static com.publiccms.common.tools.ControllerUtils.verifyCustom;
import static com.publiccms.common.tools.JsonUtils.getString;
import static com.publiccms.common.tools.RequestUtils.getIpAddress;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.publiccms.common.base.AbstractController;
import org.publiccms.entities.log.LogOperate;
import org.publiccms.entities.sys.SysMoudle;
import org.publiccms.entities.sys.SysRole;
import org.publiccms.entities.sys.SysRoleMoudle;
import org.publiccms.entities.sys.SysSite;
import org.publiccms.logic.service.log.LogLoginService;
import org.publiccms.logic.service.sys.SysMoudleService;
import org.publiccms.logic.service.sys.SysRoleAuthorizedService;
import org.publiccms.logic.service.sys.SysRoleMoudleService;
import org.publiccms.logic.service.sys.SysRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 *
 * SysMoudleAdminController
 * 
 */
@Controller
@RequestMapping("sysMoudle")
public class SysMoudleAdminController extends AbstractController {
    @Autowired
    private SysMoudleService service;
    @Autowired
    private SysRoleService roleService;
    @Autowired
    private SysMoudleService moudleService;
    @Autowired
    private SysRoleMoudleService roleMoudleService;
    @Autowired
    private SysRoleAuthorizedService roleAuthorizedService;

    private String[] ignoreProperties = new String[] { "id" };

    /**
     * @param entity
     * @param request
     * @param session
     * @param model
     * @return
     */
    @RequestMapping("save")
    public String save(SysMoudle entity, HttpServletRequest request, HttpSession session, ModelMap model) {
        SysSite site = getSite(request);
        if (verifyCustom("noright", !siteComponent.isMaster(site.getId()), model)) {
            return TEMPLATE_ERROR;
        }
        if (null != entity.getId()) {
            entity = service.update(entity.getId(), entity, ignoreProperties);
            if (null != entity) {
                @SuppressWarnings("unchecked")
                List<SysRoleMoudle> roleMoudleList = (List<SysRoleMoudle>) roleMoudleService
                        .getPage(null, entity.getId(), null, null).getList();
                dealRoleAuthorized(roleMoudleList);
                logOperateService.save(
                        new LogOperate(site.getId(), getAdminFromSession(session).getId(), LogLoginService.CHANNEL_WEB_MANAGER,
                                "update.moudle", getIpAddress(request), getDate(), getString(entity)));
            }
        } else {
            service.save(entity);
            logOperateService.save(new LogOperate(site.getId(), getAdminFromSession(session).getId(),
                    LogLoginService.CHANNEL_WEB_MANAGER, "save.moudle", getIpAddress(request), getDate(), getString(entity)));
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
    @SuppressWarnings("unchecked")
    @RequestMapping("delete")
    public String delete(Integer id, HttpServletRequest request, HttpSession session, ModelMap model) {
        SysSite site = getSite(request);
        if (verifyCustom("noright", !siteComponent.isMaster(site.getId()), model)) {
            return TEMPLATE_ERROR;
        }
        SysMoudle entity = service.getEntity(id);
        if (null != entity) {
            service.delete(id);
            List<SysRoleMoudle> roleMoudleList = (List<SysRoleMoudle>) roleMoudleService.getPage(null, id, null, null).getList();
            roleMoudleService.deleteByMoudleId(id);
            dealRoleAuthorized(roleMoudleList);
            logOperateService.save(new LogOperate(getSite(request).getId(), getAdminFromSession(session).getId(),
                    LogLoginService.CHANNEL_WEB_MANAGER, "delete.moudle", getIpAddress(request), getDate(), getString(entity)));
        }
        return TEMPLATE_DONE;
    }

    @SuppressWarnings("unchecked")
    private void dealRoleAuthorized(List<SysRoleMoudle> roleMoudleList) {
        Set<String> pageUrls = moudleService.getPageUrl(null);
        for (SysRoleMoudle roleMoudle : roleMoudleList) {
            Set<Integer> moudleIds = new HashSet<Integer>();
            for (SysRoleMoudle roleMoudle2 : (List<SysRoleMoudle>) roleMoudleService
                    .getPage(roleMoudle.getId().getRoleId(), null, null, null).getList()) {
                moudleIds.add(roleMoudle2.getId().getMoudleId());
            }
            SysRole role = roleService.getEntity(roleMoudle.getId().getRoleId());
            if (!moudleIds.isEmpty() && null != role && !role.isOwnsAllRight()) {
                roleAuthorizedService.dealRoleMoudles(roleMoudle.getId().getRoleId(), role.isShowAllMoudle(),
                        service.getEntitys(moudleIds.toArray(new Integer[moudleIds.size()])), pageUrls);
            }
        }
    }
}
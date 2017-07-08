package org.publiccms.controller.admin.sys;

import static com.publiccms.common.tools.CommonUtils.getDate;
import static com.publiccms.common.tools.ControllerUtils.verifyCustom;
import static com.publiccms.common.tools.ControllerUtils.verifyNotEmpty;
import static com.publiccms.common.tools.JsonUtils.getString;
import static com.publiccms.common.tools.RequestUtils.getIpAddress;
import static com.publiccms.common.tools.VerificationUtils.encode;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.publiccms.common.base.AbstractController;
import org.publiccms.entities.log.LogOperate;
import org.publiccms.entities.sys.SysDept;
import org.publiccms.entities.sys.SysDomain;
import org.publiccms.entities.sys.SysRole;
import org.publiccms.entities.sys.SysRoleUser;
import org.publiccms.entities.sys.SysRoleUserId;
import org.publiccms.entities.sys.SysSite;
import org.publiccms.entities.sys.SysUser;
import org.publiccms.logic.service.cms.CmsContentService;
import org.publiccms.logic.service.log.LogLoginService;
import org.publiccms.logic.service.sys.SysDeptService;
import org.publiccms.logic.service.sys.SysDomainService;
import org.publiccms.logic.service.sys.SysRoleService;
import org.publiccms.logic.service.sys.SysRoleUserService;
import org.publiccms.logic.service.sys.SysSiteService;
import org.publiccms.logic.service.sys.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 *
 * SysSiteAdminController
 * 
 */
@Controller
@RequestMapping("sysSite")
public class SysSiteAdminController extends AbstractController {
    @Autowired
    private SysSiteService service;
    @Autowired
    private SysRoleService roleService;
    @Autowired
    private SysUserService userService;
    @Autowired
    private SysDeptService deptService;
    @Autowired
    private SysRoleUserService roleUserService;
    @Autowired
    private SysDomainService domainService;
    @Autowired
    private CmsContentService contentService;

    private String[] ignoreProperties = new String[] { "id" };

    /**
     * @param entity
     * @param domainName
     * @param roleName
     * @param deptName
     * @param userName
     * @param password
     * @param request
     * @param session
     * @param model
     * @return
     */
    @RequestMapping("save")
    public String save(SysSite entity, String domainName, String roleName, String deptName, String userName, String password,
            HttpServletRequest request, HttpSession session, ModelMap model) {
        SysSite site = getSite(request);
        if (verifyCustom("noright", !siteComponent.isMaster(site.getId()), model)) {
            return TEMPLATE_ERROR;
        }
        if (!entity.isUseStatic()) {
            entity.setUseSsi(false);
        }
        if (null != entity.getId()) {
            entity = service.update(entity.getId(), entity, ignoreProperties);
            if (null != entity) {
                logOperateService.save(new LogOperate(site.getId(), getAdminFromSession(session).getId(),
                        LogLoginService.CHANNEL_WEB_MANAGER, "update.site", getIpAddress(request), getDate(), getString(entity)));
            }
        } else {
            if (verifyNotEmpty("userName", userName, model) || verifyNotEmpty("password", password, model)) {
                return TEMPLATE_ERROR;
            }
            service.save(entity);
            SysDomain domain = new SysDomain(domainName, entity.getId(), false);
            domainService.save(domain);
            SysDept dept = new SysDept(entity.getId(), deptName, 0, true, true);
            deptService.save(dept);// 初始化部门
            SysRole role = new SysRole(entity.getId(), roleName, true, true);
            roleService.save(role);// 初始化角色
            SysUser user = new SysUser(entity.getId(), userName, encode(password), userName, dept.getId(),
                    role.getId().toString(), null, false, true, false, null, null, 0, getDate());
            userService.save(user);// 初始化用户
            roleUserService.save(new SysRoleUser(new SysRoleUserId(role.getId(), user.getId())));// 初始化角色用户映射
            logOperateService.save(new LogOperate(site.getId(), getAdminFromSession(session).getId(),
                    LogLoginService.CHANNEL_WEB_MANAGER, "save.site", getIpAddress(request), getDate(), getString(entity)));
        }
        siteComponent.clear();
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
        SysSite entity = service.getEntity(id);
        if (null != entity) {
            service.delete(id);
            Long userId = getAdminFromSession(session).getId();
            Date now = getDate();
            String ip = getIpAddress(request);
            for (SysDomain domain : (List<SysDomain>) domainService.getPage(entity.getId(), null, null, null).getList()) {
                domainService.delete(domain.getName());
                logOperateService.save(new LogOperate(site.getId(), userId, LogLoginService.CHANNEL_WEB_MANAGER, "delete.domain",
                        ip, now, getString(entity)));
            }
            logOperateService.save(new LogOperate(site.getId(), userId, LogLoginService.CHANNEL_WEB_MANAGER, "delete.site", ip,
                    now, getString(entity)));
        }
        return TEMPLATE_DONE;
    }

    /**
     * @param request
     * @param session
     * @return
     */
    @RequestMapping("reCreateIndex")
    public String reCreateIndex(HttpServletRequest request, HttpSession session) {
        contentService.reCreateIndex();
        SysSite site = getSite(request);
        Long userId = getAdminFromSession(session).getId();
        logOperateService.save(new LogOperate(site.getId(), userId, LogLoginService.CHANNEL_WEB_MANAGER, "delete.site",
                getIpAddress(request), getDate(), BLANK));
        return TEMPLATE_DONE;
    }
}
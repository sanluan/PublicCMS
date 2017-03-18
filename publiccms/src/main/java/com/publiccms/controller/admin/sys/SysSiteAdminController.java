package com.publiccms.controller.admin.sys;

import static com.sanluan.common.tools.JsonUtils.getString;

// Generated 2016-1-27 21:06:08 by com.sanluan.common.source.SourceGenerator

import static com.sanluan.common.tools.RequestUtils.getIpAddress;
import static com.sanluan.common.tools.VerificationUtils.encode;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.publiccms.common.base.AbstractController;
import com.publiccms.entities.log.LogOperate;
import com.publiccms.entities.sys.SysDept;
import com.publiccms.entities.sys.SysDomain;
import com.publiccms.entities.sys.SysRole;
import com.publiccms.entities.sys.SysRoleUser;
import com.publiccms.entities.sys.SysRoleUserId;
import com.publiccms.entities.sys.SysSite;
import com.publiccms.entities.sys.SysUser;
import com.publiccms.logic.service.cms.CmsContentService;
import com.publiccms.logic.service.log.LogLoginService;
import com.publiccms.logic.service.sys.SysDeptService;
import com.publiccms.logic.service.sys.SysDomainService;
import com.publiccms.logic.service.sys.SysRoleService;
import com.publiccms.logic.service.sys.SysRoleUserService;
import com.publiccms.logic.service.sys.SysSiteService;
import com.publiccms.logic.service.sys.SysUserService;

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
            SysDept dept = new SysDept(entity.getId(), deptName, null, null, null, true, true);
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
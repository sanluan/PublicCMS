package com.publiccms.controller.admin.sys;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;

import com.publiccms.common.base.AbstractController;
import com.publiccms.common.constants.CmsVersion;
import com.publiccms.common.constants.CommonConstants;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.common.tools.ControllerUtils;
import com.publiccms.common.tools.JsonUtils;
import com.publiccms.common.tools.RequestUtils;
import com.publiccms.common.tools.VerificationUtils;
import com.publiccms.entities.log.LogOperate;
import com.publiccms.entities.log.LogUpload;
import com.publiccms.entities.sys.SysDept;
import com.publiccms.entities.sys.SysDomain;
import com.publiccms.entities.sys.SysRole;
import com.publiccms.entities.sys.SysRoleUser;
import com.publiccms.entities.sys.SysRoleUserId;
import com.publiccms.entities.sys.SysSite;
import com.publiccms.entities.sys.SysUser;
import com.publiccms.logic.component.file.FileComponent;
import com.publiccms.logic.service.cms.CmsContentService;
import com.publiccms.logic.service.log.LogLoginService;
import com.publiccms.logic.service.log.LogUploadService;
import com.publiccms.logic.service.sys.SysDeptService;
import com.publiccms.logic.service.sys.SysDomainService;
import com.publiccms.logic.service.sys.SysRoleService;
import com.publiccms.logic.service.sys.SysRoleUserService;
import com.publiccms.logic.service.sys.SysSiteService;
import com.publiccms.logic.service.sys.SysUserService;
import com.publiccms.logic.service.tools.SqlService;

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
    @Autowired
    private SqlService sqlService;
    @Autowired
    private FileComponent fileComponent;
    @Autowired
    protected LogUploadService logUploadService;

    private String[] ignoreProperties = new String[] { "id" };

    /**
     * @param entity
     * @param domain
     * @param wild
     * @param roleName
     * @param deptName
     * @param userName
     * @param password
     * @param _csrf
     * @param request
     * @param session
     * @param model
     * @return view name
     */
    @RequestMapping("save")
    public String save(SysSite entity, String domain, Boolean wild, String roleName, String deptName, String userName,
            String password, String _csrf, HttpServletRequest request, HttpSession session, ModelMap model) {
        SysSite site = getSite(request);
        if (ControllerUtils.verifyCustom("noright", !siteComponent.isMaster(site.getId()), model)
                || ControllerUtils.verifyNotEquals("_csrf", ControllerUtils.getAdminToken(request), _csrf, model)) {
            return CommonConstants.TEMPLATE_ERROR;
        }
        if (!entity.isUseStatic()) {
            entity.setUseSsi(false);
        }
        if (null != entity.getId()) {
            entity = service.update(entity.getId(), entity, ignoreProperties);
            if (null != entity) {
                logOperateService.save(new LogOperate(site.getId(), ControllerUtils.getAdminFromSession(session).getId(),
                        LogLoginService.CHANNEL_WEB_MANAGER, "update.site", RequestUtils.getIpAddress(request),
                        CommonUtils.getDate(), JsonUtils.getString(entity)));
            }
        } else {
            if (ControllerUtils.verifyCustom("needAuthorizationEdition", !CmsVersion.isAuthorizationEdition(), model)
                    || ControllerUtils.verifyCustom("unauthorizedDomain", !CmsVersion.verifyDomain(domain), model)
                    || ControllerUtils.verifyNotEmpty("userName", userName, model)
                    || ControllerUtils.verifyNotEmpty("password", password, model)
                    || ControllerUtils.verifyHasExist("domain", domainService.getEntity(domain), model)) {
                return CommonConstants.TEMPLATE_ERROR;
            }
            service.save(entity);
            if (null == wild) {
                wild = false;
            }
            domainService.save(new SysDomain(domain, entity.getId(), wild));
            SysDept dept = new SysDept(entity.getId(), deptName, 0, true, true, true);
            deptService.save(dept);// 初始化部门
            SysRole role = new SysRole(entity.getId(), roleName, true, true);
            roleService.save(role);// 初始化角色
            SysUser user = new SysUser(entity.getId(), userName, VerificationUtils.md5Encode(password), userName, dept.getId(),
                    true, role.getId().toString(), null, false, true, false, null, null, 0, CommonUtils.getDate());
            userService.save(user);// 初始化用户
            roleUserService.save(new SysRoleUser(new SysRoleUserId(role.getId(), user.getId())));// 初始化角色用户映射
            logOperateService.save(new LogOperate(site.getId(), ControllerUtils.getAdminFromSession(session).getId(),
                    LogLoginService.CHANNEL_WEB_MANAGER, "save.site", RequestUtils.getIpAddress(request), CommonUtils.getDate(),
                    JsonUtils.getString(entity)));
        }
        siteComponent.clear();
        if (!getSite(request).getId().equals(site.getId()) || site.getId().equals(entity.getId())
                && (!site.getSitePath().equals(entity.getSitePath()) || !site.getDynamicPath().equals(entity.getDynamicPath()))) {
            return CommonConstants.TEMPLATE_DONEANDREFRESH;
        } else {
            return CommonConstants.TEMPLATE_DONE;
        }
    }

    /**
     * @param id
     * @param _csrf
     * @param request
     * @param session
     * @param model
     * @return view name
     */
    @SuppressWarnings("unchecked")
    @RequestMapping("delete")
    public String delete(Short id, String _csrf, HttpServletRequest request, HttpSession session, ModelMap model) {
        SysSite site = getSite(request);
        if (ControllerUtils.verifyCustom("noright", !siteComponent.isMaster(site.getId()), model)
                || ControllerUtils.verifyNotEquals("_csrf", ControllerUtils.getAdminToken(request), _csrf, model)) {
            return CommonConstants.TEMPLATE_ERROR;
        }
        SysSite entity = service.getEntity(id);
        if (null != entity) {
            service.delete(id);
            Long userId = ControllerUtils.getAdminFromSession(session).getId();
            Date now = CommonUtils.getDate();
            String ip = RequestUtils.getIpAddress(request);
            for (SysDomain domain : (List<SysDomain>) domainService.getPage(entity.getId(), null, null, null).getList()) {
                domainService.delete(domain.getName());
                logOperateService.save(new LogOperate(site.getId(), userId, LogLoginService.CHANNEL_WEB_MANAGER, "delete.domain",
                        ip, now, JsonUtils.getString(entity)));
            }
            logOperateService.save(new LogOperate(site.getId(), userId, LogLoginService.CHANNEL_WEB_MANAGER, "delete.site", ip,
                    now, JsonUtils.getString(entity)));
        }
        return CommonConstants.TEMPLATE_DONE;
    }

    /**
     * @param sql
     * @param _csrf
     * @param request
     * @param session
     * @param model
     * @return view name
     */
    @RequestMapping("execSql")
    public String execSql(String sql, String _csrf, HttpServletRequest request, HttpSession session, ModelMap model) {
        SysSite site = getSite(request);
        if (ControllerUtils.verifyCustom("noright", !siteComponent.isMaster(site.getId()), model)
                || ControllerUtils.verifyNotEquals("_csrf", ControllerUtils.getAdminToken(request), _csrf, model)) {
            return CommonConstants.TEMPLATE_ERROR;
        }
        if (-1 < sql.indexOf(CommonConstants.BLANK_SPACE)) {
            String type = sql.substring(0, sql.indexOf(CommonConstants.BLANK_SPACE));
            try {
                if ("update".equalsIgnoreCase(type)) {
                    model.addAttribute("result", sqlService.update(sql));
                } else if ("insert".equalsIgnoreCase(type)) {
                    model.addAttribute("result", sqlService.insert(sql));
                } else if ("delete".equalsIgnoreCase(type)) {
                    model.addAttribute("result", sqlService.delete(sql));
                } else {
                    model.addAttribute("result", JsonUtils.getString(sqlService.select(sql)));
                }
            } catch (Exception e) {
                model.addAttribute("error", e.getMessage());
            }
            model.addAttribute("sql", sql);
            logOperateService.save(new LogOperate(site.getId(), ControllerUtils.getAdminFromSession(session).getId(),
                    LogLoginService.CHANNEL_WEB_MANAGER, "execsql.site", RequestUtils.getIpAddress(request),
                    CommonUtils.getDate(), JsonUtils.getString(model)));
        }
        return CommonConstants.TEMPLATE_DONE;
    }

    /**
     * @param file
     * @param _csrf
     * @param request
     * @param session
     * @param model
     * @return view name
     */
    @RequestMapping(value = "doUploadLicense", method = RequestMethod.POST)
    public String upload(MultipartFile file, String _csrf, HttpServletRequest request, HttpSession session, ModelMap model) {
        SysSite site = getSite(request);
        if (ControllerUtils.verifyCustom("noright", !siteComponent.isMaster(site.getId()), model)
                || ControllerUtils.verifyNotEquals("_csrf", ControllerUtils.getAdminToken(request), _csrf, model)) {
            return CommonConstants.TEMPLATE_ERROR;
        }
        if (null != file && !file.isEmpty()) {
            try {
                fileComponent.upload(file, siteComponent.getRootPath() + CommonConstants.LICENSE_FILENAME);
                logUploadService.save(new LogUpload(site.getId(), ControllerUtils.getAdminFromSession(session).getId(),
                        LogLoginService.CHANNEL_WEB_MANAGER, "license.dat", LogUploadService.FILE_TYPE_OTHER, file.getSize(),
                        RequestUtils.getIpAddress(request), CommonUtils.getDate(), CommonConstants.LICENSE_FILENAME));
                return CommonConstants.TEMPLATE_DONE;
            } catch (IllegalStateException | IOException e) {
                log.error(e.getMessage(), e);
            }
        }
        return CommonConstants.TEMPLATE_ERROR;
    }

    /**
     * @param _csrf
     * @param request
     * @param session
     * @param model
     * @return view name
     */
    @RequestMapping("reCreateIndex")
    public String reCreateIndex(String _csrf, HttpServletRequest request, HttpSession session, ModelMap model) {
        if (ControllerUtils.verifyNotEquals("_csrf", ControllerUtils.getAdminToken(request), _csrf, model)) {
            return CommonConstants.TEMPLATE_ERROR;
        }
        contentService.reCreateIndex();
        SysSite site = getSite(request);
        Long userId = ControllerUtils.getAdminFromSession(session).getId();
        logOperateService.save(new LogOperate(site.getId(), userId, LogLoginService.CHANNEL_WEB_MANAGER, "reCreateIndex",
                RequestUtils.getIpAddress(request), CommonUtils.getDate(), CommonConstants.BLANK));
        return CommonConstants.TEMPLATE_DONE;
    }
}
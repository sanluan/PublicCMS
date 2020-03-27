package com.publiccms.controller.admin.sys;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.multipart.MultipartFile;

import com.publiccms.common.annotation.Csrf;
import com.publiccms.common.constants.CmsVersion;
import com.publiccms.common.constants.CommonConstants;
import com.publiccms.common.tools.CmsFileUtils;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.common.tools.ControllerUtils;
import com.publiccms.common.tools.JsonUtils;
import com.publiccms.common.tools.RequestUtils;
import com.publiccms.common.tools.UserPasswordUtils;
import com.publiccms.entities.log.LogOperate;
import com.publiccms.entities.log.LogUpload;
import com.publiccms.entities.sys.SysDept;
import com.publiccms.entities.sys.SysDomain;
import com.publiccms.entities.sys.SysRole;
import com.publiccms.entities.sys.SysRoleUser;
import com.publiccms.entities.sys.SysRoleUserId;
import com.publiccms.entities.sys.SysSite;
import com.publiccms.entities.sys.SysUser;
import com.publiccms.logic.component.site.SiteComponent;
import com.publiccms.logic.service.cms.CmsContentService;
import com.publiccms.logic.service.log.LogLoginService;
import com.publiccms.logic.service.log.LogOperateService;
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
public class SysSiteAdminController {
    protected final Log log = LogFactory.getLog(getClass());
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
    protected LogUploadService logUploadService;
    @Autowired
    protected LogOperateService logOperateService;
    @Autowired
    protected SiteComponent siteComponent;

    private String[] ignoreProperties = new String[] { "id" };

    /**
     * @param site
     * @param admin
     * @param entity
     * @param domain
     * @param wild
     * @param roleName
     * @param deptName
     * @param userName
     * @param password
     * @param request
     * @param model
     * @return view name
     */
    @RequestMapping("save")
    @Csrf
    public String save(@RequestAttribute SysSite site, @SessionAttribute SysUser admin, SysSite entity, String domain,
            Boolean wild, String roleName, String deptName, String userName, String password, HttpServletRequest request,
            ModelMap model) {
        if (ControllerUtils.verifyCustom("noright", !siteComponent.isMaster(site.getId()), model)) {
            return CommonConstants.TEMPLATE_ERROR;
        }
        if (null == entity.getDynamicPath()) {
            entity.setDynamicPath(CommonConstants.SEPARATOR);
        } else if (!entity.getDynamicPath().endsWith(CommonConstants.SEPARATOR)) {
            entity.setDynamicPath(entity.getDynamicPath() + CommonConstants.SEPARATOR);
        }
        if (null == entity.getSitePath()) {
            entity.setSitePath(CommonConstants.SEPARATOR);
        } else if (!entity.getSitePath().endsWith(CommonConstants.SEPARATOR)) {
            entity.setSitePath(entity.getSitePath() + CommonConstants.SEPARATOR);
        }
        if (null != entity.getId()) {
            entity = service.update(entity.getId(), entity, ignoreProperties);
            if (null != entity) {
                logOperateService.save(new LogOperate(site.getId(), admin.getId(), LogLoginService.CHANNEL_WEB_MANAGER,
                        "update.site", RequestUtils.getIpAddress(request), CommonUtils.getDate(), JsonUtils.getString(entity)));
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
            String salt = UserPasswordUtils.getSalt();
            SysUser user = new SysUser(entity.getId(), userName, UserPasswordUtils.passwordEncode(password, salt), salt, true,
                    userName, dept.getId(), true, role.getId().toString(), null, false, true, false, null, null, 0,
                    CommonUtils.getDate());
            userService.save(user);// 初始化用户
            roleUserService.save(new SysRoleUser(new SysRoleUserId(role.getId(), user.getId())));// 初始化角色用户映射
            logOperateService.save(new LogOperate(site.getId(), admin.getId(), LogLoginService.CHANNEL_WEB_MANAGER, "save.site",
                    RequestUtils.getIpAddress(request), CommonUtils.getDate(), JsonUtils.getString(entity)));
        }
        siteComponent.clear();
        if (!siteComponent.getSite(request.getServerName()).getId().equals(site.getId()) || site.getId().equals(entity.getId())
                && (!site.getSitePath().equals(entity.getSitePath()) || !site.getDynamicPath().equals(entity.getDynamicPath()))) {
            return CommonConstants.TEMPLATE_DONEANDREFRESH;
        } else {
            return CommonConstants.TEMPLATE_DONE;
        }
    }

    /**
     * @param site
     * @param admin
     * @param id
     * @param request
     * @param model
     * @return view name
     */
    @SuppressWarnings("unchecked")
    @RequestMapping("delete")
    @Csrf
    public String delete(@RequestAttribute SysSite site, @SessionAttribute SysUser admin, Short id, HttpServletRequest request,
            ModelMap model) {
        if (ControllerUtils.verifyCustom("noright", !siteComponent.isMaster(site.getId()), model)) {
            return CommonConstants.TEMPLATE_ERROR;
        }
        SysSite entity = service.getEntity(id);
        if (null != entity) {
            service.delete(id);
            Long userId = admin.getId();
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
     * @param site
     * @param admin
     * @param sqlcommand
     * @param sqlparameters
     * @param oldurl
     * @param newurl
     * @param request
     * @param model
     * @return view name
     */
    @RequestMapping("execSql")
    @Csrf
    public String execSql(@RequestAttribute SysSite site, @SessionAttribute SysUser admin, String sqlcommand,
            String[] sqlparameters, HttpServletRequest request, ModelMap model) {
        if (ControllerUtils.verifyCustom("noright", !siteComponent.isMaster(site.getId()), model)) {
            return CommonConstants.TEMPLATE_ERROR;
        }
        if ("update_url".contains(sqlcommand)) {
            if (null != sqlparameters && 2 == sqlparameters.length) {
                try {
                    String oldurl = sqlparameters[0];
                    String newurl = sqlparameters[1];
                    int i = sqlService.updateContentAttribute(oldurl, newurl);
                    i += sqlService.updateContentRelated(oldurl, newurl);
                    i += sqlService.updatePlace(oldurl, newurl);
                    i += sqlService.updatePlaceAttribute(oldurl, newurl);
                    model.addAttribute("result", i);
                } catch (Exception e) {
                    model.addAttribute("error", e.getMessage());
                }
            }
        }
        model.addAttribute("sqlcommand", sqlcommand);
        model.addAttribute("sqlparameters", sqlparameters);
        logOperateService.save(new LogOperate(site.getId(), admin.getId(), LogLoginService.CHANNEL_WEB_MANAGER,
                "execsql.site", RequestUtils.getIpAddress(request), CommonUtils.getDate(), JsonUtils.getString(model)));
        return CommonConstants.TEMPLATE_DONE;
    }

    /**
     * @param site
     * @param admin
     * @param file
     * @param request
     * @param model
     * @return view name
     */
    @RequestMapping(value = "doUploadLicense", method = RequestMethod.POST)
    @Csrf
    public String upload(@RequestAttribute SysSite site, @SessionAttribute SysUser admin, MultipartFile file,
            HttpServletRequest request, ModelMap model) {
        if (ControllerUtils.verifyCustom("noright", !siteComponent.isMaster(site.getId()), model)) {
            return CommonConstants.TEMPLATE_ERROR;
        }
        if (null != file && !file.isEmpty()) {
            try {
                CmsFileUtils.upload(file, siteComponent.getRootPath() + CommonConstants.LICENSE_FILENAME);
                logUploadService.save(new LogUpload(site.getId(), admin.getId(), LogLoginService.CHANNEL_WEB_MANAGER,
                        "license.dat", CmsFileUtils.FILE_TYPE_OTHER, file.getSize(), null, null,
                        RequestUtils.getIpAddress(request), CommonUtils.getDate(), CommonConstants.LICENSE_FILENAME));
                return CommonConstants.TEMPLATE_DONE;
            } catch (IllegalStateException | IOException e) {
                log.error(e.getMessage(), e);
            }
        }
        return CommonConstants.TEMPLATE_ERROR;
    }

    /**
     * @param site
     * @param admin
     * @param request
     * @param model
     * @return view name
     */
    @RequestMapping("reCreateIndex")
    @Csrf
    public String reCreateIndex(@RequestAttribute SysSite site, @SessionAttribute SysUser admin, HttpServletRequest request,
            ModelMap model) {
        contentService.reCreateIndex();
        Long userId = admin.getId();
        logOperateService.save(new LogOperate(site.getId(), userId, LogLoginService.CHANNEL_WEB_MANAGER, "reCreateIndex",
                RequestUtils.getIpAddress(request), CommonUtils.getDate(), CommonConstants.BLANK));
        return CommonConstants.TEMPLATE_DONE;
    }
}
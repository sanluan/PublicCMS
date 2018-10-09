package com.publiccms.controller.admin.sys;

import static org.springframework.util.StringUtils.arrayToCommaDelimitedString;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.publiccms.common.base.AbstractController;
import com.publiccms.common.constants.CommonConstants;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.common.tools.ControllerUtils;
import com.publiccms.common.tools.JsonUtils;
import com.publiccms.common.tools.RequestUtils;
import com.publiccms.common.tools.VerificationUtils;
import com.publiccms.entities.log.LogOperate;
import com.publiccms.entities.sys.SysDept;
import com.publiccms.entities.sys.SysDeptCategory;
import com.publiccms.entities.sys.SysDeptCategoryId;
import com.publiccms.entities.sys.SysDeptConfig;
import com.publiccms.entities.sys.SysDeptConfigId;
import com.publiccms.entities.sys.SysDeptPage;
import com.publiccms.entities.sys.SysDeptPageId;
import com.publiccms.entities.sys.SysRoleUser;
import com.publiccms.entities.sys.SysRoleUserId;
import com.publiccms.entities.sys.SysSite;
import com.publiccms.entities.sys.SysUser;
import com.publiccms.logic.service.log.LogLoginService;
import com.publiccms.logic.service.sys.SysDeptCategoryService;
import com.publiccms.logic.service.sys.SysDeptConfigService;
import com.publiccms.logic.service.sys.SysDeptPageService;
import com.publiccms.logic.service.sys.SysDeptService;
import com.publiccms.logic.service.sys.SysRoleUserService;
import com.publiccms.logic.service.sys.SysUserService;

/**
 *
 * SysDeptAdminController
 * 
 */
@Controller
@RequestMapping("sysDept")
public class SysDeptAdminController extends AbstractController {
    @Autowired
    private SysDeptService service;
    @Autowired
    private SysUserService userService;
    @Autowired
    private SysRoleUserService roleUserService;
    @Autowired
    private SysDeptCategoryService sysDeptCategoryService;
    @Autowired
    private SysDeptPageService sysDeptPageService;
    @Autowired
    private SysDeptConfigService sysDeptConfigService;

    private String[] ignoreProperties = new String[] { "id", "siteId" };
    private String[] userIgnoreProperties = new String[] { "id", "superuserAccess", "registeredDate", "siteId", "authToken",
            "lastLoginDate", "lastLoginIp", "loginCount", "disabled" };

    /**
     * @param entity
     * @param categoryIds
     * @param pages
     * @param configs 
     * @param _csrf
     * @param request
     * @param session
     * @param model
     * @return view name
     */
    @RequestMapping("save")
    public String save(SysDept entity, Integer[] categoryIds, String[] pages, String[] configs, String _csrf,
            HttpServletRequest request, HttpSession session, ModelMap model) {
        if (ControllerUtils.verifyNotEquals("_csrf", ControllerUtils.getAdminToken(request), _csrf, model)) {
            return CommonConstants.TEMPLATE_ERROR;
        }
        SysSite site = getSite(request);
        if (null != entity.getId()) {
            SysDept oldEntity = service.getEntity(entity.getId());
            if (null == oldEntity || ControllerUtils.verifyNotEquals("siteId", site.getId(), oldEntity.getSiteId(), model)) {
                return CommonConstants.TEMPLATE_ERROR;
            }
            entity = service.update(entity.getId(), entity, ignoreProperties);
            if (null != entity) {
                logOperateService.save(new LogOperate(site.getId(), ControllerUtils.getAdminFromSession(session).getId(),
                        LogLoginService.CHANNEL_WEB_MANAGER, "update.dept", RequestUtils.getIpAddress(request),
                        CommonUtils.getDate(), JsonUtils.getString(entity)));
            }
            sysDeptCategoryService.updateDeptCategorys(entity.getId(), categoryIds);
            sysDeptPageService.updateDeptPages(entity.getId(), pages);
            sysDeptConfigService.updateDeptConfigs(entity.getId(), configs);
        } else {
            entity.setSiteId(site.getId());
            service.save(entity);
            logOperateService.save(new LogOperate(site.getId(), ControllerUtils.getAdminFromSession(session).getId(),
                    LogLoginService.CHANNEL_WEB_MANAGER, "save.dept", RequestUtils.getIpAddress(request), CommonUtils.getDate(),
                    JsonUtils.getString(entity)));
            if (CommonUtils.notEmpty(categoryIds)) {
                List<SysDeptCategory> list = new ArrayList<>();
                for (int categoryId : categoryIds) {
                    list.add(new SysDeptCategory(new SysDeptCategoryId(entity.getId(), categoryId)));
                }
                sysDeptCategoryService.save(list);
            }
            if (CommonUtils.notEmpty(pages)) {
                List<SysDeptPage> list = new ArrayList<>();
                for (String page : pages) {
                    list.add(new SysDeptPage(new SysDeptPageId(entity.getId(), page)));
                }
                sysDeptPageService.save(list);
            }
            if (CommonUtils.notEmpty(configs)) {
                List<SysDeptConfig> list = new ArrayList<>();
                for (String config : configs) {
                    list.add(new SysDeptConfig(new SysDeptConfigId(entity.getId(), config)));
                }
                sysDeptConfigService.save(list);
            }
        }
        return CommonConstants.TEMPLATE_DONE;
    }

    /**
     * @param entity
     * @param repassword
     * @param roleIds
     * @param _csrf
     * @param request
     * @param session
     * @param model
     * @return view name
     */
    @RequestMapping("saveUser")
    public String saveUser(SysUser entity, String repassword, Integer[] roleIds, String _csrf, HttpServletRequest request,
            HttpSession session, ModelMap model) {
        SysSite site = getSite(request);
        entity.setName(StringUtils.trim(entity.getName()));
        entity.setNickName(StringUtils.trim(entity.getNickName()));
        entity.setPassword(StringUtils.trim(entity.getPassword()));
        repassword = StringUtils.trim(repassword);
        SysDept dept = service.getEntity(entity.getDeptId());
        SysUser admin = ControllerUtils.getAdminFromSession(session);
        if (ControllerUtils.verifyNotEquals("_csrf", ControllerUtils.getAdminToken(request), _csrf, model)
                || ControllerUtils.verifyNotEmpty("username", entity.getName(), model)
                || ControllerUtils.verifyNotEmpty("deptId", dept, model)
                || ControllerUtils.verifyNotEquals("userId", dept.getUserId(), admin.getId(), model)
                || ControllerUtils.verifyNotEquals("siteId", site.getId(), dept.getSiteId(), model)
                || ControllerUtils.verifyNotEmpty("nickname", entity.getNickName(), model)
                || ControllerUtils.verifyNotUserName("username", entity.getName(), model)
                || ControllerUtils.verifyNotNickName("nickname", entity.getNickName(), model)) {
            return CommonConstants.TEMPLATE_ERROR;
        }
        entity.setSuperuserAccess(true);
        entity.setRoles(arrayToCommaDelimitedString(roleIds));
        if (null != entity.getId()) {
            SysUser oldEntity = userService.getEntity(entity.getId());
            if (null == oldEntity || ControllerUtils.verifyNotEquals("siteId", site.getId(), oldEntity.getSiteId(), model)) {
                return CommonConstants.TEMPLATE_ERROR;
            }
            if (!admin.isOwnsAllContent()) {
                entity.setOwnsAllContent(oldEntity.isOwnsAllContent());
            }
            SysUser user = userService.getEntity(entity.getId());
            if ((!user.getName().equals(entity.getName())
                    && ControllerUtils.verifyHasExist("username", userService.findByName(site.getId(), entity.getName()), model))
                    || (!user.getNickName().equals(entity.getNickName()) && ControllerUtils.verifyHasExist("nickname",
                            userService.findByNickName(site.getId(), entity.getNickName()), model))) {
                return CommonConstants.TEMPLATE_ERROR;
            }
            if (CommonUtils.notEmpty(entity.getPassword())) {
                if (ControllerUtils.verifyNotEquals("repassword", entity.getPassword(), repassword, model)) {
                    return CommonConstants.TEMPLATE_ERROR;
                }
                entity.setPassword(VerificationUtils.md5Encode(entity.getPassword()));
            } else {
                entity.setPassword(user.getPassword());
                if (CommonUtils.empty(entity.getEmail()) || !entity.getEmail().equals(user.getEmail())) {
                    entity.setEmailChecked(false);
                }
            }
            entity = userService.update(entity.getId(), entity, userIgnoreProperties);
            if (null != entity) {
                roleUserService.dealRoleUsers(entity.getId(), roleIds);
                logOperateService.save(new LogOperate(site.getId(), admin.getId(), LogLoginService.CHANNEL_WEB_MANAGER,
                        "update.user", RequestUtils.getIpAddress(request), CommonUtils.getDate(), JsonUtils.getString(entity)));
            }
        } else {
            if (ControllerUtils.verifyNotEmpty("password", entity.getPassword(), model)
                    || ControllerUtils.verifyNotEquals("repassword", entity.getPassword(), repassword, model) || ControllerUtils
                            .verifyHasExist("username", userService.findByName(site.getId(), entity.getName()), model)) {
                return CommonConstants.TEMPLATE_ERROR;
            }
            if (!admin.isOwnsAllContent()) {
                entity.setOwnsAllContent(false);
            }
            entity.setDeptId(dept.getId());
            entity.setSiteId(site.getId());
            entity.setPassword(VerificationUtils.md5Encode(entity.getPassword()));
            userService.save(entity);
            if (CommonUtils.notEmpty(roleIds)) {
                for (Integer roleId : roleIds) {
                    roleUserService.save(new SysRoleUser(new SysRoleUserId(roleId, entity.getId())));
                }
            }
            logOperateService.save(new LogOperate(site.getId(), admin.getId(), LogLoginService.CHANNEL_WEB_MANAGER, "save.user",
                    RequestUtils.getIpAddress(request), CommonUtils.getDate(), JsonUtils.getString(entity)));
        }
        return CommonConstants.TEMPLATE_DONE;
    }

    /**
     * @param id
     * @param _csrf
     * @param request
     * @param session
     * @param model
     * @return view name
     */
    @RequestMapping("delete")
    public String delete(Integer id, String _csrf, HttpServletRequest request, HttpSession session, ModelMap model) {
        if (ControllerUtils.verifyNotEquals("_csrf", ControllerUtils.getAdminToken(request), _csrf, model)) {
            return CommonConstants.TEMPLATE_ERROR;
        }
        SysSite site = getSite(request);
        SysDept entity = service.delete(site.getId(), id);
        if (null != entity) {
            sysDeptCategoryService.delete(entity.getId(), null);
            sysDeptPageService.delete(entity.getId(), null);
            sysDeptConfigService.delete(entity.getId(), null);
            logOperateService.save(new LogOperate(site.getId(), ControllerUtils.getAdminFromSession(session).getId(),
                    LogLoginService.CHANNEL_WEB_MANAGER, "delete.dept", RequestUtils.getIpAddress(request), CommonUtils.getDate(),
                    JsonUtils.getString(entity)));
        }
        return CommonConstants.TEMPLATE_DONE;
    }

    /**
     * @param id
     * @param _csrf
     * @param request
     * @param session
     * @param model
     * @return view name
     */
    @RequestMapping(value = "enableUser", method = RequestMethod.POST)
    public String enable(Long id, String _csrf, HttpServletRequest request, HttpSession session, ModelMap model) {
        if (ControllerUtils.verifyNotEquals("_csrf", ControllerUtils.getAdminToken(request), _csrf, model) || ControllerUtils
                .verifyEquals("admin.operate", ControllerUtils.getAdminFromSession(session).getId(), id, model)) {
            return CommonConstants.TEMPLATE_ERROR;
        }
        SysUser entity = userService.getEntity(id);
        if (null != entity) {
            SysSite site = getSite(request);
            SysDept dept = service.getEntity(entity.getDeptId());
            SysUser admin = ControllerUtils.getAdminFromSession(session);
            if (ControllerUtils.verifyNotEquals("siteId", site.getId(), entity.getSiteId(), model)
                    || ControllerUtils.verifyNotEmpty("deptId", dept, model)
                    || ControllerUtils.verifyNotEquals("userId", dept.getUserId(), admin.getId(), model)) {
                return CommonConstants.TEMPLATE_ERROR;
            }
            userService.updateStatus(id, false);
            logOperateService.save(new LogOperate(site.getId(), admin.getId(), LogLoginService.CHANNEL_WEB_MANAGER, "enable.user",
                    RequestUtils.getIpAddress(request), CommonUtils.getDate(), JsonUtils.getString(entity)));
        }
        return CommonConstants.TEMPLATE_DONE;
    }

    /**
     * @param id
     * @param _csrf
     * @param request
     * @param session
     * @param model
     * @return view name
     */
    @RequestMapping(value = "disableUser", method = RequestMethod.POST)
    public String disable(Long id, String _csrf, HttpServletRequest request, HttpSession session, ModelMap model) {
        if (ControllerUtils.verifyNotEquals("_csrf", ControllerUtils.getAdminToken(request), _csrf, model) || ControllerUtils
                .verifyEquals("admin.operate", ControllerUtils.getAdminFromSession(session).getId(), id, model)) {
            return CommonConstants.TEMPLATE_ERROR;
        }
        SysUser entity = userService.getEntity(id);
        if (null != entity) {
            SysSite site = getSite(request);
            SysDept dept = service.getEntity(entity.getDeptId());
            SysUser admin = ControllerUtils.getAdminFromSession(session);
            if (ControllerUtils.verifyNotEquals("siteId", site.getId(), entity.getSiteId(), model)
                    || ControllerUtils.verifyNotEmpty("deptId", dept, model)
                    || ControllerUtils.verifyNotEquals("userId", dept.getUserId(), admin.getId(), model)) {
                return CommonConstants.TEMPLATE_ERROR;
            }
            userService.updateStatus(id, true);
            logOperateService.save(new LogOperate(site.getId(), admin.getId(), LogLoginService.CHANNEL_WEB_MANAGER,
                    "disable.user", RequestUtils.getIpAddress(request), CommonUtils.getDate(), JsonUtils.getString(entity)));
        }
        return CommonConstants.TEMPLATE_DONE;
    }
}
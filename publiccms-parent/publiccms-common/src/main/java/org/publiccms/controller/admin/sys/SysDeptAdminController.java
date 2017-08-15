package org.publiccms.controller.admin.sys;

import static com.publiccms.common.tools.CommonUtils.empty;
import static com.publiccms.common.tools.CommonUtils.getDate;
import static com.publiccms.common.tools.CommonUtils.notEmpty;
import static com.publiccms.common.tools.ControllerUtils.verifyEquals;
import static com.publiccms.common.tools.ControllerUtils.verifyHasExist;
import static com.publiccms.common.tools.ControllerUtils.verifyNotEmpty;
import static com.publiccms.common.tools.ControllerUtils.verifyNotEquals;
import static com.publiccms.common.tools.JsonUtils.getString;
import static com.publiccms.common.tools.RequestUtils.getIpAddress;
import static com.publiccms.common.tools.VerificationUtils.encode;
import static org.apache.commons.lang3.StringUtils.trim;
import static org.springframework.util.StringUtils.arrayToCommaDelimitedString;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.publiccms.common.base.AbstractController;
import org.publiccms.entities.log.LogOperate;
import org.publiccms.entities.sys.SysDept;
import org.publiccms.entities.sys.SysDeptCategory;
import org.publiccms.entities.sys.SysDeptCategoryId;
import org.publiccms.entities.sys.SysDeptPage;
import org.publiccms.entities.sys.SysDeptPageId;
import org.publiccms.entities.sys.SysRoleUser;
import org.publiccms.entities.sys.SysRoleUserId;
import org.publiccms.entities.sys.SysSite;
import org.publiccms.entities.sys.SysUser;
import org.publiccms.logic.service.log.LogLoginService;
import org.publiccms.logic.service.sys.SysDeptCategoryService;
import org.publiccms.logic.service.sys.SysDeptPageService;
import org.publiccms.logic.service.sys.SysDeptService;
import org.publiccms.logic.service.sys.SysRoleUserService;
import org.publiccms.logic.service.sys.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

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

    private String[] ignoreProperties = new String[] { "id", "siteId" };
    private String[] userIgnoreProperties = new String[] { "id", "superuserAccess", "registeredDate", "siteId", "authToken",
            "lastLoginDate", "lastLoginIp", "loginCount", "disabled" };

    /**
     * @param entity
     * @param categoryIds
     * @param pages
     * @param request
     * @param session
     * @param model
     * @return
     */
    @RequestMapping("save")
    public String save(SysDept entity, Integer[] categoryIds, String[] pages, HttpServletRequest request, HttpSession session,
            ModelMap model) {
        SysSite site = getSite(request);
        if (null != entity.getId()) {
            SysDept oldEntity = service.getEntity(entity.getId());
            if (null == oldEntity || verifyNotEquals("siteId", site.getId(), oldEntity.getSiteId(), model)) {
                return TEMPLATE_ERROR;
            }
            entity = service.update(entity.getId(), entity, ignoreProperties);
            if (null != entity) {
                logOperateService.save(new LogOperate(site.getId(), getAdminFromSession(session).getId(),
                        LogLoginService.CHANNEL_WEB_MANAGER, "update.dept", getIpAddress(request), getDate(), getString(entity)));
            }
            sysDeptCategoryService.updateDeptCategorys(entity.getId(), categoryIds);
            sysDeptPageService.updateDeptPages(entity.getId(), pages);
        } else {
            entity.setSiteId(site.getId());
            service.save(entity);
            logOperateService.save(new LogOperate(site.getId(), getAdminFromSession(session).getId(),
                    LogLoginService.CHANNEL_WEB_MANAGER, "save.dept", getIpAddress(request), getDate(), getString(entity)));
            if (notEmpty(categoryIds)) {
                List<SysDeptCategory> list = new ArrayList<>();
                for (int categoryId : categoryIds) {
                    list.add(new SysDeptCategory(new SysDeptCategoryId(entity.getId(), categoryId)));
                }
                sysDeptCategoryService.save(list);
            }
            if (notEmpty(pages)) {
                List<SysDeptPage> list = new ArrayList<>();
                for (String page : pages) {
                    list.add(new SysDeptPage(new SysDeptPageId(entity.getId(), page)));
                }
                sysDeptPageService.save(list);
            }
        }
        return TEMPLATE_DONE;
    }

    /**
     * @param entity
     * @param repassword
     * @param roleIds
     * @param request
     * @param session
     * @param model
     * @return
     */
    @RequestMapping("saveUser")
    public String saveUser(SysUser entity, String repassword, Integer[] roleIds, HttpServletRequest request, HttpSession session,
            ModelMap model) {
        SysSite site = getSite(request);
        entity.setName(trim(entity.getName()));
        entity.setNickName(trim(entity.getNickName()));
        entity.setPassword(trim(entity.getPassword()));
        repassword = trim(repassword);
        SysDept dept = service.getEntity(entity.getDeptId());
        SysUser admin = getAdminFromSession(session);
        if (verifyNotEmpty("username", entity.getName(), model) || verifyNotEmpty("deptId", dept, model)
                || verifyNotEquals("userId", dept.getUserId(), admin.getId(), model)
                || verifyNotEquals("siteId", site.getId(), dept.getSiteId(), model)
                || verifyNotEmpty("nickname", entity.getNickName(), model)
                || verifyNotUserName("username", entity.getName(), model)
                || verifyNotNickName("nickname", entity.getNickName(), model)) {
            return TEMPLATE_ERROR;
        }
        entity.setSuperuserAccess(true);
        entity.setRoles(arrayToCommaDelimitedString(roleIds));
        if (null != entity.getId()) {
            SysUser oldEntity = userService.getEntity(entity.getId());
            if (null == oldEntity || verifyNotEquals("siteId", site.getId(), oldEntity.getSiteId(), model)) {
                return TEMPLATE_ERROR;
            }
            SysUser user = userService.getEntity(entity.getId());
            if ((!user.getName().equals(entity.getName())
                    && verifyHasExist("username", userService.findByName(site.getId(), entity.getName()), model))
                    || (!user.getNickName().equals(entity.getNickName()) && verifyHasExist("nickname",
                            userService.findByNickName(site.getId(), entity.getNickName()), model))) {
                return TEMPLATE_ERROR;
            }
            if (notEmpty(entity.getPassword())) {
                if (verifyNotEquals("repassword", entity.getPassword(), repassword, model)) {
                    return TEMPLATE_ERROR;
                }
                entity.setPassword(encode(entity.getPassword()));
            } else {
                entity.setPassword(user.getPassword());
                if (empty(entity.getEmail()) || !entity.getEmail().equals(user.getEmail())) {
                    entity.setEmailChecked(false);
                }
            }
            entity = userService.update(entity.getId(), entity, userIgnoreProperties);
            if (null != entity) {
                roleUserService.dealRoleUsers(entity.getId(), roleIds);
                logOperateService.save(new LogOperate(site.getId(), admin.getId(), LogLoginService.CHANNEL_WEB_MANAGER,
                        "update.user", getIpAddress(request), getDate(), getString(entity)));
            }
        } else {
            if (verifyNotEmpty("password", entity.getPassword(), model)
                    || verifyNotEquals("repassword", entity.getPassword(), repassword, model)
                    || verifyHasExist("username", userService.findByName(site.getId(), entity.getName()), model)) {
                return TEMPLATE_ERROR;
            }
            entity.setDeptId(dept.getId());
            entity.setSiteId(site.getId());
            entity.setPassword(encode(entity.getPassword()));
            userService.save(entity);
            if (notEmpty(roleIds)) {
                for (Integer roleId : roleIds) {
                    roleUserService.save(new SysRoleUser(new SysRoleUserId(roleId, entity.getId())));
                }
            }
            logOperateService.save(new LogOperate(site.getId(), admin.getId(), LogLoginService.CHANNEL_WEB_MANAGER, "save.user",
                    getIpAddress(request), getDate(), getString(entity)));
        }
        return TEMPLATE_DONE;
    }

    /**
     * @param id
     * @param request
     * @param session
     * @return
     */
    @RequestMapping("delete")
    public String delete(Integer id, HttpServletRequest request, HttpSession session) {
        SysSite site = getSite(request);
        List<Integer> list = service.delete(site.getId(), id);
        if (0 < list.size()) {
            for (Integer childId : list) {
                sysDeptCategoryService.delete(childId, null);
                sysDeptPageService.delete(childId, null);
            }
            logOperateService.save(new LogOperate(site.getId(), getAdminFromSession(session).getId(),
                    LogLoginService.CHANNEL_WEB_MANAGER, "delete.dept", getIpAddress(request), getDate(), id.toString()));
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
    @RequestMapping(value = "enableUser", method = RequestMethod.POST)
    public String enable(Long id, HttpServletRequest request, HttpSession session, ModelMap model) {
        if (verifyEquals("admin.operate", getAdminFromSession(session).getId(), id, model)) {
            return TEMPLATE_ERROR;
        }
        SysUser entity = userService.getEntity(id);
        if (null != entity) {
            SysSite site = getSite(request);
            SysDept dept = service.getEntity(entity.getDeptId());
            SysUser admin = getAdminFromSession(session);
            if (verifyNotEquals("siteId", site.getId(), entity.getSiteId(), model) || verifyNotEmpty("deptId", dept, model)
                    || verifyNotEquals("userId", dept.getUserId(), admin.getId(), model)) {
                return TEMPLATE_ERROR;
            }
            userService.updateStatus(id, false);
            logOperateService.save(new LogOperate(site.getId(), admin.getId(), LogLoginService.CHANNEL_WEB_MANAGER, "enable.user",
                    getIpAddress(request), getDate(), getString(entity)));
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
    @RequestMapping(value = "disableUser", method = RequestMethod.POST)
    public String disable(Long id, HttpServletRequest request, HttpSession session, ModelMap model) {
        if (verifyEquals("admin.operate", getAdminFromSession(session).getId(), id, model)) {
            return TEMPLATE_ERROR;
        }
        SysUser entity = userService.getEntity(id);
        if (null != entity) {
            SysSite site = getSite(request);
            SysDept dept = service.getEntity(entity.getDeptId());
            SysUser admin = getAdminFromSession(session);
            if (verifyNotEquals("siteId", site.getId(), entity.getSiteId(), model) || verifyNotEmpty("deptId", dept, model)
                    || verifyNotEquals("userId", dept.getUserId(), admin.getId(), model)) {
                return TEMPLATE_ERROR;
            }
            userService.updateStatus(id, true);
            logOperateService.save(new LogOperate(site.getId(), admin.getId(), LogLoginService.CHANNEL_WEB_MANAGER,
                    "disable.user", getIpAddress(request), getDate(), getString(entity)));
        }
        return TEMPLATE_DONE;
    }
}
package com.publiccms.logic.service.sys;

// Generated 2015-7-3 16:18:22 by com.publiccms.common.generator.SourceGenerator

import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.publiccms.common.base.BaseService;
import com.publiccms.common.handler.PageHandler;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.common.tools.UserPasswordUtils;
import com.publiccms.entities.sys.SysDept;
import com.publiccms.entities.sys.SysDomain;
import com.publiccms.entities.sys.SysRole;
import com.publiccms.entities.sys.SysRoleUser;
import com.publiccms.entities.sys.SysRoleUserId;
import com.publiccms.entities.sys.SysSite;
import com.publiccms.entities.sys.SysUser;
import com.publiccms.logic.dao.sys.SysSiteDao;

/**
 *
 * SysSiteService
 * 
 */
@Service
@Transactional
public class SysSiteService extends BaseService<SysSite> {
    @Resource
    private SysRoleService roleService;
    @Resource
    private SysUserService userService;
    @Resource
    private SysDeptService deptService;
    @Resource
    private SysDomainService domainService;
    @Resource
    private SysRoleUserService roleUserService;

    /**
     * @param disabled
     * @param parentId
     * @param name
     * @param pageIndex
     * @param pageSize
     * @return
     */
    @Transactional(readOnly = true)
    public PageHandler getPage(Boolean disabled, Short parentId, String name, Integer pageIndex, Integer pageSize) {
        return dao.getPage(disabled, parentId, name, pageIndex, pageSize);
    }

    /**
     * @param entity
     * @param domain
     * @param wild
     * @param multiple
     * @param roleName
     * @param deptName
     * @param userName
     * @param password
     * @param encoding
     * @return site master 
     */
    public SysUser save(SysSite entity, String domain, boolean wild, boolean multiple, String roleName, String deptName,
            String userName, String password, String encoding) {
        save(entity);
        if (CommonUtils.notEmpty(domain)) {
            domainService.save(new SysDomain(domain, entity.getId(), wild, multiple));
        }
        SysDept dept = new SysDept(entity.getId(), deptName, "code", 0, true, true, true);
        deptService.save(dept);// 初始化部门
        SysRole role = new SysRole(entity.getId(), roleName, true, true);
        roleService.save(role);// 初始化角色
        String salt = UserPasswordUtils.getSalt();
        SysUser user = new SysUser(entity.getId(), userName, UserPasswordUtils.passwordEncode(password, salt, null, encoding), true,
                userName, null, dept.getId(), 1, role.getId().toString(), null, false, true, false, null, null, 0,
                CommonUtils.getDate());
        userService.save(user);// 初始化用户
        roleUserService.save(new SysRoleUser(new SysRoleUserId(role.getId(), user.getId())));// 初始化角色用户映射
        return user;
    }

    /**
     * @param parentId
     * @param directory
     * @return entity
     */
    @Transactional(readOnly = true)
    public SysSite getEntity(short parentId, String directory) {
        return dao.getEntity(parentId, directory);
    }

    /**
     * @param id
     * @return
     */
    public SysSite delete(Short id) {
        SysSite entity = getEntity(id);
        if (null != entity) {
            entity.setDisabled(true);
        }
        return entity;
    }

    @Resource
    private SysSiteDao dao;

}
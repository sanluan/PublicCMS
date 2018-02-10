package com.publiccms.logic.service.sys;

import java.util.List;

import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.publiccms.common.base.BaseService;
import com.publiccms.common.handler.PageHandler;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.entities.sys.SysRoleModule;
import com.publiccms.entities.sys.SysRoleModuleId;
import com.publiccms.logic.dao.sys.SysRoleModuleDao;

/**
 *
 * SysRoleModuleService
 * 
 */
@Service
@Transactional
public class SysRoleModuleService extends BaseService<SysRoleModule> {

    /**
     * @param roleId
     * @param moduleId
     * @param pageIndex
     * @param pageSize
     * @return
     */
    @Transactional(readOnly = true)
    public PageHandler getPage(Integer roleId, Integer moduleId, Integer pageIndex, Integer pageSize) {
        return dao.getPage(roleId, moduleId, pageIndex, pageSize);
    }

    /**
     * @param roleId
     * @param moduleIds
     */
    public void updateRoleModules(Integer roleId, Integer[] moduleIds) {
        if (CommonUtils.notEmpty(roleId)) {
            @SuppressWarnings("unchecked")
            List<SysRoleModule> list = (List<SysRoleModule>) getPage(roleId, null, null, null).getList();
            for (SysRoleModule roleModule : list) {
                if (ArrayUtils.contains(moduleIds, roleModule.getId().getModuleId())) {
                    moduleIds = ArrayUtils.removeElement(moduleIds, roleModule.getId().getModuleId());
                } else {
                    delete(roleModule.getId());
                }
            }
            if (CommonUtils.notEmpty(moduleIds)) {
                for (int moduleId : moduleIds) {
                    save(new SysRoleModule(new SysRoleModuleId(roleId, moduleId)));
                }
            }
        }
    }

    /**
     * @param roleIds
     * @param moduleIds
     * @return
     */
    @Transactional(readOnly = true)
    public List<SysRoleModule> getEntitys(Integer[] roleIds, Integer[] moduleIds) {
        return dao.getEntitys(roleIds, moduleIds);
    }

    /**
     * @param roleIds
     * @param moduleId
     * @return
     */
    @Transactional(readOnly = true)
    public SysRoleModule getEntity(Integer[] roleIds, Integer moduleId) {
        return dao.getEntity(roleIds, moduleId);
    }

    /**
     * @param roleId
     * @return
     */
    public int deleteByRoleId(Integer roleId) {
        return dao.deleteByRoleId(roleId);
    }

    /**
     * @param moduleId
     * @return
     */
    public int deleteByModuleId(Integer moduleId) {
        return dao.deleteByModuleId(moduleId);
    }

    @Autowired
    private SysRoleModuleDao dao;
    
}
package org.publiccms.logic.service.sys;

// Generated 2015-7-22 13:48:39 by com.publiccms.common.source.SourceGenerator
import static com.publiccms.common.tools.CommonUtils.notEmpty;
import static org.apache.commons.lang3.ArrayUtils.contains;
import static org.apache.commons.lang3.ArrayUtils.removeElement;

import java.util.List;

import org.publiccms.entities.sys.SysRoleMoudle;
import org.publiccms.entities.sys.SysRoleMoudleId;
import org.publiccms.logic.dao.sys.SysRoleMoudleDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.publiccms.common.base.BaseService;
import com.publiccms.common.handler.PageHandler;

/**
 *
 * SysRoleMoudleService
 * 
 */
@Service
@Transactional
public class SysRoleMoudleService extends BaseService<SysRoleMoudle> {

    /**
     * @param roleId
     * @param moudleId
     * @param pageIndex
     * @param pageSize
     * @return
     */
    @Transactional(readOnly = true)
    public PageHandler getPage(Integer roleId, Integer moudleId, Integer pageIndex, Integer pageSize) {
        return dao.getPage(roleId, moudleId, pageIndex, pageSize);
    }

    /**
     * @param roleId
     * @param moudleIds
     */
    public void updateRoleMoudles(Integer roleId, Integer[] moudleIds) {
        if (notEmpty(roleId)) {
            @SuppressWarnings("unchecked")
            List<SysRoleMoudle> list = (List<SysRoleMoudle>) getPage(roleId, null, null, null).getList();
            for (SysRoleMoudle roleMoudle : list) {
                if (contains(moudleIds, roleMoudle.getId().getMoudleId())) {
                    moudleIds = removeElement(moudleIds, roleMoudle.getId().getMoudleId());
                } else {
                    delete(roleMoudle.getId());
                }
            }
            if (notEmpty(moudleIds)) {
                for (int moudleId : moudleIds) {
                    save(new SysRoleMoudle(new SysRoleMoudleId(roleId, moudleId)));
                }
            }
        }
    }

    /**
     * @param roleIds
     * @param moudleIds
     * @return
     */
    @Transactional(readOnly = true)
    public List<SysRoleMoudle> getEntitys(Integer[] roleIds, Integer[] moudleIds) {
        return dao.getEntitys(roleIds, moudleIds);
    }

    /**
     * @param roleIds
     * @param moudleId
     * @return
     */
    @Transactional(readOnly = true)
    public SysRoleMoudle getEntity(Integer[] roleIds, Integer moudleId) {
        return dao.getEntity(roleIds, moudleId);
    }

    /**
     * @param roleId
     * @return
     */
    public int deleteByRoleId(Integer roleId) {
        return dao.deleteByRoleId(roleId);
    }

    /**
     * @param moudleId
     * @return
     */
    public int deleteByMoudleId(Integer moudleId) {
        return dao.deleteByMoudleId(moudleId);
    }

    @Autowired
    private SysRoleMoudleDao dao;
    
}
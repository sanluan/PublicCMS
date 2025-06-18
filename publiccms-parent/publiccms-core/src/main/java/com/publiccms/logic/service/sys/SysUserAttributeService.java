package com.publiccms.logic.service.sys;

import java.io.Serializable;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import com.publiccms.common.base.BaseService;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.entities.sys.SysUserAttribute;

/**
 *
 * SysUserAttributeService
 * 
 */
@Service
@Transactional
public class SysUserAttributeService extends BaseService<SysUserAttribute> {

    @Override
    public List<SysUserAttribute> getEntitys(Serializable[] ids) {
        return basedao.getEntitys(ids);
    }

    /**
     * @param userId
     * @param data
     */
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public void updateAttribute(Long userId, String data) {
        SysUserAttribute entity = getEntity(userId);
        if (null != entity) {
            entity.setData(data);
            entity.setUpdateDate(CommonUtils.getDate());
        } else if (CommonUtils.notEmpty(data)) {
            entity = new SysUserAttribute(userId, data, CommonUtils.getDate());
            save(entity);
        }
    }
}
package com.publiccms.logic.service.sys;

import java.io.Serializable;
import java.util.List;

import org.springframework.stereotype.Service;
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
    public void updateAttribute(Long userId, String data) {
        SysUserAttribute attribute = getEntity(userId);
        if (null != attribute) {
            attribute.setData(data);
        } else if (CommonUtils.notEmpty(data)) {
            attribute = new SysUserAttribute(userId, data, CommonUtils.getDate());
            save(attribute);
        }
    }
}
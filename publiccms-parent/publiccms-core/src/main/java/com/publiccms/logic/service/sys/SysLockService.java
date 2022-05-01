package com.publiccms.logic.service.sys;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.publiccms.common.base.BaseService;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.entities.sys.SysLock;
import com.publiccms.logic.dao.sys.SysLockDao;

/**
 *
 * SysLockService
 * 
 */
@Service
@Transactional
public class SysLockService extends BaseService<SysLock> {
    public static final String ITEM_TYPE_LOGIN = "userLogin";
    public static final String ITEM_TYPE_IP_LOGIN = "ipLogin";
    public static final String ITEM_TYPE_REGISTER = "register";
    public static final String[] SYSTEM_ITEM_TYPES = { ITEM_TYPE_LOGIN, ITEM_TYPE_IP_LOGIN, ITEM_TYPE_REGISTER };

    /**
     * @param itemTypes
     * @param excludeItemTypes
     * @return list of site id
     */
    public List<Short> getSiteIdList(String[] itemTypes, String[] excludeItemTypes) {
        return dao.getSiteIdList(itemTypes, excludeItemTypes);
    }

    /**
     * @param itemTypes
     * @param excludeItemTypes
     * @param createDate
     * @return number of data deleted
     */
    public int delete(String[] itemTypes, String[] excludeItemTypes, Date createDate) {
        return dao.delete(itemTypes, excludeItemTypes, createDate);
    }

    /**
     * @param id
     * @return entity
     */
    @Transactional
    public SysLock updateCount(Serializable id) {
        SysLock entity = getEntity(id);
        if (null != entity) {
            entity.setCount(entity.getCount() + 1);
        }
        return entity;
    }

    /**
     * @param id
     * @param count
     * @param userId
     * @return entity
     */
    @Transactional
    public SysLock updateCreateDate(Serializable id, int count, Long userId) {
        SysLock entity = getEntity(id);
        if (null != entity) {
            entity.setCreateDate(CommonUtils.getDate());
            entity.setUserId(userId);
            entity.setCount(count);
        }
        return entity;
    }

    @Autowired
    private SysLockDao dao;
}
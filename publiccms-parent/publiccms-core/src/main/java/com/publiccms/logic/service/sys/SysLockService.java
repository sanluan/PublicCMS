package com.publiccms.logic.service.sys;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import jakarta.annotation.Resource;
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

    /**
     * @param itemTypes
     * @param excludeItemTypes
     * @return list of site id
     */
    public List<Short> getSiteIdListByItemTypes(String[] itemTypes, String[] excludeItemTypes) {
        return dao.getSiteIdList(itemTypes, excludeItemTypes);
    }

    /**
     * @param itemType
     * @param excludeItemTypes
     * @return list of site id
     */
    public List<Short> getSiteIdList(String itemType, String[] excludeItemTypes) {
        return dao.getSiteIdList(new String[] { itemType }, excludeItemTypes);
    }

    /**
     * @param itemType
     * @param excludeItemTypes
     * @param createDate
     * @return number of data deleted
     */
    public int delete(String itemType, String[] excludeItemTypes, Date createDate) {
        return dao.delete(new String[] { itemType }, excludeItemTypes, createDate);
    }

    /**
     * @param itemTypes
     * @param excludeItemTypes
     * @param createDate
     * @return number of data deleted
     */
    public int deleteByItemTypes(String[] itemTypes, String[] excludeItemTypes, Date createDate) {
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

    @Resource
    private SysLockDao dao;
}
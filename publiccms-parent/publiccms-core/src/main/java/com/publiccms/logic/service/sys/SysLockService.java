package com.publiccms.logic.service.sys;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
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
    public static final String ITEM_TYPE_CATEGORY = "category";
    public static final String ITEM_TYPE_CONTENT = "content";
    public static final String ITEM_TYPE_FILE = "file";
    public static final String ITEM_TYPE_TEMPLATE = "template";
    public static final String ITEM_TYPE_PLACE_TEMPLATE = "placeTemplate";
    public static final String ITEM_TYPE_TASK_TEMPLATE = "taskTemplate";
    public static final String ITEM_TYPE_PLACE = "place";
    public static final String ITEM_TYPE_CONFIG = "config";
    public static final String[] COMMON_ITEM_TYPES = { ITEM_TYPE_CATEGORY, ITEM_TYPE_CONTENT, ITEM_TYPE_FILE, ITEM_TYPE_TEMPLATE,
            ITEM_TYPE_PLACE_TEMPLATE, ITEM_TYPE_TASK_TEMPLATE, ITEM_TYPE_PLACE, ITEM_TYPE_CONFIG };

    /**
     * @param itemTypes
     * @return list of site id
     */
    public List<Short> getSiteIdList(String[] itemTypes) {
        return dao.getSiteIdList(itemTypes);
    }

    /**
     * @param itemTypes
     * @param createDate
     * @return number of data deleted
     */
    public int delete(String[] itemTypes, Date createDate) {
        return dao.delete(itemTypes, createDate);
    }

    /**
     * @param id
     * @return entity
     */
    @Transactional(isolation = Isolation.SERIALIZABLE)
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
    @Transactional(isolation = Isolation.SERIALIZABLE)
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
package com.publiccms.logic.dao.sys;

import java.util.Date;
import java.util.List;

// Generated 2022-2-9 10:41:51 by com.publiccms.common.generator.SourceGenerator

import org.springframework.stereotype.Repository;

import com.publiccms.common.base.BaseDao;
import com.publiccms.common.handler.QueryHandler;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.entities.sys.SysLock;

/**
 *
 * SysLockDao
 * 
 */
@Repository
public class SysLockDao extends BaseDao<SysLock> {

    /**
     * @param itemTypes
     * @param excludeItemTypes 
     * @return number of data deleted
     */
    @SuppressWarnings("unchecked")
    public List<Short> getSiteIdList(String[] itemTypes, String[] excludeItemTypes) {
        if (CommonUtils.notEmpty(itemTypes) || CommonUtils.notEmpty(excludeItemTypes)) {
            QueryHandler queryHandler = getQueryHandler("select bean.id.siteId from SysLock bean");
            if (CommonUtils.notEmpty(itemTypes)) {
                queryHandler.condition("bean.id.itemType in(:itemTypes)").setParameter("itemTypes", itemTypes);
            }
            if (CommonUtils.notEmpty(excludeItemTypes)) {
                queryHandler.condition("bean.id.itemType not in(:excludeItemTypes)").setParameter("excludeItemTypes",
                        excludeItemTypes);
            }
            queryHandler.group("bean.id.siteId");
            return (List<Short>) getList(queryHandler);
        }
        return null;
    }

    /**
     * @param itemTypes
     * @param excludeItemTypes
     * @param createDate
     * @return number of data deleted
     */
    public int delete(String[] itemTypes, String[] excludeItemTypes, Date createDate) {
        if ((CommonUtils.notEmpty(itemTypes) || CommonUtils.notEmpty(excludeItemTypes)) && null != createDate) {
            QueryHandler queryHandler = getQueryHandler("delete from SysLock bean");
            if (CommonUtils.notEmpty(itemTypes)) {
                queryHandler.condition("bean.id.itemType in(:itemTypes)").setParameter("itemTypes", itemTypes);
            }
            if (CommonUtils.notEmpty(excludeItemTypes)) {
                queryHandler.condition("bean.id.itemType not in(:excludeItemTypes)").setParameter("excludeItemTypes",
                        excludeItemTypes);
            }
            queryHandler.condition("bean.createDate <= :createDate").setParameter("createDate", createDate);
            return delete(queryHandler);
        }
        return 0;
    }

    @Override
    protected SysLock init(SysLock entity) {
        if (null == entity.getCreateDate()) {
            entity.setCreateDate(CommonUtils.getDate());
        }
        return entity;
    }

}
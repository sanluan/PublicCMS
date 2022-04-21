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
     * @return number of data deleted
     */
    @SuppressWarnings("unchecked")
    public List<Short> getSiteIdList(String[] itemTypes) {
        if (CommonUtils.notEmpty(itemTypes)) {
            QueryHandler queryHandler = getQueryHandler("select bean.id.siteId from SysLock bean");
            queryHandler.condition("bean.id.itemType in(:itemTypes)").setParameter("itemTypes", itemTypes);
            queryHandler.group("bean.id.siteId");
            return (List<Short>) getList(queryHandler);
        }
        return null;
    }

    /**
     * @param itemTypes
     * @param createDate
     * @return number of data deleted
     */
    public int delete(String[] itemTypes, Date createDate) {
        if (CommonUtils.notEmpty(itemTypes) && null != createDate) {
            QueryHandler queryHandler = getQueryHandler("delete from SysLock bean");
            queryHandler.condition("bean.id.itemType in(:itemTypes)").setParameter("itemTypes", itemTypes);
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
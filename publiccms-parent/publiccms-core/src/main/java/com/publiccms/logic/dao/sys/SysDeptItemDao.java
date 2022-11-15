package com.publiccms.logic.dao.sys;

import org.springframework.stereotype.Repository;

import com.publiccms.common.base.BaseDao;
import com.publiccms.common.handler.PageHandler;
import com.publiccms.common.handler.QueryHandler;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.entities.sys.SysDeptItem;

/**
 *
 * SysDeptItemDao
 * 
 */
@Repository
public class SysDeptItemDao extends BaseDao<SysDeptItem> {

    /**
     * @param deptId
     * @param itemType
     * @param itemId
     * @param pageIndex
     * @param pageSize
     * @return results page
     */
    public PageHandler getPage(Integer deptId, String itemType, String itemId, Integer pageIndex, Integer pageSize) {
        QueryHandler queryHandler = getQueryHandler("from SysDeptItem bean");
        if (CommonUtils.notEmpty(deptId)) {
            queryHandler.condition("bean.id.deptId = :deptId").setParameter("deptId", deptId);
        }
        if (CommonUtils.notEmpty(itemType)) {
            queryHandler.condition("bean.id.itemType = :itemType").setParameter("itemType", itemType);
        }
        if (CommonUtils.notEmpty(itemId)) {
            queryHandler.condition("bean.id.itemId = :itemId").setParameter("itemId", itemId);
        }
        return getPage(queryHandler, pageIndex, pageSize);
    }
    
    /**
     * @param deptId
     * @param itemType
     * @param itemId
     * @return results number
     */
    public int delete(Integer deptId, String itemType, String itemId) {
        QueryHandler queryHandler = getQueryHandler("delete from SysDeptItem bean");
        if (CommonUtils.notEmpty(deptId)) {
            queryHandler.condition("bean.id.deptId = :deptId").setParameter("deptId", deptId);
        }
        if (CommonUtils.notEmpty(itemType)) {
            queryHandler.condition("bean.id.itemType = :itemType").setParameter("itemType", itemType);
        }
        if (CommonUtils.notEmpty(itemId)) {
            queryHandler.condition("bean.id.itemId = :itemId").setParameter("itemId", itemId);
        }
        return delete(queryHandler);
    }

    @Override
    protected SysDeptItem init(SysDeptItem entity) {
        return entity;
    }

}
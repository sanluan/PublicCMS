package com.publiccms.logic.dao.sys;

import org.springframework.stereotype.Repository;

import com.publiccms.common.base.BaseDao;
import com.publiccms.common.handler.PageHandler;
import com.publiccms.common.handler.QueryHandler;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.entities.sys.SysDeptCategory;

/**
 *
 * SysDeptCategoryDao
 * 
 */
@Repository
public class SysDeptCategoryDao extends BaseDao<SysDeptCategory> {

    /**
     * @param deptId
     * @param categoryId
     * @param pageIndex
     * @param pageSize
     * @return results page
     */
    public PageHandler getPage(Integer deptId, Integer categoryId, Integer pageIndex, Integer pageSize) {
        QueryHandler queryHandler = getQueryHandler("from SysDeptCategory bean");
        if (CommonUtils.notEmpty(deptId)) {
            queryHandler.condition("bean.id.deptId = :deptId").setParameter("deptId", deptId);
        }
        if (CommonUtils.notEmpty(categoryId)) {
            queryHandler.condition("bean.id.categoryId = :categoryId").setParameter("categoryId", categoryId);
        }
        return getPage(queryHandler, pageIndex, pageSize);
    }

    @Override
    protected SysDeptCategory init(SysDeptCategory entity) {
        return entity;
    }

}
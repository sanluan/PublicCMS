package com.publiccms.logic.dao.sys;

import org.springframework.stereotype.Repository;

import com.publiccms.entities.sys.SysDeptCategory;
import com.sanluan.common.base.BaseDao;
import com.sanluan.common.handler.PageHandler;
import com.sanluan.common.handler.QueryHandler;

@Repository
public class SysDeptCategoryDao extends BaseDao<SysDeptCategory> {
    public PageHandler getPage(Integer deptId, Integer categoryId, Integer pageIndex, Integer pageSize) {
        QueryHandler queryHandler = getQueryHandler("from SysDeptCategory bean");
        if (notEmpty(deptId)) {
            queryHandler.condition("bean.id.deptId = :deptId").setParameter("deptId", deptId);
        }
        if (notEmpty(categoryId)) {
            queryHandler.condition("bean.id.categoryId = :categoryId").setParameter("categoryId", categoryId);
        }
        return getPage(queryHandler, pageIndex, pageSize);
    }

    @Override
    protected SysDeptCategory init(SysDeptCategory entity) {
        return entity;
    }

}
package com.publiccms.logic.dao.sys;

// Generated 2016-1-19 11:41:45 by com.sanluan.common.source.SourceMaker

import java.util.ArrayList;
import java.util.List;

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
            queryHandler.condition("bean.deptId = :deptId").setParameter("deptId", deptId);
        }
        if (notEmpty(categoryId)) {
            queryHandler.condition("bean.categoryId = :categoryId").setParameter("categoryId", categoryId);
        }
        queryHandler.order("bean.id desc");
        return getPage(queryHandler, pageIndex, pageSize);
    }

    public SysDeptCategory getEntity(Integer deptId, Integer categoryId) {
        if (notEmpty(deptId) && notEmpty(categoryId)) {
            QueryHandler queryHandler = getQueryHandler("from SysDeptCategory bean");
            queryHandler.condition("bean.deptId = :deptId").setParameter("deptId", deptId);
            queryHandler.condition("bean.categoryId = :categoryId").setParameter("categoryId", categoryId);
            return getEntity(queryHandler);
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    public List<SysDeptCategory> getEntitys(Integer deptId, Integer[] categoryIds) {
        if (notEmpty(deptId) && notEmpty(categoryIds)) {
            QueryHandler queryHandler = getQueryHandler("from SysDeptCategory bean");
            queryHandler.condition("bean.deptId = :deptId").setParameter("deptId", deptId);
            queryHandler.condition("bean.categoryId in (:categoryIds)").setParameter("categoryIds", categoryIds);
            return (List<SysDeptCategory>) getList(queryHandler);
        }
        return new ArrayList<SysDeptCategory>();
    }

    @Override
    protected SysDeptCategory init(SysDeptCategory entity) {
        return entity;
    }

}
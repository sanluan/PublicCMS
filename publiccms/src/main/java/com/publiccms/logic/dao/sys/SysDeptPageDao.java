package com.publiccms.logic.dao.sys;

import org.springframework.stereotype.Repository;

import com.publiccms.entities.sys.SysDeptPage;
import com.sanluan.common.base.BaseDao;
import com.sanluan.common.handler.PageHandler;
import com.sanluan.common.handler.QueryHandler;

@Repository
public class SysDeptPageDao extends BaseDao<SysDeptPage> {
    public PageHandler getPage(Integer deptId, String page, Integer pageIndex, Integer pageSize) {
        QueryHandler queryHandler = getQueryHandler("from SysDeptPage bean");
        if (notEmpty(deptId)) {
            queryHandler.condition("bean.id.deptId = :deptId").setParameter("deptId", deptId);
        }
        if (notEmpty(page)) {
            queryHandler.condition("bean.id.page = :page").setParameter("page", page);
        }
        return getPage(queryHandler, pageIndex, pageSize);
    }

    @Override
    protected SysDeptPage init(SysDeptPage entity) {
        return entity;
    }

}
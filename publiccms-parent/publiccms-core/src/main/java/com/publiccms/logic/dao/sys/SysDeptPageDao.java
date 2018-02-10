package com.publiccms.logic.dao.sys;

import org.springframework.stereotype.Repository;

import com.publiccms.common.base.BaseDao;
import com.publiccms.common.handler.PageHandler;
import com.publiccms.common.handler.QueryHandler;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.entities.sys.SysDeptPage;

/**
 *
 * SysDeptPageDao
 * 
 */
@Repository
public class SysDeptPageDao extends BaseDao<SysDeptPage> {

    /**
     * @param deptId
     * @param page
     * @param pageIndex
     * @param pageSize
     * @return results page
     */
    public PageHandler getPage(Integer deptId, String page, Integer pageIndex, Integer pageSize) {
        QueryHandler queryHandler = getQueryHandler("from SysDeptPage bean");
        if (CommonUtils.notEmpty(deptId)) {
            queryHandler.condition("bean.id.deptId = :deptId").setParameter("deptId", deptId);
        }
        if (CommonUtils.notEmpty(page)) {
            queryHandler.condition("bean.id.page = :page").setParameter("page", page);
        }
        return getPage(queryHandler, pageIndex, pageSize);
    }

    @Override
    protected SysDeptPage init(SysDeptPage entity) {
        return entity;
    }

}
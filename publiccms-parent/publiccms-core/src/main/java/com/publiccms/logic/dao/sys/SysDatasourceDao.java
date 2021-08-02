package com.publiccms.logic.dao.sys;

// Generated 2021-8-2 11:31:34 by com.publiccms.common.generator.SourceGenerator

import org.springframework.stereotype.Repository;

import com.publiccms.common.base.BaseDao;
import com.publiccms.common.handler.PageHandler;
import com.publiccms.common.handler.QueryHandler;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.entities.sys.SysDatasource;

/**
 *
 * SysDatasourceDao
 * 
 */
@Repository
public class SysDatasourceDao extends BaseDao<SysDatasource> {

    /**
     * @param disabled
     * @param orderType
     * @param pageIndex
     * @param pageSize
     * @return results page
     */
    public PageHandler getPage(Boolean disabled, String orderType, Integer pageIndex, Integer pageSize) {
        QueryHandler queryHandler = getQueryHandler("from SysDatasource bean");
        if (null != disabled) {
            queryHandler.condition("bean.disabled = :disabled").setParameter("disabled", disabled);
        }
        if (!ORDERTYPE_ASC.equalsIgnoreCase(orderType)) {
            orderType = ORDERTYPE_DESC;
        }
        queryHandler.order("bean.createDate ").append(orderType);
        return getPage(queryHandler, pageIndex, pageSize);
    }

    @Override
    protected SysDatasource init(SysDatasource entity) {
        if (null == entity.getCreateDate()) {
            entity.setCreateDate(CommonUtils.getDate());
        }
        return entity;
    }

}
package com.publiccms.logic.dao.sys;

import java.util.Date;

// Generated 2023-01-08 11:15:59 by com.publiccms.common.generator.SourceGenerator

import org.springframework.stereotype.Repository;

import com.publiccms.common.base.BaseDao;
import com.publiccms.common.constants.Constants;
import com.publiccms.common.handler.PageHandler;
import com.publiccms.common.handler.QueryHandler;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.entities.sys.SysRecord;

/**
 *
 * SysRecordDao
 * 
 */
@Repository
public class SysRecordDao extends BaseDao<SysRecord> {
    /**
     * @param siteId
     * @param code
     * @param startCreateDate
     * @param endCreateDate
     * @param orderField
     * @param orderType
     * @param pageIndex
     * @param pageSize
     * @return results page
     */
    public PageHandler getPage(Short siteId, String code, Date startCreateDate, Date endCreateDate, String orderField,
            String orderType, Integer pageIndex, Integer pageSize) {
        QueryHandler queryHandler = getQueryHandler("from SysRecord bean");
        if (CommonUtils.notEmpty(siteId)) {
            queryHandler.condition("bean.id.siteId = :siteId").setParameter("siteId", siteId);
        }
        if (CommonUtils.notEmpty(code)) {
            queryHandler.condition("bean.id.code like :code").setParameter("code", like(code));
        }
        if (null != startCreateDate) {
            queryHandler.condition("bean.createDate > :startCreateDate").setParameter("startCreateDate", startCreateDate);
        }
        if (null != endCreateDate) {
            queryHandler.condition("bean.createDate <= :endCreateDate").setParameter("endCreateDate", endCreateDate);
        }
        if (!ORDERTYPE_ASC.equalsIgnoreCase(orderType)) {
            orderType = ORDERTYPE_DESC;
        }
        if (null == orderField) {
            orderField = Constants.BLANK;
        }
        switch (orderField) {
        case "updateDate":
            queryHandler.order("bean.updateDate").append(orderType);
            break;
        default:
            queryHandler.order("bean.createDate").append(orderType);
        }
        return getPage(queryHandler, pageIndex, pageSize);
    }

    @Override
    protected SysRecord init(SysRecord entity) {
        if (null == entity.getCreateDate()) {
            entity.setCreateDate(CommonUtils.getDate());
        }
        return entity;
    }

}
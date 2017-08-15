package org.publiccms.logic.dao.cms;

// Generated 2016-3-1 17:24:23 by com.publiccms.common.source.SourceGenerator

import static com.publiccms.common.tools.CommonUtils.notEmpty;

import java.util.Date;

import org.publiccms.entities.cms.CmsLottery;
import org.springframework.stereotype.Repository;

import com.publiccms.common.base.BaseDao;
import com.publiccms.common.handler.PageHandler;
import com.publiccms.common.handler.QueryHandler;

/**
 *
 * CmsLotteryDao
 * 
 */
@Repository
public class CmsLotteryDao extends BaseDao<CmsLottery> {
    
    /**
     * @param siteId
     * @param startStartDate
     * @param endStartDate
     * @param startEndDate
     * @param endEndDate
     * @param disabled
     * @param orderField
     * @param orderType
     * @param pageIndex
     * @param pageSize
     * @return
     */
    public PageHandler getPage(Integer siteId, Date startStartDate, Date endStartDate, Date startEndDate, Date endEndDate,
            Boolean disabled, String orderField, String orderType, Integer pageIndex, Integer pageSize) {
        QueryHandler queryHandler = getQueryHandler("from CmsLottery bean");
        if (notEmpty(siteId)) {
            queryHandler.condition("bean.siteId = :siteId").setParameter("siteId", siteId);
        }
        if (notEmpty(startStartDate)) {
            queryHandler.condition("bean.startDate > :startStartDate").setParameter("startStartDate", startStartDate);
        }
        if (notEmpty(endStartDate)) {
            queryHandler.condition("bean.startDate <= :endStartDate").setParameter("endStartDate", endStartDate);
        }
        if (notEmpty(startEndDate)) {
            queryHandler.condition("bean.endDate > :startEndDate").setParameter("startEndDate", startEndDate);
        }
        if (notEmpty(endEndDate)) {
            queryHandler.condition("bean.endDate <= :endEndDate").setParameter("endEndDate", endEndDate);
        }
        if (notEmpty(disabled)) {
            queryHandler.condition("bean.disabled = :disabled").setParameter("disabled", disabled);
        }
        if (!ORDERTYPE_ASC.equalsIgnoreCase(orderType)) {
            orderType = ORDERTYPE_DESC;
        }
        if (null == orderField) {
            orderField = BLANK;
        }
        switch (orderField) {
        case "startDate":
            queryHandler.order("bean.startDate " + orderType);
            break;
        case "endDate":
            queryHandler.order("bean.endDate " + orderType);
            break;
        default:
            queryHandler.order("bean.id " + orderType);
        }
        return getPage(queryHandler, pageIndex, pageSize);
    }

    @Override
    protected CmsLottery init(CmsLottery entity) {
        return entity;
    }

}
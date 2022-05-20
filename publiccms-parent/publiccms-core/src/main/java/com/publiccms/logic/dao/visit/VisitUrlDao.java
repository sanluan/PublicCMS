package com.publiccms.logic.dao.visit;

import java.util.Date;

// Generated 2021-1-14 22:44:12 by com.publiccms.common.generator.SourceGenerator

import org.springframework.stereotype.Repository;

import com.publiccms.common.base.BaseDao;
import com.publiccms.common.handler.PageHandler;
import com.publiccms.common.handler.QueryHandler;
import com.publiccms.entities.visit.VisitUrl;

/**
 *
 * VisitUrlDao
 *
 */
@Repository
public class VisitUrlDao extends BaseDao<VisitUrl> {

    /**
     * @param siteId
     * @param startVisitDate
     * @param endVisitDate
     * @param pageIndex
     * @param pageSize
     * @return results page
     */
    public PageHandler getPage(short siteId, Date startVisitDate, Date endVisitDate, Integer pageIndex, Integer pageSize) {
        QueryHandler queryHandler = getQueryHandler("from VisitUrl bean");
        queryHandler.condition("bean.id.siteId = :siteId").setParameter("siteId", siteId);
        if (null != startVisitDate) {
            queryHandler.condition("bean.id.visitDate > :startVisitDate").setParameter("startVisitDate", startVisitDate);
        }
        if (null != endVisitDate) {
            queryHandler.condition("bean.id.visitDate <= :endVisitDate").setParameter("endVisitDate", endVisitDate);
        }
        queryHandler.order("bean.id.visitDate").append(ORDERTYPE_DESC);
        queryHandler.order("bean.pv").append(ORDERTYPE_DESC);
        return getPage(queryHandler, pageIndex, pageSize);
    }

    /**
     * @param begintime
     * @return number of data deleted
     */
    public int delete(Date begintime) {
        if (null != begintime) {
            QueryHandler queryHandler = getQueryHandler("delete from VisitUrl bean");
            if (null != begintime) {
                queryHandler.condition("bean.id.visitDate <= :visitDate").setParameter("visitDate", begintime);
            }
            return delete(queryHandler);
        }
        return 0;
    }

    @Override
    protected VisitUrl init(VisitUrl entity) {
        return entity;
    }

}
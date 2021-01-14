package com.publiccms.logic.dao.log;

import java.util.Date;
import java.util.List;

// Generated 2021-1-14 22:44:06 by com.publiccms.common.generator.SourceGenerator

import org.springframework.stereotype.Repository;

import com.publiccms.common.base.BaseDao;
import com.publiccms.common.handler.PageHandler;
import com.publiccms.common.handler.QueryHandler;
import com.publiccms.entities.log.LogVisitDay;
import com.publiccms.entities.log.LogVisitSession;

/**
 *
 * LogVisitSessionDao
 * 
 */
@Repository
public class LogVisitSessionDao extends BaseDao<LogVisitSession> {

    /**
     * @param siteId
     * @param sessionId
     * @param startVisitDate
     * @param endVisitDate
     * @param orderType
     * @param pageIndex
     * @param pageSize
     * @return results page
     */
    public PageHandler getPage(short siteId, String sessionId, Date startVisitDate, Date endVisitDate, String orderType,
            Integer pageIndex, Integer pageSize) {
        QueryHandler queryHandler = getQueryHandler("from LogVisitSession bean");
        queryHandler.condition("bean.id.siteId = :siteId").setParameter("siteId", siteId);
        if (null != sessionId) {
            queryHandler.condition("bean.id.sessionId = :sessionId").setParameter("sessionId", sessionId);
        }
        if (null != startVisitDate) {
            queryHandler.condition("bean.id.visitDate > :startVisitDate").setParameter("startVisitDate", startVisitDate);
        }
        if (null != endVisitDate) {
            queryHandler.condition("bean.id.visitDate <= :endVisitDate").setParameter("endVisitDate", endVisitDate);
        }
        if (!ORDERTYPE_ASC.equalsIgnoreCase(orderType)) {
            orderType = ORDERTYPE_DESC;
        }
        queryHandler.order("bean.lastVisitDate " + orderType);
        return getPage(queryHandler, pageIndex, pageSize);
    }

    /**
     * @param visitDate
     * @return results page
     */
    @SuppressWarnings("unchecked")
    public List<LogVisitDay> getDayList(Date visitDate) {
        QueryHandler queryHandler = getQueryHandler(
                "select new LogVisitDay(new LogVisitDayId(bean.siteId,bean.visitDate,-1),count(*),count(distinct bean.sessionId),sum(bean.pv)) from LogVisitSession bean");
        queryHandler.condition("bean.visitDate = :visitDate").setParameter("visitDate", visitDate);
        queryHandler.group("bean.siteId");
        queryHandler.group("bean.visitDate");
        return (List<LogVisitDay>) getList(queryHandler);
    }

    /**
     * @param begintime
     * @return number of data deleted
     */
    public int delete(Date begintime) {
        if (null != begintime) {
            QueryHandler queryHandler = getQueryHandler("delete from LogVisitSession bean");
            if (null != begintime) {
                queryHandler.condition("bean.visitDate <= :visitDate").setParameter("visitDate", begintime);
            }
            return delete(queryHandler);
        }
        return 0;
    }

    @Override
    protected LogVisitSession init(LogVisitSession entity) {
        return entity;
    }

}
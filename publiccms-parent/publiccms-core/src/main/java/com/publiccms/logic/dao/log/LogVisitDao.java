package com.publiccms.logic.dao.log;

// Generated 2021-1-14 22:43:59 by com.publiccms.common.generator.SourceGenerator
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.publiccms.common.base.BaseDao;
import com.publiccms.common.handler.PageHandler;
import com.publiccms.common.handler.QueryHandler;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.entities.log.LogVisit;
import com.publiccms.entities.log.LogVisitDay;
import com.publiccms.entities.log.LogVisitSession;

/**
 *
 * LogVisitDao
 * 
 */
@Repository
public class LogVisitDao extends BaseDao<LogVisit> {

    /**
     * @param siteId
     * @param sessionId
     * @param startCreateDate
     * @param endCreateDate
     * @param orderType
     * @param pageIndex
     * @param pageSize
     * @return results page
     */
    public PageHandler getPage(Short siteId, String sessionId, Date startCreateDate, Date endCreateDate, String orderType,
            Integer pageIndex, Integer pageSize) {
        QueryHandler queryHandler = getQueryHandler("from LogVisit bean");
        if (null != siteId) {
            queryHandler.condition("bean.siteId = :siteId").setParameter("siteId", siteId);
        }
        if (CommonUtils.notEmpty(sessionId)) {
            queryHandler.condition("bean.sessionId = :sessionId").setParameter("sessionId", sessionId);
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
        queryHandler.order("bean.createDate " + orderType);
        return getPage(queryHandler, pageIndex, pageSize);
    }

    /**
     * @param sessionId
     * @param visitDate
     * @param visitHour
     * @return results page
     */
    @SuppressWarnings("unchecked")
    public List<LogVisitDay> getHourList(Date visitDate, byte visitHour) {
        QueryHandler queryHandler = getQueryHandler(
                "select new LogVisitDay(bean.siteId,bean.visitDate,bean.visitHour,count(*),count(distinct bean.sessionId),count(distinct bean.ip)) from LogVisit bean");
        queryHandler.condition("bean.visitDate = :visitDate").setParameter("visitDate", visitDate);
        queryHandler.condition("bean.visitHour = :visitHour").setParameter("visitHour", visitHour);
        queryHandler.group("bean.siteId");
        queryHandler.group("bean.visitDate");
        queryHandler.group("bean.visitHour");
        return (List<LogVisitDay>) getList(queryHandler);
    }

    /**
     * @param startCreateDate
     * @param endCreateDate
     * @return results page
     */
    @SuppressWarnings("unchecked")
    public List<LogVisitSession> getSessionList(Date startCreateDate, Date endCreateDate) {
        QueryHandler queryHandler = getQueryHandler(
                "select new LogVisitSession(bean.siteId,bean.sessionId,bean.visitDate,max(bean.createDate), min(bean.createDate), bean.ip, count(*)) from LogVisit bean");
        if (null != startCreateDate) {
            queryHandler.condition("bean.createDate > :startCreateDate").setParameter("startCreateDate", startCreateDate);
        }
        if (null != endCreateDate) {
            queryHandler.condition("bean.createDate <= :endCreateDate").setParameter("endCreateDate", endCreateDate);
        }
        queryHandler.group("bean.siteId");
        queryHandler.group("bean.sessionId");
        queryHandler.group("bean.visitDate");
        queryHandler.group("bean.ip");
        return (List<LogVisitSession>) getList(queryHandler);
    }

    /**
     * @param begintime
     * @return number of data deleted
     */
    public int delete(Date begintime) {
        if (null != begintime) {
            QueryHandler queryHandler = getQueryHandler("delete from LogVisit bean");
            if (null != begintime) {
                queryHandler.condition("bean.createDate <= :createDate").setParameter("createDate", begintime);
            }
            return delete(queryHandler);
        }
        return 0;
    }

    @Override
    protected LogVisit init(LogVisit entity) {
        if (null == entity.getCreateDate()) {
            entity.setCreateDate(CommonUtils.getDate());
        }
        return entity;
    }

}
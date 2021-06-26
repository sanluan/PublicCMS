package com.publiccms.logic.dao.log;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.time.DateUtils;

// Generated 2021-1-14 22:44:06 by com.publiccms.common.generator.SourceGenerator

import org.springframework.stereotype.Repository;

import com.publiccms.common.base.BaseDao;
import com.publiccms.common.handler.PageHandler;
import com.publiccms.common.handler.QueryHandler;
import com.publiccms.common.tools.CommonUtils;
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
        if (CommonUtils.notEmpty(sessionId)) {
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
        queryHandler.order("bean.lastVisitDate ").append(orderType);
        return getPage(queryHandler, pageIndex, pageSize);
    }

    /**
     * @param visitDate
     * @return results page
     */
    @SuppressWarnings("unchecked")
    public List<LogVisitDay> getDayList(Date visitDate) {
        QueryHandler queryHandler = getQueryHandler(
                "select new LogVisitDay(bean.id.siteId,bean.id.visitDate,sum(bean.pv),count(distinct bean.id.sessionId),count(distinct bean.ip)) from LogVisitSession bean");
        queryHandler.condition("bean.id.visitDate = :visitDate").setParameter("visitDate", DateUtils.truncate(visitDate, Calendar.DAY_OF_MONTH));
        queryHandler.group("bean.id.siteId");
        queryHandler.group("bean.id.visitDate");
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
                queryHandler.condition("bean.id.visitDate <= :visitDate").setParameter("visitDate", begintime);
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
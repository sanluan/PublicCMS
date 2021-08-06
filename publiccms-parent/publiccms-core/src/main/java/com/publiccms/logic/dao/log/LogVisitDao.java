package com.publiccms.logic.dao.log;

import java.util.Calendar;
// Generated 2021-1-14 22:43:59 by com.publiccms.common.generator.SourceGenerator
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.time.DateUtils;
import org.springframework.stereotype.Repository;

import com.publiccms.common.base.BaseDao;
import com.publiccms.common.handler.PageHandler;
import com.publiccms.common.handler.QueryHandler;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.entities.log.LogVisit;
import com.publiccms.entities.log.LogVisitDay;
import com.publiccms.entities.log.LogVisitItem;
import com.publiccms.entities.log.LogVisitSession;
import com.publiccms.entities.log.LogVisitUrl;

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
        queryHandler.order("bean.createDate").append(orderType);
        return getPage(queryHandler, pageIndex, pageSize);
    }

    /**
     * @param siteId
     * @param visitDate
     * @param itemType
     * @param itemId
     * @return results page
     */
    @SuppressWarnings("unchecked")
    public List<LogVisitItem> getItemList(Short siteId, Date visitDate, String itemType, String itemId) {
        QueryHandler queryHandler = getQueryHandler(
                "select new LogVisitItem(bean.siteId,bean.visitDate,bean.itemType,bean.itemId,count(*),count(distinct bean.sessionId),count(distinct bean.ip)) from LogVisit bean");
        if (null != siteId) {
            queryHandler.condition("bean.siteId = :siteId").setParameter("siteId", siteId);
        }
        queryHandler.condition("bean.visitDate = :visitDate").setParameter("visitDate",
                DateUtils.truncate(visitDate, Calendar.DAY_OF_MONTH));
        if (CommonUtils.notEmpty(itemType)) {
            queryHandler.condition("bean.itemType = :itemType").setParameter("itemType", itemType);
        } else {
            queryHandler.condition("bean.itemId is not null");
        }
        if (CommonUtils.notEmpty(itemId)) {
            queryHandler.condition("bean.itemId = :itemId").setParameter("itemId", itemId);
        } else {
            queryHandler.condition("bean.itemId is not null");
        }
        queryHandler.group("bean.siteId");
        queryHandler.group("bean.visitDate");
        queryHandler.group("bean.itemType");
        queryHandler.group("bean.itemId");
        queryHandler.order("count(*) desc");
        return (List<LogVisitItem>) getList(queryHandler);
    }

    /**
     * @param siteId
     * @param visitDate
     * @return results page
     */
    @SuppressWarnings("unchecked")
    public List<LogVisitUrl> getUrlList(Short siteId, Date visitDate) {
        QueryHandler queryHandler = getQueryHandler(
                "select new LogVisitUrl(bean.siteId,bean.visitDate,bean.url,count(*),count(distinct bean.sessionId),count(distinct bean.ip)) from LogVisit bean");
        if (null != siteId) {
            queryHandler.condition("bean.siteId = :siteId").setParameter("siteId", siteId);
        }
        queryHandler.condition("bean.visitDate = :visitDate").setParameter("visitDate",
                DateUtils.truncate(visitDate, Calendar.DAY_OF_MONTH));
        queryHandler.group("bean.siteId");
        queryHandler.group("bean.visitDate");
        queryHandler.group("bean.url");
        queryHandler.order("count(*) desc");
        return (List<LogVisitUrl>) getList(queryHandler);
    }

    /**
     * @param siteId
     * @param visitDate
     * @param visitHour
     * @return results page
     */
    @SuppressWarnings("unchecked")
    public List<LogVisitDay> getHourList(Short siteId, Date visitDate, byte visitHour) {
        QueryHandler queryHandler = getQueryHandler(
                "select new LogVisitDay(bean.siteId,bean.visitDate,bean.visitHour,count(*),count(distinct bean.sessionId),count(distinct bean.ip)) from LogVisit bean");
        if (null != siteId) {
            queryHandler.condition("bean.siteId = :siteId").setParameter("siteId", siteId);
        }
        queryHandler.condition("bean.visitDate = :visitDate").setParameter("visitDate",
                DateUtils.truncate(visitDate, Calendar.DAY_OF_MONTH));
        queryHandler.condition("bean.visitHour = :visitHour").setParameter("visitHour", visitHour);
        queryHandler.group("bean.siteId");
        queryHandler.group("bean.visitDate");
        queryHandler.group("bean.visitHour");
        return (List<LogVisitDay>) getList(queryHandler);
    }

    /**
     * @param siteId
     * @param startCreateDate
     * @param endCreateDate
     * @return results page
     */
    @SuppressWarnings("unchecked")
    public List<LogVisitSession> getSessionList(Short siteId, Date startCreateDate, Date endCreateDate) {
        QueryHandler queryHandler = getQueryHandler(
                "select new LogVisitSession(bean.siteId,bean.sessionId,bean.visitDate,max(bean.createDate), min(bean.createDate), bean.ip, count(*)) from LogVisit bean");
        if (null != siteId) {
            queryHandler.condition("bean.siteId = :siteId").setParameter("siteId", siteId);
        }
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
        if (CommonUtils.notEmpty(entity.getTitle()) && entity.getTitle().length() > 255) {
            entity.setTitle(entity.getTitle().substring(0, 255));
        }
        if (CommonUtils.notEmpty(entity.getUserAgent()) && entity.getUserAgent().length() > 500) {
            entity.setUserAgent(entity.getUserAgent().substring(0, 500));
        }
        if (CommonUtils.notEmpty(entity.getItemType()) && entity.getItemType().length() > 50) {
            entity.setItemType(entity.getItemType().substring(0, 50));
        }
        if (CommonUtils.notEmpty(entity.getItemId()) && entity.getItemId().length() > 50) {
            entity.setItemId(entity.getItemId().substring(0, 50));
        }
        return entity;
    }

}
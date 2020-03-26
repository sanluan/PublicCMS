package com.publiccms.logic.dao.log;

import java.util.Date;

import org.springframework.stereotype.Repository;

import com.publiccms.common.base.BaseDao;
import com.publiccms.common.handler.PageHandler;
import com.publiccms.common.handler.QueryHandler;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.entities.log.LogTask;

/**
 *
 * LogTaskDao
 * 
 */
@Repository
public class LogTaskDao extends BaseDao<LogTask> {

    /**
     * @param siteId
     * @param taskId
     * @param startBegintime
     * @param endBegintime
     * @param success
     * @param orderType
     * @param pageIndex
     * @param pageSize
     * @return results page
     */
    public PageHandler getPage(Short siteId, Integer taskId, Date startBegintime, Date endBegintime, Boolean success,
            String orderType, Integer pageIndex, Integer pageSize) {
        QueryHandler queryHandler = getQueryHandler(
                "select new LogTask(id, siteId, taskId, begintime, endtime, success) from LogTask bean");
        if (CommonUtils.notEmpty(siteId)) {
            queryHandler.condition("bean.siteId = :siteId").setParameter("siteId", siteId);
        }
        if (CommonUtils.notEmpty(taskId)) {
            queryHandler.condition("bean.taskId = :taskId").setParameter("taskId", taskId);
        }
        if (null != startBegintime) {
            queryHandler.condition("bean.begintime > :startBegintime").setParameter("startBegintime", startBegintime);
        }
        if (null != endBegintime) {
            queryHandler.condition("bean.begintime <= :endBegintime").setParameter("endBegintime", endBegintime);
        }
        if (null != success) {
            queryHandler.condition("bean.success = :success").setParameter("success", success);
        }
        if (!ORDERTYPE_ASC.equalsIgnoreCase(orderType)) {
            orderType = ORDERTYPE_DESC;
        }
        queryHandler.order("bean.begintime ").append(orderType);
        return getPage(queryHandler, pageIndex, pageSize);
    }

    /**
     * @param siteId
     * @param begintime
     * @return number of data deleted
     */
    public int delete(Short siteId, Date begintime) {
        if (CommonUtils.notEmpty(siteId) || null != begintime) {
            QueryHandler queryHandler = getQueryHandler("delete from LogTask bean");
            if (CommonUtils.notEmpty(siteId)) {
                queryHandler.condition("bean.siteId = :siteId").setParameter("siteId", siteId);
            }
            if (null != begintime) {
                queryHandler.condition("bean.begintime <= :begintime").setParameter("begintime", begintime);
            }
            return delete(queryHandler);
        }
        return 0;
    }

    @Override
    protected LogTask init(LogTask entity) {
        if (null == entity.getBegintime()) {
            entity.setBegintime(CommonUtils.getDate());
        }
        return entity;
    }
}
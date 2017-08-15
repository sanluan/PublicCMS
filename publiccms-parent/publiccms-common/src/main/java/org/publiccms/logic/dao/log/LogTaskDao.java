package org.publiccms.logic.dao.log;

// Generated 2015-7-3 16:15:25 by com.publiccms.common.source.SourceGenerator
import static com.publiccms.common.tools.CommonUtils.empty;
import static com.publiccms.common.tools.CommonUtils.getDate;
import static com.publiccms.common.tools.CommonUtils.notEmpty;

import java.util.Date;

import org.publiccms.entities.log.LogTask;
import org.springframework.stereotype.Repository;

import com.publiccms.common.base.BaseDao;
import com.publiccms.common.handler.PageHandler;
import com.publiccms.common.handler.QueryHandler;

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
     * @return
     */
    public PageHandler getPage(Integer siteId, Integer taskId, Date startBegintime, Date endBegintime, Boolean success,
            String orderType, Integer pageIndex, Integer pageSize) {
        QueryHandler queryHandler = getQueryHandler(
                "select new LogTask(id, siteId, taskId, begintime, endtime, success) from LogTask bean");
        if (notEmpty(siteId)) {
            queryHandler.condition("bean.siteId = :siteId").setParameter("siteId", siteId);
        }
        if (notEmpty(taskId)) {
            queryHandler.condition("bean.taskId = :taskId").setParameter("taskId", taskId);
        }
        if (notEmpty(startBegintime)) {
            queryHandler.condition("bean.begintime > :startBegintime").setParameter("startBegintime", startBegintime);
        }
        if (notEmpty(endBegintime)) {
            queryHandler.condition("bean.begintime <= :endBegintime").setParameter("endBegintime", endBegintime);
        }
        if (notEmpty(success)) {
            queryHandler.condition("bean.success = :success").setParameter("success", success);
        }
        if (!ORDERTYPE_ASC.equalsIgnoreCase(orderType)) {
            orderType = ORDERTYPE_DESC;
        }
        queryHandler.order("bean.begintime " + orderType);
        return getPage(queryHandler, pageIndex, pageSize);
    }

    /**
     * @param siteId
     * @param begintime
     * @return
     */
    public int delete(Integer siteId, Date begintime) {
        if (notEmpty(siteId) || notEmpty(begintime)) {
            QueryHandler queryHandler = getDeleteQueryHandler("from LogTask bean");
            if (notEmpty(siteId)) {
                queryHandler.condition("bean.siteId = :siteId").setParameter("siteId", siteId);
            }
            if (notEmpty(begintime)) {
                queryHandler.condition("bean.begintime <= :begintime").setParameter("begintime", begintime);
            }
            return delete(queryHandler);
        }
        return 0;
    }

    @Override
    protected LogTask init(LogTask entity) {
        if (empty(entity.getBegintime())) {
            entity.setBegintime(getDate());
        }
        return entity;
    }
}
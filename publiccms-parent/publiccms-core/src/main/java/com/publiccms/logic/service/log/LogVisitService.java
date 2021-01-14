package com.publiccms.logic.service.log;

// Generated 2021-1-14 22:43:59 by com.publiccms.common.generator.SourceGenerator
import java.util.Date;
import java.util.List;
import java.util.concurrent.BlockingQueue;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.publiccms.common.base.BaseService;
import com.publiccms.common.handler.PageHandler;
import com.publiccms.entities.log.LogVisit;
import com.publiccms.entities.log.LogVisitDay;
import com.publiccms.entities.log.LogVisitSession;
import com.publiccms.logic.dao.log.LogVisitDao;

/**
 *
 * LogVisitService
 * 
 */
@Service
@Transactional
public class LogVisitService extends BaseService<LogVisit> {

    /**
     * @param siteId
     * @param sessionId
     * @param startVisitDate
     * @param endVisitDate
     * @param visitHour
     * @param startCreateDate
     * @param endCreateDate
     * @param orderType
     * @param pageIndex
     * @param pageSize
     * @return results page
     */
    @Transactional(readOnly = true)
    public PageHandler getPage(Short siteId, String sessionId, Date startCreateDate, Date endCreateDate, String orderType,
            Integer pageIndex, Integer pageSize) {
        return dao.getPage(siteId, sessionId, startCreateDate, endCreateDate, orderType, pageIndex, pageSize);
    }

    /**
     * @param startCreateDate
     * @param endCreateDate
     * @return results page
     */
    @Transactional(readOnly = true)
    public List<LogVisitSession> getSessionList(Date startCreateDate, Date endCreateDate) {
        return dao.getSessionList(startCreateDate, endCreateDate);
    }

    /**
     * @param sessionId
     * @param visitDate
     * @param visitHour
     * @return results page
     */
    @Transactional(readOnly = true)
    public List<LogVisitDay> getHourList(Date visitDate, Byte visitHour) {
        return dao.getHourList(visitDate, visitHour);
    }

    /**
     * @param begintime
     * @return number of data deleted
     */
    public int delete(Date begintime) {
        return dao.delete(begintime);
    }

    /**
     * @param blockingQueue
     */
    public void save(BlockingQueue<LogVisit> blockingQueue) {
        LogVisit entity = null;
        while (null != (entity = blockingQueue.poll())) {
            save(entity);
        }
    }

    @Autowired
    private LogVisitDao dao;

}
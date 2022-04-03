package com.publiccms.logic.service.visit;

// Generated 2021-1-14 22:43:59 by com.publiccms.common.generator.SourceGenerator
import java.util.Date;
import java.util.List;
import java.util.concurrent.BlockingQueue;

import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;

import com.publiccms.common.base.BaseService;
import com.publiccms.common.handler.PageHandler;
import com.publiccms.entities.visit.VisitHistory;
import com.publiccms.entities.visit.VisitDay;
import com.publiccms.entities.visit.VisitItem;
import com.publiccms.entities.visit.VisitSession;
import com.publiccms.entities.visit.VisitUrl;
import com.publiccms.logic.dao.visit.VisitHistoryDao;

/**
 *
 * VisitHistoryService
 * 
 */
@Service
@Transactional
public class VisitHistoryService extends BaseService<VisitHistory> {

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
    @Transactional
    public PageHandler getPage(Short siteId, String sessionId, Date startCreateDate, Date endCreateDate, String orderType,
            Integer pageIndex, Integer pageSize) {
        return dao.getPage(siteId, sessionId, startCreateDate, endCreateDate, orderType, pageIndex, pageSize);
    }

    /**
     * @param siteId
     * @param startCreateDate
     * @param endCreateDate
     * @return results page
     */
    @Transactional
    public List<VisitSession> getSessionList(Short siteId, Date startCreateDate, Date endCreateDate) {
        return dao.getSessionList(siteId, startCreateDate, endCreateDate);
    }

    /**
     * @param siteId
     * @param visitDate
     * @param visitHour
     * @return results page
     */
    @Transactional
    public List<VisitDay> getHourList(Short siteId, Date visitDate, Byte visitHour) {
        return dao.getHourList(siteId, visitDate, visitHour);
    }

    /**
     * @param siteId
     * @param visitDate
     * @param itemType
     * @param itemId
     * @return results page
     */
    @Transactional
    public List<VisitItem> getItemList(Short siteId, Date visitDate, String itemType, String itemId) {
        return dao.getItemList(siteId, visitDate, itemType, itemId);
    }

    /**
     * @param siteId
     * @param visitDate
     * @param visitHour
     * @return results page
     */
    @Transactional
    public List<VisitUrl> getUrlList(Short siteId, Date visitDate) {
        return dao.getUrlList(siteId, visitDate);
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
    public void save(BlockingQueue<VisitHistory> blockingQueue) {
        VisitHistory entity = null;
        while (null != (entity = blockingQueue.poll())) {
            save(entity);
        }
    }

    @Resource
    private VisitHistoryDao dao;

}
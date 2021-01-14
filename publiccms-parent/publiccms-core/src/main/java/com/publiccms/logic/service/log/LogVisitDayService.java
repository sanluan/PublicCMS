package com.publiccms.logic.service.log;

import java.util.Date;

// Generated 2021-1-14 22:44:12 by com.publiccms.common.generator.SourceGenerator

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.publiccms.entities.log.LogVisitDay;
import com.publiccms.logic.dao.log.LogVisitDayDao;
import com.publiccms.common.base.BaseService;
import com.publiccms.common.handler.PageHandler;

/**
 *
 * LogVisitDayService
 * 
 */
@Service
@Transactional
public class LogVisitDayService extends BaseService<LogVisitDay> {

    /**
     * @param siteId
     * @param visitDate
     * @param visitHour
     * @param pageIndex
     * @param pageSize
     * @return results page
     */
    @Transactional(readOnly = true)
    public PageHandler getPage(short siteId, Date visitDate, Byte visitHour, Integer pageIndex, Integer pageSize) {
        return dao.getPage(siteId, visitDate, visitHour, pageIndex, pageSize);
    }

    /**
     * @param begintime
     * @return number of data deleted
     */
    public int delete(Date begintime) {
        return dao.delete(begintime);
    }

    @Autowired
    private LogVisitDayDao dao;

}
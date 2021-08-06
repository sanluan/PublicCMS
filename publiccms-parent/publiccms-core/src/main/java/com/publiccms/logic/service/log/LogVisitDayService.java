package com.publiccms.logic.service.log;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.time.DateUtils;

// Generated 2021-1-14 22:44:12 by com.publiccms.common.generator.SourceGenerator

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.publiccms.common.base.BaseService;
import com.publiccms.common.handler.PageHandler;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.entities.log.LogVisitDay;
import com.publiccms.logic.dao.log.LogVisitDayDao;

/**
 *
 * LogVisitDayService
 * 
 */
@Service
@Transactional
public class LogVisitDayService extends BaseService<LogVisitDay> {
    @Autowired
    private LogVisitSessionService logVisitSessionService;
    @Autowired
    private LogVisitService logVisitService;

    /**
     * @param siteId
     * @param startVisitDate
     * @param endVisitDate
     * @param hourAnalytics
     * @param pageIndex
     * @param pageSize
     * @return results page
     */
    @SuppressWarnings("unchecked")
    @Transactional(readOnly = true)
    public PageHandler getPage(short siteId, Date startVisitDate, Date endVisitDate, boolean hourAnalytics, Integer pageIndex,
            Integer pageSize) {
        PageHandler page = dao.getPage(siteId, startVisitDate, endVisitDate, hourAnalytics, pageIndex, pageSize);
        Date now = CommonUtils.getMinuteDate();
        if ((null == pageIndex || 1 == pageIndex) && (null == endVisitDate || DateUtils.isSameDay(now, endVisitDate))) {
            if (hourAnalytics) {
                Calendar c = Calendar.getInstance();
                ((List<LogVisitDay>) page.getList()).addAll(0,
                        logVisitService.getHourList(siteId, now, (byte) c.get(Calendar.HOUR_OF_DAY)));
            } else {
                ((List<LogVisitDay>) page.getList()).addAll(0, logVisitSessionService.getDayList(siteId, now));
            }
        }
        return page;
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
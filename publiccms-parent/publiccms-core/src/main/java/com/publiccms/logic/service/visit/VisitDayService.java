package com.publiccms.logic.service.visit;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.time.DateUtils;

// Generated 2021-1-14 22:44:12 by com.publiccms.common.generator.SourceGenerator

import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.publiccms.common.base.BaseService;
import com.publiccms.common.handler.PageHandler;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.entities.visit.VisitDay;
import com.publiccms.logic.dao.visit.VisitDayDao;

/**
 *
 * VisitDayService
 * 
 */
@Service
@Transactional
public class VisitDayService extends BaseService<VisitDay> {
    @Resource
    private VisitSessionService visitSessionService;
    @Resource
    private VisitHistoryService visitHistoryService;

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
                ((List<VisitDay>) page.getList()).addAll(0,
                        visitHistoryService.getHourList(siteId, now, (byte) c.get(Calendar.HOUR_OF_DAY)));
            } else {
                ((List<VisitDay>) page.getList()).addAll(0, visitSessionService.getDayList(siteId, now));
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

    @Resource
    private VisitDayDao dao;

}
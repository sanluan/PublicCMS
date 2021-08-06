package com.publiccms.logic.service.log;

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
import com.publiccms.entities.log.LogVisitItem;
import com.publiccms.logic.dao.log.LogVisitItemDao;

/**
 *
 * LogVisitItemService
 * 
 */
@Service
@Transactional
public class LogVisitItemService extends BaseService<LogVisitItem> {
    @Autowired
    private LogVisitService logVisitService;

    /**
     * @param siteId
     * @param startVisitDate
     * @param endVisitDate
     * @param itemType
     * @param itemId
     * @param pageIndex
     * @param pageSize
     * @return results page
     */
    @SuppressWarnings("unchecked")
    @Transactional(readOnly = true)
    public PageHandler getPage(short siteId, Date startVisitDate, Date endVisitDate, String itemType, String itemId,
            Integer pageIndex, Integer pageSize) {
        PageHandler page = dao.getPage(siteId, startVisitDate, endVisitDate, itemType, itemId, pageIndex, pageSize);
        Date now = CommonUtils.getMinuteDate();
        if ((null == pageIndex || 1 == pageIndex) && (null == endVisitDate || DateUtils.isSameDay(now, endVisitDate))) {
            ((List<LogVisitItem>) page.getList()).addAll(0, logVisitService.getItemList(siteId, now, itemType, itemId));
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
    private LogVisitItemDao dao;

}
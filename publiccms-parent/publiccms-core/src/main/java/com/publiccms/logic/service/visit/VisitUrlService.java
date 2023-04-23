package com.publiccms.logic.service.visit;

import java.util.Date;
import java.util.List;

// Generated 2021-1-14 22:44:12 by com.publiccms.common.generator.SourceGenerator

import jakarta.annotation.Resource;

import org.apache.commons.lang3.time.DateUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.publiccms.common.base.BaseService;
import com.publiccms.common.handler.PageHandler;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.entities.visit.VisitUrl;
import com.publiccms.logic.dao.visit.VisitUrlDao;

/**
 *
 * VisitUrlService
 * 
 */
@Service
@Transactional
public class VisitUrlService extends BaseService<VisitUrl> {
    @Resource
    private VisitHistoryService visitHistoryService;

    /**
     * @param siteId
     * @param url
     * @param startVisitDate
     * @param endVisitDate
     * @param pageIndex
     * @param pageSize
     * @return results page
     */
    @SuppressWarnings("unchecked")
    @Transactional(readOnly = true)
    public PageHandler getPage(short siteId, String url, Date startVisitDate, Date endVisitDate, Integer pageIndex,
            Integer pageSize) {
        PageHandler page = dao.getPage(siteId, url, startVisitDate, endVisitDate, pageIndex, pageSize);
        Date now = CommonUtils.getMinuteDate();
        if (null!= page.getList() && (null == pageIndex || 1 == pageIndex) && (null == endVisitDate || DateUtils.isSameDay(now, endVisitDate))) {
            ((List<VisitUrl>) page.getList()).addAll(0, visitHistoryService.getUrlList(siteId, url, now));
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
    private VisitUrlDao dao;

}
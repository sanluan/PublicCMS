package com.publiccms.logic.component.task;

import java.util.Date;

import org.apache.commons.lang3.time.DateUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import jakarta.annotation.Resource;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.publiccms.common.constants.CmsVersion;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.logic.component.cache.CacheComponent;
import com.publiccms.logic.component.site.LockComponent;
import com.publiccms.logic.component.site.StatisticsComponent;
import com.publiccms.logic.component.site.VisitComponent;
import com.publiccms.logic.service.log.LogLoginService;
import com.publiccms.logic.service.log.LogOperateService;
import com.publiccms.logic.service.log.LogTaskService;
import com.publiccms.logic.service.sys.SysAppTokenService;
import com.publiccms.logic.service.sys.SysEmailTokenService;
import com.publiccms.logic.service.sys.SysUserTokenService;
import com.publiccms.logic.service.visit.VisitDayService;
import com.publiccms.logic.service.visit.VisitHistoryService;
import com.publiccms.logic.service.visit.VisitItemService;
import com.publiccms.logic.service.visit.VisitSessionService;
import com.publiccms.logic.service.visit.VisitUrlService;

/**
 *
 * ScheduledTaskComponent
 * 
 */
@Component
public class ScheduledTaskComponent {
    protected final Log log = LogFactory.getLog(getClass());

    @Resource
    private SysAppTokenService appTokenService;
    @Resource
    private SysEmailTokenService emailTokenService;
    @Resource
    private SysUserTokenService userTokenService;
    @Resource
    private VisitHistoryService visitHistoryService;
    @Resource
    private VisitSessionService visitSessionService;
    @Resource
    private VisitDayService visitDayService;
    @Resource
    private VisitItemService visitItemService;
    @Resource
    private VisitUrlService visitUrlService;
    @Resource
    private LogLoginService logLoginService;
    @Resource
    private LogOperateService logOperateService;
    @Resource
    private LogTaskService logTaskService;
    @Resource
    private CacheComponent cacheComponent;
    @Resource
    private VisitComponent visitComponent;
    @Resource
    private StatisticsComponent statisticsComponent;
    @Resource
    private LockComponent lockComponent;

    /**
     * 10分钟清理过期token
     */
    @Scheduled(fixedDelay = 10 * 60 * 1000L)
    public void clearAppToken() {
        if (CmsVersion.isMaster()) {
            Date now = CommonUtils.getDate();
            appTokenService.delete(now);
            emailTokenService.delete(now);
            userTokenService.delete(now);
            lockComponent.clear();
        }
    }

    /**
     * 10秒种清理访问日志
     */
    @Scheduled(fixedDelay = 10 * 1000L)
    public void clearVisitLog() {
        if (CmsVersion.isScheduled()) {
            synchronized (visitComponent) {
                visitComponent.clear();
            }
        }
    }

    /**
     * 每分钟汇总访问数据
     */
    @Scheduled(cron = "10 * * * * ?")
    public void dealLastMinuteVisitLog() {
        if (CmsVersion.isMaster()) {
            synchronized (visitComponent) {
                visitComponent.dealLastMinuteVisitLog();
            }
        }
    }

    /**
     * 每小时汇总访问数据
     */
    @Scheduled(cron = "0 1 * * * ?")
    public void dealLastHourVisitLog() {
        if (CmsVersion.isMaster()) {
            synchronized (visitComponent) {
                visitComponent.dealLastHourVisitLog();
            }
        }
    }

    /**
     * 每天汇总访问数据
     */
    @Scheduled(cron = "0 10 0 * * ?")
    public void dealLastDayVisitLog() {
        if (CmsVersion.isMaster()) {
            synchronized (visitComponent) {
                visitComponent.dealLastDayVisitLog();
                visitComponent.dealLastDayItemVisitLog();
                visitComponent.dealLastDayUrlVisitLog();
            }
        }
    }

    /**
     * 每5分钟清理统计缓存
     */
    @Scheduled(fixedDelay = 5 * 60 * 1000L)
    public void clearStatistics() {
        if (CmsVersion.isScheduled()) {
            synchronized (cacheComponent) {
                statisticsComponent.clear();
            }
        }
    }

    /**
     * 每6个小时清理缓存
     */
    @Scheduled(cron = "0 30 0/6 * * ?")
    public void clearCache() {
        if (CmsVersion.isScheduled()) {
            synchronized (cacheComponent) {
                cacheComponent.clear();
            }
        }
    }

    /**
     * 每月1号凌晨清理三年以前的日志
     */
    @Scheduled(cron = "0 0 0 1 * ?")
    public void clearLog() {
        if (CmsVersion.isMaster()) {
            Date date = DateUtils.addYears(CommonUtils.getDate(), -3);
            logLoginService.delete(null, date);
            logOperateService.delete(null, date);
            visitDayService.delete(date);
            date = DateUtils.addMonths(CommonUtils.getDate(), -12);
            logTaskService.delete(null, date);
            date = DateUtils.addMonths(CommonUtils.getDate(), -3);
            visitSessionService.delete(date);
            visitHistoryService.delete(date);
            visitItemService.delete(date);
            visitUrlService.delete(date);
        }
    }
}

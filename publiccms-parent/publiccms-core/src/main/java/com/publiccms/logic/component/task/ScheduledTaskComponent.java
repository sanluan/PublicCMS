package com.publiccms.logic.component.task;

import java.util.Date;

import org.apache.commons.lang3.time.DateUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.publiccms.common.constants.CmsVersion;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.logic.component.cache.CacheComponent;
import com.publiccms.logic.component.site.DatasourceComponent;
import com.publiccms.logic.component.site.StatisticsComponent;
import com.publiccms.logic.component.site.VisitComponent;
import com.publiccms.logic.service.log.LogLoginService;
import com.publiccms.logic.service.log.LogOperateService;
import com.publiccms.logic.service.log.LogTaskService;
import com.publiccms.logic.service.log.LogVisitDayService;
import com.publiccms.logic.service.log.LogVisitItemService;
import com.publiccms.logic.service.log.LogVisitService;
import com.publiccms.logic.service.log.LogVisitSessionService;
import com.publiccms.logic.service.log.LogVisitUrlService;
import com.publiccms.logic.service.sys.SysAppTokenService;
import com.publiccms.logic.service.sys.SysEmailTokenService;
import com.publiccms.logic.service.sys.SysUserTokenService;

/**
 *
 * ScheduledTaskComponent
 * 
 */
@Component
public class ScheduledTaskComponent {
    protected final Log log = LogFactory.getLog(getClass());

    @Autowired
    private SysAppTokenService appTokenService;
    @Autowired
    private SysEmailTokenService emailTokenService;
    @Autowired
    private SysUserTokenService userTokenService;
    @Autowired
    private LogVisitService logVisitService;
    @Autowired
    private LogVisitSessionService logVisitSessionService;
    @Autowired
    private LogVisitDayService logVisitDayService;
    @Autowired
    private LogVisitItemService logVisitItemService;
    @Autowired
    private LogVisitUrlService logVisitUrlService;
    @Autowired
    private LogLoginService logLoginService;
    @Autowired
    private LogOperateService logOperateService;
    @Autowired
    private LogTaskService logTaskService;
    @Autowired
    private CacheComponent cacheComponent;
    @Autowired
    private VisitComponent visitComponent;
    @Autowired
    private StatisticsComponent statisticsComponent;
    @Autowired
    private DatasourceComponent datasourceComponent;

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
     * 30分钟清理已禁用的数据源
     */
    @Scheduled(cron = "01 0/30 * * * ?")
    public void cleanDisabledDatasource() {
        if (CmsVersion.isScheduled()) {
            synchronized (datasourceComponent) {
                datasourceComponent.cleanDisabledDatasource(30);
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
     * 每月1号凌晨清理两年以前的日志
     */
    @Scheduled(cron = "0 0 0 1 * ?")
    public void clearLog() {
        if (CmsVersion.isMaster()) {
            Date date = DateUtils.addYears(CommonUtils.getDate(), -2);
            logLoginService.delete(null, date);
            logOperateService.delete(null, date);
            logTaskService.delete(null, date);
            logVisitDayService.delete(date);
            date = DateUtils.addMonths(CommonUtils.getDate(), -3);
            logVisitSessionService.delete(date);
            logVisitService.delete(date);
            logVisitItemService.delete(date);
            logVisitUrlService.delete(date);
        }
    }
}

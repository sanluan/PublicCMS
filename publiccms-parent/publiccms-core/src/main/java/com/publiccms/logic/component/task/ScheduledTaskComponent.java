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
import com.publiccms.logic.component.site.StatisticsComponent;
import com.publiccms.logic.service.log.LogLoginService;
import com.publiccms.logic.service.log.LogOperateService;
import com.publiccms.logic.service.log.LogTaskService;
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
    private LogLoginService logLoginService;
    @Autowired
    private LogOperateService logOperateService;
    @Autowired
    private LogTaskService logTaskService;
    @Autowired
    private CacheComponent cacheComponent;
    @Autowired
    private StatisticsComponent statisticsComponent;

    /**
     * 每分钟清理半小时前的token
     */
    @Scheduled(fixedDelay = 60 * 1000L)
    public void clearAppToken() {
        if (CmsVersion.isInitialized() && CmsVersion.isMaster()) {
            Date date = DateUtils.addMinutes(CommonUtils.getDate(), -30);
            appTokenService.delete(date);
            emailTokenService.delete(date);
        }
    }

    /**
     * 每5分钟清理统计缓存
     */
    @Scheduled(fixedDelay = 5 * 60 * 1000L)
    public void clearStatistics() {
        if (CmsVersion.isInitialized()) {
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
        if (CmsVersion.isInitialized()) {
            synchronized (cacheComponent) {
                cacheComponent.clear();
            }
            if (CmsVersion.isMaster()) {
                // 清理3个月前的Token
                userTokenService.delete(DateUtils.addMonths(CommonUtils.getDate(), -3));
            }
        }
    }

    /**
     * 每月1号凌晨清理两年以前的日志
     */
    @Scheduled(cron = "0 0 0 1 * ?")
    public void clearLog() {
        if (CmsVersion.isInitialized() && CmsVersion.isMaster()) {
            Date date = DateUtils.addYears(CommonUtils.getDate(), -2);
            logLoginService.delete(null, date);
            logOperateService.delete(null, date);
            logTaskService.delete(null, date);
        }
    }
}

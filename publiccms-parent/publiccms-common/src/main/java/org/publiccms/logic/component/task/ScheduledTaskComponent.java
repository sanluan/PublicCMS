package org.publiccms.logic.component.task;
import static com.publiccms.common.tools.CommonUtils.getDate;
import static org.apache.commons.lang3.time.DateUtils.addMinutes;
import static org.apache.commons.lang3.time.DateUtils.addMonths;
import static org.apache.commons.lang3.time.DateUtils.addYears;
import static org.apache.commons.logging.LogFactory.getLog;

import java.util.Date;

import org.apache.commons.logging.Log;
import org.publiccms.common.constants.CmsVersion;
import org.publiccms.logic.component.cache.CacheComponent;
import org.publiccms.logic.service.log.LogLoginService;
import org.publiccms.logic.service.log.LogOperateService;
import org.publiccms.logic.service.log.LogTaskService;
import org.publiccms.logic.service.sys.SysAppTokenService;
import org.publiccms.logic.service.sys.SysEmailTokenService;
import org.publiccms.logic.service.sys.SysUserTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 *
 * ScheduledTaskComponent
 * 
 */
@Component
public class ScheduledTaskComponent {
    protected final Log log = getLog(getClass());
    
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

    /**
     * 每分钟清理半小时前的token
     */
    @Scheduled(fixedDelay = 60 * 1000L)
    public void clearAppToken() {
        if (CmsVersion.isInitialized() && CmsVersion.isMaster()) {
            Date date = addMinutes(getDate(), -30);
            appTokenService.delete(date);
            emailTokenService.delete(date);
        }
    }

    /**
     * 每6个小时清理缓存
     */
    @Scheduled(cron = "0 30 0/6 * * ?")
    public void clearCache() {
        if (CmsVersion.isInitialized()) {
            cacheComponent.clear();
            if (CmsVersion.isMaster()) {
                // 清理3个月前的Token
                userTokenService.delete(addMonths(getDate(), -3));
            }
        }
    }

    /**
     * 每月1号凌晨清理两年以前的日志
     */
    @Scheduled(cron = "0 0 0 1 * ?")
    public void clearLog() {
        if (CmsVersion.isInitialized() && CmsVersion.isMaster()) {
            Date date = addYears(getDate(), -2);
            logLoginService.delete(null, date);
            logOperateService.delete(null, date);
            logTaskService.delete(null, date);
        }
    }
}

package com.publiccms.logic.component;

import static org.apache.commons.lang3.time.DateUtils.addMinutes;
import static org.apache.commons.lang3.time.DateUtils.addYears;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.publiccms.common.constants.CmsVersion;
import com.publiccms.logic.service.log.LogLoginService;
import com.publiccms.logic.service.log.LogOperateService;
import com.publiccms.logic.service.log.LogTaskService;
import com.publiccms.logic.service.log.LogUploadService;
import com.publiccms.logic.service.sys.SysAppTokenService;
import com.publiccms.logic.service.sys.SysEmailTokenService;
import com.sanluan.common.base.Base;

@Component
public class ScheduledTaskComponent extends Base {
    @Autowired
    private SysAppTokenService appTokenService;
    @Autowired
    private SysEmailTokenService sysEmailTokenService;
    @Autowired
    private LogLoginService logLoginService;
    @Autowired
    private LogOperateService logOperateService;
    @Autowired
    private LogTaskService logTaskService;
    @Autowired
    private LogUploadService logUploadService;
    @Autowired
    private CacheComponent cacheComponent;

    /**
     * 每分钟清理半小时前的token
     */
    @Scheduled(fixedDelay = 60 * 1000L)
    public void clearAppToken() {
        if (CmsVersion.isMaster()) {
            Date date = addMinutes(getDate(), -30);
            appTokenService.delete(date);
            sysEmailTokenService.delete(date);
        }
    }

    /**
     * 每天凌晨清理缓存
     */
    @Scheduled(cron = "0 0 0  * * ?")
    public void clearCache() {
        cacheComponent.clear();
    }

    /**
     * 每月1号凌晨清理两年以前的日志
     */
    @Scheduled(cron = "0 0 0 1 * ?")
    public void clearLog() {
        if (CmsVersion.isMaster()) {
            Date date = addYears(getDate(), -2);
            logLoginService.delete(null, date);
            logOperateService.delete(null, date);
            logTaskService.delete(null, date);
            logUploadService.delete(null, date);
        }
    }
}

package com.publiccms.logic.component.site;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.publiccms.common.api.Cache;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.common.tools.VerificationUtils;
import com.publiccms.entities.log.LogVisit;
import com.publiccms.entities.log.LogVisitDay;
import com.publiccms.entities.log.LogVisitItem;
import com.publiccms.entities.log.LogVisitSession;
import com.publiccms.entities.log.LogVisitUrl;
import com.publiccms.logic.service.log.LogVisitDayService;
import com.publiccms.logic.service.log.LogVisitItemService;
import com.publiccms.logic.service.log.LogVisitService;
import com.publiccms.logic.service.log.LogVisitSessionService;
import com.publiccms.logic.service.log.LogVisitUrlService;

/**
 *
 * VisitComponent
 * 
 */
@Component
public class VisitComponent implements Cache {
    private BlockingQueue<LogVisit> blockingQueue = new LinkedBlockingQueue<>();

    @Autowired
    private LogVisitDayService logVisitDayService;
    @Autowired
    private LogVisitItemService logVisitItemService;
    @Autowired
    private LogVisitUrlService logVisitUrlService;
    @Autowired
    private LogVisitService logVisitService;
    @Autowired
    private LogVisitSessionService logVisitSessionService;

    public void dealLastMinuteVisitLog() {
        Date now = CommonUtils.getMinuteDate();
        List<LogVisitSession> entityList = logVisitService.getSessionList(null, DateUtils.addMinutes(now, -2),
                DateUtils.addMinutes(now, -1));
        logVisitSessionService.save(entityList);
    }

    public void dealLastHourVisitLog() {
        Calendar now = Calendar.getInstance();
        now.add(Calendar.HOUR_OF_DAY, -1);
        List<LogVisitDay> entityList = logVisitService.getHourList(null, now.getTime(), (byte) now.get(Calendar.HOUR_OF_DAY));
        logVisitDayService.save(entityList);
    }

    public void dealLastDayItemVisitLog() {
        Calendar now = Calendar.getInstance();
        now.add(Calendar.HOUR_OF_DAY, -1);
        List<LogVisitItem> entityList = logVisitService.getItemList(null, now.getTime(), null, null);
        logVisitItemService.save(entityList);
    }

    public void dealLastDayUrlVisitLog() {
        Calendar now = Calendar.getInstance();
        now.add(Calendar.HOUR_OF_DAY, -1);
        List<LogVisitUrl> entityList = logVisitService.getUrlList(null, now.getTime());
        for (LogVisitUrl entity : entityList) {
            entity.getId().setUrlMd5(VerificationUtils.md5Encode(entity.getUrl()));
            entity.getId().setUrlSha(VerificationUtils.sha1Encode(entity.getUrl()));
        }
        logVisitUrlService.save(entityList);
    }

    public void dealLastDayVisitLog() {
        Calendar now = Calendar.getInstance();
        now.add(Calendar.DAY_OF_MONTH, -1);
        List<LogVisitDay> entityList = logVisitSessionService.getDayList(null, now.getTime());
        logVisitDayService.save(entityList);
    }

    public void add(LogVisit entity) {
        if (null != entity.getSessionId() && null != entity.getUrl() && null != entity.getIp()) {
            Calendar now = Calendar.getInstance();
            entity.setCreateDate(now.getTime());
            entity.setVisitDate(now.getTime());
            entity.setVisitHour((byte) now.get(Calendar.HOUR_OF_DAY));
            blockingQueue.offer(entity);
        }
    }

    @Override
    public void clear() {
        logVisitService.save(blockingQueue);
    }

}
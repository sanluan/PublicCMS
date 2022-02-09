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
import com.publiccms.entities.visit.VisitHistory;
import com.publiccms.entities.visit.VisitDay;
import com.publiccms.entities.visit.VisitItem;
import com.publiccms.entities.visit.VisitSession;
import com.publiccms.entities.visit.VisitUrl;
import com.publiccms.logic.service.visit.VisitDayService;
import com.publiccms.logic.service.visit.VisitItemService;
import com.publiccms.logic.service.visit.VisitHistoryService;
import com.publiccms.logic.service.visit.VisitSessionService;
import com.publiccms.logic.service.visit.VisitUrlService;

/**
 *
 * VisitComponent
 * 
 */
@Component
public class VisitComponent implements Cache {
    private BlockingQueue<VisitHistory> blockingQueue = new LinkedBlockingQueue<>();

    @Autowired
    private VisitDayService visitDayService;
    @Autowired
    private VisitItemService visitItemService;
    @Autowired
    private VisitUrlService visitUrlService;
    @Autowired
    private VisitHistoryService visitHistoryService;
    @Autowired
    private VisitSessionService visitSessionService;

    public void dealLastMinuteVisitLog() {
        Date now = CommonUtils.getMinuteDate();
        List<VisitSession> entityList = visitHistoryService.getSessionList(null, DateUtils.addMinutes(now, -2),
                DateUtils.addMinutes(now, -1));
        visitSessionService.save(entityList);
    }

    public void dealLastHourVisitLog() {
        Calendar now = Calendar.getInstance();
        now.add(Calendar.HOUR_OF_DAY, -1);
        List<VisitDay> entityList = visitHistoryService.getHourList(null, now.getTime(), (byte) now.get(Calendar.HOUR_OF_DAY));
        visitDayService.save(entityList);
    }

    public void dealLastDayItemVisitLog() {
        Calendar now = Calendar.getInstance();
        now.add(Calendar.HOUR_OF_DAY, -1);
        List<VisitItem> entityList = visitHistoryService.getItemList(null, now.getTime(), null, null);
        visitItemService.save(entityList);
    }

    public void dealLastDayUrlVisitLog() {
        Calendar now = Calendar.getInstance();
        now.add(Calendar.HOUR_OF_DAY, -1);
        List<VisitUrl> entityList = visitHistoryService.getUrlList(null, now.getTime());
        for (VisitUrl entity : entityList) {
            entity.getId().setUrlMd5(VerificationUtils.md5Encode(entity.getUrl()));
            entity.getId().setUrlSha(VerificationUtils.sha1Encode(entity.getUrl()));
        }
        visitUrlService.save(entityList);
    }

    public void dealLastDayVisitLog() {
        Calendar now = Calendar.getInstance();
        now.add(Calendar.DAY_OF_MONTH, -1);
        List<VisitDay> entityList = visitSessionService.getDayList(null, now.getTime());
        visitDayService.save(entityList);
    }

    public void add(VisitHistory entity) {
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
        visitHistoryService.save(blockingQueue);
    }

}
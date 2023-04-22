package com.publiccms.logic.component.site;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import javax.annotation.Resource;

import org.apache.commons.lang3.time.DateUtils;
import org.springframework.stereotype.Component;

import com.publiccms.common.api.Cache;
import com.publiccms.common.constants.Constants;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.common.tools.JsonUtils;
import com.publiccms.common.tools.VerificationUtils;
import com.publiccms.entities.sys.SysRecord;
import com.publiccms.entities.sys.SysRecordId;
import com.publiccms.entities.visit.VisitDay;
import com.publiccms.entities.visit.VisitHistory;
import com.publiccms.entities.visit.VisitItem;
import com.publiccms.entities.visit.VisitSession;
import com.publiccms.entities.visit.VisitUrl;
import com.publiccms.logic.service.sys.SysRecordService;
import com.publiccms.logic.service.visit.VisitDayService;
import com.publiccms.logic.service.visit.VisitHistoryService;
import com.publiccms.logic.service.visit.VisitItemService;
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

    @Resource
    private VisitDayService visitDayService;
    @Resource
    private VisitItemService visitItemService;
    @Resource
    private VisitUrlService visitUrlService;
    @Resource
    private VisitHistoryService visitHistoryService;
    @Resource
    private VisitSessionService visitSessionService;
    @Resource
    private SysRecordService recordService;

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
        List<VisitUrl> entityList = visitHistoryService.getUrlList(null, null, now.getTime());
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
        for (VisitDay entity : entityList) {
            SysRecordId recordId = new SysRecordId(entity.getId().getSiteId(), "visit");
            SysRecord recordEntity = recordService.getEntity(recordId);
            Visit visit = null;
            if (null != recordEntity) {
                try {
                    visit = Constants.objectMapper.readValue(recordEntity.getData(),
                            Constants.objectMapper.getTypeFactory().constructType(Visit.class));
                } catch (IOException | ClassCastException e) {
                    recordService.delete(recordId);
                }
            }
            if (null == visit) {
                visit = new Visit();
                visit.setUv(entity.getUv());
                visit.setPv(entity.getPv());
                visit.setIpviews(entity.getIpviews());
            } else {
                visit.setUv(entity.getUv() + visit.getUv());
                visit.setPv(entity.getPv() + visit.getPv());
                visit.setIpviews(entity.getIpviews() + visit.getIpviews());
            }
            if (null == visit || entity.getUv() > visit.getMaxUv()) {
                visit.setMaxUv(entity.getUv());
                visit.setMaxUvDate(entity.getId().getVisitDate());
            }
            if (null == visit || entity.getPv() > visit.getMaxPv()) {
                visit.setMaxPv(entity.getPv());
                visit.setMaxPvDate(entity.getId().getVisitDate());
            }
            if (null == visit || entity.getIpviews() > visit.getMaxIpviews()) {
                visit.setMaxIpviews(entity.getIpviews());
                visit.setMaxIpviewsDate(entity.getId().getVisitDate());
            }
            recordService.saveOrUpdate(recordId, JsonUtils.getString(visit));
        }
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

    public static class Visit {
        long uv;
        long pv;
        long ipviews;
        long maxUv;
        long maxPv;
        long maxIpviews;
        Date maxUvDate;
        Date maxPvDate;
        Date maxIpviewsDate;

        /**
         * @return the uv
         */
        public long getUv() {
            return uv;
        }

        /**
         * @param uv
         *            the uv to set
         */
        public void setUv(long uv) {
            this.uv = uv;
        }

        /**
         * @return the pv
         */
        public long getPv() {
            return pv;
        }

        /**
         * @param pv
         *            the pv to set
         */
        public void setPv(long pv) {
            this.pv = pv;
        }

        /**
         * @return the ipviews
         */
        public long getIpviews() {
            return ipviews;
        }

        /**
         * @param ipviews
         *            the ipviews to set
         */
        public void setIpviews(long ipviews) {
            this.ipviews = ipviews;
        }

        /**
         * @return the maxUv
         */
        public long getMaxUv() {
            return maxUv;
        }

        /**
         * @param maxUv
         *            the maxUv to set
         */
        public void setMaxUv(long maxUv) {
            this.maxUv = maxUv;
        }

        /**
         * @return the maxPv
         */
        public long getMaxPv() {
            return maxPv;
        }

        /**
         * @param maxPv
         *            the maxPv to set
         */
        public void setMaxPv(long maxPv) {
            this.maxPv = maxPv;
        }

        /**
         * @return the maxIpviews
         */
        public long getMaxIpviews() {
            return maxIpviews;
        }

        /**
         * @param maxIpviews
         *            the maxIpviews to set
         */
        public void setMaxIpviews(long maxIpviews) {
            this.maxIpviews = maxIpviews;
        }

        /**
         * @return the maxUvDate
         */
        public Date getMaxUvDate() {
            return maxUvDate;
        }

        /**
         * @param maxUvDate
         *            the maxUvDate to set
         */
        public void setMaxUvDate(Date maxUvDate) {
            this.maxUvDate = maxUvDate;
        }

        /**
         * @return the maxPvDate
         */
        public Date getMaxPvDate() {
            return maxPvDate;
        }

        /**
         * @param maxPvDate
         *            the maxPvDate to set
         */
        public void setMaxPvDate(Date maxPvDate) {
            this.maxPvDate = maxPvDate;
        }

        /**
         * @return the maxIpviewsDate
         */
        public Date getMaxIpviewsDate() {
            return maxIpviewsDate;
        }

        /**
         * @param maxIpviewsDate
         *            the maxIpviewsDate to set
         */
        public void setMaxIpviewsDate(Date maxIpviewsDate) {
            this.maxIpviewsDate = maxIpviewsDate;
        }
    }
}
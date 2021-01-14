package com.publiccms.logic.service.log;

import java.util.Date;
import java.util.List;

import org.springframework.beans.BeanUtils;

// Generated 2021-1-14 22:44:06 by com.publiccms.common.generator.SourceGenerator

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.publiccms.common.base.BaseService;
import com.publiccms.common.handler.PageHandler;
import com.publiccms.entities.log.LogVisitDay;
import com.publiccms.entities.log.LogVisitSession;
import com.publiccms.logic.dao.log.LogVisitSessionDao;

/**
 *
 * LogVisitSessionService
 * 
 */
@Service
@Transactional
public class LogVisitSessionService extends BaseService<LogVisitSession> {
    private String[] ignoreProperties = new String[] { "firstVisitDate", "ip", "refererUrl", "refererKeyword" };

    /**
     * @param siteId
     * @param sessionId
     * @param startVisitDate
     * @param endVisitDate
     * @param orderType
     * @param pageIndex
     * @param pageSize
     * @return results page
     */
    @Transactional(readOnly = true)
    public PageHandler getPage(short siteId, String sessionId, Date startVisitDate, Date endVisitDate, String orderType,
            Integer pageIndex, Integer pageSize) {
        return dao.getPage(siteId, sessionId, startVisitDate, endVisitDate, orderType, pageIndex, pageSize);
    }

    /**
     * @param visitDate
     * @return results page
     */
    @Transactional(readOnly = true)
    public List<LogVisitDay> getDayList(Date visitDate) {
        return dao.getDayList(visitDate);
    }

    /**
     * @param begintime
     * @return number of data deleted
     */
    public int delete(Date begintime) {
        return dao.delete(begintime);
    }

    /**
     * @param entity
     */
    public void save(List<LogVisitSession> entityList) {
        for (LogVisitSession entity : entityList) {
            LogVisitSession oldEntity = getEntity(entity.getId());
            if (null == oldEntity) {
                dao.save(entity);
            } else {
                entity.setPv(oldEntity.getPv() + entity.getPv());
                BeanUtils.copyProperties(entity, oldEntity, ignoreProperties);
            }
        }
    }

    @Autowired
    private LogVisitSessionDao dao;

}
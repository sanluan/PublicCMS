package com.publiccms.logic.service.visit;

import java.util.Date;
import java.util.List;

import org.springframework.beans.BeanUtils;

// Generated 2021-1-14 22:44:06 by com.publiccms.common.generator.SourceGenerator

import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.publiccms.common.base.BaseService;
import com.publiccms.common.handler.PageHandler;
import com.publiccms.entities.visit.VisitDay;
import com.publiccms.entities.visit.VisitSession;
import com.publiccms.logic.dao.visit.VisitSessionDao;

/**
 *
 * VisitSessionService
 * 
 */
@Service
@Transactional
public class VisitSessionService extends BaseService<VisitSession> {
    private String[] ignoreProperties = new String[] { "firstVisitDate", "ip", "refererUrl", "refererKeyword" };

    /**
     * @param siteId
     * @param sessionId
     * @param ip
     * @param startVisitDate
     * @param endVisitDate
     * @param orderType
     * @param pageIndex
     * @param pageSize
     * @return results page
     */
    @Transactional(readOnly = true)
    public PageHandler getPage(short siteId, String sessionId, String ip, Date startVisitDate, Date endVisitDate,
            String orderType, Integer pageIndex, Integer pageSize) {
        return dao.getPage(siteId, sessionId, ip, startVisitDate, endVisitDate, orderType, pageIndex, pageSize);
    }

    /**
     * @param siteId
     * @param visitDate
     * @return results page
     */
    @Transactional(readOnly = true)
    public List<VisitDay> getDayList(Short siteId, Date visitDate) {
        return dao.getDayList(siteId, visitDate);
    }

    /**
     * @param begintime
     * @return number of data deleted
     */
    public int delete(Date begintime) {
        return dao.delete(begintime);
    }

    /**
     * @param entityList
     */
    public void save(List<VisitSession> entityList) {
        for (VisitSession entity : entityList) {
            VisitSession oldEntity = getEntity(entity.getId());
            if (null == oldEntity) {
                dao.save(entity);
            } else {
                entity.setPv(oldEntity.getPv() + entity.getPv());
                BeanUtils.copyProperties(entity, oldEntity, ignoreProperties);
            }
        }
    }

    @Resource
    private VisitSessionDao dao;

}
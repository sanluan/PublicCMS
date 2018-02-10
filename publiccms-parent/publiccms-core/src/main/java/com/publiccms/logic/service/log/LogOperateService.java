package com.publiccms.logic.service.log;

import java.io.Serializable;
import java.util.Date;

import com.publiccms.entities.log.LogOperate;
import com.publiccms.logic.dao.log.LogOperateDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.publiccms.common.base.BaseService;
import com.publiccms.common.handler.PageHandler;

/**
 *
 * LogOperateService
 * 
 */
@Service
@Transactional
public class LogOperateService extends BaseService<LogOperate> {

    /**
     * @param siteId
     * @param channel
     * @param operate
     * @param userId
     * @param startCreateDate
     * @param endCreateDate
     * @param content
     * @param ip
     * @param orderType
     * @param pageIndex
     * @param pageSize
     * @return
     */
    @Transactional(readOnly = true)
    public PageHandler getPage(Short siteId, String channel, String operate, Long userId, Date startCreateDate,
            Date endCreateDate, String content, String ip, String orderType, Integer pageIndex, Integer pageSize) {
        return dao.getPage(siteId, channel, operate, userId, startCreateDate, endCreateDate, content, ip, orderType, pageIndex,
                pageSize);
    }

    /**
     * @param siteId
     * @param createDate
     * @return
     */
    public int delete(Short siteId, Date createDate) {
        return dao.delete(siteId, createDate);
    }

    /**
     * @param siteId
     * @param ids
     */
    public void delete(short siteId, Serializable[] ids) {
        for (LogOperate entity : getEntitys(ids)) {
            if (siteId == entity.getSiteId()) {
                delete(entity.getId());
            }
        }
    }

    @Autowired
    private LogOperateDao dao;

}

package com.publiccms.logic.service.log;

import java.io.Serializable;
import java.util.Date;

import com.publiccms.entities.log.LogLogin;
import com.publiccms.logic.dao.log.LogLoginDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.publiccms.common.base.BaseService;
import com.publiccms.common.handler.PageHandler;

/**
 *
 * LogLoginService
 * 
 */
@Service
@Transactional
public class LogLoginService extends BaseService<LogLogin> {

    /**
     * 
     */
    public static final String CHANNEL_WEB_MANAGER = "web_manager";
    /**
     * 
     */
    public static final String CHANNEL_WEB = "web";

    /**
     * @param siteId
     * @param userId
     * @param startCreateDate
     * @param endCreateDate
     * @param channel
     * @param result
     * @param name
     * @param ip
     * @param orderType
     * @param pageIndex
     * @param pageSize
     * @return
     */
    @Transactional(readOnly = true)
    public PageHandler getPage(Short siteId, Long userId, Date startCreateDate, Date endCreateDate, String channel,
            Boolean result, String name, String ip, String orderType, Integer pageIndex, Integer pageSize) {
        return dao.getPage(siteId, userId, startCreateDate, endCreateDate, channel, result, name, ip, orderType, pageIndex,
                pageSize);
    }

    /**
     * @param siteId
     * @param ids
     */
    public void delete(short siteId, Serializable[] ids) {
        for (LogLogin entity : getEntitys(ids)) {
            if (siteId == entity.getSiteId()) {
                delete(entity.getId());
            }
        }
    }

    /**
     * @param siteId
     * @param createDate
     * @return
     */
    public int delete(Short siteId, Date createDate) {
        return dao.delete(siteId, createDate);
    }

    @Autowired
    private LogLoginDao dao;

}

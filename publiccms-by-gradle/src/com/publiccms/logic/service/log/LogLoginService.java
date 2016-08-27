package com.publiccms.logic.service.log;

import java.io.Serializable;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.publiccms.entities.log.LogLogin;
import com.publiccms.logic.dao.log.LogLoginDao;
import com.sanluan.common.base.BaseService;
import com.sanluan.common.handler.PageHandler;

@Service
@Transactional
public class LogLoginService extends BaseService<LogLogin> {
    public static final String CHANNEL_WEB_MANAGER = "web_manager", CHANNEL_WEB = "web";

    @Transactional(readOnly = true)
    public PageHandler getPage(Integer siteId, Long userId, Date startCreateDate, Date endCreateDate, String channel,
            Boolean result, String name, String ip, String orderType, Integer pageIndex, Integer pageSize) {
        return dao.getPage(siteId, userId, startCreateDate, endCreateDate, channel, result, name, ip, orderType, pageIndex,
                pageSize);
    }

    public void delete(int siteId, Serializable[] ids) {
        for (LogLogin entity : getEntitys(ids)) {
            if (siteId == entity.getSiteId()) {
                delete(entity.getId());
            }
        }
    }

    public int delete(Integer siteId, Date createDate) {
        return dao.delete(siteId, createDate);
    }

    @Autowired
    private LogLoginDao dao;
}

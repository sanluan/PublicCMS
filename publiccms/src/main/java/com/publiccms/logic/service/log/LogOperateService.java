package com.publiccms.logic.service.log;

import java.io.Serializable;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.publiccms.entities.log.LogOperate;
import com.publiccms.logic.dao.log.LogOperateDao;
import com.sanluan.common.base.BaseService;
import com.sanluan.common.handler.PageHandler;

@Service
@Transactional
public class LogOperateService extends BaseService<LogOperate> {

    @Transactional(readOnly = true)
    public PageHandler getPage(Integer siteId, String channel, String operate, Long userId, Date startCreateDate,
            Date endCreateDate, String content, String ip, String orderType, Integer pageIndex, Integer pageSize) {
        return dao.getPage(siteId, channel, operate, userId, startCreateDate, endCreateDate, content, ip, orderType, pageIndex,
                pageSize);
    }

    public int delete(Integer siteId, Date createDate) {
        return dao.delete(siteId, createDate);
    }

    public void delete(int siteId, Serializable[] ids) {
        for (LogOperate entity : getEntitys(ids)) {
            if (siteId == entity.getSiteId()) {
                delete(entity.getId());
            }
        }
    }

    @Autowired
    private LogOperateDao dao;
}

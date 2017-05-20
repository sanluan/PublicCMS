package org.publiccms.logic.service.home;

import java.io.Serializable;

import org.publiccms.entities.home.HomeBroadcast;
import org.publiccms.logic.dao.home.HomeBroadcastDao;

// Generated 2016-11-13 11:38:14 by com.publiccms.common.source.SourceGenerator

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.publiccms.common.base.BaseService;
import com.publiccms.common.handler.PageHandler;

/**
 *
 * HomeBroadcastService
 * 
 */
@Service
@Transactional
public class HomeBroadcastService extends BaseService<HomeBroadcast> {

    /**
     * @param siteId
     * @param userId
     * @param reposted
     * @param repostId
     * @param disabled
     * @param orderField
     * @param orderType
     * @param pageIndex
     * @param pageSize
     * @return
     */
    @Transactional(readOnly = true)
    public PageHandler getPage(Integer siteId, Long userId, Boolean reposted, Long repostId, Boolean disabled, String orderField,
            String orderType, Integer pageIndex, Integer pageSize) {
        return dao.getPage(siteId, userId, reposted, repostId, disabled, orderField, orderType, pageIndex, pageSize);
    }

    /**
     * @param id
     * @param status
     * @return
     */
    public HomeBroadcast updateStatus(Serializable id, boolean status) {
        HomeBroadcast entity = getEntity(id);
        if (null != entity) {
            entity.setDisabled(status);
        }
        return entity;
    }

    @Autowired
    private HomeBroadcastDao dao;

}
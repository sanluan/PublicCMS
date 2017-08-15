package org.publiccms.logic.service.home;

import java.io.Serializable;

import org.publiccms.entities.home.HomeDirectory;
import org.publiccms.logic.dao.home.HomeDirectoryDao;

// Generated 2016-11-13 11:38:13 by com.publiccms.common.source.SourceGenerator

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.publiccms.common.base.BaseService;
import com.publiccms.common.handler.PageHandler;

/**
 *
 * HomeDirectoryService
 * 
 */
@Service
@Transactional
public class HomeDirectoryService extends BaseService<HomeDirectory> {

    /**
     * @param siteId
     * @param userId
     * @param disabled
     * @param orderType
     * @param pageIndex
     * @param pageSize
     * @return
     */
    @Transactional(readOnly = true)
    public PageHandler getPage(Integer siteId, Long userId, Boolean disabled, String orderType, Integer pageIndex,
            Integer pageSize) {
        return dao.getPage(siteId, userId, disabled, orderType, pageIndex, pageSize);
    }

    /**
     * @param id
     * @param status
     * @return
     */
    public HomeDirectory updateStatus(Serializable id, boolean status) {
        HomeDirectory entity = getEntity(id);
        if (null != entity) {
            entity.setDisabled(status);
        }
        return entity;
    }

    @Autowired
    private HomeDirectoryDao dao;

}
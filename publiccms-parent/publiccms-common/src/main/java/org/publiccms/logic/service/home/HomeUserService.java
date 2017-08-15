package org.publiccms.logic.service.home;

import java.io.Serializable;

import org.publiccms.entities.home.HomeUser;
import org.publiccms.logic.dao.home.HomeUserDao;

// Generated 2016-11-13 11:38:14 by com.publiccms.common.source.SourceGenerator

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.publiccms.common.base.BaseService;
import com.publiccms.common.handler.PageHandler;

/**
 *
 * HomeUserService
 * 
 */
@Service
@Transactional
public class HomeUserService extends BaseService<HomeUser> {

    /**
     * @param siteId
     * @param disabled
     * @param orderField
     * @param orderType
     * @param pageIndex
     * @param pageSize
     * @return
     */
    @Transactional(readOnly = true)
    public PageHandler getPage(Integer siteId, Boolean disabled, String orderField, String orderType, Integer pageIndex,
            Integer pageSize) {
        return dao.getPage(siteId, disabled, orderField, orderType, pageIndex, pageSize);
    }

    /**
     * @param id
     * @param status
     * @return
     */
    public HomeUser updateStatus(Serializable id, boolean status) {
        HomeUser entity = getEntity(id);
        if (null != entity) {
            entity.setDisabled(status);
        }
        return entity;
    }

    @Autowired
    private HomeUserDao dao;

}
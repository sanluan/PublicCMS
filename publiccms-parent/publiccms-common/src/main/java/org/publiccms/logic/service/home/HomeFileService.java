package org.publiccms.logic.service.home;

import java.io.Serializable;

import org.publiccms.entities.home.HomeFile;
import org.publiccms.logic.dao.home.HomeFileDao;

// Generated 2016-11-13 11:38:14 by com.publiccms.common.source.SourceGenerator

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.publiccms.common.base.BaseService;
import com.publiccms.common.handler.PageHandler;

/**
 *
 * HomeFileService
 * 
 */
@Service
@Transactional
public class HomeFileService extends BaseService<HomeFile> {

    /**
     * @param siteId
     * @param userId
     * @param directoryId
     * @param title
     * @param filePath
     * @param image
     * @param disabled
     * @param orderField
     * @param orderType
     * @param pageIndex
     * @param pageSize
     * @return
     */
    @Transactional(readOnly = true)
    public PageHandler getPage(Integer siteId, Long userId, Long directoryId, String title, String filePath, Boolean image,
            Boolean disabled, String orderField, String orderType, Integer pageIndex, Integer pageSize) {
        return dao.getPage(siteId, userId, directoryId, title, filePath, image, disabled, orderField, orderType, pageIndex,
                pageSize);
    }

    /**
     * @param id
     * @param status
     * @return
     */
    public HomeFile updateStatus(Serializable id, boolean status) {
        HomeFile entity = getEntity(id);
        if (null != entity) {
            entity.setDisabled(status);
        }
        return entity;
    }

    @Autowired
    private HomeFileDao dao;
    
}
package org.publiccms.logic.service.home;

import java.io.Serializable;

import org.publiccms.entities.home.HomeArticle;
import org.publiccms.logic.dao.home.HomeArticleDao;

// Generated 2016-11-13 11:38:14 by com.publiccms.common.source.SourceGenerator

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.publiccms.common.base.BaseService;
import com.publiccms.common.handler.PageHandler;

/**
 *
 * HomeArticleService
 * 
 */
@Service
@Transactional
public class HomeArticleService extends BaseService<HomeArticle> {

    /**
     * @param siteId
     * @param directoryId
     * @param userId
     * @param disabled
     * @param orderField
     * @param orderType
     * @param pageIndex
     * @param pageSize
     * @return
     */
    @Transactional(readOnly = true)
    public PageHandler getPage(Integer siteId, Long directoryId, Long userId, Boolean disabled, String orderField,
            String orderType, Integer pageIndex, Integer pageSize) {
        return dao.getPage(siteId, directoryId, userId, disabled, orderField, orderType, pageIndex, pageSize);
    }

    /**
     * @param id
     * @param status
     * @return
     */
    public HomeArticle updateStatus(Serializable id, boolean status) {
        HomeArticle entity = getEntity(id);
        if (null != entity) {
            entity.setDisabled(status);
        }
        return entity;
    }

    @Autowired
    private HomeArticleDao dao;

}
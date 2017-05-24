package org.publiccms.logic.dao.cms;

import org.publiccms.entities.cms.CmsContentFile;

// Generated 2016-1-25 10:53:21 by com.publiccms.common.source.SourceGenerator

import org.springframework.stereotype.Repository;

import com.publiccms.common.base.BaseDao;
import com.publiccms.common.handler.PageHandler;
import com.publiccms.common.handler.QueryHandler;

/**
 *
 * CmsContentFileDao
 * 
 */
@Repository
public class CmsContentFileDao extends BaseDao<CmsContentFile> {
    
    /**
     * @param contentId
     * @param userId
     * @param image
     * @param orderField
     * @param orderType
     * @param pageIndex
     * @param pageSize
     * @return
     */
    public PageHandler getPage(Long contentId, Long userId, Boolean image, String orderField, String orderType,
            Integer pageIndex, Integer pageSize) {
        QueryHandler queryHandler = getQueryHandler("from CmsContentFile bean");
        if (notEmpty(contentId)) {
            queryHandler.condition("bean.contentId = :contentId").setParameter("contentId", contentId);
        }
        if (notEmpty(userId)) {
            queryHandler.condition("bean.userId = :userId").setParameter("userId", userId);
        }
        if (notEmpty(image)) {
            queryHandler.condition("bean.image = :image").setParameter("image", image);
        }
        if ("asc".equalsIgnoreCase(orderType)) {
            orderType = "asc";
        } else {
            orderType = "desc";
        }
        if (null == orderField) {
            orderField = BLANK;
        }
        switch (orderField) {
        case "size":
            queryHandler.order("bean.size " + orderType);
            break;
        case "clicks":
            queryHandler.order("bean.clicks " + orderType);
            break;
        default:
            queryHandler.order("bean.sort asc,bean.id asc");
        }
        return getPage(queryHandler, pageIndex, pageSize);
    }

    @Override
    protected CmsContentFile init(CmsContentFile entity) {
        return entity;
    }

}
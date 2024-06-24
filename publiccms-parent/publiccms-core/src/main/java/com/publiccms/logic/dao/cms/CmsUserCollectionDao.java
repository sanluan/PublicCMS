package com.publiccms.logic.dao.cms;

// Generated 2020-3-26 11:46:48 by com.publiccms.common.generator.SourceGenerator

import org.springframework.stereotype.Repository;

import com.publiccms.common.base.BaseDao;
import com.publiccms.common.handler.PageHandler;
import com.publiccms.common.handler.QueryHandler;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.entities.cms.CmsUserCollection;

/**
 *
 * CmsUserCollectionDao
 * 
 */
@Repository
public class CmsUserCollectionDao extends BaseDao<CmsUserCollection> {

    /**
     * @param userId
     * @param contentId
     * @param pageIndex
     * @param pageSize
     * @return results page
     */
    public PageHandler getPage(Long userId, Long contentId, Integer pageIndex, Integer pageSize) {
        QueryHandler queryHandler = getQueryHandler("from CmsUserCollection bean");
        if (null != userId) {
            queryHandler.condition("bean.id.userId = :userId").setParameter("userId", userId);
        }
        if (null != contentId) {
            queryHandler.condition("bean.id.contentId = :contentId").setParameter("contentId", contentId);
        }
        queryHandler.order("bean.createDate desc");
        return getPage(queryHandler, pageIndex, pageSize);
    }

    @Override
    protected CmsUserCollection init(CmsUserCollection entity) {
        if (null == entity.getCreateDate()) {
            entity.setCreateDate(CommonUtils.getDate());
        }
        return entity;
    }

}
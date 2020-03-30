package com.publiccms.logic.dao.cms;

// Generated 2020-3-26 11:46:48 by com.publiccms.common.generator.SourceGenerator

import org.springframework.stereotype.Repository;

import com.publiccms.common.base.BaseDao;
import com.publiccms.common.handler.PageHandler;
import com.publiccms.common.handler.QueryHandler;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.entities.cms.CmsUserScore;

/**
 *
 * CmsUserScoreDao
 * 
 */
@Repository
public class CmsUserScoreDao extends BaseDao<CmsUserScore> {

    /**
     * @param userId
     * @param itemType
     * @param itemId
     * @param pageIndex
     * @param pageSize
     * @return results page
     */
    public PageHandler getPage(Long userId, String itemType, Long itemId, Integer pageIndex, Integer pageSize) {
        QueryHandler queryHandler = getQueryHandler("from CmsUserScore bean");
        if (null != userId) {
            queryHandler.condition("bean.id.userId = :userId").setParameter("userId", userId);
        }
        if (CommonUtils.notEmpty(itemType) && null != itemId) {
            queryHandler.condition("bean.id.itemType = :itemType").setParameter("itemType", userId);
            queryHandler.condition("bean.id.itemId = :itemId").setParameter("itemId", itemId);
        }
        queryHandler.order("bean.createDate desc");
        return getPage(queryHandler, pageIndex, pageSize);
    }

    @Override
    protected CmsUserScore init(CmsUserScore entity) {
        if (null == entity.getCreateDate()) {
            entity.setCreateDate(CommonUtils.getDate());
        }
        return entity;
    }

}
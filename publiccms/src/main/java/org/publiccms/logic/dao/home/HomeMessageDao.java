package org.publiccms.logic.dao.home;

import org.publiccms.entities.home.HomeMessage;

// Generated 2016-11-19 9:58:45 by com.publiccms.common.source.SourceGenerator

import org.springframework.stereotype.Repository;

import com.publiccms.common.base.BaseDao;
import com.publiccms.common.handler.PageHandler;
import com.publiccms.common.handler.QueryHandler;

/**
 *
 * HomeMessageDao
 * 
 */
@Repository
public class HomeMessageDao extends BaseDao<HomeMessage> {

    /**
     * @param userId
     * @param itemType
     * @param itemId
     * @param pageIndex
     * @param pageSize
     * @return
     */
    public PageHandler getPage(Long userId, String itemType, Long itemId, Integer pageIndex, Integer pageSize) {
        QueryHandler queryHandler = getQueryHandler("from HomeMessage bean");
        if (notEmpty(userId)) {
            queryHandler.condition("bean.userId = :userId").setParameter("userId", userId);
        }
        if (notEmpty(itemType)) {
            queryHandler.condition("bean.itemType = :itemType").setParameter("itemType", itemType);
        }
        if (notEmpty(itemId)) {
            queryHandler.condition("bean.itemId = :itemId").setParameter("itemId", itemId);
        }
        queryHandler.order("bean.id desc");
        return getPage(queryHandler, pageIndex, pageSize);
    }

    @Override
    protected HomeMessage init(HomeMessage entity) {
        if (empty(entity.getCreateDate())) {
            entity.setCreateDate(getDate());
        }
        return entity;
    }

}
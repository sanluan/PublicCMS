package org.publiccms.logic.dao.home;

import org.publiccms.entities.home.HomeGroupActive;

// Generated 2016-11-19 9:58:46 by com.publiccms.common.source.SourceGenerator

import org.springframework.stereotype.Repository;

import com.publiccms.common.base.BaseDao;
import com.publiccms.common.handler.PageHandler;
import com.publiccms.common.handler.QueryHandler;

/**
 *
 * HomeGroupActiveDao
 * 
 */
@Repository
public class HomeGroupActiveDao extends BaseDao<HomeGroupActive> {

    /**
     * @param groupId
     * @param itemType
     * @param userId
     * @param pageIndex
     * @param pageSize
     * @return
     */
    public PageHandler getPage(Long groupId, String itemType, Long userId, Integer pageIndex, Integer pageSize) {
        QueryHandler queryHandler = getQueryHandler("from HomeGroupActive bean");
        if (notEmpty(groupId)) {
            queryHandler.condition("bean.groupId = :groupId").setParameter("groupId", groupId);
        }
        if (notEmpty(itemType)) {
            queryHandler.condition("bean.itemType = :itemType").setParameter("itemType", itemType);
        }
        if (notEmpty(userId)) {
            queryHandler.condition("bean.userId = :userId").setParameter("userId", userId);
        }
        queryHandler.order("bean.id desc");
        return getPage(queryHandler, pageIndex, pageSize);
    }

    @Override
    protected HomeGroupActive init(HomeGroupActive entity) {
        if (empty(entity.getCreateDate())) {
            entity.setCreateDate(getDate());
        }
        return entity;
    }

}
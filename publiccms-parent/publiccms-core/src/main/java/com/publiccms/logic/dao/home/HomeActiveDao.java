package com.publiccms.logic.dao.home;

import org.springframework.stereotype.Repository;

import com.publiccms.common.base.BaseDao;
import com.publiccms.common.handler.PageHandler;
import com.publiccms.common.handler.QueryHandler;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.entities.home.HomeActive;

/**
 *
 * HomeActiveDao
 * 
 */
@Repository
public class HomeActiveDao extends BaseDao<HomeActive> {
    
    /**
     * @param itemType
     * @param userId
     * @param userIds
     * @param pageIndex
     * @param pageSize
     * @return results page
     */
    public PageHandler getPage(String itemType, Long userId, Long[] userIds, Integer pageIndex, Integer pageSize) {
        QueryHandler queryHandler = getQueryHandler("from HomeActive bean");
        if (CommonUtils.notEmpty(itemType)) {
            queryHandler.condition("bean.itemType = :itemType").setParameter("itemType", itemType);
        }
        if (CommonUtils.notEmpty(userId)) {
            queryHandler.condition("bean.userId = :userId").setParameter("userId", userId);
        }
        if (CommonUtils.notEmpty(userIds)) {
            queryHandler.condition("bean.userIds = :userIds").setParameter("userIds", userIds);
        }
        queryHandler.order("bean.id desc");
        return getPage(queryHandler, pageIndex, pageSize);
    }

    @Override
    protected HomeActive init(HomeActive entity) {
        if (null == entity.getCreateDate()) {
            entity.setCreateDate(CommonUtils.getDate());
        }
        return entity;
    }

}
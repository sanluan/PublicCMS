package org.publiccms.logic.dao.home;

// Generated 2016-11-13 11:38:14 by com.publiccms.common.source.SourceGenerator
import static com.publiccms.common.tools.CommonUtils.empty;
import static com.publiccms.common.tools.CommonUtils.getDate;
import static com.publiccms.common.tools.CommonUtils.notEmpty;

import org.publiccms.entities.home.HomeActive;
import org.springframework.stereotype.Repository;

import com.publiccms.common.base.BaseDao;
import com.publiccms.common.handler.PageHandler;
import com.publiccms.common.handler.QueryHandler;

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
     * @return
     */
    public PageHandler getPage(String itemType, Long userId, Long[] userIds, Integer pageIndex, Integer pageSize) {
        QueryHandler queryHandler = getQueryHandler("from HomeActive bean");
        if (notEmpty(itemType)) {
            queryHandler.condition("bean.itemType = :itemType").setParameter("itemType", itemType);
        }
        if (notEmpty(userId)) {
            queryHandler.condition("bean.userId = :userId").setParameter("userId", userId);
        }
        if (notEmpty(userIds)) {
            queryHandler.condition("bean.userIds = :userIds").setParameter("userIds", userIds);
        }
        queryHandler.order("bean.id desc");
        return getPage(queryHandler, pageIndex, pageSize);
    }

    @Override
    protected HomeActive init(HomeActive entity) {
        if (empty(entity.getCreateDate())) {
            entity.setCreateDate(getDate());
        }
        return entity;
    }

}
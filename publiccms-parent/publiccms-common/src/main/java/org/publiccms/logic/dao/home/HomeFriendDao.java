package org.publiccms.logic.dao.home;

// Generated 2016-11-19 9:58:46 by com.publiccms.common.source.SourceGenerator
import static com.publiccms.common.tools.CommonUtils.empty;
import static com.publiccms.common.tools.CommonUtils.getDate;
import static com.publiccms.common.tools.CommonUtils.notEmpty;

import org.publiccms.entities.home.HomeFriend;
import org.springframework.stereotype.Repository;

import com.publiccms.common.base.BaseDao;
import com.publiccms.common.handler.PageHandler;
import com.publiccms.common.handler.QueryHandler;

/**
 *
 * HomeFriendDao
 * 
 */
@Repository
public class HomeFriendDao extends BaseDao<HomeFriend> {

    /**
     * @param userId
     * @param friendId
     * @param pageIndex
     * @param pageSize
     * @return
     */
    public PageHandler getPage(Long userId, Long friendId, Integer pageIndex, Integer pageSize) {
        QueryHandler queryHandler = getQueryHandler("from HomeFriend bean");
        if (notEmpty(userId)) {
            queryHandler.condition("bean.userId = :userId").setParameter("userId", userId);
        }
        if (notEmpty(friendId)) {
            queryHandler.condition("bean.friendId = :friendId").setParameter("friendId", friendId);
        }
        queryHandler.order("bean.id desc");
        return getPage(queryHandler, pageIndex, pageSize);
    }

    @Override
    protected HomeFriend init(HomeFriend entity) {
        if (empty(entity.getCreateDate())) {
            entity.setCreateDate(getDate());
        }
        return entity;
    }

}
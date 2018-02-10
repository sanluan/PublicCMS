package com.publiccms.logic.dao.home;

import org.springframework.stereotype.Repository;

import com.publiccms.common.base.BaseDao;
import com.publiccms.common.handler.PageHandler;
import com.publiccms.common.handler.QueryHandler;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.entities.home.HomeAttention;

/**
 *
 * HomeAttentionDao
 * 
 */
@Repository
public class HomeAttentionDao extends BaseDao<HomeAttention> {
    
    /**
     * @param userId
     * @param attentionId
     * @param pageIndex
     * @param pageSize
     * @return results page
     */
    public PageHandler getPage(Long userId, Long attentionId, Integer pageIndex, Integer pageSize) {
        QueryHandler queryHandler = getQueryHandler("from HomeAttention bean");
        if (CommonUtils.notEmpty(userId)) {
            queryHandler.condition("bean.id.userId = :userId").setParameter("userId", userId);
        }
        if (CommonUtils.notEmpty(attentionId)) {
            queryHandler.condition("bean.id.attentionId = :attentionId").setParameter("attentionId", attentionId);
        }
        queryHandler.order("bean.createDate desc");
        return getPage(queryHandler, pageIndex, pageSize);
    }

    @Override
    protected HomeAttention init(HomeAttention entity) {
        if (null == entity.getCreateDate()) {
            entity.setCreateDate(CommonUtils.getDate());
        }
        return entity;
    }

}
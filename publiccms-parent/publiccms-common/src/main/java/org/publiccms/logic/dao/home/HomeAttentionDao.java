package org.publiccms.logic.dao.home;

// Generated 2016-11-13 11:38:14 by com.publiccms.common.source.SourceGenerator
import static com.publiccms.common.tools.CommonUtils.empty;
import static com.publiccms.common.tools.CommonUtils.getDate;
import static com.publiccms.common.tools.CommonUtils.notEmpty;

import org.publiccms.entities.home.HomeAttention;
import org.springframework.stereotype.Repository;

import com.publiccms.common.base.BaseDao;
import com.publiccms.common.handler.PageHandler;
import com.publiccms.common.handler.QueryHandler;

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
     * @return
     */
    public PageHandler getPage(Long userId, Long attentionId, Integer pageIndex, Integer pageSize) {
        QueryHandler queryHandler = getQueryHandler("from HomeAttention bean");
        if (notEmpty(userId)) {
            queryHandler.condition("bean.id.userId = :userId").setParameter("userId", userId);
        }
        if (notEmpty(attentionId)) {
            queryHandler.condition("bean.id.attentionId = :attentionId").setParameter("attentionId", attentionId);
        }
        queryHandler.order("bean.createDate desc");
        return getPage(queryHandler, pageIndex, pageSize);
    }

    @Override
    protected HomeAttention init(HomeAttention entity) {
        if (empty(entity.getCreateDate())) {
            entity.setCreateDate(getDate());
        }
        return entity;
    }

}
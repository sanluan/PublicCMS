package org.publiccms.logic.dao.cms;

// Generated 2016-3-1 17:24:24 by com.publiccms.common.source.SourceGenerator

import static com.publiccms.common.tools.CommonUtils.empty;
import static com.publiccms.common.tools.CommonUtils.getDate;
import static com.publiccms.common.tools.CommonUtils.notEmpty;

import java.util.Date;

import org.publiccms.entities.cms.CmsLotteryUser;
import org.springframework.stereotype.Repository;

import com.publiccms.common.base.BaseDao;
import com.publiccms.common.handler.PageHandler;
import com.publiccms.common.handler.QueryHandler;

/**
 *
 * CmsLotteryUserDao
 * 
 */
@Repository
public class CmsLotteryUserDao extends BaseDao<CmsLotteryUser> {
    
    /**
     * @param lotteryId
     * @param userId
     * @param winning
     * @param startCreateDate
     * @param endCreateDate
     * @param orderType
     * @param pageIndex
     * @param pageSize
     * @return
     */
    public PageHandler getPage(Long lotteryId, Long userId, 
                Boolean winning, Date startCreateDate, Date endCreateDate, 
                String orderType, Integer pageIndex, Integer pageSize) {
        QueryHandler queryHandler = getQueryHandler("from CmsLotteryUser bean");
        if (notEmpty(lotteryId)) {
            queryHandler.condition("bean.lotteryId = :lotteryId").setParameter("lotteryId", lotteryId);
        }
        if (notEmpty(userId)) {
            queryHandler.condition("bean.userId = :userId").setParameter("userId", userId);
        }
        if (notEmpty(winning)) {
            queryHandler.condition("bean.winning = :winning").setParameter("winning", winning);
        }
        if (notEmpty(startCreateDate)) {
            queryHandler.condition("bean.createDate > :startCreateDate").setParameter("startCreateDate", startCreateDate);
        }
        if (notEmpty(endCreateDate)) {
            queryHandler.condition("bean.createDate <= :endCreateDate").setParameter("endCreateDate", endCreateDate);
        }
        if(!ORDERTYPE_ASC.equalsIgnoreCase(orderType)){
            orderType = ORDERTYPE_DESC;
        }
        queryHandler.order("bean.createDate " + orderType);
        return getPage(queryHandler, pageIndex, pageSize);
    }

    @Override
    protected CmsLotteryUser init(CmsLotteryUser entity) {
        if (empty(entity.getCreateDate())) {
            entity.setCreateDate(getDate());
        }
        return entity;
    }

}
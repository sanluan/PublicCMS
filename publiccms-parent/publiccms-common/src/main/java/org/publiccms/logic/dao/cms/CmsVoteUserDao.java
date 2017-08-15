package org.publiccms.logic.dao.cms;

// Generated 2016-3-3 17:46:06 by com.publiccms.common.source.SourceGenerator
import static com.publiccms.common.tools.CommonUtils.empty;
import static com.publiccms.common.tools.CommonUtils.getDate;
import static com.publiccms.common.tools.CommonUtils.notEmpty;

import java.util.Date;

import org.publiccms.entities.cms.CmsVoteUser;
import org.springframework.stereotype.Repository;

import com.publiccms.common.base.BaseDao;
import com.publiccms.common.handler.PageHandler;
import com.publiccms.common.handler.QueryHandler;

/**
 *
 * CmsVoteUserDao
 * 
 */
@Repository
public class CmsVoteUserDao extends BaseDao<CmsVoteUser> {
    
    /**
     * @param lotteryId
     * @param userId
     * @param ip
     * @param startCreateDate
     * @param endCreateDate
     * @param orderType
     * @param pageIndex
     * @param pageSize
     * @return
     */
    public PageHandler getPage(Integer lotteryId, Long userId, String ip, Date startCreateDate, Date endCreateDate,
            String orderType, Integer pageIndex, Integer pageSize) {
        QueryHandler queryHandler = getQueryHandler("from CmsVoteUser bean");
        if (notEmpty(lotteryId)) {
            queryHandler.condition("bean.lotteryId = :lotteryId").setParameter("lotteryId", lotteryId);
        }
        if (notEmpty(userId)) {
            queryHandler.condition("bean.userId = :userId").setParameter("userId", userId);
        }
        if (notEmpty(ip)) {
            queryHandler.condition("bean.ip = :ip").setParameter("ip", ip);
        }
        if (notEmpty(startCreateDate)) {
            queryHandler.condition("bean.createDate > :startCreateDate").setParameter("startCreateDate", startCreateDate);
        }
        if (notEmpty(endCreateDate)) {
            queryHandler.condition("bean.createDate <= :endCreateDate").setParameter("endCreateDate", endCreateDate);
        }
        if (!ORDERTYPE_ASC.equalsIgnoreCase(orderType)) {
            orderType = ORDERTYPE_DESC;
        }
        queryHandler.order("bean.createDate " + orderType);
        return getPage(queryHandler, pageIndex, pageSize);
    }

    @Override
    protected CmsVoteUser init(CmsVoteUser entity) {
        if (empty(entity.getCreateDate())) {
            entity.setCreateDate(getDate());
        }
        return entity;
    }

}
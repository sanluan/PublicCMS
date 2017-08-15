package org.publiccms.logic.dao.sys;

// Generated 2016-1-20 11:19:18 by com.publiccms.common.source.SourceGenerator
import static com.publiccms.common.tools.CommonUtils.empty;
import static com.publiccms.common.tools.CommonUtils.getDate;
import static com.publiccms.common.tools.CommonUtils.notEmpty;

import java.util.Date;

import org.publiccms.entities.sys.SysUserToken;
import org.springframework.stereotype.Repository;

import com.publiccms.common.base.BaseDao;
import com.publiccms.common.handler.PageHandler;
import com.publiccms.common.handler.QueryHandler;

/**
 *
 * SysUserTokenDao
 * 
 */
@Repository
public class SysUserTokenDao extends BaseDao<SysUserToken> {
    
    /**
     * @param siteId
     * @param userId
     * @param channel
     * @param orderType
     * @param pageIndex
     * @param pageSize
     * @return
     */
    public PageHandler getPage(Integer siteId, Long userId, String channel, String orderType, Integer pageIndex, Integer pageSize) {
        QueryHandler queryHandler = getQueryHandler("from SysUserToken bean");
        if (notEmpty(siteId)) {
            queryHandler.condition("bean.siteId = :siteId").setParameter("siteId", siteId);
        }
        if (notEmpty(userId)) {
            queryHandler.condition("bean.userId = :userId").setParameter("userId", userId);
        }
        if (notEmpty(channel)) {
            queryHandler.condition("bean.channel = :channel").setParameter("channel", channel);
        }
        if (!ORDERTYPE_ASC.equalsIgnoreCase(orderType)) {
            orderType = ORDERTYPE_DESC;
        }
        queryHandler.order("bean.createDate " + orderType );
        return getPage(queryHandler, pageIndex, pageSize);
    }
    
    /**
     * @param createDate
     * @return
     */
    public int delete(Date createDate) {
        if (notEmpty(createDate)) {
            QueryHandler queryHandler = getDeleteQueryHandler("from SysUserToken bean");
            queryHandler.condition("bean.createDate <= :createDate").setParameter("createDate", createDate);
            return delete(queryHandler);
        }
        return 0;
    }

    @Override
    protected SysUserToken init(SysUserToken entity) {
        if (empty(entity.getCreateDate())) {
            entity.setCreateDate(getDate());
        }
        return entity;
    }

}
package com.publiccms.logic.dao.sys;

import java.util.Date;

import org.springframework.stereotype.Repository;

import com.publiccms.common.base.BaseDao;
import com.publiccms.common.constants.CommonConstants;
import com.publiccms.common.handler.PageHandler;
import com.publiccms.common.handler.QueryHandler;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.entities.sys.SysUserToken;

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
     * @param orderField 
     * @param orderType
     * @param pageIndex
     * @param pageSize
     * @return results page
     */
    public PageHandler getPage(Short siteId, Long userId, String channel, String orderField, String orderType, Integer pageIndex,
            Integer pageSize) {
        QueryHandler queryHandler = getQueryHandler("from SysUserToken bean");
        if (CommonUtils.notEmpty(siteId)) {
            queryHandler.condition("bean.siteId = :siteId").setParameter("siteId", siteId);
        }
        if (CommonUtils.notEmpty(userId)) {
            queryHandler.condition("bean.userId = :userId").setParameter("userId", userId);
        }
        if (CommonUtils.notEmpty(channel)) {
            queryHandler.condition("bean.channel = :channel").setParameter("channel", channel);
        }
        if (!ORDERTYPE_ASC.equalsIgnoreCase(orderType)) {
            orderType = ORDERTYPE_DESC;
        }
        if (null == orderField) {
            orderField = CommonConstants.BLANK;
        }
        switch (orderField) {
        case "expiryDate":
            queryHandler.order("bean.expiryDate ").append(orderType);
            break;
        default:
            queryHandler.order("bean.createDate ").append(orderType);
        }
        return getPage(queryHandler, pageIndex, pageSize);
    }

    /**
     * @param now
     * @return number of data deleted
     */
    public int delete(Date now) {
        if (null != now) {
            QueryHandler queryHandler = getQueryHandler("delete from SysUserToken bean");
            queryHandler.condition("bean.expiryDate is not null");
            queryHandler.condition("bean.expiryDate <= :expiryDate").setParameter("expiryDate", now);
            return delete(queryHandler);
        }
        return 0;
    }

    /**
     * @param userId
     * @return number of data deleted
     */
    public int delete(Long userId) {
        if (CommonUtils.notEmpty(userId)) {
            QueryHandler queryHandler = getQueryHandler("delete from SysUserToken bean");
            queryHandler.condition("bean.userId = :userId").setParameter("userId", userId);
            return delete(queryHandler);
        }
        return 0;
    }

    @Override
    protected SysUserToken init(SysUserToken entity) {
        if (null == entity.getCreateDate()) {
            entity.setCreateDate(CommonUtils.getDate());
        }
        return entity;
    }

}
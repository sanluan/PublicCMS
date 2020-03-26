package com.publiccms.logic.dao.sys;

import java.util.Date;

import org.springframework.stereotype.Repository;

import com.publiccms.common.base.BaseDao;
import com.publiccms.common.constants.CommonConstants;
import com.publiccms.common.handler.PageHandler;
import com.publiccms.common.handler.QueryHandler;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.entities.sys.SysAppClient;

/**
 *
 * SysAppClientDao
 * 
 */
@Repository
public class SysAppClientDao extends BaseDao<SysAppClient> {

    /**
     * @param siteId
     * @param channel
     * @param userId
     * @param startLastLoginDate
     * @param endLastLoginDate
     * @param startCreateDate
     * @param endCreateDate
     * @param disabled
     * @param orderField
     * @param orderType
     * @param pageIndex
     * @param pageSize
     * @return results page
     */
    public PageHandler getPage(Short siteId, String channel, Long userId, Date startLastLoginDate, Date endLastLoginDate,
            Date startCreateDate, Date endCreateDate, Boolean disabled, String orderField, String orderType, Integer pageIndex,
            Integer pageSize) {
        QueryHandler queryHandler = getQueryHandler("from SysAppClient bean");
        if (CommonUtils.notEmpty(siteId)) {
            queryHandler.condition("bean.siteId = :siteId").setParameter("siteId", siteId);
        }
        if (CommonUtils.notEmpty(channel)) {
            queryHandler.condition("bean.channel = :channel").setParameter("channel", channel);
        }
        if (CommonUtils.notEmpty(userId)) {
            queryHandler.condition("bean.userId = :userId").setParameter("userId", userId);
        }
        if (null != startLastLoginDate) {
            queryHandler.condition("bean.lastLoginDate > :startLastLoginDate").setParameter("startLastLoginDate",
                    startLastLoginDate);
        }
        if (null != endLastLoginDate) {
            queryHandler.condition("bean.lastLoginDate <= :endLastLoginDate").setParameter("endLastLoginDate", endLastLoginDate);
        }
        if (null != startCreateDate) {
            queryHandler.condition("bean.createDate > :startCreateDate").setParameter("startCreateDate", startCreateDate);
        }
        if (null != endCreateDate) {
            queryHandler.condition("bean.createDate <= :endCreateDate").setParameter("endCreateDate", endCreateDate);
        }
        if (null != disabled) {
            queryHandler.condition("bean.disabled = :disabled").setParameter("disabled", disabled);
        }
        if (!ORDERTYPE_ASC.equalsIgnoreCase(orderType)) {
            orderType = ORDERTYPE_DESC;
        }
        if (null == orderField) {
            orderField = CommonConstants.BLANK;
        }
        switch (orderField) {
        case "lastLoginDate":
            queryHandler.order("bean.lastLoginDate ").append(orderType);
            break;
        case "createDate":
            queryHandler.order("bean.createDate ").append(orderType);
            break;
        default:
            queryHandler.order("bean.createDate ").append(orderType);
        }
        return getPage(queryHandler, pageIndex, pageSize);
    }

    /**
     * @param siteId
     * @param channel
     * @param uuid
     * @return the entity
     */
    public SysAppClient getEntity(Short siteId, String channel, String uuid) {
        QueryHandler queryHandler = getQueryHandler("from SysAppClient bean");
        queryHandler.condition("bean.siteId = :siteId").setParameter("siteId", siteId);
        queryHandler.condition("bean.channel = :channel").setParameter("channel", channel);
        queryHandler.condition("bean.uuid = :uuid").setParameter("uuid", uuid);
        return getEntity(queryHandler);
    }

    @Override
    protected SysAppClient init(SysAppClient entity) {
        if (null == entity.getCreateDate()) {
            entity.setCreateDate(CommonUtils.getDate());
        }
        return entity;
    }
}
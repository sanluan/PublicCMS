package org.publiccms.logic.dao.sys;

// Generated 2016-3-1 17:24:12 by com.publiccms.common.source.SourceGenerator
import static com.publiccms.common.tools.CommonUtils.empty;
import static com.publiccms.common.tools.CommonUtils.getDate;
import static com.publiccms.common.tools.CommonUtils.notEmpty;

import java.util.Date;

import org.publiccms.entities.sys.SysAppClient;
import org.springframework.stereotype.Repository;

import com.publiccms.common.base.BaseDao;
import com.publiccms.common.handler.PageHandler;
import com.publiccms.common.handler.QueryHandler;

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
     * @param allowPush
     * @param startLastLoginDate
     * @param endLastLoginDate
     * @param startCreateDate
     * @param endCreateDate
     * @param disabled
     * @param orderField
     * @param orderType
     * @param pageIndex
     * @param pageSize
     * @return
     */
    public PageHandler getPage(Integer siteId, String channel, Long userId, Boolean allowPush, Date startLastLoginDate,
            Date endLastLoginDate, Date startCreateDate, Date endCreateDate, Boolean disabled, String orderField,
            String orderType, Integer pageIndex, Integer pageSize) {
        QueryHandler queryHandler = getQueryHandler("from SysAppClient bean");
        if (notEmpty(siteId)) {
            queryHandler.condition("bean.id.siteId = :siteId").setParameter("siteId", siteId);
        }
        if (notEmpty(channel)) {
            queryHandler.condition("bean.id.channel = :channel").setParameter("channel", channel);
        }
        if (notEmpty(userId)) {
            queryHandler.condition("bean.userId = :userId").setParameter("userId", userId);
        }
        if (notEmpty(allowPush)) {
            queryHandler.condition("bean.allowPush = :allowPush").setParameter("allowPush", allowPush);
        }
        if (notEmpty(startLastLoginDate)) {
            queryHandler.condition("bean.lastLoginDate > :startLastLoginDate").setParameter("startLastLoginDate",
                    startLastLoginDate);
        }
        if (notEmpty(endLastLoginDate)) {
            queryHandler.condition("bean.lastLoginDate <= :endLastLoginDate").setParameter("endLastLoginDate", endLastLoginDate);
        }
        if (notEmpty(startCreateDate)) {
            queryHandler.condition("bean.createDate > :startCreateDate").setParameter("startCreateDate", startCreateDate);
        }
        if (notEmpty(endCreateDate)) {
            queryHandler.condition("bean.createDate <= :endCreateDate").setParameter("endCreateDate", endCreateDate);
        }
        if (notEmpty(disabled)) {
            queryHandler.condition("bean.disabled = :disabled").setParameter("disabled", disabled);
        }
        if (!ORDERTYPE_ASC.equalsIgnoreCase(orderType)) {
            orderType = ORDERTYPE_DESC;
        }
        if (null == orderField) {
            orderField = BLANK;
        }
        switch (orderField) {
        case "lastLoginDate":
            queryHandler.order("bean.lastLoginDate " + orderType);
            break;
        case "createDate":
            queryHandler.order("bean.createDate " + orderType);
            break;
        default:
            queryHandler.order("bean.createDate " + orderType);
        }
        return getPage(queryHandler, pageIndex, pageSize);
    }

    @Override
    protected SysAppClient init(SysAppClient entity) {
        if (empty(entity.getCreateDate())) {
            entity.setCreateDate(getDate());
        }
        return entity;
    }

}
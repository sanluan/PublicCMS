package com.publiccms.logic.dao.cms;

// Generated 2018-11-7 16:25:06 by com.publiccms.common.generator.SourceGenerator

import org.springframework.stereotype.Repository;

import com.publiccms.common.base.BaseDao;
import com.publiccms.common.constants.CommonConstants;
import com.publiccms.common.handler.PageHandler;
import com.publiccms.common.handler.QueryHandler;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.entities.cms.CmsComment;

/**
 *
 * CmsCommentDao
 * 
 */
@Repository
public class CmsCommentDao extends BaseDao<CmsComment> {

    /**
     * 
     * @param siteId
     * @param userId
     * @param replyId
     * @param emptyReply 
     * @param replyUserId
     * @param contentId
     * @param checkUserId
     * @param status
     * @param disabled
     * @param orderField
     * @param orderType
     * @param pageIndex
     * @param pageSize
     * @return results page
     */
    public PageHandler getPage(Short siteId, Long userId, Long replyId, Boolean emptyReply, Long replyUserId, Long contentId,
            Long checkUserId, Integer status, Boolean disabled, String orderField, String orderType, Integer pageIndex,
            Integer pageSize) {
        QueryHandler queryHandler = getQueryHandler("from CmsComment bean");
        if (CommonUtils.notEmpty(siteId)) {
            queryHandler.condition("bean.siteId = :siteId").setParameter("siteId", siteId);
        }
        if (CommonUtils.notEmpty(userId)) {
            queryHandler.condition("bean.userId = :userId").setParameter("userId", userId);
        }
        if (CommonUtils.notEmpty(replyId)) {
            queryHandler.condition("bean.replyId = :replyId").setParameter("replyId", replyId);
        } else if (null != emptyReply && emptyReply) {
            queryHandler.condition("bean.replyId is null");
        }
        if (CommonUtils.notEmpty(replyUserId)) {
            queryHandler.condition("bean.replyUserId = :replyUserId").setParameter("replyUserId", replyUserId);
        }
        if (CommonUtils.notEmpty(contentId)) {
            queryHandler.condition("bean.contentId = :contentId").setParameter("contentId", contentId);
        }
        if (CommonUtils.notEmpty(checkUserId)) {
            queryHandler.condition("bean.checkUserId = :checkUserId").setParameter("checkUserId", checkUserId);
        }
        if (CommonUtils.notEmpty(status)) {
            queryHandler.condition("bean.status = :status").setParameter("status", status);
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
        case "checkDate":
            queryHandler.order("bean.checkDate " + orderType);
            break;
        case "updateDate":
            queryHandler.order("bean.updateDate " + orderType);
            break;
        case "createDate":
            queryHandler.order("bean.createDate " + orderType);
            break;
        default:
            queryHandler.order("bean.id " + orderType);
        }
        return getPage(queryHandler, pageIndex, pageSize);
    }

    @Override
    protected CmsComment init(CmsComment entity) {
        if (null == entity.getCreateDate()) {
            entity.setCreateDate(CommonUtils.getDate());
        }
        return entity;
    }

}
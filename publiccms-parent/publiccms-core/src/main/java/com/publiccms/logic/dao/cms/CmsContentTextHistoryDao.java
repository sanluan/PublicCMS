package com.publiccms.logic.dao.cms;

// Generated 2022-5-10 by com.publiccms.common.generator.SourceGenerator

import org.springframework.stereotype.Repository;

import com.publiccms.common.base.BaseDao;
import com.publiccms.common.handler.PageHandler;
import com.publiccms.common.handler.QueryHandler;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.entities.cms.CmsContentTextHistory;

/**
 *
 * CmsContentHistoryDao
 * 
 */
@Repository
public class CmsContentTextHistoryDao extends BaseDao<CmsContentTextHistory> {

    /**
     * @param contentId
     * @param fieldName
     * @param userId
     * @param orderType
     * @param pageIndex
     * @param pageSize
     * @return results page
     */
    public PageHandler getPage(Long contentId, String fieldName, Long userId, String orderType, Integer pageIndex,
            Integer pageSize) {
        QueryHandler queryHandler = getQueryHandler("select new CmsContentTextHistory(id, contentId, fieldName, createDate, userId) from CmsContentTextHistory bean");
        if (CommonUtils.notEmpty(contentId)) {
            queryHandler.condition("bean.contentId = :contentId").setParameter("contentId", contentId);
        }
        if (CommonUtils.notEmpty(fieldName)) {
            queryHandler.condition("bean.fieldName = :fieldName").setParameter("fieldName", fieldName);
        }
        if (CommonUtils.notEmpty(userId)) {
            queryHandler.condition("bean.userId = :userId").setParameter("userId", userId);
        }
        if (!ORDERTYPE_ASC.equalsIgnoreCase(orderType)) {
            orderType = ORDERTYPE_DESC;
        }
        queryHandler.order("bean.createDate").append(orderType);
        return getPage(queryHandler, pageIndex, pageSize);
    }

    @Override
    protected CmsContentTextHistory init(CmsContentTextHistory entity) {
        if (null == entity.getCreateDate()) {
            entity.setCreateDate(CommonUtils.getDate());
        }
        return entity;
    }

}
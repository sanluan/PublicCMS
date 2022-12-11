package com.publiccms.logic.dao.cms;

// Generated 2022-5-10 by com.publiccms.common.generator.SourceGenerator

import org.springframework.stereotype.Repository;

import com.publiccms.common.base.BaseDao;
import com.publiccms.common.handler.PageHandler;
import com.publiccms.common.handler.QueryHandler;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.entities.cms.CmsEditorHistory;

/**
 *
 * CmsContentHistoryDao
 * 
 */
@Repository
public class CmsEditorHistoryDao extends BaseDao<CmsEditorHistory> {

    /**
     * @param itemType
     * @param itemId
     * @param fieldName
     * @param userId
     * @param orderType
     * @param pageIndex
     * @param pageSize
     * @return results page
     */
    public PageHandler getPage(String itemType, String itemId, String fieldName, Long userId, String orderType,
            Integer pageIndex, Integer pageSize) {
        QueryHandler queryHandler = getQueryHandler(
                "select new CmsEditorHistory(id,itemType, itemId, fieldName, createDate, userId) from CmsEditorHistory bean");
        if (CommonUtils.notEmpty(itemType)) {
            queryHandler.condition("bean.itemType = :itemType").setParameter("itemType", itemType);
        }
        if (CommonUtils.notEmpty(itemId)) {
            queryHandler.condition("bean.itemId = :itemId").setParameter("itemId", itemId);
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
    protected CmsEditorHistory init(CmsEditorHistory entity) {
        if (null == entity.getCreateDate()) {
            entity.setCreateDate(CommonUtils.getDate());
        }
        return entity;
    }

}
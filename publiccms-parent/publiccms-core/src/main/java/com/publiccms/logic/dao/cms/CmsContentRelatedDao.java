package com.publiccms.logic.dao.cms;

import org.springframework.stereotype.Repository;

import com.publiccms.common.base.BaseDao;
import com.publiccms.common.constants.CommonConstants;
import com.publiccms.common.handler.PageHandler;
import com.publiccms.common.handler.QueryHandler;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.entities.cms.CmsContentRelated;

/**
 *
 * CmsContentRelatedDao
 * 
 */
@Repository
public class CmsContentRelatedDao extends BaseDao<CmsContentRelated> {
    
    /**
     * @param contentId
     * @param relatedContentId
     * @param userId
     * @param orderField
     * @param orderType
     * @param pageIndex
     * @param pageSize
     * @return results page
     */
    public PageHandler getPage(Long contentId, Long relatedContentId, Long userId, String orderField, String orderType,
            Integer pageIndex, Integer pageSize) {
        QueryHandler queryHandler = getQueryHandler("from CmsContentRelated bean");
        if (CommonUtils.notEmpty(contentId)) {
            queryHandler.condition("bean.contentId = :contentId").setParameter("contentId", contentId);
        }
        if (CommonUtils.notEmpty(relatedContentId)) {
            queryHandler.condition("bean.relatedContentId = :relatedContentId")
                    .setParameter("relatedContentId", relatedContentId);
        }
        if (CommonUtils.notEmpty(userId)) {
            queryHandler.condition("bean.userId = :userId").setParameter("userId", userId);
        }
        if (!ORDERTYPE_ASC.equalsIgnoreCase(orderType)) {
            orderType = ORDERTYPE_DESC;
        }
        if (null == orderField) {
            orderField = CommonConstants.BLANK;
        }
        switch (orderField) {
        case "clicks":
            queryHandler.order("bean.clicks ").append(orderType);
            break;
        default:
            queryHandler.order("bean.sort asc,bean.id asc");
        }
        return getPage(queryHandler, pageIndex, pageSize);
    }

    @Override
    protected CmsContentRelated init(CmsContentRelated entity) {
        return entity;
    }

}
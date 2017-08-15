package org.publiccms.logic.dao.cms;

// Generated 2016-1-25 10:53:21 by com.publiccms.common.source.SourceGenerator

import static com.publiccms.common.tools.CommonUtils.notEmpty;

import org.publiccms.entities.cms.CmsContentRelated;
import org.springframework.stereotype.Repository;

import com.publiccms.common.base.BaseDao;
import com.publiccms.common.handler.PageHandler;
import com.publiccms.common.handler.QueryHandler;

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
     * @return
     */
    public PageHandler getPage(Long contentId, Long relatedContentId, Long userId, String orderField, String orderType,
            Integer pageIndex, Integer pageSize) {
        QueryHandler queryHandler = getQueryHandler("from CmsContentRelated bean");
        if (notEmpty(contentId)) {
            queryHandler.condition("bean.contentId = :contentId").setParameter("contentId", contentId);
        }
        if (notEmpty(relatedContentId)) {
            queryHandler.condition("bean.relatedContentId = :relatedContentId")
                    .setParameter("relatedContentId", relatedContentId);
        }
        if (notEmpty(userId)) {
            queryHandler.condition("bean.userId = :userId").setParameter("userId", userId);
        }
        if (!ORDERTYPE_ASC.equalsIgnoreCase(orderType)) {
            orderType = ORDERTYPE_DESC;
        }
        if (null == orderField) {
            orderField = BLANK;
        }
        switch (orderField) {
        case "clicks":
            queryHandler.order("bean.clicks " + orderType);
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
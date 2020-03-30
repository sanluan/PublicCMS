package com.publiccms.logic.dao.cms;

// Generated 2020-3-26 11:46:48 by com.publiccms.common.generator.SourceGenerator

import org.springframework.stereotype.Repository;

import com.publiccms.common.base.BaseDao;
import com.publiccms.common.constants.CommonConstants;
import com.publiccms.common.handler.PageHandler;
import com.publiccms.common.handler.QueryHandler;
import com.publiccms.entities.cms.CmsVoteItem;

/**
 *
 * CmsVoteItemDao
 * 
 */
@Repository
public class CmsVoteItemDao extends BaseDao<CmsVoteItem> {

    /**
     * @param voteId
     * @param orderField
     * @param orderType
     * @param pageIndex
     * @param pageSize
     * @return results page
     */
    public PageHandler getPage(Long voteId, String orderField, String orderType, Integer pageIndex, Integer pageSize) {
        QueryHandler queryHandler = getQueryHandler("from CmsVoteItem bean");
        if (null != voteId) {
            queryHandler.condition("bean.voteId = :voteId").setParameter("voteId", voteId);
        }
        if (!ORDERTYPE_ASC.equalsIgnoreCase(orderType)) {
            orderType = ORDERTYPE_DESC;
        }
        if (null == orderField) {
            orderField = CommonConstants.BLANK;
        }
        switch (orderField) {
        case "scores":
            queryHandler.order("bean.scores ").append(orderType);
            break;
        default:
            queryHandler.order("bean.sort asc");
        }
        return getPage(queryHandler, pageIndex, pageSize);
    }

    @Override
    protected CmsVoteItem init(CmsVoteItem entity) {
        return entity;
    }

    public void deleteByVoteId(Long voteId) {
        if (null != voteId) {
            QueryHandler queryHandler = getQueryHandler("update from CmsVoteItem bean set bean.disabled = :disabled");
            queryHandler.condition("bean.voteId = :voteId").setParameter("voteId", voteId).setParameter("disabled", true);
        }
    }

}
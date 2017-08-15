package org.publiccms.logic.dao.cms;

//Generated 2016-3-3 17:43:34 by com.publiccms.common.source.SourceGenerator
import static com.publiccms.common.tools.CommonUtils.notEmpty;

import org.publiccms.entities.cms.CmsVoteItem;
import org.springframework.stereotype.Repository;

import com.publiccms.common.base.BaseDao;
import com.publiccms.common.handler.PageHandler;
import com.publiccms.common.handler.QueryHandler;

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
     * @return
     */
    public PageHandler getPage(Integer voteId, 
                String orderField, String orderType, Integer pageIndex, Integer pageSize) {
        QueryHandler queryHandler = getQueryHandler("from CmsVoteItem bean");
        if (notEmpty(voteId)) {
            queryHandler.condition("bean.voteId = :voteId").setParameter("voteId", voteId);
        }
        if(!ORDERTYPE_ASC.equalsIgnoreCase(orderType)){
            orderType = ORDERTYPE_DESC;
        }
        if(null == orderField){
            orderField=BLANK;
        }
        switch(orderField) {
            case "scores" : queryHandler.order("bean.scores " + orderType); break;
            case "sort" : queryHandler.order("bean.sort " + orderType); break;
            default : queryHandler.order("bean.id " + orderType);
        }
        return getPage(queryHandler, pageIndex, pageSize);
    }

    @Override
    protected CmsVoteItem init(CmsVoteItem entity) {
        return entity;
    }

}
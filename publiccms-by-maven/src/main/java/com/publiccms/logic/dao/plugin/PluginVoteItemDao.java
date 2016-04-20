package com.publiccms.logic.dao.plugin;

// Generated 2016-3-3 17:43:34 by com.sanluan.common.source.SourceMaker


import org.springframework.stereotype.Repository;

import com.publiccms.entities.plugin.PluginVoteItem;
import com.sanluan.common.base.BaseDao;
import com.sanluan.common.handler.PageHandler;
import com.sanluan.common.handler.QueryHandler;

@Repository
public class PluginVoteItemDao extends BaseDao<PluginVoteItem> {
    public PageHandler getPage(Integer voteId, 
                String orderField, String orderType, Integer pageIndex, Integer pageSize) {
        QueryHandler queryHandler = getQueryHandler("from PluginVoteItem bean");
        if (notEmpty(voteId)) {
            queryHandler.condition("bean.voteId = :voteId").setParameter("voteId", voteId);
        }
        if("asc".equalsIgnoreCase(orderType)){
            orderType = "asc";
        }else{
            orderType = "desc";
        }
        if(null == orderField){
            orderField="";
        }
        switch(orderField) {
            case "scores" : queryHandler.order("bean.scores " + orderType); break;
            case "sort" : queryHandler.order("bean.sort " + orderType); break;
            default : queryHandler.order("bean.id " + orderType);
        }
        return getPage(queryHandler, pageIndex, pageSize);
    }

    @Override
    protected PluginVoteItem init(PluginVoteItem entity) {
        return entity;
    }

}
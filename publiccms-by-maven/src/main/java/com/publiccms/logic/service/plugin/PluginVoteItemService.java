package com.publiccms.logic.service.plugin;

// Generated 2016-3-3 17:43:34 by com.sanluan.common.source.SourceMaker


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.publiccms.entities.plugin.PluginVoteItem;
import com.publiccms.logic.dao.plugin.PluginVoteItemDao;
import com.sanluan.common.base.BaseService;
import com.sanluan.common.handler.PageHandler;

@Service
@Transactional
public class PluginVoteItemService extends BaseService<PluginVoteItem> {

    @Transactional(readOnly = true)
    public PageHandler getPage(Integer voteId, 
                String orderField, String orderType, Integer pageIndex, Integer pageSize) {
        return dao.getPage(voteId, 
                orderField, orderType, pageIndex, pageSize);
    }
    
    @Autowired
    private PluginVoteItemDao dao;
}
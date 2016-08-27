package com.publiccms.logic.service.plugin;

// Generated 2016-3-3 17:46:07 by com.sanluan.common.source.SourceMaker

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.publiccms.entities.plugin.PluginVoteUser;
import com.publiccms.logic.dao.plugin.PluginVoteUserDao;
import com.sanluan.common.base.BaseService;
import com.sanluan.common.handler.PageHandler;

@Service
@Transactional
public class PluginVoteUserService extends BaseService<PluginVoteUser> {

    @Transactional(readOnly = true)
    public PageHandler getPage(Integer lotteryId, Long userId, 
                String ip, Date startCreateDate, Date endCreateDate, 
                String orderType, Integer pageIndex, Integer pageSize) {
        return dao.getPage(lotteryId, userId, 
                ip, startCreateDate, endCreateDate, 
                orderType, pageIndex, pageSize);
    }
    
    @Autowired
    private PluginVoteUserDao dao;
}
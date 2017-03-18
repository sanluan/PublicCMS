package com.publiccms.logic.service.home;

// Generated 2016-11-13 11:38:14 by com.sanluan.common.source.SourceGenerator

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.publiccms.entities.home.HomeAttention;
import com.publiccms.logic.dao.home.HomeAttentionDao;
import com.sanluan.common.base.BaseService;
import com.sanluan.common.handler.PageHandler;

@Service
@Transactional
public class HomeAttentionService extends BaseService<HomeAttention> {

    @Transactional(readOnly = true)
    public PageHandler getPage(Long userId, Long attentionId, Integer pageIndex, Integer pageSize) {
        return dao.getPage(userId, attentionId, pageIndex, pageSize);
    }

    @Autowired
    private HomeAttentionDao dao;
}
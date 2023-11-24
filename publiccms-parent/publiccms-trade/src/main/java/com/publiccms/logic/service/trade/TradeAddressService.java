package com.publiccms.logic.service.trade;

// Generated 2023-8-16 by com.publiccms.common.generator.SourceGenerator

import javax.annotation.Resource;


import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.publiccms.entities.trade.TradeAddress;
import com.publiccms.logic.dao.trade.TradeAddressDao;
import com.publiccms.common.base.BaseService;
import com.publiccms.common.handler.PageHandler;

/**
 *
 * TradeAddressService
 * 
 */
@Service
@Transactional
public class TradeAddressService extends BaseService<TradeAddress> {

    /**
     * @param siteId
     * @param userId
     * @param pageIndex
     * @param pageSize
     * @return results page
     */
    @Transactional(readOnly = true)
    public PageHandler getPage(Short siteId, Long userId, 
                Integer pageIndex, Integer pageSize) {
        return dao.getPage(siteId, userId, 
                pageIndex, pageSize);
    }
    
    @Resource
    private TradeAddressDao dao;
    
}
package com.publiccms.logic.service.trade;

import java.util.List;

// Generated 2023-8-16 by com.publiccms.common.generator.SourceGenerator

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import com.publiccms.common.base.BaseService;
import com.publiccms.common.handler.PageHandler;
import com.publiccms.entities.trade.TradeAddress;
import com.publiccms.logic.dao.trade.TradeAddressDao;

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
    public PageHandler getPage(Short siteId, Long userId, Integer pageIndex, Integer pageSize) {
        return dao.getPage(siteId, userId, pageIndex, pageSize);
    }

    @Transactional(isolation = Isolation.READ_COMMITTED)
    public void delete(short siteId, Long[] ids, Long userId) {
        List<TradeAddress> entityList = getEntitys(ids);
        for (TradeAddress entity : entityList) {
            if (null != entity && siteId == entity.getSiteId() && (null == userId || entity.getUserId() == userId)) {
                dao.delete(entity);
            }
        }
    }
    
    @Resource
    private TradeAddressDao dao;
    
}
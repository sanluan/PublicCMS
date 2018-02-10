package com.publiccms.logic.service.cms;

import java.io.Serializable;

// Generated 2016-3-1 17:24:24 by com.publiccms.common.source.SourceGenerator

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.publiccms.common.base.BaseService;
import com.publiccms.common.handler.PageHandler;
import com.publiccms.entities.cms.CmsLottery;
import com.publiccms.logic.dao.cms.CmsLotteryDao;

/**
 *
 * CmsLotteryService
 * 
 */
@Service
@Transactional
public class CmsLotteryService extends BaseService<CmsLottery> {

    /**
     * @param siteId
     * @param startStartDate
     * @param endStartDate
     * @param startEndDate
     * @param endEndDate
     * @param disabled
     * @param orderField
     * @param orderType
     * @param pageIndex
     * @param pageSize
     * @return results page
     */
    @Transactional(readOnly = true)
    public PageHandler getPage(Short siteId, Date startStartDate, Date endStartDate, Date startEndDate, Date endEndDate,
            Boolean disabled, String orderField, String orderType, Integer pageIndex, Integer pageSize) {
        return dao.getPage(siteId, startStartDate, endStartDate, startEndDate, endEndDate, disabled, orderField, orderType,
                pageIndex, pageSize);
    }

    /**
     * @param id
     */
    public void delete(Serializable id) {
        CmsLottery entity = getEntity(id);
        if (null != entity) {
            entity.setDisabled(true);
        }
    }

    /**
     * @param id
     * @return whether update success
     */
    public boolean updateLastGift(Serializable id) {
        CmsLottery entity = getEntity(id);
        if (null != entity) {
            if (0 < entity.getLastGift()) {
                entity.setLastGift(entity.getLastGift() - 1);
                return true;
            }
        }
        return false;
    }

    @Autowired
    private CmsLotteryDao dao;

}
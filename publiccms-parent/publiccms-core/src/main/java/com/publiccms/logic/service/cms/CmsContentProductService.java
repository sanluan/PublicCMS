package com.publiccms.logic.service.cms;

// Generated 2021-6-26 17:53:08 by com.publiccms.common.generator.SourceGenerator
import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.publiccms.entities.cms.CmsContentProduct;
import com.publiccms.logic.dao.cms.CmsContentProductDao;
import com.publiccms.common.base.BaseService;
import com.publiccms.common.handler.PageHandler;

/**
 *
 * CmsContentProductService
 * 
 */
@Service
@Transactional
public class CmsContentProductService extends BaseService<CmsContentProduct> {

    /**
     * @param contentId
     * @param userId
     * @param price
     * @param orderField
     * @param orderType
     * @param pageIndex
     * @param pageSize
     * @return results page
     */
    @Transactional(readOnly = true)
    public PageHandler getPage(Long contentId, Long userId, BigDecimal price, String orderField, String orderType,
            Integer pageIndex, Integer pageSize) {
        return dao.getPage(contentId, userId, price, orderField, orderType, pageIndex, pageSize);
    }

    @Autowired
    private CmsContentProductDao dao;

}
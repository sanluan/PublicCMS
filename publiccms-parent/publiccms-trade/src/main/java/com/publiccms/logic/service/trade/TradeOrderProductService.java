package com.publiccms.logic.service.trade;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

// Generated 2021-6-26 22:16:13 by com.publiccms.common.generator.SourceGenerator

import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import com.publiccms.common.base.BaseService;
import com.publiccms.common.handler.PageHandler;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.entities.cms.CmsContent;
import com.publiccms.entities.cms.CmsContentProduct;
import com.publiccms.entities.trade.TradeOrderProduct;
import com.publiccms.logic.dao.trade.TradeOrderProductDao;
import com.publiccms.logic.service.cms.CmsContentProductService;
import com.publiccms.logic.service.cms.CmsContentService;

import jakarta.transaction.Transactional;

/**
 *
 * TradeOrderProductService
 * 
 */
@Service
@Transactional
public class TradeOrderProductService extends BaseService<TradeOrderProduct> {

    /**
     * @param siteId
     * @param orderId
     * @param pageIndex
     * @param pageSize
     * @return results page
     */
    @Transactional
    public PageHandler getPage(Short siteId, Long orderId, Integer pageIndex, Integer pageSize) {
        return dao.getPage(siteId, orderId, pageIndex, pageSize);
    }

    /**
     * @param siteId
     * @param orderId
     * @param pageIndex
     * @param pageSize
     * @return results page
     */
    @Transactional
    public List<TradeOrderProduct> getList(Short siteId, Long orderId) {
        return dao.getList(siteId, orderId);
    }

    @Transactional
    public BigDecimal create(short siteId, long orderId, List<TradeOrderProduct> tradeOrderProductList) {
        BigDecimal amount = BigDecimal.ZERO;
        if (null != tradeOrderProductList && 0 < tradeOrderProductList.size()) {
            Date now = CommonUtils.getDate();
            List<Long> contentIdsList = new ArrayList<>();
            List<Long> productIdsList = new ArrayList<>();
            for (TradeOrderProduct entity : tradeOrderProductList) {
                contentIdsList.add(entity.getContentId());
                productIdsList.add(entity.getProductId());
            }

            List<CmsContent> contentList = contentService.getEntitys(contentIdsList.toArray(new Long[contentIdsList.size()]));
            Map<Long, CmsContent> contentMap = CommonUtils.listToMap(contentList, k -> k.getId(), null, null);
            List<CmsContentProduct> productList = productService
                    .getEntitys(productIdsList.toArray(new Long[productIdsList.size()]));
            Map<Long, CmsContentProduct> productMap = CommonUtils.listToMap(productList, k -> k.getId(), null, null);

            for (TradeOrderProduct entity : tradeOrderProductList) {
                CmsContent content = contentMap.get(entity.getContentId());
                CmsContentProduct product = productMap.get(entity.getProductId());
                if (null != content && !content.isDisabled() && CmsContentService.STATUS_NORMAL == content.getStatus()
                        && now.after(content.getPublishDate())
                        && (null == content.getExpiryDate() || now.before(content.getExpiryDate())) && null != product
                        && product.getContentId() == content.getId() && 0 < product.getInventory() && 0 < entity.getQuantity()
                        && product.getInventory() >= entity.getQuantity()
                        && (null == product.getMinQuantity() || product.getMinQuantity() <= entity.getQuantity())
                        && (null == product.getMaxQuantity() || product.getMaxQuantity() >= entity.getQuantity())) {
                    entity.setId(null);
                    entity.setSiteId(siteId);
                    entity.setOrderId(orderId);
                    entity.setPrice(product.getPrice());
                    entity.setAmount(product.getPrice().multiply(new BigDecimal(entity.getQuantity())));
                    amount = amount.add(entity.getAmount());
                } else {
                    return null;
                }
            }
            save(tradeOrderProductList);
            return amount;
        }
        return null;
    }

    @Resource
    private TradeOrderProductDao dao;
    @Resource
    private CmsContentService contentService;
    @Resource
    private CmsContentProductService productService;
}
package com.publiccms.controller.web.trade;

import java.math.BigDecimal;
import java.util.Date;

// Generated 2021-6-26 20:16:25 by com.publiccms.common.generator.SourceGenerator

import javax.servlet.http.HttpServletRequest;
import javax.annotation.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.servlet.view.UrlBasedViewResolver;

import com.publiccms.common.annotation.Csrf;
import com.publiccms.common.api.PaymentGateway;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.common.tools.RequestUtils;
import com.publiccms.entities.sys.SysSite;
import com.publiccms.entities.sys.SysUser;
import com.publiccms.entities.trade.TradeOrder;
import com.publiccms.entities.trade.TradePayment;
import com.publiccms.logic.component.config.SafeConfigComponent;
import com.publiccms.logic.component.paymentprocessor.ProductProcessorComponent;
import com.publiccms.logic.component.trade.PaymentGatewayComponent;
import com.publiccms.logic.service.trade.TradeOrderService;
import com.publiccms.logic.service.trade.TradePaymentService;
import com.publiccms.views.pojo.model.TradeOrderParameters;

/**
 *
 * TradeOrderAdminController
 * 
 */
@Controller
@RequestMapping("tradeOrder")
public class TradeOrderController {
    /**
     * @param site
     * @param accountType
     * @param orderId
     * @param paymentType
     * @param returnUrl
     * @param request
     * @return
     */
    @RequestMapping(value = "pay/{accountType}")
    public String pay(@RequestAttribute SysSite site, @PathVariable("accountType") String accountType, long orderId,
            String paymentType, String returnUrl, HttpServletRequest request) {
        returnUrl = safeConfigComponent.getSafeUrl(returnUrl, site, request.getContextPath());
        PaymentGateway paymentGateway = gatewayComponent.get(accountType);
        TradeOrder order = service.getEntity(orderId);
        StringBuilder sb = new StringBuilder(UrlBasedViewResolver.REDIRECT_URL_PREFIX);
        if (null != paymentGateway && paymentGateway.enable(site.getId()) && null == order.getPaymentId()) {
            if (1 == order.getAmount().compareTo(BigDecimal.ZERO)) {
                String ip = RequestUtils.getIpAddress(request);
                Date now = CommonUtils.getDate();
                TradePayment entity = new TradePayment(site.getId(), order.getUserId(), order.getAmount(),
                        ProductProcessorComponent.GRADE_TYPE, String.valueOf(orderId), accountType, ip,
                        TradePaymentService.STATUS_PENDING_PAY, false, now);
                entity.setDescription(order.getTitle());
                paymentService.create(site.getId(), entity);
                service.pay(site.getId(), orderId, entity.getId());
                sb.append(site.getDynamicPath()).append("tradePayment/pay?paymentId=").append(entity.getId());
                if (CommonUtils.notEmpty(paymentType)) {
                    sb.append("&paymentType=").append(paymentType);
                }
                return sb.append("&returnUrl=").append(returnUrl).toString();
            }
        } else if (null != order.getPaymentId()) {
            sb.append(site.getDynamicPath()).append("tradePayment/pay?paymentId=").append(order.getPaymentId());
            if (CommonUtils.notEmpty(paymentType)) {
                sb.append("&paymentType=").append(paymentType);
            }
            return sb.append("&returnUrl=").append(returnUrl).toString();
        }
        return sb.append(returnUrl).toString();
    }

    /**
     * @param site
     * @param user
     * @param entity
     * @param tradeOrderParameters
     * @param orderIdField
     * @param returnUrl
     * @param request
     * @return operate result
     */
    @RequestMapping("create")
    @Csrf
    public String create(@RequestAttribute SysSite site, @SessionAttribute SysUser user, TradeOrder entity,
            @ModelAttribute TradeOrderParameters tradeOrderParameters, String orderIdField, String returnUrl,
            HttpServletRequest request) {
        returnUrl = safeConfigComponent.getSafeUrl(returnUrl, site, request.getContextPath());
        if (CommonUtils.empty(orderIdField)) {
            orderIdField = "orderId";
        }
        Long orderId = service.create(site.getId(), user.getId(), entity, RequestUtils.getIpAddress(request),
                tradeOrderParameters.getTradeOrderProductList());
        if (null != orderId) {
            return new StringBuilder(UrlBasedViewResolver.REDIRECT_URL_PREFIX).append(returnUrl).toString()
                    + (returnUrl.contains("?") ? "&" : "?") + orderIdField + "=" + orderId;
        } else {
            return new StringBuilder(UrlBasedViewResolver.REDIRECT_URL_PREFIX).append(returnUrl).toString();
        }
    }

    /**
     * @param site
     * @param user
     * @param orderId
     * @param returnUrl
     * @param request
     * @return operate result
     */
    @RequestMapping("close")
    @Csrf
    public String close(@RequestAttribute SysSite site, @SessionAttribute SysUser user, long orderId, String returnUrl,
            HttpServletRequest request) {
        returnUrl = safeConfigComponent.getSafeUrl(returnUrl, site, request.getContextPath());
        service.close(site.getId(), orderId, user.getId());
        return new StringBuilder(UrlBasedViewResolver.REDIRECT_URL_PREFIX).append(returnUrl).toString();
    }

    @Resource
    private TradeOrderService service;
    @Resource
    private TradePaymentService paymentService;
    @Resource
    protected SafeConfigComponent safeConfigComponent;
    @Resource
    private PaymentGatewayComponent gatewayComponent;
}
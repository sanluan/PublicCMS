package com.publiccms.controller.admin.trade;

import java.math.BigDecimal;

// Generated 2019-6-15 20:08:45 by com.publiccms.common.generator.SourceGenerator

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttribute;

import com.publiccms.common.annotation.Csrf;
import com.publiccms.common.api.PaymentGateway;
import com.publiccms.common.constants.CommonConstants;
import com.publiccms.common.tools.ControllerUtils;
import com.publiccms.entities.sys.SysSite;
import com.publiccms.entities.sys.SysUser;
import com.publiccms.entities.trade.TradeOrder;
import com.publiccms.entities.trade.TradeRefund;
import com.publiccms.logic.component.trade.PaymentGatewayComponent;
import com.publiccms.logic.service.trade.TradeOrderService;
import com.publiccms.logic.service.trade.TradeRefundService;

/**
 *
 * TradeRefundAdminController
 * 
 */
@Controller
@RequestMapping("tradeRefund")
public class TradeRefundAdminController {

    /**
     * @param site
     * @param admin
     * @param id
     * @param refundAmount
     * @param reply
     * @param request
     * @param model
     * @return operate result
     */
    @RequestMapping("refund")
    @Csrf
    public String save(@RequestAttribute SysSite site, @SessionAttribute SysUser admin, Long id, BigDecimal refundAmount,
            String reply, HttpServletRequest request, ModelMap model) {
        TradeRefund entity = service.getEntity(id);
        if (ControllerUtils.verifyNotEmpty("refund", entity, model)) {
            return CommonConstants.TEMPLATE_ERROR;
        }
        TradeOrder order = orderService.getEntity(entity.getOrderId());
        if (ControllerUtils.verifyNotEmpty("order", order, model)) {
            return CommonConstants.TEMPLATE_ERROR;
        }
        PaymentGateway paymentGateway = gatewayComponent.get(order.getAccountType());
        if (ControllerUtils.verifyNotEmpty("paymentGateway", paymentGateway, model)
                || ControllerUtils.verifyCustom("tradeOrderStatus", !orderService.refunded(site.getId(), order.getId()), model)
                || ControllerUtils.verifyCustom("refundStatus", !service.updateResund(id, refundAmount, reply), model)) {
            return CommonConstants.TEMPLATE_ERROR;
        }
        if (paymentGateway.refund(site, order, entity)) {
            service.updateStatus(entity.getId(), admin.getId(), TradeRefundService.STATUS_REFUNDED);
        } else {
            orderService.pendingRefund(site.getId(), order.getId());
            service.updateStatus(entity.getId(), admin.getId(), TradeRefundService.STATUS_FAIL);
        }
        return CommonConstants.TEMPLATE_DONE;
    }

    @Autowired
    private PaymentGatewayComponent gatewayComponent;
    @Autowired
    private TradeRefundService service;
    @Autowired
    private TradeOrderService orderService;
}
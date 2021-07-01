package com.publiccms.controller.admin.trade;

import java.math.BigDecimal;

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
import com.publiccms.entities.trade.TradePayment;
import com.publiccms.entities.trade.TradeRefund;
import com.publiccms.logic.component.trade.PaymentGatewayComponent;
import com.publiccms.logic.service.trade.TradePaymentService;
import com.publiccms.logic.service.trade.TradeRefundService;

/**
 *
 * TradeRefundAdminController
 * 
 */
@Controller
@RequestMapping("tradePayment")
public class TradePaymentAdminController {

    /**
     * @param site
     * @param admin
     * @param id
     * @param refundAmount
     * @param reply
     * @param model
     * @return operate result
     */
    @RequestMapping("refund")
    @Csrf
    public String refund(@RequestAttribute SysSite site, @SessionAttribute SysUser admin, Long id, BigDecimal refundAmount,
            String reply, ModelMap model) {
        TradeRefund entity = service.getEntity(id);
        if (ControllerUtils.verifyNotEmpty("refund", entity, model)) {
            return CommonConstants.TEMPLATE_ERROR;
        }
        TradePayment payment = paymentService.getEntity(entity.getPaymentId());
        if (ControllerUtils.verifyNotEmpty("payment", payment, model)) {
            return CommonConstants.TEMPLATE_ERROR;
        }
        PaymentGateway paymentGateway = gatewayComponent.get(payment.getAccountType());
        if (ControllerUtils.verifyNotEmpty("paymentGateway", paymentGateway, model) || ControllerUtils
                .verifyCustom("refundStatus", !service.updateResund(site.getId(), id, refundAmount, reply), model)) {
            return CommonConstants.TEMPLATE_ERROR;
        }
        if (paymentGateway.refund(site.getId(), payment, entity)) {
            service.updateStatus(site.getId(), entity.getId(), admin.getId(), TradeRefundService.STATUS_REFUNDED);
        } else {
            paymentService.pendingRefund(site.getId(), payment.getId());
            service.updateStatus(site.getId(), entity.getId(), admin.getId(), TradeRefundService.STATUS_FAIL);
        }
        return CommonConstants.TEMPLATE_DONE;
    }

    /**
     * @param site
     * @param admin
     * @param id
     * @param reply
     * @param model
     * @return operate result
     */
    @RequestMapping("refuse")
    @Csrf
    public String refuse(@RequestAttribute SysSite site, @SessionAttribute SysUser admin, Long id, String reply, ModelMap model) {
        TradeRefund entity = service.getEntity(id);
        if (ControllerUtils.verifyNotEmpty("refund", entity, model)) {
            return CommonConstants.TEMPLATE_ERROR;
        }
        service.refuseResund(site.getId(), id, admin.getId(), reply);
        return CommonConstants.TEMPLATE_DONE;
    }

    @Autowired
    private PaymentGatewayComponent gatewayComponent;
    @Autowired
    private TradeRefundService service;
    @Autowired
    private TradePaymentService paymentService;
}
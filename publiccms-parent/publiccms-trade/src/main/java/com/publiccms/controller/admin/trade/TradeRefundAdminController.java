package com.publiccms.controller.admin.trade;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.common.tools.ControllerUtils;
import com.publiccms.common.tools.JsonUtils;
import com.publiccms.common.tools.RequestUtils;
import com.publiccms.entities.log.LogOperate;
import com.publiccms.entities.sys.SysSite;
import com.publiccms.entities.sys.SysUser;
import com.publiccms.entities.trade.TradeOrder;
import com.publiccms.entities.trade.TradeRefund;
import com.publiccms.logic.service.log.LogLoginService;
import com.publiccms.logic.service.log.LogOperateService;
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
    private Map<String, PaymentGateway> paymentGatewayMap = new HashMap<>();

    /**
     * @param site
     * @param admin
     * @param id
     * @param refundAmount
     * @param reply
     * @param entity
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
        PaymentGateway paymentGateway = paymentGatewayMap.get(order.getAccountType());
        if (ControllerUtils.verifyNotEmpty("paymentGateway", paymentGateway, model)
                || ControllerUtils.verifyCustom("tradeOrderStatus", !orderService.refunded(order.getId()), model)
                || ControllerUtils.verifyCustom("refundStatus", !service.updateResund(id, refundAmount, reply), model)) {
            return CommonConstants.TEMPLATE_ERROR;
        }
        if (paymentGateway.refund(order, entity)) {
            service.updateStatus(entity.getId(), admin.getId(), TradeRefundService.STATUS_REFUNDED);
        } else {
            orderService.pendingRefund(order.getId());
            service.updateStatus(entity.getId(), admin.getId(), TradeRefundService.STATUS_FAIL);
        }
        logOperateService.save(new LogOperate(site.getId(), admin.getId(), LogLoginService.CHANNEL_WEB_MANAGER, "refund",
                RequestUtils.getIpAddress(request), CommonUtils.getDate(), JsonUtils.getString(service.getEntity(id))));
        return CommonConstants.TEMPLATE_DONE;
    }

    @Autowired(required = false)
    public void init(List<PaymentGateway> paymentGatewayList) {
        if (null != paymentGatewayList) {
            for (PaymentGateway paymentGateway : paymentGatewayList) {
                paymentGatewayMap.put(paymentGateway.getChannel(), paymentGateway);
            }
        }
    }

    @Autowired
    private TradeRefundService service;
    @Autowired
    private TradeOrderService orderService;
    @Autowired
    protected LogOperateService logOperateService;
}
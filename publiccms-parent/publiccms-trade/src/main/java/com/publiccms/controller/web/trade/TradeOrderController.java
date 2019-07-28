package com.publiccms.controller.web.trade;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.view.UrlBasedViewResolver;

import com.alipay.api.AlipayApiException;
import com.alipay.api.internal.util.AlipaySignature;
import com.github.wxpay.sdk.WXPay;
import com.github.wxpay.sdk.WXPayConstants;
import com.github.wxpay.sdk.WXPayUtil;
import com.publiccms.common.annotation.Csrf;
import com.publiccms.common.api.Config;
import com.publiccms.common.api.PaymentGateway;
import com.publiccms.common.api.TradeOrderProcessor;
import com.publiccms.common.constants.CommonConstants;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.common.tools.ControllerUtils;
import com.publiccms.common.tools.JsonUtils;
import com.publiccms.entities.sys.SysSite;
import com.publiccms.entities.sys.SysUser;
import com.publiccms.entities.trade.TradeOrder;
import com.publiccms.entities.trade.TradeOrderHistory;
import com.publiccms.entities.trade.TradeRefund;
import com.publiccms.logic.component.config.ConfigComponent;
import com.publiccms.logic.component.config.LoginConfigComponent;
import com.publiccms.logic.component.paymentgateway.AlipayGatewayComponent;
import com.publiccms.logic.component.paymentgateway.WechatConfig;
import com.publiccms.logic.component.paymentgateway.WechatGatewayComponent;
import com.publiccms.logic.component.trade.PaymentGatewayComponent;
import com.publiccms.logic.component.trade.TradeOrderProcessorComponent;
import com.publiccms.logic.service.trade.TradeOrderHistoryService;
import com.publiccms.logic.service.trade.TradeOrderService;
import com.publiccms.logic.service.trade.TradeRefundService;

@Controller
@RequestMapping("tradeOrder")
public class TradeOrderController {
    /**
     * @param site
     * @param accountType
     * @param orderId
     * @param returnUrl
     * @param request
     * @param response
     * @param model
     * @throws Exception
     */
    @RequestMapping(value = "pay/{accountType}")
    public void pay(@RequestAttribute SysSite site, @PathVariable("accountType") String accountType, Long orderId,
            String returnUrl, HttpServletRequest request, HttpServletResponse response, ModelMap model) throws Exception {
        Map<String, String> config = configComponent.getConfigData(site.getId(), Config.CONFIG_CODE_SITE);
        String safeReturnUrl = config.get(LoginConfigComponent.CONFIG_RETURN_URL);
        if (ControllerUtils.isUnSafeUrl(returnUrl, site, safeReturnUrl, request)) {
            returnUrl = site.isUseStatic() ? site.getSitePath() : site.getDynamicPath();
        }
        PaymentGateway paymentGateway = gatewayComponent.get(accountType);
        TradeOrder entity = service.getEntity(orderId);
        if (null != paymentGateway && null == entity
                || ControllerUtils.verifyNotEquals("siteId", site.getId(), entity.getSiteId(), model)) {
            response.sendRedirect(returnUrl);
        }
        if (!paymentGateway.pay(site, entity, returnUrl, response)) {
            response.sendRedirect(returnUrl);
        }
    }

    /**
     * @param site
     * @param out_trade_no
     * @param total_fee
     * @param trade_no
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "notify/alipay")
    @ResponseBody
    public String notifyAlipay(@RequestAttribute SysSite site, long out_trade_no, String total_fee, String trade_no,
            HttpServletRequest request) throws Exception {
        Map<String, String> config = configComponent.getConfigData(site.getId(), AlipayGatewayComponent.CONFIG_CODE);
        if (CommonUtils.notEmpty(config)) {
            Map<String, String> params = request.getParameterMap().entrySet().stream()
                    .collect(Collectors.toMap(e -> e.getKey(), e -> StringUtils.join(e.getValue(), ",")));
            try {
                if (AlipaySignature.rsaCheckV1(params, config.get(AlipayGatewayComponent.CONFIG_ALIPAY_PUBLIC_KEY),
                        CommonConstants.DEFAULT_CHARSET_NAME, "RSA2")) {
                    try {
                        TradeOrderHistory history = new TradeOrderHistory(site.getId(), out_trade_no, CommonUtils.getDate(),
                                TradeOrderHistoryService.OPERATE_NOTIFY, JsonUtils.getString(params));
                        historyService.save(history);
                        TradeOrder order = service.getEntity(out_trade_no);
                        if (null != order && order.getStatus() == TradeOrderService.STATUS_PENDING_PAY
                                && order.getAmount().toString().equals(total_fee)) {
                            if (service.paid(site.getId(), order.getId(), trade_no)) {
                                TradeOrderProcessor tradeOrderProcessor = tradeOrderProcessorComponent.get(order.getTradeType());
                                if (null != tradeOrderProcessor && tradeOrderProcessor.paid(order)) {
                                    service.processed(order.getSiteId(), order.getId());
                                } else {
                                    history = new TradeOrderHistory(order.getSiteId(), order.getId(), CommonUtils.getDate(),
                                            TradeOrderHistoryService.OPERATE_PROCESS_ERROR);
                                    historyService.save(history);
                                }
                            }
                        }
                        return "success";
                    } catch (NumberFormatException e) {
                    }
                }
            } catch (AlipayApiException e) {
            }
        }
        return "fail";

    }

    /**
     * @param site
     * @param body
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "notify/wechat")
    @ResponseBody
    public String notifyWechat(@RequestAttribute SysSite site, @RequestBody String body) throws Exception {
        Map<String, String> config = configComponent.getConfigData(site.getId(), AlipayGatewayComponent.CONFIG_CODE);
        if (CommonUtils.notEmpty(config)) {
            WechatConfig wechatConfig = new WechatConfig(config.get(WechatGatewayComponent.CONFIG_APPID),
                    config.get(WechatGatewayComponent.CONFIG_MCHID), config.get(WechatGatewayComponent.CONFIG_KEY),
                    config.get(WechatGatewayComponent.CONFIG_CERT));
            boolean usesandbox = CommonUtils.notEmpty(config.get(WechatGatewayComponent.CONFIG_USESANDBOX))
                    && "true".equals(config.get(WechatGatewayComponent.CONFIG_USESANDBOX));
            try {
                WXPay wxpay = new WXPay(wechatConfig, usesandbox);
                Map<String, String> result = wxpay.processResponseXml(body);
                if (WXPayConstants.SUCCESS.equalsIgnoreCase(result.get("return_code"))) {
                    long orderId = Long.parseLong(result.get("out_trade_no"));
                    TradeOrderHistory history = new TradeOrderHistory(site.getId(), orderId, CommonUtils.getDate(),
                            TradeOrderHistoryService.OPERATE_NOTIFY, body);
                    historyService.save(history);
                    if (WXPayConstants.SUCCESS.equalsIgnoreCase(result.get("result_code"))) {
                        TradeOrder order = service.getEntity(orderId);
                        if (null != order && order.getStatus() == TradeOrderService.STATUS_PENDING_PAY
                                && order.getAmount().toString().equals(result.get("total_fee"))) {
                            if (service.paid(site.getId(), order.getId(), result.get("transaction_id"))) {
                                TradeOrderProcessor tradeOrderProcessor = tradeOrderProcessorComponent.get(order.getTradeType());
                                if (null != tradeOrderProcessor && tradeOrderProcessor.paid(order)) {
                                    service.processed(order.getSiteId(), order.getId());
                                } else {
                                    history = new TradeOrderHistory(order.getSiteId(), order.getId(), CommonUtils.getDate(),
                                            TradeOrderHistoryService.OPERATE_PROCESS_ERROR);
                                    historyService.save(history);
                                }
                            }
                        }
                    }
                }
            } catch (Exception e) {
            }
        }
        Map<String, String> response = new HashMap<>();
        response.put("return_code", WXPayConstants.SUCCESS);
        response.put("return_msg", "OK");
        return WXPayUtil.mapToXml(response);
    }

    /**
     * @param site
     * @param entity
     * @param returnUrl
     * @param request
     * @param session
     * @param model
     * @return
     */
    @RequestMapping(value = "refund")
    @Csrf
    public String refund(@RequestAttribute SysSite site, TradeRefund entity, String returnUrl, HttpServletRequest request,
            HttpSession session, ModelMap model) {
        Map<String, String> config = configComponent.getConfigData(site.getId(), Config.CONFIG_CODE_SITE);
        String safeReturnUrl = config.get(LoginConfigComponent.CONFIG_RETURN_URL);
        if (ControllerUtils.isUnSafeUrl(returnUrl, site, safeReturnUrl, request)) {
            returnUrl = site.isUseStatic() ? site.getSitePath() : site.getDynamicPath();
        }
        SysUser user = ControllerUtils.getUserFromSession(session);
        if (null != user && ControllerUtils.verifyCustom("tradeOrderStatus",
                !service.pendingRefund(site.getId(), entity.getOrderId()), model)) {
            return CommonConstants.TEMPLATE_ERROR;
        }
        if (null == entity.getId()) {
            entity.setRefundAmount(null);
            entity.setReply(null);
            entity.setRefundUserId(null);
            refundService.save(entity);
        } else {
            refundService.updateAmound(entity.getId(), entity.getAmount(), entity.getReason());
        }
        return UrlBasedViewResolver.REDIRECT_URL_PREFIX + returnUrl;
    }

    @Autowired
    private PaymentGatewayComponent gatewayComponent;
    @Autowired
    protected ConfigComponent configComponent;
    @Autowired
    private TradeOrderService service;
    @Autowired
    private TradeRefundService refundService;
    @Autowired
    private TradeOrderHistoryService historyService;
    @Autowired
    private TradeOrderProcessorComponent tradeOrderProcessorComponent;

}

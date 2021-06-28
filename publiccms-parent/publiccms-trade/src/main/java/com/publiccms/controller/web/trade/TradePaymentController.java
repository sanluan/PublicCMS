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
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.servlet.view.UrlBasedViewResolver;

import com.alipay.api.AlipayApiException;
import com.alipay.api.internal.util.AlipaySignature;
import com.github.wxpay.sdk.WXPay;
import com.github.wxpay.sdk.WXPayConstants;
import com.github.wxpay.sdk.WXPayUtil;
import com.publiccms.common.annotation.Csrf;
import com.publiccms.common.api.Config;
import com.publiccms.common.api.PaymentGateway;
import com.publiccms.common.api.TradePaymentProcessor;
import com.publiccms.common.constants.CommonConstants;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.common.tools.ControllerUtils;
import com.publiccms.common.tools.JsonUtils;
import com.publiccms.entities.sys.SysSite;
import com.publiccms.entities.sys.SysUser;
import com.publiccms.entities.trade.TradePayment;
import com.publiccms.entities.trade.TradePaymentHistory;
import com.publiccms.entities.trade.TradeRefund;
import com.publiccms.logic.component.config.ConfigComponent;
import com.publiccms.logic.component.config.LoginConfigComponent;
import com.publiccms.logic.component.paymentgateway.AlipayGatewayComponent;
import com.publiccms.logic.component.paymentgateway.WechatConfig;
import com.publiccms.logic.component.paymentgateway.WechatGatewayComponent;
import com.publiccms.logic.component.trade.PaymentGatewayComponent;
import com.publiccms.logic.component.trade.PaymentProcessorComponent;
import com.publiccms.logic.service.trade.TradePaymentHistoryService;
import com.publiccms.logic.service.trade.TradePaymentService;
import com.publiccms.logic.service.trade.TradeRefundService;

@Controller
@RequestMapping("tradePayment")
public class TradePaymentController {

    /**
     * @param site
     * @param accountType
     * @param paymentId
     * @param returnUrl
     * @param request
     * @param response
     * @param model
     * @throws Exception
     */
    @RequestMapping(value = "pay")
    public void pay(@RequestAttribute SysSite site, Long paymentId, String returnUrl, HttpServletRequest request,
            HttpServletResponse response, ModelMap model) throws Exception {
        Map<String, String> config = configComponent.getConfigData(site.getId(), Config.CONFIG_CODE_SITE);
        String safeReturnUrl = config.get(LoginConfigComponent.CONFIG_RETURN_URL);
        if (ControllerUtils.isUnSafeUrl(returnUrl, site, safeReturnUrl, request)) {
            returnUrl = site.isUseStatic() ? site.getSitePath() : site.getDynamicPath();
        }
        TradePayment entity = service.getEntity(paymentId);
        PaymentGateway paymentGateway = gatewayComponent.get(entity.getAccountType());
        if (null != paymentGateway && null == entity
                || ControllerUtils.verifyNotEquals("siteId", site.getId(), entity.getSiteId(), model)) {
            response.sendRedirect(returnUrl);
        } else if (!paymentGateway.pay(site, entity, returnUrl, response)) {
            response.sendRedirect(returnUrl);
        }
    }

    /**
     * @param site
     * @param paymentId
     * @param returnUrl
     * @param request
     * @param response
     * @param model
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "cancel")
    @Csrf
    public String cancel(@RequestAttribute SysSite site, Long paymentId, String returnUrl, HttpServletRequest request,
            HttpServletResponse response, ModelMap model) throws Exception {
        Map<String, String> config = configComponent.getConfigData(site.getId(), Config.CONFIG_CODE_SITE);
        String safeReturnUrl = config.get(LoginConfigComponent.CONFIG_RETURN_URL);
        if (ControllerUtils.isUnSafeUrl(returnUrl, site, safeReturnUrl, request)) {
            returnUrl = site.isUseStatic() ? site.getSitePath() : site.getDynamicPath();
        }
        TradePayment entity = service.getEntity(paymentId);
        if (null == entity || ControllerUtils.verifyNotEquals("siteId", site.getId(), entity.getSiteId(), model)) {
            return UrlBasedViewResolver.REDIRECT_URL_PREFIX + returnUrl;
        }
        TradePaymentProcessor paymentProcessor = paymentProcessorComponent.get(entity.getTradeType());
        if (null != paymentProcessor && service.cancel(site.getId(), paymentId)) {
            paymentProcessor.cancel(entity);
        }
        return UrlBasedViewResolver.REDIRECT_URL_PREFIX + returnUrl;
    }

    /**
     * @param site
     * @param out_trade_no
     * @param total_fee
     * @param trade_no
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "notify/alipay")
    @ResponseBody
    public String notifyAlipay(@RequestAttribute SysSite site, long out_trade_no, String total_fee, String trade_no,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        Map<String, String> config = configComponent.getConfigData(site.getId(), AlipayGatewayComponent.CONFIG_CODE);
        if (CommonUtils.notEmpty(config)) {
            Map<String, String> params = request.getParameterMap().entrySet().stream()
                    .collect(Collectors.toMap(e -> e.getKey(), e -> StringUtils.join(e.getValue(), ",")));
            try {
                if (AlipaySignature.rsaCheckV1(params, config.get(AlipayGatewayComponent.CONFIG_ALIPAY_PUBLIC_KEY),
                        CommonConstants.DEFAULT_CHARSET_NAME, "RSA2")) {
                    try {
                        TradePaymentHistory history = new TradePaymentHistory(site.getId(), out_trade_no, CommonUtils.getDate(),
                                TradePaymentHistoryService.OPERATE_NOTIFY, JsonUtils.getString(params));
                        historyService.save(history);
                        TradePayment payment = service.getEntity(out_trade_no);
                        if (null != payment && payment.getStatus() == TradePaymentService.STATUS_PENDING_PAY
                                && payment.getAmount().toString().equals(total_fee)) {
                            if (service.paid(site.getId(), payment.getId(), trade_no)) {
                                payment = service.getEntity(payment.getId());
                                alipayGatewayComponent.confirmPay(site, payment, response);
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
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "notify/wechat")
    @ResponseBody
    public String notifyWechat(@RequestAttribute SysSite site, @RequestBody String body, HttpServletResponse response)
            throws Exception {
        Map<String, String> config = configComponent.getConfigData(site.getId(), AlipayGatewayComponent.CONFIG_CODE);
        Map<String, String> resultMap = new HashMap<>();
        resultMap.put("return_code", WXPayConstants.FAIL);
        resultMap.put("return_msg", "OK");
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
                    long paymentId = Long.parseLong(result.get("out_trade_no"));
                    TradePaymentHistory history = new TradePaymentHistory(site.getId(), paymentId, CommonUtils.getDate(),
                            TradePaymentHistoryService.OPERATE_NOTIFY, body);
                    historyService.save(history);
                    if (WXPayConstants.SUCCESS.equalsIgnoreCase(result.get("result_code"))) {
                        TradePayment payment = service.getEntity(paymentId);
                        if (null != payment && payment.getStatus() == TradePaymentService.STATUS_PENDING_PAY
                                && payment.getAmount().toString().equals(result.get("total_fee"))) {
                            payment = service.getEntity(paymentId);
                            if (service.paid(site.getId(), payment.getId(), result.get("transaction_id"))) {
                                if (wechatGatewayComponent.confirmPay(site, payment, response)) {
                                    resultMap.put("return_code", WXPayConstants.SUCCESS);
                                    resultMap.put("return_msg", "OK");
                                }
                            }
                        }
                    }
                }
            } catch (Exception e) {
            }
        }
        return WXPayUtil.mapToXml(resultMap);
    }

    /**
     * @param site
     * @param user
     * @param entity
     * @param returnUrl
     * @param request
     * @param session
     * @param model
     * @return
     */
    @RequestMapping(value = "refund")
    @Csrf
    public String refund(@RequestAttribute SysSite site, @SessionAttribute SysUser user, TradeRefund entity, String returnUrl,
            HttpServletRequest request, HttpSession session, ModelMap model) {
        Map<String, String> config = configComponent.getConfigData(site.getId(), Config.CONFIG_CODE_SITE);
        String safeReturnUrl = config.get(LoginConfigComponent.CONFIG_RETURN_URL);
        if (ControllerUtils.isUnSafeUrl(returnUrl, site, safeReturnUrl, request)) {
            returnUrl = site.isUseStatic() ? site.getSitePath() : site.getDynamicPath();
        }
        if (null != user && ControllerUtils.verifyCustom("tradePaymentStatus",
                !service.pendingRefund(site.getId(), entity.getPaymentId()), model)) {
            return CommonConstants.TEMPLATE_ERROR;
        }
        if (null == entity.getId()) {
            entity.setSiteId(site.getId());
            entity.setRefundAmount(null);
            entity.setReply(null);
            entity.setRefundUserId(null);
            entity.setUserId(user.getId());
            entity.setStatus(TradeRefundService.STATUS_PENDING);
            entity.setCreateDate(null);
            refundService.save(entity);
        } else {
            refundService.updateAmound(entity.getId(), user.getId(), entity.getAmount(), entity.getReason());
        }
        return UrlBasedViewResolver.REDIRECT_URL_PREFIX + returnUrl;
    }

    /**
     * @param site
     * @param user
     * @param refundId
     * @param returnUrl
     * @param request
     * @param session
     * @param model
     * @return
     */
    @RequestMapping(value = "cancelRefund")
    @Csrf
    public String cancel(@RequestAttribute SysSite site, @SessionAttribute SysUser user, long refundId, String returnUrl,
            HttpServletRequest request, HttpSession session, ModelMap model) {
        Map<String, String> config = configComponent.getConfigData(site.getId(), Config.CONFIG_CODE_SITE);
        String safeReturnUrl = config.get(LoginConfigComponent.CONFIG_RETURN_URL);
        if (ControllerUtils.isUnSafeUrl(returnUrl, site, safeReturnUrl, request)) {
            returnUrl = site.isUseStatic() ? site.getSitePath() : site.getDynamicPath();
        }
        refundService.updateStatus(site.getId(), refundId, null, TradeRefundService.STATUS_CANCELLED);
        return UrlBasedViewResolver.REDIRECT_URL_PREFIX + returnUrl;
    }

    @Autowired
    private PaymentProcessorComponent paymentProcessorComponent;
    @Autowired
    private PaymentGatewayComponent gatewayComponent;
    @Autowired
    private WechatGatewayComponent wechatGatewayComponent;
    @Autowired
    private AlipayGatewayComponent alipayGatewayComponent;
    @Autowired
    protected ConfigComponent configComponent;
    @Autowired
    private TradePaymentService service;
    @Autowired
    private TradeRefundService refundService;
    @Autowired
    private TradePaymentHistoryService historyService;

}

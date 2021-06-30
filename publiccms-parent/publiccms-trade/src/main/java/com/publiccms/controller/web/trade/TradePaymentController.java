package com.publiccms.controller.web.trade;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.servlet.view.UrlBasedViewResolver;

import com.alipay.api.AlipayApiException;
import com.alipay.api.internal.util.AlipaySignature;
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
import com.publiccms.logic.component.paymentgateway.WechatGatewayComponent;
import com.publiccms.logic.component.trade.PaymentGatewayComponent;
import com.publiccms.logic.component.trade.PaymentProcessorComponent;
import com.publiccms.logic.service.trade.TradePaymentHistoryService;
import com.publiccms.logic.service.trade.TradePaymentService;
import com.publiccms.logic.service.trade.TradeRefundService;
import com.wechat.pay.contrib.apache.httpclient.auth.AutoUpdateCertificatesVerifier;
import com.wechat.pay.contrib.apache.httpclient.util.AesUtil;

@Controller
@RequestMapping("tradePayment")
public class TradePaymentController {
    protected final Log log = LogFactory.getLog(getClass());

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
        log.info("alipay notify out_trade_no:" + out_trade_no + ",total_fee:" + total_fee + ",trade_no:" + trade_no);
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
     * @param signature
     * @param serial
     * @param body
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "notify/wechat")
    @ResponseBody
    public Map<String, String> notifyWechat(@RequestAttribute SysSite site,
            @RequestHeader(value = "Wechatpay-Signature") String signature,
            @RequestHeader(value = "Wechatpay-Serial") String serial, @RequestBody String body, HttpServletResponse response)
            throws Exception {
        log.info("wechat notify signature:" + signature + ",serial:" + serial + ",body:" + body);
        Map<String, String> config = configComponent.getConfigData(site.getId(), AlipayGatewayComponent.CONFIG_CODE);
        Map<String, String> resultMap = new HashMap<>();
        resultMap.put("code", "FAIL");
        resultMap.put("message", "error");
        if (CommonUtils.notEmpty(config) && CommonUtils.notEmpty(config.get(WechatGatewayComponent.CONFIG_KEY))) {
            byte[] apiV3Key = config.get(WechatGatewayComponent.CONFIG_KEY).getBytes(CommonConstants.DEFAULT_CHARSET);
            AutoUpdateCertificatesVerifier verifier = wechatGatewayComponent.getVerifier(site.getId(), config, apiV3Key);
            try {
                if (verifier.verify(serial, body.getBytes(CommonConstants.DEFAULT_CHARSET), signature)) {
                    Map<String, Object> result = CommonConstants.objectMapper.readValue(body, CommonConstants.objectMapper
                            .getTypeFactory().constructMapLikeType(HashMap.class, String.class, String.class));
                    @SuppressWarnings("unchecked")
                    Map<String, String> resource = (Map<String, String>) result.get("resource");
                    if (null != resource) {
                        AesUtil decryptor = new AesUtil(apiV3Key);
                        String decodeResult = decryptor.decryptToString(
                                resource.get("associated_data").replaceAll("\"", "").getBytes(CommonConstants.DEFAULT_CHARSET),
                                resource.get("nonce").replaceAll("\"", "").getBytes(CommonConstants.DEFAULT_CHARSET),
                                resource.get("ciphertext"));
                        Map<String, Object> data = CommonConstants.objectMapper.readValue(decodeResult,
                                CommonConstants.objectMapper.getTypeFactory().constructMapLikeType(HashMap.class, String.class,
                                        String.class));
                        long paymentId = Long.parseLong((String) data.get("out_trade_no"));
                        TradePaymentHistory history = new TradePaymentHistory(site.getId(), paymentId, CommonUtils.getDate(),
                                TradePaymentHistoryService.OPERATE_NOTIFY, decodeResult);
                        historyService.save(history);
                        @SuppressWarnings("unchecked")
                        Map<String, Object> amount = (Map<String, Object>) data.get("trade_state");
                        if ("SUCCESS".equalsIgnoreCase((String) data.get("trade_state")) && null != amount) {
                            TradePayment payment = service.getEntity(paymentId);
                            if (null != payment && payment.getStatus() == TradePaymentService.STATUS_PENDING_PAY
                                    && (payment.getAmount().multiply(new BigDecimal(100)))
                                            .intValue() == (int) amount.get("payer_total")) {
                                payment = service.getEntity(paymentId);
                                if (service.paid(site.getId(), payment.getId(), (String) result.get("transaction_id"))) {
                                    if (wechatGatewayComponent.confirmPay(site, payment, response)) {
                                        resultMap.put("code", "SUCCESS");
                                        resultMap.put("message", "OK");
                                    } else {
                                        resultMap.put("message", "payment confirm error");
                                    }
                                } else {
                                    resultMap.put("message", "payment status update error");
                                }
                            } else {
                                resultMap.put("message", "payment status error");
                            }
                        } else {
                            resultMap.put("message", "error trade_state");
                        }
                    } else {
                        resultMap.put("message", "response error empty resource");
                    }
                } else {
                    resultMap.put("message", "response verify error");
                }
            } catch (Exception e) {
                resultMap.put("message", e.getMessage());
            }
        } else {
            resultMap.put("message", "empty config");
        }
        return resultMap;
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
            return UrlBasedViewResolver.REDIRECT_URL_PREFIX + returnUrl;
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

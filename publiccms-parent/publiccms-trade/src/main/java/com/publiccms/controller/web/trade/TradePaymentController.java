package com.publiccms.controller.web.trade;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.UrlBasedViewResolver;

import com.alipay.easysdk.kernel.util.Signer;
import com.publiccms.common.annotation.Csrf;
import com.publiccms.common.api.PaymentGateway;
import com.publiccms.common.api.TradePaymentProcessor;
import com.publiccms.common.constants.Constants;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.common.tools.ControllerUtils;
import com.publiccms.common.tools.JsonUtils;
import com.publiccms.entities.sys.SysSite;
import com.publiccms.entities.sys.SysUser;
import com.publiccms.entities.trade.TradePayment;
import com.publiccms.entities.trade.TradePaymentHistory;
import com.publiccms.entities.trade.TradeRefund;
import com.publiccms.logic.component.config.ConfigDataComponent;
import com.publiccms.logic.component.config.SafeConfigComponent;
import com.publiccms.logic.component.paymentgateway.AlipayGatewayComponent;
import com.publiccms.logic.component.paymentgateway.WechatGatewayComponent;
import com.publiccms.logic.component.trade.PaymentGatewayComponent;
import com.publiccms.logic.component.trade.PaymentProcessorComponent;
import com.publiccms.logic.service.trade.TradePaymentHistoryService;
import com.publiccms.logic.service.trade.TradePaymentService;
import com.publiccms.logic.service.trade.TradeRefundService;
import com.wechat.pay.contrib.apache.httpclient.auth.Verifier;
import com.wechat.pay.contrib.apache.httpclient.util.AesUtil;

@Controller
@RequestMapping("tradePayment")
public class TradePaymentController {
    protected final Log log = LogFactory.getLog(getClass());

    @Resource
    private PaymentProcessorComponent tradePaymentProcessorComponent;
    @Resource
    private PaymentProcessorComponent paymentProcessorComponent;
    @Resource
    private PaymentGatewayComponent gatewayComponent;
    @Resource
    private WechatGatewayComponent wechatGatewayComponent;
    @Resource
    private AlipayGatewayComponent alipayGatewayComponent;
    @Resource
    protected ConfigDataComponent configDataComponent;
    @Resource
    protected SafeConfigComponent safeConfigComponent;
    @Resource
    private TradeRefundService refundService;
    @Resource
    private TradePaymentHistoryService historyService;

    /**
     * @param site
     * @param paymentId
     * @param paymentType
     * @param returnUrl
     * @param request
     * @param response
     * @param model
     * @return view name
     */
    @RequestMapping(value = "pay")
    public String pay(@RequestAttribute SysSite site, Long paymentId, String paymentType, String returnUrl,
            HttpServletRequest request, HttpServletResponse response, RedirectAttributes model) {
        returnUrl = safeConfigComponent.getSafeUrl(returnUrl, site, request.getContextPath());
        TradePayment entity = service.getEntity(paymentId);
        if (null != entity) {
            PaymentGateway paymentGateway = gatewayComponent.get(entity.getAccountType());
            if (null == paymentGateway || ControllerUtils.errorNotEquals("siteId", site.getId(), entity.getSiteId(), model)) {
                log.info("pay parameter error");
            } else if (!paymentGateway.pay(site, entity, paymentType, returnUrl, response)) {
                log.info("pay error");
            }
        }
        return CommonUtils.joinString(UrlBasedViewResolver.REDIRECT_URL_PREFIX, returnUrl);
    }

    /**
     * @param site
     * @param paymentId
     * @param returnUrl
     * @param request
     * @param model
     * @return view name
     */
    @RequestMapping(value = "cancel")
    @Csrf
    public String cancel(@RequestAttribute SysSite site, Long paymentId, String returnUrl, HttpServletRequest request,
            RedirectAttributes model) {
        returnUrl = safeConfigComponent.getSafeUrl(returnUrl, site, request.getContextPath());
        TradePayment entity = service.getEntity(paymentId);
        if (null == entity || ControllerUtils.errorNotEquals("siteId", site.getId(), entity.getSiteId(), model)) {
        } else {
            TradePaymentProcessor paymentProcessor = paymentProcessorComponent.get(entity.getTradeType());
            if (null != paymentProcessor && service.cancel(site.getId(), paymentId)) {
                paymentProcessor.cancel(site.getId(), entity);
            }
        }
        return CommonUtils.joinString(UrlBasedViewResolver.REDIRECT_URL_PREFIX, returnUrl);
    }

    /**
     * @param site
     * @param out_trade_no
     * @param total_amount
     * @param trade_no
     * @param request
     * @return
     */
    @RequestMapping(value = "notify/alipay")
    @ResponseBody
    public String notifyAlipay(@RequestAttribute SysSite site, long out_trade_no, String total_amount, String trade_no,
            HttpServletRequest request) {
        log.info(CommonUtils.joinString("alipay notify out_trade_no:", out_trade_no, ",total_amount:", total_amount, ",trade_no:",
                trade_no));
        Map<String, String> config = configDataComponent.getConfigData(site.getId(), AlipayGatewayComponent.CONFIG_CODE);
        if (CommonUtils.notEmpty(config)) {
            Map<String, String> params = request.getParameterMap().entrySet().stream()
                    .collect(Collectors.toMap(Entry::getKey, e -> StringUtils.join(e.getValue(), ",")));
            if (Signer.verifyParams(params, config.get(AlipayGatewayComponent.CONFIG_ALIPAY_PUBLIC_KEY))) {
                try {
                    TradePaymentHistory history = new TradePaymentHistory(site.getId(), out_trade_no, CommonUtils.getDate(),
                            TradePaymentHistoryService.OPERATE_NOTIFY, JsonUtils.getString(params));
                    historyService.save(history);
                    TradePayment payment = service.getEntity(out_trade_no);
                    if (null != payment && payment.getStatus() == TradePaymentService.STATUS_PENDING_PAY
                            && payment.getAmount().toString().equals(total_amount)
                            && service.paid(site.getId(), payment.getId(), trade_no)) {
                        payment = service.getEntity(payment.getId());
                        alipayGatewayComponent.confirmPay(site.getId(), payment);

                    }
                    return "success";
                } catch (NumberFormatException e) {
                    log.info(e.getMessage());
                }
            } else {
                log.info("response verify error");
            }
        }
        return "fail";

    }

    /**
     * @param site
     * @param signature
     * @param timestamp
     * @param nonce
     * @param serial
     * @param body
     * @return result
     */
    @RequestMapping(value = "notify/wechat")
    @ResponseBody
    public Map<String, String> notifyWechat(@RequestAttribute SysSite site,
            @RequestHeader(value = "Wechatpay-Signature") String signature,
            @RequestHeader(value = "Wechatpay-Timestamp") String timestamp,
            @RequestHeader(value = "Wechatpay-Nonce") String nonce, @RequestHeader(value = "Wechatpay-Serial") String serial,
            @RequestBody String body) {
        log.info(CommonUtils.joinString("wechat notify signature:", signature, ",serial:", serial, ",timestamp:", timestamp,
                ",nonce:", nonce, ",body:", body));
        Map<String, String> config = configDataComponent.getConfigData(site.getId(), WechatGatewayComponent.CONFIG_CODE);
        Map<String, String> resultMap = new HashMap<>();
        resultMap.put("code", "FAIL");
        resultMap.put("message", "error");
        if (CommonUtils.notEmpty(config) && CommonUtils.notEmpty(config.get(WechatGatewayComponent.CONFIG_KEY))) {
            byte[] apiV3Key = config.get(WechatGatewayComponent.CONFIG_KEY).getBytes(StandardCharsets.UTF_8);
            Verifier verifier = wechatGatewayComponent.getVerifier(config, apiV3Key);
            try {
                String joinString = CommonUtils.joinString(timestamp, "\n", nonce, "\n", body, "\n");
                if (verifier.verify(serial, joinString.getBytes(StandardCharsets.UTF_8), signature)) {
                    Map<String, Object> result = Constants.objectMapper.readValue(body, Constants.objectMapper.getTypeFactory()
                            .constructMapType(HashMap.class, String.class, Object.class));
                    @SuppressWarnings("unchecked")
                    Map<String, String> resource = (Map<String, String>) result.get("resource");
                    if (null != resource) {
                        AesUtil decryptor = new AesUtil(apiV3Key);
                        String decodeResult = decryptor.decryptToString(
                                resource.get("associated_data").replace("\"", "").getBytes(StandardCharsets.UTF_8),
                                resource.get("nonce").replace("\"", "").getBytes(StandardCharsets.UTF_8),
                                resource.get("ciphertext"));
                        Map<String, Object> data = Constants.objectMapper.readValue(decodeResult, Constants.objectMapper
                                .getTypeFactory().constructMapType(HashMap.class, String.class, Object.class));
                        long paymentId = Long.parseLong((String) data.get("out_trade_no"));
                        TradePaymentHistory history = new TradePaymentHistory(site.getId(), paymentId, CommonUtils.getDate(),
                                TradePaymentHistoryService.OPERATE_NOTIFY, decodeResult);
                        historyService.save(history);
                        if ("REFUND.SUCCESS".equalsIgnoreCase((String) result.get("event_type"))) {
                            @SuppressWarnings("unchecked")
                            Map<String, Integer> amount = (Map<String, Integer>) data.get("amount");
                            if ("SUCCESS".equalsIgnoreCase((String) data.get("refund_status")) && null != amount) {
                                TradePayment payment = service.getEntity(paymentId);
                                TradeRefund refund = refundService.getEntity(Long.parseLong((String) data.get("out_refund_no")));
                                if (null != payment && null != refund
                                        && (refund.getStatus() == TradeRefundService.STATUS_PENDING
                                                || refund.getStatus() == TradeRefundService.STATUS_FAIL)
                                        && (refund.getRefundAmount().multiply(new BigDecimal(100))).intValue() == amount
                                                .get("refund")) {
                                    TradePaymentProcessor tradePaymentProcessor = tradePaymentProcessorComponent
                                            .get(payment.getTradeType());
                                    if (null != tradePaymentProcessor && tradePaymentProcessor.refunded(site.getId(), payment)) {
                                        service.refunded(site.getId(), payment.getId());
                                        refundService.updateStatus(site.getId(), refund.getId(), refund.getRefundUserId(), null,
                                                TradeRefundService.STATUS_REFUNDED);
                                        resultMap.put("code", "SUCCESS");
                                        resultMap.put("message", "OK");
                                        log.info("OK");
                                    } else {
                                        log.info("order status update error");
                                        resultMap.put("message", "order status update error");
                                    }
                                } else {
                                    log.info("payment status error");
                                    resultMap.put("message", "payment status error");
                                }
                            } else {
                                log.info("error trade_state");
                                resultMap.put("message", "error trade_state");
                            }
                        } else if ("TRANSACTION.SUCCESS".equalsIgnoreCase((String) result.get("event_type"))) {
                            @SuppressWarnings("unchecked")
                            Map<String, Object> amount = (Map<String, Object>) data.get("amount");
                            if ("SUCCESS".equalsIgnoreCase((String) data.get("trade_state")) && null != amount) {
                                TradePayment payment = service.getEntity(paymentId);
                                if (null != payment && payment.getStatus() == TradePaymentService.STATUS_PENDING_PAY
                                        && (payment.getAmount().multiply(new BigDecimal(100)))
                                                .intValue() == (int) amount.get("payer_total")) {
                                    payment = service.getEntity(paymentId);
                                    if (service.paid(site.getId(), payment.getId(), (String) result.get("transaction_id"))) {
                                        if (wechatGatewayComponent.confirmPay(site.getId(), payment)) {
                                            resultMap.put("code", "SUCCESS");
                                            resultMap.put("message", "OK");
                                            log.info("OK");
                                        } else {
                                            log.info("payment confirm error");
                                            resultMap.put("message", "payment confirm error");
                                        }
                                    } else {
                                        log.info("payment status update error");
                                        resultMap.put("message", "payment status update error");
                                    }
                                } else {
                                    log.info("payment status error");
                                    resultMap.put("message", "payment status error");
                                }
                            } else {
                                log.info("error trade_state");
                                resultMap.put("message", "error trade_state");
                            }
                        }
                    } else {
                        log.info("response error empty resource");
                        resultMap.put("message", "response error empty resource");
                    }
                } else {
                    log.info("response verify error");
                    resultMap.put("message", "response verify error");
                }
            } catch (Exception e) {
                log.info(e.getMessage());
                resultMap.put("message", e.getMessage());
            }
        } else {
            log.info("empty config");
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
     * @param model
     * @return
     */
    @RequestMapping(value = "refund")
    @Csrf
    public String refund(@RequestAttribute SysSite site, @SessionAttribute SysUser user, TradeRefund entity, String returnUrl,
            HttpServletRequest request, RedirectAttributes model) {
        returnUrl = safeConfigComponent.getSafeUrl(returnUrl, site, request.getContextPath());
        if (ControllerUtils.errorCustom("tradePaymentStatus", !service.pendingRefund(site.getId(), entity.getPaymentId()),
                model)) {
            return CommonUtils.joinString(UrlBasedViewResolver.REDIRECT_URL_PREFIX, returnUrl);
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
        return CommonUtils.joinString(UrlBasedViewResolver.REDIRECT_URL_PREFIX, returnUrl);
    }

    /**
     * @param site
     *            站点
     * @param user
     *            用户
     * @param refundId
     *            退款id
     * @param returnUrl
     *            重定向页面地址
     * @param request
     * @return 重定向页面地址
     */
    @RequestMapping(value = "cancelRefund")
    @Csrf
    public String cancel(@RequestAttribute SysSite site, @SessionAttribute SysUser user, long refundId, String returnUrl,
            HttpServletRequest request) {
        returnUrl = safeConfigComponent.getSafeUrl(returnUrl, site, request.getContextPath());
        refundService.updateStatus(site.getId(), refundId, null, user.getId(), TradeRefundService.STATUS_CANCELLED);
        return CommonUtils.joinString(UrlBasedViewResolver.REDIRECT_URL_PREFIX, returnUrl);
    }

    @Resource
    private TradePaymentService service;

}

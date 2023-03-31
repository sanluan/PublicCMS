package com.publiccms.logic.component.paymentgateway;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.security.GeneralSecurityException;
import java.security.PrivateKey;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Component;

import com.publiccms.common.api.Config;
import com.publiccms.common.api.TradePaymentProcessor;
import com.publiccms.common.base.AbstractFreemarkerView;
import com.publiccms.common.base.AbstractPaymentGateway;
import com.publiccms.common.constants.Constants;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.common.tools.JsonUtils;
import com.publiccms.entities.sys.SysExtendField;
import com.publiccms.entities.sys.SysSite;
import com.publiccms.entities.trade.TradePayment;
import com.publiccms.entities.trade.TradePaymentHistory;
import com.publiccms.entities.trade.TradeRefund;
import com.publiccms.logic.component.config.ConfigDataComponent;
import com.publiccms.logic.component.site.SiteComponent;
import com.publiccms.logic.component.template.TemplateComponent;
import com.publiccms.logic.component.trade.PaymentProcessorComponent;
import com.publiccms.logic.service.trade.TradePaymentHistoryService;
import com.publiccms.logic.service.trade.TradePaymentService;
import com.wechat.pay.contrib.apache.httpclient.WechatPayHttpClientBuilder;
import com.wechat.pay.contrib.apache.httpclient.auth.PrivateKeySigner;
import com.wechat.pay.contrib.apache.httpclient.auth.Verifier;
import com.wechat.pay.contrib.apache.httpclient.auth.WechatPay2Credentials;
import com.wechat.pay.contrib.apache.httpclient.auth.WechatPay2Validator;
import com.wechat.pay.contrib.apache.httpclient.cert.CertificatesManager;
import com.wechat.pay.contrib.apache.httpclient.exception.HttpCodeException;
import com.wechat.pay.contrib.apache.httpclient.exception.NotFoundException;
import com.wechat.pay.contrib.apache.httpclient.util.PemUtil;

import freemarker.template.Template;

@Component
public class WechatGatewayComponent extends AbstractPaymentGateway implements Config {
    /**
     * 
     */
    public static final String CONFIG_CODE = "wechat";
    /**
     * 
     */
    public static final String CONFIG_CODE_DESCRIPTION = CommonUtils.joinString(CONFIGPREFIX, CONFIG_CODE);
    /**
     * 
     */
    public static final String CONFIG_APPID = "appId";
    /**
     * 
     */
    public static final String CONFIG_MCHID = "mchId";
    /**
     * 
     */
    public static final String CONFIG_SERIALNO = "serialNo";
    /**
     * 
     */
    public static final String CONFIG_KEY = "key";
    /**
     * 
     */
    public static final String CONFIG_PRIVATEKEY = "privateKey";
    /**
     * 
     */
    public static final String CONFIG_NOTIFYURL = "notifyUrl";
    /**
     * 
     */
    public static final String CONFIG_APITYPE = "apiType";
    /**
     * 
     */
    public static final String CONFIG_RESULTPAGE = "resultPage";

    public static final String[] API_TYPES = { "h5", "native", "app" };
    @Resource
    private TemplateComponent templateComponent;
    @Resource
    private TradePaymentService service;
    @Resource
    private TradePaymentHistoryService historyService;
    @Resource
    private PaymentProcessorComponent tradePaymentProcessorComponent;
    @Resource
    private ConfigDataComponent configDataComponent;

    private CertificatesManager certificatesManager = CertificatesManager.getInstance();
    protected final Log log = LogFactory.getLog(getClass());

    /**
     * @param config
     * @param apiV3Key
     * @return verifier
     */
    public Verifier getVerifier(Map<String, String> config, byte[] apiV3Key) {
        return getVerifier(config, apiV3Key, null);
    }

    /**
     * @param config
     * @param apiV3Key
     * @param merchantPrivateKey
     * @return verifier
     */
    public Verifier getVerifier(Map<String, String> config, byte[] apiV3Key, PrivateKey merchantPrivateKey) {
        Verifier verifier = null;
        try {
            verifier = certificatesManager.getVerifier(config.get(CONFIG_MCHID));
        } catch (NotFoundException e) {
            if (null == merchantPrivateKey) {
                byte[] privateKey = config.get(CONFIG_PRIVATEKEY).getBytes(Constants.DEFAULT_CHARSET);
                merchantPrivateKey = PemUtil.loadPrivateKey(new ByteArrayInputStream(privateKey));
            }
            try {
                certificatesManager.putMerchant(config.get(CONFIG_MCHID), new WechatPay2Credentials(config.get(CONFIG_MCHID),
                        new PrivateKeySigner(config.get(CONFIG_SERIALNO), merchantPrivateKey)), apiV3Key);
                verifier = certificatesManager.getVerifier(config.get(CONFIG_MCHID));
            } catch (HttpCodeException | IOException | GeneralSecurityException | NotFoundException e1) {
                e1.printStackTrace();
            }
        }
        return verifier;
    }

    @Override
    public String getCode(short siteId, boolean showAll) {
        return CONFIG_CODE;
    }

    /**
     * @param locale
     * @return description
     */
    @Override
    public String getCodeDescription(Locale locale) {
        return getMessage(locale, CONFIG_CODE_DESCRIPTION);
    }

    @Override
    public String getAccountType() {
        return CONFIG_CODE;
    }

    @Override
    public boolean pay(SysSite site, TradePayment payment, String paymentType, String callbackUrl, HttpServletResponse response) {
        if (null != payment) {
            Map<String, String> config = configDataComponent.getConfigData(site.getId(), CONFIG_CODE);
            if (CommonUtils.notEmpty(config) && CommonUtils.notEmpty(config.get(CONFIG_KEY))) {
                try {
                    byte[] apiV3Key = config.get(CONFIG_KEY).getBytes(Constants.DEFAULT_CHARSET);
                    byte[] privateKey = config.get(CONFIG_PRIVATEKEY).getBytes(Constants.DEFAULT_CHARSET);
                    PrivateKey merchantPrivateKey = PemUtil.loadPrivateKey(new ByteArrayInputStream(privateKey));

                    Verifier verifier = getVerifier(config, apiV3Key, merchantPrivateKey);

                    WechatPayHttpClientBuilder builder = WechatPayHttpClientBuilder.create()
                            .withMerchant(config.get(CONFIG_MCHID), config.get(CONFIG_SERIALNO), merchantPrivateKey)
                            .withValidator(new WechatPay2Validator(verifier));
                    try (CloseableHttpClient httpClient = builder.build()) {
                        String apiType;
                        if (CommonUtils.notEmpty(paymentType) && ArrayUtils.contains(API_TYPES, paymentType)) {
                            apiType = paymentType;
                        } else {
                            apiType = config.get(CONFIG_APITYPE);
                        }
                        HttpPost httpPost = new HttpPost(
                                CommonUtils.joinString("https://api.mch.weixin.qq.com/v3/pay/transactions/", apiType));
                        httpPost.addHeader("Accept", "application/json");
                        httpPost.addHeader("Content-type", "application/json; charset=utf-8");
                        Map<String, Object> requestMap = new HashMap<>();
                        requestMap.put("mchid", config.get(CONFIG_MCHID));
                        requestMap.put("appid", config.get(CONFIG_APPID));
                        requestMap.put("description", null == payment.getDescription() ? "order" : payment.getDescription());
                        requestMap.put("notify_url", config.get(CONFIG_NOTIFYURL));
                        requestMap.put("out_trade_no", getOutTradeNo(payment.getId()));
                        Map<String, Object> amountMap = new HashMap<>();
                        amountMap.put("total", (payment.getAmount().multiply(new BigDecimal(100))).intValue());
                        requestMap.put("amount", amountMap);
                        Map<String, Object> sceneMap = new HashMap<>();
                        sceneMap.put("payer_client_ip", payment.getIp());
                        requestMap.put("scene_info", sceneMap);
                        String requestBody = JsonUtils.getString(requestMap);
                        httpPost.setEntity(new StringEntity(requestBody, Constants.DEFAULT_CHARSET));
                        log.info(CommonUtils.joinString("pay request: ", requestBody));
                        CloseableHttpResponse res = httpClient.execute(httpPost);
                        HttpEntity entity = res.getEntity();
                        log.info(CommonUtils.joinString("pay response status: ", res.getStatusLine().getStatusCode()));
                        if (null != entity) {
                            String bodyAsString = EntityUtils.toString(entity, Constants.DEFAULT_CHARSET);
                            log.info(CommonUtils.joinString("pay response: ", bodyAsString));
                            if (200 == res.getStatusLine().getStatusCode()) {
                                Map<String, String> result = Constants.objectMapper.readValue(bodyAsString, Constants.objectMapper
                                        .getTypeFactory().constructMapLikeType(HashMap.class, String.class, String.class));
                                if ("h5".equalsIgnoreCase(config.get(CONFIG_APITYPE))) {
                                    response.sendRedirect(result.get("h5_url"));
                                } else {
                                    Template template = templateComponent.getWebConfiguration().getTemplate(
                                            SiteComponent.getFullTemplatePath(site.getId(), config.get(CONFIG_RESULTPAGE)));
                                    Map<String, Object> model = new HashMap<>();
                                    AbstractFreemarkerView.exposeSite(model, site);
                                    model.putAll(result);
                                    model.put("payment", payment);
                                    template.process(model, response.getWriter());
                                }
                                return true;
                            }
                        }
                    }
                } catch (Exception e) {
                    TradePaymentHistory history = new TradePaymentHistory(site.getId(), payment.getId(), CommonUtils.getDate(),
                            TradePaymentHistoryService.OPERATE_PAYERROR, e.getMessage());
                    historyService.save(history);
                }
            }
        }
        return false;
    }

    private String getOutTradeNo(long paymentId) {
        return CommonUtils.joinString("00000", paymentId);
    }

    @Override
    public boolean refund(short siteId, TradePayment payment, TradeRefund refund) {
        Map<String, String> config = configDataComponent.getConfigData(siteId, CONFIG_CODE);
        if (null != payment && CommonUtils.notEmpty(config) && CommonUtils.notEmpty(config.get(CONFIG_KEY))
                && service.refunded(siteId, payment.getId())) {
            try {
                byte[] apiV3Key = config.get(CONFIG_KEY).getBytes(Constants.DEFAULT_CHARSET);
                byte[] privateKey = config.get(CONFIG_PRIVATEKEY).getBytes(Constants.DEFAULT_CHARSET);
                PrivateKey merchantPrivateKey = PemUtil.loadPrivateKey(new ByteArrayInputStream(privateKey));

                Verifier verifier = getVerifier(config, apiV3Key, merchantPrivateKey);

                WechatPayHttpClientBuilder builder = WechatPayHttpClientBuilder.create()
                        .withMerchant(config.get(CONFIG_MCHID), config.get(CONFIG_SERIALNO), merchantPrivateKey)
                        .withValidator(new WechatPay2Validator(verifier));
                try (CloseableHttpClient httpClient = builder.build()) {
                    HttpPost httpPost = new HttpPost("https://api.mch.weixin.qq.com/v3/refund/domestic/refunds");
                    httpPost.addHeader("Accept", "application/json");
                    httpPost.addHeader("Content-type", "application/json; charset=utf-8");
                    Map<String, Object> requestMap = new HashMap<>();
                    requestMap.put("out_trade_no", getOutTradeNo(payment.getId()));
                    requestMap.put("out_refund_no", String.valueOf(refund.getId()));
                    requestMap.put("reason", null == refund.getReply() ? refund.getReason() : refund.getReply());
                    requestMap.put("notify_url", config.get(CONFIG_NOTIFYURL));
                    Map<String, Object> amountMap = new HashMap<>();
                    amountMap.put("refund", (refund.getRefundAmount().multiply(new BigDecimal(100))).intValue());
                    amountMap.put("total", (payment.getAmount().multiply(new BigDecimal(100))).intValue());
                    amountMap.put("currency", "CNY");
                    requestMap.put("amount", amountMap);
                    String requestBody = JsonUtils.getString(requestMap);
                    httpPost.setEntity(new StringEntity(requestBody, Constants.DEFAULT_CHARSET));
                    log.info(CommonUtils.joinString("refund request: ", requestBody));
                    CloseableHttpResponse res = httpClient.execute(httpPost);
                    HttpEntity entity = res.getEntity();
                    log.info(CommonUtils.joinString("refund response status: ", res.getStatusLine().getStatusCode()));
                    if (null != entity) {
                        String bodyAsString = EntityUtils.toString(entity, Constants.DEFAULT_CHARSET);
                        log.info(CommonUtils.joinString("refund response: ", bodyAsString));
                        TradePaymentHistory history = new TradePaymentHistory(siteId, payment.getId(), CommonUtils.getDate(),
                                TradePaymentHistoryService.OPERATE_REFUND_RESPONSE, bodyAsString);
                        historyService.save(history);
                        if (200 == res.getStatusLine().getStatusCode()) {
                            Map<String, Object> result = Constants.objectMapper.readValue(bodyAsString, Constants.objectMapper
                                    .getTypeFactory().constructMapLikeType(HashMap.class, String.class, Object.class));
                            if ("SUCCESS".equalsIgnoreCase((String) result.get("status"))) {
                                TradePaymentProcessor tradePaymentProcessor = tradePaymentProcessorComponent
                                        .get(payment.getTradeType());
                                if (null != tradePaymentProcessor && tradePaymentProcessor.refunded(siteId, payment)) {
                                    service.refunded(siteId, payment.getId());
                                }
                                return true;
                            } else {
                                TradePaymentHistory history1 = new TradePaymentHistory(siteId, payment.getId(),
                                        CommonUtils.getDate(), TradePaymentHistoryService.OPERATE_REFUNDERROR,
                                        CommonUtils.joinString("response result status: ", result.get("status")));
                                historyService.save(history1);
                                service.pendingRefund(siteId, payment.getId());
                            }
                        }
                    } else {
                        TradePaymentHistory history = new TradePaymentHistory(siteId, payment.getId(), CommonUtils.getDate(),
                                TradePaymentHistoryService.OPERATE_REFUNDERROR,
                                CommonUtils.joinString("response status error: ", res.getStatusLine().getStatusCode()));
                        historyService.save(history);
                        service.pendingRefund(siteId, payment.getId());
                    }
                }
            } catch (Exception e) {
                TradePaymentHistory history = new TradePaymentHistory(siteId, payment.getId(), CommonUtils.getDate(),
                        TradePaymentHistoryService.OPERATE_REFUNDERROR, e.getMessage());
                historyService.save(history);
                e.printStackTrace();
            }
        }
        return false;
    }

    @Override
    public List<SysExtendField> getExtendFieldList(SysSite site, Locale locale) {
        List<SysExtendField> extendFieldList = new ArrayList<>();
        extendFieldList.add(new SysExtendField(CONFIG_APPID, INPUTTYPE_TEXT,
                getMessage(locale, CommonUtils.joinString(CONFIG_CODE_DESCRIPTION, Constants.DOT, CONFIG_APPID)),
                getMessage(locale, CommonUtils.joinString(CONFIG_CODE_DESCRIPTION, Constants.DOT, CONFIG_APPID,
                        CONFIG_CODE_DESCRIPTION_SUFFIX))));
        extendFieldList.add(new SysExtendField(CONFIG_MCHID, INPUTTYPE_TEXT,
                getMessage(locale, CommonUtils.joinString(CONFIG_CODE_DESCRIPTION, Constants.DOT, CONFIG_MCHID)),
                getMessage(locale, CommonUtils.joinString(CONFIG_CODE_DESCRIPTION, Constants.DOT, CONFIG_MCHID,
                        CONFIG_CODE_DESCRIPTION_SUFFIX))));
        extendFieldList.add(new SysExtendField(CONFIG_KEY, INPUTTYPE_TEXT,
                getMessage(locale, CommonUtils.joinString(CONFIG_CODE_DESCRIPTION, Constants.DOT, CONFIG_KEY)),
                getMessage(locale, CommonUtils.joinString(CONFIG_CODE_DESCRIPTION, Constants.DOT, CONFIG_KEY,
                        CONFIG_CODE_DESCRIPTION_SUFFIX))));
        extendFieldList.add(new SysExtendField(CONFIG_SERIALNO, INPUTTYPE_TEXT,
                getMessage(locale, CommonUtils.joinString(CONFIG_CODE_DESCRIPTION, Constants.DOT, CONFIG_SERIALNO)),
                getMessage(locale, CommonUtils.joinString(CONFIG_CODE_DESCRIPTION, Constants.DOT, CONFIG_SERIALNO,
                        CONFIG_CODE_DESCRIPTION_SUFFIX))));
        extendFieldList.add(new SysExtendField(CONFIG_PRIVATEKEY, INPUTTYPE_TEXTAREA,
                getMessage(locale, CommonUtils.joinString(CONFIG_CODE_DESCRIPTION, Constants.DOT, CONFIG_PRIVATEKEY)),
                getMessage(locale, CommonUtils.joinString(CONFIG_CODE_DESCRIPTION, Constants.DOT, CONFIG_PRIVATEKEY,
                        CONFIG_CODE_DESCRIPTION_SUFFIX))));
        extendFieldList.add(new SysExtendField(CONFIG_NOTIFYURL, INPUTTYPE_TEXT,
                getMessage(locale, CommonUtils.joinString(CONFIG_CODE_DESCRIPTION, Constants.DOT, CONFIG_NOTIFYURL)),
                getMessage(locale, CommonUtils.joinString(CONFIG_CODE_DESCRIPTION, Constants.DOT, CONFIG_NOTIFYURL,
                        CONFIG_CODE_DESCRIPTION_SUFFIX), site.getDynamicPath())));
        extendFieldList.add(new SysExtendField(CONFIG_APITYPE, INPUTTYPE_TEXT, false,
                getMessage(locale, CommonUtils.joinString(CONFIG_CODE_DESCRIPTION, Constants.DOT, CONFIG_APITYPE)),
                getMessage(locale, CommonUtils.joinString(CONFIG_CODE_DESCRIPTION, Constants.DOT, CONFIG_APITYPE,
                        CONFIG_CODE_DESCRIPTION_SUFFIX), site.getDynamicPath()),
                "native"));
        extendFieldList.add(new SysExtendField(CONFIG_RESULTPAGE, INPUTTYPE_TEMPLATE,
                getMessage(locale, CommonUtils.joinString(CONFIG_CODE_DESCRIPTION, Constants.DOT, CONFIG_RESULTPAGE)),
                getMessage(locale, CommonUtils.joinString(CONFIG_CODE_DESCRIPTION, Constants.DOT, CONFIG_RESULTPAGE,
                        CONFIG_CODE_DESCRIPTION_SUFFIX), site.getDynamicPath())));
        return extendFieldList;
    }

    @Override
    public boolean enable(short siteId) {
        Map<String, String> config = configDataComponent.getConfigData(siteId, CONFIG_CODE);
        return CommonUtils.notEmpty(config) && CommonUtils.notEmpty(config.get(CONFIG_APPID));
    }

    @PreDestroy
    public void destroy() {
        certificatesManager.stop();
    }

}

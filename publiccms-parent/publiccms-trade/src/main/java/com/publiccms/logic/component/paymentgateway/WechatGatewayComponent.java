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
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.publiccms.common.api.Config;
import com.publiccms.common.api.TradePaymentProcessor;
import com.publiccms.common.base.AbstractFreemarkerView;
import com.publiccms.common.base.AbstractPaymentGateway;
import com.publiccms.common.constants.CommonConstants;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.common.tools.JsonUtils;
import com.publiccms.entities.sys.SysExtendField;
import com.publiccms.entities.sys.SysSite;
import com.publiccms.entities.trade.TradePayment;
import com.publiccms.entities.trade.TradePaymentHistory;
import com.publiccms.entities.trade.TradeRefund;
import com.publiccms.logic.component.BeanComponent;
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
    public static final String CONFIG_CODE_DESCRIPTION = CONFIGPREFIX + CONFIG_CODE;
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
    @Autowired
    private TemplateComponent templateComponent;
    @Autowired
    private TradePaymentService service;
    @Autowired
    private TradePaymentHistoryService historyService;
    @Autowired
    private PaymentProcessorComponent tradePaymentProcessorComponent;

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
                byte[] privateKey = config.get(CONFIG_PRIVATEKEY).getBytes(CommonConstants.DEFAULT_CHARSET);
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

    /**
     * @param site
     * @param showAll
     * @return config code or null
     */
    public String getCode(SysSite site, boolean showAll) {
        return CONFIG_CODE;
    }

    @Override
    public String getAccountType() {
        return CONFIG_CODE;
    }

    @Override
    public boolean pay(SysSite site, TradePayment payment, String callbackUrl, HttpServletResponse response) {
        if (null != payment) {
            Map<String, String> config = BeanComponent.getConfigComponent().getConfigData(site.getId(), CONFIG_CODE);
            if (CommonUtils.notEmpty(config) && CommonUtils.notEmpty(config.get(CONFIG_KEY))) {
                try {
                    byte[] apiV3Key = config.get(CONFIG_KEY).getBytes(CommonConstants.DEFAULT_CHARSET);
                    byte[] privateKey = config.get(CONFIG_PRIVATEKEY).getBytes(CommonConstants.DEFAULT_CHARSET);
                    PrivateKey merchantPrivateKey = PemUtil.loadPrivateKey(new ByteArrayInputStream(privateKey));

                    Verifier verifier = getVerifier(config, apiV3Key, merchantPrivateKey);

                    WechatPayHttpClientBuilder builder = WechatPayHttpClientBuilder.create()
                            .withMerchant(config.get(CONFIG_MCHID), config.get(CONFIG_SERIALNO), merchantPrivateKey)
                            .withValidator(new WechatPay2Validator(verifier));
                    CloseableHttpClient httpClient = builder.build();
                    HttpPost httpPost = new HttpPost(
                            "https://api.mch.weixin.qq.com/v3/pay/transactions/" + config.get(CONFIG_APITYPE));
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
                    httpPost.setEntity(new StringEntity(requestBody, CommonConstants.DEFAULT_CHARSET));
                    log.info(String.format("pay request: %s", requestBody));
                    CloseableHttpResponse res = httpClient.execute(httpPost);
                    HttpEntity entity = res.getEntity();
                    log.info(String.format("pay response status: %d", res.getStatusLine().getStatusCode()));
                    if (200 == res.getStatusLine().getStatusCode() && null != entity) {
                        String bodyAsString = EntityUtils.toString(entity, CommonConstants.DEFAULT_CHARSET);
                        log.info(String.format("pay response: %s", bodyAsString));
                        Map<String, String> result = CommonConstants.objectMapper.readValue(bodyAsString,
                                CommonConstants.objectMapper.getTypeFactory().constructMapLikeType(HashMap.class, String.class,
                                        String.class));
                        if ("h5".equalsIgnoreCase(config.get(CONFIG_APITYPE))) {
                            response.sendRedirect(result.get("h5_url"));
                        } else {
                            Template template = templateComponent.getWebConfiguration()
                                    .getTemplate(SiteComponent.getFullTemplatePath(site, config.get(CONFIG_RESULTPAGE)));
                            Map<String, Object> model = new HashMap<>();
                            AbstractFreemarkerView.exposeSite(model, site);
                            model.putAll(result);
                            model.put("payment", payment);
                            template.process(model, response.getWriter());
                        }
                        return true;
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
        return "00000" + paymentId;
    }

    @Override
    public boolean refund(short siteId, TradePayment payment, TradeRefund refund) {
        Map<String, String> config = BeanComponent.getConfigComponent().getConfigData(siteId, CONFIG_CODE);
        if (null != payment && CommonUtils.notEmpty(config) && CommonUtils.notEmpty(config.get(CONFIG_KEY))
                && service.refunded(siteId, payment.getId())) {
            try {
                byte[] apiV3Key = config.get(CONFIG_KEY).getBytes(CommonConstants.DEFAULT_CHARSET);
                byte[] privateKey = config.get(CONFIG_PRIVATEKEY).getBytes(CommonConstants.DEFAULT_CHARSET);
                PrivateKey merchantPrivateKey = PemUtil.loadPrivateKey(new ByteArrayInputStream(privateKey));

                Verifier verifier = getVerifier(config, apiV3Key, merchantPrivateKey);

                WechatPayHttpClientBuilder builder = WechatPayHttpClientBuilder.create()
                        .withMerchant(config.get(CONFIG_MCHID), config.get(CONFIG_SERIALNO), merchantPrivateKey)
                        .withValidator(new WechatPay2Validator(verifier));
                CloseableHttpClient httpClient = builder.build();
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
                httpPost.setEntity(new StringEntity(requestBody, CommonConstants.DEFAULT_CHARSET));
                log.info(String.format("refund request: %s", requestBody));
                CloseableHttpResponse res = httpClient.execute(httpPost);
                HttpEntity entity = res.getEntity();
                log.info(String.format("refund response status: %d", res.getStatusLine().getStatusCode()));
                if (200 == res.getStatusLine().getStatusCode() && null != entity) {
                    String bodyAsString = EntityUtils.toString(entity, CommonConstants.DEFAULT_CHARSET);
                    log.info(String.format("refund response: %s", bodyAsString));
                    TradePaymentHistory history = new TradePaymentHistory(siteId, payment.getId(), CommonUtils.getDate(),
                            TradePaymentHistoryService.OPERATE_REFUND_RESPONSE, bodyAsString);
                    historyService.save(history);
                    Map<String, Object> result = CommonConstants.objectMapper.readValue(bodyAsString, CommonConstants.objectMapper
                            .getTypeFactory().constructMapLikeType(HashMap.class, String.class, Object.class));
                    if ("SUCCESS".equalsIgnoreCase((String) result.get("status"))) {
                        TradePaymentProcessor tradePaymentProcessor = tradePaymentProcessorComponent.get(payment.getTradeType());
                        if (null != tradePaymentProcessor && tradePaymentProcessor.refunded(siteId, payment)) {
                            service.refunded(siteId, payment.getId());
                        }
                        return true;
                    } else {
                        TradePaymentHistory history1 = new TradePaymentHistory(siteId, payment.getId(), CommonUtils.getDate(),
                                TradePaymentHistoryService.OPERATE_REFUNDERROR,
                                String.format("response result status: %s", result.get("status")));
                        historyService.save(history1);
                        service.pendingRefund(siteId, payment.getId());
                    }
                } else {
                    TradePaymentHistory history = new TradePaymentHistory(siteId, payment.getId(), CommonUtils.getDate(),
                            TradePaymentHistoryService.OPERATE_REFUNDERROR,
                            String.format("response status error: %d", res.getStatusLine().getStatusCode()));
                    historyService.save(history);
                    service.pendingRefund(siteId, payment.getId());
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
                getMessage(locale, CONFIG_CODE_DESCRIPTION + CommonConstants.DOT + CONFIG_APPID), getMessage(locale,
                        CONFIG_CODE_DESCRIPTION + CommonConstants.DOT + CONFIG_APPID + CONFIG_CODE_DESCRIPTION_SUFFIX)));
        extendFieldList.add(new SysExtendField(CONFIG_MCHID, INPUTTYPE_TEXT,
                getMessage(locale, CONFIG_CODE_DESCRIPTION + CommonConstants.DOT + CONFIG_MCHID), getMessage(locale,
                        CONFIG_CODE_DESCRIPTION + CommonConstants.DOT + CONFIG_MCHID + CONFIG_CODE_DESCRIPTION_SUFFIX)));
        extendFieldList.add(new SysExtendField(CONFIG_KEY, INPUTTYPE_TEXT,
                getMessage(locale, CONFIG_CODE_DESCRIPTION + CommonConstants.DOT + CONFIG_KEY),
                getMessage(locale, CONFIG_CODE_DESCRIPTION + CommonConstants.DOT + CONFIG_KEY + CONFIG_CODE_DESCRIPTION_SUFFIX)));
        extendFieldList.add(new SysExtendField(CONFIG_SERIALNO, INPUTTYPE_TEXT,
                getMessage(locale, CONFIG_CODE_DESCRIPTION + CommonConstants.DOT + CONFIG_SERIALNO), getMessage(locale,
                        CONFIG_CODE_DESCRIPTION + CommonConstants.DOT + CONFIG_SERIALNO + CONFIG_CODE_DESCRIPTION_SUFFIX)));
        extendFieldList.add(new SysExtendField(CONFIG_PRIVATEKEY, INPUTTYPE_TEXTAREA,
                getMessage(locale, CONFIG_CODE_DESCRIPTION + CommonConstants.DOT + CONFIG_PRIVATEKEY), getMessage(locale,
                        CONFIG_CODE_DESCRIPTION + CommonConstants.DOT + CONFIG_PRIVATEKEY + CONFIG_CODE_DESCRIPTION_SUFFIX)));
        extendFieldList.add(new SysExtendField(CONFIG_NOTIFYURL, INPUTTYPE_TEXT,
                getMessage(locale, CONFIG_CODE_DESCRIPTION + CommonConstants.DOT + CONFIG_NOTIFYURL),
                getMessage(locale,
                        CONFIG_CODE_DESCRIPTION + CommonConstants.DOT + CONFIG_NOTIFYURL + CONFIG_CODE_DESCRIPTION_SUFFIX,
                        site.getDynamicPath())));
        extendFieldList.add(new SysExtendField(CONFIG_APITYPE, INPUTTYPE_TEXT, false,
                getMessage(locale, CONFIG_CODE_DESCRIPTION + CommonConstants.DOT + CONFIG_APITYPE),
                getMessage(locale,
                        CONFIG_CODE_DESCRIPTION + CommonConstants.DOT + CONFIG_APITYPE + CONFIG_CODE_DESCRIPTION_SUFFIX,
                        site.getDynamicPath()),
                "native"));
        extendFieldList.add(new SysExtendField(CONFIG_RESULTPAGE, INPUTTYPE_TEMPLATE,
                getMessage(locale, CONFIG_CODE_DESCRIPTION + CommonConstants.DOT + CONFIG_RESULTPAGE),
                getMessage(locale,
                        CONFIG_CODE_DESCRIPTION + CommonConstants.DOT + CONFIG_RESULTPAGE + CONFIG_CODE_DESCRIPTION_SUFFIX,
                        site.getDynamicPath())));
        return extendFieldList;
    }

    @Override
    public boolean enable(short siteId) {
        Map<String, String> config = BeanComponent.getConfigComponent().getConfigData(siteId, CONFIG_CODE);
        if (CommonUtils.notEmpty(config) && CommonUtils.notEmpty(config.get(CONFIG_APPID))) {
            return true;
        }
        return false;
    }

    @PreDestroy
    public void destroy() {
        certificatesManager.stop();
    }
}

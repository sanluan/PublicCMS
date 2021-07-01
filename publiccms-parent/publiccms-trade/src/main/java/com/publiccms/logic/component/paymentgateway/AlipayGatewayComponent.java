package com.publiccms.logic.component.paymentgateway;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.AlipayConstants;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.AlipayTradeRefundModel;
import com.alipay.api.domain.AlipayTradeWapPayModel;
import com.alipay.api.request.AlipayTradeRefundRequest;
import com.alipay.api.request.AlipayTradeWapPayRequest;
import com.alipay.api.response.AlipayTradeRefundResponse;
import com.publiccms.common.api.Config;
import com.publiccms.common.api.TradePaymentProcessor;
import com.publiccms.common.base.AbstractPaymentGateway;
import com.publiccms.common.constants.CommonConstants;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.entities.sys.SysExtendField;
import com.publiccms.entities.sys.SysSite;
import com.publiccms.entities.trade.TradePayment;
import com.publiccms.entities.trade.TradePaymentHistory;
import com.publiccms.entities.trade.TradeRefund;
import com.publiccms.logic.component.config.ConfigComponent;
import com.publiccms.logic.component.trade.PaymentProcessorComponent;
import com.publiccms.logic.service.trade.TradePaymentHistoryService;
import com.publiccms.logic.service.trade.TradePaymentService;

@Component
public class AlipayGatewayComponent extends AbstractPaymentGateway implements Config {
    /**
     * 
     */
    public static final String CONFIG_CODE = "alipay";
    /**
     * 
     */
    public static final String CONFIG_CODE_DESCRIPTION = CONFIGPREFIX + CONFIG_CODE;
    /**
     * 
     */
    public static final String CONFIG_URL = "url";
    /**
     * 
     */
    public static final String CONFIG_APPID = "appId";
    /**
     * 
     */
    public static final String CONFIG_PRIVATE_KEY = "privateKey";
    /**
     * 
     */
    public static final String CONFIG_TIMEOUT_EXPRESS = "timeoutExpress";
    /**
     * 
     */
    public static final String CONFIG_ALIPAY_PUBLIC_KEY = "alipayPublicKey";
    /**
     * 
     */
    public static final String CONFIG_PRODUCT_CODE = "productCode";
    /**
     * 
     */
    public static final String CONFIG_NOTIFYURL = "notifyUrl";
    /**
     * 
     */
    public static final String CONFIG_ENCRYPTKEY = "encryptKey";
    @Autowired
    private ConfigComponent configComponent;
    @Autowired
    private TradePaymentService service;
    @Autowired
    private TradePaymentHistoryService historyService;
    @Autowired
    private PaymentProcessorComponent tradePaymentProcessorComponent;

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
    public boolean pay(short siteId, TradePayment payment, String callbackUrl, HttpServletResponse response) {
        if (null != payment) {
            Map<String, String> config = configComponent.getConfigData(siteId, CONFIG_CODE);
            if (CommonUtils.notEmpty(config)) {
                AlipayClient client = new DefaultAlipayClient(config.get(CONFIG_URL), config.get(CONFIG_APPID),
                        config.get(CONFIG_PRIVATE_KEY), null, CommonConstants.DEFAULT_CHARSET_NAME,
                        config.get(CONFIG_ALIPAY_PUBLIC_KEY), AlipayConstants.SIGN_TYPE_RSA2, config.get(CONFIG_ENCRYPTKEY),
                        AlipayConstants.ENCRYPT_TYPE_AES);
                AlipayTradeWapPayRequest alipay_request = new AlipayTradeWapPayRequest();
                AlipayTradeWapPayModel model = new AlipayTradeWapPayModel();
                model.setOutTradeNo(String.valueOf(payment.getId()));
                model.setSubject(payment.getDescription());
                model.setTotalAmount(payment.getAmount().toString());
                model.setBody(payment.getDescription());
                model.setTimeoutExpress(config.get(CONFIG_TIMEOUT_EXPRESS));
                model.setProductCode(config.get(CONFIG_PRODUCT_CODE));
                alipay_request.setBizModel(model);
                alipay_request.setNotifyUrl(config.get(CONFIG_NOTIFYURL));
                alipay_request.setReturnUrl(callbackUrl);
                try {
                    String form = client.pageExecute(alipay_request).getBody();
                    response.setContentType("text/html;charset=" + CommonConstants.DEFAULT_CHARSET_NAME);
                    response.getWriter()
                            .write("<html><head><meta http-equiv='Content-Type' content='text/html;charset=UTF-8'></head><body>");
                    response.getWriter().write(form);
                    response.getWriter().write("</body></html>");
                    response.getWriter().flush();
                    response.getWriter().close();
                    return true;
                } catch (AlipayApiException | IOException e) {
                }
            }
        }
        return false;
    }

    @Override
    public boolean refund(short siteId, TradePayment payment, TradeRefund refund) {
        Map<String, String> config = configComponent.getConfigData(siteId, CONFIG_CODE);
        if (null != payment && CommonUtils.notEmpty(config) && service.refunded(siteId, payment.getId())) {
            AlipayClient client = new DefaultAlipayClient(config.get(CONFIG_URL), config.get(CONFIG_APPID),
                    config.get(CONFIG_PRIVATE_KEY), null, CommonConstants.DEFAULT_CHARSET_NAME,
                    config.get(CONFIG_ALIPAY_PUBLIC_KEY));
            AlipayTradeRefundRequest alipay_request = new AlipayTradeRefundRequest();

            AlipayTradeRefundModel model = new AlipayTradeRefundModel();
            model.setOutTradeNo(String.valueOf(refund.getPaymentId()));
            model.setTradeNo(payment.getAccountSerialNumber());
            model.setRefundAmount(refund.getRefundAmount().toString());
            model.setRefundReason(refund.getReason());
            model.setOutRequestNo(String.valueOf(refund.getPaymentId()));
            alipay_request.setBizModel(model);
            try {
                AlipayTradeRefundResponse alipay_response = client.execute(alipay_request);
                if ("10000".equalsIgnoreCase(alipay_response.getCode())) {
                    TradePaymentProcessor tradePaymentProcessor = tradePaymentProcessorComponent.get(payment.getTradeType());
                    if (null != tradePaymentProcessor && tradePaymentProcessor.refunded(siteId, payment)) {
                        service.refunded(siteId, payment.getId());
                    }
                    return true;
                } else {
                    service.pendingRefund(siteId, payment.getId());
                }
            } catch (AlipayApiException e) {
                TradePaymentHistory history = new TradePaymentHistory(siteId, payment.getId(), CommonUtils.getDate(),
                        TradePaymentHistoryService.OPERATE_PAYERROR, e.getMessage());
                historyService.save(history);
            }
        }
        return false;
    }

    @Override
    public List<SysExtendField> getExtendFieldList(SysSite site, Locale locale) {
        List<SysExtendField> extendFieldList = new ArrayList<>();
        extendFieldList.add(new SysExtendField(CONFIG_URL, INPUTTYPE_TEXT,
                getMessage(locale, CONFIG_CODE_DESCRIPTION + CommonConstants.DOT + CONFIG_URL),
                getMessage(locale, CONFIG_CODE_DESCRIPTION + CommonConstants.DOT + CONFIG_URL + CONFIG_CODE_DESCRIPTION_SUFFIX)));
        extendFieldList.add(new SysExtendField(CONFIG_APPID, INPUTTYPE_TEXT,
                getMessage(locale, CONFIG_CODE_DESCRIPTION + CommonConstants.DOT + CONFIG_APPID), getMessage(locale,
                        CONFIG_CODE_DESCRIPTION + CommonConstants.DOT + CONFIG_APPID + CONFIG_CODE_DESCRIPTION_SUFFIX)));
        extendFieldList.add(new SysExtendField(CONFIG_PRIVATE_KEY, INPUTTYPE_TEXTAREA,
                getMessage(locale, CONFIG_CODE_DESCRIPTION + CommonConstants.DOT + CONFIG_PRIVATE_KEY), getMessage(locale,
                        CONFIG_CODE_DESCRIPTION + CommonConstants.DOT + CONFIG_PRIVATE_KEY + CONFIG_CODE_DESCRIPTION_SUFFIX)));
        extendFieldList.add(new SysExtendField(CONFIG_TIMEOUT_EXPRESS, INPUTTYPE_TEXT, false,
                getMessage(locale, CONFIG_CODE_DESCRIPTION + CommonConstants.DOT + CONFIG_TIMEOUT_EXPRESS),
                getMessage(locale,
                        CONFIG_CODE_DESCRIPTION + CommonConstants.DOT + CONFIG_TIMEOUT_EXPRESS + CONFIG_CODE_DESCRIPTION_SUFFIX),
                "30M"));
        extendFieldList.add(new SysExtendField(CONFIG_ALIPAY_PUBLIC_KEY, INPUTTYPE_TEXTAREA,
                getMessage(locale, CONFIG_CODE_DESCRIPTION + CommonConstants.DOT + CONFIG_ALIPAY_PUBLIC_KEY),
                getMessage(locale, CONFIG_CODE_DESCRIPTION + CommonConstants.DOT + CONFIG_ALIPAY_PUBLIC_KEY
                        + CONFIG_CODE_DESCRIPTION_SUFFIX)));
        extendFieldList.add(new SysExtendField(CONFIG_PRODUCT_CODE, INPUTTYPE_TEXT,
                getMessage(locale, CONFIG_CODE_DESCRIPTION + CommonConstants.DOT + CONFIG_PRODUCT_CODE), getMessage(locale,
                        CONFIG_CODE_DESCRIPTION + CommonConstants.DOT + CONFIG_PRODUCT_CODE + CONFIG_CODE_DESCRIPTION_SUFFIX)));
        extendFieldList.add(new SysExtendField(CONFIG_ENCRYPTKEY, INPUTTYPE_TEXT,
                getMessage(locale, CONFIG_CODE_DESCRIPTION + CommonConstants.DOT + CONFIG_ENCRYPTKEY), getMessage(locale,
                        CONFIG_CODE_DESCRIPTION + CommonConstants.DOT + CONFIG_ENCRYPTKEY + CONFIG_CODE_DESCRIPTION_SUFFIX)));
        extendFieldList.add(new SysExtendField(CONFIG_NOTIFYURL, INPUTTYPE_TEXT,
                getMessage(locale, CONFIG_CODE_DESCRIPTION + CommonConstants.DOT + CONFIG_NOTIFYURL),
                getMessage(locale,
                        CONFIG_CODE_DESCRIPTION + CommonConstants.DOT + CONFIG_NOTIFYURL + CONFIG_CODE_DESCRIPTION_SUFFIX,
                        site.getDynamicPath())));
        return extendFieldList;
    }

    @Override
    public boolean enable(short siteId) {
        Map<String, String> config = configComponent.getConfigData(siteId, CONFIG_CODE);
        if (CommonUtils.notEmpty(config) && CommonUtils.notEmpty(config.get(CONFIG_URL))) {
            return true;
        }
        return false;
    }

}

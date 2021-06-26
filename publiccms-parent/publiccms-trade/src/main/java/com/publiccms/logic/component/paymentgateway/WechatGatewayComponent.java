package com.publiccms.logic.component.paymentgateway;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.github.wxpay.sdk.WXPay;
import com.github.wxpay.sdk.WXPayConstants;
import com.publiccms.common.api.Config;
import com.publiccms.common.api.TradePaymentProcessor;
import com.publiccms.common.base.AbstractPaymentGateway;
import com.publiccms.common.constants.CommonConstants;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.entities.sys.SysExtendField;
import com.publiccms.entities.sys.SysSite;
import com.publiccms.entities.trade.TradePayment;
import com.publiccms.entities.trade.TradeRefund;
import com.publiccms.logic.component.config.ConfigComponent;
import com.publiccms.logic.component.trade.PaymentProcessorComponent;
import com.publiccms.logic.service.trade.TradePaymentService;

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
    public static final String CONFIG_KEY = "key";
    /**
     * 
     */
    public static final String CONFIG_CERT = "cert";
    /**
     * 
     */
    public static final String CONFIG_USESANDBOX = "useSandbox";
    /**
     * 
     */
    public static final String CONFIG_NOTIFYURL = "notifyUrl";
    @Autowired
    private ConfigComponent configComponent;
    @Autowired
    private TradePaymentService service;
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
    public boolean pay(SysSite site, TradePayment payment, String callbackUrl, HttpServletResponse response) {
        if (null != payment) {
            Map<String, String> config = configComponent.getConfigData(payment.getSiteId(), CONFIG_CODE);
            if (CommonUtils.notEmpty(config)) {
                WechatConfig wechatConfig = new WechatConfig(config.get(CONFIG_APPID), config.get(CONFIG_MCHID),
                        config.get(CONFIG_KEY), config.get(CONFIG_CERT));
                boolean usesandbox = CommonUtils.notEmpty(config.get(CONFIG_USESANDBOX))
                        && "true".equals(config.get(CONFIG_USESANDBOX));
                try {
                    WXPay wxpay = new WXPay(wechatConfig, config.get(CONFIG_NOTIFYURL), usesandbox);
                    Map<String, String> reqData = new HashMap<>();
                    reqData.put("body", payment.getDescription());
                    reqData.put("attach", site.getName());
                    reqData.put("out_trade_no", String.valueOf(payment.getId()));
                    reqData.put("total_fee", String.valueOf((payment.getAmount().multiply(new BigDecimal(100))).intValue()));
                    reqData.put("spbill_create_ip", payment.getIp());
                    reqData.put("notify_url", config.get(CONFIG_NOTIFYURL));
                    reqData.put("trade_type", "MWEB");
                    reqData.put("scene_info", "{\"h5_info\": {\"type\":\"Wap\",\"wap_url\": \"" + site.getSitePath()
                            + "\",\"wap_name\": \"" + site.getName() + "\"}}   ");
                    Map<String, String> result = wxpay.unifiedOrder(reqData);
                    if (WXPayConstants.SUCCESS.equalsIgnoreCase(result.get("return_code"))
                            && WXPayConstants.SUCCESS.equalsIgnoreCase(result.get("result_code"))) {
                        response.sendRedirect(result.get("mweb_url"));
                        return true;
                    }
                } catch (Exception e) {
                }
            }
        }
        return false;
    }

    @Override
    public boolean refund(SysSite site, TradePayment payment, TradeRefund refund) {
        Map<String, String> config = configComponent.getConfigData(payment.getSiteId(), CONFIG_CODE);
        if (null != payment && CommonUtils.notEmpty(config) && service.refunded(payment.getSiteId(), payment.getId())) {
            WechatConfig wechatConfig = new WechatConfig(config.get(CONFIG_APPID), config.get(CONFIG_MCHID),
                    config.get(CONFIG_KEY), config.get(CONFIG_CERT));
            boolean usesandbox = CommonUtils.notEmpty(config.get(CONFIG_USESANDBOX))
                    && "true".equals(config.get(CONFIG_USESANDBOX));
            try {
                WXPay wxpay = new WXPay(wechatConfig, config.get(CONFIG_NOTIFYURL), usesandbox);
                Map<String, String> reqData = new HashMap<>();
                reqData.put("out_trade_no", String.valueOf(payment.getId()));
                reqData.put("out_refund_no", String.valueOf(refund.getId()));
                reqData.put("total_fee", String.valueOf((payment.getAmount().multiply(new BigDecimal(100))).intValue()));
                reqData.put("refund_fee", String.valueOf((refund.getAmount().multiply(new BigDecimal(100))).intValue()));
                Map<String, String> result = wxpay.refund(reqData);
                if (WXPayConstants.SUCCESS.equalsIgnoreCase(result.get("return_code"))
                        && WXPayConstants.SUCCESS.equalsIgnoreCase(result.get("result_code"))) {
                    TradePaymentProcessor tradePaymentProcessor = tradePaymentProcessorComponent.get(payment.getTradeType());
                    if (null != tradePaymentProcessor && tradePaymentProcessor.refunded(payment)) {
                        service.close(payment.getSiteId(), payment.getId());
                    }
                    return true;
                } else {
                    service.pendingRefund(payment.getSiteId(), payment.getId());
                }
            } catch (Exception e) {
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
        extendFieldList.add(new SysExtendField(CONFIG_KEY, INPUTTYPE_TEXTAREA,
                getMessage(locale, CONFIG_CODE_DESCRIPTION + CommonConstants.DOT + CONFIG_KEY),
                getMessage(locale, CONFIG_CODE_DESCRIPTION + CommonConstants.DOT + CONFIG_KEY + CONFIG_CODE_DESCRIPTION_SUFFIX)));
        extendFieldList.add(new SysExtendField(CONFIG_CERT, INPUTTYPE_TEMPLATE,
                getMessage(locale, CONFIG_CODE_DESCRIPTION + CommonConstants.DOT + CONFIG_CERT), getMessage(locale,
                        CONFIG_CODE_DESCRIPTION + CommonConstants.DOT + CONFIG_CERT + CONFIG_CODE_DESCRIPTION_SUFFIX)));
        extendFieldList.add(new SysExtendField(CONFIG_USESANDBOX, INPUTTYPE_BOOLEAN,
                getMessage(locale, CONFIG_CODE_DESCRIPTION + CommonConstants.DOT + CONFIG_USESANDBOX), getMessage(locale,
                        CONFIG_CODE_DESCRIPTION + CommonConstants.DOT + CONFIG_USESANDBOX + CONFIG_CODE_DESCRIPTION_SUFFIX)));
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
        if (CommonUtils.notEmpty(config) && CommonUtils.notEmpty(config.get(CONFIG_APPID))) {
            return true;
        }
        return false;
    }

}

package com.publiccms.logic.component.paymentgateway;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alipay.easysdk.factory.MultipleFactory;
import com.alipay.easysdk.kernel.AlipayConstants;
import com.alipay.easysdk.kernel.Config;
import com.publiccms.common.api.SiteCache;
import com.publiccms.common.api.TradePaymentProcessor;
import com.publiccms.common.base.AbstractPaymentGateway;
import com.publiccms.common.cache.CacheEntity;
import com.publiccms.common.cache.CacheEntityFactory;
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
public class AlipayGatewayComponent extends AbstractPaymentGateway implements com.publiccms.common.api.Config, SiteCache {
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
    public static final String CONFIG_GATEWAY = "gateway";
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

    private CacheEntity<Short, MultipleFactory> cache;

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

    /**
     * @param siteId
     * @param config
     * @return factory
     */
    public MultipleFactory getFactory(short siteId, Map<String, String> config) {
        MultipleFactory factory = cache.get(siteId);
        if (null == factory) {
            synchronized (cache) {
                factory = cache.get(siteId);
                if (null == factory) {
                    factory = new MultipleFactory();
                    Config c = new Config();
                    c.protocol = "https";
                    c.gatewayHost = config.get(CONFIG_GATEWAY);
                    c.signType = AlipayConstants.RSA2;
                    c.appId = config.get(CONFIG_APPID);
                    c.merchantPrivateKey = config.get(CONFIG_PRIVATE_KEY);
                    c.alipayPublicKey = config.get(CONFIG_ALIPAY_PUBLIC_KEY);
                    c.notifyUrl = config.get(CONFIG_NOTIFYURL);
                    c.encryptKey = config.get(CONFIG_ENCRYPTKEY);
                    factory.setOptions(c);
                    cache.put(siteId, factory);
                }
            }
        }
        return factory;
    }

    @Override
    public boolean pay(short siteId, TradePayment payment, String callbackUrl, HttpServletResponse response) {
        if (null != payment) {
            Map<String, String> config = configComponent.getConfigData(siteId, CONFIG_CODE);
            if (CommonUtils.notEmpty(config) && CommonUtils.notEmpty(config.get(CONFIG_GATEWAY))) {
                MultipleFactory factory = getFactory(siteId, config);
                try {
                    String form = factory
                            .Wap().optional("timeout_express", config.get(CONFIG_TIMEOUT_EXPRESS)).pay(payment.getDescription(),
                                    String.valueOf(payment.getId()), payment.getAmount().toString(), callbackUrl, callbackUrl)
                            .getBody();
                    response.setContentType("text/html;charset=" + CommonConstants.DEFAULT_CHARSET_NAME);
                    response.getWriter()
                            .write("<html><head><meta http-equiv='Content-Type' content='text/html;charset=UTF-8'></head><body>");
                    response.getWriter().write(form);
                    response.getWriter().write("</body></html>");
                    response.getWriter().flush();
                    response.getWriter().close();
                    return true;
                } catch (Exception e) {
                }
            }
        }
        return false;
    }

    @Override
    public boolean refund(short siteId, TradePayment payment, TradeRefund refund) {
        Map<String, String> config = configComponent.getConfigData(siteId, CONFIG_CODE);
        if (null != payment && CommonUtils.notEmpty(config) && CommonUtils.notEmpty(config.get(CONFIG_GATEWAY))
                && service.refunded(siteId, payment.getId())) {
            MultipleFactory factory = getFactory(siteId, config);
            try {
                if ("10000".equalsIgnoreCase(factory.Common()
                        .refund(String.valueOf(refund.getPaymentId()), refund.getRefundAmount().toString()).getCode())) {
                    TradePaymentProcessor tradePaymentProcessor = tradePaymentProcessorComponent.get(payment.getTradeType());
                    if (null != tradePaymentProcessor && tradePaymentProcessor.refunded(siteId, payment)) {
                        service.refunded(siteId, payment.getId());
                    }
                    return true;
                } else {
                    service.pendingRefund(siteId, payment.getId());
                }
            } catch (Exception e) {
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
        extendFieldList.add(new SysExtendField(CONFIG_GATEWAY, INPUTTYPE_TEXT,
                getMessage(locale, CONFIG_CODE_DESCRIPTION + CommonConstants.DOT + CONFIG_GATEWAY), getMessage(locale,
                        CONFIG_CODE_DESCRIPTION + CommonConstants.DOT + CONFIG_GATEWAY + CONFIG_CODE_DESCRIPTION_SUFFIX)));
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
                "30m"));
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

    /**
     * @param cacheEntityFactory
     * @throws IllegalAccessException
     * @throws InstantiationException
     * @throws ClassNotFoundException
     */
    @Autowired
    public void initCache(CacheEntityFactory cacheEntityFactory)
            throws ClassNotFoundException, InstantiationException, IllegalAccessException {
        cache = cacheEntityFactory.createCacheEntity(CONFIG_CODE, CacheEntityFactory.MEMORY_CACHE_ENTITY);
    }

    @Override
    public boolean enable(short siteId) {
        Map<String, String> config = configComponent.getConfigData(siteId, CONFIG_CODE);
        if (CommonUtils.notEmpty(config) && CommonUtils.notEmpty(config.get(CONFIG_GATEWAY))) {
            return true;
        }
        return false;
    }

    @Override
    public void clear(short siteId) {
        cache.remove(siteId);
    }

    @Override
    public void clear() {
        cache.clear();
    }
}

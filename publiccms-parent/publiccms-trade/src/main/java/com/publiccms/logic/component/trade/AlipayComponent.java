package com.publiccms.logic.component.trade;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.AlipayTradeRefundModel;
import com.alipay.api.domain.AlipayTradeWapPayModel;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.AlipayTradeRefundRequest;
import com.alipay.api.request.AlipayTradeWapPayRequest;
import com.alipay.api.response.AlipayTradeRefundResponse;
import com.publiccms.common.api.Config;
import com.publiccms.common.api.PaymentGateway;
import com.publiccms.common.constants.CommonConstants;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.entities.sys.SysExtendField;
import com.publiccms.entities.sys.SysSite;
import com.publiccms.entities.trade.TradeOrder;
import com.publiccms.entities.trade.TradeRefund;
import com.publiccms.logic.component.config.ConfigComponent;
import com.publiccms.logic.service.trade.TradeOrderService;

@Component
public class AlipayComponent implements PaymentGateway, Config {
    /**
     * 
     */
    public static final String CONFIG_CODE = "alipay";
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
    @Autowired
    private ConfigComponent configComponent;
    @Autowired
    private TradeOrderService service;

    @Override
    public String getChannel() {
        return CONFIG_CODE;
    }

    @Override
    public void pay(TradeOrder order, String callbackUrl, String notifyUrl, HttpServletResponse response) {
        Map<String, String> config = configComponent.getConfigData(order.getSiteId(), CONFIG_CODE);
        if (CommonUtils.notEmpty(config)) {
            AlipayClient client = new DefaultAlipayClient(config.get(CONFIG_URL), config.get(CONFIG_APPID),
                    config.get(CONFIG_PRIVATE_KEY), null, CommonConstants.DEFAULT_CHARSET_NAME,
                    config.get(CONFIG_ALIPAY_PUBLIC_KEY));
            AlipayTradeWapPayRequest alipay_request = new AlipayTradeWapPayRequest();
            AlipayTradeWapPayModel model = new AlipayTradeWapPayModel();
            model.setOutTradeNo(String.valueOf(order.getId()));
            model.setSubject(order.getDescription());
            model.setTotalAmount(order.getAmount().toString());
            model.setBody(order.getDescription());
            model.setTimeoutExpress(config.get(CONFIG_TIMEOUT_EXPRESS));
            model.setProductCode(config.get(CONFIG_PRODUCT_CODE));
            alipay_request.setBizModel(model);
            alipay_request.setNotifyUrl(notifyUrl);
            alipay_request.setReturnUrl(callbackUrl);
            try {
                String form = client.pageExecute(alipay_request).getBody();
                response.setContentType("text/html;charset=" + CommonConstants.DEFAULT_CHARSET_NAME);
                response.getWriter().write(form);
                response.getWriter().flush();
                response.getWriter().close();
            } catch (AlipayApiException | IOException e) {
            }
        }
    }

    @Override
    public boolean refund(TradeOrder order, TradeRefund refund) {
        Map<String, String> config = configComponent.getConfigData(order.getSiteId(), CONFIG_CODE);
        if (CommonUtils.notEmpty(config)) {
            AlipayClient client = new DefaultAlipayClient(config.get(CONFIG_URL), config.get(CONFIG_APPID),
                    config.get(CONFIG_PRIVATE_KEY), null, CommonConstants.DEFAULT_CHARSET_NAME,
                    config.get(CONFIG_ALIPAY_PUBLIC_KEY));
            AlipayTradeRefundRequest alipay_request = new AlipayTradeRefundRequest();

            AlipayTradeRefundModel model = new AlipayTradeRefundModel();
            model.setOutTradeNo(String.valueOf(refund.getOrderId()));
            model.setTradeNo(order.getAccountSerialNumber());
            model.setRefundAmount(refund.getRefundAmount().toString());
            model.setRefundReason(refund.getReason());
            model.setOutRequestNo(String.valueOf(refund.getOrderId()));
            alipay_request.setBizModel(model);
            try {
                AlipayTradeRefundResponse alipay_response = client.execute(alipay_request);
                return "10000".equalsIgnoreCase(alipay_response.getCode());
            } catch (AlipayApiException e) {
            }
        }
        return false;
    }

    @Override
    public List<SysExtendField> getExtendFieldList(SysSite site, Locale locale) {
        return null;
    }

    @Override
    public boolean enable(short siteId) {
        Map<String, String> config = configComponent.getConfigData(siteId, CONFIG_CODE);
        if (CommonUtils.notEmpty(config) && CommonUtils.notEmpty(config.get(CONFIG_URL))) {
            return true;
        }
        return false;
    }

    @Override
    public String notify(short siteId, String body, Map<String, String[]> parameterMap) {
        Map<String, String> config = configComponent.getConfigData(siteId, CONFIG_CODE);
        if (CommonUtils.notEmpty(config)) {
            Map<String, String> params = new HashMap<String, String>();
            for (Entry<String, String[]> entry : parameterMap.entrySet()) {
                String[] values = entry.getValue();
                String valueStr = "";
                for (int i = 0; i < values.length; i++) {
                    valueStr = (i == values.length - 1) ? valueStr + values[i] : valueStr + values[i] + ",";
                }
                params.put(entry.getKey(), valueStr);
            }
            try {
                if (AlipaySignature.rsaCheckV1(params, config.get(CONFIG_ALIPAY_PUBLIC_KEY), CommonConstants.DEFAULT_CHARSET_NAME,
                        "RSA2")) {
                    String[] out_trade_nos = parameterMap.get("out_trade_no");
                    String[] total_fees = parameterMap.get("trade_status");
                    String[] trade_nos = parameterMap.get("trade_no");
                    if (CommonUtils.notEmpty(out_trade_nos) && CommonUtils.notEmpty(total_fees)
                            && CommonUtils.notEmpty(trade_nos)) {
                        String out_trade_no = out_trade_nos[0];
                        String total_fee = total_fees[0];
                        String trade_no = trade_nos[0];
                        TradeOrder order = service.getEntity(out_trade_no);
                        if (null != order && order.getStatus() == TradeOrderService.STATUS_PENDING_PAY
                                && order.getAmount().toString().equals(total_fee)) {
                            service.paid(order.getId(), trade_no);
                            return "success";
                        }
                    }
                }
            } catch (AlipayApiException e) {
            }
        }
        return "fail";
    }

}

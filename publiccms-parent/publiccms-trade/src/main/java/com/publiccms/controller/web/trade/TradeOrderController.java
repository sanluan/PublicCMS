package com.publiccms.controller.web.trade;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.view.UrlBasedViewResolver;

import com.publiccms.common.api.Config;
import com.publiccms.common.api.PaymentGateway;
import com.publiccms.common.constants.CommonConstants;
import com.publiccms.common.tools.ControllerUtils;
import com.publiccms.entities.sys.SysSite;
import com.publiccms.entities.trade.TradeOrder;
import com.publiccms.entities.trade.TradeRefund;
import com.publiccms.logic.component.config.ConfigComponent;
import com.publiccms.logic.component.config.LoginConfigComponent;
import com.publiccms.logic.component.trade.PaymentGatewayComponent;
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
        if (!paymentGateway.pay(entity, returnUrl, site.getDynamicPath() + "tradeOrder/notify/" + accountType, response)) {
            response.sendRedirect(returnUrl);
        }
    }

    /**
     * @param site
     * @param accountType
     * @param body
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "notify/{accountType}")
    @ResponseBody
    public String notify(@RequestAttribute SysSite site, @PathVariable("accountType") String accountType,
            @RequestBody(required = false) String body, HttpServletRequest request) throws Exception {
        PaymentGateway paymentGateway = gatewayComponent.get(accountType);
        if (null != paymentGateway) {
            return paymentGateway.notify(site.getId(), body, request.getParameterMap());
        }
        return CommonConstants.BLANK;
    }

    /**
     * @param site
     * @param entity
     * @param returnUrl
     * @param request
     * @param model
     * @return
     */
    @RequestMapping(value = "refund")
    @ResponseBody
    public String refund(@RequestAttribute SysSite site, TradeRefund entity, String returnUrl, HttpServletRequest request,
            ModelMap model) {
        Map<String, String> config = configComponent.getConfigData(site.getId(), Config.CONFIG_CODE_SITE);
        String safeReturnUrl = config.get(LoginConfigComponent.CONFIG_RETURN_URL);
        if (ControllerUtils.isUnSafeUrl(returnUrl, site, safeReturnUrl, request)) {
            returnUrl = site.isUseStatic() ? site.getSitePath() : site.getDynamicPath();
        }
        if (ControllerUtils.verifyCustom("tradeOrderStatus", !service.pendingRefund(site.getId(), entity.getOrderId()), model)) {
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
}

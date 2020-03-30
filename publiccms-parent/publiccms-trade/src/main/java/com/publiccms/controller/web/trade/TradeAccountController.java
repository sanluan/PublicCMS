package com.publiccms.controller.web.trade;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

// Generated 2019-6-16 9:47:27 by com.publiccms.common.generator.SourceGenerator

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.view.UrlBasedViewResolver;

import com.publiccms.common.annotation.Csrf;
import com.publiccms.common.api.Config;
import com.publiccms.common.api.PaymentGateway;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.common.tools.ControllerUtils;
import com.publiccms.common.tools.RequestUtils;
import com.publiccms.entities.sys.SysSite;
import com.publiccms.entities.sys.SysUser;
import com.publiccms.entities.trade.TradeAccountHistory;
import com.publiccms.entities.trade.TradeOrder;
import com.publiccms.logic.component.config.ConfigComponent;
import com.publiccms.logic.component.config.LoginConfigComponent;
import com.publiccms.logic.component.orderprocessor.ChargeProcessorComponent;
import com.publiccms.logic.component.trade.PaymentGatewayComponent;
import com.publiccms.logic.service.trade.TradeAccountHistoryService;
import com.publiccms.logic.service.trade.TradeOrderService;

/**
 *
 * TradeAccountAdminController
 * 
 */
@Controller
@RequestMapping("tradeAccount")
public class TradeAccountController {

    /**
     * @param site
     * @param change
     * @param accountType
     * @param returnUrl
     * @param request
     * @param session
     * @param model
     * @return operate result
     */
    @RequestMapping("recharge/{accountType}")
    @Csrf
    public String recharge(@RequestAttribute SysSite site, BigDecimal change, @PathVariable("accountType") String accountType,
            String returnUrl, HttpServletRequest request, HttpSession session, ModelMap model) {
        Map<String, String> config = configComponent.getConfigData(site.getId(), Config.CONFIG_CODE_SITE);
        String safeReturnUrl = config.get(LoginConfigComponent.CONFIG_RETURN_URL);
        if (ControllerUtils.isUnSafeUrl(returnUrl, site, safeReturnUrl, request)) {
            returnUrl = site.isUseStatic() ? site.getSitePath() : site.getDynamicPath();
        }
        PaymentGateway paymentGateway = gatewayComponent.get(accountType);
        if (null != paymentGateway && paymentGateway.enable(site.getId())) {
            SysUser user = ControllerUtils.getUserFromSession(session);
            if (null != user && null != change && 1 == change.compareTo(BigDecimal.ZERO)) {
                String ip = RequestUtils.getIpAddress(request);
                Date now = CommonUtils.getDate();
                TradeOrder entity = new TradeOrder(site.getId(), user.getId(), change, ChargeProcessorComponent.GRADE_TYPE,
                        UUID.randomUUID().toString(), accountType, ip, TradeOrderService.STATUS_PENDING_PAY, false, now);
                orderService.create(entity);
                TradeAccountHistory history = new TradeAccountHistory(site.getId(), UUID.randomUUID().toString(), user.getId(),
                        user.getId(), change, BigDecimal.ZERO, BigDecimal.ZERO, TradeAccountHistoryService.STATUS_PEND, null,
                        now);
                historyService.save(history);
                return UrlBasedViewResolver.REDIRECT_URL_PREFIX + site.getDynamicPath() + "tradeOrder/pay/" + accountType
                        + "?orderId=" + entity.getId() + "&returnUrl=" + returnUrl;
            }
        }
        return UrlBasedViewResolver.REDIRECT_URL_PREFIX + returnUrl;
    }

    @Autowired
    private PaymentGatewayComponent gatewayComponent;
    @Autowired
    protected ConfigComponent configComponent;
    @Autowired
    private TradeOrderService orderService;
    @Autowired
    private TradeAccountHistoryService historyService;
}
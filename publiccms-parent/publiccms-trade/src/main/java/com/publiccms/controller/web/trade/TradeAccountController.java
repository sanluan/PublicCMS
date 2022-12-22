package com.publiccms.controller.web.trade;

import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

// Generated 2019-6-16 9:47:27 by com.publiccms.common.generator.SourceGenerator

import javax.servlet.http.HttpServletRequest;
import javax.annotation.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.servlet.view.UrlBasedViewResolver;

import com.publiccms.common.annotation.Csrf;
import com.publiccms.common.api.PaymentGateway;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.common.tools.RequestUtils;
import com.publiccms.entities.sys.SysSite;
import com.publiccms.entities.sys.SysUser;
import com.publiccms.entities.trade.TradeAccountHistory;
import com.publiccms.entities.trade.TradePayment;
import com.publiccms.logic.component.config.SiteConfigComponent;
import com.publiccms.logic.component.paymentgateway.AccountGatewayComponent;
import com.publiccms.logic.component.paymentprocessor.RechargeProcessorComponent;
import com.publiccms.logic.component.trade.PaymentGatewayComponent;
import com.publiccms.logic.service.trade.TradeAccountHistoryService;
import com.publiccms.logic.service.trade.TradePaymentService;

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
     * @param user
     * @param change
     * @param accountType
     * @param returnUrl
     * @param request
     * @return operate result
     */
    @RequestMapping("recharge")
    @Csrf
    public String recharge(@RequestAttribute SysSite site, @SessionAttribute SysUser user, BigDecimal change, String accountType,
            String returnUrl, HttpServletRequest request) {
        returnUrl = siteConfigComponent.getSafeUrl(returnUrl, site, request.getContextPath());
        PaymentGateway paymentGateway = gatewayComponent.get(accountType);
        if (null != paymentGateway && !accountType.equalsIgnoreCase(accountGatewayComponent.getAccountType())
                && paymentGateway.enable(site.getId()) && null != change) {
            if (1 == change.compareTo(BigDecimal.ZERO)) {
                String ip = RequestUtils.getIpAddress(request);
                Date now = CommonUtils.getDate();
                TradePayment entity = new TradePayment(site.getId(), user.getId(), change, RechargeProcessorComponent.GRADE_TYPE,
                        UUID.randomUUID().toString(), accountType, ip, TradePaymentService.STATUS_PENDING_PAY, false, now);
                entity.setDescription("recharge");
                paymentService.create(site.getId(), entity);
                TradeAccountHistory history = new TradeAccountHistory(site.getId(), UUID.randomUUID().toString(), user.getId(),
                        user.getId(), change, BigDecimal.ZERO, BigDecimal.ZERO, TradeAccountHistoryService.STATUS_PEND, null,
                        now);
                historyService.save(history);
                return UrlBasedViewResolver.REDIRECT_URL_PREFIX + site.getDynamicPath() + "tradePayment/pay?paymentId="
                        + entity.getId() + "&returnUrl=" + returnUrl;
            }
        }
        return new StringBuilder(UrlBasedViewResolver.REDIRECT_URL_PREFIX).append(returnUrl).toString();
    }

    @Resource
    private AccountGatewayComponent accountGatewayComponent;
    @Resource
    private PaymentGatewayComponent gatewayComponent;
    @Resource
    protected SiteConfigComponent siteConfigComponent;
    @Resource
    private TradePaymentService paymentService;
    @Resource
    private TradeAccountHistoryService historyService;
}
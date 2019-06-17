package com.publiccms.controller.web.trade;

import java.math.BigDecimal;
import java.util.Date;
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

import com.publiccms.common.annotation.Csrf;
import com.publiccms.common.constants.CommonConstants;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.common.tools.ControllerUtils;
import com.publiccms.common.tools.RequestUtils;
import com.publiccms.entities.sys.SysSite;
import com.publiccms.entities.sys.SysUser;
import com.publiccms.entities.trade.TradeOrder;
import com.publiccms.logic.component.orderhandler.ChargeProcessorComponent;
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
     * @param request
     * @param session
     * @param model
     * @return operate result
     */
    @RequestMapping("charge/{accountType}")
    @Csrf
    public String save(@RequestAttribute SysSite site, BigDecimal change, @PathVariable("accountType") String accountType,
            HttpServletRequest request, HttpSession session, ModelMap model) {
        SysUser user = ControllerUtils.getUserFromSession(session);
        if (null != user && null != change && 1 == change.compareTo(BigDecimal.ZERO)) {
            String ip = RequestUtils.getIpAddress(request);
            Date now = CommonUtils.getDate();
            TradeOrder entity = new TradeOrder(site.getId(), user.getId(), change, ChargeProcessorComponent.GRADE_TYPE,
                    UUID.randomUUID().toString(), accountType, ip, TradeOrderService.STATUS_PENDING_PAY, false, now);
            orderService.create(entity);
        }
        return CommonConstants.TEMPLATE_DONE;
    }

    @Autowired
    private TradeOrderService orderService;
}
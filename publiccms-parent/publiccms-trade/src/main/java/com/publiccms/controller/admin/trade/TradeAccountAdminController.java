package com.publiccms.controller.admin.trade;

import java.math.BigDecimal;

// Generated 2019-6-16 9:47:27 by com.publiccms.common.generator.SourceGenerator

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttribute;

import com.publiccms.common.annotation.Csrf;
import com.publiccms.common.constants.CommonConstants;
import com.publiccms.entities.sys.SysSite;
import com.publiccms.entities.sys.SysUser;
import com.publiccms.entities.trade.TradeAccountHistory;
import com.publiccms.logic.service.trade.TradeAccountHistoryService;
import com.publiccms.logic.service.trade.TradeAccountService;

/**
 *
 * TradeAccountAdminController
 * 
 */
@Controller
@RequestMapping("tradeAccount")
public class TradeAccountAdminController {

    /**
     * @param site
     * @param admin
     * @param serialNumber
     * @param accountId
     * @param change
     * @param description 
     * @param request
     * @param model
     * @return operate result
     */
    @RequestMapping("charge")
    @Csrf
    public String save(@RequestAttribute SysSite site, @SessionAttribute SysUser admin, String serialNumber, long accountId,
            BigDecimal change, String description, HttpServletRequest request, ModelMap model) {
        TradeAccountHistory history = service.change(site.getId(), serialNumber, accountId, admin.getId(),
                TradeAccountHistoryService.STATUS_CHARGE, change, description);
        if (null != history) {
            return CommonConstants.TEMPLATE_DONE;
        }
        return CommonConstants.TEMPLATE_ERROR;
    }

    @Autowired
    private TradeAccountService service;
}
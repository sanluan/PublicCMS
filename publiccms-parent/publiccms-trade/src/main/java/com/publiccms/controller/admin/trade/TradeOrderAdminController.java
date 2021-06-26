package com.publiccms.controller.admin.trade;

// Generated 2021-6-26 20:16:25 by com.publiccms.common.generator.SourceGenerator

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
import com.publiccms.logic.service.log.LogOperateService;
import com.publiccms.logic.service.trade.TradeOrderService;

/**
 *
 * TradeOrderAdminController
 * 
 */
@Controller
@RequestMapping("tradeOrder")
public class TradeOrderAdminController {

    /**
     * @param site
     * @param admin
     * @param orderId
     * @param request
     * @param model
     * @return operate result
     */
    @RequestMapping("confirm")
    @Csrf
    public String confirm(@RequestAttribute SysSite site, @SessionAttribute SysUser admin, long orderId,
            HttpServletRequest request, ModelMap model) {
        service.confirm(site.getId(), orderId);
        return CommonConstants.TEMPLATE_DONE;
    }

    /**
     * @param ids
     * @param request
     * @param site
     * @param admin
     * @param orderId
     * @param processInfo
     * @param model
     * @return operate result
     */
    @RequestMapping("process")
    @Csrf
    public String process(@RequestAttribute SysSite site, @SessionAttribute SysUser admin, long orderId, String processInfo,
            HttpServletRequest request, ModelMap model) {
        service.processed(site.getId(), orderId, admin.getId(), processInfo);
        return CommonConstants.TEMPLATE_DONE;
    }

    @Autowired
    private TradeOrderService service;
    @Autowired
    protected LogOperateService logOperateService;
}
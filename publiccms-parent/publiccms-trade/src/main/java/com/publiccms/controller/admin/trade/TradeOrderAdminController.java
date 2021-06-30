package com.publiccms.controller.admin.trade;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttribute;

import com.publiccms.common.annotation.Csrf;
import com.publiccms.common.constants.CommonConstants;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.entities.sys.SysSite;
import com.publiccms.entities.sys.SysUser;
import com.publiccms.entities.trade.TradeOrder;
import com.publiccms.entities.trade.TradeRefund;
import com.publiccms.logic.service.log.LogOperateService;
import com.publiccms.logic.service.trade.TradeOrderService;
import com.publiccms.logic.service.trade.TradeRefundService;

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
     * @param id
     * @return operate result
     */
    @RequestMapping("confirm")
    @Csrf
    public String confirm(@RequestAttribute SysSite site, @SessionAttribute SysUser admin, long id) {
        service.confirm(site.getId(), id);
        return CommonConstants.TEMPLATE_DONE;
    }

    /**
     * @param site
     * @param admin
     * @param id
     * @return operate result
     */
    @RequestMapping("invalid")
    @Csrf
    public String invalid(@RequestAttribute SysSite site, @SessionAttribute SysUser admin, long id) {
        TradeOrder entity = service.getEntity(id);
        if (null != entity && service.invalid(site.getId(), id)) {
            if (null != entity.getPaymentId()) {
                TradeRefund refund = new TradeRefund(site.getId(), entity.getUserId(), entity.getPaymentId(), entity.getAmount(),
                        TradeRefundService.STATUS_PENDING, CommonUtils.getDate());
                refundService.save(refund);
            }
        }
        return CommonConstants.TEMPLATE_DONE;
    }

    /**
     * @param ids
     * @param request
     * @param site
     * @param admin
     * @param id
     * @param processInfo
     * @return operate result
     */
    @RequestMapping("process")
    @Csrf
    public String process(@RequestAttribute SysSite site, @SessionAttribute SysUser admin, long id, String processInfo) {
        service.processed(site.getId(), id, admin.getId(), processInfo);
        return CommonConstants.TEMPLATE_DONE;
    }

    /**
     * @param site 
     * @param admin 
     * @param id 
     * @return operate result
     */
    @RequestMapping("close")
    @Csrf
    public String close(@RequestAttribute SysSite site, @SessionAttribute SysUser admin, long id) {
        service.close(site.getId(), id);
        return CommonConstants.TEMPLATE_DONE;
    }

    @Autowired
    private TradeOrderService service;
    @Autowired
    private TradeRefundService refundService;
    @Autowired
    protected LogOperateService logOperateService;
}
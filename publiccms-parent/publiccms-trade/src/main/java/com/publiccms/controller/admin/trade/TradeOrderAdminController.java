package com.publiccms.controller.admin.trade;

import java.io.Serializable;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.servlet.support.RequestContextUtils;

import com.publiccms.common.annotation.Csrf;
import com.publiccms.common.constants.CommonConstants;
import com.publiccms.common.constants.Constants;
import com.publiccms.common.handler.PageHandler;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.common.tools.DateFormatUtils;
import com.publiccms.common.tools.LanguagesUtils;
import com.publiccms.common.view.ExcelView;
import com.publiccms.entities.cms.CmsContentProduct;
import com.publiccms.entities.sys.SysSite;
import com.publiccms.entities.sys.SysUser;
import com.publiccms.entities.trade.TradeOrder;
import com.publiccms.entities.trade.TradeOrderProduct;
import com.publiccms.entities.trade.TradeRefund;
import com.publiccms.logic.service.cms.CmsContentProductService;
import com.publiccms.logic.service.sys.SysUserService;
import com.publiccms.logic.service.trade.TradeOrderProductService;
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
     * @param userId
     * @param paymentId
     * @param status
     * @param processed
     * @param startCreateDate
     * @param endCreateDate
     * @param orderType
     * @param request
     * @return
     */
    @RequestMapping("export")
    @Csrf
    public ExcelView export(@RequestAttribute SysSite site, Long userId, Long paymentId, Integer[] status, Boolean processed,
            @DateTimeFormat(pattern = "yyyy-MM-dd") Date startCreateDate,
            @DateTimeFormat(pattern = "yyyy-MM-dd") Date endCreateDate, String orderType, HttpServletRequest request) {
        Locale locale = RequestContextUtils.getLocale(request);
        PageHandler page = service.getPage(site.getId(), userId, paymentId, status, processed, startCreateDate, endCreateDate,
                orderType, 1, PageHandler.MAX_PAGE_SIZE);
        @SuppressWarnings("unchecked")
        List<TradeOrder> entityList = (List<TradeOrder>) page.getList();
        List<Serializable> userIdList = new ArrayList<>();
        for (TradeOrder entity : entityList) {
            userIdList.add(entity.getUserId());
            userIdList.add(entity.getProcessUserId());
        }
        Map<Long, SysUser> userMap = new HashMap<>();
        if (!userIdList.isEmpty()) {
            List<SysUser> entitys = sysUserService.getEntitys(userIdList);
            for (SysUser entity : entitys) {
                userMap.put(entity.getId(), entity);
            }
        }

        ExcelView view = new ExcelView(workbook -> {
            Sheet sheet = workbook
                    .createSheet(LanguagesUtils.getMessage(CommonConstants.applicationContext, locale, "page.order"));
            int i = 0;
            int j = 0;
            Row row = sheet.createRow(i++);
            row.createCell(j++).setCellValue(LanguagesUtils.getMessage(CommonConstants.applicationContext, locale, "page.id"));
            row.createCell(j++).setCellValue(LanguagesUtils.getMessage(CommonConstants.applicationContext, locale, "page.user"));
            row.createCell(j++).setCellValue(LanguagesUtils.getMessage(CommonConstants.applicationContext, locale, "page.title"));
            row.createCell(j++)
                    .setCellValue(LanguagesUtils.getMessage(CommonConstants.applicationContext, locale, "page.receiver"));
            row.createCell(j++)
                    .setCellValue(LanguagesUtils.getMessage(CommonConstants.applicationContext, locale, "page.remark"));
            row.createCell(j++)
                    .setCellValue(LanguagesUtils.getMessage(CommonConstants.applicationContext, locale, "page.status"));
            row.createCell(j++)
                    .setCellValue(LanguagesUtils.getMessage(CommonConstants.applicationContext, locale, "page.order.process"));
            row.createCell(j++)
                    .setCellValue(LanguagesUtils.getMessage(CommonConstants.applicationContext, locale, "page.payment"));
            row.createCell(j++)
                    .setCellValue(LanguagesUtils.getMessage(CommonConstants.applicationContext, locale, "page.createDate"));

            SysUser user;
            DateFormat dateFormat = DateFormatUtils.getDateFormat(DateFormatUtils.FULL_DATE_FORMAT_STRING);
            for (TradeOrder entity : entityList) {
                row = sheet.createRow(i++);
                j = 0;
                row.createCell(j++).setCellValue(entity.getId().toString());
                user = userMap.get(entity.getUserId());
                row.createCell(j++)
                        .setCellValue(null == user ? null : CommonUtils.joinString(user.getNickname(), entity.getIp()));
                row.createCell(j++).setCellValue(entity.getTitle());
                row.createCell(j++)
                        .setCellValue(CommonUtils.joinString(entity.getAddressee(), " ", entity.getTelephone(), "\r\n",
                                LanguagesUtils.getMessage(CommonConstants.applicationContext, locale, "page.shipping_address"),
                                ":", entity.getAddress()));
                row.createCell(j++).setCellValue(entity.getRemark());
                row.createCell(j++).setCellValue(LanguagesUtils.getMessage(CommonConstants.applicationContext, locale,
                        CommonUtils.joinString("page.status.order.", entity.getStatus())));
                row.createCell(j++)
                        .setCellValue(CommonUtils.joinString(
                                null == entity.getProcessDate() ? ""
                                        : CommonUtils.joinString(dateFormat.format(entity.getProcessDate()), "\r\n"),
                                LanguagesUtils.getMessage(CommonConstants.applicationContext, locale, "page.order.process_info"),
                                ":", entity.getProcessInfo()));
                row.createCell(j++)
                        .setCellValue(CommonUtils.joinString(
                                null == entity.getPaymentId() ? ""
                                        : CommonUtils.joinString(LanguagesUtils.getMessage(CommonConstants.applicationContext,
                                                locale, "page.payment.id"), ":", entity.getPaymentId(), "\r\n"),
                                (null == entity.getPaymentDate() ? "" : dateFormat.format(entity.getProcessDate()))));
                row.createCell(j++).setCellValue(dateFormat.format(entity.getCreateDate()));

                List<TradeOrderProduct> productList = traderProductService.getList(site.getId(), entity.getId());
                for (TradeOrderProduct tradeOrderProduct : productList) {
                    CmsContentProduct product = productService.getEntity(tradeOrderProduct.getProductId());
                    if (null != product) {
                        row = sheet.createRow(i++);
                        j = 0;
                        row.createCell(j++).setCellValue("-");
                        row.createCell(j++).setCellValue(
                                LanguagesUtils.getMessage(CommonConstants.applicationContext, locale, "page.product_list"));
                        row.createCell(j++).setCellValue(product.getTitle());
                        row.createCell(j++).setCellValue(
                                LanguagesUtils.getMessage(CommonConstants.applicationContext, locale, "page.product.quantity")
                                        + ":" + tradeOrderProduct.getQuantity());
                        row.createCell(j++).setCellValue(tradeOrderProduct.getRemark());
                    }
                }
            }

        });
        DateFormat dateFormat = DateFormatUtils.getDateFormat(DateFormatUtils.SHORT_DATE_FORMAT_STRING);
        view.setFilename(
                CommonUtils.joinString(LanguagesUtils.getMessage(CommonConstants.applicationContext, locale, "page.order"),
                        Constants.UNDERLINE, dateFormat.format(new Date())));
        return view;
    }

    /**
     * @param site
     * @param id
     * @return operate result
     */
    @RequestMapping("confirm")
    @Csrf
    public String confirm(@RequestAttribute SysSite site, long id) {
        service.confirm(site.getId(), id);
        return CommonConstants.TEMPLATE_DONE;
    }

    /**
     * @param site
     * @param id
     * @return operate result
     */
    @RequestMapping("invalid")
    @Csrf
    public String invalid(@RequestAttribute SysSite site, long id) {
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
     * @param id
     * @return operate result
     */
    @RequestMapping("close")
    @Csrf
    public String close(@RequestAttribute SysSite site, long id) {
        service.close(site.getId(), id, null);
        return CommonConstants.TEMPLATE_DONE;
    }

    @Resource
    private TradeOrderService service;
    @Resource
    private TradeOrderProductService traderProductService;
    @Resource
    private CmsContentProductService productService;
    @Resource
    private TradeRefundService refundService;
    @Resource
    private SysUserService sysUserService;
}
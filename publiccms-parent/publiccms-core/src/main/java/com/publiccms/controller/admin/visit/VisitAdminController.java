package com.publiccms.controller.admin.visit;

import java.util.Date;
import java.util.Locale;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.support.RequestContextUtils;

import com.publiccms.common.annotation.Csrf;
import com.publiccms.common.view.ExcelView;
import com.publiccms.entities.sys.SysSite;
import com.publiccms.logic.component.exchange.VisitExchangeComponent;

import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;

/**
 * 
 * VisitAdminController
 *
 */
@Controller
@RequestMapping("visit")
public class VisitAdminController {
    protected final Log log = LogFactory.getLog(getClass());
    @Resource
    private VisitExchangeComponent exchangeComponent;

    /**
     * @param site
     * @param sessionId
     * @param ip
     * @param url
     * @param userId 
     * @param startCreateDate
     * @param endCreateDate
     * @param orderType
     * @param request
     * @return view name
     */
    @RequestMapping("exportHistory")
    @Csrf
    public ExcelView exportHistory(@RequestAttribute SysSite site, String sessionId, String ip, String url, Long userId,
            @DateTimeFormat(pattern = "yyyy-MM-dd") Date startCreateDate,
            @DateTimeFormat(pattern = "yyyy-MM-dd") Date endCreateDate, String orderType, HttpServletRequest request) {
        Locale locale = RequestContextUtils.getLocale(request);
        return exchangeComponent.exportHistory(site, sessionId, ip, url, userId, startCreateDate, endCreateDate, orderType,
                locale);
    }

    /**
     * @param site
     * @param hourAnalytics
     * @param startVisitDate
     * @param endVisitDate
     * @param orderField
     * @param orderType
     * @param request
     * @return view name
     */
    @RequestMapping("exportDay")
    @Csrf
    public ExcelView exportDay(@RequestAttribute SysSite site, @DateTimeFormat(pattern = "yyyy-MM-dd") Date startVisitDate,
            @DateTimeFormat(pattern = "yyyy-MM-dd") Date endVisitDate, boolean hourAnalytics, HttpServletRequest request) {
        Locale locale = RequestContextUtils.getLocale(request);
        return exchangeComponent.exportDay(site, startVisitDate, endVisitDate, hourAnalytics, locale);
    }

    /**
     * @param site
     * @param sessionId
     * @param ip
     * @param startVisitDate
     * @param endVisitDate
     * @param orderType
     * @param request
     * @return view name
     */
    @RequestMapping("exportSession")
    @Csrf
    public ExcelView exportSession(@RequestAttribute SysSite site, String sessionId, String ip,
            @DateTimeFormat(pattern = "yyyy-MM-dd") Date startVisitDate,
            @DateTimeFormat(pattern = "yyyy-MM-dd") Date endVisitDate, String orderType, HttpServletRequest request) {
        Locale locale = RequestContextUtils.getLocale(request);
        return exchangeComponent.exportSession(site, sessionId, ip, startVisitDate, endVisitDate, orderType, locale);
    }

    /**
     * @param site
     * @param url
     * @param startVisitDate
     * @param endVisitDate
     * @param request
     * @return view name
     */
    @RequestMapping("exportUrl")
    @Csrf
    public ExcelView exportUrl(@RequestAttribute SysSite site, String url,
            @DateTimeFormat(pattern = "yyyy-MM-dd") Date startVisitDate,
            @DateTimeFormat(pattern = "yyyy-MM-dd") Date endVisitDate, HttpServletRequest request) {
        Locale locale = RequestContextUtils.getLocale(request);
        return exchangeComponent.exportUrl(site, url, startVisitDate, endVisitDate, locale);
    }

    /**
     * @param site
     * @param itemType
     * @param itemId
     * @param startVisitDate
     * @param endVisitDate
     * @param request
     * @return view name
     */
    @RequestMapping("exportItem")
    @Csrf
    public ExcelView exportItem(@RequestAttribute SysSite site, String itemType, String itemId,
            @DateTimeFormat(pattern = "yyyy-MM-dd") Date startVisitDate,
            @DateTimeFormat(pattern = "yyyy-MM-dd") Date endVisitDate, HttpServletRequest request) {
        Locale locale = RequestContextUtils.getLocale(request);
        return exchangeComponent.exportItem(site, itemType, itemId, startVisitDate, endVisitDate, locale);
    }
}

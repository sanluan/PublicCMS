package com.publiccms.logic.component.exchange;

import java.text.DateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.stereotype.Component;

import com.publiccms.common.constants.CommonConstants;
import com.publiccms.common.handler.PageHandler;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.common.tools.DateFormatUtils;
import com.publiccms.common.tools.LanguagesUtils;
import com.publiccms.common.view.ExcelView;
import com.publiccms.entities.sys.SysSite;
import com.publiccms.entities.visit.VisitDay;
import com.publiccms.entities.visit.VisitHistory;
import com.publiccms.entities.visit.VisitItem;
import com.publiccms.entities.visit.VisitSession;
import com.publiccms.entities.visit.VisitUrl;
import com.publiccms.logic.service.visit.VisitDayService;
import com.publiccms.logic.service.visit.VisitHistoryService;
import com.publiccms.logic.service.visit.VisitItemService;
import com.publiccms.logic.service.visit.VisitSessionService;
import com.publiccms.logic.service.visit.VisitUrlService;

import jakarta.annotation.Resource;

/**
 * VisitExchangeComponent 统计数据导出组件
 * 
 */
@Component
public class VisitExchangeComponent {
    @Resource
    private VisitHistoryService historyService;
    @Resource
    private VisitDayService dayService;
    @Resource
    private VisitSessionService sessionService;
    @Resource
    private VisitUrlService urlService;
    @Resource
    private VisitItemService itemService;

    public ExcelView exportHistory(SysSite site, String sessionId, String ip, String url, Date startCreateDate,
            Date endCreateDate, String orderType, Locale locale) {
        PageHandler page = historyService.getPage(site.getId(), sessionId, ip, url, startCreateDate, endCreateDate, orderType,
                null, PageHandler.MAX_PAGE_SIZE);
        @SuppressWarnings("unchecked")
        List<VisitHistory> entityList = (List<VisitHistory>) page.getList();
        ExcelView view = new ExcelView(workbook -> {
            Sheet sheet = workbook
                    .createSheet(LanguagesUtils.getMessage(CommonConstants.applicationContext, locale, "page.visit.history"));
            sheet.setDefaultColumnWidth(20);
            int i = 0, j = 0;
            Row row = sheet.createRow(i++);
            row.createCell(j++).setCellValue(LanguagesUtils.getMessage(CommonConstants.applicationContext, locale, "page.id"));
            row.createCell(j++)
                    .setCellValue(LanguagesUtils.getMessage(CommonConstants.applicationContext, locale, "page.visit.session"));
            row.createCell(j++).setCellValue(LanguagesUtils.getMessage(CommonConstants.applicationContext, locale, "page.title"));
            row.createCell(j++).setCellValue(LanguagesUtils.getMessage(CommonConstants.applicationContext, locale, "page.url"));
            row.createCell(j++)
                    .setCellValue(LanguagesUtils.getMessage(CommonConstants.applicationContext, locale, "page.visit.referer"));
            row.createCell(j++)
                    .setCellValue(LanguagesUtils.getMessage(CommonConstants.applicationContext, locale, "page.visit.screen"));
            row.createCell(j++)
                    .setCellValue(LanguagesUtils.getMessage(CommonConstants.applicationContext, locale, "page.visit.item"));
            row.createCell(j++)
                    .setCellValue(LanguagesUtils.getMessage(CommonConstants.applicationContext, locale, "page.visit.ip"));
            row.createCell(j++)
                    .setCellValue(LanguagesUtils.getMessage(CommonConstants.applicationContext, locale, "page.visit.ip"));
            row.createCell(j++)
                    .setCellValue(LanguagesUtils.getMessage(CommonConstants.applicationContext, locale, "page.visit.visit_date"));
            DateFormat dateFormat = DateFormatUtils.getDateFormat(DateFormatUtils.FULL_DATE_FORMAT_STRING);
            for (VisitHistory entity : entityList) {
                row = sheet.createRow(i++);
                j = 0;
                row.createCell(j++).setCellValue(entity.getId().toString());
                row.createCell(j++).setCellValue(entity.getSessionId());
                row.createCell(j++).setCellValue(entity.getTitle());
                row.createCell(j++).setCellValue(entity.getUrl());
                row.createCell(j++).setCellValue(entity.getRefererUrl());
                row.createCell(j++).setCellValue(CommonUtils.joinString(entity.getScreenWidth(), "*", entity.getScreenHeight()));
                row.createCell(j++).setCellValue(null == entity.getItemId() ? null
                        : CommonUtils.joinString(entity.getItemType(), ":", entity.getItemId()));
                row.createCell(j++).setCellValue(entity.getIp());
                row.createCell(j++).setCellValue(dateFormat.format(entity.getCreateDate()));
            }
        });
        DateFormat dateFormat = DateFormatUtils.getDateFormat(DateFormatUtils.DOWNLOAD_FORMAT_STRING);
        view.setFilename(CommonUtils.joinString(
                LanguagesUtils.getMessage(CommonConstants.applicationContext, locale, "page.visit.history"),
                dateFormat.format(new Date())));
        return view;
    }

    public ExcelView exportSession(SysSite site, String sessionId, String ip, Date startVisitDate, Date endVisitDate,
            String orderField, String orderType, Locale locale) {
        PageHandler page = sessionService.getPage(site.getId(), sessionId, ip, startVisitDate, endVisitDate, orderType, null,
                PageHandler.MAX_PAGE_SIZE);
        @SuppressWarnings("unchecked")
        List<VisitSession> entityList = (List<VisitSession>) page.getList();
        ExcelView view = new ExcelView(workbook -> {
            Sheet sheet = workbook
                    .createSheet(LanguagesUtils.getMessage(CommonConstants.applicationContext, locale, "page.visit.session"));
            sheet.setDefaultColumnWidth(20);
            int i = 0, j = 0;
            Row row = sheet.createRow(i++);
            row.createCell(j++)
                    .setCellValue(LanguagesUtils.getMessage(CommonConstants.applicationContext, locale, "page.visit.session"));
            row.createCell(j++).setCellValue(
                    LanguagesUtils.getMessage(CommonConstants.applicationContext, locale, "page.visit.visit_date.last"));
            row.createCell(j++).setCellValue(
                    LanguagesUtils.getMessage(CommonConstants.applicationContext, locale, "page.visit.visit_date.first"));
            row.createCell(j++)
                    .setCellValue(LanguagesUtils.getMessage(CommonConstants.applicationContext, locale, "page.visit.ip"));
            row.createCell(j++).setCellValue("PV");
            DateFormat dateFormat = DateFormatUtils.getDateFormat(DateFormatUtils.FULL_DATE_FORMAT_STRING);
            for (VisitSession entity : entityList) {
                row = sheet.createRow(i++);
                j = 0;
                row.createCell(j++).setCellValue(entity.getId().getSessionId());
                row.createCell(j++).setCellValue(dateFormat.format(entity.getLastVisitDate()));
                row.createCell(j++).setCellValue(dateFormat.format(entity.getFirstVisitDate()));
                row.createCell(j++).setCellValue(entity.getIp());
                row.createCell(j++).setCellValue(entity.getPv());
            }
        });
        DateFormat dateFormat = DateFormatUtils.getDateFormat(DateFormatUtils.DOWNLOAD_FORMAT_STRING);
        view.setFilename(CommonUtils.joinString(
                LanguagesUtils.getMessage(CommonConstants.applicationContext, locale, "page.visit.session"),
                dateFormat.format(new Date())));
        return view;
    }

    public ExcelView exportDay(SysSite site, Date startVisitDate, Date endVisitDate, boolean hourAnalytics, Locale locale) {
        PageHandler page = dayService.getPage(site.getId(), startVisitDate, endVisitDate, hourAnalytics, null,
                PageHandler.MAX_PAGE_SIZE);
        @SuppressWarnings("unchecked")
        List<VisitDay> entityList = (List<VisitDay>) page.getList();
        ExcelView view = new ExcelView(workbook -> {
            Sheet sheet = workbook
                    .createSheet(LanguagesUtils.getMessage(CommonConstants.applicationContext, locale, "page.visit.visit_date"));
            sheet.setDefaultColumnWidth(20);
            int i = 0, j = 0;
            Row row = sheet.createRow(i++);
            row.createCell(j++)
                    .setCellValue(LanguagesUtils.getMessage(CommonConstants.applicationContext, locale, "page.visit.visit_date"));
            row.createCell(j++).setCellValue("PV");
            row.createCell(j++).setCellValue("UV");
            row.createCell(j++).setCellValue("IP Views");
            DateFormat dateFormat = DateFormatUtils.getDateFormat(DateFormatUtils.SHORT_DATE_FORMAT_STRING);
            for (VisitDay entity : entityList) {
                row = sheet.createRow(i++);
                j = 0;
                row.createCell(j++)
                        .setCellValue(hourAnalytics
                                ? CommonUtils.joinString(dateFormat.format(entity.getId().getVisitDate()),
                                        entity.getId().getVisitHour(), ":00:00")
                                : dateFormat.format(entity.getId().getVisitDate()));
                row.createCell(j++).setCellValue(entity.getPv());
                row.createCell(j++).setCellValue(entity.getUv());
                row.createCell(j++).setCellValue(entity.getIpviews());
            }
        });
        DateFormat dateFormat = DateFormatUtils.getDateFormat(DateFormatUtils.DOWNLOAD_FORMAT_STRING);
        view.setFilename(CommonUtils.joinString(
                LanguagesUtils.getMessage(CommonConstants.applicationContext, locale, "page.visit.visit_date"),
                dateFormat.format(new Date())));
        return view;
    }

    public ExcelView exportUrl(SysSite site, String url, Date startVisitDate, Date endVisitDate, Locale locale) {
        PageHandler page = urlService.getPage(site.getId(), url, startVisitDate, endVisitDate, null, PageHandler.MAX_PAGE_SIZE);
        @SuppressWarnings("unchecked")
        List<VisitUrl> entityList = (List<VisitUrl>) page.getList();
        ExcelView view = new ExcelView(workbook -> {
            Sheet sheet = workbook.createSheet(LanguagesUtils.getMessage(CommonConstants.applicationContext, locale, "url"));
            sheet.setDefaultColumnWidth(20);
            int i = 0, j = 0;
            Row row = sheet.createRow(i++);
            row.createCell(j++)
                    .setCellValue(LanguagesUtils.getMessage(CommonConstants.applicationContext, locale, "page.visit.visit_date"));
            row.createCell(j++).setCellValue(LanguagesUtils.getMessage(CommonConstants.applicationContext, locale, "page.url"));
            row.createCell(j++).setCellValue("PV");
            row.createCell(j++).setCellValue("UV");
            row.createCell(j++).setCellValue("IP Views");
            DateFormat dateFormat = DateFormatUtils.getDateFormat(DateFormatUtils.SHORT_DATE_FORMAT_STRING);
            for (VisitUrl entity : entityList) {
                row = sheet.createRow(i++);
                j = 0;
                row.createCell(j++).setCellValue(dateFormat.format(entity.getId().getVisitDate()));
                row.createCell(j++).setCellValue(entity.getPv());
                row.createCell(j++).setCellValue(entity.getUv());
                row.createCell(j++).setCellValue(entity.getIpviews());
            }
        });
        DateFormat dateFormat = DateFormatUtils.getDateFormat(DateFormatUtils.DOWNLOAD_FORMAT_STRING);
        view.setFilename(CommonUtils.joinString(LanguagesUtils.getMessage(CommonConstants.applicationContext, locale, "url"),
                dateFormat.format(new Date())));
        return view;
    }

    public ExcelView exportItem(SysSite site, String itemType, String itemId, Date startVisitDate, Date endVisitDate,
            Locale locale) {
        PageHandler page = itemService.getPage(site.getId(), startVisitDate, endVisitDate, itemType, itemId, null,
                PageHandler.MAX_PAGE_SIZE);
        @SuppressWarnings("unchecked")
        List<VisitItem> entityList = (List<VisitItem>) page.getList();
        ExcelView view = new ExcelView(workbook -> {
            Sheet sheet = workbook
                    .createSheet(LanguagesUtils.getMessage(CommonConstants.applicationContext, locale, "page.visit.item"));
            sheet.setDefaultColumnWidth(20);
            int i = 0, j = 0;
            Row row = sheet.createRow(i++);
            row.createCell(j++)
                    .setCellValue(LanguagesUtils.getMessage(CommonConstants.applicationContext, locale, "page.visit.visit_date"));
            row.createCell(j++)
                    .setCellValue(LanguagesUtils.getMessage(CommonConstants.applicationContext, locale, "page.item_type"));
            row.createCell(j++)
                    .setCellValue(LanguagesUtils.getMessage(CommonConstants.applicationContext, locale, "page.visit.item"));
            row.createCell(j++).setCellValue("PV");
            row.createCell(j++).setCellValue("UV");
            row.createCell(j++).setCellValue("IP Views");
            DateFormat dateFormat = DateFormatUtils.getDateFormat(DateFormatUtils.SHORT_DATE_FORMAT_STRING);
            for (VisitItem entity : entityList) {
                row = sheet.createRow(i++);
                j = 0;
                row.createCell(j++).setCellValue(dateFormat.format(entity.getId().getVisitDate()));
                row.createCell(j++).setCellValue(entity.getId().getItemType());
                row.createCell(j++).setCellValue(entity.getId().getItemId());
                row.createCell(j++).setCellValue(entity.getPv());
                row.createCell(j++).setCellValue(entity.getUv());
                row.createCell(j++).setCellValue(entity.getIpviews());
            }
        });
        DateFormat dateFormat = DateFormatUtils.getDateFormat(DateFormatUtils.DOWNLOAD_FORMAT_STRING);
        view.setFilename(
                CommonUtils.joinString(LanguagesUtils.getMessage(CommonConstants.applicationContext, locale, "page.visit.item"),
                        dateFormat.format(new Date())));
        return view;
    }

}

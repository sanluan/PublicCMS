package com.publiccms.logic.component.exchange;

import java.io.Serializable;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.stereotype.Component;

import com.publiccms.common.constants.CommonConstants;
import com.publiccms.common.handler.PageHandler;
import com.publiccms.common.tools.CmsUrlUtils;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.common.tools.DateFormatUtils;
import com.publiccms.common.tools.LanguagesUtils;
import com.publiccms.common.view.ExcelView;
import com.publiccms.entities.cms.CmsCategory;
import com.publiccms.entities.cms.CmsContent;
import com.publiccms.entities.sys.SysSite;
import com.publiccms.entities.visit.VisitDay;
import com.publiccms.entities.visit.VisitHistory;
import com.publiccms.entities.visit.VisitItem;
import com.publiccms.entities.visit.VisitSession;
import com.publiccms.entities.visit.VisitUrl;
import com.publiccms.logic.service.cms.CmsCategoryService;
import com.publiccms.logic.service.cms.CmsContentService;
import com.publiccms.logic.service.visit.VisitDayService;
import com.publiccms.logic.service.visit.VisitHistoryService;
import com.publiccms.logic.service.visit.VisitItemService;
import com.publiccms.logic.service.visit.VisitSessionService;
import com.publiccms.logic.service.visit.VisitUrlService;

/**
 * VisitExportComponent 统计数据导出组件
 * 
 */
@Component
public class VisitExportComponent {
    @Resource
    private VisitHistoryService historyService;
    @Resource
    private VisitDayService dayService;
    @Resource
    private VisitSessionService sessionService;
    @Resource
    private CmsContentService contentService;
    @Resource
    private CmsCategoryService categoryService;
    @Resource
    private VisitUrlService urlService;
    @Resource
    private VisitItemService itemService;

    public ExcelView exportHistory(SysSite site, String sessionId, String ip, String url, Long userId, Date startCreateDate,
            Date endCreateDate, String orderType, Locale locale) {
        PageHandler page = historyService.getPage(site.getId(), sessionId, ip, url, userId, startCreateDate, endCreateDate,
                orderType, null, PageHandler.MAX_PAGE_SIZE);
        @SuppressWarnings("unchecked")
        List<VisitHistory> entityList = (List<VisitHistory>) page.getList();
        ExcelView view = new ExcelView(workbook -> {
            Sheet sheet = workbook
                    .createSheet(LanguagesUtils.getMessage(CommonConstants.applicationContext, locale, "page.visit.history"));
            sheet.setDefaultColumnWidth(20);
            int i = 0;
            int j = 0;
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
            row.createCell(j++).setCellValue(LanguagesUtils.getMessage(CommonConstants.applicationContext, locale, "page.item"));
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
            String orderType, Locale locale) {
        PageHandler page = sessionService.getPage(site.getId(), sessionId, ip, startVisitDate, endVisitDate, orderType, null,
                PageHandler.MAX_PAGE_SIZE);
        @SuppressWarnings("unchecked")
        List<VisitSession> entityList = (List<VisitSession>) page.getList();
        ExcelView view = new ExcelView(workbook -> {
            Sheet sheet = workbook
                    .createSheet(LanguagesUtils.getMessage(CommonConstants.applicationContext, locale, "page.visit.session"));
            sheet.setDefaultColumnWidth(20);
            int i = 0;
            int j = 0;
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
            int i = 0;
            int j = 0;
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
            Sheet sheet = workbook.createSheet(LanguagesUtils.getMessage(CommonConstants.applicationContext, locale, "page.url"));
            sheet.setDefaultColumnWidth(20);
            int i = 0;
            int j = 0;
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
        view.setFilename(CommonUtils.joinString(LanguagesUtils.getMessage(CommonConstants.applicationContext, locale, "page.url"),
                dateFormat.format(new Date())));
        return view;
    }

    public ExcelView exportItem(SysSite site, String itemType, String itemId, String orderField, Date startVisitDate,
            Date endVisitDate, Boolean dayAnalytics, Locale locale) {
        PageHandler page = itemService.getPage(site.getId(), startVisitDate, endVisitDate,
                null == dayAnalytics ? true : dayAnalytics, itemType, itemId, orderField, null, PageHandler.MAX_PAGE_SIZE);
        @SuppressWarnings("unchecked")
        List<VisitItem> entityList = (List<VisitItem>) page.getList();
        ExcelView view = new ExcelView(workbook -> {
            Sheet sheet = workbook
                    .createSheet(LanguagesUtils.getMessage(CommonConstants.applicationContext, locale, "page.item"));
            sheet.setDefaultColumnWidth(20);
            int i = 0;
            int j = 0;
            Row row = sheet.createRow(i++);
            row.createCell(j++)
                    .setCellValue(LanguagesUtils.getMessage(CommonConstants.applicationContext, locale, "page.visit.visit_date"));
            row.createCell(j++)
                    .setCellValue(LanguagesUtils.getMessage(CommonConstants.applicationContext, locale, "page.item_type"));
            row.createCell(j++).setCellValue(LanguagesUtils.getMessage(CommonConstants.applicationContext, locale, "page.item"));
            row.createCell(j++).setCellValue(LanguagesUtils.getMessage(CommonConstants.applicationContext, locale, "page.title"));
            row.createCell(j++).setCellValue(LanguagesUtils.getMessage(CommonConstants.applicationContext, locale, "page.url"));
            row.createCell(j++).setCellValue("PV");
            row.createCell(j++).setCellValue("UV");
            row.createCell(j++).setCellValue("IP Views");
            DateFormat dateFormat = DateFormatUtils.getDateFormat(DateFormatUtils.SHORT_DATE_FORMAT_STRING);
            List<Serializable> categoryIds = new ArrayList<>();
            List<Serializable> contentIds = new ArrayList<>();

            for (VisitItem entity : entityList) {
                if (CommonUtils.notEmpty(entity.getId().getItemType()) && CommonUtils.notEmpty(entity.getId().getItemId())) {
                    if ("category".equalsIgnoreCase(entity.getId().getItemType())) {
                        try {
                            categoryIds.add(Integer.parseInt(entity.getId().getItemId()));
                        } catch (NumberFormatException e) {
                        }
                    } else if ("content".equalsIgnoreCase(entity.getId().getItemType())) {
                        try {
                            contentIds.add(Long.parseLong(entity.getId().getItemId()));
                        } catch (NumberFormatException e) {
                        }
                    }
                }
            }
            List<CmsCategory> categoryList = categoryService.getEntitys(categoryIds);
            List<CmsContent> contentList = contentService.getEntitys(contentIds);
            Map<String, CmsCategory> categoryMap = CommonUtils.listToMapSorted(categoryList, k -> k.getId().toString(),
                    entity -> {
                        CmsUrlUtils.initCategoryUrl(site, entity);
                        return entity;
                    }, categoryIds.toArray(new Serializable[categoryIds.size()]), e -> e.getId(),
                    entity -> site.getId() == entity.getSiteId());
            Map<String, CmsContent> contentMap = CommonUtils.listToMapSorted(contentList, k -> k.getId().toString(), entity -> {
                CmsUrlUtils.initContentUrl(site, entity);
                return entity;
            }, contentIds.toArray(new Serializable[contentIds.size()]), e -> e.getId(),
                    entity -> site.getId() == entity.getSiteId());

            for (VisitItem entity : entityList) {
                row = sheet.createRow(i++);
                j = 0;
                row.createCell(j++).setCellValue(
                        null == entity.getId().getVisitDate() ? null : dateFormat.format(entity.getId().getVisitDate()));
                row.createCell(j++).setCellValue(entity.getId().getItemType());
                row.createCell(j++).setCellValue(entity.getId().getItemId());
                if (CommonUtils.notEmpty(entity.getId().getItemType()) && CommonUtils.notEmpty(entity.getId().getItemId())) {
                    if ("category".equalsIgnoreCase(entity.getId().getItemType())
                            && categoryMap.containsKey(entity.getId().getItemId())) {
                        CmsCategory category = categoryMap.get(entity.getId().getItemId());
                        row.createCell(j++).setCellValue(category.getName());
                        row.createCell(j++).setCellValue(category.getUrl());
                    } else if ("content".equalsIgnoreCase(entity.getId().getItemType())
                            && contentMap.containsKey(entity.getId().getItemId())) {
                        CmsContent content = contentMap.get(entity.getId().getItemId());
                        row.createCell(j++).setCellValue(content.getTitle());
                        row.createCell(j++).setCellValue(content.getUrl());
                    } else {
                        row.createCell(j++);
                        row.createCell(j++);
                    }
                } else {
                    row.createCell(j++);
                    row.createCell(j++);
                }
                row.createCell(j++).setCellValue(entity.getPv());
                row.createCell(j++).setCellValue(entity.getUv());
                row.createCell(j++).setCellValue(entity.getIpviews());
            }
        });
        DateFormat dateFormat = DateFormatUtils.getDateFormat(DateFormatUtils.DOWNLOAD_FORMAT_STRING);
        view.setFilename(
                CommonUtils.joinString(LanguagesUtils.getMessage(CommonConstants.applicationContext, locale, "page.item"),
                        dateFormat.format(new Date())));
        return view;
    }

}

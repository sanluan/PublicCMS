package com.publiccms.logic.component.exchange;

import java.io.Serializable;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.stereotype.Component;

import com.publiccms.common.constants.CommonConstants;
import com.publiccms.common.handler.PageHandler;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.common.tools.DateFormatUtils;
import com.publiccms.common.tools.ExtendUtils;
import com.publiccms.common.tools.LanguagesUtils;
import com.publiccms.common.view.ExcelView;
import com.publiccms.entities.cms.CmsPlace;
import com.publiccms.entities.cms.CmsPlaceAttribute;
import com.publiccms.entities.sys.SysExtendField;
import com.publiccms.entities.sys.SysSite;
import com.publiccms.entities.sys.SysUser;
import com.publiccms.logic.component.site.SiteComponent;
import com.publiccms.logic.component.template.MetadataComponent;
import com.publiccms.logic.component.template.TemplateComponent;
import com.publiccms.logic.service.cms.CmsPlaceAttributeService;
import com.publiccms.logic.service.cms.CmsPlaceService;
import com.publiccms.logic.service.sys.SysUserService;
import com.publiccms.views.pojo.entities.CmsPlaceMetadata;

/**
 * PlaceExportComponent 页面片段数据导出组件
 * 
 */
@Component
public class PlaceExportComponent  {
    @Resource
    private CmsPlaceService service;
    @Resource
    private CmsPlaceAttributeService attributeService;
    @Resource
    private SysUserService sysUserService;
    @Resource
    private MetadataComponent metadataComponent;
    @Resource
    private SiteComponent siteComponent;


    /**
     * @param site
     * @param path
     * @param userId
     * @param status
     * @param itemType
     * @param itemId
     * @param startPublishDate
     * @param endPublishDate
     * @param orderField
     * @param orderType
     * @param locale
     * @return
     */
    public ExcelView exportExcelByQuery(SysSite site, String path, Long userId, Integer[] status, String itemType, Long itemId,
            Date startPublishDate, Date endPublishDate, String orderField, String orderType, Locale locale) {
        String filepath = siteComponent.getTemplateFilePath(site.getId(),
                CommonUtils.joinString(TemplateComponent.INCLUDE_DIRECTORY, path));
        CmsPlaceMetadata metadata = metadataComponent.getPlaceMetadata(filepath);

        PageHandler page = service.getPage(site.getId(), userId, path, itemType, itemId, startPublishDate, endPublishDate,
                CommonUtils.getMinuteDate(), status, false, orderField, orderType, 1, PageHandler.MAX_PAGE_SIZE);
        @SuppressWarnings("unchecked")
        List<CmsPlace> entityList = (List<CmsPlace>) page.getList();
        Map<String, List<Serializable>> pksMap = new HashMap<>();
        for (CmsPlace entity : entityList) {
            List<Serializable> userIds = pksMap.computeIfAbsent("userIds", k -> new ArrayList<>());
            List<Serializable> ids = pksMap.computeIfAbsent("ids", k -> new ArrayList<>());
            userIds.add(entity.getUserId());
            userIds.add(entity.getCheckUserId());
            ids.add(entity.getId());
        }
        Map<Serializable, SysUser> userMap = new HashMap<>();
        Map<Serializable, CmsPlaceAttribute> attributeMap = new HashMap<>();
        if (null != pksMap.get("userIds")) {
            List<Serializable> userIds = pksMap.get("userIds");
            List<SysUser> entitys = sysUserService.getEntitys(userIds);
            for (SysUser entity : entitys) {
                userMap.put(entity.getId(), entity);
            }
            List<Serializable> ids = pksMap.get("ids");
            List<CmsPlaceAttribute> attributes = attributeService.getEntitys(ids);
            for (CmsPlaceAttribute attribute : attributes) {
                attributeMap.put(attribute.getPlaceId(), attribute);
            }
        }
        Map<String, String> fieldTextMap = metadata.getFieldTextMap();
        List<String> fieldList = metadata.getFieldList();
        ExcelView view = new ExcelView(workbook -> {
            Sheet sheet = workbook
                    .createSheet(LanguagesUtils.getMessage(CommonConstants.applicationContext, locale, "page.content"));
            sheet.setDefaultColumnWidth(20);
            int i = 0;
            int j = 0;
            Row row = sheet.createRow(i++);
            row.createCell(j++).setCellValue(LanguagesUtils.getMessage(CommonConstants.applicationContext, locale, "page.id"));
            row.createCell(j++)
                    .setCellValue(null == fieldTextMap
                            ? LanguagesUtils.getMessage(CommonConstants.applicationContext, locale, "page.title")
                            : fieldTextMap.get("title"));
            if (null != fieldList && fieldList.contains("url")) {
                row.createCell(j++)
                        .setCellValue(null == fieldTextMap
                                ? LanguagesUtils.getMessage(CommonConstants.applicationContext, locale, "page.url")
                                : fieldTextMap.get("url"));
            }
            if (null != fieldList && fieldList.contains("description")) {
                row.createCell(j++)
                        .setCellValue(null == fieldTextMap
                                ? LanguagesUtils.getMessage(CommonConstants.applicationContext, locale, "page.description")
                                : fieldTextMap.get("description"));
            }
            row.createCell(j++).setCellValue(
                    LanguagesUtils.getMessage(CommonConstants.applicationContext, locale, "page.content.promulgator"));
            row.createCell(j++)
                    .setCellValue(LanguagesUtils.getMessage(CommonConstants.applicationContext, locale, "page.clicks"));
            row.createCell(j++)
                    .setCellValue(LanguagesUtils.getMessage(CommonConstants.applicationContext, locale, "page.publish_date"));
            row.createCell(j++)
                    .setCellValue(LanguagesUtils.getMessage(CommonConstants.applicationContext, locale, "page.create_date"));
            row.createCell(j++)
                    .setCellValue(LanguagesUtils.getMessage(CommonConstants.applicationContext, locale, "page.status"));
            row.createCell(j++)
                    .setCellValue(LanguagesUtils.getMessage(CommonConstants.applicationContext, locale, "page.inspector"));

            if (CommonUtils.notEmpty(metadata.getExtendList())) {
                for (SysExtendField extend : metadata.getExtendList()) {
                    row.createCell(j++).setCellValue(extend.getName());
                }
            }

            SysUser user;
            CmsPlaceAttribute attribute;
            DateFormat dateFormat = DateFormatUtils.getDateFormat(DateFormatUtils.FULL_DATE_FORMAT_STRING);
            for (CmsPlace entity : entityList) {
                row = sheet.createRow(i++);
                j = 0;
                row.createCell(j++).setCellValue(entity.getId().toString());
                row.createCell(j++).setCellValue(entity.getTitle());
                if (null != fieldList && fieldList.contains("url")) {
                    row.createCell(j++).setCellValue(entity.getUrl());
                }
                if (null != fieldList && fieldList.contains("description")) {
                    row.createCell(j++).setCellValue(entity.getDescription());
                }
                user = userMap.get(entity.getUserId());
                row.createCell(j++).setCellValue(null == user ? null : user.getNickname());
                row.createCell(j++).setCellValue(String.valueOf(entity.getClicks()));
                row.createCell(j++).setCellValue(dateFormat.format(entity.getPublishDate()));
                row.createCell(j++).setCellValue(dateFormat.format(entity.getCreateDate()));

                row.createCell(j++).setCellValue(LanguagesUtils.getMessage(CommonConstants.applicationContext, locale,
                        CommonUtils.joinString("page.status.place.data.", entity.getStatus())));

                user = userMap.get(entity.getCheckUserId());
                row.createCell(j++).setCellValue(null == user ? null : user.getNickname());

                if (CommonUtils.notEmpty(metadata.getExtendList())) {
                    attribute = attributeMap.get(entity.getId());
                    Map<String, String> map = ExtendUtils.getExtendMap(null == attribute ? null : attribute.getData());
                    for (SysExtendField extend : metadata.getExtendList()) {
                        row.createCell(j++).setCellValue(map.get(extend.getId().getCode()));
                    }
                }
            }
        });
        DateFormat dateFormat = DateFormatUtils.getDateFormat(DateFormatUtils.DOWNLOAD_FORMAT_STRING);
        view.setFilename(CommonUtils.joinString(metadata.getAlias(), dateFormat.format(new Date())));
        return view;
    }
}

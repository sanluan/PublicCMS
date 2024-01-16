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

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.stereotype.Component;

import com.publiccms.common.constants.CommonConstants;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.common.tools.DateFormatUtils;
import com.publiccms.common.tools.ExtendUtils;
import com.publiccms.common.tools.LanguagesUtils;
import com.publiccms.common.view.ExcelView;
import com.publiccms.entities.cms.CmsCategory;
import com.publiccms.entities.cms.CmsContent;
import com.publiccms.entities.cms.CmsContentAttribute;
import com.publiccms.entities.sys.SysDept;
import com.publiccms.entities.sys.SysExtendField;
import com.publiccms.entities.sys.SysSite;
import com.publiccms.entities.sys.SysUser;
import com.publiccms.logic.component.template.ModelComponent;
import com.publiccms.logic.service.cms.CmsCategoryService;
import com.publiccms.logic.service.cms.CmsContentAttributeService;
import com.publiccms.logic.service.cms.CmsContentService;
import com.publiccms.logic.service.sys.SysDeptService;
import com.publiccms.logic.service.sys.SysExtendFieldService;
import com.publiccms.logic.service.sys.SysUserService;
import com.publiccms.views.pojo.entities.CmsModel;
import com.publiccms.views.pojo.entities.Workload;
import com.publiccms.views.pojo.query.CmsContentQuery;

/**
 * ContentExportComponent 内容数据导出组件
 * 
 */
@Component
public class ContentExportComponent {
    @Resource
    private CmsContentService service;
    @Resource
    private ModelComponent modelComponent;
    @Resource
    private CmsCategoryService categoryService;
    @Resource
    private CmsContentAttributeService attributeService;
    @Resource
    private SysUserService sysUserService;
    @Resource
    private SysDeptService sysDeptService;
    @Resource
    private SysExtendFieldService extendFieldService;

    /**
     * @param site
     * @param status
     * @param startCreateDate
     * @param endCreateDate
     * @param workloadType
     * @param dateField
     * @param locale
     * @return
     */
    public ExcelView exportWorkload(SysSite site, Integer[] status, Date startCreateDate, Date endCreateDate, String workloadType,
            String dateField, Locale locale) {
        List<Workload> entityList = service.getWorkLoadList(site.getId(), status, startCreateDate, endCreateDate, workloadType, dateField);
        Map<String, List<Serializable>> pksMap = new HashMap<>();
        for (Workload entity : entityList) {
            List<Serializable> categoryIds = pksMap.computeIfAbsent("categoryIds", k -> new ArrayList<>());
            categoryIds.add(entity.getCategoryId());
            List<Serializable> deptIds = pksMap.computeIfAbsent("deptIds", k -> new ArrayList<>());
            deptIds.add(entity.getDeptId());
            List<Serializable> userIds = pksMap.computeIfAbsent("userIds", k -> new ArrayList<>());
            userIds.add(entity.getUserId());
        }
        Map<Integer, CmsCategory> categoryMap = new HashMap<>();
        if (null != pksMap.get("categoryIds")) {
            List<Serializable> categoryIds = pksMap.get("categoryIds");
            List<CmsCategory> entitys = categoryService.getEntitys(categoryIds);
            for (CmsCategory entity : entitys) {
                categoryMap.put(entity.getId(), entity);
            }
        }
        Map<Integer, SysDept> deptMap = new HashMap<>();
        if (null != pksMap.get("deptIds")) {
            List<Serializable> deptIds = pksMap.get("deptIds");
            List<SysDept> entitys = sysDeptService.getEntitys(deptIds);
            for (SysDept entity : entitys) {
                deptMap.put(entity.getId(), entity);
            }
        }
        Map<Long, SysUser> userMap = new HashMap<>();
        if (null != pksMap.get("userIds")) {
            List<Serializable> userIds = pksMap.get("userIds");
            List<SysUser> entitys = sysUserService.getEntitys(userIds);
            for (SysUser entity : entitys) {
                userMap.put(entity.getId(), entity);
            }
        }
        ExcelView view = new ExcelView(workbook -> {
            Sheet sheet = workbook
                    .createSheet(LanguagesUtils.getMessage(CommonConstants.applicationContext, locale, "page.workload"));
            int i = 0;
            int j = 0;
            Row row = sheet.createRow(i++);
            row.createCell(j++)
                    .setCellValue(LanguagesUtils.getMessage(CommonConstants.applicationContext, locale, "page.category"));
            row.createCell(j++).setCellValue(LanguagesUtils.getMessage(CommonConstants.applicationContext, locale, "page.dept"));
            row.createCell(j++).setCellValue(LanguagesUtils.getMessage(CommonConstants.applicationContext, locale, "page.user"));
            row.createCell(j++)
                    .setCellValue(LanguagesUtils.getMessage(CommonConstants.applicationContext, locale, "page.content.count"));

            CmsCategory category;
            SysDept dept;
            SysUser user;
            for (Workload entity : entityList) {
                row = sheet.createRow(i++);
                j = 0;
                category = categoryMap.get(entity.getCategoryId());
                row.createCell(j++).setCellValue(null == category ? null : category.getName());
                dept = deptMap.get(entity.getDeptId());
                row.createCell(j++).setCellValue(null == dept ? null : dept.getName());
                user = userMap.get(entity.getUserId());
                row.createCell(j++).setCellValue(null == user ? null : user.getNickname());
                row.createCell(j++).setCellValue(entity.getCount());
            }
        });
        view.setFilename(LanguagesUtils.getMessage(CommonConstants.applicationContext, locale, "page.workload"));
        return view;
    }

    /**
     * @param site
     * @param queryEntity
     * @param orderField
     * @param orderType
     * @param locale
     * @return
     */
    public ExcelView exportExcelByQuery(SysSite site, CmsContentQuery queryEntity, String orderField, String orderType,
            Locale locale) {
        List<CmsContent> entityList = service.getList(queryEntity, true, orderField, orderType);
        Map<String, List<Serializable>> pksMap = new HashMap<>();
        for (CmsContent entity : entityList) {
            List<Serializable> userIds = pksMap.computeIfAbsent("userIds", k -> new ArrayList<>());
            userIds.add(entity.getUserId());
            userIds.add(entity.getCheckUserId());
            List<Serializable> deptIds = pksMap.computeIfAbsent("deptIds", k -> new ArrayList<>());
            deptIds.add(entity.getDeptId());
            List<Serializable> categoryIds = pksMap.computeIfAbsent("categoryIds", k -> new ArrayList<>());
            categoryIds.add(entity.getCategoryId());
            List<Serializable> modelIds = pksMap.computeIfAbsent("modelIds", k -> new ArrayList<>());
            modelIds.add(entity.getModelId());
            List<Serializable> contentIds = pksMap.computeIfAbsent("contentIds", k -> new ArrayList<>());
            contentIds.add(entity.getId());
        }
        Map<Long, SysUser> userMap = new HashMap<>();
        if (null != pksMap.get("userIds")) {
            List<Serializable> userIds = pksMap.get("userIds");
            List<SysUser> entitys = sysUserService.getEntitys(userIds);
            for (SysUser entity : entitys) {
                userMap.put(entity.getId(), entity);
            }
        }
        Map<Integer, SysDept> deptMap = new HashMap<>();
        if (null != pksMap.get("deptIds")) {
            List<Serializable> deptIds = pksMap.get("deptIds");
            List<SysDept> entitys = sysDeptService.getEntitys(deptIds);
            for (SysDept entity : entitys) {
                deptMap.put(entity.getId(), entity);
            }
        }
        Map<Integer, CmsCategory> categoryMap = new HashMap<>();
        if (null != pksMap.get("categoryIds")) {
            List<Serializable> categoryIds = pksMap.get("categoryIds");
            List<CmsCategory> entitys = categoryService.getEntitys(categoryIds);
            for (CmsCategory entity : entitys) {
                categoryMap.put(entity.getId(), entity);
            }
        }
        Map<String, CmsModel> modelMap = modelComponent.getModelMap(site);
        Map<Long, CmsContentAttribute> contentAttributeMap = new HashMap<>();
        if (null != pksMap.get("contentIds")) {
            List<Serializable> contentIds = pksMap.get("contentIds");
            List<CmsContentAttribute> entitys = attributeService.getEntitys(contentIds);
            for (CmsContentAttribute entity : entitys) {
                contentAttributeMap.put(entity.getContentId(), entity);
            }
        }

        ExcelView view = new ExcelView(workbook -> {
            List<SysExtendField> categoryExtendList = null;
            if (CommonUtils.notEmpty(queryEntity.getCategoryId())) {
                CmsCategory category = categoryService.getEntity(queryEntity.getCategoryId());
                if (null != category && null != category.getExtendId()) {
                    categoryExtendList = extendFieldService.getList(category.getExtendId(), null, null);
                }
            }
            List<SysExtendField> modelExtendList = null;
            Map<String, String> fieldTextMap = null;
            List<String> fieldList = null;
            if (CommonUtils.notEmpty(queryEntity.getModelIds()) && 1 == queryEntity.getModelIds().length) {
                CmsModel cmsModel = modelComponent.getModel(site, queryEntity.getModelIds()[0]);
                if (null != cmsModel) {
                    modelExtendList = cmsModel.getExtendList();
                    fieldTextMap = cmsModel.getFieldTextMap();
                    fieldList = cmsModel.getFieldList();
                }
            }

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
            row.createCell(j++)
                    .setCellValue(null == fieldTextMap
                            ? LanguagesUtils.getMessage(CommonConstants.applicationContext, locale, "page.url")
                            : fieldTextMap.get("url"));
            if (null != fieldList && fieldList.contains("author")) {
                row.createCell(j++)
                        .setCellValue(null == fieldTextMap
                                ? LanguagesUtils.getMessage(CommonConstants.applicationContext, locale, "content.author")
                                : fieldTextMap.get("author"));
            }
            if (null != fieldList && fieldList.contains("editor")) {
                row.createCell(j++)
                        .setCellValue(null == fieldTextMap
                                ? LanguagesUtils.getMessage(CommonConstants.applicationContext, locale, "editor")
                                : fieldTextMap.get("editor"));
            }
            if (null != fieldList && fieldList.contains("description")) {
                row.createCell(j++)
                        .setCellValue(null == fieldTextMap
                                ? LanguagesUtils.getMessage(CommonConstants.applicationContext, locale, "page.description")
                                : fieldTextMap.get("description"));
            }
            row.createCell(j++).setCellValue(
                    LanguagesUtils.getMessage(CommonConstants.applicationContext, locale, "page.content.promulgator"));
            row.createCell(j++).setCellValue(LanguagesUtils.getMessage(CommonConstants.applicationContext, locale, "page.dept"));
            row.createCell(j++)
                    .setCellValue(LanguagesUtils.getMessage(CommonConstants.applicationContext, locale, "page.category"));
            row.createCell(j++).setCellValue(LanguagesUtils.getMessage(CommonConstants.applicationContext, locale, "page.model"));
            row.createCell(j++)
                    .setCellValue(LanguagesUtils.getMessage(CommonConstants.applicationContext, locale, "page.word_count"));
            row.createCell(j++)
                    .setCellValue(LanguagesUtils.getMessage(CommonConstants.applicationContext, locale, "page.content.score"));
            row.createCell(j++)
                    .setCellValue(LanguagesUtils.getMessage(CommonConstants.applicationContext, locale, "page.content.comments"));
            row.createCell(j++)
                    .setCellValue(LanguagesUtils.getMessage(CommonConstants.applicationContext, locale, "page.clicks"));
            row.createCell(j++)
                    .setCellValue(LanguagesUtils.getMessage(CommonConstants.applicationContext, locale, "page.publish_date"));
            row.createCell(j++)
                    .setCellValue(LanguagesUtils.getMessage(CommonConstants.applicationContext, locale, "page.expiry_date"));
            row.createCell(j++)
                    .setCellValue(LanguagesUtils.getMessage(CommonConstants.applicationContext, locale, "page.create_date"));
            row.createCell(j++).setCellValue(
                    LanguagesUtils.getMessage(CommonConstants.applicationContext, locale, "page.content.top_level"));
            row.createCell(j++)
                    .setCellValue(LanguagesUtils.getMessage(CommonConstants.applicationContext, locale, "page.status"));
            row.createCell(j++)
                    .setCellValue(LanguagesUtils.getMessage(CommonConstants.applicationContext, locale, "page.inspector"));
            row.createCell(j++)
                    .setCellValue(LanguagesUtils.getMessage(CommonConstants.applicationContext, locale, "page.content.source"));
            row.createCell(j++).setCellValue(
                    LanguagesUtils.getMessage(CommonConstants.applicationContext, locale, "page.content.source_url"));

            if (CommonUtils.notEmpty(categoryExtendList)) {
                for (SysExtendField extend : categoryExtendList) {
                    row.createCell(j++).setCellValue(extend.getName());
                }
            }
            if (CommonUtils.notEmpty(modelExtendList)) {
                for (SysExtendField extend : modelExtendList) {
                    row.createCell(j++).setCellValue(extend.getName());
                }
            }
            if (null != fieldList && fieldList.contains("content")) {
                row.createCell(j++)
                        .setCellValue(null == fieldTextMap
                                ? LanguagesUtils.getMessage(CommonConstants.applicationContext, locale, "page.content.text")
                                : fieldTextMap.get("content"));
            }

            SysUser user;
            SysDept dept;
            CmsCategory category;
            CmsModel cmsModel;
            CmsContentAttribute attribute;
            DateFormat dateFormat = DateFormatUtils.getDateFormat(DateFormatUtils.FULL_DATE_FORMAT_STRING);
            for (CmsContent entity : entityList) {
                row = sheet.createRow(i++);
                j = 0;
                row.createCell(j++).setCellValue(entity.getId().toString());
                row.createCell(j++).setCellValue(entity.getTitle());
                row.createCell(j++).setCellValue(entity.getUrl());
                if (null != fieldList && fieldList.contains("author")) {
                    row.createCell(j++).setCellValue(entity.getAuthor());
                }
                if (null != fieldList && fieldList.contains("editor")) {
                    row.createCell(j++).setCellValue(entity.getEditor());
                }
                if (null != fieldList && fieldList.contains("description")) {
                    row.createCell(j++).setCellValue(entity.getDescription());
                }
                user = userMap.get(entity.getUserId());
                dept = deptMap.get(entity.getDeptId());
                row.createCell(j++).setCellValue(null == user ? null : user.getNickname());
                row.createCell(j++).setCellValue(null == dept ? null : dept.getName());
                category = categoryMap.get(entity.getCategoryId());
                row.createCell(j++).setCellValue(null == category ? null : category.getName());
                cmsModel = modelMap.get(entity.getModelId());
                row.createCell(j++).setCellValue(null == cmsModel ? null : cmsModel.getName());
                attribute = contentAttributeMap.get(entity.getId());
                row.createCell(j++).setCellValue(null == attribute ? null : String.valueOf(attribute.getWordCount()));
                row.createCell(j++).setCellValue(String.valueOf(entity.getScores()));
                row.createCell(j++).setCellValue(String.valueOf(entity.getComments()));
                row.createCell(j++).setCellValue(String.valueOf(entity.getClicks()));
                row.createCell(j++).setCellValue(dateFormat.format(entity.getPublishDate()));
                row.createCell(j++)
                        .setCellValue(null == entity.getExpiryDate() ? null : dateFormat.format(entity.getExpiryDate()));
                row.createCell(j++).setCellValue(dateFormat.format(entity.getCreateDate()));
                row.createCell(j++).setCellValue(String.valueOf(entity.getSort()));
                row.createCell(j++).setCellValue(LanguagesUtils.getMessage(CommonConstants.applicationContext, locale,
                        "page.status.content." + entity.getStatus()));
                user = userMap.get(entity.getCheckUserId());
                row.createCell(j++).setCellValue(null == user ? null : user.getNickname());
                row.createCell(j++).setCellValue(null == attribute ? null : attribute.getSource());
                row.createCell(j++).setCellValue(null == attribute ? null : attribute.getSourceUrl());

                Map<String, String> map = ExtendUtils.getExtendMap(null == attribute ? null : attribute.getData());
                if (CommonUtils.notEmpty(categoryExtendList) && entity.getCategoryId() == queryEntity.getCategoryId()) {
                    for (SysExtendField extend : categoryExtendList) {
                        row.createCell(j++).setCellValue(map.get(extend.getId().getCode()));
                    }
                }
                if (CommonUtils.notEmpty(modelExtendList)) {
                    for (SysExtendField extend : modelExtendList) {
                        row.createCell(j++).setCellValue(map.get(extend.getId().getCode()));
                    }
                }

                if (null != fieldList && fieldList.contains("content")) {
                    row.createCell(j++)
                            .setCellValue(null == attribute ? null : StringUtils.substring(attribute.getText(), 0, 32767));
                    if (null != attribute && null != attribute.getText() && attribute.getText().length() > 32767) {
                        long length = attribute.getText().length();
                        int m = 0;
                        while ((length = length - 32767) > 0) {
                            m++;
                            row.createCell(j++)
                                    .setCellValue(StringUtils.substring(attribute.getText(), m * 32767, (m + 1) * 32767));
                        }
                    }
                }
            }
        });
        DateFormat dateFormat = DateFormatUtils.getDateFormat(DateFormatUtils.DOWNLOAD_FORMAT_STRING);
        view.setFilename(
                CommonUtils.joinString(LanguagesUtils.getMessage(CommonConstants.applicationContext, locale, "page.content"),
                        dateFormat.format(new Date())));
        return view;
    }
}

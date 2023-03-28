package com.publiccms.logic.component.exchange;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.tools.zip.ZipOutputStream;
import org.springframework.stereotype.Component;

import com.publiccms.common.base.AbstractExchange;
import com.publiccms.common.constants.CommonConstants;
import com.publiccms.common.handler.PageHandler;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.common.tools.DateFormatUtils;
import com.publiccms.common.tools.ExtendUtils;
import com.publiccms.common.tools.LanguagesUtils;
import com.publiccms.common.view.ExcelView;
import com.publiccms.entities.cms.CmsCategory;
import com.publiccms.entities.cms.CmsContent;
import com.publiccms.entities.cms.CmsContentAttribute;
import com.publiccms.entities.cms.CmsContentFile;
import com.publiccms.entities.cms.CmsContentProduct;
import com.publiccms.entities.cms.CmsContentRelated;
import com.publiccms.entities.sys.SysDept;
import com.publiccms.entities.sys.SysExtendField;
import com.publiccms.entities.sys.SysSite;
import com.publiccms.entities.sys.SysUser;
import com.publiccms.logic.component.template.ModelComponent;
import com.publiccms.logic.component.template.TemplateComponent;
import com.publiccms.logic.service.cms.CmsCategoryService;
import com.publiccms.logic.service.cms.CmsContentAttributeService;
import com.publiccms.logic.service.cms.CmsContentFileService;
import com.publiccms.logic.service.cms.CmsContentProductService;
import com.publiccms.logic.service.cms.CmsContentRelatedService;
import com.publiccms.logic.service.cms.CmsContentService;
import com.publiccms.logic.service.sys.SysDeptService;
import com.publiccms.logic.service.sys.SysExtendFieldService;
import com.publiccms.logic.service.sys.SysUserService;
import com.publiccms.views.pojo.entities.CmsModel;
import com.publiccms.views.pojo.entities.Workload;
import com.publiccms.views.pojo.exchange.Content;
import com.publiccms.views.pojo.query.CmsContentQuery;

import freemarker.template.TemplateException;
import jakarta.annotation.Resource;

/**
 * ContentExchangeComponent 内容数据导入导出组件
 * 
 */
@Component
public class ContentExchangeComponent extends AbstractExchange<CmsContent, Content> {
    @Resource
    private CmsContentService service;
    @Resource
    private TemplateComponent templateComponent;
    @Resource
    private ModelComponent modelComponent;
    @Resource
    private CmsCategoryService categoryService;
    @Resource
    private CmsContentAttributeService attributeService;
    @Resource
    private CmsContentFileService fileService;
    @Resource
    private CmsContentProductService productService;
    @Resource
    private CmsContentRelatedService relatedService;
    @Resource
    private SysUserService sysUserService;
    @Resource
    private SysDeptService sysDeptService;
    @Resource
    private SysExtendFieldService extendFieldService;

    @Override
    public void exportAll(SysSite site, String directory, ByteArrayOutputStream outputStream, ZipOutputStream zipOutputStream) {
        CmsContentQuery queryEntity = new CmsContentQuery();
        queryEntity.setSiteId(site.getId());
        queryEntity.setDisabled(false);
        queryEntity.setEmptyParent(true);
        exportDataByQuery(site, directory, queryEntity, outputStream, zipOutputStream);
    }

    /**
     * @param site
     * @param directory
     * @param queryEntity
     * @param zipOutputStream
     */
    public void exportDataByQuery(SysSite site, String directory, CmsContentQuery queryEntity, ZipOutputStream zipOutputStream) {
        exportDataByQuery(site, directory, queryEntity, new ByteArrayOutputStream(), zipOutputStream);
    }

    /**
     * @param site
     * @param directory
     * @param queryEntity
     * @param outputStream
     * @param zipOutputStream
     */
    public void exportDataByQuery(SysSite site, String directory, CmsContentQuery queryEntity, ByteArrayOutputStream outputStream,
            ZipOutputStream zipOutputStream) {
        PageHandler page = service.getPage(queryEntity, null, null, null, null, null, PageHandler.MAX_PAGE_SIZE, null);
        int i = 1;
        do {
            @SuppressWarnings("unchecked")
            List<CmsContent> list = (List<CmsContent>) page.getList();
            for (CmsContent entity : list) {
                exportEntity(site, directory, entity, outputStream, zipOutputStream);
            }
            page = service.getPage(queryEntity, null, null, null, null, i++, PageHandler.MAX_PAGE_SIZE, null);
        } while (!page.isLastPage());
    }

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
        PageHandler page = service.getWorkLoadPage(site.getId(), status, startCreateDate, endCreateDate, workloadType, dateField,
                1, PageHandler.MAX_PAGE_SIZE);
        @SuppressWarnings("unchecked")
        List<Workload> entityList = (List<Workload>) page.getList();
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
            int i = 0, j = 0;
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
        PageHandler page = service.getPage(queryEntity, null, orderField, orderType, null, 1, PageHandler.MAX_PAGE_SIZE, null);
        @SuppressWarnings("unchecked")
        List<CmsContent> entityList = (List<CmsContent>) page.getList();
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
            int i = 0, j = 0;
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
        view.setFilename(new StringBuilder(LanguagesUtils.getMessage(CommonConstants.applicationContext, locale, "page.content"))
                .append(dateFormat.format(new Date())).toString());
        return view;
    }

    @Override
    public void exportEntity(SysSite site, String directory, CmsContent entity, ByteArrayOutputStream out,
            ZipOutputStream zipOutputStream) {
        CmsCategory category = categoryService.getEntity(entity.getCategoryId());
        if (null != category) {
            Content data = exportEntity(site, category.getCode(), entity);
            CmsModel model = modelComponent.getModel(site, entity.getModelId());
            if (null != model && model.isHasChild()) {
                List<CmsContent> list = service.getListByTopId(site.getId(), entity.getId());
                if (null != list) {
                    List<Content> childList = new ArrayList<>();
                    for (CmsContent content : list) {
                        childList.add(exportEntity(site, category.getCode(), content));
                    }
                    data.setChildList(childList);
                }
            }
            export(directory, out, zipOutputStream, data, entity.getId() + ".json");
        }
    }

    @Override
    public void save(SysSite site, long userId, boolean overwrite, Content data) {
        CmsCategory category = categoryService.getEntityByCode(site.getId(), data.getCategoryCode());
        SysUser user = sysUserService.getEntity(userId);
        if (null != category) {
            save(site, userId, overwrite, category, user, data);
        }
    }

    public void save(SysSite site, long userId, boolean overwrite, CmsCategory category, SysUser user, Content data) {
        CmsContent entity = data.getEntity();
        CmsContent oldentity = service.getEntity(entity.getId());
        if (null != category
                && (null == oldentity || oldentity.isDisabled() || oldentity.getSiteId() != site.getId() || overwrite)) {
            if (null != oldentity && oldentity.getSiteId() != site.getId()) {
                entity.setId(null);
            }
            entity.setSiteId(site.getId());
            entity.setUserId(userId);
            entity.setDeptId(null != user ? user.getDeptId() : null);
            entity.setCategoryId(category.getId());
            service.saveOrUpdate(entity);
            if (null != data.getAttribute()) {
                data.getAttribute().setContentId(entity.getId());
                if (CommonUtils.notEmpty(data.getAttribute().getText())) {
                    data.getAttribute()
                            .setText(StringUtils.replace(data.getAttribute().getText(), "#DYNAMICPATH#", site.getDynamicPath()));
                }
                if (CommonUtils.notEmpty(data.getAttribute().getText())) {
                    data.getAttribute()
                            .setText(StringUtils.replace(data.getAttribute().getText(), "#SITEPATH#", site.getSitePath()));
                }
                if (CommonUtils.notEmpty(data.getAttribute().getData())) {
                    data.getAttribute()
                            .setData(StringUtils.replace(data.getAttribute().getData(), "#DYNAMICPATH#", site.getDynamicPath()));
                }
                if (CommonUtils.notEmpty(data.getAttribute().getData())) {
                    data.getAttribute()
                            .setData(StringUtils.replace(data.getAttribute().getData(), "#SITEPATH#", site.getSitePath()));
                }
                attributeService.saveOrUpdate(data.getAttribute());
            }
            if (null != data.getChildList()) {
                for (Content child : data.getChildList()) {
                    child.getEntity().setParentId(entity.getId());
                    save(site, userId, overwrite, category, user, child);
                }
            }
            try {
                templateComponent.createContentFile(site, entity, category, null);
            } catch (IOException | TemplateException e) {
            }
        }
    }

    private Content exportEntity(SysSite site, String categoryCode, CmsContent entity) {
        Content data = new Content();
        data.setCategoryCode(categoryCode);
        data.setEntity(entity);
        data.setAttribute(attributeService.getEntity(entity.getId()));
        if (null != data.getAttribute()) {
            if (CommonUtils.notEmpty(data.getAttribute().getText())) {
                data.getAttribute().setText(StringUtils.replace(data.getAttribute().getText(), site.getSitePath(), "#SITEPATH#"));
            }
            if (CommonUtils.notEmpty(data.getAttribute().getText())) {
                data.getAttribute()
                        .setText(StringUtils.replace(data.getAttribute().getText(), site.getDynamicPath(), "#DYNAMICPATH#"));
            }
            if (CommonUtils.notEmpty(data.getAttribute().getData())) {
                data.getAttribute().setData(StringUtils.replace(data.getAttribute().getData(), site.getSitePath(), "#SITEPATH#"));
            }
            if (CommonUtils.notEmpty(data.getAttribute().getData())) {
                data.getAttribute()
                        .setData(StringUtils.replace(data.getAttribute().getData(), site.getDynamicPath(), "#DYNAMICPATH#"));
            }
        }
        if (entity.isHasFiles() || entity.isHasImages()) {
            @SuppressWarnings("unchecked")
            List<CmsContentFile> fileList = (List<CmsContentFile>) fileService
                    .getPage(entity.getId(), null, null, null, null, null, null).getList();
            data.setFileList(fileList);
        }
        if (entity.isHasProducts()) {
            List<CmsContentProduct> productList = productService.getList(site.getId(), entity.getId());
            data.setProductList(productList);
        }
        @SuppressWarnings("unchecked")
        List<CmsContentRelated> relatedList = (List<CmsContentRelated>) relatedService
                .getPage(entity.getId(), null, null, null, null, null, null, null).getList();
        data.setRelatedList(relatedList);
        return data;
    }

    @Override
    public String getDirectory() {
        return "content";
    }
}

package com.publiccms.logic.component.template;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.publiccms.common.api.Cache;
import com.publiccms.common.base.AbstractFreemarkerView;
import com.publiccms.common.constants.CommonConstants;
import com.publiccms.common.handler.PageHandler;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.common.tools.ExtendUtils;
import com.publiccms.common.tools.FreeMarkerUtils;
import com.publiccms.entities.cms.CmsCategory;
import com.publiccms.entities.cms.CmsCategoryAttribute;
import com.publiccms.entities.cms.CmsCategoryModel;
import com.publiccms.entities.cms.CmsCategoryModelId;
import com.publiccms.entities.cms.CmsContent;
import com.publiccms.entities.cms.CmsContentAttribute;
import com.publiccms.entities.cms.CmsPlace;
import com.publiccms.entities.sys.SysSite;
import com.publiccms.logic.component.site.SiteComponent;
import com.publiccms.logic.component.site.StatisticsComponent;
import com.publiccms.logic.service.cms.CmsCategoryAttributeService;
import com.publiccms.logic.service.cms.CmsCategoryModelService;
import com.publiccms.logic.service.cms.CmsCategoryService;
import com.publiccms.logic.service.cms.CmsContentAttributeService;
import com.publiccms.logic.service.cms.CmsContentService;
import com.publiccms.logic.service.cms.CmsPlaceService;
import com.publiccms.views.pojo.entities.CmsPageData;
import com.publiccms.views.pojo.entities.CmsPageMetadata;
import com.publiccms.views.pojo.entities.CmsPlaceMetadata;

import freemarker.template.Configuration;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModelException;

/**
 * 模板处理组件 Template Component
 *
 */
@Component
public class TemplateComponent implements Cache {
    protected final Log log = LogFactory.getLog(getClass());
    /**
     * 包含目录 include directory
     */
    public static String INCLUDE_DIRECTORY = "include";
    /**
     * 管理后台上下文路径 Context Management Context Path Context
     */
    public static final String CONTEXT_ADMIN_CONTEXT_PATH = "adminContextPath";

    private Configuration adminConfiguration;
    private Configuration webConfiguration;
    private Configuration taskConfiguration;

    @Autowired
    private CmsContentAttributeService contentAttributeService;
    @Autowired
    private CmsCategoryAttributeService categoryAttributeService;
    @Autowired
    private CmsContentService contentService;
    @Autowired
    private CmsCategoryModelService categoryModelService;
    @Autowired
    private CmsCategoryService categoryService;
    @Autowired
    private SiteComponent siteComponent;
    @Autowired
    private MetadataComponent metadataComponent;
    @Autowired
    private CmsPlaceService placeService;
    @Autowired
    private StatisticsComponent statisticsComponent;

    /**
     * 创建静态化页面
     * 
     * @param site
     * @param fullTemplatePath
     * @param filePath
     * @param pageIndex
     * @param metadataMap
     * @param model
     * @return static file path
     * @throws IOException
     * @throws TemplateException
     */
    public String createStaticFile(SysSite site, String fullTemplatePath, String filePath, Integer pageIndex,
            Map<String, Object> metadataMap, Map<String, Object> model) throws IOException, TemplateException {
        if (CommonUtils.notEmpty(filePath)) {
            if (null == model) {
                model = new HashMap<>();
            }
            model.put("metadata", metadataMap);
            model.put("pageIndex", pageIndex);
            AbstractFreemarkerView.exposeSite(model, site);
            filePath = FreeMarkerUtils.generateStringByString(filePath, webConfiguration, model);
            if (filePath.startsWith(CommonConstants.SEPARATOR)) {
                filePath = filePath.substring(1);
            }
            model.put("url", site.getSitePath() + filePath);
            String staticFilePath;
            if (filePath.endsWith(CommonConstants.SEPARATOR)) {
                staticFilePath = filePath + CommonConstants.getDefaultPage();
            } else {
                staticFilePath = filePath;
            }
            if (CommonUtils.notEmpty(pageIndex) && 1 < pageIndex) {
                int index = staticFilePath.lastIndexOf(CommonConstants.DOT);
                staticFilePath = staticFilePath.substring(0, index) + CommonConstants.UNDERLINE + pageIndex
                        + staticFilePath.substring(index, staticFilePath.length());
            }
            FreeMarkerUtils.generateFileByFile(fullTemplatePath, siteComponent.getWebFilePath(site, staticFilePath),
                    webConfiguration, model);
        }
        return filePath;
    }

    /**
     * 内容页面静态化
     * 
     * @param site
     * @param entity
     * @param category
     * @param categoryModel
     * @return whether the create is successful
     */
    public boolean createContentFile(SysSite site, CmsContent entity, CmsCategory category, CmsCategoryModel categoryModel) {
        if (null != site && null != entity) {
            if (!entity.isOnlyUrl()) {
                if (null == category) {
                    category = categoryService.getEntity(entity.getCategoryId());
                }
                if (null == categoryModel) {
                    categoryModel = categoryModelService
                            .getEntity(new CmsCategoryModelId(entity.getCategoryId(), entity.getModelId()));
                }
                if (null != categoryModel && null != category) {
                    try {
                        if (site.isUseStatic() && CommonUtils.notEmpty(categoryModel.getTemplatePath())) {
                            String filePath = createContentFile(site, entity, category, true, categoryModel.getTemplatePath(),
                                    null, null);
                            contentService.updateUrl(entity.getId(), filePath, true);
                        } else {
                            Map<String, Object> model = new HashMap<>();
                            model.put("content", entity);
                            model.put("category", category);
                            model.put(AbstractFreemarkerView.CONTEXT_SITE, site);
                            String filePath = FreeMarkerUtils.generateStringByString(category.getContentPath(), webConfiguration,
                                    model);
                            contentService.updateUrl(entity.getId(), filePath, false);
                        }
                        return true;
                    } catch (IOException | TemplateException e) {
                        log.error(e.getMessage(), e);
                        return false;
                    }
                }
            } else if (null != entity.getQuoteContentId()) {
                if (null != categoryModel && null != category) {
                    CmsContent quote = contentService.getEntity(entity.getQuoteContentId());
                    if (null != quote) {
                        contentService.updateUrl(entity.getId(), quote.getUrl(), false);
                    }
                }
            }
        }
        return false;
    }

    /**
     * @param site
     * @param entity
     */
    public void initPlaceCover(SysSite site, CmsPlace entity) {
        entity.setCover(getUrl(site, true, entity.getCover()));
    }

    /**
     * @param site
     * @param entity
     */
    public void initContentUrl(SysSite site, CmsContent entity) {
        if (!entity.isOnlyUrl()) {
            entity.setUrl(getUrl(site, entity.isHasStatic(), entity.getUrl()));
        }
    }

    /**
     * @param site
     * @param entity
     */
    public void initContentCover(SysSite site, CmsContent entity) {
        entity.setCover(getUrl(site, true, entity.getCover()));
    }

    /**
     * @param site
     * @param hasStatic
     * @param url
     * @return
     */
    public String getUrl(SysSite site, boolean hasStatic, String url) {
        if (CommonUtils.empty(url) || url.contains("://") || url.startsWith("/")) {
            return url;
        } else {
            return hasStatic ? site.getSitePath() + url : site.getDynamicPath() + url;
        }
    }

    /**
     * @param site
     * @param entity
     */
    public void initCategoryUrl(SysSite site, CmsCategory entity) {
        if (!entity.isOnlyUrl()) {
            entity.setUrl(getUrl(site, entity.isHasStatic(), entity.getUrl()));
        }
    }

    /**
     * 内容页面静态化
     * 
     * @param site
     * @param entity
     * @param category
     * @param createMultiContentPage
     * @param templatePath
     * @param filePath
     * @param pageIndex
     * @return content static file path
     * @throws IOException
     * @throws TemplateException
     */
    public String createContentFile(SysSite site, CmsContent entity, CmsCategory category, boolean createMultiContentPage,
            String templatePath, String filePath, Integer pageIndex) throws IOException, TemplateException {
        Map<String, Object> model = new HashMap<>();

        initContentUrl(site, entity);
        initContentCover(site, entity);
        initCategoryUrl(site, category);

        model.put("content", entity);
        model.put("category", category);

        CmsContentAttribute attribute = contentAttributeService.getEntity(entity.getId());
        if (null != attribute) {
            Map<String, String> map = ExtendUtils.getExtendMap(attribute.getData());
            map.put("text", attribute.getText());
            map.put("source", attribute.getSource());
            map.put("sourceUrl", attribute.getSourceUrl());
            map.put("wordCount", String.valueOf(attribute.getWordCount()));
            model.put("attribute", map);
        } else {
            model.put("attribute", attribute);
        }
        if (CommonUtils.empty(filePath)) {
            filePath = category.getContentPath();
        }
        String realTemplatePath = siteComponent.getWebTemplateFilePath(site, templatePath);
        CmsPageMetadata metadata = metadataComponent.getTemplateMetadata(realTemplatePath);
        CmsPageData data = metadataComponent.getTemplateData(siteComponent.getCurrentSiteWebTemplateFilePath(site, templatePath));
        Map<String, Object> metadataMap = metadata.getAsMap(data);
        String fullTemplatePath = SiteComponent.getFullTemplatePath(site, templatePath);
        if (null != attribute && CommonUtils.notEmpty(filePath) && CommonUtils.notEmpty(attribute.getText())) {
            String pageBreakTag = null;
            if (-1 < attribute.getText().indexOf(CommonConstants.getCkeditorPageBreakTag())) {
                pageBreakTag = CommonConstants.getCkeditorPageBreakTag();
            } else if (-1 < attribute.getText().indexOf(CommonConstants.getKindEditorPageBreakTag())) {
                pageBreakTag = CommonConstants.getKindEditorPageBreakTag();
            } else {
                pageBreakTag = CommonConstants.getUeditorPageBreakTag();
            }
            String[] texts = StringUtils.splitByWholeSeparator(attribute.getText(), pageBreakTag);
            if (createMultiContentPage) {
                for (int i = 1; i < texts.length; i++) {
                    PageHandler page = new PageHandler(i + 1, 1, texts.length, null);
                    model.put("text", texts[i]);
                    model.put("page", page);
                    createStaticFile(site, fullTemplatePath, filePath, i + 1, metadataMap, model);
                }
                pageIndex = 1;
            }
            PageHandler page = new PageHandler(pageIndex, 1, texts.length, null);
            model.put("page", page);
            model.put("text", texts[page.getPageIndex() - 1]);
        }
        return createStaticFile(site, fullTemplatePath, filePath, pageIndex, metadataMap, model);
    }

    /**
     * 分类页面静态化
     * 
     * @param site
     * @param entity
     * @param pageIndex
     * @param totalPage
     * @return whether the create is successful
     */
    public boolean createCategoryFile(SysSite site, CmsCategory entity, Integer pageIndex, Integer totalPage) {
        if (entity.isOnlyUrl()) {
            categoryService.updateUrl(entity.getId(), entity.getPath(), false);
        } else if (CommonUtils.notEmpty(entity.getPath())) {
            try {
                if (site.isUseStatic() && CommonUtils.notEmpty(entity.getTemplatePath())) {
                    String filePath = createCategoryFile(site, entity, entity.getTemplatePath(), entity.getPath(), pageIndex,
                            totalPage);
                    categoryService.updateUrl(entity.getId(), filePath, true);
                } else {
                    Map<String, Object> model = new HashMap<>();
                    initCategoryUrl(site, entity);
                    model.put("category", entity);
                    model.put(AbstractFreemarkerView.CONTEXT_SITE, site);
                    String filePath = FreeMarkerUtils.generateStringByString(entity.getPath(), webConfiguration, model);
                    categoryService.updateUrl(entity.getId(), filePath, false);
                }
            } catch (IOException | TemplateException e) {
                log.error(e.getMessage(), e);
                return false;
            }
            return true;
        }
        return false;

    }

    /**
     * 分类页面静态化
     * 
     * @param site
     * @param entity
     * @param templatePath
     * @param filePath
     * @param pageIndex
     * @param totalPage
     * @return category static file path
     * @throws IOException
     * @throws TemplateException
     */
    public String createCategoryFile(SysSite site, CmsCategory entity, String templatePath, String filePath, Integer pageIndex,
            Integer totalPage) throws IOException, TemplateException {
        Map<String, Object> model = new HashMap<>();
        if (CommonUtils.empty(pageIndex)) {
            pageIndex = 1;
        }
        model.put("category", entity);
        CmsCategoryAttribute attribute = categoryAttributeService.getEntity(entity.getId());
        if (null != attribute) {
            Map<String, String> map = ExtendUtils.getExtendMap(attribute.getData());
            map.put("title", attribute.getTitle());
            map.put("keywords", attribute.getKeywords());
            map.put("description", attribute.getDescription());
            model.put("attribute", map);
        } else {
            model.put("attribute", attribute);
        }
        String realTemplatePath = siteComponent.getWebTemplateFilePath(site, templatePath);
        CmsPageMetadata metadata = metadataComponent.getTemplateMetadata(realTemplatePath);
        CmsPageData data = metadataComponent.getTemplateData(siteComponent.getCurrentSiteWebTemplateFilePath(site, templatePath));
        Map<String, Object> metadataMap = metadata.getAsMap(data);
        String fullTemplatePath = SiteComponent.getFullTemplatePath(site, templatePath);
        if (CommonUtils.notEmpty(totalPage) && pageIndex + 1 <= totalPage) {
            for (int i = pageIndex + 1; i <= totalPage; i++) {
                createStaticFile(site, fullTemplatePath, filePath, i, metadataMap, model);
            }
        }

        return createStaticFile(site, fullTemplatePath, filePath, pageIndex, metadataMap, model);
    }

    private void exposePlace(SysSite site, String templatePath, CmsPlaceMetadata metadata, Map<String, Object> model) {
        if (null != metadata.getSize() && 0 < metadata.getSize()) {
            Date now = CommonUtils.getMinuteDate();
            PageHandler page = placeService.getPage(site.getId(), null, templatePath, null, null, null, now, now,
                    CmsPlaceService.STATUS_NORMAL_ARRAY, false, null, null, 1, metadata.getSize());
            @SuppressWarnings("unchecked")
            List<CmsPlace> list = (List<CmsPlace>) page.getList();
            if (null != list) {
                list.forEach(e -> {
                    Integer clicks = statisticsComponent.getPlaceClicks(e.getId());
                    if (null != clicks) {
                        e.setClicks(e.getClicks() + clicks);
                    }
                    initPlaceCover(site, e);
                });
            }
            model.put("page", page);
        }
        model.put("metadata", metadata);
        AbstractFreemarkerView.exposeSite(model, site);
    }

    /**
     * 静态化页面片段
     * 
     * @param site
     * @param templatePath
     * @param metadata
     * @throws IOException
     * @throws TemplateException
     */
    public void staticPlace(SysSite site, String templatePath, CmsPlaceMetadata metadata) throws IOException, TemplateException {
        if (CommonUtils.notEmpty(templatePath)) {
            Map<String, Object> model = new HashMap<>();
            exposePlace(site, templatePath, metadata, model);
            String placeTemplatePath = INCLUDE_DIRECTORY + templatePath;
            String templateFullPath = SiteComponent.getFullTemplatePath(site, placeTemplatePath);
            FreeMarkerUtils.generateFileByFile(templateFullPath, siteComponent.getWebFilePath(site, placeTemplatePath),
                    webConfiguration, model);
        }
    }

    /**
     * 输出页面片段
     * 
     * @param site
     * @param templatePath
     * @param metadata
     * @return place content
     * @throws IOException
     * @throws TemplateException
     */
    public String printPlace(SysSite site, String templatePath, CmsPlaceMetadata metadata) throws IOException, TemplateException {
        StringWriter writer = new StringWriter();
        printPlace(writer, site, templatePath, metadata);
        return writer.toString();
    }

    /**
     * 输出页面片段
     * 
     * @param writer
     * @param site
     * @param templatePath
     * @param metadata
     * @throws IOException
     * @throws TemplateException
     */
    public void printPlace(Writer writer, SysSite site, String templatePath, CmsPlaceMetadata metadata)
            throws IOException, TemplateException {
        if (CommonUtils.notEmpty(templatePath)) {
            Map<String, Object> model = new HashMap<>();
            exposePlace(site, templatePath, metadata, model);
            String templateFullPath = SiteComponent.getFullTemplatePath(site, INCLUDE_DIRECTORY + templatePath);
            FreeMarkerUtils.generateStringByFile(writer, templateFullPath, webConfiguration, model);
        }
    }

    public void setAdminContextPath(String adminContextPath) {
        try {
            adminConfiguration.setSharedVariable(CONTEXT_ADMIN_CONTEXT_PATH, adminContextPath);
        } catch (TemplateModelException e) {
        }
    }

    @Override
    public void clear() {
        adminConfiguration.clearTemplateCache();
        clearTemplateCache();
        clearTaskTemplateCache();
    }

    /**
     * 清理模板缓存
     * 
     * Clear Template Cache
     */
    public void clearTemplateCache() {
        webConfiguration.clearTemplateCache();
    }

    /**
     * 清理任务计划模板缓存
     * 
     * Clear Template Cache
     */
    public void clearTaskTemplateCache() {
        taskConfiguration.clearTemplateCache();
    }

    /**
     * @param adminConfiguration
     *            the adminConfiguration to set
     */
    public void setAdminConfiguration(Configuration adminConfiguration) {
        this.adminConfiguration = adminConfiguration;
    }

    /**
     * @param webConfiguration
     *            the webConfiguration to set
     */
    public void setWebConfiguration(Configuration webConfiguration) {
        this.webConfiguration = webConfiguration;
    }

    /**
     * @param taskConfiguration
     *            the taskConfiguration to set
     */
    public void setTaskConfiguration(Configuration taskConfiguration) {
        this.taskConfiguration = taskConfiguration;
    }

    /**
     * 获取FreeMarker管理后台配置
     * 
     * @return FreeMarker admin config
     */
    public Configuration getAdminConfiguration() {
        return adminConfiguration;
    }

    /**
     * 获取FreeMarker前台配置
     * 
     * @return FreeMarker web config
     */
    public Configuration getWebConfiguration() {
        return webConfiguration;
    }

    /**
     * 获取FreeMarker任务计划配置
     * 
     * @return FreeMarker task config
     */
    public Configuration getTaskConfiguration() {
        return taskConfiguration;
    }

}

package com.publiccms.logic.component.template;

import java.io.IOException;
import java.io.Serializable;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.function.Consumer;

import javax.annotation.PreDestroy;
import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;

import com.publiccms.common.api.Cache;
import com.publiccms.common.api.Config;
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
import com.publiccms.logic.component.BeanComponent;
import com.publiccms.logic.component.config.SiteConfigComponent;
import com.publiccms.logic.component.site.SiteComponent;
import com.publiccms.logic.component.site.StatisticsComponent;
import com.publiccms.logic.service.cms.CmsCategoryAttributeService;
import com.publiccms.logic.service.cms.CmsCategoryModelService;
import com.publiccms.logic.service.cms.CmsCategoryService;
import com.publiccms.logic.service.cms.CmsContentAttributeService;
import com.publiccms.logic.service.cms.CmsContentService;
import com.publiccms.logic.service.cms.CmsPlaceService;
import com.publiccms.views.pojo.entities.CmsCategoryType;
import com.publiccms.views.pojo.entities.CmsModel;
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
    public static final String INCLUDE_DIRECTORY = "include";
    /**
     * 管理后台上下文路径 Context Management Context Path Context
     */
    public static final String CONTEXT_ADMIN_CONTEXT_PATH = "adminContextPath";

    private Configuration adminConfiguration;
    private Configuration webConfiguration;
    private Configuration taskConfiguration;

    @Resource
    private CmsContentAttributeService contentAttributeService;
    @Resource
    private CmsCategoryAttributeService categoryAttributeService;
    @Resource
    private CmsContentService contentService;
    @Resource
    private CmsCategoryModelService categoryModelService;
    @Resource
    private CmsCategoryService categoryService;
    @Resource
    private SiteComponent siteComponent;
    @Resource
    private MetadataComponent metadataComponent;
    @Resource
    private ModelComponent modelComponent;
    @Resource
    private CmsPlaceService placeService;
    @Resource
    private StatisticsComponent statisticsComponent;

    private static ExecutorService pool = Executors.newFixedThreadPool(2 * Runtime.getRuntime().availableProcessors());

    /**
     * 分类页面静态化
     *
     * @param site
     * @param entity
     * @param templatePath
     * @param filepath
     * @param pageIndex
     * @param totalPage
     * @return category static file path
     * @throws IOException
     * @throws TemplateException
     */
    public String createCategoryFile(SysSite site, CmsCategory entity, String templatePath, String filepath, Integer pageIndex,
            Integer totalPage) throws IOException, TemplateException {
        Map<String, Object> model = new HashMap<>();
        if (CommonUtils.empty(pageIndex)) {
            pageIndex = 1;
        }
        initCategoryUrl(site, entity);
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
        String realTemplatePath = siteComponent.getTemplateFilePath(site.getId(), templatePath);
        CmsPageMetadata metadata = metadataComponent.getTemplateMetadata(realTemplatePath);
        CmsPageData data = metadataComponent.getTemplateData(realTemplatePath);
        Map<String, Object> metadataMap = metadata.getAsMap(data);
        String fullTemplatePath = SiteComponent.getFullTemplatePath(site.getId(), templatePath);
        if (CommonUtils.notEmpty(totalPage) && pageIndex + 1 <= totalPage) {
            for (int i = pageIndex + 1; i <= totalPage; i++) {
                createStaticFile(site, fullTemplatePath, filepath, i, metadataMap, model, url -> {
                    if (null == entity.getUrl()) {
                        entity.setUrl(url);
                    }
                });
            }
        }

        return createStaticFile(site, fullTemplatePath, filepath, pageIndex, metadataMap, model, url -> {
            if (null == entity.getUrl()) {
                entity.setUrl(url);
            }
        });
    }

    /**
     * 内容页面静态化
     *
     * @param site
     * @param entity
     * @param category
     * @param createMultiContentPage
     * @param templatePath
     * @param filepath
     * @param pageIndex
     * @return content static file path
     * @throws IOException
     * @throws TemplateException
     */
    public String createContentFile(SysSite site, CmsContent entity, CmsCategory category, boolean createMultiContentPage,
            String templatePath, String filepath, Integer pageIndex) throws IOException, TemplateException {
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
            map.put("minPrice", String.valueOf(attribute.getMinPrice()));
            map.put("maxPrice", String.valueOf(attribute.getMaxPrice()));
            model.put("attribute", map);
        } else {
            model.put("attribute", attribute);
        }
        String realTemplatePath = siteComponent.getTemplateFilePath(site.getId(), templatePath);
        CmsPageMetadata metadata = metadataComponent.getTemplateMetadata(realTemplatePath);
        CmsPageData data = metadataComponent.getTemplateData(realTemplatePath);
        Map<String, Object> metadataMap = metadata.getAsMap(data);
        String fullTemplatePath = SiteComponent.getFullTemplatePath(site.getId(), templatePath);
        if (null != attribute && CommonUtils.notEmpty(filepath) && CommonUtils.notEmpty(attribute.getText())) {
            String pageBreakTag = null;
            if (-1 < attribute.getText().indexOf(CommonConstants.getCkeditorPageBreakTag())) {
                pageBreakTag = CommonConstants.getCkeditorPageBreakTag();
            } else if (-1 < attribute.getText().indexOf(CommonConstants.getTinyMCEPageBreakTag())) {
                pageBreakTag = CommonConstants.getTinyMCEPageBreakTag();
            } else if (-1 < attribute.getText().indexOf(CommonConstants.getKindEditorPageBreakTag())) {
                pageBreakTag = CommonConstants.getKindEditorPageBreakTag();
            } else {
                pageBreakTag = CommonConstants.getUeditorPageBreakTag();
            }
            String[] texts = StringUtils.splitByWholeSeparator(attribute.getText(), pageBreakTag);
            if (createMultiContentPage) {
                for (int i = 1; i < texts.length; i++) {
                    PageHandler page = new PageHandler(i + 1, 1);
                    page.setTotalCount(texts.length);
                    model.put("text", texts[i]);
                    model.put("page", page);
                    createStaticFile(site, fullTemplatePath, filepath, i + 1, metadataMap, model, url -> {
                        if (null == entity.getUrl()) {
                            entity.setUrl(url);
                        }
                    });
                }
                pageIndex = 1;
            }
            PageHandler page = new PageHandler(pageIndex, 1);
            page.setTotalCount(texts.length);
            model.put("page", page);
            model.put("text", texts[page.getPageIndex() - 1]);
        }
        return createStaticFile(site, fullTemplatePath, filepath, pageIndex, metadataMap, model, url -> {
            if (null == entity.getUrl()) {
                entity.setUrl(url);
            }
        });
    }

    /**
     * 内容页面静态化
     *
     * @param site
     * @param entity
     * @param category
     * @param categoryModel
     * @return whether the create is successful
     * @throws TemplateException
     * @throws IOException
     */
    public boolean createContentFile(SysSite site, CmsContent entity, CmsCategory category, CmsCategoryModel categoryModel)
            throws IOException, TemplateException {
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
                    String contentPath = null;
                    String templatePath = null;
                    if (categoryModel.isCustomContentPath()) {
                        contentPath = categoryModel.getContentPath();
                        templatePath = categoryModel.getTemplatePath();
                    } else {
                        if (category.isCustomContentPath()) {
                            contentPath = category.getContentPath();
                        } else if (CommonUtils.notEmpty(category.getTypeId())) {
                            CmsCategoryType categoryType = modelComponent.getCategoryTypeMap(site.getId())
                                    .get(category.getTypeId());
                            if (null != categoryType) {
                                contentPath = categoryType.getContentPath();
                            }
                        }
                        CmsModel model = modelComponent.getModelMap(site.getId()).get(entity.getModelId());
                        if (null != model) {
                            templatePath = model.getTemplatePath();
                            if (null == contentPath) {
                                contentPath = model.getContentPath();
                            }
                        }
                    }
                    if (site.isUseStatic() && CommonUtils.notEmpty(templatePath) && CommonUtils.notEmpty(contentPath)) {
                        String oldUrl = entity.getUrl();
                        String filepath = createContentFile(site, entity, category, true, templatePath, contentPath, null);
                        if (!entity.isHasStatic() || null == oldUrl || !oldUrl.equals(filepath)) {
                            contentService.updateUrl(entity.getId(), filepath, true);
                        }
                    } else if (CommonUtils.notEmpty(contentPath)) {
                        Map<String, Object> model = new HashMap<>();
                        model.put("content", entity);
                        model.put("category", category);
                        model.put(CommonConstants.getAttributeSite(), site);
                        String filepath = FreeMarkerUtils.generateStringByString(contentPath, webConfiguration, model);
                        if (entity.isHasStatic() || null == entity.getUrl() || !entity.getUrl().equals(filepath)) {
                            contentService.updateUrl(entity.getId(), filepath, false);
                        }
                    } else if (entity.isHasStatic() || CommonUtils.notEmpty(entity.getUrl())) {
                        contentService.updateUrl(entity.getId(), null, false);
                    }
                    return true;
                }
            } else if (null == entity.getParentId() && null != entity.getQuoteContentId()) {
                CmsContent quote = contentService.getEntity(entity.getQuoteContentId());
                if (null != quote) {
                    contentService.updateUrl(entity.getId(), quote.getUrl(), false);
                }
            }
        }
        return false;

    }

    /**
     * 分类页面静态化
     *
     * @param site
     * @param entity
     * @param pageIndex
     * @param totalPage
     * @return whether the create is successful
     * @throws IOException
     * @throws TemplateException
     */
    public boolean createCategoryFile(SysSite site, CmsCategory entity, Integer pageIndex, Integer totalPage)
            throws IOException, TemplateException {
        if (entity.isOnlyUrl()) {
            categoryService.updateUrl(entity.getId(), entity.getPath(), false);
        } else {
            Map<String, String> config = BeanComponent.getConfigComponent().getConfigData(site.getId(), Config.CONFIG_CODE_SITE);
            String categoryPath;
            String templatePath = null;
            if (entity.isCustomPath()) {
                categoryPath = entity.getPath();
                templatePath = entity.getTemplatePath();
            } else if (CommonUtils.notEmpty(entity.getTypeId())) {
                CmsCategoryType categoryType = modelComponent.getCategoryTypeMap(site.getId()).get(entity.getTypeId());
                if (null != categoryType) {
                    categoryPath = categoryType.getPath();
                    templatePath = categoryType.getTemplatePath();
                } else {
                    categoryPath = config.get(SiteConfigComponent.CONFIG_CATEGORY_PATH);
                    templatePath = config.get(SiteConfigComponent.CONFIG_CATEGORY_TEMPLATE_PATH);
                }
            } else {
                categoryPath = config.get(SiteConfigComponent.CONFIG_CATEGORY_PATH);
                templatePath = config.get(SiteConfigComponent.CONFIG_CATEGORY_TEMPLATE_PATH);
            }
            if (site.isUseStatic() && CommonUtils.notEmpty(templatePath) && CommonUtils.notEmpty(categoryPath)) {
                String oldUrl = entity.getUrl();
                String filepath = createCategoryFile(site, entity, templatePath, categoryPath, pageIndex, totalPage);
                if (!entity.isHasStatic() || null == oldUrl || !oldUrl.equals(filepath)) {
                    categoryService.updateUrl(entity.getId(), filepath, true);
                }
                return true;
            } else if (CommonUtils.notEmpty(categoryPath)) {
                Map<String, Object> model = new HashMap<>();
                initCategoryUrl(site, entity);
                model.put("category", entity);
                model.put(CommonConstants.getAttributeSite(), site);
                String filepath = FreeMarkerUtils.generateStringByString(categoryPath, webConfiguration, model);
                if (entity.isHasStatic() || null == entity.getUrl() || !entity.getUrl().equals(filepath)) {
                    categoryService.updateUrl(entity.getId(), filepath, false);
                }
            } else if (entity.isHasStatic() || CommonUtils.notEmpty(entity.getUrl())) {
                categoryService.updateUrl(entity.getId(), null, false);
            }
        }
        return false;
    }

    /**
     * 内容页面静态化
     *
     * @param site
     * @param idList
     * @param category
     * @param categoryModel
     */
    public void createContentFile(SysSite site, List<Serializable> idList, CmsCategory category, CmsCategoryModel categoryModel) {
        List<Future<?>> futureList = new ArrayList<>();
        for (CmsContent content : contentService.getEntitys(idList)) {
            futureList.add(pool.submit(new PublishTask(this, site, content, category, categoryModel)));
        }
        for (Future<?> future : futureList) {
            try {
                future.get();
            } catch (InterruptedException | ExecutionException e) {
            }
        }
    }

    /**
     * 生成页面片段路径
     * 
     * @param filepath
     * @param category
     * @param model
     * @return
     * @throws IOException
     * @throws TemplateException
     */
    public String generatePlaceFilePath(String filepath, CmsCategory category, Map<String, Object> model)
            throws IOException, TemplateException {
        if (null == model) {
            model = new HashMap<>();
        }
        model.put("category", category);
        return FreeMarkerUtils.generateStringByString(filepath, webConfiguration, model);
    }

    /**
     * 静态化页面片段
     *
     * @param site
     * @param templatePath
     * @param metadata
     * @param data
     * @throws IOException
     * @throws TemplateException
     */
    public void staticPlace(SysSite site, String templatePath, CmsPlaceMetadata metadata, CmsPageData data)
            throws IOException, TemplateException {
        if (CommonUtils.notEmpty(templatePath)) {
            Map<String, Object> model = new HashMap<>();
            exposePlace(site, templatePath, metadata, data, model);
            String placeTemplatePath = INCLUDE_DIRECTORY + templatePath;
            String templateFullPath = SiteComponent.getFullTemplatePath(site.getId(), placeTemplatePath);
            FreeMarkerUtils.generateFileByFile(templateFullPath, siteComponent.getWebFilePath(site.getId(), placeTemplatePath),
                    webConfiguration, model);
        }
    }

    /**
     * 输出页面片段
     *
     * @param site
     * @param templatePath
     * @param metadata
     * @param data
     * @return place content
     * @throws IOException
     * @throws TemplateException
     */
    public String printPlace(SysSite site, String templatePath, CmsPlaceMetadata metadata, CmsPageData data)
            throws IOException, TemplateException {
        StringWriter writer = new StringWriter();
        printPlace(writer, site, templatePath, metadata, data);
        return writer.toString();
    }

    /**
     * 输出页面片段
     *
     * @param writer
     * @param site
     * @param templatePath
     * @param metadata
     * @param data
     * @throws IOException
     * @throws TemplateException
     */
    public void printPlace(Writer writer, SysSite site, String templatePath, CmsPlaceMetadata metadata, CmsPageData data)
            throws IOException, TemplateException {
        if (CommonUtils.notEmpty(templatePath)) {
            Map<String, Object> model = new HashMap<>();
            exposePlace(site, templatePath, metadata, data, model);
            String templateFullPath = SiteComponent.getFullTemplatePath(site.getId(), INCLUDE_DIRECTORY + templatePath);
            FreeMarkerUtils.generateStringByFile(writer, templateFullPath, webConfiguration, model);
        }
    }

    /**
     * 创建静态化页面
     *
     * @param site
     * @param fullTemplatePath
     * @param filepath
     * @param pageIndex
     * @param metadataMap
     * @param model
     * @param urlConsumer
     * @return static file path
     * @throws IOException
     * @throws TemplateException
     */
    public String createStaticFile(SysSite site, String fullTemplatePath, String filepath, Integer pageIndex,
            Map<String, Object> metadataMap, Map<String, Object> model, Consumer<String> urlConsumer)
            throws IOException, TemplateException {
        if (CommonUtils.notEmpty(filepath)) {
            if (null == model) {
                model = new HashMap<>();
            }
            model.put("metadata", metadataMap);
            model.put("pageIndex", pageIndex);
            AbstractFreemarkerView.exposeSite(model, site);
            filepath = FreeMarkerUtils.generateStringByString(filepath, webConfiguration, model);
            if (filepath.startsWith(CommonConstants.SEPARATOR)) {
                filepath = filepath.substring(1);
            }
            String fullPath = site.getSitePath() + filepath;
            model.put("url", fullPath);
            if (null != urlConsumer) {
                urlConsumer.accept(fullPath);
            }
            String staticFilePath;
            if (filepath.endsWith(CommonConstants.SEPARATOR)) {
                staticFilePath = filepath + CommonConstants.getDefaultPage();
            } else {
                staticFilePath = filepath;
            }
            if (CommonUtils.notEmpty(pageIndex) && 1 < pageIndex) {
                int index = staticFilePath.lastIndexOf(CommonConstants.DOT);
                staticFilePath = staticFilePath.substring(0, index) + CommonConstants.UNDERLINE + pageIndex
                        + staticFilePath.substring(index, staticFilePath.length());
            }
            FreeMarkerUtils.generateFileByFile(fullTemplatePath, siteComponent.getWebFilePath(site.getId(), staticFilePath),
                    webConfiguration, model);
        }
        return filepath;
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
    public static void initContentUrl(SysSite site, CmsContent entity) {
        entity.setUrl(getUrl(site, entity.isHasStatic(), entity.getUrl()));
    }

    /**
     * @param site
     * @param entity
     */
    public static void initContentCover(SysSite site, CmsContent entity) {
        entity.setCover(getUrl(site, true, entity.getCover()));
    }

    /**
     * @param site
     * @param hasStatic
     * @param url
     * @return
     */
    public static String getUrl(SysSite site, boolean hasStatic, String url) {
        if (CommonUtils.empty(url) || url.contains("://") || url.startsWith("//") || url.startsWith("#")) {
            return url;
        } else {
            return hasStatic ? getUrl(site.getSitePath(), url) : getUrl(site.getDynamicPath(), url);
        }
    }

    /**
     * @param sitePath
     * @param url
     * @return
     */
    public static String getUrl(String sitePath, String url) {
        if (CommonUtils.empty(url) || url.contains("://") || url.startsWith("//") || url.startsWith("#")) {
            return url;
        } else {
            return sitePath + url;
        }
    }

    /**
     * @param site
     * @param entity
     */
    public static void initCategoryUrl(SysSite site, CmsCategory entity) {
        if (!entity.isOnlyUrl()) {
            entity.setUrl(getUrl(site, entity.isHasStatic(), entity.getUrl()));
        }
    }

    private void exposePlace(SysSite site, String templatePath, CmsPlaceMetadata metadata, CmsPageData data,
            Map<String, Object> model) {
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
        model.put("path", templatePath);
        model.put("metadata", metadata.getAsMap(data));
        AbstractFreemarkerView.exposeSite(model, site);
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

    @PreDestroy
    public void destroy() {
        pool.shutdown();
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

/**
 * 
 * PublishTask 静态化线程
 *
 */
class PublishTask implements Runnable {
    private TemplateComponent templateComponent;
    private SysSite site;
    private CmsContent content;
    private CmsCategory category;
    private CmsCategoryModel categoryModel;
    private final Log log = LogFactory.getLog(getClass());

    public PublishTask(TemplateComponent templateComponent, SysSite site, CmsContent content, CmsCategory category,
            CmsCategoryModel categoryModel) {
        this.templateComponent = templateComponent;
        this.site = site;
        this.content = content;
        this.category = category;
        this.categoryModel = categoryModel;
    }

    @Override
    public void run() {
        try {
            templateComponent.createContentFile(site, content, category, categoryModel);
        } catch (IOException | TemplateException e) {
            log.error(e.getMessage());
        }
    }
}

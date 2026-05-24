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
import org.springframework.ui.ModelMap;

import com.publiccms.common.api.AdminContextPath;
import com.publiccms.common.api.Cache;
import com.publiccms.common.base.AbstractFreemarkerView;
import com.publiccms.common.base.AbstractTemplateDirective;
import com.publiccms.common.base.BaseMethod;
import com.publiccms.common.constants.CommonConstants;
import com.publiccms.common.constants.Constants;
import com.publiccms.common.directive.BaseTemplateDirective;
import com.publiccms.common.handler.PageHandler;
import com.publiccms.common.handler.RenderHandler;
import com.publiccms.common.tools.CmsFileUtils;
import com.publiccms.common.tools.CmsLangUtils;
import com.publiccms.common.tools.CmsUrlUtils;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.common.tools.ExtendUtils;
import com.publiccms.common.tools.FreeMarkerUtils;
import com.publiccms.entities.cms.CmsCategory;
import com.publiccms.entities.cms.CmsCategoryAttribute;
import com.publiccms.entities.cms.CmsCategoryLang;
import com.publiccms.entities.cms.CmsCategoryLangId;
import com.publiccms.entities.cms.CmsCategoryModel;
import com.publiccms.entities.cms.CmsCategoryModelId;
import com.publiccms.entities.cms.CmsContent;
import com.publiccms.entities.cms.CmsContentAttribute;
import com.publiccms.entities.cms.CmsContentLang;
import com.publiccms.entities.cms.CmsContentLangId;
import com.publiccms.entities.cms.CmsLanguage;
import com.publiccms.entities.cms.CmsPlace;
import com.publiccms.entities.cms.CmsPlaceAttribute;
import com.publiccms.entities.sys.SysSite;
import com.publiccms.logic.component.BeanComponent;
import com.publiccms.logic.component.config.ConfigDataComponent;
import com.publiccms.logic.component.config.ContentConfigComponent;
import com.publiccms.logic.component.config.ContentConfigComponent.KeywordsConfig;
import com.publiccms.logic.component.config.SiteAttributeComponent;
import com.publiccms.logic.component.config.SiteConfigComponent;
import com.publiccms.logic.component.site.FileUploadComponent;
import com.publiccms.logic.component.site.SiteComponent;
import com.publiccms.logic.component.site.StatisticsComponent;
import com.publiccms.logic.service.cms.CmsCategoryAttributeService;
import com.publiccms.logic.service.cms.CmsCategoryLangService;
import com.publiccms.logic.service.cms.CmsCategoryModelService;
import com.publiccms.logic.service.cms.CmsCategoryService;
import com.publiccms.logic.service.cms.CmsContentAttributeService;
import com.publiccms.logic.service.cms.CmsContentLangService;
import com.publiccms.logic.service.cms.CmsContentService;
import com.publiccms.logic.service.cms.CmsLanguageService;
import com.publiccms.logic.service.cms.CmsPlaceAttributeService;
import com.publiccms.logic.service.cms.CmsPlaceService;
import com.publiccms.views.pojo.entities.CmsCategoryType;
import com.publiccms.views.pojo.entities.CmsModel;
import com.publiccms.views.pojo.entities.CmsPageData;
import com.publiccms.views.pojo.entities.CmsPageMetadata;
import com.publiccms.views.pojo.entities.CmsPlaceMetadata;
import com.publiccms.views.pojo.model.CmsContentParameters;

import freemarker.template.Configuration;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;
import freemarker.template.TemplateModelException;

/**
 * 模板处理组件 Template Component
 *
 */
@Component
public class TemplateComponent implements Cache, AdminContextPath {
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
    private CmsContentLangService contentLangService;
    @Resource
    private CmsCategoryLangService categoryLangService;
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
    private CmsPlaceAttributeService placeAttributeService;
    @Resource
    protected FileUploadComponent fileUploadComponent;
    @Resource
    protected ConfigDataComponent configDataComponent;
    @Resource
    protected ContentConfigComponent contentConfigComponent;
    @Resource
    private StatisticsComponent statisticsComponent;
    @Resource
    private CmsLanguageService languageService;
    @Resource
    protected SiteAttributeComponent siteAttributeComponent;

    private static ExecutorService pool = Executors.newFixedThreadPool(2 * Runtime.getRuntime().availableProcessors());

    /**
     * 分类页面静态化
     *
     * @param site
     * @param entity
     * @param langEntity
     * @param defaultLang
     * @param templatePath
     * @param filepathTemplate
     * @param pageIndex
     * @param totalPage
     * @return category static file path
     * @throws IOException
     * @throws TemplateException
     */
    public String createCategoryFile(SysSite site, CmsCategory entity, CmsCategoryLang langEntity, String templatePath,
            String filepathTemplate, Integer pageIndex, Integer totalPage) throws IOException, TemplateException {
        Map<String, Object> model = new HashMap<>();
        if (CommonUtils.empty(pageIndex)) {
            pageIndex = 1;
        }
        CmsUrlUtils.initCategoryUrl(site, entity);

        CmsCategoryAttribute attribute = categoryAttributeService.getEntity(entity.getId());
        CmsLangUtils.initLang(attribute, langEntity);

        entity.setAttribute(ExtendUtils.getAttributeMap(attribute));

        model.put("category", entity);
        model.put("attribute", entity.getAttribute());
        String defaultLang = siteAttributeComponent.getDefaultLanguage(site.getId());
        String realTemplatePath = siteComponent.getTemplateFilePath(site.getId(), templatePath);
        CmsPageMetadata metadata = metadataComponent.getTemplateMetadata(realTemplatePath, entity.getLang(), defaultLang);

        String fullTemplatePath = SiteComponent.getFullTemplatePath(site.getId(), templatePath);
        String filepath = generateFilepath(filepathTemplate, site, model);
        String fullStaticFilePath = CmsLangUtils.getFullFilepath(filepath,
                null == langEntity ? null : langEntity.getId().getLang(), defaultLang);
        if (CommonUtils.notEmpty(totalPage) && pageIndex < totalPage) {
            for (int i = pageIndex + 1; i <= totalPage; i++) {
                createStaticFile(site, fullTemplatePath, fullStaticFilePath, entity.getLang(), i, metadata, model, url -> {
                    if (null == entity.getUrl()) {
                        entity.setUrl(url);
                    }
                });
            }
        }

        return createStaticFile(site, fullTemplatePath, fullStaticFilePath, entity.getLang(), pageIndex, metadata, model, url -> {
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
     * @param langEntity
     * @param defaultLang
     * @param category
     * @param createMultiContentPage
     * @param templatePath
     * @param filepathTemplate
     * @param pageIndex
     * @return content static file path
     * @throws IOException
     * @throws TemplateException
     */
    public String createContentFile(SysSite site, CmsContent entity, CmsContentLang langEntity, CmsCategory category,
            boolean createMultiContentPage, String templatePath, String filepathTemplate, Integer pageIndex)
            throws IOException, TemplateException {
        Map<String, Object> model = new HashMap<>();

        CmsUrlUtils.initContentUrl(site, entity);
        fileUploadComponent.initContentCover(site, entity);
        CmsUrlUtils.initCategoryUrl(site, category);

        CmsContentAttribute attribute = contentAttributeService.getEntity(entity.getId());
        CmsLangUtils.initLang(attribute, langEntity);

        KeywordsConfig config = contentConfigComponent.getKeywordsConfig(site.getId());
        entity.setAttribute(ExtendUtils.getAttributeMap(attribute, config));

        model.put("content", entity);
        model.put("attribute", entity.getAttribute());
        model.put("category", category);

        String realTemplatePath = siteComponent.getTemplateFilePath(site.getId(), templatePath);
        String defaultLang = siteAttributeComponent.getDefaultLanguage(site.getId());
        CmsPageMetadata metadata = metadataComponent.getTemplateMetadata(realTemplatePath, entity.getLang(), defaultLang);

        String fullTemplatePath = SiteComponent.getFullTemplatePath(site.getId(), templatePath);
        String filepath = generateFilepath(filepathTemplate, site, model);
        String fullStaticFilePath = CmsLangUtils.getFullFilepath(filepath,
                null == langEntity ? null : langEntity.getId().getLang(), defaultLang);
        if (null != attribute && CommonUtils.notEmpty(filepath) && CommonUtils.notEmpty(attribute.getText())) {
            String pageBreakTag = null;
            if (-1 < attribute.getText().indexOf(CommonConstants.getCkeditorPageBreakTag())) {
                pageBreakTag = CommonConstants.getCkeditorPageBreakTag();
            } else if (-1 < attribute.getText().indexOf(CommonConstants.getTinyMCEPageBreakTag())) {
                pageBreakTag = CommonConstants.getTinyMCEPageBreakTag();
            } else {
                pageBreakTag = CommonConstants.getUeditorPageBreakTag();
            }
            String[] texts = StringUtils.splitByWholeSeparator(attribute.getText(), pageBreakTag);
            if (createMultiContentPage) {
                for (int i = 1; i < texts.length; i++) {
                    PageHandler page = new PageHandler(i + 1, 1);
                    page.setTotalCount(texts.length);
                    model.put("text", ExtendUtils.replaceText(texts[i], config));
                    model.put("page", page);
                    createStaticFile(site, fullTemplatePath, fullStaticFilePath, entity.getLang(), i + 1, metadata, model,
                            url -> {
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
            model.put("text", ExtendUtils.replaceText(texts[page.getPageIndex() - 1], config));
        }
        return createStaticFile(site, fullTemplatePath, fullStaticFilePath, entity.getLang(), pageIndex, metadata, model, url -> {
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
     * @param lang
     * @return whether the create is successful
     * @throws TemplateException
     * @throws IOException
     */
    private boolean createContentFile(SysSite site, CmsContent entity, CmsContentLang lang, CmsCategory category,
            CmsCategoryModel categoryModel) throws IOException, TemplateException {
        if (null != site && null != entity) {
            if (entity.isOnlyUrl()) {
                if (null == entity.getParentId() && null != entity.getQuoteContentId()) {
                    if (null != lang) {
                        CmsContentLang quote = contentLangService
                                .getEntity(new CmsContentLangId(entity.getQuoteContentId(), entity.getLang()));
                        if (null != quote) {
                            contentLangService.updateUrl(lang.getId(), quote.getUrl());
                        }
                    } else {
                        CmsContent quote = contentService.getEntity(entity.getQuoteContentId());
                        if (null != quote) {
                            contentService.updateUrl(entity.getId(), quote.getUrl(), quote.isHasStatic());
                        }
                    }
                }
                return true;
            } else {
                if (null == category) {
                    category = categoryService.getEntity(entity.getCategoryId());
                }
                if (null != lang) {
                    CmsCategoryLang categoryLang = categoryLangService
                            .getEntity(new CmsCategoryLangId(category.getId(), lang.getId().getLang()));
                    CmsLangUtils.initLang(category, category.getLang(), categoryLang);
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
                        CmsModel model = modelComponent.getModel(site, entity.getModelId());
                        templatePath = model.getTemplatePath();
                        if (category.isCustomContentPath()) {
                            contentPath = category.getContentPath();
                        } else {
                            contentPath = model.getContentPath();
                        }
                    }
                    if (site.isUseStatic() && CommonUtils.notEmpty(templatePath) && CommonUtils.notEmpty(contentPath)) {
                        String oldUrl = entity.getUrl();
                        String filepath = createContentFile(site, entity, lang, category, true, templatePath, contentPath, null);
                        if (null != lang) {
                            contentLangService.updateUrl(lang.getId(), filepath);
                        } else if (!entity.isHasStatic() || null == oldUrl || !oldUrl.equals(filepath)) {
                            contentService.updateUrl(entity.getId(), filepath, true);
                        }
                    } else if (CommonUtils.notEmpty(contentPath)) {
                        Map<String, Object> model = new HashMap<>();
                        CmsUrlUtils.initContentUrl(site, entity);
                        fileUploadComponent.initContentCover(site, entity);
                        CmsUrlUtils.initCategoryUrl(site, category);
                        CmsContentAttribute attribute = contentAttributeService.getEntity(entity.getId());
                        if (CommonUtils.notEmpty(entity.getLang())) {
                            CmsContentLang langEntity = contentLangService
                                    .getEntity(new CmsContentLangId(entity.getId(), entity.getLang()));
                            CmsLangUtils.initLang(attribute, langEntity);
                        }

                        entity.setAttribute(ExtendUtils.getAttributeMap(attribute, null));
                        model.put("content", entity);
                        model.put("attribute", entity.getAttribute());
                        model.put("category", category);
                        AbstractFreemarkerView.exposeSite(model, site);
                        String filepath = FreeMarkerUtils.generateStringByString(contentPath, webConfiguration, model);
                        if (null != lang) {
                            contentLangService.updateUrl(lang.getId(), filepath);
                        } else if (entity.isHasStatic() || null == entity.getUrl() || !entity.getUrl().equals(filepath)) {
                            contentService.updateUrl(entity.getId(), filepath, false);
                        }
                    } else if (null != lang) {
                        contentLangService.updateUrl(lang.getId(), null);
                    } else if (entity.isHasStatic() || CommonUtils.notEmpty(entity.getUrl())) {
                        contentService.updateUrl(entity.getId(), null, false);
                    }
                    return true;
                }
            }
        }
        return false;

    }

    /**
     * @param site
     * @param entity
     * @param pageIndex
     * @param totalPage
     * @return
     * @throws TemplateException
     * @throws IOException
     */
    public boolean publish(SysSite site, CmsCategory entity, Integer pageIndex, Integer totalPage)
            throws IOException, TemplateException {
        boolean flag = false;
        if (null != site && null != entity && site.getId() == entity.getSiteId()) {
            flag = createCategoryFile(site, entity, null, pageIndex, totalPage);
            if (siteAttributeComponent.enableMultilingual(site.getId())) {
                String oldLang = entity.getLang();
                List<CmsCategoryLang> langList = categoryLangService.getList(entity.getId());
                for (CmsCategoryLang lang : langList) {
                    if (CmsLangUtils.initLang(entity, oldLang, lang)) {
                        createCategoryFile(site, entity, lang, pageIndex, totalPage);
                    }
                }
            }
        }
        return flag;
    }

    /**
     * @param site
     * @param entity
     * @return
     * @throws TemplateException
     * @throws IOException
     */
    public boolean publish(SysSite site, CmsContent entity) throws IOException, TemplateException {
        boolean flag = false;
        if (null != site && null != entity && site.getId() == entity.getSiteId()) {
            flag = publish(site, entity, null);
            if (null != entity.getParentId()) {
                CmsContent parent = contentService.getEntity(entity.getParentId());
                if (null != parent) {
                    publish(site, parent, null);
                }
            } else {
                CmsCategory category = categoryService.getEntity(entity.getCategoryId());
                if (null != category) {
                    publish(site, category, null, null);
                }
            }
        }
        return flag;
    }

    /**
     * @param site
     * @param entity
     * @param category
     * @return
     * @throws TemplateException
     * @throws IOException
     */
    public boolean publish(SysSite site, CmsContent entity, CmsCategory category) throws IOException, TemplateException {
        return publish(site, entity, category, null);
    }

    /**
     * @param site
     * @param entity
     * @param category
     * @param categoryModel
     * @return
     * @throws TemplateException
     * @throws IOException
     */
    public boolean publish(SysSite site, CmsContent entity, CmsCategory category, CmsCategoryModel categoryModel)
            throws IOException, TemplateException {
        boolean flag = false;
        if (null != entity) {
            flag = createContentFile(site, entity, null, category, categoryModel);
            if (siteAttributeComponent.enableMultilingual(site.getId())) {
                String oldLang = entity.getLang();
                List<CmsContentLang> langList = contentLangService.getList(entity.getId());
                for (CmsContentLang lang : langList) {
                    if (CmsLangUtils.initLang(entity, oldLang, lang)) {
                        createContentFile(site, entity, lang, category, categoryModel);
                    }
                }
            }
        }
        return flag;
    }

    /**
     * @param siteId
     * @param entity
     *            分类
     */
    public void deleteStaticFile(short siteId, CmsCategory entity) {
        if (null != entity && entity.isHasStatic() && CommonUtils.notEmpty(entity.getUrl())) {
            deleteStaticFile(siteId, entity.getUrl());
            if (siteAttributeComponent.enableMultilingual(siteId)) {
                List<CmsCategoryLang> langList = categoryLangService.getList(entity.getId());
                for (CmsCategoryLang lang : langList) {
                    if (!lang.getId().getLang().equalsIgnoreCase(entity.getLang())) {
                        deleteStaticFile(siteId, entity.getUrl());
                    }
                }
            }
        }
    }

    /**
     * @param siteId
     * @param entity
     *            内容
     */
    public void deleteStaticFile(short siteId, CmsContent entity) {
        if (null != entity && !entity.isOnlyUrl() && entity.isHasStatic() && null == entity.getQuoteContentId()
                && CommonUtils.notEmpty(entity.getUrl())) {
            deleteStaticFile(siteId, entity.getUrl());
            if (siteAttributeComponent.enableMultilingual(siteId)) {
                List<CmsContentLang> langList = contentLangService.getList(entity.getId());
                for (CmsContentLang lang : langList) {
                    if (!lang.getId().getLang().equalsIgnoreCase(entity.getLang())) {
                        deleteStaticFile(siteId, entity.getUrl());
                    }
                }
            }
        }
    }

    /**
     * @param siteId
     * @param url
     */
    public void deleteStaticFile(short siteId, String url) {
        if (url.endsWith(Constants.SEPARATOR)) {
            url = CommonUtils.joinString(url, CommonConstants.getDefaultPage());
        }
        String filepath = siteComponent.getWebFilePath(siteId, url);
        if (CmsFileUtils.isFile(filepath)) {
            String backupFilePath = siteComponent.getWebBackupFilePath(siteId, url);
            CmsFileUtils.moveFile(filepath, backupFilePath);
        }
    }

    /**
     * 分类页面静态化
     *
     * @param site
     * @param entity
     * @param lang
     * @param defaultLang
     * @param pageIndex
     * @param totalPage
     * @return whether the create is successful
     * @throws IOException
     * @throws TemplateException
     */
    private boolean createCategoryFile(SysSite site, CmsCategory entity, CmsCategoryLang lang, Integer pageIndex,
            Integer totalPage) throws IOException, TemplateException {
        if (entity.isOnlyUrl()) {
            if (null != lang) {
                categoryLangService.updateUrl(lang.getId(), entity.getPath());
            } else {
                categoryService.updateUrl(entity.getId(), entity.getPath(), false);
            }
        } else {
            String categoryPathTemplate;
            String templatePath = null;
            if (entity.isCustomPath()) {
                templatePath = entity.getTemplatePath();
                categoryPathTemplate = entity.getPath();
            } else {
                Map<String, String> config = configDataComponent.getConfigData(site.getId(), SiteConfigComponent.CONFIG_CODE);
                if (CommonUtils.notEmpty(entity.getTypeId())) {
                    CmsCategoryType categoryType = modelComponent.getCategoryType(site.getId(), entity.getTypeId());
                    if (null != categoryType) {
                        templatePath = categoryType.getTemplatePath();
                        categoryPathTemplate = categoryType.getPath();
                    } else {
                        templatePath = config.get(SiteConfigComponent.CONFIG_CATEGORY_TEMPLATE_PATH);
                        categoryPathTemplate = config.get(SiteConfigComponent.CONFIG_CATEGORY_PATH);
                    }
                } else {
                    templatePath = config.get(SiteConfigComponent.CONFIG_CATEGORY_TEMPLATE_PATH);
                    categoryPathTemplate = config.get(SiteConfigComponent.CONFIG_CATEGORY_PATH);
                }
            }
            if (site.isUseStatic() && CommonUtils.notEmpty(templatePath) && CommonUtils.notEmpty(categoryPathTemplate)) {
                String oldUrl = entity.getUrl();
                String filepath = createCategoryFile(site, entity, lang, templatePath, categoryPathTemplate, pageIndex,
                        totalPage);
                if (null != lang) {
                    categoryLangService.updateUrl(lang.getId(), filepath);
                } else if (!entity.isHasStatic() || null == oldUrl || !oldUrl.equals(filepath)) {
                    categoryService.updateUrl(entity.getId(), filepath, true);
                }
                return true;
            } else if (CommonUtils.notEmpty(categoryPathTemplate)) {
                Map<String, Object> model = new HashMap<>();
                CmsUrlUtils.initCategoryUrl(site, entity);
                CmsCategoryAttribute attribute = categoryAttributeService.getEntity(entity.getId());
                CmsLangUtils.initLang(attribute, lang);
                entity.setAttribute(ExtendUtils.getAttributeMap(attribute));
                model.put("category", entity);
                AbstractFreemarkerView.exposeSite(model, site);
                String filepath = FreeMarkerUtils.generateStringByString(categoryPathTemplate, webConfiguration, model);
                if (null != lang) {
                    categoryLangService.updateUrl(lang.getId(), filepath);
                } else if (entity.isHasStatic() || null == entity.getUrl() || !entity.getUrl().equals(filepath)) {
                    categoryService.updateUrl(entity.getId(), filepath, false);
                }
            } else if (null != lang) {
                categoryLangService.updateUrl(lang.getId(), null);
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
                Thread.currentThread().interrupt();
            }
        }
    }

    /**
     * @param site
     * @param entity
     * @param attribute
     * @param contentParameters
     * @param writer
     * @param model
     */
    public void initPreviewContentModel(SysSite site, CmsContent entity, CmsContentAttribute attribute,
            CmsContentParameters contentParameters, ModelMap model) {
        KeywordsConfig config = contentConfigComponent.getKeywordsConfig(site.getId());
        Map<String, BaseTemplateDirective> directiveMap = new HashMap<>();
        directiveMap.putAll(BeanComponent.getDirectiveComponent().getNamespaceMap().get("cms"));
        directiveMap.put("content", new AbstractTemplateDirective() {
            @Override
            public void execute(RenderHandler handler) throws IOException, TemplateException {
                handler.put("object", entity);
                handler.render();
            }
        });
        if (null != contentParameters.getFiles() || null != contentParameters.getImages()) {
            directiveMap.put("contentFileList", new AbstractTemplateDirective() {
                @Override
                public void execute(RenderHandler handler) throws IOException, TemplateException {
                    PageHandler page = new PageHandler(1, 0);
                    boolean absoluteURL = handler.getBoolean("absoluteURL", true);
                    if (handler.getBoolean("image", false) && null != contentParameters.getImages()
                            || null == contentParameters.getFiles()) {
                        if (absoluteURL) {
                            contentParameters.getImages().forEach(
                                    e -> e.setFilePath(CmsUrlUtils.getUrl(fileUploadComponent.getPrefix(site), e.getFilePath())));
                        }
                        page.setList(contentParameters.getImages());
                    } else if (null != contentParameters.getFiles()) {
                        if (absoluteURL) {
                            contentParameters.getFiles().forEach(
                                    e -> e.setFilePath(CmsUrlUtils.getUrl(fileUploadComponent.getPrefix(site), e.getFilePath())));
                        }
                        page.setList(contentParameters.getFiles());
                    }
                    page.setTotalCount(page.getList().size());
                    handler.put("page", page);
                    handler.render();
                }
            });
        }
        if (null != contentParameters.getProducts()) {
            directiveMap.put("contentProductList", new AbstractTemplateDirective() {
                @Override
                public void execute(RenderHandler handler) throws IOException, TemplateException {
                    PageHandler page = new PageHandler(1, 0);
                    boolean absoluteURL = handler.getBoolean("absoluteURL", true);
                    contentParameters.getProducts().forEach(e -> {
                        if (absoluteURL) {
                            e.setCover(CmsUrlUtils.getUrl(fileUploadComponent.getPrefix(site), e.getCover()));
                        }
                    });
                    page.setList(contentParameters.getFiles());
                    page.setTotalCount(page.getList().size());
                    handler.put("page", page);
                    handler.render();
                }
            });
        }
        AbstractFreemarkerView.exposeSite(model, site);
        model.put("cms", directiveMap);
        attribute.setData(ExtendUtils.getExtendString(contentParameters.getExtendData(), site.getSitePath()));
        model.put("getContentAttribute", new BaseMethod() {

            @Override
            public Object execute(List<TemplateModel> arguments) throws TemplateModelException {
                return ExtendUtils.getAttributeMap(attribute, config);
            }

            @Override
            public int minParametersNumber() {
                return 0;
            }

            @Override
            public boolean needAppToken() {
                return false;
            }

        });
    }

    /**
     * @param site
     * @param entity
     * @param attribute
     * @param writer
     * @param model
     */
    public void previewContent(SysSite site, CmsContent entity, CmsContentAttribute attribute, Writer writer, ModelMap model) {
        CmsCategoryModel categoryModel = categoryModelService
                .getEntity(new CmsCategoryModelId(entity.getCategoryId(), entity.getModelId()));
        if (null != categoryModel) {
            String contentPath = null;
            String templatePath = null;
            CmsCategory category = categoryService.getEntity(entity.getCategoryId());
            if (categoryModel.isCustomContentPath()) {
                contentPath = categoryModel.getContentPath();
                templatePath = categoryModel.getTemplatePath();
            } else {
                CmsModel cmsmodel = modelComponent.getModel(site, entity.getModelId());
                templatePath = cmsmodel.getTemplatePath();
                if (null != category && category.isCustomContentPath()) {
                    contentPath = category.getContentPath();
                } else if (null != cmsmodel) {
                    contentPath = cmsmodel.getContentPath();
                }
            }
            if (CommonUtils.notEmpty(templatePath) || CommonUtils.notEmpty(contentPath)) {
                if (CommonUtils.empty(templatePath)) {
                    if (contentPath.contains("?")) {
                        templatePath = contentPath.substring(0, contentPath.indexOf("?"));
                    } else if (contentPath.contains("/${content.id}")) {
                        templatePath = CommonUtils.joinString(contentPath.substring(0, contentPath.indexOf("/${content.id}")),
                                ".html");
                    } else {
                        templatePath = null;
                    }
                }
                if (CommonUtils.notEmpty(templatePath)) {
                    CmsUrlUtils.initContentUrl(site, entity);
                    fileUploadComponent.initContentCover(site, entity);
                    CmsUrlUtils.initCategoryUrl(site, category);
                    KeywordsConfig config = contentConfigComponent.getKeywordsConfig(site.getId());
                    entity.setAttribute(ExtendUtils.getAttributeMap(attribute, config));
                    AbstractFreemarkerView.exposeSite(model, site);
                    model.put("content", entity);
                    model.put("attribute", entity.getAttribute());
                    model.put("category", category);
                    model.put("text", ExtendUtils.replaceText(attribute.getText(), config));
                    String realTemplatePath = siteComponent.getTemplateFilePath(site.getId(), templatePath);

                    CmsPageMetadata metadata = metadataComponent.getTemplateMetadata(realTemplatePath, null, null);
                    model.put("metadata", metadata);
                    model.put("url", templatePath);
                    try {
                        FreeMarkerUtils.generateStringByFile(writer,
                                SiteComponent.getFullTemplatePath(site.getId(), templatePath), webConfiguration, model);
                    } catch (IOException | TemplateException e) {
                    }
                }
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
     *            站点
     * @param templatePath
     *            模板路径 不含include
     * @param checkExists
     *            检查是否存在
     * @throws IOException
     * @throws TemplateException
     */
    public void publishPlace(SysSite site, String templatePath, boolean checkExists) throws IOException, TemplateException {
        if (CommonUtils.notEmpty(templatePath)) {
            String fullTemplatePath = CommonUtils.joinString(INCLUDE_DIRECTORY, templatePath);
            if (site.isUseSsi() || CmsFileUtils.exists(siteComponent.getWebFilePath(site.getId(), fullTemplatePath))) {
                String realFilepath = siteComponent.getTemplateFilePath(site.getId(), fullTemplatePath);
                CmsPlaceMetadata metadata = metadataComponent.getPlaceMetadata(realFilepath, null, null);
                if (siteAttributeComponent.enableMultilingual(site.getId())
                        && (metadata.isEnableMultilingual() || metadata.isEnablePlaceMultilingual())) {
                    List<CmsLanguage> languageList = languageService.getList(site.getId());
                    if (null != languageList) {
                        String defaultLang = siteAttributeComponent.getDefaultLanguage(site.getId());
                        for (CmsLanguage lang : languageList) {
                            String fullStaticFilePath = CommonUtils.joinString(INCLUDE_DIRECTORY,
                                    CmsLangUtils.getPlaceFilepath(templatePath, lang.getId().getCode(), defaultLang));
                            if (!checkExists
                                    || CmsFileUtils.exists(siteComponent.getWebFilePath(site.getId(), fullStaticFilePath))) {
                                CmsPageData data = metadataComponent.getPageData(realFilepath, lang.getId().getCode(),
                                        defaultLang);
                                if (null != data) {
                                    metadata.setExtendData(data.getExtendData());
                                }
                                staticPlace(site, templatePath, lang.getId().getCode(), defaultLang, metadata);
                            }
                        }
                    }
                } else if (!checkExists || CmsFileUtils.exists(siteComponent.getWebFilePath(site.getId(), fullTemplatePath))) {
                    staticPlace(site, templatePath, null, null, metadata);
                }
            }
        }
    }

    /**
     * 静态化页面片段
     *
     * @param site
     * @param templatePath
     * @param lang
     * @param metadata
     * @throws IOException
     * @throws TemplateException
     */
    private void staticPlace(SysSite site, String templatePath, String lang, String defaultLang, CmsPlaceMetadata metadata)
            throws IOException, TemplateException {
        if (CommonUtils.notEmpty(templatePath)) {
            Map<String, Object> model = new HashMap<>();
            exposePlace(site, templatePath, lang, defaultLang, metadata, model);
            String fullStaticFilePath = CommonUtils.joinString(INCLUDE_DIRECTORY,
                    CmsLangUtils.getPlaceFilepath(templatePath, lang, defaultLang));
            String realTemplatePath = SiteComponent.getFullTemplatePath(site.getId(),
                    CommonUtils.joinString(INCLUDE_DIRECTORY, templatePath));
            FreeMarkerUtils.generateFileByFile(realTemplatePath, siteComponent.getWebFilePath(site.getId(), fullStaticFilePath),
                    webConfiguration, model);
        }
    }

    /**
     * 输出页面片段
     *
     * @param site
     * @param templatePath
     * @param lang
     * @param metadata
     * @return place content
     * @throws IOException
     * @throws TemplateException
     */
    public String printPlace(SysSite site, String templatePath, String lang, CmsPlaceMetadata metadata)
            throws IOException, TemplateException {
        StringWriter writer = new StringWriter();
        String defaultLang = siteAttributeComponent.getDefaultLanguage(site.getId());
        printPlace(writer, site, templatePath, lang, defaultLang, metadata);
        return writer.toString();
    }

    /**
     * 输出页面片段
     *
     * @param writer
     * @param site
     * @param templatePath
     * @param lang
     * @param defaultLang
     * @param metadata
     * @throws IOException
     * @throws TemplateException
     */
    public void printPlace(Writer writer, SysSite site, String templatePath, String lang, String defaultLang,
            CmsPlaceMetadata metadata) throws IOException, TemplateException {
        if (CommonUtils.notEmpty(templatePath)) {
            Map<String, Object> model = new HashMap<>();
            exposePlace(site, templatePath, lang, defaultLang, metadata, model);
            String templateFullPath = SiteComponent.getFullTemplatePath(site.getId(),
                    CommonUtils.joinString(INCLUDE_DIRECTORY, templatePath));
            FreeMarkerUtils.generateStringByFile(writer, templateFullPath, webConfiguration, model);
        }
    }

    /**
     * 创建静态化页面
     *
     * @param site
     * @param templatePath
     * @param lang
     * @return
     * @throws IOException
     * @throws TemplateException
     */
    public boolean publishPage(SysSite site, String templatePath, String lang) throws IOException, TemplateException {
        if (CommonUtils.notEmpty(templatePath)) {
            String fullTemplatePath = SiteComponent.getFullTemplatePath(site.getId(), templatePath);
            String realFilepath = siteComponent.getTemplateFilePath(site.getId(), templatePath);
            CmsPageMetadata metadata = metadataComponent.getTemplateMetadata(realFilepath, null, null);
            if (site.isUseStatic() && CommonUtils.notEmpty(metadata.getPublishPath())) {
                if (siteAttributeComponent.enableMultilingual(site.getId()) && metadata.isEnableMultilingual()) {
                    String defaultLang = siteAttributeComponent.getDefaultLanguage(site.getId());
                    String fullStaticFilePath = CmsLangUtils.getFullFilepath(metadata.getPublishPath(), lang, defaultLang);
                    CmsPageData data = metadataComponent.getPageData(realFilepath, lang, defaultLang);
                    if (null != data) {
                        metadata.setExtendData(data.getExtendData());
                    }
                    createStaticFile(site, fullTemplatePath, fullStaticFilePath, lang, null, metadata, null, null);
                } else {
                    createStaticFile(site, fullTemplatePath, metadata.getPublishPath(), null, null, metadata, null, null);
                }
                return true;
            }
        }
        return false;
    }

    /**
     * 创建静态化页面
     *
     * @param site
     * @param templatePath
     * @throws IOException
     * @throws TemplateException
     */
    public void publishPage(SysSite site, String templatePath) throws IOException, TemplateException {
        if (CommonUtils.notEmpty(templatePath)) {
            String fullTemplatePath = SiteComponent.getFullTemplatePath(site.getId(), templatePath);
            String realFilepath = siteComponent.getTemplateFilePath(site.getId(), templatePath);
            CmsPageMetadata metadata = metadataComponent.getTemplateMetadata(realFilepath, null, null);
            if (site.isUseStatic() && CommonUtils.notEmpty(metadata.getPublishPath())) {
                if (siteAttributeComponent.enableMultilingual(site.getId()) && metadata.isEnableMultilingual()) {
                    List<CmsLanguage> languageList = languageService.getList(site.getId());
                    if (null != languageList) {
                        String defaultLang = siteAttributeComponent.getDefaultLanguage(site.getId());
                        for (CmsLanguage lang : languageList) {
                            String fullStaticFilePath = CmsLangUtils.getFullFilepath(metadata.getPublishPath(),
                                    lang.getId().getCode(), defaultLang);
                            CmsPageData data = metadataComponent.getPageData(realFilepath, lang.getId().getCode(), defaultLang);
                            if (null != data) {
                                metadata.setExtendData(data.getExtendData());
                            }
                            createStaticFile(site, fullTemplatePath, fullStaticFilePath, lang.getId().getCode(), null, metadata,
                                    null, null);
                        }
                    }
                } else {
                    createStaticFile(site, fullTemplatePath, metadata.getPublishPath(), null, null, metadata, null, null);
                }

            }
        }
    }

    public String generateFilepath(String filepathTemplate, SysSite site, Map<String, Object> model)
            throws IOException, TemplateException {
        AbstractFreemarkerView.exposeSite(model, site);
        String filepath = FreeMarkerUtils.generateStringByString(filepathTemplate, webConfiguration, model);
        if (filepath.startsWith(Constants.SEPARATOR)) {
            filepath = filepath.substring(1);
        }
        return filepath;
    }

    /**
     * 创建静态化页面
     *
     * @param site
     * @param fullTemplatePath
     * @param filepath
     * @param lang
     * @param pageIndex
     * @param metadata
     * @param model
     * @param urlConsumer
     * @return static file path
     * @throws IOException
     * @throws TemplateException
     */
    public String createStaticFile(SysSite site, String fullTemplatePath, String filepath, String lang, Integer pageIndex,
            CmsPageMetadata metadata, Map<String, Object> model, Consumer<String> urlConsumer)
            throws IOException, TemplateException {
        if (CommonUtils.notEmpty(filepath)) {
            if (null == model) {
                model = new HashMap<>();
                AbstractFreemarkerView.exposeSite(model, site);
            }
            model.put("metadata", metadata);
            model.put(CommonConstants.DEFAULT_PAGEINDEX, pageIndex);
            String fullPath = CommonUtils.joinString(site.getSitePath(), filepath);
            model.put("url", fullPath);
            model.put("lang", lang);
            if (null != urlConsumer) {
                urlConsumer.accept(fullPath);
            }
            String staticFilePath;
            if (filepath.endsWith(Constants.SEPARATOR)) {
                staticFilePath = CommonUtils.joinString(filepath, CommonConstants.getDefaultPage());
            } else {
                staticFilePath = filepath;
            }
            if (CommonUtils.notEmpty(pageIndex) && 1 < pageIndex) {
                int index = staticFilePath.lastIndexOf(Constants.DOT);
                staticFilePath = CommonUtils.joinString(staticFilePath.substring(0, index), Constants.UNDERLINE, pageIndex,
                        staticFilePath.substring(index, staticFilePath.length()));
            }
            FreeMarkerUtils.generateFileByFile(fullTemplatePath, siteComponent.getWebFilePath(site.getId(), staticFilePath),
                    webConfiguration, model);
        }
        return filepath;
    }

    private void exposePlace(SysSite site, String templatePath, String lang, String defaultLang, CmsPlaceMetadata metadata,
            Map<String, Object> model) {
        if (null != metadata.getSize() && 0 < metadata.getSize()) {
            Date now = CommonUtils.getMinuteDate();
            PageHandler page = placeService.getPage(site.getId(), null, templatePath, null, null,
                    metadata.isEnablePlaceMultilingual() ? lang : null, defaultLang, null, now, now,
                    CmsPlaceService.STATUS_NORMAL_ARRAY, false, null, null, 1, metadata.getSize());
            @SuppressWarnings("unchecked")
            List<CmsPlace> list = (List<CmsPlace>) page.getList();
            if (null != list) {
                Long[] ids = list.stream().map(CmsPlace::getId).toArray(Long[]::new);
                List<CmsPlaceAttribute> attributeList = placeAttributeService.getEntitys(ids);
                Map<Long, CmsPlaceAttribute> attributeMap = CommonUtils.listToMap(attributeList, k -> k.getPlaceId());
                list.forEach(e -> {
                    Integer clicks = statisticsComponent.getPlaceClicks(e.getId());
                    if (null != clicks) {
                        e.setClicks(e.getClicks() + clicks);
                    }
                    CmsUrlUtils.initPlaceUrl(site, e);
                    fileUploadComponent.initPlaceCover(site, e);
                    e.setAttribute(ExtendUtils.getAttributeMap(attributeMap.get(e.getId())));
                });
            }
            model.put("page", page);
        }
        model.put("path", templatePath);
        model.put("lang", lang);
        model.put("metadata", metadata);
        AbstractFreemarkerView.exposeSite(model, site);
    }

    @Override
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
            templateComponent.publish(site, content, category, categoryModel);
        } catch (IOException | TemplateException e) {
            log.error(e.getMessage());
        }
    }
}
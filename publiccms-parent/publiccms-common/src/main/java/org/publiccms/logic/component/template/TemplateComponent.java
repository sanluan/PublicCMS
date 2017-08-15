package org.publiccms.logic.component.template;
import static com.publiccms.common.tools.CommonUtils.empty;
import static com.publiccms.common.tools.CommonUtils.getDate;
import static com.publiccms.common.tools.CommonUtils.notEmpty;
import static com.publiccms.common.tools.FreeMarkerUtils.generateFileByFile;
import static com.publiccms.common.tools.FreeMarkerUtils.generateStringByFile;
import static com.publiccms.common.tools.FreeMarkerUtils.generateStringByString;
import static org.apache.commons.lang3.StringUtils.splitByWholeSeparator;
import static org.apache.commons.logging.LogFactory.getLog;
import static org.publiccms.common.base.AbstractFreemarkerView.CONTEXT_SITE;
import static org.publiccms.common.base.AbstractFreemarkerView.exposeSite;
import static org.publiccms.common.constants.CommonConstants.getDefaultPageBreakTag;
import static org.publiccms.common.tools.ExtendUtils.getExtendMap;
import static org.publiccms.logic.component.site.SiteComponent.getFullFileName;
import static org.publiccms.logic.component.template.TemplateCacheComponent.CONTENT_CACHE;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.logging.Log;
import org.publiccms.common.api.Cache;
import org.publiccms.common.base.AbstractTaskDirective;
import org.publiccms.common.base.AbstractTemplateDirective;
import org.publiccms.entities.cms.CmsCategory;
import org.publiccms.entities.cms.CmsCategoryAttribute;
import org.publiccms.entities.cms.CmsCategoryModel;
import org.publiccms.entities.cms.CmsCategoryModelId;
import org.publiccms.entities.cms.CmsContent;
import org.publiccms.entities.cms.CmsContentAttribute;
import org.publiccms.entities.sys.SysSite;
import org.publiccms.logic.component.site.DirectiveComponent;
import org.publiccms.logic.component.site.SiteComponent;
import org.publiccms.logic.service.cms.CmsCategoryAttributeService;
import org.publiccms.logic.service.cms.CmsCategoryModelService;
import org.publiccms.logic.service.cms.CmsCategoryService;
import org.publiccms.logic.service.cms.CmsContentAttributeService;
import org.publiccms.logic.service.cms.CmsContentService;
import org.publiccms.logic.service.cms.CmsPlaceService;
import org.publiccms.views.pojo.CmsPageMetadata;
import org.publiccms.views.pojo.CmsPlaceMetadata;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import com.publiccms.common.handler.PageHandler;

import freemarker.template.Configuration;
import freemarker.template.SimpleHash;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModelException;

/**
 * 模板处理组件 Template Component
 *
 */
public class TemplateComponent implements Cache {
    protected final Log log = getLog(getClass());
    /**
     * 包含目录 include directory
     */
    public static String INCLUDE_DIRECTORY = "include";

    private String directivePrefix;

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

    /**
     * 创建静态化页面
     * 
     * @param site
     * @param templatePath
     * @param filePath
     * @param pageIndex
     * @param metadata
     * @param model
     * @return
     * @throws IOException
     * @throws TemplateException
     */
    public String createStaticFile(SysSite site, String templatePath, String filePath, Integer pageIndex,
            CmsPageMetadata metadata, Map<String, Object> model) throws IOException, TemplateException {
        if (notEmpty(filePath)) {
            if (null == model) {
                model = new HashMap<>();
            }
            if (null == metadata) {
                metadata = metadataComponent.getTemplateMetadata(siteComponent.getWebTemplateFilePath() + templatePath);
            }
            model.put("metadata", metadata);
            exposeSite(model, site);
            filePath = generateStringByString(filePath, webConfiguration, model);
            model.put("url", site.getSitePath() + filePath);
            if (notEmpty(pageIndex) && 1 < pageIndex) {
                int index = filePath.lastIndexOf('.');
                filePath = filePath.substring(0, index) + '_' + pageIndex + filePath.substring(index, filePath.length());
            }
            generateFileByFile(templatePath, siteComponent.getWebFilePath(site, filePath), webConfiguration, model);
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
     * @return
     */
    public boolean createContentFile(SysSite site, CmsContent entity, CmsCategory category, CmsCategoryModel categoryModel) {
        if (null != site && null != entity) {
            if (null == category) {
                category = categoryService.getEntity(entity.getCategoryId());
            }
            if (null == categoryModel) {
                categoryModel = categoryModelService
                        .getEntity(new CmsCategoryModelId(entity.getCategoryId(), entity.getModelId()));
            }
            if (null != categoryModel && null != category && !entity.isOnlyUrl()) {
                try {
                    if (site.isUseStatic() && notEmpty(categoryModel.getTemplatePath())) {
                        String url = site.getSitePath() + createContentFile(site, entity, category, true,
                                getFullFileName(site, categoryModel.getTemplatePath()), null, null);
                        contentService.updateUrl(entity.getId(), url, true);
                    } else {
                        Map<String, Object> model = new HashMap<>();
                        model.put("content", entity);
                        model.put("category", category);
                        model.put(CONTEXT_SITE, site);
                        String url = site.getDynamicPath()
                                + generateStringByString(category.getContentPath(), webConfiguration, model);
                        contentService.updateUrl(entity.getId(), url, false);
                    }
                    return true;
                } catch (IOException | TemplateException e) {
                    log.error(e.getMessage());
                    return false;
                }
            }
        }
        return false;
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
     * @return
     * @throws IOException
     * @throws TemplateException
     */
    public String createContentFile(SysSite site, CmsContent entity, CmsCategory category, boolean createMultiContentPage,
            String templatePath, String filePath, Integer pageIndex) throws IOException, TemplateException {
        Map<String, Object> model = new HashMap<>();
        model.put("content", entity);
        model.put("category", category);

        CmsContentAttribute attribute = contentAttributeService.getEntity(entity.getId());
        if (null != attribute) {
            Map<String, String> map = getExtendMap(attribute.getData());
            map.put("text", attribute.getText());
            map.put("source", attribute.getSource());
            map.put("sourceUrl", attribute.getSourceUrl());
            map.put("wordCount", String.valueOf(attribute.getWordCount()));
            model.put("attribute", map);
        } else {
            model.put("attribute", attribute);
        }
        if (empty(filePath)) {
            filePath = category.getContentPath();
        }

        if (null != attribute && notEmpty(attribute.getText())) {
            String[] texts = splitByWholeSeparator(attribute.getText(), getDefaultPageBreakTag());
            if (createMultiContentPage) {
                for (int i = 1; i < texts.length; i++) {
                    PageHandler page = new PageHandler(i + 1, 1, texts.length, null);
                    model.put("text", texts[i]);
                    model.put("page", page);
                    createStaticFile(site, templatePath, filePath, i + 1, null, model);
                }
                PageHandler page = new PageHandler(pageIndex, 1, texts.length, null);
                model.put("page", page);
                model.put("text", texts[page.getPageIndex() - 1]);
            } else {
                PageHandler page = new PageHandler(pageIndex, 1, texts.length, null);
                model.put("page", page);
                model.put("text", texts[page.getPageIndex() - 1]);
            }
        }
        return createStaticFile(site, templatePath, filePath, 1, null, model);
    }

    /**
     * 分类页面静态化
     * 
     * @param site
     * @param entity
     * @param pageIndex
     * @param totalPage
     * @return
     */
    public boolean createCategoryFile(SysSite site, CmsCategory entity, Integer pageIndex, Integer totalPage) {
        if (entity.isOnlyUrl()) {
            categoryService.updateUrl(entity.getId(), entity.getPath(), false);
        } else if (notEmpty(entity.getPath())) {
            try {
                if (site.isUseStatic() && notEmpty(entity.getTemplatePath())) {
                    String url = site.getSitePath() + createCategoryFile(site, entity,
                            getFullFileName(site, entity.getTemplatePath()), entity.getPath(), pageIndex, totalPage);
                    categoryService.updateUrl(entity.getId(), url, true);
                } else {
                    Map<String, Object> model = new HashMap<>();
                    model.put("category", entity);
                    model.put(CONTEXT_SITE, site);
                    String url = site.getDynamicPath() + generateStringByString(entity.getPath(), webConfiguration, model);
                    categoryService.updateUrl(entity.getId(), url, false);
                }
            } catch (IOException | TemplateException e) {
                log.error(e.getMessage());
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
     * @return
     * @throws IOException
     * @throws TemplateException
     */
    public String createCategoryFile(SysSite site, CmsCategory entity, String templatePath, String filePath, Integer pageIndex,
            Integer totalPage) throws IOException, TemplateException {
        Map<String, Object> model = new HashMap<>();
        if (empty(pageIndex)) {
            pageIndex = 1;
        }
        model.put("category", entity);
        CmsCategoryAttribute attribute = categoryAttributeService.getEntity(entity.getId());
        if (null != attribute) {
            Map<String, String> map = getExtendMap(attribute.getData());
            map.put("title", attribute.getTitle());
            map.put("keywords", attribute.getKeywords());
            map.put("description", attribute.getDescription());
            model.put("attribute", map);
        } else {
            model.put("attribute", attribute);
        }

        if (notEmpty(totalPage) && pageIndex + 1 <= totalPage) {
            for (int i = pageIndex + 1; i <= totalPage; i++) {
                model.put("pageIndex", i);
                createStaticFile(site, templatePath, filePath, i, null, model);
            }
        }

        model.put("pageIndex", pageIndex);
        return createStaticFile(site, templatePath, filePath, 1, null, model);
    }

    private void exposePlace(SysSite site, String templatePath, CmsPlaceMetadata metadata, Map<String, Object> model) {
        int pageSize = 10;
        if (notEmpty(metadata.getSize())) {
            pageSize = metadata.getSize();
        }
        if (pageSize > 0) {
            model.put("page", placeService.getPage(site.getId(), null, templatePath, null, null, null, getDate(),
                    CmsPlaceService.STATUS_NORMAL, false, null, null, 1, pageSize));
        }
        model.put("metadata", metadata);
        exposeSite(model, site);
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
        if (notEmpty(templatePath)) {
            Map<String, Object> model = new HashMap<>();
            exposePlace(site, templatePath, metadata, model);
            String placeTemplatePath = INCLUDE_DIRECTORY + templatePath;
            generateFileByFile(getFullFileName(site, placeTemplatePath), siteComponent.getWebFilePath(site, placeTemplatePath),
                    webConfiguration, model);
        }
    }

    /**
     * 输出页面片段
     * 
     * @param site
     * @param templatePath
     * @param metadata
     * @return
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
        if (notEmpty(templatePath)) {
            Map<String, Object> model = new HashMap<>();
            exposePlace(site, templatePath, metadata, model);
            generateStringByFile(writer, getFullFileName(site, INCLUDE_DIRECTORY + templatePath), webConfiguration, model);
        }
    }

    @Autowired
    private void init(FreeMarkerConfigurer freeMarkerConfigurer, DirectiveComponent directiveComponent)
            throws IOException, TemplateModelException {
        Map<String, Object> freemarkerVariables = new HashMap<>();
        adminConfiguration = freeMarkerConfigurer.getConfiguration();
        for (Entry<String, AbstractTemplateDirective> entry : directiveComponent.getTemplateDirectiveMap().entrySet()) {
            freemarkerVariables.put(directivePrefix + entry.getKey(), entry.getValue());
        }
        freemarkerVariables.putAll(directiveComponent.getMethodMap());
        adminConfiguration.setAllSharedVariables(new SimpleHash(freemarkerVariables, adminConfiguration.getObjectWrapper()));

        webConfiguration = new Configuration(Configuration.getVersion());
        File webFile = new File(siteComponent.getWebTemplateFilePath());
        webFile.mkdirs();
        webConfiguration.setDirectoryForTemplateLoading(webFile);
        copyConfig(adminConfiguration, webConfiguration);
        Map<String, Object> webFreemarkerVariables = new HashMap<String, Object>(freemarkerVariables);
        webFreemarkerVariables.put(CONTENT_CACHE, new NoCacheDirective());
        webConfiguration.setAllSharedVariables(new SimpleHash(webFreemarkerVariables, webConfiguration.getObjectWrapper()));

        taskConfiguration = new Configuration(Configuration.getVersion());
        File taskFile = new File(siteComponent.getTaskTemplateFilePath());
        taskFile.mkdirs();
        taskConfiguration.setDirectoryForTemplateLoading(taskFile);
        copyConfig(adminConfiguration, taskConfiguration);
        for (Entry<String, AbstractTaskDirective> entry : directiveComponent.getTaskDirectiveMap().entrySet()) {
            freemarkerVariables.put(directivePrefix + entry.getKey(), entry.getValue());
        }
        taskConfiguration.setAllSharedVariables(new SimpleHash(freemarkerVariables, taskConfiguration.getObjectWrapper()));
    }

    private void copyConfig(Configuration source, Configuration target) {
        target.setNewBuiltinClassResolver(source.getNewBuiltinClassResolver());
        target.setTemplateUpdateDelayMilliseconds(source.getTemplateUpdateDelayMilliseconds());
        target.setDefaultEncoding(source.getDefaultEncoding());
        target.setLocale(source.getLocale());
        target.setBooleanFormat(source.getBooleanFormat());
        target.setDateTimeFormat(source.getDateTimeFormat());
        target.setDateFormat(source.getDateFormat());
        target.setTimeFormat(source.getTimeFormat());
        target.setNumberFormat(source.getNumberFormat());
        target.setOutputFormat(source.getOutputFormat());
        target.setURLEscapingCharset(source.getURLEscapingCharset());
        target.setLazyAutoImports(source.getLazyAutoImports());
        target.setTemplateExceptionHandler(source.getTemplateExceptionHandler());
    }

    @Override
    public void clear() {
        adminConfiguration.clearTemplateCache();
        clearTemplateCache();
    }

    /**
     * 清理模板缓存
     * 
     * Clear Template Cache
     */
    public void clearTemplateCache() {
        webConfiguration.clearTemplateCache();
        taskConfiguration.clearTemplateCache();
    }

    /**
     * 获取FreeMarker前台配置
     * 
     * Get FreeMarker Web Configuration
     * 
     * @return
     */
    public Configuration getWebConfiguration() {
        return webConfiguration;
    }

    /**
     * 获取FreeMarker任务计划配置
     * 
     * Get FreeMarker Task Configuration
     * 
     * @return
     */
    public Configuration getTaskConfiguration() {
        return taskConfiguration;
    }

    /**
     * 获取FreeMarker管理后台配置 
     * 
     * Get FreeMarker Admin Configuration
     * 
     * @return
     */
    public Configuration getAdminConfiguration() {
        return adminConfiguration;
    }

    /**
     * 设置指令前缀
     * 
     * Set Directive Prefix
     * 
     * @param directivePrefix
     */
    public void setDirectivePrefix(String directivePrefix) {
        this.directivePrefix = directivePrefix;
    }
}

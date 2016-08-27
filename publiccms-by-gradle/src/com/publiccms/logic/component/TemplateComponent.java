package com.publiccms.logic.component;

import static com.publiccms.common.constants.CommonConstants.getDefaultPageBreakTag;
import static com.publiccms.common.tools.ExtendUtils.getExtendMap;
import static com.publiccms.logic.component.SiteComponent.CONTEXT_SITE;
import static com.publiccms.logic.component.SiteComponent.expose;
import static com.publiccms.logic.component.SiteComponent.getFullFileName;
import static com.publiccms.logic.component.TemplateCacheComponent.CONTENT_CACHE;
import static com.sanluan.common.tools.FreeMarkerUtils.makeFileByFile;
import static com.sanluan.common.tools.FreeMarkerUtils.makeStringByString;
import static com.sanluan.common.tools.FreeMarkerUtils.makeStringByFile;
import static org.apache.commons.lang3.StringUtils.splitByWholeSeparator;
import static org.apache.commons.lang3.StringUtils.uncapitalize;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import com.publiccms.common.base.AbstractTaskDirective;
import com.publiccms.common.base.AbstractTemplateDirective;
import com.publiccms.common.spi.Cacheable;
import com.publiccms.entities.cms.CmsCategory;
import com.publiccms.entities.cms.CmsCategoryAttribute;
import com.publiccms.entities.cms.CmsCategoryModel;
import com.publiccms.entities.cms.CmsContent;
import com.publiccms.entities.cms.CmsContentAttribute;
import com.publiccms.entities.sys.SysSite;
import com.publiccms.logic.service.cms.CmsCategoryAttributeService;
import com.publiccms.logic.service.cms.CmsCategoryModelService;
import com.publiccms.logic.service.cms.CmsCategoryService;
import com.publiccms.logic.service.cms.CmsContentAttributeService;
import com.publiccms.logic.service.cms.CmsContentService;
import com.publiccms.logic.service.cms.CmsPlaceService;
import com.publiccms.views.pojo.CmsPageMetadata;
import com.publiccms.views.pojo.CmsPlaceMetadata;
import com.sanluan.common.base.Base;
import com.sanluan.common.handler.PageHandler;

import freemarker.template.Configuration;
import freemarker.template.SimpleHash;
import freemarker.template.TemplateException;
import freemarker.template.TemplateMethodModelEx;
import freemarker.template.TemplateModelException;

/**
 * 
 * TemplateComponent 模板处理组件
 *
 */
public class TemplateComponent extends Base implements Cacheable {
	public static String INCLUDE_DIRECTORY = "include";

	private String directivePrefix;
	private String directiveRemoveRegex;
	private String methodRemoveRegex;
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
	 * @param templatePath
	 * @param filePath
	 * @param model
	 * @return
	 * @throws TemplateException
	 * @throws IOException
	 */
	public String createStaticFile(SysSite site, String templatePath, String filePath, Integer pageIndex,
			CmsPageMetadata metadata, Map<String, Object> model) throws IOException, TemplateException {
		if (notEmpty(filePath)) {
			if (empty(model)) {
				model = new HashMap<String, Object>();
			}
			if (empty(metadata)) {
				metadata = metadataComponent.getTemplateMetadata(siteComponent.getWebTemplateFilePath() + templatePath);
			}
			model.put("metadata", metadata);
			expose(model, site);
			filePath = makeStringByString(filePath, webConfiguration, model);
			model.put("url", site.getSitePath() + filePath);
			if (notEmpty(pageIndex) && 1 < pageIndex) {
				int index = filePath.lastIndexOf('.');
				filePath = filePath.substring(0, index) + '_' + pageIndex
						+ filePath.substring(index, filePath.length());
			}
			makeFileByFile(templatePath, siteComponent.getStaticFilePath(site, filePath), webConfiguration, model);
		}
		return filePath;
	}

	/**
	 * 内容页面静态化
	 * 
	 * @param entity
	 * @param category
	 * @param templatePath
	 * @return
	 * @throws IOException
	 * @throws TemplateException
	 */
	public boolean createContentFile(SysSite site, CmsContent entity, CmsCategory category,
			CmsCategoryModel categoryModel) {
		if (notEmpty(site) && notEmpty(entity)) {
			if (empty(category)) {
				category = categoryService.getEntity(entity.getCategoryId());
			}
			if (empty(categoryModel)) {
				categoryModel = categoryModelService.getEntity(entity.getModelId(), entity.getCategoryId());
			}
			if (notEmpty(categoryModel) && notEmpty(category) && !entity.isOnlyUrl()) {
				try {
					if (site.isUseStatic() && notEmpty(categoryModel.getTemplatePath())) {
						String url = site.getSitePath() + createContentFile(site, entity, category, true,
								getFullFileName(site, categoryModel.getTemplatePath()), null, null);
						contentService.updateUrl(entity.getId(), url, true);
					} else {
						Map<String, Object> model = new HashMap<String, Object>();
						model.put("content", entity);
						model.put("category", category);
						model.put(CONTEXT_SITE, site);
						String url = site.getDynamicPath()
								+ makeStringByString(category.getContentPath(), webConfiguration, model);
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
	 * @param entity
	 * @param category
	 * @param templatePath
	 * @param filePath
	 * @return
	 * @throws IOException
	 * @throws TemplateException
	 */
	public String createContentFile(SysSite site, CmsContent entity, CmsCategory category,
			boolean createMultiContentPage, String templatePath, String filePath, Integer pageIndex)
			throws IOException, TemplateException {
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("content", entity);
		model.put("category", category);

		CmsContentAttribute attribute = contentAttributeService.getEntity(entity.getId());
		if (notEmpty(attribute)) {
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

		if (notEmpty(attribute) && notEmpty(attribute.getText())) {
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
	 * @param entity
	 * @param templatePath
	 * @param filePath
	 * @param pageIndex
	 * @param totalPage
	 * @return
	 * @throws IOException
	 * @throws TemplateException
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
					Map<String, Object> model = new HashMap<String, Object>();
					model.put("category", entity);
					model.put(CONTEXT_SITE, site);
					String url = site.getDynamicPath() + makeStringByString(entity.getPath(), webConfiguration, model);
					categoryService.updateUrl(entity.getId(), url, false);
				}
			} catch (IOException | TemplateException e) {
				return false;
			}
			return true;
		}
		return false;

	}

	/**
	 * 分类页面静态化
	 * 
	 * @param entity
	 * @param templatePath
	 * @param filePath
	 * @param pageIndex
	 * @param totalPage
	 * @return
	 * @throws IOException
	 * @throws TemplateException
	 */
	public String createCategoryFile(SysSite site, CmsCategory entity, String templatePath, String filePath,
			Integer pageIndex, Integer totalPage) throws IOException, TemplateException {
		Map<String, Object> model = new HashMap<String, Object>();
		if (empty(pageIndex)) {
			pageIndex = 1;
		}
		model.put("category", entity);
		CmsCategoryAttribute attribute = categoryAttributeService.getEntity(entity.getId());
		if (notEmpty(attribute)) {
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
		expose(model, site);
	}

	/**
	 * 静态化页面片段
	 * 
	 * @param filePath
	 * @return
	 * @throws TemplateException
	 * @throws IOException
	 */
	public void staticPlace(SysSite site, String templatePath, CmsPlaceMetadata metadata)
			throws IOException, TemplateException {
		if (notEmpty(templatePath)) {
			Map<String, Object> model = new HashMap<String, Object>();
			exposePlace(site, templatePath, metadata, model);
			String placeTemplatePath = INCLUDE_DIRECTORY + templatePath;
			makeFileByFile(getFullFileName(site, placeTemplatePath),
					siteComponent.getStaticFilePath(site, placeTemplatePath), webConfiguration, model);
		}
	}

	/**
	 * 输出页面片段
	 * 
	 * @param filePath
	 * @return
	 * @throws TemplateException
	 * @throws IOException
	 */
	public String printPlace(SysSite site, String templatePath, CmsPlaceMetadata metadata)
			throws IOException, TemplateException {
		if (notEmpty(templatePath)) {
			Map<String, Object> model = new HashMap<String, Object>();
			exposePlace(site, templatePath, metadata, model);
			return makeStringByFile(getFullFileName(site, INCLUDE_DIRECTORY + templatePath), webConfiguration, model);
		}
		return "";
	}

	@Autowired(required = false)
	private void setFreeMarkerConfigurer(FreeMarkerConfigurer freeMarkerConfigurer,
			Map<String, AbstractTaskDirective> taskDirectiveMap,
			Map<String, AbstractTemplateDirective> templateDirectiveMap, Map<String, TemplateMethodModelEx> methodMap)
			throws IOException, TemplateModelException {
		Map<String, Object> freemarkerVariables = new HashMap<String, Object>();
		adminConfiguration = freeMarkerConfigurer.getConfiguration();
		log.info("Freemarker directives and methods Handler started");

		StringBuffer templateDirectives = new StringBuffer();
		for (Entry<String, AbstractTemplateDirective> entry : templateDirectiveMap.entrySet()) {
			String directiveName = directivePrefix
					+ uncapitalize(entry.getKey().replaceAll(directiveRemoveRegex, BLANK));
			freemarkerVariables.put(directiveName, entry.getValue());
			if (0 != templateDirectives.length()) {
				templateDirectives.append(COMMA_DELIMITED);
			}
			templateDirectives.append(directiveName);
		}
		StringBuffer methods = new StringBuffer();
		for (Entry<String, TemplateMethodModelEx> entry : methodMap.entrySet()) {
			String methodName = uncapitalize(entry.getKey().replaceAll(methodRemoveRegex, BLANK));
			freemarkerVariables.put(methodName, entry.getValue());
			if (0 != methods.length()) {
				methods.append(COMMA_DELIMITED);
			}
			methods.append(methodName);
		}
		adminConfiguration
				.setAllSharedVariables(new SimpleHash(freemarkerVariables, adminConfiguration.getObjectWrapper()));
		log.info(templateDirectiveMap.size() + " template directives created:[" + templateDirectives.toString() + "];");
		log.info(methodMap.size() + " methods created:[" + methods.toString() + "];");

		webConfiguration = new Configuration(Configuration.getVersion());
		File webFile = new File(siteComponent.getWebTemplateFilePath());
		webFile.mkdirs();
		webConfiguration.setDirectoryForTemplateLoading(webFile);
		copyConfig(adminConfiguration, webConfiguration);
		Map<String, Object> webFreemarkerVariables = new HashMap<String, Object>(freemarkerVariables);
		webFreemarkerVariables.put(CONTENT_CACHE, new NoCacheDirective());
		webConfiguration
				.setAllSharedVariables(new SimpleHash(webFreemarkerVariables, webConfiguration.getObjectWrapper()));

		taskConfiguration = new Configuration(Configuration.getVersion());
		File taskFile = new File(siteComponent.getTaskTemplateFilePath());
		taskFile.mkdirs();
		taskConfiguration.setDirectoryForTemplateLoading(taskFile);
		copyConfig(adminConfiguration, taskConfiguration);

		StringBuffer taskDirectives = new StringBuffer();
		for (Entry<String, AbstractTaskDirective> entry : taskDirectiveMap.entrySet()) {
			String directiveName = directivePrefix
					+ uncapitalize(entry.getKey().replaceAll(directiveRemoveRegex, BLANK));
			freemarkerVariables.put(directiveName, entry.getValue());
			if (0 != taskDirectives.length()) {
				taskDirectives.append(COMMA_DELIMITED);
			}
			taskDirectives.append(directiveName);
		}

		taskConfiguration
				.setAllSharedVariables(new SimpleHash(freemarkerVariables, taskConfiguration.getObjectWrapper()));
		log.info((taskDirectiveMap.size()) + " task directives created:[" + taskDirectives.toString() + "];");
	}

	private void copyConfig(Configuration source, Configuration target) {
		target.setBooleanFormat(source.getBooleanFormat());
		target.setDateTimeFormat(source.getDateTimeFormat());
		target.setDateFormat(source.getDateFormat());
		target.setTimeFormat(source.getTimeFormat());
		target.setDefaultEncoding(source.getDefaultEncoding());
		target.setURLEscapingCharset(source.getURLEscapingCharset());
		target.setTemplateUpdateDelayMilliseconds(source.getTemplateUpdateDelayMilliseconds());
		target.setLocale(source.getLocale());
		target.setNewBuiltinClassResolver(source.getNewBuiltinClassResolver());
		target.setNumberFormat(source.getNumberFormat());
		target.setTemplateExceptionHandler(source.getTemplateExceptionHandler());
	}

	@Override
	public void clear() {
		adminConfiguration.clearTemplateCache();
		webConfiguration.clearTemplateCache();
		taskConfiguration.clearTemplateCache();
	}

	public void setDirectivePrefix(String directivePrefix) {
		this.directivePrefix = directivePrefix;
	}

	public void setDirectiveRemoveRegex(String directiveRemoveRegex) {
		this.directiveRemoveRegex = directiveRemoveRegex;
	}

	public void setMethodRemoveRegex(String methodRemoveRegex) {
		this.methodRemoveRegex = methodRemoveRegex;
	}

	public String getDirectiveRemoveRegex() {
		return directiveRemoveRegex;
	}

	public Configuration getWebConfiguration() {
		return webConfiguration;
	}

	public Configuration getTaskConfiguration() {
		return taskConfiguration;
	}
}

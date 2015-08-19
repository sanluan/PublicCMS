package com.sanluan.common.handler;

import static org.apache.commons.logging.LogFactory.getLog;
import static org.springframework.util.StringUtils.uncapitalize;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import com.sanluan.common.tools.MyClassUtils;

import freemarker.template.SimpleHash;
import freemarker.template.TemplateDirectiveModel;
import freemarker.template.TemplateMethodModelEx;
import freemarker.template.TemplateModelException;

public class FreeMarkerExtendHandler implements ApplicationContextAware {
	private String directiveBasePackage;
	private String methodBasePackage;
	private String directivePrefix;
	private String directiveRemoveRegex;
	private String methodRemoveRegex;
	private Map<String, Object> freemarkerVariables = new HashMap<String, Object>();
	private final Log log = getLog(getClass());

	@Override
	public void setApplicationContext(ApplicationContext applicationcontext) throws BeansException {
		log.info("Freemarker directives and methods Handler started");
		FreeMarkerConfigurer freeMarkerConfigurer = applicationcontext.getBean(FreeMarkerConfigurer.class);

		StringBuffer directives = new StringBuffer();
		List<Class<?>> directiveClasses = MyClassUtils.getAllAssignedClass(TemplateDirectiveModel.class, new String[]{directiveBasePackage});
		for (Class<?> c : directiveClasses) {
			String directiveName = directivePrefix + uncapitalize(c.getSimpleName().replaceAll(directiveRemoveRegex, ""));
			freemarkerVariables.put(directiveName, applicationcontext.getBean(c));
			if (0 != directives.length())
				directives.append(",");
			directives.append(directiveName);
		}

		List<Class<?>> methodClasses = MyClassUtils.getAllAssignedClass(TemplateMethodModelEx.class, new String[]{methodBasePackage});
		StringBuffer methods = new StringBuffer();
		for (Class<?> c : methodClasses) {
			String methodName = uncapitalize(c.getSimpleName().replaceAll(methodRemoveRegex, ""));
			freemarkerVariables.put(methodName, applicationcontext.getBean(c));
			if (0 != methods.length())
				methods.append(",");
			methods.append(methodName);
		}

		try {
			freeMarkerConfigurer.getConfiguration().setAllSharedVariables(
					new SimpleHash(freemarkerVariables, freeMarkerConfigurer.getConfiguration().getObjectWrapper()));
			log.info((directiveClasses.size()) + " directives created:[" + directives.toString() + "];" + methodClasses.size()
					+ " methods created:[" + methods.toString() + "]");
		} catch (TemplateModelException e) {
		}
	}

	/**
	 * @param directiveBasePackage
	 *            the directiveBasePackage to set
	 */
	public void setDirectiveBasePackage(String directiveBasePackage) {
		this.directiveBasePackage = directiveBasePackage;
	}

	/**
	 * @param methodBasePackage
	 *            the methodBasePackage to set
	 */
	public void setMethodBasePackage(String methodBasePackage) {
		this.methodBasePackage = methodBasePackage;
	}

	/**
	 * @param directivePrefix
	 *            the directivePrefix to set
	 */
	public void setDirectivePrefix(String directivePrefix) {
		this.directivePrefix = directivePrefix;
	}

	/**
	 * @param directiveRemoveRegex
	 *            the directiveRemoveRegex to set
	 */
	public void setDirectiveRemoveRegex(String directiveRemoveRegex) {
		this.directiveRemoveRegex = directiveRemoveRegex;
	}

	/**
	 * @param methodRemoveRegex
	 *            the methodRemoveRegex to set
	 */
	public void setMethodRemoveRegex(String methodRemoveRegex) {
		this.methodRemoveRegex = methodRemoveRegex;
	}

	public Map<String, Object> getFreemarkerVariables() {
		return freemarkerVariables;
	}

	public void setFreemarkerVariables(Map<String, Object> freemarkerVariables) {
		this.freemarkerVariables = freemarkerVariables;
	}

	public String getDirectivePrefix() {
		return directivePrefix;
	}
}

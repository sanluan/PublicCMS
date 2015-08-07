package com.sanluan.common.base;

import java.io.IOException;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;

import com.sanluan.common.handler.DirectiveHandler;
import com.sanluan.common.handler.HttpParameterHandler;
import com.sanluan.common.handler.RenderHandler;
import com.sanluan.common.servlet.HttpServletDirective;

import freemarker.core.Environment;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateDirectiveModel;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;

public abstract class BaseDirective implements TemplateDirectiveModel, HttpServletDirective {
	@SuppressWarnings("unchecked")
	@Override
	public void execute(Environment environment, @SuppressWarnings("rawtypes") Map parameters, TemplateModel[] loopVars,
			TemplateDirectiveBody templateDirectiveBody) throws TemplateException, IOException {
		try {
			execute(new DirectiveHandler(parameters, loopVars, environment.getOut(), environment.getCurrentNamespace(),
					environment.getObjectWrapper(), templateDirectiveBody));
		} catch (IOException e) {
			throw e;
		} catch (Exception e) {
			throw new TemplateException(e, environment);
		}
	}

	@Override
	public void execute(HttpMessageConverter<Object> httpMessageConverter, MediaType mediaType,
			Map<String, String[]> parameterMap, String callback, HttpServletResponse response) throws IOException, Exception {
		execute(new HttpParameterHandler(httpMessageConverter, mediaType, parameterMap, callback, response));
	}

	public abstract void execute(RenderHandler handler) throws IOException, Exception;
}

package com.sanluan.common.base;

import java.io.IOException;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;

import com.sanluan.common.handler.HttpParameterHandler;
import com.sanluan.common.servlet.HttpServletDirective;

public abstract class BaseDirective extends BaseTemplateDirective implements HttpServletDirective {
	@Override
	public void execute(HttpMessageConverter<Object> httpMessageConverter, MediaType mediaType,
			Map<String, String[]> parameterMap, String callback, HttpServletResponse response) throws IOException, Exception {
		execute(new HttpParameterHandler(httpMessageConverter, mediaType, parameterMap, callback, response));
	}

}

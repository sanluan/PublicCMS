package com.publiccms.common.directive;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;

import freemarker.template.TemplateException;

/**
 * 
 * HttpServletDirective 接口指令
 *
 */
public interface HttpDirective {
    /**
     * @param httpMessageConverter
     * @param mediaType
     * @param request
     * @param response
     * @throws IOException
     * @throws TemplateException
     */
    void execute(HttpMessageConverter<Object> httpMessageConverter, MediaType mediaType, HttpServletRequest request,
            HttpServletResponse response) throws IOException, TemplateException;
}

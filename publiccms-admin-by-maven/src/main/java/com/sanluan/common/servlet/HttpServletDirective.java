package com.sanluan.common.servlet;

import java.io.IOException;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;

/**
 * 
 * HttpServletDirective 接口指令
 *
 */
public interface HttpServletDirective {
    /**
     * @param httpMessageConverter
     * @param mediaType
     * @param parameterMap
     * @param callback
     * @param response
     * @throws IOException
     * @throws Exception
     */
    public void execute(HttpMessageConverter<Object> httpMessageConverter, MediaType mediaType,
            Map<String, String[]> parameterMap, String callback, HttpServletResponse response) throws IOException, Exception;
}

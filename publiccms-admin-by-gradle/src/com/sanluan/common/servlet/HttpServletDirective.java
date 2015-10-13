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
<<<<<<< HEAD
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
=======
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
>>>>>>> b7117fb2de906a985a5be5015f24f8c6b6b5a315
}

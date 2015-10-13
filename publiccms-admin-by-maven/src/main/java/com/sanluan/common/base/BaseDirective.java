package com.sanluan.common.base;

import static org.apache.commons.logging.LogFactory.getLog;

import java.io.IOException;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;

import com.sanluan.common.handler.HttpParameterHandler;
import com.sanluan.common.servlet.HttpServletDirective;

/**
 * 
 * BaseDirective 自定义模板指令，接口指令基类
 *
 */
public abstract class BaseDirective extends BaseTemplateDirective implements HttpServletDirective {
    protected final Log log = getLog(getClass());
    @Override
    public void execute(HttpMessageConverter<Object> httpMessageConverter, MediaType mediaType,
            Map<String, String[]> parameterMap, String callback, HttpServletResponse response) throws IOException, Exception {
        execute(new HttpParameterHandler(httpMessageConverter, mediaType, parameterMap, callback, response));
    }

}

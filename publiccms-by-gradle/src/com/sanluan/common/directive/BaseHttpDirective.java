package com.sanluan.common.directive;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;

import com.sanluan.common.handler.HttpParameterHandler;

/**
 * 
 * BaseDirective 自定义模板指令，接口指令基类
 *
 */
public abstract class BaseHttpDirective extends BaseDirective implements HttpDirective {

    @Override
    public void execute(HttpMessageConverter<Object> httpMessageConverter, MediaType mediaType, HttpServletRequest request,
            String callback, HttpServletResponse response) throws IOException, Exception {
        execute(new HttpParameterHandler(httpMessageConverter, mediaType, request, callback, response));
    }
}
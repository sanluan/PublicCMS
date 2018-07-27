package com.publiccms.common.directive;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;

import com.publiccms.common.handler.HttpParameterHandler;

/**
 * 指令基类
 * 
 * Base directive class 
 *
 */
public abstract class BaseHttpDirective implements HttpDirective, Directive {
    private String name;
    
    @Override
    public void execute(HttpMessageConverter<Object> httpMessageConverter, MediaType mediaType, HttpServletRequest request,
            HttpServletResponse response) throws IOException, Exception {
        execute(new HttpParameterHandler(httpMessageConverter, mediaType, request, response));
    }

    public String getName() {
        return this.name;
    }

    /**
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }
}
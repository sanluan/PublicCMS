package com.publiccms.common.base;

import static org.apache.commons.logging.LogFactory.getLog;

import org.apache.commons.logging.Log;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import org.springframework.web.util.UrlPathHelper;

/**
 * 
 * BaseInterceptor 拦截器基类
 *
 */
public abstract class BaseInterceptor extends HandlerInterceptorAdapter {
    protected UrlPathHelper urlPathHelper = new UrlPathHelper();
    protected final Log log = getLog(getClass());
}

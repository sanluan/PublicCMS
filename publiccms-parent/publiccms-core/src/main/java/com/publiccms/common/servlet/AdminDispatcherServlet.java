package com.publiccms.common.servlet;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.web.HttpRequestHandler;
import org.springframework.web.context.WebApplicationContext;

import com.publiccms.common.constants.CmsVersion;

/**
 * 
 * AdminDispatcherServlet
 *
 */
public class AdminDispatcherServlet extends CommonDispatcherServlet {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private HttpRequestHandler installHandler;

    /**
     * @param applicationContext
     * @param installHandler
     */
    public AdminDispatcherServlet(WebApplicationContext applicationContext, HttpRequestHandler installHandler) {
        super(applicationContext);
        this.installHandler = installHandler;
    }

    @Override
    protected void doService(HttpServletRequest request, HttpServletResponse response) throws Exception {
        if (CmsVersion.isInitialized()) {
            super.doService(request, response);
        } else {
            installHandler.handleRequest(request, response);
        }
    }
}

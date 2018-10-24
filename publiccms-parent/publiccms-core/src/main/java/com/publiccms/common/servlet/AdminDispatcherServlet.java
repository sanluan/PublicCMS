package com.publiccms.common.servlet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.HttpRequestHandler;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.ModelAndView;

import com.publiccms.common.constants.CmsVersion;

/**
 * 
 * AdminDispatcherServlet
 *
 */
public class AdminDispatcherServlet extends ErrorToNotFoundDispatcherServlet {

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
    public void render(ModelAndView mv, HttpServletRequest request, HttpServletResponse response) throws Exception {
        super.render(mv, request, response);
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

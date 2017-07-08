package org.publiccms.common.servlet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.publiccms.common.constants.CmsVersion;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.resource.DefaultServletHttpRequestHandler;

/**
 * 
 * AdminDispatcherServlet
 *
 */
public class AdminDispatcherServlet extends DispatcherServlet {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private DefaultServletHttpRequestHandler installHandler;

    public AdminDispatcherServlet(WebApplicationContext webApplicationContext) {
        super(webApplicationContext);
    }

    protected void doService(HttpServletRequest request, HttpServletResponse response) throws Exception {
        if (CmsVersion.isInitialized()) {
            super.doService(request, response);
        } else {
            getInstallHandler().handleRequest(request, response);
        }
    }

    /**
     * @return
     */
    public DefaultServletHttpRequestHandler getInstallHandler() {
        if (null == installHandler) {
            installHandler = new DefaultServletHttpRequestHandler();
            installHandler.setServletContext(getWebApplicationContext().getServletContext());
        }
        return installHandler;
    }
}

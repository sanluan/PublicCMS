package com.publiccms.common.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.HttpRequestHandler;

import com.publiccms.common.tools.CommonUtils;

/**
 *
 * InstallHttpRequestHandler 安装跳转处理器
 *
 */
public class InstallHttpRequestHandler implements HttpRequestHandler {
    private String installServletMapping;

    /**
     * @param installServletMapping
     */
    public InstallHttpRequestHandler(String installServletMapping) {
        this.installServletMapping = installServletMapping;
    }

    @Override
    public void handleRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.sendRedirect(CommonUtils.joinString(request.getContextPath(),installServletMapping));
    }

}

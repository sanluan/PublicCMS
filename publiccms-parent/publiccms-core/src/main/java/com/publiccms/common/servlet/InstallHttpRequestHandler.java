package com.publiccms.common.servlet;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.web.HttpRequestHandler;

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
        response.sendRedirect(new StringBuilder(request.getContextPath()).append(installServletMapping).toString());
    }

}

package com.sanluan.common.base;

import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.apache.commons.logging.LogFactory.getLog;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import org.springframework.web.util.UrlPathHelper;

import com.sanluan.common.tools.RequestUtils;

import eu.bitwalker.useragentutils.DeviceType;
import eu.bitwalker.useragentutils.OperatingSystem;
import eu.bitwalker.useragentutils.UserAgent;

/**
 * 
 * BaseInterceptor 拦截器基类
 *
 */
public abstract class BaseInterceptor extends HandlerInterceptorAdapter {
    protected UrlPathHelper urlPathHelper = new UrlPathHelper();
    protected final Log log = getLog(getClass());
    protected String getEncodeQueryString(String queryString) {
        String encodeQueryString = "";
        if (isNotBlank(queryString)) {
            try {
                encodeQueryString = URLEncoder.encode("?" + queryString, "utf-8");
            } catch (UnsupportedEncodingException e) {
                log.debug(e.getMessage());
            }
        }
        return encodeQueryString;
    }

    /**
     * @param request
     * @return
     * @throws IllegalStateException
     */
    protected static boolean checkComputer(HttpServletRequest request) {
        UserAgent userAgent = UserAgent.parseUserAgentString(RequestUtils.getUserAgent(request));
        OperatingSystem operatingSystem = userAgent.getOperatingSystem();
        return operatingSystem.getDeviceType() == DeviceType.COMPUTER;
    }

    /**
     * @param request
     * @return
     * @throws IllegalStateException
     */
    protected static String getURL(HttpServletRequest request) throws IllegalStateException {
        UrlPathHelper helper = new UrlPathHelper();
        String url = helper.getOriginatingRequestUri(request);
        String ctxPath = helper.getOriginatingContextPath(request);
        if (isNotBlank(ctxPath)) {
            url = url.substring(url.indexOf(ctxPath) + ctxPath.length());
        }
        return url;
    }

}

package com.publiccms.common.tools;

import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.publiccms.common.constants.Constants;

/**
 * Request工具类
 * 
 * RequestUtils
 * 
 */
public class RequestUtils {
    private RequestUtils() {
    }

    public static final String CRLF = "[\r\n]";

    /**
     * @param parameterMap
     * @param key
     * @return parameter value
     */
    public static String getValue(Map<String, String[]> parameterMap, String key) {
        String[] values = parameterMap.get(key);
        if (CommonUtils.notEmpty(values)) {
            return values[0];
        }
        return null;
    }

    /**
     * 获取转码路径
     * 
     * @param path
     * @param queryString
     * @return encoded path
     */
    public static String getEncodePath(String path, String queryString) {
        String url = path;
        if (CommonUtils.notEmpty(queryString)) {
            url = CommonUtils.joinString(url, "?", queryString);
        }
        url = CommonUtils.encodeURI(url);
        return url;
    }

    /**
     * @param string
     * @return
     */
    public static String removeCRLF(String string) {
        if (null != string) {
            return string.replaceAll(CRLF, Constants.BLANK);
        }
        return string;
    }

    /**
     * @param values
     */
    public static void removeCRLF(String[] values) {
        if (null != values) {
            for (int i = 0; i < values.length; i++) {
                values[i] = removeCRLF(values[i]);
            }
        }
    }

    /**
     * 获取UserAgent
     * 
     * @param request
     * @return user agent
     */
    public static String getUserAgent(HttpServletRequest request) {
        return request.getHeader("user-agent");
    }

    /**
     * @param request
     * @return accept
     */
    public static String getAccept(HttpServletRequest request) {
        return request.getHeader("Accept");
    }

    /**
     * @param cookies
     * @param name
     * @return cookie
     */
    public static Cookie getCookie(Cookie[] cookies, String name) {
        if (CommonUtils.notEmpty(cookies)) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equalsIgnoreCase(name)) {
                    return cookie;
                }
            }
        }
        return null;
    }

    /**
     * @param contextPath
     * @param schema
     * @param response
     * @param name
     * @param value
     * @param expiry
     * @param domain
     * @return cookie
     */
    public static Cookie addCookie(String contextPath, String schema, HttpServletResponse response, String name, String value,
            Integer expiry, String domain) {
        Cookie cookie = new Cookie(removeCRLF(name), removeCRLF(value));
        if (CommonUtils.notEmpty(expiry)) {
            cookie.setMaxAge(expiry);
        }
        if (CommonUtils.notEmpty(domain)) {
            cookie.setDomain(domain);
        }
        cookie.setSecure("https".equalsIgnoreCase(schema));
        cookie.setHttpOnly(true);
        cookie.setPath(CommonUtils.empty(contextPath) ? Constants.SEPARATOR : contextPath);
        response.addCookie(cookie);
        return cookie;
    }

    /**
     * @param contextPath
     * @param schema
     * @param response
     * @param name
     * @param domain
     */
    public static void cancleCookie(String contextPath, String schema, HttpServletResponse response, String name, String domain) {
        Cookie cookie = new Cookie(name, null);
        cookie.setMaxAge(0);
        cookie.setHttpOnly(true);
        cookie.setSecure("https".equalsIgnoreCase(schema));
        cookie.setPath(CommonUtils.empty(contextPath) ? Constants.SEPARATOR : contextPath);
        if (CommonUtils.notEmpty(domain)) {
            cookie.setDomain(domain);
        }
        response.addCookie(cookie);
    }

    /**
     * @param request
     * @return ip address
     */
    public static String getIpAddress(HttpServletRequest request) {
        if (null != request) {
            return request.getRemoteAddr();
        }
        return null;
    }
}
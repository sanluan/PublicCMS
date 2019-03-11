package com.publiccms.common.tools;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
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
        try {
            if (CommonUtils.notEmpty(queryString)) {
                url += "?" + queryString;
            }
            url = URLEncoder.encode(url, Constants.DEFAULT_CHARSET_NAME);
        } catch (UnsupportedEncodingException e) {
            url = Constants.BLANK;
        }
        return url;
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
     * @param response
     * @param name
     * @param value
     * @param expiry
     * @param domain
     * @return cookie
     */
    public static Cookie addCookie(String contextPath, HttpServletResponse response, String name, String value, Integer expiry,
            String domain) {
        Cookie cookie = new Cookie(name, value);
        if (CommonUtils.notEmpty(expiry)) {
            cookie.setMaxAge(expiry);
        }
        if (CommonUtils.notEmpty(domain)) {
            cookie.setDomain(domain);
        }
        cookie.setPath(CommonUtils.empty(contextPath) ? Constants.SEPARATOR : contextPath);
        response.addCookie(cookie);
        return cookie;
    }

    /**
     * @param contextPath
     * @param response
     * @param name
     * @param domain
     */
    public static void cancleCookie(String contextPath, HttpServletResponse response, String name, String domain) {
        Cookie cookie = new Cookie(name, null);
        cookie.setMaxAge(0);
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
            String ip = request.getHeader("X-Real-IP");
            if (CommonUtils.notEmpty(ip) && !"unknown".equalsIgnoreCase(ip)) {
                return ip;
            }
            ip = request.getHeader("X-Forwarded-For");
            if (CommonUtils.notEmpty(ip) && !"unknown".equalsIgnoreCase(ip)) {
                int index = ip.indexOf(Constants.COMMA);
                if (index != -1) {
                    return ip.substring(0, index);
                }
                return ip;
            }
            return request.getRemoteAddr();
        }
        return null;
    }
}
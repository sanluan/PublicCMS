package com.sanluan.common.tools;

import static org.apache.commons.lang3.ArrayUtils.isNotEmpty;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.sanluan.common.base.Base;

/**
 * Request工具类
 * 
 * @author kerneler
 *
 */
public class RequestUtils extends Base {
    /**
     * @param parameterMap
     * @param key
     * @return
     */
    public static String getValue(Map<String, String[]> parameterMap, String key) {
        String[] values = parameterMap.get(key);
        if (isNotEmpty(values))
            return values[0];
        return null;
    }

    /**
     * @param path
     * @param queryString
     * @return
     * @throws UnsupportedEncodingException
     */
    public static String getEncodePath(String path, String queryString) {
        String url = path;
        try {
            if (notEmpty(queryString)) {
                url += "?" + queryString;
            }
            url = URLEncoder.encode(url, DEFAULT_CHARSET);
        } catch (UnsupportedEncodingException e) {
            url = "";
        }
        return url;
    }

    /**
     * @param serverName
     * @param serverPort
     * @return
     */
    public static String getDomain(String serverName, int serverPort) {
        return 80 == serverPort ? serverName : serverName + ":" + serverPort;
    }

    /**
     * @param request
     * @return
     */
    public static String getUserAgent(HttpServletRequest request) {
        return request.getHeader("user-agent");
    }

    /**
     * @param request
     * @return
     */
    public static String getAccept(HttpServletRequest request) {
        return request.getHeader("Accept");
    }

    /**
     * @param request
     * @param name
     * @return
     */
    public static Cookie getCookie(Cookie[] cookies, String name) {
        if (isNotEmpty(cookies)) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equalsIgnoreCase(name)) {
                    return cookie;
                }
            }
        }
        return null;
    }

    /**
     * @param request
     * @param response
     * @param name
     * @param value
     * @param expiry
     * @param domain
     * @return
     */
    public static Cookie addCookie(String contextPath, HttpServletResponse response, String name, String value, Integer expiry,
            String domain) {
        Cookie cookie = new Cookie(name, value);
        if (notEmpty(expiry)) {
            cookie.setMaxAge(expiry);
        }
        if (notEmpty(domain)) {
            cookie.setDomain(domain);
        }
        cookie.setPath(empty(contextPath) ? SEPARATOR : contextPath);
        response.addCookie(cookie);
        return cookie;
    }

    /**
     * @param request
     * @param response
     * @param name
     * @param domain
     */
    public static void cancleCookie(String contextPath, HttpServletResponse response, String name, String domain) {
        Cookie cookie = new Cookie(name, null);
        cookie.setMaxAge(0);
        cookie.setPath(empty(contextPath) ? SEPARATOR : contextPath);
        if (notEmpty(domain)) {
            cookie.setDomain(domain);
        }
        response.addCookie(cookie);
    }

    /**
     * @param request
     * @return
     */
    public static String getIpAddress(HttpServletRequest request) {
        String ip = request.getHeader("X-Real-IP");
        if (notEmpty(ip) && !"unknown".equalsIgnoreCase(ip)) {
            return ip;
        }
        ip = request.getHeader("X-Forwarded-For");
        if (notEmpty(ip) && !"unknown".equalsIgnoreCase(ip)) {
            int index = ip.indexOf(',');
            if (index != -1) {
                return ip.substring(0, index);
            }
            return ip;
        }
        return request.getRemoteAddr();
    }
}
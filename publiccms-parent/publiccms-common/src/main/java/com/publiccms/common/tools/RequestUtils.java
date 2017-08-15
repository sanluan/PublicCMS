package com.publiccms.common.tools;

import static com.publiccms.common.tools.CommonUtils.empty;
import static com.publiccms.common.tools.CommonUtils.notEmpty;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.publiccms.common.base.Base;

/**
 * Request工具类
 * 
 * RequestUtils 
 * 
 */
public class RequestUtils implements Base {
    /**
     * @param parameterMap
     * @param key
     * @return
     */
    public static String getValue(Map<String, String[]> parameterMap, String key) {
        String[] values = parameterMap.get(key);
        if (notEmpty(values)) {
            return values[0];
        }
        return null;
    }

    /**
     * 获取转码路径
     * 
     * Get Encoded Path
     * 
     * @param path
     * @param queryString
     * @return
     */
    public static String getEncodePath(String path, String queryString) {
        String url = path;
        try {
            if (notEmpty(queryString)) {
                url += "?" + queryString;
            }
            url = URLEncoder.encode(url, DEFAULT_CHARSET_NAME);
        } catch (UnsupportedEncodingException e) {
            url = "";
        }
        return url;
    }

    /**
     * 获取UserAgent
     * 
     * Get UserAgent
     * 
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
     * @param cookies
     * @param name
     * @return
     */
    public static Cookie getCookie(Cookie[] cookies, String name) {
        if (notEmpty(cookies)) {
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
     * @param contextPath
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
        if (null != request) {
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
        return null;
    }
}
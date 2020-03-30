package com.publiccms.common.tools;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.springframework.ui.ModelMap;

import com.publiccms.common.base.AbstractFreemarkerView;
import com.publiccms.common.constants.CommonConstants;
import com.publiccms.entities.sys.SysSite;
import com.publiccms.entities.sys.SysUser;

/**
 * Controller工具类
 * 
 * ControllerUtils
 *
 */
public class ControllerUtils {
    /**
     * Number Pattern
     */
    public static final Pattern NUMBER_PATTERN = Pattern.compile("^[0-9]*$");
    /**
     * UNVALID Pattern
     */
    public static final Pattern UNVALID_CHARS = Pattern
            .compile("[ 　`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}‘；：”“’。，、？]|\n|\r|\t");

    private static final String VALID_CHARS = "[^\\s\\(\\)<>@,;:\\\\\\\"\\.\\[\\]+]+";
    /**
     * Email Pattern
     */
    public static final Pattern EMAIL_PATTERN = Pattern
            .compile("(" + VALID_CHARS + "(\\." + VALID_CHARS + ")*@" + VALID_CHARS + "(\\." + VALID_CHARS + ")*)");

    public static boolean isUnSafeUrl(String url, SysSite site, String safeReturnUrl, HttpServletRequest request) {
        if (CommonUtils.empty(url)) {
            return true;
        } else if (url.contains("\r") || url.contains("\n")) {
            return true;
        } else if (url.contains("://") || url.startsWith("//")) {
            if (url.startsWith("//")) {
                url = new StringBuilder(request.getScheme()).append(":").append(url).toString();
            }
            if (unSafe(url, site, request)) {
                if (CommonUtils.notEmpty(safeReturnUrl)) {
                    for (String safeUrlPrefix : StringUtils.split(safeReturnUrl, CommonConstants.COMMA_DELIMITED)) {
                        if (url.startsWith(safeUrlPrefix)) {
                            return false;
                        }
                    }
                }
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    private static boolean unSafe(String url, SysSite site, HttpServletRequest request) {
        String basePath = AbstractFreemarkerView.getBasePath(request.getScheme(), request.getServerPort(),
                request.getServerName(), request.getContextPath()) + "/";
        String fixedUrl = url.substring(url.indexOf("://") + 1);
        if (url.startsWith(site.getDynamicPath()) || url.startsWith(site.getSitePath())
                || fixedUrl.startsWith(site.getDynamicPath()) || fixedUrl.startsWith(site.getSitePath())
                || url.startsWith(basePath)) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * @param response
     * @param url
     */
    public static void redirectPermanently(HttpServletResponse response, String url) {
        response.setHeader("Location", RequestUtils.removeCRLF(url));
        response.setStatus(HttpServletResponse.SC_MOVED_PERMANENTLY);
    }

    /**
     * @param field
     * @param value
     * @param model
     * @return value是否为空
     */
    public static boolean verifyNotEmpty(String field, String value, Map<String, Object> model) {
        if (CommonUtils.empty(value)) {
            model.put(CommonConstants.ERROR, "verify.notEmpty." + field);
            return true;
        }
        return false;
    }

    /**
     * @param field
     * @param value
     * @param model
     * @return value是否为true
     */
    public static boolean verifyCustom(String field, boolean value, Map<String, Object> model) {
        if (value) {
            model.put(CommonConstants.ERROR, "verify.custom." + field);
            return true;
        }
        return false;
    }

    /**
     * @param field
     * @param value
     * @param model
     * @return value是否为空
     */
    public static boolean verifyNotEmpty(String field, Object value, Map<String, Object> model) {
        if (null == value) {
            model.put(CommonConstants.ERROR, "verify.notEmpty." + field);
            return true;
        }
        return false;
    }

    /**
     * @param field
     * @param value
     * @param specific
     * @param model
     * @return value是否为空或是否大于等于specific
     */
    public static boolean verifyNotGreaterThen(String field, Integer value, int specific, Map<String, Object> model) {
        if (null == value) {
            model.put(CommonConstants.ERROR, "verify.notEmpty." + field);
            return true;
        } else if (value >= specific) {
            model.put(CommonConstants.ERROR, "verify.notGreaterThen." + field);
            return true;
        }
        return false;
    }

    /**
     * @param field
     * @param value
     * @param specific
     * @param model
     * @return value是否为空或是否大于等于specific
     */
    public static boolean verifyNotGreaterThen(String field, Long value, long specific, Map<String, Object> model) {
        if (null == value) {
            model.put(CommonConstants.ERROR, "verify.notEmpty." + field);
            return true;
        } else if (value >= specific) {
            model.put(CommonConstants.ERROR, "verify.notGreaterThen." + field);
            return true;
        }
        return false;
    }

    /**
     * @param field
     * @param value
     * @param specific
     * @param model
     * @return value是否为空或长度大于specific
     */
    public static boolean verifyNotLongThen(String field, String value, int specific, Map<String, Object> model) {
        if (null == value) {
            model.put(CommonConstants.ERROR, "verify.notEmpty." + field);
            return true;
        } else if (value.length() > specific) {
            model.put(CommonConstants.ERROR, "verify.notLongThen." + field);
            return true;
        }
        return false;
    }

    /**
     * @param field
     * @param value
     * @param specific
     * @param model
     * @return value是否为空或长度小于specific
     */
    public static boolean verifyNotLessThen(String field, Integer value, int specific, Map<String, Object> model) {
        if (null == value) {
            model.put(CommonConstants.ERROR, "verify.notEmpty." + field);
            return true;
        } else if (value < specific) {
            model.put(CommonConstants.ERROR, "verify.notLessThen." + field);
            return true;
        }
        return false;
    }

    /**
     * @param field
     * @param value
     * @param model
     * @return value是否为空
     */
    public static boolean verifyNotExist(String field, Object value, Map<String, Object> model) {
        if (null == value) {
            model.put(CommonConstants.ERROR, "verify.notExist." + field);
            return true;
        }
        return false;
    }

    /**
     * @param field
     * @param value
     * @param model
     * @return value是否不为空
     */
    public static boolean verifyHasExist(String field, Object value, Map<String, Object> model) {
        if (null != value) {
            model.put(CommonConstants.ERROR, "verify.hasExist." + field);
            return true;
        }
        return false;
    }

    /**
     * @param field
     * @param value
     * @param value2
     * @param model
     * @return value1是否不为空并等于value2
     */
    public static boolean verifyEquals(String field, Long value, Long value2, ModelMap model) {
        if (CommonUtils.notEmpty(value) && value.equals(value2)) {
            model.addAttribute(CommonConstants.ERROR, "verify.equals." + field);
            return true;
        }
        return false;
    }

    /**
     * @param field
     * @param value1
     * @param value2
     * @param model
     * @return value1是否不为空并不等于value2
     */
    public static boolean verifyNotEquals(String field, String value1, String value2, Map<String, Object> model) {
        if (CommonUtils.notEmpty(value1) && !value1.equals(value2)) {
            model.put(CommonConstants.ERROR, "verify.notEquals." + field);
            return true;
        }
        return false;
    }

    /**
     * @param field
     * @param value1
     * @param value2
     * @param model
     * @return value1是否不为空并不等于value2
     */
    public static boolean verifyNotEquals(String field, Integer value1, Integer value2, Map<String, Object> model) {
        if (CommonUtils.notEmpty(value1) && !value1.equals(value2)) {
            model.put(CommonConstants.ERROR, "verify.notEquals." + field);
            return true;
        }
        return false;
    }

    /**
     * @param field
     * @param value1
     * @param value2
     * @param model
     * @return value1是否不为空并不等于value2
     */
    public static boolean verifyNotEquals(String field, Long value1, Long value2, Map<String, Object> model) {
        if (CommonUtils.notEmpty(value1) && !value1.equals(value2)) {
            model.put(CommonConstants.ERROR, "verify.notEquals." + field);
            return true;
        }
        return false;
    }

    /**
     * @param field
     * @param value1
     * @param value2
     * @param model
     * @return value1是否不为空并不等于value2
     */
    public static boolean verifyNotEquals(String field, Short value1, Short value2, Map<String, Object> model) {
        if (CommonUtils.notEmpty(value1) && !value1.equals(value2)) {
            model.put(CommonConstants.ERROR, "verify.notEquals." + field);
            return true;
        }
        return false;
    }

    /**
     * @param session
     * @return SysUser
     */
    public static SysUser getUserFromSession(HttpSession session) {
        if (null != session) {
            return (SysUser) session.getAttribute(CommonConstants.getSessionUser());
        } else {
            return null;
        }
    }

    /**
     * @param session
     * @return Date
     */
    public static Long getUserTimeFromSession(HttpSession session) {
        if (null != session) {
            return (Long) session.getAttribute(CommonConstants.getSessionUserTime());
        } else {
            return null;
        }
    }

    /**
     * @param session
     * @param user
     */
    public static void setUserToSession(HttpSession session, SysUser user) {
        session.setAttribute(CommonConstants.getSessionUser(), user);
        session.setAttribute(CommonConstants.getSessionUserTime(), System.currentTimeMillis());
    }

    /**
     * @param contextPath
     * @param session
     * @param response
     */
    public static void clearUserToSession(String contextPath, HttpSession session, HttpServletResponse response) {
        RequestUtils.cancleCookie(contextPath, response, CommonConstants.getCookiesUser(), null);
        session.removeAttribute(CommonConstants.getSessionUser());
    }

    /**
     * @param session
     */
    public static void clearUserTimeToSession(HttpSession session) {
        session.removeAttribute(CommonConstants.getSessionUserTime());
    }

    /**
     * @param session
     * @return SysUser
     */
    public static SysUser getAdminFromSession(HttpSession session) {
        return (SysUser) session.getAttribute(CommonConstants.getSessionAdmin());
    }

    /**
     * @param session
     * @param user
     */
    public static void setAdminToSession(HttpSession session, SysUser user) {
        session.setAttribute(CommonConstants.getSessionAdmin(), user);
    }

    /**
     * @param contextPath
     * @param session
     * @param response
     */
    public static void clearAdminToSession(String contextPath, HttpSession session, HttpServletResponse response) {
        RequestUtils.cancleCookie(contextPath, response, CommonConstants.getCookiesAdmin(), null);
        session.removeAttribute(CommonConstants.getSessionAdmin());
    }

    /**
     * @param request
     * @return
     */
    public static String getWebToken(HttpServletRequest request) {
        return getToken(request, CommonConstants.getCookiesUser());
    }

    /**
     * @param request
     * @return
     */
    public static String getAdminToken(HttpServletRequest request) {
        return getToken(request, CommonConstants.getCookiesAdmin());
    }

    /**
     * @param request
     * @param cookiesName
     * @return
     */
    private static String getToken(HttpServletRequest request, String cookiesName) {
        Cookie userCookie = RequestUtils.getCookie(request.getCookies(), cookiesName);
        if (null != userCookie && CommonUtils.notEmpty(userCookie.getValue())) {
            String value = userCookie.getValue();
            if (null != value) {
                String[] userData = value.split(CommonConstants.getCookiesUserSplit());
                if (userData.length > 1) {
                    return userData[1];
                }
            }
        }
        return null;
    }

    /**
     * @param value
     * @return boolean
     */
    public static boolean verifyNotValid(String value) {
        Matcher m = UNVALID_CHARS.matcher(value);
        if (null == value || 0 == value.length() || m.matches()) {
            return true;
        }
        return false;
    }

    /**
     * @param field
     * @param value
     * @param model
     * @return boolean
     */
    public static boolean verifyNotEMail(String field, String value, Map<String, Object> model) {
        if (verifyNotEMail(value)) {
            model.put(CommonConstants.ERROR, "verify.notEmail." + field);
            return true;
        }
        return false;
    }

    /**
     * @param field
     * @param value
     * @param model
     * @return boolean
     */
    public static boolean verifyNotUserName(String field, String value, Map<String, Object> model) {
        if (verifyNotValid(value)) {
            model.put(CommonConstants.ERROR, "verify.notUserName." + field);
            return true;
        }
        return false;
    }

    /**
     * @param field
     * @param value
     * @param model
     * @return boolean
     */
    public static boolean verifyNotNickName(String field, String value, Map<String, Object> model) {
        if (verifyNotValid(value)) {
            model.put(CommonConstants.ERROR, "verify.notNickName." + field);
            return true;
        }
        return false;
    }

    /**
     * @param value
     * @return boolean
     */
    public static boolean verifyNotEMail(String value) {
        Matcher m = EMAIL_PATTERN.matcher(value);
        if (!m.matches()) {
            return true;
        }
        return false;
    }

    /**
     * @param value
     * @return boolean
     */
    public static boolean verifyNotNumber(String value) {
        Matcher m = NUMBER_PATTERN.matcher(value);
        if (!m.matches()) {
            return true;
        }
        return false;
    }
}

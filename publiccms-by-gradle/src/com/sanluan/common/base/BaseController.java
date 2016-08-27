package com.sanluan.common.base;

import static org.apache.commons.lang3.StringUtils.isEmpty;
import static org.springframework.web.servlet.view.UrlBasedViewResolver.FORWARD_URL_PREFIX;
import static org.springframework.web.servlet.view.UrlBasedViewResolver.REDIRECT_URL_PREFIX;

import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.ui.ModelMap;

/**
 * 
 * BaseController
 *
 */
public abstract class BaseController extends Base {
    protected static final String REDIRECT = REDIRECT_URL_PREFIX;
    protected static final String FORWARD = FORWARD_URL_PREFIX;
    protected static final String ERROR = "error";

    protected static void redirectPermanently(HttpServletResponse response, String url) {
        response.setHeader("Location", url);
        response.setStatus(HttpServletResponse.SC_MOVED_PERMANENTLY);
    }

    /**
     * @param field
     * @param value
     * @param model
     * @return
     */
    protected static boolean verifyNotEmpty(String field, String value, Map<String, Object> model) {
        if (isEmpty(value)) {
            model.put(ERROR, "verify.notEmpty." + field);
            return true;
        }
        return false;
    }

    /**
     * @param field
     * @param value
     * @param model
     * @return
     */
    protected static boolean verifyCustom(String field, boolean value, Map<String, Object> model) {
        if (value) {
            model.put(ERROR, "verify.custom." + field);
            return true;
        }
        return false;
    }

    /**
     * @param field
     * @param value
     * @param model
     * @return
     */
    protected static boolean verifyNotEmpty(String field, Object value, Map<String, Object> model) {
        if (null == value) {
            model.put(ERROR, "verify.notEmpty." + field);
            return true;
        }
        return false;
    }

    /**
     * @param field
     * @param value
     * @param specific
     * @param model
     * @return
     */
    protected static boolean verifyNotGreaterThen(String field, Integer value, int specific, Map<String, Object> model) {
        if (null == value) {
            model.put(ERROR, "verify.notEmpty." + field);
            return true;
        } else if (value >= specific) {
            model.put(ERROR, "verify.notGreaterThen." + field);
            return true;
        }
        return false;
    }

    /**
     * @param field
     * @param value
     * @param specific
     * @param model
     * @return
     */
    protected static boolean verifyNotLongThen(String field, String value, int specific, Map<String, Object> model) {
        if (null == value) {
            model.put(ERROR, "verify.notEmpty." + field);
            return true;
        } else if (value.length() > specific) {
            model.put(ERROR, "verify.notLongThen." + field);
            return true;
        }
        return false;
    }

    /**
     * @param field
     * @param value
     * @param specific
     * @param model
     * @return
     */
    protected static boolean verifyNotLessThen(String field, Integer value, int specific, Map<String, Object> model) {
        if (null == value) {
            model.put(ERROR, "verify.notEmpty." + field);
            return true;
        } else if (value < specific) {
            model.put(ERROR, "verify.notLessThen." + field);
            return true;
        }
        return false;
    }

    /**
     * @param field
     * @param value
     * @param model
     * @return
     */
    protected static boolean verifyNotExist(String field, Object value, Map<String, Object> model) {
        if (null == value) {
            model.put(ERROR, "verify.notExist." + field);
            return true;
        }
        return false;
    }

    /**
     * @param field
     * @param value
     * @param model
     * @return
     */
    protected static boolean verifyHasExist(String field, Object value, Map<String, Object> model) {
        if (notEmpty(value)) {
            model.put(ERROR, "verify.hasExist." + field);
            return true;
        }
        return false;
    }
    
    /**
     * @param field
     * @param value
     * @param value2
     * @param model
     * @return
     */
    protected boolean verifyEquals(String field, Long value, Long value2, ModelMap model) {
        if (notEmpty(value) && value.equals(value2)) {
            model.addAttribute(ERROR, "verify.equals." + field);
            return true;
        }
        return false;
    }

    /**
     * @param field
     * @param value1
     * @param value2
     * @param model
     * @return
     */
    protected static boolean verifyNotEquals(String field, String value1, String value2, Map<String, Object> model) {
        if (notEmpty(value1) && !value1.equals(value2)) {
            model.put(ERROR, "verify.notEquals." + field);
            return true;
        }
        return false;
    }

    /**
     * @param field
     * @param value1
     * @param value2
     * @param model
     * @return
     */
    protected static boolean verifyNotEquals(String field, Integer value1, Integer value2, Map<String, Object> model) {
        if (notEmpty(value1) && !value1.equals(value2)) {
            model.put(ERROR, "verify.notEquals." + field);
            return true;
        }
        return false;
    }
    
    /**
     * @param field
     * @param value1
     * @param value2
     * @param model
     * @return
     */
    protected static boolean verifyNotEquals(String field, Long value1, Long value2, Map<String, Object> model) {
        if (notEmpty(value1) && !value1.equals(value2)) {
            model.put(ERROR, "verify.notEquals." + field);
            return true;
        }
        return false;
    }
}

package com.publiccms.common.tools;

import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.ui.ModelMap;

/**
 * Controller工具类
 * 
 * ControllerUtils
 *
 */
public abstract class ControllerUtils {
    /**
     * 错误
     */
    public static final String ERROR = "error";

    /**
     * @param response
     * @param url
     */
    public static void redirectPermanently(HttpServletResponse response, String url) {
        response.setHeader("Location", url);
        response.setStatus(HttpServletResponse.SC_MOVED_PERMANENTLY);
    }

    /**
     * @param field
     * @param value
     * @param model
     * @return value是否为空
     */
    public static boolean verifyNotEmpty(String field, String value, Map<String, Object> model) {
        if (StringUtils.isEmpty(value)) {
            model.put(ERROR, "verify.notEmpty." + field);
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
            model.put(ERROR, "verify.custom." + field);
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
     * @return value是否为空或是否大于等于specific
     */
    public static boolean verifyNotGreaterThen(String field, Integer value, int specific, Map<String, Object> model) {
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
     * @return value是否为空或是否大于等于specific
     */
    public static boolean verifyNotGreaterThen(String field, Long value, long specific, Map<String, Object> model) {
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
     * @return value是否为空或长度大于specific
     */
    public static boolean verifyNotLongThen(String field, String value, int specific, Map<String, Object> model) {
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
     * @return value是否为空或长度小于specific
     */
    public static boolean verifyNotLessThen(String field, Integer value, int specific, Map<String, Object> model) {
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
     * @return value是否为空
     */
    public static boolean verifyNotExist(String field, Object value, Map<String, Object> model) {
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
     * @return value是否不为空
     */
    public static boolean verifyHasExist(String field, Object value, Map<String, Object> model) {
        if (null != value) {
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
     * @return value1是否不为空并等于value2
     */
    public static boolean verifyEquals(String field, Long value, Long value2, ModelMap model) {
        if (CommonUtils.notEmpty(value) && value.equals(value2)) {
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
     * @return value1是否不为空并不等于value2
     */
    public static boolean verifyNotEquals(String field, String value1, String value2, Map<String, Object> model) {
        if (CommonUtils.notEmpty(value1) && !value1.equals(value2)) {
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
     * @return value1是否不为空并不等于value2
     */
    public static boolean verifyNotEquals(String field, Integer value1, Integer value2, Map<String, Object> model) {
        if (CommonUtils.notEmpty(value1) && !value1.equals(value2)) {
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
     * @return value1是否不为空并不等于value2
     */
    public static boolean verifyNotEquals(String field, Long value1, Long value2, Map<String, Object> model) {
        if (CommonUtils.notEmpty(value1) && !value1.equals(value2)) {
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
     * @return value1是否不为空并不等于value2
     */
    public static boolean verifyNotEquals(String field, Short value1, Short value2, Map<String, Object> model) {
        if (CommonUtils.notEmpty(value1) && !value1.equals(value2)) {
            model.put(ERROR, "verify.notEquals." + field);
            return true;
        }
        return false;
    }
}

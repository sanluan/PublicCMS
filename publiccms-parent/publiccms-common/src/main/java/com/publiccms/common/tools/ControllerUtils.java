package com.publiccms.common.tools;

import static com.publiccms.common.tools.CommonUtils.notEmpty;
import static org.apache.commons.lang3.StringUtils.isEmpty;

import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.ui.ModelMap;

/**
 * 
 * BaseController
 *
 */
public abstract class ControllerUtils {
    public static final String ERROR = "error";

    public static void redirectPermanently(HttpServletResponse response, String url) {
        response.setHeader("Location", url);
        response.setStatus(HttpServletResponse.SC_MOVED_PERMANENTLY);
    }

    /**
     * @param field
     * @param value
     * @param model
     * @return
     */
    public static boolean verifyNotEmpty(String field, String value, Map<String, Object> model) {
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
     * @return
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
     * @return
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
     * @return
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
     * @return
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
     * @return
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
     * @return
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
     * @return
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
     * @return
     */
    public static boolean verifyEquals(String field, Long value, Long value2, ModelMap model) {
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
    public static boolean verifyNotEquals(String field, String value1, String value2, Map<String, Object> model) {
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
    public static boolean verifyNotEquals(String field, Integer value1, Integer value2, Map<String, Object> model) {
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
    public static boolean verifyNotEquals(String field, Long value1, Long value2, Map<String, Object> model) {
        if (notEmpty(value1) && !value1.equals(value2)) {
            model.put(ERROR, "verify.notEquals." + field);
            return true;
        }
        return false;
    }
}

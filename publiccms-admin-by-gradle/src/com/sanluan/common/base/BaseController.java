package com.sanluan.common.base;

import static com.sanluan.common.constants.CommonConstants.EMAIL_PATTERN;
import static com.sanluan.common.constants.CommonConstants.MOBILE_PATTERN;
import static com.sanluan.common.constants.CommonConstants.NICKNAME_PATTERN;
import static com.sanluan.common.constants.CommonConstants.NUMBER_PATTERN;
import static com.sanluan.common.constants.CommonConstants.USERNAME_PATTERN;
import static org.springframework.web.servlet.view.UrlBasedViewResolver.FORWARD_URL_PREFIX;
import static org.springframework.web.servlet.view.UrlBasedViewResolver.REDIRECT_URL_PREFIX;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.apache.commons.lang3.StringUtils.isEmpty;

/**
 * 
 * BaseController
 *
 */
public abstract class BaseController extends Base {
    protected static final String TEMPLATE_INDEX = "index";
    protected static final String TEMPLATE_DONE = "common/ajaxDone";
    protected static final String TEMPLATE_ERROR = "common/ajaxError";
    protected static final String MESSAGE = "message";
    protected static final String SUCCESS = "success";
    protected static final String ERROR = "error";
    protected static final String DELETE = "delete";
    protected static final String SAVE = "save";
    protected static final String ID = "id";
    protected static final String REDIRECT = REDIRECT_URL_PREFIX;
    protected static final String FORWARD = FORWARD_URL_PREFIX;

    public static Map<String, Object> getMap() {
        return new HashMap<String, Object>();
    }

    /**
     * @param field
     * @param value
     * @param model
     * @return
     */
    protected boolean virifyNotEmpty(String field, String value, Map<String, Object> model) {
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
    protected boolean virifyCustom(String field, boolean value, Map<String, Object> model) {
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
    protected boolean virifyNotEmpty(String field, Object value, Map<String, Object> model) {
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
    protected boolean virifyNotGreaterThen(String field, Integer value, int specific, Map<String, Object> model) {
        if (null == value) {
            model.put(ERROR, "verify.notEmpty." + field);
            return true;
        } else if (value <= specific) {
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
    protected boolean virifyNotLongThen(String field, String value, int specific, Map<String, Object> model) {
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
    protected boolean virifyNotLessThen(String field, String value, int specific, Map<String, Object> model) {
        if (null == value) {
            model.put(ERROR, "verify.notEmpty." + field);
            return true;
        } else if (value.length() < specific) {
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
    protected boolean virifyNotUserName(String field, String value, Map<String, Object> model) {
        if (virifyNotUserName(value)) {
            model.put(ERROR, "verify.notUserName." + field);
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
    protected boolean virifyNotNickName(String field, String value, Map<String, Object> model) {
        if (virifyNotNickName(value)) {
            model.put(ERROR, "verify.notNickName." + field);
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
    protected boolean virifyNotMobile(String field, String value, Map<String, Object> model) {
        if (virifyNotMobile(value)) {
            model.put(ERROR, "verify.notMobile." + field);
            return true;
        }
        return false;
    }

    /**
     * @param value
     * @return
     */
    protected boolean virifyNotUserName(String value) {
        Pattern p = Pattern.compile(USERNAME_PATTERN);
        Matcher m = p.matcher(value);
        if (!m.matches()) {
            return true;
        }
        return false;
    }

    /**
     * @param value
     * @return
     */
    protected boolean virifyNotNickName(String value) {
        Pattern p = Pattern.compile(NICKNAME_PATTERN);
        Matcher m = p.matcher(value);
        if (!m.matches()) {
            return true;
        }
        return false;
    }

    /**
     * @param value
     * @return
     */
    protected boolean virifyNotMobile(String value) {
        Pattern p = Pattern.compile(MOBILE_PATTERN);
        Matcher m = p.matcher(value);
        if (!m.matches()) {
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
    protected boolean virifyNotEMail(String field, String value, Map<String, Object> model) {
        if (virifyNotEMail(value)) {
            model.put(ERROR, "verify.notEmail." + field);
            return true;
        }
        return false;
    }

    /**
     * @param value
     * @return
     */
    protected boolean virifyNotEMail(String value) {
        Pattern p = Pattern.compile(EMAIL_PATTERN);
        Matcher m = p.matcher(value);
        if (!m.matches()) {
            return true;
        }
        return false;
    }

    /**
     * @param value
     * @return
     */
    protected boolean virifyNotNumber(String value) {
        Pattern p = Pattern.compile(NUMBER_PATTERN);
        Matcher m = p.matcher(value);
        if (!m.matches()) {
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
    protected boolean virifyNotEMailAndMobile(String field, String value, Map<String, Object> model) {
        if (virifyNotEMail(value) && virifyNotMobile(value)) {
            model.put(ERROR, "verify.notEmailAndMobile." + field);
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
    protected boolean virifyNotExist(String field, Object value, Map<String, Object> model) {
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
    protected boolean virifyHasExist(String field, Object value, Map<String, Object> model) {
        if (notEmpty(value)) {
            model.put(ERROR, "verify.hasExist." + field);
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
    protected boolean virifyNotEquals(String field, String value1, String value2, Map<String, Object> model) {
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
    protected boolean virifyNotEquals(String field, Integer value1, Integer value2, Map<String, Object> model) {
        if (notEmpty(value1) && !value1.equals(value2)) {
            model.put(ERROR, "verify.notEquals." + field);
            return true;
        }
        return false;
    }
}

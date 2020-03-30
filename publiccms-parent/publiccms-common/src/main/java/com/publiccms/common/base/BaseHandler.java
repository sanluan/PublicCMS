package com.publiccms.common.base;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.publiccms.common.handler.RenderHandler;
import com.publiccms.common.tools.CommonUtils;

/**
 * 指令处理器基类
 * 
 * BaseHandler
 *
 */
public abstract class BaseHandler implements RenderHandler {
    protected final Log log = LogFactory.getLog(getClass());
    /**
     * 参数名称
     * 
     * Parameters name
     */
    public static final String PARAMETERS_NAME = "parameters";
    /**
     * 控制参数
     * 
     * Controller parameters
     * 
     */
    public static final String PARAMETERS_CONTROLLER = "showParameters";
    /**
     * String类型参数
     * 
     * String type parameter
     * 
     */
    public static final String PARAMETER_TYPE_STRING = "string";
    /**
     * Char类型参数
     * 
     * Char type parameter
     * 
     */
    public static final String PARAMETER_TYPE_CHAR = "char";
    /**
     * Short类型参数
     * 
     * Short type parameter
     * 
     */
    public static final String PARAMETER_TYPE_SHORT = "short";
    /**
     * Long类型参数
     * 
     * Long type parameter
     * 
     */
    public static final String PARAMETER_TYPE_LONG = "long";
    /**
     * Double类型参数
     * 
     * Double type parameter
     * 
     */
    public static final String PARAMETER_TYPE_DOUBLE = "double";
    /**
     * Boolean类型参数
     * 
     * Boolean type parameter
     * 
     */
    public static final String PARAMETER_TYPE_BOOLEAN = "boolean";
    /**
     * Integer类型参数
     * 
     * Integer type parameter
     * 
     */
    public static final String PARAMETER_TYPE_INTEGER = "integer";
    /**
     * Date类型参数
     * 
     * Date type parameter
     * 
     */
    public static final String PARAMETER_TYPE_DATE = "date";
    /**
     * Long数组类型参数
     * 
     * Long array type parameter
     * 
     */
    public static final String PARAMETER_TYPE_LONGARRAY = "longArray";
    /**
     * Integer数组类型参数
     * 
     * Integer array type parameter
     * 
     */
    public static final String PARAMETER_TYPE_INTEGERARRAY = "integerArray";
    /**
     * Short数组类型参数
     * 
     * Short array type parameter
     * 
     */
    public static final String PARAMETER_TYPE_SHORTARRAY = "shortArray";
    /**
     * String数组类型参数
     * 
     * String array type parameter
     * 
     */
    public static final String PARAMETER_TYPE_STRINGARRAY = "stringArray";
    protected Map<String, Object> map = new LinkedHashMap<>();
    protected List<Map<String, Object>> parameterList;
    protected boolean regristerParameters;
    protected boolean renderd = false;

    /**
     * 注册参数
     * 
     * Register parameters
     */
    public void regristerParameters() {
        this.regristerParameters = getBooleanWithoutRegister(PARAMETERS_CONTROLLER, false);
        if (regristerParameters) {
            parameterList = new ArrayList<>();
            put(PARAMETERS_NAME, parameterList);
        }
    }

    protected void regristerParameter(String type, String name) {
        regristerParameter(type, name, null);
    }

    protected void regristerParameter(String type, String name, Object defaultValue) {
        if (regristerParameters) {
            HashMap<String, Object> parameter = new HashMap<>();
            parameter.put("name", name);
            parameter.put("type", type);
            parameter.put("defaultValue", defaultValue);
            parameterList.add(parameter);
        }
    }

    @Override
    public RenderHandler put(String key, Object value) {
        map.put(key, value);
        return this;
    }

    /**
     * 获取结果集大小
     * 
     * @return results size
     */
    public int getSize() {
        return map.size();
    }

    protected abstract Integer getIntegerWithoutRegister(String name) throws Exception;

    protected abstract String[] getStringArrayWithoutRegister(String name) throws Exception;

    protected abstract String getStringWithoutRegister(String name) throws Exception;

    protected abstract Boolean getBooleanWithoutRegister(String name) throws Exception;

    protected abstract Date getDateWithoutRegister(String name) throws Exception;

    @Override
    public String getString(String name) throws Exception {
        regristerParameter(PARAMETER_TYPE_STRING, name);
        return getStringWithoutRegister(name);
    }

    @Override
    public String getString(String name, String defaultValue) throws Exception {
        try {
            String result = getString(name);
            regristerParameter(PARAMETER_TYPE_STRING, name, defaultValue);
            return CommonUtils.notEmpty(result) ? result : defaultValue;
        } catch (Exception e) {
            return defaultValue;
        }
    }

    @Override
    public Character getCharacter(String name) throws Exception {
        regristerParameter(PARAMETER_TYPE_CHAR, name);
        String result = getStringWithoutRegister(name);
        if (CommonUtils.notEmpty(result)) {
            return result.charAt(0);
        }
        return null;
    }

    @Override
    public Integer getInteger(String name) throws Exception {
        regristerParameter(PARAMETER_TYPE_INTEGER, name);
        return getIntegerWithoutRegister(name);
    }

    @Override
    public Integer getInteger(String name, Integer defaultValue) {
        try {
            regristerParameter(PARAMETER_TYPE_INTEGER, name, defaultValue);
            Integer result = getIntegerWithoutRegister(name);
            return CommonUtils.notEmpty(result) ? result : defaultValue;
        } catch (Exception e) {
            return defaultValue;
        }
    }

    @Override
    public Integer[] getIntegerArray(String name) throws Exception {
        regristerParameter(PARAMETER_TYPE_INTEGERARRAY, name);
        String[] arr = getStringArrayWithoutRegister(name);
        if (CommonUtils.notEmpty(arr)) {
            Set<Integer> set = new TreeSet<>();
            for (String s : arr) {
                try {
                    set.add(Integer.valueOf(s));
                } catch (NumberFormatException e) {
                }
            }
            return set.toArray(new Integer[set.size()]);
        }
        return null;
    }

    @Override
    public Long[] getLongArray(String name) throws Exception {
        regristerParameter(PARAMETER_TYPE_LONGARRAY, name);
        String[] arr = getStringArrayWithoutRegister(name);
        if (CommonUtils.notEmpty(arr)) {
            Set<Long> set = new TreeSet<>();
            for (String s : arr) {
                try {
                    set.add(Long.valueOf(s));
                } catch (NumberFormatException e) {
                }
            }
            return set.toArray(new Long[set.size()]);
        }
        return null;
    }

    @Override
    public Short[] getShortArray(String name) throws Exception {
        regristerParameter(PARAMETER_TYPE_SHORTARRAY, name);
        String[] arr = getStringArrayWithoutRegister(name);
        if (CommonUtils.notEmpty(arr)) {
            Set<Short> set = new TreeSet<>();
            for (String s : arr) {
                try {
                    set.add(Short.valueOf(s));
                } catch (NumberFormatException e) {
                }
            }
            int i = 0;
            Short[] ids = new Short[set.size()];
            for (Short number : set) {
                ids[i++] = number;
            }
            return ids;
        }
        return null;
    }

    protected Boolean getBooleanWithoutRegister(String name, Boolean defaultValue) {
        try {
            Boolean result = getBooleanWithoutRegister(name);
            return null != result ? result : defaultValue;
        } catch (Exception e) {
            return defaultValue;
        }
    }

    @Override
    public Boolean getBoolean(String name) throws Exception {
        regristerParameter(PARAMETER_TYPE_BOOLEAN, name);
        return getBooleanWithoutRegister(name);
    }

    @Override
    public Boolean getBoolean(String name, Boolean defaultValue) throws Exception {
        regristerParameter(PARAMETER_TYPE_BOOLEAN, name, defaultValue);
        return getBooleanWithoutRegister(name, defaultValue);
    }

    @Override
    public Date getDate(String name) throws Exception {
        regristerParameter(PARAMETER_TYPE_DATE, name);
        return getDateWithoutRegister(name);
    }

    @Override
    public Date getDate(String name, Date defaultValue) throws Exception {
        regristerParameter(PARAMETER_TYPE_DATE, name, defaultValue);
        try {
            Date result = getDateWithoutRegister(name);
            return null != result ? result : defaultValue;
        } catch (Exception e) {
            return defaultValue;
        }
    }

    @Override
    public String[] getStringArray(String name) throws Exception {
        regristerParameter(PARAMETER_TYPE_STRINGARRAY, name);
        return getStringArrayWithoutRegister(name);
    }

    @Override
    public void setRenderd() {
        this.renderd = true;
    }
}
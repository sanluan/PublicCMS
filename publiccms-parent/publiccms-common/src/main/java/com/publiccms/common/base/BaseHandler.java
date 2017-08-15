package com.publiccms.common.base;

import static com.publiccms.common.tools.CommonUtils.notEmpty;
import static org.apache.commons.logging.LogFactory.getLog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.apache.commons.logging.Log;

import com.publiccms.common.handler.RenderHandler;

/**
 * 指令处理器基类
 * 
 * BaseHandler
 *
 */
public abstract class BaseHandler implements RenderHandler {
    protected final Log log = getLog(getClass());
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
    public static final String PARAMETERS_CONTROLLER = "showParamters";
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
     * String数组类型参数
     * 
     * String array type parameter
     * 
     */
    public static final String PARAMETER_TYPE_STRINGARRAY = "stringArray";
    protected Map<String, Object> map = new LinkedHashMap<>();
    protected List<Map<String, Object>> parameterList;
    protected boolean regristerParamters;
    protected boolean renderd = false;

    /**
     * 注册参数
     * 
     * Regrister paramters
     */
    public void regristerParamters() {
        this.regristerParamters = getBooleanWithoutRegrister(PARAMETERS_CONTROLLER, false);
        if (regristerParamters) {
            parameterList = new ArrayList<>();
            put(PARAMETERS_NAME, parameterList);
        }
    }

    protected void regristerParamter(String type, String name) {
        regristerParamter(type, name, null);
    }

    protected void regristerParamter(String type, String name, Object defaultValue) {
        if (regristerParamters) {
            HashMap<String, Object> map = new HashMap<>();
            map.put("name", name);
            map.put("type", type);
            map.put("defaultValue", defaultValue);
            parameterList.add(map);
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
     * Get size
     * 
     * @return
     */
    public int getSize() {
        return map.size();
    }

    protected abstract Integer getIntegerWithoutRegrister(String name) throws Exception;

    protected abstract String[] getStringArrayWithoutRegrister(String name) throws Exception;

    protected abstract String getStringWithoutRegrister(String name) throws Exception;

    protected abstract Boolean getBooleanWithoutRegrister(String name) throws Exception;

    @Override
    public String getString(String name) throws Exception {
        regristerParamter(PARAMETER_TYPE_STRING, name);
        return getStringWithoutRegrister(name);
    }

    @Override
    public String getString(String name, String defaultValue) throws Exception {
        try {
            String result = getString(name);
            regristerParamter(PARAMETER_TYPE_STRING, name, defaultValue);
            return notEmpty(result) ? result : defaultValue;
        } catch (Exception e) {
            return defaultValue;
        }
    }

    @Override
    public Character getCharacter(String name) throws Exception {
        regristerParamter(PARAMETER_TYPE_CHAR, name);
        String result = getStringWithoutRegrister(name);
        if (notEmpty(result)) {
            return result.charAt(0);
        }
        return null;
    }

    @Override
    public Integer getInteger(String name) throws Exception {
        regristerParamter(PARAMETER_TYPE_INTEGER, name);
        return getIntegerWithoutRegrister(name);
    }

    @Override
    public int getInteger(String name, int defaultValue) {
        try {
            regristerParamter(PARAMETER_TYPE_INTEGER, name, defaultValue);
            Integer result = getIntegerWithoutRegrister(name);
            return notEmpty(result) ? result : defaultValue;
        } catch (Exception e) {
            return defaultValue;
        }
    }

    @Override
    public Integer[] getIntegerArray(String name) throws Exception {
        regristerParamter(PARAMETER_TYPE_INTEGERARRAY, name);
        String[] arr = getStringArrayWithoutRegrister(name);
        if (notEmpty(arr)) {
            Set<Integer> set = new TreeSet<>();
            for (String s : arr) {
                try {
                    set.add(Integer.valueOf(s));
                } catch (NumberFormatException e) {
                }
            }
            int i = 0;
            Integer[] ids = new Integer[set.size()];
            for (Integer number : set) {
                ids[i++] = number;
            }
            return ids;
        }
        return null;
    }

    @Override
    public Long[] getLongArray(String name) throws Exception {
        regristerParamter(PARAMETER_TYPE_LONGARRAY, name);
        String[] arr = getStringArrayWithoutRegrister(name);
        if (notEmpty(arr)) {
            Set<Long> set = new TreeSet<>();
            for (String s : arr) {
                try {
                    set.add(Long.valueOf(s));
                } catch (NumberFormatException e) {
                }
            }
            int i = 0;
            Long[] ids = new Long[set.size()];
            for (Long number : set) {
                ids[i++] = number;
            }
            return ids;
        }
        return null;
    }

    protected boolean getBooleanWithoutRegrister(String name, boolean defaultValue) {
        try {
            Boolean result = getBooleanWithoutRegrister(name);
            return notEmpty(result) ? result : defaultValue;
        } catch (Exception e) {
            return defaultValue;
        }
    }

    @Override
    public Boolean getBoolean(String name) throws Exception {
        regristerParamter(PARAMETER_TYPE_BOOLEAN, name);
        return getBooleanWithoutRegrister(name);
    }

    @Override
    public boolean getBoolean(String name, boolean defaultValue) throws Exception {
        regristerParamter(PARAMETER_TYPE_BOOLEAN, name, defaultValue);
        return getBooleanWithoutRegrister(name, defaultValue);
    }

    @Override
    public String[] getStringArray(String name) throws Exception {
        regristerParamter(PARAMETER_TYPE_STRINGARRAY, name);
        return getStringArrayWithoutRegrister(name);
    }

    @Override
    public void setRenderd() {
        this.renderd = true;
    }
}
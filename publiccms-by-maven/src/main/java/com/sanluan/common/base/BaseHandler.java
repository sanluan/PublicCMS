package com.sanluan.common.base;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import com.sanluan.common.handler.RenderHandler;

/**
 * 
 * BaseHandler 指令处理器基类
 *
 */
public abstract class BaseHandler extends Base implements RenderHandler {
    public static final String PARAMETERS_NAME = "parameters";
    public static final String PARAMETERS_CONTROLLER = "showParamters";
    public static final String PARAMETER_TYPE_STRING = "string";
    public static final String PARAMETER_TYPE_CHAR = "char";
    public static final String PARAMETER_TYPE_SHORT = "short";
    public static final String PARAMETER_TYPE_LONG = "long";
    public static final String PARAMETER_TYPE_DOUBLE = "double";
    public static final String PARAMETER_TYPE_LONGARRAY = "longArray";
    public static final String PARAMETER_TYPE_DATE = "date";
    public static final String PARAMETER_TYPE_BOOLEAN = "boolean";
    public static final String PARAMETER_TYPE_INTEGER = "integer";
    public static final String PARAMETER_TYPE_INTEGERARRAY = "integerArray";
    public static final String PARAMETER_TYPE_STRINGARRAY = "stringArray";
    protected Map<String, Object> map = new LinkedHashMap<String, Object>();
    protected List<Map<String, Object>> parameterList;
    protected boolean regristerParamters;
    protected boolean renderd = false;

    public void regristerParamters() throws Exception {
        this.regristerParamters = getBooleanWithoutRegrister(PARAMETERS_CONTROLLER, false);
        if (regristerParamters) {
            parameterList = new ArrayList<Map<String, Object>>();
            put(PARAMETERS_NAME, parameterList);
        }
    }

    protected void regristerParamter(String type, String name) {
        regristerParamter(type, name, null);
    }

    protected void regristerParamter(final String type, final String name, final Object defaultValue) {
        if (regristerParamters) {
            parameterList.add(new HashMap<String, Object>() {
                private static final long serialVersionUID = 1L;
                {
                    put("name", name);
                    put("type", type);
                    put("defaultValue", defaultValue);
                }
            });
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.sanluan.common.handler.RenderHandler#put(java.lang.String,
     * java.lang.Object)
     */
    @Override
    public RenderHandler put(String key, Object value) {
        map.put(key, value);
        return this;
    }

    public int getSize() {
        return map.size();
    }

    protected abstract Integer getIntegerWithoutRegrister(String name) throws Exception;

    protected abstract String[] getStringArrayWithoutRegrister(String name) throws Exception;

    protected abstract String getStringWithoutRegrister(String name) throws Exception;

    protected abstract Boolean getBooleanWithoutRegrister(String name) throws Exception;

    /*
     * (non-Javadoc)
     * 
     * @see com.sanluan.common.handler.RenderHandler#getString(java.lang.String)
     */
    @Override
    public String getString(String name) throws Exception {
        regristerParamter(PARAMETER_TYPE_STRING, name);
        return getStringWithoutRegrister(name);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.sanluan.common.handler.RenderHandler#getString(java.lang.String,
     * java.lang.String)
     */
    @Override
    public String getString(String name, String defaultValue) throws Exception {
        String result = getString(name);
        regristerParamter(PARAMETER_TYPE_STRING, name, defaultValue);
        return notEmpty(result) ? result : defaultValue;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.sanluan.common.handler.RenderHandler#getCharacter(java.lang.String)
     */
    @Override
    public Character getCharacter(String name) throws Exception {
        regristerParamter(PARAMETER_TYPE_CHAR, name);
        String result = getStringWithoutRegrister(name);
        if (notEmpty(result)) {
            return result.charAt(0);
        }
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.sanluan.common.handler.RenderHandler#getInteger(java.lang.String)
     */
    @Override
    public Integer getInteger(String name) throws Exception {
        regristerParamter(PARAMETER_TYPE_INTEGER, name);
        return getIntegerWithoutRegrister(name);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.sanluan.common.handler.RenderHandler#getInteger(java.lang.String,
     * int)
     */
    @Override
    public int getInteger(String name, int defaultValue) throws Exception {
        regristerParamter(PARAMETER_TYPE_INTEGER, name, defaultValue);
        Integer result = getIntegerWithoutRegrister(name);
        return notEmpty(result) ? result : defaultValue;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.sanluan.common.handler.RenderHandler#getIntegerArray(java.lang.String
     * )
     */
    @Override
    public Integer[] getIntegerArray(String name) throws Exception {
        regristerParamter(PARAMETER_TYPE_INTEGERARRAY, name);
        String[] arr = getStringArrayWithoutRegrister(name);
        if (notEmpty(arr)) {
            Set<Integer> set = new TreeSet<Integer>();
            for (String s : arr) {
                set.add(Integer.valueOf(s));
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

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.sanluan.common.handler.RenderHandler#getLongArray(java.lang.String)
     */
    @Override
    public Long[] getLongArray(String name) throws Exception {
        regristerParamter(PARAMETER_TYPE_LONGARRAY, name);
        String[] arr = getStringArrayWithoutRegrister(name);
        if (notEmpty(arr)) {
            Set<Long> set = new TreeSet<Long>();
            for (String s : arr) {
                set.add(Long.valueOf(s));
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

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.sanluan.common.handler.RenderHandler#getBoolean(java.lang.String,
     * java.lang.Boolean)
     */
    protected boolean getBooleanWithoutRegrister(String name, boolean defaultValue) throws Exception {
        Boolean result = getBooleanWithoutRegrister(name);
        return notEmpty(result) ? result : defaultValue;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.sanluan.common.handler.RenderHandler#getBoolean(java.lang.String)
     */
    @Override
    public Boolean getBoolean(String name) throws Exception {
        regristerParamter(PARAMETER_TYPE_BOOLEAN, name);
        return getBooleanWithoutRegrister(name);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.sanluan.common.handler.RenderHandler#getBoolean(java.lang.String,
     * java.lang.Boolean)
     */
    @Override
    public boolean getBoolean(String name, boolean defaultValue) throws Exception {
        regristerParamter(PARAMETER_TYPE_BOOLEAN, name, defaultValue);
        return getBooleanWithoutRegrister(name, defaultValue);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.sanluan.common.handler.RenderHandler#getStringArray(java.lang.String)
     */
    @Override
    public String[] getStringArray(String name) throws Exception {
        regristerParamter(PARAMETER_TYPE_STRINGARRAY, name);
        return getStringArrayWithoutRegrister(name);
    }
}
package com.publiccms.common.base;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.publiccms.common.tools.CommonUtils;
import com.publiccms.common.tools.TemplateModelUtils;

import freemarker.template.TemplateHashModelEx;
import freemarker.template.TemplateMethodModelEx;
import freemarker.template.TemplateModel;
import freemarker.template.TemplateModelException;

/**
 * 
 * BaseMethod FreeMarker自定义方法基类
 *
 */
public abstract class BaseMethod implements TemplateMethodModelEx, Base {
    protected final Log log = LogFactory.getLog(getClass());
    private String name;

    private static TemplateModel getModel(int index, List<TemplateModel> arguments) {
        if (CommonUtils.notEmpty(arguments) && index < arguments.size()) {
            return arguments.get(index);
        }
        return null;
    }

    /**
     * @param index
     * @param arguments
     * @return map value
     * @throws TemplateModelException
     */
    public static TemplateHashModelEx getMap(int index, List<TemplateModel> arguments) throws TemplateModelException {
        return TemplateModelUtils.converMap(getModel(index, arguments));
    }

    /**
     * @param index
     * @param arguments
     * @return string value
     * @throws TemplateModelException
     */
    public static String getString(int index, List<TemplateModel> arguments) throws TemplateModelException {
        return TemplateModelUtils.converString(getModel(index, arguments));
    }

    /**
     * @param index
     * @param arguments
     * @return int value
     * @throws TemplateModelException
     */
    public static Integer getInteger(int index, List<TemplateModel> arguments) throws TemplateModelException {
        return TemplateModelUtils.converInteger(getModel(index, arguments));
    }

    /**
     * @param index
     * @param arguments
     * @return short value
     * @throws TemplateModelException
     */
    public static Short getShort(int index, List<TemplateModel> arguments) throws TemplateModelException {
        return TemplateModelUtils.converShort(getModel(index, arguments));
    }

    /**
     * @param index
     * @param arguments
     * @return long value
     * @throws TemplateModelException
     */
    public static Long getLong(int index, List<TemplateModel> arguments) throws TemplateModelException {
        return TemplateModelUtils.converLong(getModel(index, arguments));
    }

    /**
     * @param index
     * @param arguments
     * @return double value
     * @throws TemplateModelException
     */
    public static Double getDouble(int index, List<TemplateModel> arguments) throws TemplateModelException {
        return TemplateModelUtils.converDouble(getModel(index, arguments));
    }

    /**
     * @param index
     * @param arguments
     * @return string array value
     * @throws TemplateModelException
     */
    public static String[] getStringArray(int index, List<TemplateModel> arguments) throws TemplateModelException {
        return TemplateModelUtils.converStringArray(getModel(index, arguments));
    }

    /**
     * @param index
     * @param arguments
     * @return int array value
     * @throws TemplateModelException
     */
    public static Integer[] getIntegerArray(int index, List<TemplateModel> arguments) throws TemplateModelException {
        String[] arr = getStringArray(index, arguments);
        if (CommonUtils.notEmpty(arr)) {
            Integer[] ids = new Integer[arr.length];
            int i = 0;
            for (String s : arr) {
                ids[i++] = Integer.valueOf(s);
            }
            return ids;
        }
        return null;
    }

    /**
     * @param index
     * @param arguments
     * @return long array value
     * @throws TemplateModelException
     */
    public static Long[] getLongArray(int index, List<TemplateModel> arguments) throws TemplateModelException {
        String[] arr = getStringArray(index, arguments);
        if (CommonUtils.notEmpty(arr)) {
            Long[] ids = new Long[arr.length];
            int i = 0;
            for (String s : arr) {
                ids[i++] = Long.valueOf(s);
            }
            return ids;
        }
        return null;
    }

    /**
     * @param index
     * @param arguments
     * @return bool value
     * @throws TemplateModelException
     */
    public static Boolean getBoolean(int index, List<TemplateModel> arguments) throws TemplateModelException {
        return TemplateModelUtils.converBoolean(getModel(index, arguments));
    }

    /**
     * @param index
     * @param arguments
     * @return date
     * @throws TemplateModelException
     * @throws ParseException
     */
    public static Date getDate(int index, List<TemplateModel> arguments) throws TemplateModelException, ParseException {
        return TemplateModelUtils.converDate(getModel(index, arguments));
    }

    /**
     * @return whether to enable http 
     */
    public boolean httpEnabled() {
        return true;
    }

    /**
     * @return min paramters number
     */
    public abstract int minParamtersNumber();

    /**
     * @return whether to need app token
     */
    public abstract boolean needAppToken();

    /**
     * @return name
     */
    public String getName() {
        return this.name;
    }

    /**
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }
}

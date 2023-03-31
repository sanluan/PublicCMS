package com.publiccms.common.handler;

import java.io.IOException;
import java.io.Writer;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import freemarker.template.TemplateException;
import freemarker.template.TemplateModelException;

/**
 *
 * RenderHandler
 * 
 */
public interface RenderHandler {

    /**
     * 渲染
     * 
     * @throws IOException
     * @throws TemplateModelException
     * @throws TemplateException 
     */
    void render() throws TemplateException, IOException;

    /**
     * 打印
     * 
     * @param value
     * @throws IOException
     */
    void print(String value) throws IOException;

    /**
     * 获取Writer
     * 
     * @return writer
     * @throws IOException
     */
    Writer getWriter() throws IOException;

    /**
     * @param key
     * @param value
     * @return render handler
     */
    RenderHandler put(String key, Object value);

    /**
     * @param name
     * @param defaultValue
     * @return string value
     * @throws TemplateModelException
     */
    String getString(String name, String defaultValue) throws TemplateModelException;

    /**
     * @param name
     * 
     * @return string value
     * @throws TemplateModelException
     */
    String getString(String name) throws TemplateModelException;

    /**
     * @param name
     * 
     * @return character value
     * @throws TemplateModelException
     */
    Character getCharacter(String name) throws TemplateModelException;

    /**
     * @param name
     * @param defaultValue
     * @return int value
     * @throws TemplateModelException
     */
    Integer getInteger(String name, Integer defaultValue) throws TemplateModelException;

    /**
     * @param name
     * 
     * @return int value
     * @throws TemplateModelException
     */
    Integer getInteger(String name) throws TemplateModelException;

    /**
     * @param name
     * 
     * @return byte value
     * @throws TemplateModelException
     */
    Byte getByte(String name) throws TemplateModelException;

    /**
     * @param name
     * @param defaultValue
     * @return byte value
     * @throws TemplateModelException
     */
    Byte getByte(String name, Byte defaultValue) throws TemplateModelException;

    /**
     * @param name
     * 
     * @return short value
     * @throws TemplateModelException
     */
    Short getShort(String name) throws TemplateModelException;

    /**
     * @param name
     * 
     * @return long value
     * @throws TemplateModelException
     */
    Long getLong(String name) throws TemplateModelException;

    /**
     * @param name
     * @param defaultValue
     * @return long value
     * @throws TemplateModelException
     */
    Long getLong(String name, Long defaultValue) throws TemplateModelException;

    /**
     * @param name
     * 
     * @return double value
     * @throws TemplateModelException
     */
    Double getDouble(String name) throws TemplateModelException;

    /**
     * @param name
     * 
     * @return bigDecimal value
     * @throws TemplateModelException
     */
    BigDecimal getBigDecimal(String name) throws TemplateModelException;

    /**
     * @param name
     * 
     * @return int array value
     * @throws TemplateModelException
     */
    Integer[] getIntegerArray(String name) throws TemplateModelException;

    /**
     * @param name
     * 
     * @return long array value
     * @throws TemplateModelException
     */
    Long[] getLongArray(String name) throws TemplateModelException;

    /**
     * @param name
     * 
     * @return long array value
     * @throws TemplateModelException
     */
    Short[] getShortArray(String name) throws TemplateModelException;

    /**
     * @param name
     * 
     * @return string array value
     * @throws TemplateModelException
     */
    String[] getStringArray(String name) throws TemplateModelException;

    /**
     * @param name
     * @param defaultValue
     * 
     * @return string array value
     * @throws TemplateModelException
     */
    String[] getStringArray(String name, String[] defaultValue) throws TemplateModelException;

    /**
     * @param name
     * @param defaultValue
     * @return bool value
     * @throws TemplateModelException
     */
    Boolean getBoolean(String name, Boolean defaultValue) throws TemplateModelException;

    /**
     * @param name
     * 
     * @return bool value
     * @throws TemplateModelException
     */
    Boolean getBoolean(String name) throws TemplateModelException;

    /**
     * @param name
     * 
     * @return date value
     * @throws TemplateModelException
     */
    Date getDate(String name) throws TemplateModelException;

    /**
     * @param name
     * @param defaultValue
     * 
     * @return date value
     * @throws TemplateModelException
     */
    Date getDate(String name, Date defaultValue) throws TemplateModelException;

    /**
     * @return locale
     */
    Locale getLocale();

    /**
     * @param name
     * @return map value
     * @throws TemplateModelException
     */
    public Map<String, String> getMap(String name) throws TemplateModelException;

    /**
     * @return request
     * @throws TemplateModelException
     */
    HttpServletRequest getRequest() throws TemplateModelException;

    /**
     * @param name
     * @return attribute
     * @throws TemplateModelException
     */
    Object getAttribute(String name) throws TemplateModelException;

    /**
     * @return if renderd
     */
    boolean getRenderd();

}
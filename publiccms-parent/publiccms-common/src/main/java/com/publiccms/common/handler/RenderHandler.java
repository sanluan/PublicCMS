package com.publiccms.common.handler;

import java.io.IOException;
import java.io.Writer;
import java.util.Date;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

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
     * @throws Exception
     */
    public void render() throws Exception;

    /**
     * 打印
     * 
     * @param value
     * @throws IOException
     */
    public void print(String value) throws IOException;

    /**
     * 获取Writer
     * 
     * @return writer
     * @throws IOException
     */
    public Writer getWriter() throws IOException;

    /**
     * @param key
     * @param value
     * @return render handler
     */
    public RenderHandler put(String key, Object value);


    /**
     * @param name
     * @param defaultValue
     * @return string value
     * @throws Exception
     */
    public String getString(String name, String defaultValue) throws Exception;

    /**
     * @param name
     * 
     * @return string value
     * @throws Exception
     */
    public String getString(String name) throws Exception;

    /**
     * @param name
     * 
     * @return character value
     * @throws Exception
     */
    public Character getCharacter(String name) throws Exception;

    /**
     * @param name
     * @param defaultValue
     * @return int value
     * @throws Exception
     * @throws Exception
     */
    public Integer getInteger(String name, Integer defaultValue) throws Exception;

    /**
     * @param name
     * 
     * @return int value
     * @throws Exception
     */
    public Integer getInteger(String name) throws Exception;

    /**
     * @param name
     * 
     * @return short value
     * @throws Exception
     */
    public Short getShort(String name) throws Exception;

    /**
     * @param name
     * 
     * @return long value
     * @throws Exception
     */
    public Long getLong(String name) throws Exception;

    /**
     * @param name
     * 
     * @return double value
     * @throws Exception
     */
    public Double getDouble(String name) throws Exception;

    /**
     * @param name
     * 
     * @return int array value
     * @throws Exception
     */
    public Integer[] getIntegerArray(String name) throws Exception;

    /**
     * @param name
     * 
     * @return long array value
     * @throws Exception
     */
    public Long[] getLongArray(String name) throws Exception;

    /**
     * @param name
     * 
     * @return long array value
     * @throws Exception
     */
    public Short[] getShortArray(String name) throws Exception;

    /**
     * @param name
     * 
     * @return string array value
     * @throws Exception
     */
    public String[] getStringArray(String name) throws Exception;

    /**
     * @param name
     * @param defaultValue
     * @return bool value
     * @throws Exception
     */
    public Boolean getBoolean(String name, Boolean defaultValue) throws Exception;

    /**
     * @param name
     * 
     * @return bool value
     * @throws Exception
     */
    public Boolean getBoolean(String name) throws Exception;

    /**
     * @param name
     * 
     * @return date value
     * @throws Exception
     */
    public Date getDate(String name) throws Exception;

    /**
     * @param name
     * @param defaultValue
     * 
     * @return date value
     * @throws Exception
     */
    public Date getDate(String name, Date defaultValue) throws Exception;

    /**
     * @return locale
     * @throws Exception
     */
    public Locale getLocale() throws Exception;

    /**
     * @return request
     * @throws IOException
     * @throws Exception
     */
    public HttpServletRequest getRequest() throws IOException, Exception;

    /**
     * @param name
     * @return attribute
     * @throws IOException
     * @throws Exception
     */
    public Object getAttribute(String name) throws IOException, Exception;

    /**
     * set renderd
     */
    public void setRenderd();

}
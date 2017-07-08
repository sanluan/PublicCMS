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
     * @return 
     * @throws IOException
     */
    public Writer getWriter() throws IOException;

    /**
     * @param key
     * @param value
     * @return
     */
    public RenderHandler put(String key, Object value);

    /**
     * @param name
     * @param defaultValue
     * @return
     * @throws Exception
     */
    public String getString(String name, String defaultValue) throws Exception;

    /**
     * @param name
     * 
     * @return
     * @throws Exception
     */
    public String getString(String name) throws Exception;

    /**
     * @param name
     * 
     * @return
     * @throws Exception
     */
    public Character getCharacter(String name) throws Exception;

    /**
     * @param name
     * @param defaultValue
     * @return
     * @throws Exception
     * @throws Exception
     */
    public int getInteger(String name, int defaultValue) throws Exception;

    /**
     * @param name
     * 
     * @return
     * @throws Exception
     */
    public Integer getInteger(String name) throws Exception;

    /**
     * @param name
     * 
     * @return
     * @throws Exception
     */
    public Short getShort(String name) throws Exception;

    /**
     * @param name
     * 
     * @return
     * @throws Exception
     */
    public Long getLong(String name) throws Exception;

    /**
     * @param name
     * 
     * @return
     * @throws Exception
     */
    public Double getDouble(String name) throws Exception;

    /**
     * @param name
     * 
     * @return
     * @throws Exception
     */
    public Integer[] getIntegerArray(String name) throws Exception;

    /**
     * @param name
     * 
     * @return
     * @throws Exception
     */
    public Long[] getLongArray(String name) throws Exception;

    /**
     * @param name
     * 
     * @return
     * @throws Exception
     */
    public String[] getStringArray(String name) throws Exception;

    /**
     * @param name
     * @param defaultValue
     * @return
     * @throws Exception
     */
    public boolean getBoolean(String name, boolean defaultValue) throws Exception;

    /**
     * @param name
     * 
     * @return
     * @throws Exception
     */
    public Boolean getBoolean(String name) throws Exception;

    /**
     * @param name
     * 
     * @return
     * @throws Exception
     */
    public Date getDate(String name) throws Exception;

    /**
     * @return
     * @throws Exception
     */
    public Locale getLocale() throws Exception;

    /**
     * @return
     * @throws IOException
     * @throws Exception
     */
    public HttpServletRequest getRequest() throws IOException, Exception;

    /**
     * @param name
     * @return
     * @throws IOException
     * @throws Exception
     */
    public Object getAttribute(String name) throws IOException, Exception;

    /**
     * 
     */
    public void setRenderd();

}
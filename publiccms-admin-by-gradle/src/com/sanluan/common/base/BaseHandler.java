package com.sanluan.common.base;

import static org.apache.commons.logging.LogFactory.getLog;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.apache.commons.logging.Log;

import com.sanluan.common.handler.RenderHandler;

/**
 * 
 * BaseHandler 指令处理器基类
 *
 */
public abstract class BaseHandler implements RenderHandler {
    protected Map<String, Object> map = new HashMap<String, Object>();
    protected final Log log = getLog(getClass());

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.sanluan.common.handler.RenderHandler#renderIfNotNull(java.lang.Object
     * )
     */
    @Override
    public void renderIfNotNull(Object notEmptyObject) throws IOException, Exception {
        if (null != notEmptyObject) {
            render();
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

    /*
     * (non-Javadoc)
     * 
     * @see com.sanluan.common.handler.RenderHandler#getString(java.lang.String,
     * java.lang.String)
     */
    @Override
    public String getString(String name, String defaultValue) throws Exception {
        String result = getString(name);
        return null == result ? defaultValue : result;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.sanluan.common.handler.RenderHandler#getInteger(java.lang.String,
     * int)
     */
    @Override
    public Integer getInteger(String name, int defaultValue) throws Exception {
        Integer result = getInteger(name);
        return null == result ? defaultValue : result;
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
        String[] arr = getStringArray(name);
        if (null != arr) {
            try {
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
            } catch (NumberFormatException e) {
                log.debug(e.getMessage());
            }
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
        String[] arr = getStringArray(name);
        if (null != arr) {
            try {
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
            } catch (NumberFormatException e) {
                log.debug(e.getMessage());
            }
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
    @Override
    public Boolean getBoolean(String name, Boolean defaultValue) throws Exception {
        Boolean result = getBoolean(name);
        return null == result ? defaultValue : result;
    }
}

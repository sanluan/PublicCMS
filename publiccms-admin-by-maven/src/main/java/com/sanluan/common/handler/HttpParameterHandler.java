package com.sanluan.common.handler;

import static com.sanluan.common.constants.CommonConstants.FULL_DATE_FORMAT;
import static com.sanluan.common.constants.CommonConstants.FULL_DATE_LENGTH;
import static com.sanluan.common.constants.CommonConstants.SHORT_DATE_FORMAT;
import static com.sanluan.common.constants.CommonConstants.SHORT_DATE_LENGTH;
import static org.apache.commons.lang3.ArrayUtils.isNotEmpty;
import static org.apache.commons.lang3.StringUtils.trimToEmpty;
import static org.apache.commons.lang3.StringUtils.split;
import static org.apache.commons.logging.LogFactory.getLog;

import java.io.IOException;
import java.text.ParseException;
import java.util.Date;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.http.server.ServletServerHttpResponse;

import com.sanluan.common.base.BaseHandler;

public class HttpParameterHandler extends BaseHandler {
    private MediaType mediaType;
    private HttpMessageConverter<Object> httpMessageConverter;
    private Map<String, String[]> parameterMap;
    private ServletServerHttpResponse response;
    private String callback;
    private final Log log = getLog(getClass());

    public HttpParameterHandler(HttpMessageConverter<Object> httpMessageConverter, MediaType mediaType,
            Map<String, String[]> parameterMap, String callback, HttpServletResponse response) {
        this.httpMessageConverter = httpMessageConverter;
        this.parameterMap = parameterMap;
        this.callback = callback;
        this.response = new ServletServerHttpResponse(response);
        this.mediaType = mediaType;
    }

    @Override
    public void render() throws IOException, Exception {
        MappingJacksonValue mappingJacksonValue = new MappingJacksonValue(map);
        mappingJacksonValue.setJsonpFunction(callback);
        httpMessageConverter.write(mappingJacksonValue, mediaType, response);
    }

    @Override
    public String getString(String name) throws Exception {
        String[] values = parameterMap.get(name);
        if (isNotEmpty(values)) {
            return values[0];
        }
        return null;
    }

    @Override
    public Integer getInteger(String name) throws Exception {
        String result = getString(name);
        if (null != result) {
            try {
                return Integer.valueOf(result);
            } catch (NumberFormatException e) {
                log.debug(e.getMessage());
            }
        }
        return null;
    }

    @Override
    public Short getShort(String name) throws Exception {
        String result = getString(name);
        if (null != result) {
            try {
                return Short.valueOf(result);
            } catch (NumberFormatException e) {
                log.debug(e.getMessage());
            }
        }
        return null;
    }

    @Override
    public Long getLong(String name) throws Exception {
        String result = getString(name);
        if (null != result) {
            try {
                return Long.valueOf(result);
            } catch (NumberFormatException e) {
                log.debug(e.getMessage());
            }
        }
        return null;
    }

    @Override
    public Double getDouble(String name) throws Exception {
        String result = getString(name);
        if (null != result) {
            try {
                return Double.valueOf(result);
            } catch (NumberFormatException e) {
                log.debug(e.getMessage());
            }
        }
        return null;
    }

    @Override
    public String[] getStringArray(String name) throws Exception {
        String[] values = parameterMap.get(name);
        if (isNotEmpty(values) && 1 == values.length && 0 <= values[0].indexOf(",")) {
            return split(values[0], ',');
        }
        return values;
    }

    @Override
    public Boolean getBoolean(String name) throws Exception {
        String result = getString(name);
        if (null != result) {
            try {
                return Boolean.valueOf(result);
            } catch (NumberFormatException e) {
                log.debug(e.getMessage());
            }
        }
        return null;
    }

    @Override
    public Date getDate(String name) throws Exception {
        String result = getString(name);
        if (null != result) {
            try {
                String temp = trimToEmpty(result);
                try {
                    if (FULL_DATE_LENGTH == temp.length()) {
                        return FULL_DATE_FORMAT.parse(temp);
                    } else if (SHORT_DATE_LENGTH == temp.length()) {
                        return SHORT_DATE_FORMAT.parse(temp);
                    }
                } catch (ParseException e) {
                    log.debug(e.getMessage());
                }
            } catch (NumberFormatException e) {
                log.debug(e.getMessage());
            }
        }
        return null;
    }
}

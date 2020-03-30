package com.publiccms.common.handler;

import java.io.IOException;
import java.io.Writer;
import java.text.ParseException;
import java.util.Date;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.web.servlet.support.RequestContextUtils;

import com.publiccms.common.base.BaseHandler;
import com.publiccms.common.constants.Constants;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.common.tools.DateFormatUtils;

/**
 *
 * HttpParameterHandler
 * 
 */
public class HttpParameterHandler extends BaseHandler {

    private MediaType mediaType;
    private HttpMessageConverter<Object> httpMessageConverter;
    private HttpServletRequest request;
    private HttpServletResponse response;

    /**
     * @param httpMessageConverter
     * @param mediaType
     * @param request
     * @param response
     */
    public HttpParameterHandler(HttpMessageConverter<Object> httpMessageConverter, MediaType mediaType,
            HttpServletRequest request, HttpServletResponse response) {
        this.httpMessageConverter = httpMessageConverter;
        this.request = request;
        this.response = response;
        this.mediaType = mediaType;
        regristerParameters();
    }

    @Override
    public void render() throws HttpMessageNotWritableException, IOException {
        if (!renderd) {
            httpMessageConverter.write(map, mediaType, new ServletServerHttpResponse(response));
            renderd = true;
        }
    }

    @Override
    public void print(String value) throws IOException {
        response.getWriter().print(value);
    }

    @Override
    public Writer getWriter() throws IOException {
        return response.getWriter();
    }

    @Override
    protected String getStringWithoutRegister(String name) {
        return request.getParameter(name);
    }

    @Override
    protected Integer getIntegerWithoutRegister(String name) {
        String result = getStringWithoutRegister(name);
        if (CommonUtils.notEmpty(result)) {
            try {
                return Integer.valueOf(result);
            } catch (NumberFormatException e) {
                return null;
            }
        }
        return null;
    }

    @Override
    public Short getShort(String name) {
        regristerParameter(PARAMETER_TYPE_STRING, name);
        String result = getStringWithoutRegister(name);
        if (CommonUtils.notEmpty(result)) {
            try {
                return Short.valueOf(result);
            } catch (NumberFormatException e) {
                return null;
            }
        }
        return null;
    }

    @Override
    public Long getLong(String name) {
        regristerParameter(PARAMETER_TYPE_LONG, name);
        String result = getStringWithoutRegister(name);
        if (CommonUtils.notEmpty(result)) {
            try {
                return Long.valueOf(result);
            } catch (NumberFormatException e) {
                return null;
            }
        }
        return null;
    }

    @Override
    public Double getDouble(String name) {
        regristerParameter(PARAMETER_TYPE_DOUBLE, name);
        String result = getStringWithoutRegister(name);
        if (CommonUtils.notEmpty(result)) {
            try {
                return Double.valueOf(result);
            } catch (NumberFormatException e) {
                return null;
            }
        }
        return null;
    }

    @Override
    protected String[] getStringArrayWithoutRegister(String name) {
        String[] values = request.getParameterValues(name);
        if (CommonUtils.notEmpty(values) && 1 == values.length && values[0].contains(Constants.COMMA_DELIMITED)) {
            return StringUtils.split(values[0], Constants.COMMA_DELIMITED);
        }
        return values;
    }

    @Override
    protected Boolean getBooleanWithoutRegister(String name) {
        String result = getStringWithoutRegister(name);
        if (CommonUtils.notEmpty(result)) {
            return Boolean.valueOf(result);
        }
        return null;
    }

    @Override
    public Date getDateWithoutRegister(String name) throws ParseException {
        String result = getStringWithoutRegister(name);
        if (CommonUtils.notEmpty(result)) {
            String temp = StringUtils.trimToEmpty(result);
            if (DateFormatUtils.FULL_DATE_LENGTH == temp.length()) {
                return DateFormatUtils.getDateFormat(DateFormatUtils.FULL_DATE_FORMAT_STRING).parse(temp);
            } else if (DateFormatUtils.SHORT_DATE_LENGTH == temp.length()) {
                return DateFormatUtils.getDateFormat(DateFormatUtils.SHORT_DATE_FORMAT_STRING).parse(temp);
            } else {
                try {
                    return new Date(Long.parseLong(temp));
                } catch (NumberFormatException e) {
                    return null;
                }
            }
        }
        return null;
    }

    @Override
    public HttpServletRequest getRequest() throws IOException, Exception {
        return request;
    }

    @Override
    public Object getAttribute(String name) throws IOException, Exception {
        return request.getAttribute(name);
    }

    @Override
    public Locale getLocale() throws Exception {
        return RequestContextUtils.getLocale(request);
    }

}

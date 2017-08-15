package com.publiccms.common.handler;

import static com.publiccms.common.tools.CommonUtils.notEmpty;
import static com.publiccms.common.tools.TemplateModelUtils.FULL_DATE_FORMAT;
import static com.publiccms.common.tools.TemplateModelUtils.FULL_DATE_LENGTH;
import static com.publiccms.common.tools.TemplateModelUtils.SHORT_DATE_FORMAT;
import static com.publiccms.common.tools.TemplateModelUtils.SHORT_DATE_LENGTH;
import static org.apache.commons.lang3.StringUtils.split;
import static org.apache.commons.lang3.StringUtils.trimToEmpty;

import java.io.IOException;
import java.io.Writer;
import java.text.ParseException;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.web.servlet.support.RequestContextUtils;

import com.publiccms.common.base.Base;
import com.publiccms.common.base.BaseHandler;

/**
 *
 * HttpParameterHandler
 * 
 */
public class HttpParameterHandler extends BaseHandler implements Base {

    /**
     * 
     */
    public static final Pattern FUNCTIONNAME_PATTERN = Pattern.compile("[0-9A-Za-z_\\.]*");

    private MediaType mediaType;
    private HttpMessageConverter<Object> httpMessageConverter;
    private HttpServletRequest request;
    private HttpServletResponse response;
    private String callback;

    /**
     * @param httpMessageConverter
     * @param mediaType
     * @param request
     * @param callback
     * @param response
     */
    public HttpParameterHandler(HttpMessageConverter<Object> httpMessageConverter, MediaType mediaType,
            HttpServletRequest request, String callback, HttpServletResponse response) {
        this.httpMessageConverter = httpMessageConverter;
        this.request = request;
        this.callback = callback;
        this.response = response;
        this.mediaType = mediaType;
        regristerParamters();
    }

    @Override
    public void render() throws HttpMessageNotWritableException, IOException {
        if (!renderd) {
            MappingJacksonValue mappingJacksonValue = new MappingJacksonValue(map);
            if (notEmpty(callback)) {
                Matcher m = FUNCTIONNAME_PATTERN.matcher(callback);
                if (m.matches()) {
                    mappingJacksonValue.setJsonpFunction(callback);
                }
            }
            httpMessageConverter.write(mappingJacksonValue, mediaType, new ServletServerHttpResponse(response));
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
    protected String getStringWithoutRegrister(String name) {
        return request.getParameter(name);
    }

    @Override
    protected Integer getIntegerWithoutRegrister(String name) {
        String result = getStringWithoutRegrister(name);
        if (notEmpty(result)) {
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
        regristerParamter(PARAMETER_TYPE_STRING, name);
        String result = getStringWithoutRegrister(name);
        if (notEmpty(result)) {
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
        regristerParamter(PARAMETER_TYPE_LONG, name);
        String result = getStringWithoutRegrister(name);
        if (notEmpty(result)) {
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
        regristerParamter(PARAMETER_TYPE_DOUBLE, name);
        String result = getStringWithoutRegrister(name);
        if (notEmpty(result)) {
            try {
                return Double.valueOf(result);
            } catch (NumberFormatException e) {
                return null;
            }
        }
        return null;
    }

    @Override
    protected String[] getStringArrayWithoutRegrister(String name) {
        String[] values = request.getParameterValues(name);
        if (notEmpty(values) && 1 == values.length && 0 <= values[0].indexOf(COMMA_DELIMITED)) {
            return split(values[0], COMMA_DELIMITED);
        }
        return values;
    }

    @Override
    protected Boolean getBooleanWithoutRegrister(String name) {
        String result = getStringWithoutRegrister(name);
        if (notEmpty(result)) {
            return Boolean.valueOf(result);
        }
        return null;
    }

    @Override
    public Date getDate(String name) throws ParseException {
        regristerParamter(PARAMETER_TYPE_DATE, name);
        String result = getStringWithoutRegrister(name);
        if (notEmpty(result)) {
            String temp = trimToEmpty(result);
            if (FULL_DATE_LENGTH == temp.length()) {
                synchronized (FULL_DATE_FORMAT) {
                    return FULL_DATE_FORMAT.parse(temp);
                }
            } else if (SHORT_DATE_LENGTH == temp.length()) {
                synchronized (SHORT_DATE_FORMAT) {
                    return SHORT_DATE_FORMAT.parse(temp);
                }
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

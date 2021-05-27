package com.publiccms.common.view;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.MessageSourceResolvable;
import org.springframework.context.NoSuchMessageException;
import org.springframework.context.i18n.LocaleContext;
import org.springframework.context.i18n.SimpleTimeZoneAwareLocaleContext;
import org.springframework.context.i18n.TimeZoneAwareLocaleContext;
import org.springframework.lang.Nullable;
import org.springframework.ui.context.Theme;
import org.springframework.ui.context.ThemeSource;
import org.springframework.ui.context.support.ResourceBundleThemeSource;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.EscapedErrors;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.LocaleContextResolver;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.ThemeResolver;
import org.springframework.web.servlet.support.RequestContext;
import org.springframework.web.servlet.support.RequestContextUtils;
import org.springframework.web.servlet.support.RequestDataValueProcessor;
import org.springframework.web.util.HtmlUtils;
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.web.util.UrlPathHelper;
import org.springframework.web.util.WebUtils;

public class SafeRequestContext {

    public static final String DEFAULT_THEME_NAME = "theme";

    public static final String WEB_APPLICATION_CONTEXT_ATTRIBUTE = RequestContext.class.getName() + ".CONTEXT";

    private HttpServletRequest request;

    @Nullable
    private HttpServletResponse response;

    @Nullable
    private Map<String, Object> model;

    private WebApplicationContext webApplicationContext;

    @Nullable
    private Locale locale;

    @Nullable
    private TimeZone timeZone;

    @Nullable
    private Theme theme;

    @Nullable
    private Boolean defaultHtmlEscape;

    @Nullable
    private Boolean responseEncodedHtmlEscape;

    private UrlPathHelper urlPathHelper;

    @Nullable
    private RequestDataValueProcessor requestDataValueProcessor;

    @Nullable
    private Map<String, Errors> errorsMap;

    public SafeRequestContext(HttpServletRequest request, @Nullable HttpServletResponse response,
            @Nullable ServletContext servletContext, @Nullable Map<String, Object> model) {

        this.request = request;
        this.response = response;
        this.model = model;

        WebApplicationContext wac = (WebApplicationContext) request.getAttribute(WEB_APPLICATION_CONTEXT_ATTRIBUTE);
        if (wac == null) {
            wac = RequestContextUtils.findWebApplicationContext(request, servletContext);
            if (wac == null) {
                throw new IllegalStateException("No WebApplicationContext found: not in a DispatcherServlet "
                        + "request and no ContextLoaderListener registered?");
            }
        }
        this.webApplicationContext = wac;

        Locale locale = null;
        TimeZone timeZone = null;

        LocaleResolver localeResolver = RequestContextUtils.getLocaleResolver(request);
        if (localeResolver instanceof LocaleContextResolver) {
            LocaleContext localeContext = ((LocaleContextResolver) localeResolver).resolveLocaleContext(request);
            locale = localeContext.getLocale();
            if (localeContext instanceof TimeZoneAwareLocaleContext) {
                timeZone = ((TimeZoneAwareLocaleContext) localeContext).getTimeZone();
            }
        } else if (localeResolver != null) {
            locale = localeResolver.resolveLocale(request);
        }

        this.locale = locale;
        this.timeZone = timeZone;

        this.defaultHtmlEscape = WebUtils.getDefaultHtmlEscape(this.webApplicationContext.getServletContext());

        this.responseEncodedHtmlEscape = WebUtils.getResponseEncodedHtmlEscape(this.webApplicationContext.getServletContext());

        this.urlPathHelper = new UrlPathHelper();

        if (this.webApplicationContext.containsBean(RequestContextUtils.REQUEST_DATA_VALUE_PROCESSOR_BEAN_NAME)) {
            this.requestDataValueProcessor = this.webApplicationContext
                    .getBean(RequestContextUtils.REQUEST_DATA_VALUE_PROCESSOR_BEAN_NAME, RequestDataValueProcessor.class);
        }
    }

    protected final HttpServletRequest getRequest() {
        return this.request;
    }

    /**
     * Return the underlying ServletContext.
     */
    @Nullable
    protected final ServletContext getServletContext() {
        return this.webApplicationContext.getServletContext();
    }

    /**
     * 
     * @return the populated model Map, or {@code null} if none available
     */
    @Nullable
    public final Map<String, Object> getModel() {
        return this.model;
    }

    /**
     * @return current Locale (never {@code null})
     * 
     */
    public final Locale getLocale() {
        return (this.locale != null ? this.locale : getFallbackLocale());
    }

    /**
     * @return current timezone (never {@code null})
     */
    @Nullable
    public TimeZone getTimeZone() {
        return (this.timeZone != null ? this.timeZone : getFallbackTimeZone());
    }

    /**
     * @return the fallback locale (never {@code null})
     */
    protected Locale getFallbackLocale() {
        return getRequest().getLocale();
    }

    /**
     * 
     * @return the fallback time zone (or {@code null} if none derivable from
     *         the request)
     */
    @Nullable
    protected TimeZone getFallbackTimeZone() {
        return null;
    }

    /**
     * 
     * @param locale
     *            the new locale
     */
    public void changeLocale(Locale locale) {
        LocaleResolver localeResolver = RequestContextUtils.getLocaleResolver(this.request);
        if (localeResolver == null) {
            throw new IllegalStateException("Cannot change locale if no LocaleResolver configured");
        }
        localeResolver.setLocale(this.request, this.response, locale);
        this.locale = locale;
    }

    /**
     * 
     * @param locale
     *            the new locale
     * @param timeZone
     *            the new time zone
     */
    public void changeLocale(Locale locale, TimeZone timeZone) {
        LocaleResolver localeResolver = RequestContextUtils.getLocaleResolver(this.request);
        if (!(localeResolver instanceof LocaleContextResolver)) {
            throw new IllegalStateException("Cannot change locale context if no LocaleContextResolver configured");
        }
        ((LocaleContextResolver) localeResolver).setLocaleContext(this.request, this.response,
                new SimpleTimeZoneAwareLocaleContext(locale, timeZone));
        this.locale = locale;
        this.timeZone = timeZone;
    }

    /**
     * @return current theme (never {@code null}).
     */
    public Theme getTheme() {
        if (this.theme == null) {
            this.theme = RequestContextUtils.getTheme(this.request);
            if (this.theme == null) {
                this.theme = getFallbackTheme();
            }
        }
        return this.theme;
    }

    /**
     * 
     * @return the fallback theme (never {@code null})
     */
    protected Theme getFallbackTheme() {
        ThemeSource themeSource = RequestContextUtils.getThemeSource(getRequest());
        if (themeSource == null) {
            themeSource = new ResourceBundleThemeSource();
        }
        Theme theme = themeSource.getTheme(DEFAULT_THEME_NAME);
        if (theme == null) {
            throw new IllegalStateException("No theme defined and no fallback theme found");
        }
        return theme;
    }

    /**
     * 
     * @param theme
     *            the new theme
     * @see ThemeResolver#setThemeName
     */
    public void changeTheme(@Nullable Theme theme) {
        ThemeResolver themeResolver = RequestContextUtils.getThemeResolver(this.request);
        if (themeResolver == null) {
            throw new IllegalStateException("Cannot change theme if no ThemeResolver configured");
        }
        themeResolver.setThemeName(this.request, this.response, (theme != null ? theme.getName() : null));
        this.theme = theme;
    }

    /**
     * 
     * @param themeName
     *            the name of the new theme
     * @see ThemeResolver#setThemeName
     */
    public void changeTheme(String themeName) {
        ThemeResolver themeResolver = RequestContextUtils.getThemeResolver(this.request);
        if (themeResolver == null) {
            throw new IllegalStateException("Cannot change theme if no ThemeResolver configured");
        }
        themeResolver.setThemeName(this.request, this.response, themeName);
        // Ask for re-resolution on next getTheme call.
        this.theme = null;
    }

    /**
     * @param defaultHtmlEscape 
     * 
     */
    public void setDefaultHtmlEscape(boolean defaultHtmlEscape) {
        this.defaultHtmlEscape = defaultHtmlEscape;
    }

    /**
     * @return Is default HTML escaping active? Falls back to {@code false} in case of
     * no explicit default given.
     */
    public boolean isDefaultHtmlEscape() {
        return (this.defaultHtmlEscape != null && this.defaultHtmlEscape.booleanValue());
    }

    /**
     * 
     * @return whether default HTML escaping is enabled (null = no explicit
     *         default)
     */
    @Nullable
    public Boolean getDefaultHtmlEscape() {
        return this.defaultHtmlEscape;
    }

    /**
     * @return Is HTML escaping using the response encoding by default? If enabled, only
     * XML markup significant characters will be escaped with UTF-* encodings.
     */
    public boolean isResponseEncodedHtmlEscape() {
        return (this.responseEncodedHtmlEscape == null || this.responseEncodedHtmlEscape.booleanValue());
    }

    /**
     * @return whether default use of response encoding HTML escaping is enabled
     *         (null = no explicit default)
     * @since 4.1.2
     */
    @Nullable
    public Boolean getResponseEncodedHtmlEscape() {
        return this.responseEncodedHtmlEscape;
    }

    /**
     * @param urlPathHelper 
     */
    public void setUrlPathHelper(UrlPathHelper urlPathHelper) {
        Assert.notNull(urlPathHelper, "UrlPathHelper must not be null");
        this.urlPathHelper = urlPathHelper;
    }

    /**
     * @return the UrlPathHelper used for context path and request URI decoding.
     * Can be used to configure the current UrlPathHelper.
     */
    public UrlPathHelper getUrlPathHelper() {
        return this.urlPathHelper;
    }

    /**
     * @return the RequestDataValueProcessor instance to use obtained from the
     * WebApplicationContext under the name {@code "requestDataValueProcessor"}.
     * Or {@code null} if no matching bean was found.
     */
    @Nullable
    public RequestDataValueProcessor getRequestDataValueProcessor() {
        return this.requestDataValueProcessor;
    }

    /**
     * @return the context path of the original request
     */
    public String getContextPath() {
        return this.urlPathHelper.getOriginatingContextPath(this.request);
    }

    /**
     * 
     * @param relativeUrl
     *            the relative URL part
     * @return a URL that points back to the server with an absolute path (also
     *         URL-encoded accordingly)
     */
    public String getContextUrl(String relativeUrl) {
        String url = getContextPath() + relativeUrl;
        if (this.response != null) {
            url = this.response.encodeURL(url);
        }
        return url;
    }

    /**
     * @param relativeUrl
     *            the relative URL part
     * @param params
     *            a map of parameters to insert as placeholders in the url
     * @return a URL that points back to the server with an absolute path (also
     *         URL-encoded accordingly)
     */
    public String getContextUrl(String relativeUrl, Map<String, ?> params) {
        String url = getContextPath() + relativeUrl;
        url = UriComponentsBuilder.fromUriString(url).buildAndExpand(params).encode().toUri().toASCIIString();
        if (this.response != null) {
            url = this.response.encodeURL(url);
        }
        return url;
    }

    /**
     * @return the path to URL mappings within the current servlet including the
     *         context path and the servlet path of the original request.
     */
    public String getPathToServlet() {
        String path = this.urlPathHelper.getOriginatingContextPath(this.request);
        if (StringUtils.hasText(this.urlPathHelper.getPathWithinServletMapping(this.request))) {
            path += this.urlPathHelper.getOriginatingServletPath(this.request);
        }
        return path;
    }

    /**
     * @return the request URI of the original request
     */
    public String getRequestUri() {
        return this.urlPathHelper.getOriginatingRequestUri(this.request);
    }

    /**
     * @return the query string of the current request
     */
    public String getQueryString() {
        return this.urlPathHelper.getOriginatingQueryString(this.request);
    }

    /**
     * 
     * @param code
     *            the code of the message
     * @param defaultMessage
     *            the String to return if the lookup fails
     * @return the message
     */
    public String getMessage(String code, String defaultMessage) {
        return getMessage(code, null, defaultMessage, isDefaultHtmlEscape());
    }

    /**
     * 
     * @param code
     *            the code of the message
     * @param args
     *            arguments for the message, or {@code null} if none
     * @param defaultMessage
     *            the String to return if the lookup fails
     * @return the message
     */
    public String getMessage(String code, @Nullable Object[] args, String defaultMessage) {
        return getMessage(code, args, defaultMessage, isDefaultHtmlEscape());
    }

    /**
     * 
     * @param code
     *            the code of the message
     * @param args
     *            arguments for the message as a List, or {@code null} if none
     * @param defaultMessage
     *            the String to return if the lookup fails
     * @return the message
     */
    public String getMessage(String code, @Nullable List<?> args, String defaultMessage) {
        return getMessage(code, (args != null ? args.toArray() : null), defaultMessage, isDefaultHtmlEscape());
    }

    /**
     * 
     * @param code
     *            the code of the message
     * @param args
     *            arguments for the message, or {@code null} if none
     * @param defaultMessage
     *            the String to return if the lookup fails
     * @param htmlEscape
     *            if the message should be HTML-escaped
     * @return the message
     */
    public String getMessage(String code, @Nullable Object[] args, String defaultMessage, boolean htmlEscape) {
        String msg = this.webApplicationContext.getMessage(code, args, defaultMessage, getLocale());
        if (msg == null) {
            return "";
        }
        return (htmlEscape ? HtmlUtils.htmlEscape(msg) : msg);
    }

    /**
     * 
     * @param code
     *            the code of the message
     * @return the message
     * @throws org.springframework.context.NoSuchMessageException
     *             if not found
     */
    public String getMessage(String code) throws NoSuchMessageException {
        return getMessage(code, null, isDefaultHtmlEscape());
    }

    /**
     * 
     * @param code
     *            the code of the message
     * @param args
     *            arguments for the message, or {@code null} if none
     * @return the message
     * @throws org.springframework.context.NoSuchMessageException
     *             if not found
     */
    public String getMessage(String code, @Nullable Object[] args) throws NoSuchMessageException {
        return getMessage(code, args, isDefaultHtmlEscape());
    }

    /**
     * 
     * @param code
     *            the code of the message
     * @param args
     *            arguments for the message as a List, or {@code null} if none
     * @return the message
     * @throws org.springframework.context.NoSuchMessageException
     *             if not found
     */
    public String getMessage(String code, @Nullable List<?> args) throws NoSuchMessageException {
        return getMessage(code, (args != null ? args.toArray() : null), isDefaultHtmlEscape());
    }

    /**
     * 
     * @param code
     *            the code of the message
     * @param args
     *            arguments for the message, or {@code null} if none
     * @param htmlEscape
     *            if the message should be HTML-escaped
     * @return the message
     * @throws org.springframework.context.NoSuchMessageException
     *             if not found
     */
    public String getMessage(String code, @Nullable Object[] args, boolean htmlEscape) throws NoSuchMessageException {
        String msg = this.webApplicationContext.getMessage(code, args, getLocale());
        return (htmlEscape ? HtmlUtils.htmlEscape(msg) : msg);
    }

    /**
     * 
     * @param resolvable
     *            the MessageSourceResolvable
     * @return the message
     * @throws org.springframework.context.NoSuchMessageException
     *             if not found
     */
    public String getMessage(MessageSourceResolvable resolvable) throws NoSuchMessageException {
        return getMessage(resolvable, isDefaultHtmlEscape());
    }

    /**
     * 
     * @param resolvable
     *            the MessageSourceResolvable
     * @param htmlEscape
     *            if the message should be HTML-escaped
     * @return the message
     * @throws org.springframework.context.NoSuchMessageException
     *             if not found
     */
    public String getMessage(MessageSourceResolvable resolvable, boolean htmlEscape) throws NoSuchMessageException {
        String msg = this.webApplicationContext.getMessage(resolvable, getLocale());
        return (htmlEscape ? HtmlUtils.htmlEscape(msg) : msg);
    }

    /**
     * 
     * @param code
     *            the code of the message
     * @param defaultMessage
     *            the String to return if the lookup fails
     * @return the message
     */
    public String getThemeMessage(String code, String defaultMessage) {
        String msg = getTheme().getMessageSource().getMessage(code, null, defaultMessage, getLocale());
        return (msg != null ? msg : "");
    }

    /**
     * 
     * @param code
     *            the code of the message
     * @param args
     *            arguments for the message, or {@code null} if none
     * @param defaultMessage
     *            the String to return if the lookup fails
     * @return the message
     */
    public String getThemeMessage(String code, @Nullable Object[] args, String defaultMessage) {
        String msg = getTheme().getMessageSource().getMessage(code, args, defaultMessage, getLocale());
        return (msg != null ? msg : "");
    }

    /**
     * 
     * @param code
     *            the code of the message
     * @param args
     *            arguments for the message as a List, or {@code null} if none
     * @param defaultMessage
     *            the String to return if the lookup fails
     * @return the message
     */
    public String getThemeMessage(String code, @Nullable List<?> args, String defaultMessage) {
        String msg = getTheme().getMessageSource().getMessage(code, (args != null ? args.toArray() : null), defaultMessage,
                getLocale());
        return (msg != null ? msg : "");
    }

    /**
     * 
     * @param code
     *            the code of the message
     * @return the message
     * @throws org.springframework.context.NoSuchMessageException
     *             if not found
     */
    public String getThemeMessage(String code) throws NoSuchMessageException {
        return getTheme().getMessageSource().getMessage(code, null, getLocale());
    }

    /**
     * 
     * @param code
     *            the code of the message
     * @param args
     *            arguments for the message, or {@code null} if none
     * @return the message
     * @throws org.springframework.context.NoSuchMessageException
     *             if not found
     */
    public String getThemeMessage(String code, @Nullable Object[] args) throws NoSuchMessageException {
        return getTheme().getMessageSource().getMessage(code, args, getLocale());
    }

    /**
     * 
     * @param code
     *            the code of the message
     * @param args
     *            arguments for the message as a List, or {@code null} if none
     * @return the message
     * @throws org.springframework.context.NoSuchMessageException
     *             if not found
     */
    public String getThemeMessage(String code, @Nullable List<?> args) throws NoSuchMessageException {
        return getTheme().getMessageSource().getMessage(code, (args != null ? args.toArray() : null), getLocale());
    }

    /**
     * 
     * @param resolvable
     *            the MessageSourceResolvable
     * @return the message
     * @throws org.springframework.context.NoSuchMessageException
     *             if not found
     */
    public String getThemeMessage(MessageSourceResolvable resolvable) throws NoSuchMessageException {
        return getTheme().getMessageSource().getMessage(resolvable, getLocale());
    }

    /**
     * 
     * @param name
     *            the name of the bind object
     * @return the Errors instance, or {@code null} if not found
     */
    @Nullable
    public Errors getErrors(String name) {
        return getErrors(name, isDefaultHtmlEscape());
    }

    /**
     * 
     * @param name
     *            the name of the bind object
     * @param htmlEscape
     *            create an Errors instance with automatic HTML escaping?
     * @return the Errors instance, or {@code null} if not found
     */
    @Nullable
    public Errors getErrors(String name, boolean htmlEscape) {
        if (this.errorsMap == null) {
            this.errorsMap = new HashMap<>();
        }
        Errors errors = this.errorsMap.get(name);
        boolean put = false;
        if (errors == null) {
            errors = (Errors) getModelObject(BindingResult.MODEL_KEY_PREFIX + name);
            if (errors instanceof BindException) {
                errors = ((BindException) errors).getBindingResult();
            }
            if (errors == null) {
                return null;
            }
            put = true;
        }
        if (htmlEscape && !(errors instanceof EscapedErrors)) {
            errors = new EscapedErrors(errors);
            put = true;
        } else if (!htmlEscape && errors instanceof EscapedErrors) {
            errors = ((EscapedErrors) errors).getSource();
            put = true;
        }
        if (put) {
            this.errorsMap.put(name, errors);
        }
        return errors;
    }

    /**
     * 
     * @param modelName
     *            the name of the model object
     * @return the model object
     */
    @Nullable
    protected Object getModelObject(String modelName) {
        if (this.model != null) {
            return this.model.get(modelName);
        } else {
            return this.request.getAttribute(modelName);
        }
    }

}

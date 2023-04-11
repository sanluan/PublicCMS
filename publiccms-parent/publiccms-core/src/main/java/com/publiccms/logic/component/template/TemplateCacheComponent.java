package com.publiccms.logic.component.template;

import java.io.File;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.ui.ModelMap;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.view.UrlBasedViewResolver;

import com.publiccms.common.api.Cache;
import com.publiccms.common.api.Config;
import com.publiccms.common.api.ParameterTypeHandler;
import com.publiccms.common.base.AbstractFreemarkerView;
import com.publiccms.common.constants.CommonConstants;
import com.publiccms.common.constants.Constants;
import com.publiccms.common.servlet.WebDispatcherServlet;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.common.tools.ControllerUtils;
import com.publiccms.common.tools.FreeMarkerUtils;
import com.publiccms.common.tools.RequestUtils;
import com.publiccms.entities.sys.SysDomain;
import com.publiccms.entities.sys.SysSite;
import com.publiccms.logic.component.config.ConfigDataComponent;
import com.publiccms.logic.component.config.SiteConfigComponent;
import com.publiccms.logic.component.site.SiteComponent;
import com.publiccms.views.pojo.entities.CmsPageData;
import com.publiccms.views.pojo.entities.CmsPageMetadata;
import com.publiccms.views.pojo.entities.ParameterType;

/**
 * 
 * TemplateCacheComponent 动态模板缓存组件
 *
 */
@Component
public class TemplateCacheComponent implements Cache {

    protected final Log log = LogFactory.getLog(getClass());
    /**
     * 
     */
    public static final String CACHE_VAR = "useCache";
    /**
     * 
     */
    public static final String CONTENT_CACHE = "noCache";
    /**
     * 
     */
    public static final String CACHE_FILE_DIRECTORY = "/cache";
    private final Lock lock = new ReentrantLock();
    @Resource
    private SiteComponent siteComponent;
    @Resource
    private TemplateComponent templateComponent;
    @Resource
    private MetadataComponent metadataComponent;
    @Resource
    private ConfigDataComponent configDataComponent;
    private Map<String, ParameterTypeHandler<?, ?>> parameterTypeHandlerMap;

    public String getViewName(LocaleResolver localeResolver, SysSite site, Long id, Integer pageIndex, String requestPath,
            String body, HttpServletRequest request, HttpServletResponse response, ModelMap model) {
        requestPath = siteComponent.getPath(site, requestPath);
        SysDomain domain = siteComponent.getDomain(request.getServerName());
        String fullRequestPath = siteComponent.getViewName(site.getId(), domain, requestPath);
        String templatePath = CommonUtils.joinString(siteComponent.getTemplateFilePath(), fullRequestPath);
        CmsPageMetadata metadata = metadataComponent.getTemplateMetadata(templatePath);
        if (metadata.isUseDynamic()) {
            if (metadata.isNeedLogin() && null == ControllerUtils.getUserFromSession(request.getSession())) {
                Map<String, String> config = configDataComponent.getConfigData(site.getId(), Config.CONFIG_CODE_SITE);
                String loginPath = config.get(SiteConfigComponent.CONFIG_LOGIN_PATH);
                StringBuilder sb = new StringBuilder(UrlBasedViewResolver.REDIRECT_URL_PREFIX);
                if (CommonUtils.notEmpty(loginPath)) {
                    if (null != id) {
                        int index = requestPath.lastIndexOf(Constants.DOT);
                        if (0 < index) {
                            requestPath = requestPath.substring(0, index);
                        }
                        requestPath = CommonUtils.joinString(requestPath, Constants.SEPARATOR, id);
                        if (null != pageIndex) {
                            requestPath = CommonUtils.joinString(requestPath, Constants.UNDERLINE, pageIndex);
                        }
                    }
                    return sb.append(loginPath).append("?returnUrl=")
                            .append(RequestUtils.getEncodePath(requestPath, request.getQueryString())).toString();
                } else {
                    return sb.append(site.getDynamicPath()).toString();
                }
            }
            String[] acceptParameters = StringUtils.split(metadata.getAcceptParameters(), Constants.COMMA);
            if (CommonUtils.notEmpty(acceptParameters) && !billingRequestParametersToModel(request, acceptParameters, id,
                    pageIndex, metadata.getParameterTypeMap(), site, model)) {
                try {
                    response.sendError(HttpServletResponse.SC_NOT_FOUND);
                } catch (IOException e) {
                }
                return requestPath;
            }
            CmsPageData data = metadataComponent.getTemplateData(templatePath);
            model.addAttribute("metadata", metadata.getAsMap(data));
            if (metadata.isNeedBody()) {
                model.addAttribute("body", body);
            }
            if (CommonUtils.notEmpty(metadata.getContentType())) {
                response.setContentType(metadata.getContentType());
            }
            if (CommonUtils.notEmpty(metadata.getCacheTime()) && 0 < metadata.getCacheTime()) {
                int cacheMillisTime = metadata.getCacheTime() * 1000;
                String cacheControl = request.getHeader("Cache-Control");
                String pragma = request.getHeader("Pragma");
                if (CommonUtils.notEmpty(cacheControl) && "no-cache".equalsIgnoreCase(cacheControl)
                        || CommonUtils.notEmpty(pragma) && "no-cache".equalsIgnoreCase(pragma)) {
                    cacheMillisTime = 0;
                }
                return getCachedPath(requestPath, fullRequestPath, localeResolver.resolveLocale(request), cacheMillisTime,
                        acceptParameters, request, model);
            }
        } else {
            try {
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
            } catch (IOException e) {
            }
        }
        return requestPath;
    }

    private boolean billingRequestParametersToModel(HttpServletRequest request, String[] acceptParameters, Long id,
            Integer pageIndex, Map<String, ParameterType> parameterTypeMap, SysSite site, ModelMap model) {
        for (String parameterName : acceptParameters) {
            String[] values = request.getParameterValues(parameterName);
            if ("id".equals(parameterName) && null != id) {
                values = new String[] { id.toString() };
            } else if (CommonConstants.DEFAULT_PAGEINDEX.equals(parameterName) && null != pageIndex) {
                values = new String[] { pageIndex.toString() };
            }
            ParameterType parameterType = null;
            if (null != parameterTypeMap) {
                parameterType = parameterTypeMap.get(parameterName);
            }
            if (null == parameterType) {
                if (CommonUtils.notEmpty(values)) {
                    if (1 < values.length) {
                        RequestUtils.removeCRLF(values);
                        model.addAttribute(parameterName, values);
                    } else {
                        model.addAttribute(parameterName, RequestUtils.removeCRLF(values[0]));
                    }
                }
            } else if (!parameterType.isRequired() || CommonUtils.notEmpty(values)) {
                try {
                    if (!billingValue(CommonUtils.notEmpty(parameterType.getAlias()) ? parameterType.getAlias() : parameterName,
                            values, parameterType, site, model)) {
                        return false;
                    }
                } catch (IllegalArgumentException e) {
                    return false;
                }
            } else {
                return false;
            }
        }
        return true;
    }

    private <E, P> boolean billingValue(String parameterName, String[] values, ParameterType parameterType, SysSite site,
            ModelMap model) {
        @SuppressWarnings("unchecked")
        ParameterTypeHandler<E, P> parameterTypeHandler = (ParameterTypeHandler<E, P>) parameterTypeHandlerMap
                .get(parameterType.getType());
        if (null == parameterTypeHandler) {
            if (parameterType.isArray()) {
                RequestUtils.removeCRLF(values);
                model.addAttribute(parameterName, values);
            } else if (CommonUtils.notEmpty(values)) {
                model.addAttribute(parameterName, RequestUtils.removeCRLF(values[0]));
            }
        } else {
            if (parameterType.isArray() && CommonUtils.notEmpty(values)) {
                P[] ids = parameterTypeHandler.dealParameterValues(values);
                if (CommonUtils.empty(ids) && parameterType.isRequired()) {
                    return false;
                } else {
                    List<E> list = parameterTypeHandler.getParameterValueList(site, ids);
                    if ((null == list || list.isEmpty()) && parameterType.isRequired()) {
                        return false;
                    } else {
                        model.addAttribute(parameterName, list);
                    }
                }
            } else if (CommonUtils.notEmpty(values) && CommonUtils.notEmpty(values[0])) {
                P id = parameterTypeHandler.dealParameterValue(values[0]);
                if (null == id && parameterType.isRequired()) {
                    return false;
                } else {
                    E entity = parameterTypeHandler.getParameterValue(site, id);
                    if (null == entity && parameterType.isRequired()) {
                        return false;
                    } else {
                        model.addAttribute(parameterName, entity);
                    }
                }
            } else if (parameterType.isRequired()) {
                return false;
            }
        }
        return true;
    }

    /**
     * 返回缓存模板路径或者模板原路径
     * 
     * @param requestPath
     * @param fullTemplatePath
     * @param locale
     * @param cacheMillisTime
     * @param acceptParameters
     * @param request
     * @param modelMap
     * @return cached path
     */
    public String getCachedPath(String requestPath, String fullTemplatePath, Locale locale, int cacheMillisTime,
            String[] acceptParameters, HttpServletRequest request, ModelMap modelMap) {
        ModelMap model = (ModelMap) modelMap.clone();
        AbstractFreemarkerView.exposeAttribute(model, request);
        model.addAttribute(CACHE_VAR, true);
        return createCache(requestPath, fullTemplatePath,
                CommonUtils.joinString(fullTemplatePath, getRequestParametersString(request, acceptParameters)), locale,
                cacheMillisTime, model);
    }

    private static String getRequestParametersString(HttpServletRequest request, String[] acceptParameters) {
        StringBuilder sb = new StringBuilder();
        sb.append("/default.html");
        if (null != acceptParameters) {
            for (String parameterName : acceptParameters) {
                String[] values = request.getParameterValues(parameterName);
                if (CommonUtils.notEmpty(values)) {
                    for (int i = 0; i < values.length; i++) {
                        sb.append(Constants.UNDERLINE);
                        sb.append(parameterName);
                        sb.append("=");
                        sb.append(values[i]);
                    }
                }
            }
        }
        return sb.toString();
    }

    /**
     * 删除缓存文件
     * 
     * @param path
     */
    public void deleteCachedFile(String path) {
        FileUtils.deleteQuietly(new File(getCachedFilePath(path)));
    }

    @Override
    public void clear() {
        deleteCachedFile(getCachedFilePath(Constants.BLANK));
    }

    private String createCache(String requestPath, String fullTemplatePath, String cachePath, Locale locale, int cacheMillisTime,
            ModelMap model) {
        String cachedFilePath = getCachedFilePath(cachePath);
        String cachedtemplatePath = CommonUtils.joinString(CACHE_FILE_DIRECTORY, cachePath);
        String cachedPath = CommonUtils.joinString(WebDispatcherServlet.GLOBLE_URL_PREFIX, cachedtemplatePath);
        try {
            lock.lock();
            if (checkCacheFile(cachedFilePath, cacheMillisTime)) {
                return cachedPath;
            }
            FreeMarkerUtils.generateFileByFile(fullTemplatePath, cachedFilePath, templateComponent.getWebConfiguration(), model);
            templateComponent.getWebConfiguration().removeTemplateFromCache(cachedtemplatePath, locale);
            return cachedPath;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return requestPath;
        } finally {
            lock.unlock();
        }
    }

    private static boolean checkCacheFile(String cacheFilePath, int millisTime) {
        if (0 < millisTime) {
            File dest = new File(cacheFilePath);
            if (dest.exists()) {
                if (dest.lastModified() > (System.currentTimeMillis() - millisTime)) {
                    return true;
                } else {
                    dest.setLastModified(System.currentTimeMillis());
                }
            }
        }
        return false;
    }

    private String getCachedFilePath(String path) {
        return CommonUtils.joinString(siteComponent.getTemplateFilePath(), CACHE_FILE_DIRECTORY, path);
    }

    /**
     * @return the parameterTypeHandlerMap
     */
    public Map<String, ParameterTypeHandler<?, ?>> getParameterTypeHandlerMap() {
        return parameterTypeHandlerMap;
    }

    /**
     * @param parameterTypeHandlerList
     * @param parameterTypeHandlerMap
     *            the parameterTypeHandlerMap to set
     */
    @Autowired
    public <E, P> void setParameterTypeHandlerMap(List<ParameterTypeHandler<E, P>> parameterTypeHandlerList) {
        this.parameterTypeHandlerMap = new LinkedHashMap<>();
        for (ParameterTypeHandler<E, P> parameterTypeHandler : parameterTypeHandlerList) {
            this.parameterTypeHandlerMap.put(parameterTypeHandler.getType(), parameterTypeHandler);
        }
    }
}
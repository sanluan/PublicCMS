package com.publiccms.logic.component.template;

import java.io.File;
import java.util.Locale;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.ui.ModelMap;

import com.publiccms.common.api.Cache;
import com.publiccms.common.base.AbstractFreemarkerView;
import com.publiccms.common.constants.CommonConstants;
import com.publiccms.common.servlet.WebDispatcherServlet;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.common.tools.FreeMarkerUtils;
import com.publiccms.logic.component.site.SiteComponent;

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
    @Autowired
    private SiteComponent siteComponent;
    @Autowired
    private TemplateComponent templateComponent;

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
                fullTemplatePath + getRequestParametersString(request, acceptParameters), locale, cacheMillisTime, model);
    }

    private static String getRequestParametersString(HttpServletRequest request, String[] acceptParameters) {
        StringBuilder sb = new StringBuilder();
        sb.append("/default.html");
        if (null != acceptParameters) {
            for (String parameterName : acceptParameters) {
                String[] values = request.getParameterValues(parameterName);
                if (CommonUtils.notEmpty(values)) {
                    for (int i = 0; i < values.length; i++) {
                        sb.append(CommonConstants.UNDERLINE);
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
        deleteCachedFile(getCachedFilePath(CommonConstants.BLANK));
    }

    private String createCache(String requestPath, String fullTemplatePath, String cachePath, Locale locale, int cacheMillisTime,
            ModelMap model) {
        String cachedFilePath = getCachedFilePath(cachePath);
        String cachedtemplatePath = CACHE_FILE_DIRECTORY + cachePath;
        String cachedPath = WebDispatcherServlet.GLOBLE_URL_PREFIX + cachedtemplatePath;
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
        return siteComponent.getWebTemplateFilePath() + CACHE_FILE_DIRECTORY + path;
    }
}
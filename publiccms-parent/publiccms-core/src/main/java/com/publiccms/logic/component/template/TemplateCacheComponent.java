package com.publiccms.logic.component.template;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.ui.ModelMap;

import com.publiccms.common.api.Cache;
import com.publiccms.common.base.AbstractFreemarkerView;
import com.publiccms.common.base.Base;
import com.publiccms.common.servlet.WebDispatcherServlet;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.common.tools.FreeMarkerUtils;
import com.publiccms.logic.component.site.SiteComponent;

import freemarker.core.DirectiveCallPlace;
import freemarker.core.Environment;
import freemarker.template.TemplateBooleanModel;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateDirectiveModel;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;

/**
 * 
 * TemplateCacheComponent 动态模板缓存组件
 *
 */
@Component
public class TemplateCacheComponent implements Cache, Base {

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
    @Autowired
    private SiteComponent siteComponent;
    @Autowired
    private TemplateComponent templateComponent;

    /**
     * 返回缓存模板路径或者模板原路径
     * 
     * @param requestPath
     * @param fullTemplatePath
     * @param cacheMillisTime
     * @param acceptParamters
     * @param request
     * @param modelMap
     * @return cached path
     */
    public String getCachedPath(String requestPath, String fullTemplatePath, int cacheMillisTime, String[] acceptParamters,
            HttpServletRequest request, ModelMap modelMap) {
        ModelMap model = (ModelMap) modelMap.clone();
        AbstractFreemarkerView.exposeAttribute(model, request.getScheme(), request.getServerName(), request.getServerPort(),
                request.getContextPath());
        model.addAttribute(CACHE_VAR, true);
        return createCache(requestPath, fullTemplatePath, fullTemplatePath + getRequestParamtersString(request, acceptParamters),
                cacheMillisTime, model);
    }

    private String getRequestParamtersString(HttpServletRequest request, String[] acceptParamters) {
        StringBuilder sb = new StringBuilder();
        sb.append("/default.html");
        if (null != acceptParamters) {
            for (String paramterName : acceptParamters) {
                String[] values = request.getParameterValues(paramterName);
                if (CommonUtils.notEmpty(values)) {
                    for (int i = 0; i < values.length; i++) {
                        sb.append("_");
                        sb.append(paramterName);
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
        deleteCachedFile(getCachedFilePath(""));
    }

    private String createCache(String requestPath, String fullTemplatePath, String cachePath, int cacheMillisTime,
            ModelMap model) {
        String cachedFilePath = getCachedFilePath(cachePath);
        String cachedtemplatePath = CACHE_FILE_DIRECTORY + cachePath;
        String cachedPath = WebDispatcherServlet.GLOBLE_URL_PREFIX + cachedtemplatePath;
        if (checkCacheFile(cachedFilePath, cacheMillisTime)) {
            return cachedPath;
        }
        try {
            FreeMarkerUtils.generateFileByFile(fullTemplatePath, cachedFilePath, templateComponent.getWebConfiguration(), model);
            templateComponent.getWebConfiguration().removeTemplateFromCache(cachedtemplatePath);
            return cachedPath;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return requestPath;
        }
    }

    private boolean checkCacheFile(String cacheFilePath, int millisTime) {
        File dest = new File(cacheFilePath);
        if (dest.exists()) {
            if (dest.lastModified() > (System.currentTimeMillis() - millisTime)) {
                return true;
            } else {
                dest.setLastModified(System.currentTimeMillis());
            }
        }
        return false;
    }

    private String getCachedFilePath(String path) {
        return siteComponent.getWebTemplateFilePath() + CACHE_FILE_DIRECTORY + path;
    }
}

class NoCacheDirective implements TemplateDirectiveModel {
    protected final Log log = LogFactory.getLog(getClass());

    @Override
    public void execute(Environment environment, @SuppressWarnings("rawtypes") Map parameters, TemplateModel[] templateModel,
            TemplateDirectiveBody templateDirectiveBody) throws TemplateException, IOException {
        if (null != templateDirectiveBody) {
            TemplateModel model = environment.getVariable(TemplateCacheComponent.CACHE_VAR);
            if (null != model && model instanceof TemplateBooleanModel) {
                try {
                    DirectiveCallPlace directiveCallPlace = environment.getCurrentDirectiveCallPlace();
                    if (null != directiveCallPlace) {
                        environment.getOut().append(directiveCallPlace.toString());
                    }
                } catch (Exception e) {
                    environment.getOut().append(e.getMessage());
                }
            } else {
                templateDirectiveBody.render(environment.getOut());
            }
        }
    }
}
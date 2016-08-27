package com.publiccms.logic.component;

import static com.publiccms.logic.component.SiteComponent.expose;
import static com.publiccms.logic.component.TemplateCacheComponent.CACHE_VAR;
import static com.sanluan.common.tools.FreeMarkerUtils.makeFileByFile;
import static org.apache.commons.io.FileUtils.deleteQuietly;
import static org.apache.commons.lang3.ArrayUtils.isNotEmpty;
import static org.apache.commons.lang3.StringUtils.split;
import static org.apache.commons.logging.LogFactory.getLog;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.ui.ModelMap;

import com.publiccms.common.spi.Cacheable;
import com.publiccms.entities.sys.SysSite;
import com.sanluan.common.base.Base;

import freemarker.core.DirectiveCallPlace;
import freemarker.core.Environment;
import freemarker.core.TemplateElement;
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
@SuppressWarnings("deprecation")
@Component
public class TemplateCacheComponent extends Base implements Cacheable {
    public static final String CACHE_VAR = "useCache";
    public static final String CONTENT_CACHE = "noCache";
    @Autowired
    private SiteComponent siteComponent;
    @Autowired
    private TemplateComponent templateComponent;
    public static final String CACHE_FILE_DIRECTORY = "/cache";
    public static final String CACHE_URL_PREFIX = "cache:";

    /**
     * 返回缓存模板路径或者模板原路径
     * 
     * @param path
     * @param request
     * @param response
     * @param model
     * @return
     */
    public String getCachedPath(String requestPath, String fullTemplatePath, int cacheMillisTime, String acceptParamters,
            SysSite site, HttpServletRequest request, ModelMap modelMap) {
        ModelMap model = (ModelMap) modelMap.clone();
        expose(model, site, request.getScheme(), request.getServerName(), request.getServerPort(), request.getContextPath());
        model.put(CACHE_VAR, true);
        return createCache(requestPath, fullTemplatePath, fullTemplatePath + getRequestParamtersString(request, acceptParamters),
                cacheMillisTime, model);
    }

    private String getRequestParamtersString(HttpServletRequest request, String acceptParamters) {
        StringBuilder sb = new StringBuilder();
        sb.append("/default.html");
        for (String paramterName : split(acceptParamters, COMMA_DELIMITED)) {
            String[] values = request.getParameterValues(paramterName);
            if (isNotEmpty(values)) {
                for (int i = 0; i < values.length; i++) {
                    sb.append("_");
                    sb.append(paramterName);
                    sb.append("=");
                    sb.append(values[i]);
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
        deleteQuietly(new File(getCachedFilePath(path)));
    }

    @Override
    public void clear() {
        deleteCachedFile(getCachedFilePath(""));
    }

    private String createCache(String requestPath, String fullTemplatePath, String cachePath, int cacheMillisTime,
            ModelMap model) {
        String cachedFilePath = getCachedFilePath(cachePath);
        String cachedtemplatePath = CACHE_FILE_DIRECTORY + cachePath;
        String cachedPath = CACHE_URL_PREFIX + cachedtemplatePath;
        if (checkCacheFile(cachedFilePath, cacheMillisTime)) {
            return cachedPath;
        }
        try {
            makeFileByFile(fullTemplatePath, cachedFilePath, templateComponent.getWebConfiguration(), model);
            templateComponent.getWebConfiguration().removeTemplateFromCache(cachedtemplatePath);
            return cachedPath;
        } catch (Exception e) {
            log.error(e.getMessage());
            return requestPath;
        }
    }

    private boolean checkCacheFile(String cacheFilePath, int millisTime) {
        File dest = new File(cacheFilePath);
        if (dest.exists() && dest.lastModified() > (System.currentTimeMillis() - millisTime)) {
            return true;
        }
        return false;
    }

    private String getCachedFilePath(String path) {
        return siteComponent.getWebTemplateFilePath() + CACHE_FILE_DIRECTORY + path;
    }
}

@SuppressWarnings("deprecation")
class NoCacheDirective extends Base implements TemplateDirectiveModel {
    private static final Class<Environment> clazz = Environment.class;
    private static Method method;
    static {
        try {
            method = clazz.getDeclaredMethod("getInstructionStackSnapshot");
            method.setAccessible(true);
        } catch (NoSuchMethodException | SecurityException e) {
            method = null;
        }
    }
    protected final Log log = getLog(getClass());

    /*
     * (non-Javadoc)
     * 
     * @see freemarker.template.TemplateDirectiveModel#execute(freemarker.core.
     * Environment, java.util.Map, freemarker.template.TemplateModel[],
     * freemarker.template.TemplateDirectiveBody)
     */
    @Override
    public void execute(Environment environment, @SuppressWarnings("rawtypes") Map parameters, TemplateModel[] templateModel,
            TemplateDirectiveBody templateDirectiveBody) throws TemplateException, IOException {
        if (notEmpty(templateDirectiveBody) && notEmpty(method)) {
            TemplateModel model = environment.getVariable(CACHE_VAR);
            if (notEmpty(model) && model instanceof TemplateBooleanModel) {
                try {
                    TemplateElement[] elements = (TemplateElement[]) method.invoke(environment);
                    if (notEmpty(elements)) {
                        int i = 1;
                        TemplateElement currentElement = elements[elements.length - i];
                        while (currentElement instanceof DirectiveCallPlace && i < elements.length) {
                            i++;
                            currentElement = elements[elements.length - i];
                        }
                        environment.getOut().append(currentElement.getSource());
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
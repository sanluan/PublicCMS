package com.publiccms.logic.component;

import static com.publiccms.common.constants.FreeMakerConstants.CACHE_VAR;
import static com.publiccms.common.constants.FreeMakerConstants.CONTEXT_BASE;
import static com.publiccms.common.constants.FreeMakerConstants.TEMPLATE_SUFFIX;
import static com.publiccms.common.view.InitializeFreeMarkerView.UNSAFE_PATTERN;
import static com.sanluan.common.constants.CommonConstants.NUMBER_PATTERN;
import static freemarker.ext.servlet.FreemarkerServlet.KEY_INCLUDE;
import static org.apache.commons.io.FileUtils.deleteQuietly;
import static org.apache.commons.lang3.time.DateUtils.addSeconds;

import java.io.File;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.ui.ModelMap;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import com.sanluan.common.tools.FreeMarkerUtils;

import freemarker.ext.servlet.IncludePage;

/**
 * 
 * CacheComponent 缓存组件
 *
 */
@Component
public class CacheComponent {
    private String cacheFileDirectory;
    private Map<Integer, String[]> cacheFilePaths = new HashMap<Integer, String[]>();
    private String basePath;

    public final static String TEMPLATE_BASE_PATH = "/WEB-INF";
    public final static String TEMPLATE_PREFIX = "/web/";
    
    private final static String TEMPLATE_LOADER_PATH = TEMPLATE_BASE_PATH + TEMPLATE_PREFIX;

    @Autowired
    private FreeMarkerConfigurer freeMarkerConfigurer;

    /**
     * 返回缓存模板路径或者模板原路径
     * 
     * @param path
     * @param request
     * @param response
     * @param model
     * @return
     */
    public String getFilePath(String path, HttpServletRequest request, HttpServletResponse response, ModelMap model) {
        String templatePath = path;
        int dirEndIndex = path.lastIndexOf("/");
        String lastPath = path.substring(dirEndIndex + 1, path.length());
        if (dirEndIndex > 0) {
            if (virifyNotNumber(lastPath)) {
                templatePath = path;
            } else {
                model.addAttribute("id", lastPath);
                templatePath = path.substring(0, dirEndIndex);
            }
        }
        model = (ModelMap) model.clone();

        Enumeration<String> parameters = request.getParameterNames();
        StringBuilder sb = new StringBuilder();
        while (parameters.hasMoreElements()) {
            String paramterName = parameters.nextElement();
            if (!paramterName.startsWith("spm") && !UNSAFE_PATTERN.matcher(paramterName).matches()) {
                if (0 == sb.length()) {
                    sb.append("/");
                } else {
                    sb.append("&");
                }
                sb.append(paramterName);
                sb.append("=");
                sb.append(request.getParameter(paramterName));
                model.put(paramterName, request.getParameter(paramterName));
            }
        }
        path += sb.toString();
        model.put(KEY_INCLUDE, new IncludePage(request, response));
        model.put(CACHE_VAR, true);
        model.put(CONTEXT_BASE, request.getContextPath());
        return createCache(templatePath, path, model, response);
    }

    /**
     * 删除缓存文件
     * 
     * @param path
     */
    public void deleteCacheFile(String path) {
        String htmlFilePath = getHtmlFilePath(path);
        File cacheFile = new File(htmlFilePath);
        if (cacheFile.exists()) {
            cacheFile.delete();
        }
        File cacheDir = new File(htmlFilePath.substring(0, htmlFilePath.lastIndexOf(TEMPLATE_SUFFIX)));
        if (cacheDir.isDirectory()) {
            deleteQuietly(cacheDir);
        }
    }

    /**
     * 清除模板缓存
     */
    public void clearTemplateCache() {
        freeMarkerConfigurer.getConfiguration().clearTemplateCache();
        File cacheDir = new File(basePath + TEMPLATE_LOADER_PATH + cacheFileDirectory);
        deleteQuietly(cacheDir);
    }

    private String createCache(String templatePath, String path, ModelMap model, HttpServletResponse response) {
        int time = verify(templatePath);
        if (0 != time) {
            String htmlPath = getHtmlFilePath(path);
            if (check(htmlPath, time)) {
                return cacheFileDirectory + path;
            } else {
                response.setCharacterEncoding("UTF-8");
                try {
                    FreeMarkerUtils.makeFileByFile(getTemplateFilePath(templatePath), htmlPath,
                            freeMarkerConfigurer.getConfiguration(), model);
                    return cacheFileDirectory + path;
                } catch (Exception e) {
                    return templatePath;
                }
            }
        } else {
            return templatePath;
        }
    }

    private boolean check(String templatePath, int time) {
        File dest = new File(templatePath);
        if (dest.exists()) {
            if ((new Date(dest.lastModified())).after(addSeconds(new Date(), -time))) {
                return true;
            }
        }
        return false;
    }

    private boolean virifyNotNumber(String value) {
        Pattern p = Pattern.compile(NUMBER_PATTERN);
        Matcher m = p.matcher(value);
        if (!m.matches()) {
            return true;
        }
        return false;
    }

    private int verify(String path) {
        for (Integer time : cacheFilePaths.keySet()) {
            for (String cachePath : cacheFilePaths.get(time)) {
                if (path.startsWith(cachePath)) {
                    return time;
                }
            }
        }
        return 0;
    }

    private String getHtmlFilePath(String path) {
        return basePath + TEMPLATE_LOADER_PATH + cacheFileDirectory + path + TEMPLATE_SUFFIX;
    }

    private String getTemplateFilePath(String path) {
        return TEMPLATE_PREFIX + path + TEMPLATE_SUFFIX;
    }

    /**
     * @param cacheFileDirectory
     *            the cacheFileDirectory to set
     */
    public void setCacheFileDirectory(String cacheFileDirectory) {
        this.cacheFileDirectory = cacheFileDirectory;
    }

    /**
     * @param cachePaths
     *            the cachePaths to set
     */
    public void setCachePaths(Map<Integer, String[]> cachePaths) {
        cacheFilePaths = cachePaths;
    }

    public void setBasePath(String basePath) {
        this.basePath = basePath;
    }
}

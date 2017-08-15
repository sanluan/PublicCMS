package org.publiccms.logic.component.site;

import static com.publiccms.common.tools.CommonUtils.empty;
import static com.publiccms.common.tools.CommonUtils.notEmpty;
import static org.apache.commons.lang3.StringUtils.split;

import java.util.HashSet;
import java.util.Set;

import org.publiccms.common.api.Cache;
import org.publiccms.entities.sys.SysDomain;
import org.publiccms.entities.sys.SysSite;
import org.publiccms.logic.service.sys.SysDomainService;
import org.publiccms.logic.service.sys.SysSiteService;
import org.springframework.beans.factory.annotation.Autowired;

import com.publiccms.common.base.Base;
import com.publiccms.common.cache.CacheEntity;
import com.publiccms.common.cache.CacheEntityFactory;

/**
 *
 * SiteComponent
 * 
 */
public class SiteComponent implements Cache, Base {

    /**
     * 
     */
    public static final String TEMPLATE_PATH = "template";
    /**
     * 
     */
    public static final String TASK_FILE_PATH = "task";
    /**
     * 
     */
    public static final String STATIC_FILE_PATH_WEB = "web";

    /**
     * 
     */
    public static final String SITE_PATH_PREFIX = "/site_";
    /**
     * 
     */
    public static final String MODEL_FILE = "model.data";
    /**
     * 
     */
    public static final String CONFIG_FILE = "config.data";

    private CacheEntity<String, SysSite> siteCache;
    private CacheEntity<String, SysDomain> domainCache;
    private String rootPath;
    private String webFilePath;
    private String taskTemplateFilePath;
    private String webTemplateFilePath;

    private int defaultSiteId;
    private Set<Integer> idSet = new HashSet<Integer>();
    @Autowired
    private SysDomainService sysDomainService;
    @Autowired
    private SysSiteService sysSiteService;

    /**
     * @param site
     * @param path
     * @return
     */
    public static String getFullFileName(SysSite site, String path) {
        if (path.startsWith(SEPARATOR) || path.startsWith("\\")) {
            return SITE_PATH_PREFIX + site.getId() + path;
        }
        return SITE_PATH_PREFIX + site.getId() + SEPARATOR + path;
    }

    /**
     * @param serverName
     * @return
     */
    public String getViewNamePreffix(String serverName) {
        SysDomain sysDomain = getDomain(serverName);
        SysSite site = getSite(serverName);
        return getViewNamePreffix(site, sysDomain);
    }

    /**
     * @param site
     * @param sysDomain
     * @return
     */
    public String getViewNamePreffix(SysSite site, SysDomain sysDomain) {
        return getFullFileName(site, empty(sysDomain.getPath()) ? BLANK : sysDomain.getPath() + SEPARATOR);
    }

    /**
     * @param serverName
     * @return
     */
    public SysDomain getDomain(String serverName) {
        SysDomain sysDomain = domainCache.get(serverName);
        if (null == sysDomain) {
            sysDomain = sysDomainService.getEntity(serverName);
            if (null == sysDomain) {
                int index;
                if (null != serverName && 0 < (index = serverName.indexOf(DOT)) && index != serverName.lastIndexOf(DOT)) {
                    sysDomain = getDomain(serverName.substring(index + 1));
                    if (null != sysDomain.getName()) {
                        if (!sysDomain.isWild()) {
                            sysDomain = new SysDomain();
                            sysDomain.setSiteId(defaultSiteId);
                        }
                    }
                } else {
                    sysDomain = new SysDomain();
                    sysDomain.setSiteId(defaultSiteId);
                }
            } else {
                domainCache.put(serverName, sysDomain);
            }
        }
        return sysDomain;
    }

    /**
     * @param serverName
     * @return
     */
    public SysSite getSite(String serverName) {
        SysSite site = siteCache.get(serverName);
        if (null == site) {
            site = sysSiteService.getEntity(getDomain(serverName).getSiteId());
            siteCache.put(serverName, site);
        }
        return site;
    }

    /**
     * @param siteId
     * @return
     */
    public boolean isMaster(int siteId) {
        return null != idSet && idSet.contains(siteId);
    }

    /**
     * @param site
     * @param filePath
     * @return
     */
    public String getWebFilePath(SysSite site, String filePath) {
        return webFilePath + getFullFileName(site, filePath);
    }

    /**
     * @param site
     * @param templatePath
     * @return
     */
    public String getTaskTemplateFilePath(SysSite site, String templatePath) {
        return getTaskTemplateFilePath() + getFullFileName(site, templatePath);
    }

    /**
     * @param site
     * @param templatePath
     * @return
     */
    public String getWebTemplateFilePath(SysSite site, String templatePath) {
        return getWebTemplateFilePath() + getFullFileName(site, templatePath);
    }

    /**
     * @param site
     * @return
     */
    public String getModelFilePath(SysSite site) {
        return getWebTemplateFilePath() + getFullFileName(site, MODEL_FILE);
    }

    /**
     * @param site
     * @return
     */
    public String getConfigFilePath(SysSite site) {
        return getWebTemplateFilePath() + getFullFileName(site, CONFIG_FILE);
    }

    /**
     * @param defaultSiteId
     */
    public void setDefaultSiteId(int defaultSiteId) {
        this.defaultSiteId = defaultSiteId;
    }

    /**
     * @param siteMasters
     */
    public void setSiteMasters(String siteMasters) {
        String[] masters = split(siteMasters, COMMA_DELIMITED);
        for (String master : masters) {
            Integer id;
            try {
                id = Integer.parseInt(master);
            } catch (NumberFormatException e) {
                id = null;
            }
            if (notEmpty(id)) {
                idSet.add(id);
            }
        }
    }

    /**
     * @param rootPath
     */
    public void setRootPath(String rootPath) {
        if (notEmpty(rootPath)) {
            if (!(rootPath.endsWith(SEPARATOR) || rootPath.endsWith("\\"))) {
                rootPath += SEPARATOR;
            }
        }
        this.rootPath = rootPath;
        this.webFilePath = rootPath + STATIC_FILE_PATH_WEB;
        this.taskTemplateFilePath = rootPath + TASK_FILE_PATH;
        this.webTemplateFilePath = rootPath + TEMPLATE_PATH;
    }

    @Override
    public void clear() {
        siteCache.clear();
        domainCache.clear();
    }

    /**
     * @param cacheEntityFactory
     * @throws IllegalAccessException
     * @throws InstantiationException
     * @throws ClassNotFoundException
     */
    @Autowired
    public void initCache(CacheEntityFactory cacheEntityFactory)
            throws ClassNotFoundException, InstantiationException, IllegalAccessException {
        domainCache = cacheEntityFactory.createCacheEntity("domain");
        siteCache = cacheEntityFactory.createCacheEntity("site");
    }

    /**
     * @return
     */
    public String getRootPath() {
        return rootPath;
    }

    /**
     * @param taskTemplateFilePath
     */
    public void setTaskTemplateFilePath(String taskTemplateFilePath) {
        this.taskTemplateFilePath = taskTemplateFilePath;
    }

    /**
     * @param webTemplateFilePath
     */
    public void setWebTemplateFilePath(String webTemplateFilePath) {
        this.webTemplateFilePath = webTemplateFilePath;
    }

    /**
     * @return
     */
    public String getTaskTemplateFilePath() {
        return taskTemplateFilePath;
    }

    /**
     * @return
     */
    public String getWebTemplateFilePath() {
        return webTemplateFilePath;
    }
}
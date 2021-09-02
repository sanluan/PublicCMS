package com.publiccms.logic.component.site;

import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.publiccms.common.api.Cache;
import com.publiccms.common.cache.CacheEntity;
import com.publiccms.common.cache.CacheEntityFactory;
import com.publiccms.common.constants.CommonConstants;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.common.tools.DateFormatUtils;
import com.publiccms.entities.sys.SysDomain;
import com.publiccms.entities.sys.SysSite;
import com.publiccms.logic.service.sys.SysDomainService;
import com.publiccms.logic.service.sys.SysSiteService;

/**
 *
 * SiteComponent
 * 
 */
public class SiteComponent implements Cache {

    /**
     * 
     */
    public static final String TEMPLATE_PATH = "template";
    /**
     * 
     */
    public static final String BACKUP_PATH = "backup";
    /**
     * 
     */
    public static final String HISTORY_PATH = "history";
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

    private static final String FILE_NAME_FORMAT_STRING = "yyyy-MM-dd_HH-mm-ssSSSS";

    private CacheEntity<String, SysSite> siteCache;
    private CacheEntity<String, SysDomain> domainCache;
    private String rootPath;
    private String webFilePath;
    private String taskTemplateFilePath;
    private String webTemplateFilePath;

    private String webBackupFilePath;
    private String webTemplateBackupFilePath;
    private String taskTemplateBackupFilePath;

    private String webHistoryFilePath;
    private String webTemplateHistoryFilePath;
    private String taskTemplateHistoryFilePath;

    private short defaultSiteId;
    private boolean dictEnable = false;

    private Set<Short> masterSiteIdSet = new HashSet<>();
    @Autowired
    private SysDomainService sysDomainService;
    @Autowired
    private SysSiteService sysSiteService;

    /**
     * @param dictEnable
     *            the dictEnable to set
     */
    public void setDictEnable(boolean dictEnable) {
        this.dictEnable = dictEnable;
    }

    /**
     * @return the dictEnable
     */
    public boolean isDictEnable() {
        return dictEnable;
    }

    /**
     * @param site
     * @param path
     * @return full file name
     */
    public static String getFullTemplatePath(SysSite site, String path) {
        return getFullFileName(site.getId(), path);
    }

    /**
     * @param site
     * @param path
     * @return full file name
     */
    private static String getFullFileName(short siteId, String path) {
        if (path.contains("..")) {
            path = path.replace("..", CommonConstants.BLANK);
        }
        if (path.startsWith(CommonConstants.SEPARATOR) || path.startsWith("\\")) {
            return SITE_PATH_PREFIX + siteId + path;
        }
        return SITE_PATH_PREFIX + siteId + CommonConstants.SEPARATOR + path;
    }

    /**
     * @param serverName
     * @param path
     * @return view name
     */
    public String getViewName(String serverName, String path) {
        SysDomain sysDomain = getDomain(serverName);
        SysSite site = getSite(serverName, path);
        path = getPath(site, path);
        return getViewName(site, sysDomain, path);
    }

    /**
     * @param site
     * @param sysDomain
     * @param path
     * @return view name
     */
    public String getViewName(SysSite site, SysDomain sysDomain, String path) {
        path = getPath(site, path);
        if (CommonUtils.notEmpty(sysDomain.getPath())) {
            if (path.startsWith(CommonConstants.SEPARATOR) || sysDomain.getPath().endsWith(CommonConstants.SEPARATOR)) {
                if (path.startsWith(CommonConstants.SEPARATOR) && sysDomain.getPath().endsWith(CommonConstants.SEPARATOR)) {
                    path = sysDomain.getPath() + path.substring(1);
                } else {
                    path = sysDomain.getPath() + path;
                }
            } else {
                path = sysDomain.getPath() + CommonConstants.SEPARATOR + path;
            }
        }
        return getFullTemplatePath(site, path);
    }

    /**
     * @param serverName
     * @return domain
     */
    public SysDomain getDomain(String serverName) {
        SysDomain sysDomain = domainCache.get(serverName);
        if (null == sysDomain) {
            sysDomain = sysDomainService.getEntity(serverName);
            if (null == sysDomain) {
                int index;
                if (null != serverName && 0 < (index = serverName.indexOf(CommonConstants.DOT))
                        && index != serverName.lastIndexOf(CommonConstants.DOT)) {
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
            }
            domainCache.put(serverName, sysDomain);
        }
        return sysDomain;
    }

    /**
     * @param site
     * @param path
     * @return site
     */
    public String getPath(SysSite site, String path) {
        if (null != site.getParentId() && CommonUtils.notEmpty(site.getDirectory())) {
            int index = 0;
            if (path.startsWith(CommonConstants.SEPARATOR)) {
                index = path.indexOf(CommonConstants.SEPARATOR, 1);
            } else {
                index = path.indexOf(CommonConstants.SEPARATOR);
            }
            if (0 < index) {
                return path.substring(index, path.length());
            }
        }
        return path;
    }

    /**
     * @param id
     * @return site
     */
    public SysSite getSiteById(Short id) {
        if (CommonUtils.notEmpty(id)) {
            String key = id.toString();
            SysSite site = siteCache.get(key);
            if (null == site) {
                try {
                    site = sysSiteService.getEntity(id);
                    siteCache.put(key, site);
                    return site;
                } catch (NumberFormatException e) {
                }
            } else {
                return site;
            }
        }
        return null;
    }

    /**
     * @param id
     * @return site
     */
    public SysSite getSiteById(String id) {
        if (CommonUtils.notEmpty(id)) {
            SysSite site = siteCache.get(id);
            if (null == site) {
                try {
                    site = sysSiteService.getEntity(Short.parseShort(id));
                    siteCache.put(id, site);
                    return site;
                } catch (NumberFormatException e) {
                }
            } else {
                return site;
            }
        }
        return null;
    }

    /**
     * @param domain
     * @param serverName
     * @param path
     * @return site
     */
    public SysSite getSite(SysDomain domain, String serverName, String path) {
        String cacheKey;
        String directory = null;
        if (domain.isMultiple() && CommonUtils.notEmpty(path)) {
            int index = 0;
            if (path.startsWith(CommonConstants.SEPARATOR)) {
                index = path.indexOf(CommonConstants.SEPARATOR, 1);
                if (0 < index) {
                    directory = path.substring(1, index);
                }
            } else {
                index = path.indexOf(CommonConstants.SEPARATOR);
                if (0 < index) {
                    directory = path.substring(0, index);
                }
            }
            cacheKey = null == directory ? serverName : (serverName + CommonConstants.SEPARATOR + directory);
        } else {
            cacheKey = serverName;
        }
        SysSite site = siteCache.get(cacheKey);
        if (null == site) {
            if (null != directory) {
                site = sysSiteService.getEntity(domain.getSiteId(), directory);
            }
            if (null == site) {
                site = sysSiteService.getEntity(domain.getSiteId());
            }
            siteCache.put(cacheKey, site);
        }
        return site;
    }

    /**
     * @param serverName
     * @param path
     * @return site
     */
    public SysSite getSite(String serverName, String path) {
        SysDomain domain = getDomain(serverName);
        return getSite(domain, serverName, path);
    }

    /**
     * @param siteId
     * @return whether the master site
     */
    public boolean isMaster(short siteId) {
        return null != masterSiteIdSet && masterSiteIdSet.contains(siteId);
    }

    /**
     * @param site
     * @param filePath
     * @return web file path
     */
    public String getWebFilePath(SysSite site, String filePath) {
        return webFilePath + getFullFileName(site.getId(), filePath);
    }

    /**
     * @param site
     * @param filePath
     * @return web history file path
     */
    public String getWebHistoryFilePath(SysSite site, String filePath) {
        StringBuilder sb = new StringBuilder(webHistoryFilePath);
        sb.append(getFullFileName(site.getId(), filePath));
        sb.append(CommonConstants.SEPARATOR);
        sb.append(DateFormatUtils.getDateFormat(FILE_NAME_FORMAT_STRING).format(CommonUtils.getDate()));
        return sb.toString();
    }

    /**
     * @param site
     * @param filePath
     * @return web backup file path
     */
    public String getWebBackupFilePath(SysSite site, String filePath) {
        return webBackupFilePath + getFullFileName(site.getId(), filePath);
    }

    /**
     * @param site
     * @param templatePath
     * @return task template file path
     */
    public String getTaskTemplateFilePath(SysSite site, String templatePath) {
        return getTaskTemplateFilePath() + getFullTemplatePath(site, templatePath);
    }

    /**
     * @param site
     * @param templatePath
     * @return task template history file path
     */
    public String getTaskTemplateHistoryFilePath(SysSite site, String templatePath) {
        StringBuilder sb = new StringBuilder(taskTemplateHistoryFilePath);
        sb.append(getFullFileName(site.getId(), templatePath));
        sb.append(CommonConstants.SEPARATOR);
        sb.append(DateFormatUtils.getDateFormat(FILE_NAME_FORMAT_STRING).format(CommonUtils.getDate()));
        return sb.toString();
    }

    /**
     * @param site
     * @param templatePath
     * @return task template backup file path
     */
    public String getTaskTemplateBackupFilePath(SysSite site, String templatePath) {
        return taskTemplateBackupFilePath + getFullFileName(site.getId(), templatePath);
    }

    /**
     * @param site
     * @param templatePath
     * @return web template file path
     */
    public String getWebTemplateFilePath(SysSite site, String templatePath) {
        return getWebTemplateFilePath() + getFullTemplatePath(site, templatePath);
    }

    /**
     * @param site
     * @param templatePath
     * @return web template history file path
     */
    public String getWebTemplateHistoryFilePath(SysSite site, String templatePath) {
        StringBuilder sb = new StringBuilder(webTemplateHistoryFilePath);
        sb.append(getFullFileName(site.getId(), templatePath));
        sb.append(CommonConstants.SEPARATOR);
        sb.append(DateFormatUtils.getDateFormat(FILE_NAME_FORMAT_STRING).format(CommonUtils.getDate()));
        return sb.toString();
    }

    /**
     * @param site
     * @param templatePath
     * @return web template backup file path
     */
    public String getWebTemplateBackupFilePath(SysSite site, String templatePath) {
        return webTemplateBackupFilePath + getFullFileName(site.getId(), templatePath);
    }

    /**
     * @param site
     * @return model file path
     */
    public String getModelFilePath(SysSite site) {
        return getWebTemplateFilePath() + getFullTemplatePath(site, MODEL_FILE);
    }

    /**
     * @param site
     * @return config file path
     */
    public String getConfigFilePath(SysSite site) {
        return getWebTemplateFilePath() + getFullTemplatePath(site, CONFIG_FILE);
    }

    /**
     * @param defaultSiteId
     */
    public void setDefaultSiteId(short defaultSiteId) {
        this.defaultSiteId = defaultSiteId;
    }

    /**
     * @param masterSiteIds
     */
    public void setMasterSiteIds(String masterSiteIds) {
        String[] masters = StringUtils.split(masterSiteIds, CommonConstants.COMMA_DELIMITED);
        for (String master : masters) {
            try {
                Short id = Short.parseShort(master);
                masterSiteIdSet.add(id);
            } catch (NumberFormatException e) {
            }
        }
    }

    /**
     * @param rootPath
     */
    public void setRootPath(String rootPath) {
        if (CommonUtils.notEmpty(rootPath)) {
            if (!(rootPath.endsWith(CommonConstants.SEPARATOR) || rootPath.endsWith("\\"))) {
                rootPath += CommonConstants.SEPARATOR;
            }
        }
        this.rootPath = rootPath;
        this.webFilePath = rootPath + STATIC_FILE_PATH_WEB;
        this.taskTemplateFilePath = rootPath + TASK_FILE_PATH;
        this.webTemplateFilePath = rootPath + TEMPLATE_PATH;
        this.webTemplateBackupFilePath = rootPath + BACKUP_PATH + CommonConstants.SEPARATOR + TEMPLATE_PATH;
        this.taskTemplateBackupFilePath = rootPath + BACKUP_PATH + CommonConstants.SEPARATOR + TASK_FILE_PATH;
        this.webBackupFilePath = rootPath + BACKUP_PATH + CommonConstants.SEPARATOR + STATIC_FILE_PATH_WEB;
        this.webTemplateHistoryFilePath = rootPath + HISTORY_PATH + CommonConstants.SEPARATOR + TEMPLATE_PATH;
        this.taskTemplateHistoryFilePath = rootPath + HISTORY_PATH + CommonConstants.SEPARATOR + TASK_FILE_PATH;
        this.webHistoryFilePath = rootPath + HISTORY_PATH + CommonConstants.SEPARATOR + STATIC_FILE_PATH_WEB;
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
     * @return root path
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
     * @return task template file path
     */
    public String getTaskTemplateFilePath() {
        return taskTemplateFilePath;
    }

    /**
     * @return web template file path
     */
    public String getWebTemplateFilePath() {
        return webTemplateFilePath;
    }
}
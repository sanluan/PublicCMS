package com.publiccms.logic.component.site;

import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import jakarta.annotation.Resource;

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
    public static final String CATEGORY_TYPE_FILE = "categoryType.data";
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
    private String templateFilePath;

    private String webBackupFilePath;
    private String templateBackupFilePath;
    private String taskTemplateBackupFilePath;

    private String webHistoryFilePath;
    private String templateHistoryFilePath;
    private String taskTemplateHistoryFilePath;

    private short defaultSiteId;
    private boolean dictEnable = false;

    private Set<Short> masterSiteIdSet = new HashSet<>();
    @Resource
    private SysDomainService sysDomainService;
    @Resource
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
     * @param siteId
     * @param path
     * @return full file name
     */
    public static String getFullTemplatePath(short siteId, String path) {
        return getFullFileName(siteId, path);
    }

    /**
     * @param siteId
     * @param path
     * @return full file name
     */
    private static String getFullFileName(short siteId, String path) {
        if (CommonUtils.empty(path)) {
            path = CommonConstants.BLANK;
        }
        if (path.contains("..")) {
            path = path.replace("..", CommonConstants.BLANK);
        }
        if (path.startsWith(CommonConstants.SEPARATOR) || path.startsWith("\\")) {
            return SITE_PATH_PREFIX + siteId + path;
        }
        return SITE_PATH_PREFIX + siteId + CommonConstants.SEPARATOR + path;
    }

    /**
     * @param siteId
     * @param serverName
     * @param path
     * @return view name
     */
    public String getViewName(short siteId, String serverName, String path) {
        return getViewName(siteId, getDomain(serverName), path);
    }

    /**
     * @param siteId
     * @param sysDomain
     * @param path
     * @return view name
     */
    public String getViewName(short siteId, SysDomain sysDomain, String path) {
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
        return getFullTemplatePath(siteId, path);
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
                if (null != serverName && 0 < (index = serverName.indexOf(CommonConstants.DOT))) {
                    String subname = serverName.substring(index + 1);
                    sysDomain = domainCache.get(subname);
                    if (null == sysDomain) {
                        sysDomain = sysDomainService.getEntity(subname);
                        if (null == sysDomain || !sysDomain.isWild()) {
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
            if (path.startsWith(CommonConstants.SEPARATOR + site.getDirectory())) {
                index = path.indexOf(CommonConstants.SEPARATOR, 1);
            } else if (path.startsWith(site.getDirectory())) {
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
     * @param siteId
     * @param filepath
     * @return web file path
     */
    public String getWebFilePath(short siteId, String filepath) {
        return webFilePath + getFullFileName(siteId, filepath);
    }

    /**
     * @param siteId
     * @param filepath
     * @param newfile
     *            add new file name
     * @return web history file path
     */
    public String getWebHistoryFilePath(short siteId, String filepath, boolean newfile) {
        StringBuilder sb = new StringBuilder(webHistoryFilePath);
        sb.append(getFullFileName(siteId, filepath));
        sb.append(CommonConstants.SEPARATOR);
        if (newfile) {
            sb.append(DateFormatUtils.getDateFormat(FILE_NAME_FORMAT_STRING).format(CommonUtils.getDate()));
        }
        return sb.toString();
    }

    /**
     * @param siteId
     * @param filepath
     * @return web backup file path
     */
    public String getWebBackupFilePath(short siteId, String filepath) {
        return webBackupFilePath + getFullFileName(siteId, filepath);
    }

    /**
     * @param siteId
     * @param templatePath
     * @return task template file path
     */
    public String getTaskTemplateFilePath(short siteId, String templatePath) {
        return getTaskTemplateFilePath() + getFullTemplatePath(siteId, templatePath);
    }

    /**
     * @param siteId
     * @param templatePath
     * @param newfile
     *            add new file name
     * @return task template history file path
     */
    public String getTaskTemplateHistoryFilePath(short siteId, String templatePath, boolean newfile) {
        StringBuilder sb = new StringBuilder(taskTemplateHistoryFilePath);
        sb.append(getFullFileName(siteId, templatePath));
        sb.append(CommonConstants.SEPARATOR);
        if (newfile) {
            sb.append(DateFormatUtils.getDateFormat(FILE_NAME_FORMAT_STRING).format(CommonUtils.getDate()));
        }
        return sb.toString();
    }

    /**
     * @param siteId
     * @param templatePath
     * @return task template backup file path
     */
    public String getTaskTemplateBackupFilePath(short siteId, String templatePath) {
        return taskTemplateBackupFilePath + getFullFileName(siteId, templatePath);
    }

    /**
     * @param siteId
     * @param templatePath
     * @return template file path
     */
    public String getTemplateFilePath(short siteId, String templatePath) {
        return getTemplateFilePath() + getFullTemplatePath(siteId, templatePath);
    }

    /**
     * @param siteId
     * @param templatePath
     * @param newfile
     *            add new file name
     * @return template history file path
     */
    public String getTemplateHistoryFilePath(short siteId, String templatePath, boolean newfile) {
        StringBuilder sb = new StringBuilder(templateHistoryFilePath);
        sb.append(getFullFileName(siteId, templatePath));
        sb.append(CommonConstants.SEPARATOR);
        if (newfile) {
            sb.append(DateFormatUtils.getDateFormat(FILE_NAME_FORMAT_STRING).format(CommonUtils.getDate()));
        }
        return sb.toString();
    }

    /**
     * @param siteId
     * @param templatePath
     * @return template backup file path
     */
    public String getTemplateBackupFilePath(short siteId, String templatePath) {
        return templateBackupFilePath + getFullFileName(siteId, templatePath);
    }

    /**
     * @param siteId
     * @return model file path
     */
    public String getModelFilePath(short siteId) {
        return getTemplateFilePath() + getFullTemplatePath(siteId, MODEL_FILE);
    }

    /**
     * @param siteId
     * @return category type file path
     */
    public String getCategoryTypeFilePath(short siteId) {
        return getTemplateFilePath() + getFullTemplatePath(siteId, CATEGORY_TYPE_FILE);
    }

    /**
     * @param siteId
     * @return config file path
     */
    public String getConfigFilePath(short siteId) {
        return getTemplateFilePath() + getFullTemplatePath(siteId, CONFIG_FILE);
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
        this.templateFilePath = rootPath + TEMPLATE_PATH;
        this.templateBackupFilePath = rootPath + BACKUP_PATH + CommonConstants.SEPARATOR + TEMPLATE_PATH;
        this.taskTemplateBackupFilePath = rootPath + BACKUP_PATH + CommonConstants.SEPARATOR + TASK_FILE_PATH;
        this.webBackupFilePath = rootPath + BACKUP_PATH + CommonConstants.SEPARATOR + STATIC_FILE_PATH_WEB;
        this.templateHistoryFilePath = rootPath + HISTORY_PATH + CommonConstants.SEPARATOR + TEMPLATE_PATH;
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
    @Resource
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
     * @return task template file path
     */
    public String getTaskTemplateFilePath() {
        return taskTemplateFilePath;
    }

    /**
     * @return web template file path
     */
    public String getTemplateFilePath() {
        return templateFilePath;
    }
}
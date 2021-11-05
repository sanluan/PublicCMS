package com.publiccms.logic.component.site;

import java.beans.PropertyVetoException;
import java.io.IOException;
import java.io.StringReader;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.annotation.PreDestroy;

import org.apache.commons.lang3.time.DateUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.publiccms.common.annotation.CopyToDatasource;
import com.publiccms.common.api.SiteCache;
import com.publiccms.common.cache.CacheEntity;
import com.publiccms.common.cache.CacheEntityFactory;
import com.publiccms.common.constants.CommonConstants;
import com.publiccms.common.database.CmsDataSource;
import com.publiccms.common.handler.PageHandler;
import com.publiccms.common.servlet.InstallServlet;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.entities.cms.CmsComment;
import com.publiccms.entities.cms.CmsContent;
import com.publiccms.entities.sys.SysDatasource;
import com.publiccms.entities.sys.SysSiteDatasource;
import com.publiccms.logic.service.cms.CmsCommentService;
import com.publiccms.logic.service.cms.CmsContentService;
import com.publiccms.logic.service.sys.SysDatasourceService;
import com.publiccms.logic.service.sys.SysSiteDatasourceService;

/**
 *
 * DatasourceComponent
 * 
 */
@Aspect
@Order(Ordered.LOWEST_PRECEDENCE - 1)
@Component
public class DatasourceComponent implements SiteCache {

    protected final Log log = LogFactory.getLog(getClass());
    private static ExecutorService pool = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
    @Autowired
    private SysDatasourceService service;
    @Autowired
    private SysSiteDatasourceService siteDatasourceService;
    @Autowired
    private CmsContentService contentService;
    @Autowired
    private CmsCommentService commentService;
    @Autowired
    private CmsDataSource dataSource;
    private CacheEntity<Short, List<String>> cache;

    @Pointcut("@annotation(com.publiccms.common.annotation.CopyToDatasource)")
    public void copy() {
    }

    @Around("copy()")
    public Object copy(ProceedingJoinPoint pjp) throws Throwable {
        short siteId = 0;
        MethodSignature ms = (MethodSignature) pjp.getSignature();
        Method method = ms.getMethod();
        CopyToDatasource annotation = method.getAnnotation(CopyToDatasource.class);
        String field = annotation.field();
        Object[] args = pjp.getArgs();
        String[] parameterNames = ms.getParameterNames();
        for (int i = 0; i < parameterNames.length; i++) {
            if (field.equalsIgnoreCase(parameterNames[i])) {
                siteId = (short) args[i];
                break;
            }
        }
        if (0 != siteId) {
            try {
                Object result = pjp.proceed();
                pool.execute(new WriteTask(siteId, pjp));
                return result;
            } finally {
                CmsDataSource.resetDataSourceName();
            }
        } else {
            return pjp.proceed();
        }
    }

    /**
     * @param initDatabase
     * @param connection
     * @param entity
     * @param siteIds
     * @throws IOException
     * @throws PropertyVetoException
     */
    public void sync(boolean initDatabase, Connection connection, SysDatasource entity, Short[] siteIds)
            throws IOException, PropertyVetoException {
        if (CommonUtils.notEmpty(siteIds)) {
            if (initDatabase) {
                try {
                    InstallServlet.installDatasource(connection);
                } catch (SQLException | IOException e) {
                }
            }
            createDatasource(entity);
            pool.execute(new SyncTask(entity.getName(), siteIds, initDatabase));
        }
    }

    /**
     * @param siteId
     * @return
     */
    public List<String> getAllDatabase(short siteId) {
        List<String> datasourceList = getDatasourceList(siteId);
        for (String datasource : datasourceList) {
            if (!dataSource.contains(datasource)) {
                SysDatasource entity = service.getEntity(datasource);
                if (null != entity) {
                    try {
                        createDatasource(entity);
                    } catch (IOException | PropertyVetoException e) {
                        log.error(e.getMessage(), e);
                    }
                }
            }
        }
        return datasourceList;
    }

    /**
     * @param datasource
     * @param siteId
     */
    public void removeDatasource(short siteId, String datasource) {
        if (dataSource.contains(datasource)) {
            dataSource.remove(datasource);
        }
        List<String> list = cache.get(siteId);
        if (null != list) {
            list.remove(datasource);
        }
    }

    /**
     * @param siteId
     * @return
     */
    public String getRandomDatasource(short siteId) {
        List<String> datasourceList = getDatasourceList(siteId);
        if (CommonUtils.notEmpty(datasourceList)) {
            String datasource;
            if (datasourceList.size() == 1) {
                datasource = datasourceList.get(0);
            } else {
                datasource = datasourceList.get(CommonConstants.random.nextInt(datasourceList.size()));
            }
            if (dataSource.contains(datasource)) {
                return datasource;
            } else {
                SysDatasource entity = service.getEntity(datasource);
                if (null != entity) {
                    try {
                        createDatasource(entity);
                        return datasource;
                    } catch (IOException | PropertyVetoException e) {
                        log.error(e.getMessage(), e);
                    }
                }
            }
        }
        return CmsDataSource.DEFAULT_DATABASE_NAME;
    }

    private void saveContentList(String datasource, boolean saveFirst, List<CmsContent> list) {
        if (saveFirst) {
            try {
                CmsDataSource.setDataSourceName(datasource);
                contentService.save(list);
            } catch (Exception e) {
                List<CmsContent> result = contentService.batchUpdate(list);
                try {
                    contentService.save(result);
                } catch (Exception e2) {
                    log.error(e2.getMessage());
                }
            } finally {
                CmsDataSource.resetDataSourceName();
            }
        } else {
            try {
                CmsDataSource.setDataSourceName(datasource);
                List<CmsContent> result = contentService.batchUpdate(list);
                contentService.save(result);
            } catch (Exception e2) {
                log.error(e2.getMessage());
            } finally {
                CmsDataSource.resetDataSourceName();
            }
        }
    }

    private void saveCommentList(String datasource, boolean saveFirst, List<CmsComment> list) {
        if (saveFirst) {
            try {
                CmsDataSource.setDataSourceName(datasource);
                commentService.save(list);
            } catch (Exception e) {
                List<CmsComment> result = commentService.batchUpdate(list);
                try {
                    commentService.save(result);
                } catch (Exception e2) {
                    log.error(e2.getMessage());
                }
            } finally {
                CmsDataSource.resetDataSourceName();
            }
        } else {
            try {
                CmsDataSource.setDataSourceName(datasource);
                List<CmsComment> result = commentService.batchUpdate(list);
                commentService.save(result);
            } catch (Exception e2) {
                log.error(e2.getMessage());
            } finally {
                CmsDataSource.resetDataSourceName();
            }
        }
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
        cache = cacheEntityFactory.createCacheEntity("datasource");
    }

    /**
     * @param siteId
     * @param config
     * @return datasource list
     */
    private List<String> getDatasourceList(short siteId) {
        List<String> datasources = cache.get(siteId);
        if (null == datasources) {
            synchronized (cache) {
                datasources = cache.get(siteId);
                if (null == datasources) {
                    datasources = new ArrayList<>();
                    List<SysSiteDatasource> list = siteDatasourceService.getList(siteId, null);
                    for (SysSiteDatasource siteDatasource : list) {
                        datasources.add(siteDatasource.getId().getDatasource());
                    }
                    cache.put(siteId, datasources);
                }
            }
        }
        return datasources;
    }

    private void createDatasource(SysDatasource entity) throws IOException, PropertyVetoException {
        Properties properties = new Properties();
        properties.load(new StringReader(entity.getConfig()));
        dataSource.put(entity.getName(), CmsDataSource.getDataSource(properties));
    }

    @Override
    public void clear() {
        cache.clear();
    }

    @Override
    public void clear(short siteId) {
        cache.remove(siteId);
    }

    @PreDestroy
    public void destroy() {
        if (pool.isShutdown()) {
            pool.shutdown();
        }
    }

    public void cleanDisabledDatasource(int length) {
        Date now = CommonUtils.getMinuteDate();
        boolean cleanFlag = false;
        List<SysDatasource> entityList = service.getList(DateUtils.addMinutes(now, -length));
        for (SysDatasource entity : entityList) {
            if (dataSource.contains(entity.getName())) {
                dataSource.remove(entity.getName());
            }
            cleanFlag = true;
        }
        if (cleanFlag) {
            clear();
        }
    }

    /**
     * 
     * WriteTask 同步线程
     *
     */
    class WriteTask implements Runnable {
        private short siteId;
        private ProceedingJoinPoint pjp;

        public WriteTask(short siteId, ProceedingJoinPoint pjp) {
            super();
            this.siteId = siteId;
            this.pjp = pjp;
        }

        @Override
        public void run() {
            List<String> list = getAllDatabase(siteId);
            if (CommonUtils.notEmpty(list)) {
                for (String datasource : list) {
                    try {
                        CmsDataSource.setDataSourceName(datasource);
                        pjp.proceed();
                    } catch (Throwable e) {
                        log.error(e);
                    } finally {
                        CmsDataSource.resetDataSourceName();
                    }
                }
            }
        }
    }

    /**
     * 
     * SyncTask 同步线程
     *
     */
    class SyncTask implements Runnable {
        private String datasource;
        private Short[] siteIds;
        private boolean saveFirst;

        public SyncTask(String datasource, Short[] siteIds, boolean saveFirst) {
            this.datasource = datasource;
            this.siteIds = siteIds;
            this.saveFirst = saveFirst;
        }

        @SuppressWarnings("unchecked")
        @Override
        public void run() {
            {
                PageHandler page = contentService.getPage(siteIds, null, PageHandler.MAX_PAGE_SIZE);
                List<CmsContent> list = (List<CmsContent>) page.getList();
                saveContentList(datasource, saveFirst, list);
                if (!page.isLastPage()) {
                    for (int i = 2; i <= page.getTotalPage(); i++) {
                        try {
                            Thread.sleep(100L);
                        } catch (InterruptedException e) {
                            log.error(e.getMessage());
                        }
                        list = (List<CmsContent>) contentService.getPage(siteIds, i, PageHandler.MAX_PAGE_SIZE).getList();
                        saveContentList(datasource, saveFirst, list);
                    }
                }
            }

            {
                PageHandler page = commentService.getPage(siteIds, null, PageHandler.MAX_PAGE_SIZE);
                List<CmsComment> list = (List<CmsComment>) page.getList();
                saveCommentList(datasource, saveFirst, list);
                if (!page.isLastPage()) {
                    for (int i = 2; i <= page.getTotalPage(); i++) {
                        try {
                            Thread.sleep(100L);
                        } catch (InterruptedException e) {
                            log.error(e.getMessage());
                        }
                        list = (List<CmsComment>) commentService.getPage(siteIds, i, PageHandler.MAX_PAGE_SIZE).getList();
                        saveCommentList(datasource, saveFirst, list);
                    }
                }
            }
        }
    }
}

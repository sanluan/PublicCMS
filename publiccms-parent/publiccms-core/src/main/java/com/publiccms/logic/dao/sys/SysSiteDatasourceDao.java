package com.publiccms.logic.dao.sys;

import java.util.Collections;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.publiccms.common.base.BaseDao;
import com.publiccms.common.handler.QueryHandler;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.entities.sys.SysSiteDatasource;

/**
 *
 * SysSiteDatasourceDao
 * 
 */
@Repository
public class SysSiteDatasourceDao extends BaseDao<SysSiteDatasource> {

    /**
     * @param siteId
     * @param datasource
     * @return results list
     */
    @SuppressWarnings("unchecked")
    public List<SysSiteDatasource> getList(Short siteId, String datasource) {
        QueryHandler queryHandler = getQueryHandler("from SysSiteDatasource bean");
        if (CommonUtils.notEmpty(siteId)) {
            queryHandler.condition("bean.id.siteId = :siteId").setParameter("siteId", siteId);
        }
        if (CommonUtils.notEmpty(datasource)) {
            queryHandler.condition("bean.id.datasource = :datasource").setParameter("datasource", datasource);
        }
        return (List<SysSiteDatasource>) getList(queryHandler);
    }

    /**
     * @param siteIds
     * @param datasource
     * @return entity
     */
    public SysSiteDatasource getEntity(Short[] siteIds, String datasource) {
        if (CommonUtils.notEmpty(siteIds) && CommonUtils.notEmpty(datasource)) {
            QueryHandler queryHandler = getQueryHandler("from SysSiteDatasource bean");
            queryHandler.condition("bean.id.siteId in (:siteIds)").setParameter("siteIds", siteIds);
            queryHandler.condition("bean.id.datasource = :datasource").setParameter("datasource", datasource);
            return getEntity(queryHandler);
        }
        return null;
    }

    /**
     * @param siteIds
     * @param datasources
     * @return entitys list
     */
    @SuppressWarnings("unchecked")
    public List<SysSiteDatasource> getEntitys(Short[] siteIds, String[] datasources) {
        if (CommonUtils.notEmpty(siteIds) && CommonUtils.notEmpty(datasources)) {
            QueryHandler queryHandler = getQueryHandler("from SysSiteDatasource bean");
            queryHandler.condition("bean.id.siteId in (:siteIds)").setParameter("siteIds", siteIds);
            queryHandler.condition("bean.id.datasource in (:datasources)").setParameter("datasources", datasources);
            return (List<SysSiteDatasource>) getList(queryHandler);
        }
        return Collections.emptyList();
    }

    /**
     * @param siteId
     * @return number of data deleted
     */
    public int deleteBySiteId(Short siteId) {
        if (CommonUtils.notEmpty(siteId)) {
            QueryHandler queryHandler = getQueryHandler("delete from SysSiteDatasource bean where bean.id.siteId = :siteId");
            queryHandler.setParameter("siteId", siteId);
            return delete(queryHandler);
        }
        return 0;
    }

    /**
     * @param datasource
     * @return number of data deleted
     */
    public int deleteByDatasource(String datasource) {
        if (CommonUtils.notEmpty(datasource)) {
            QueryHandler queryHandler = getQueryHandler(
                    "delete from SysSiteDatasource bean where bean.id.datasource = :datasource");
            queryHandler.setParameter("datasource", datasource);
            return delete(queryHandler);
        }
        return 0;
    }

    @Override
    protected SysSiteDatasource init(SysSiteDatasource entity) {
        return entity;
    }

}
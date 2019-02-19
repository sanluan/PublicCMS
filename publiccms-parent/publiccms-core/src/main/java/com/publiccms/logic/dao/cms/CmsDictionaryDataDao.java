package com.publiccms.logic.dao.cms;

import java.util.List;

import com.publiccms.entities.cms.CmsDictionaryData;

// Generated 2016-11-20 14:50:55 by com.publiccms.common.source.SourceGenerator

import org.springframework.stereotype.Repository;

import com.publiccms.common.base.BaseDao;
import com.publiccms.common.handler.QueryHandler;

/**
 *
 * CmsDictionaryDataDao
 * 
 */
@Repository
public class CmsDictionaryDataDao extends BaseDao<CmsDictionaryData> {

    /**
     * @param siteId
     * @param dictionaryId
     * @return results page
     */
    @SuppressWarnings("unchecked")
    public List<CmsDictionaryData> getList(short siteId, String dictionaryId) {
        QueryHandler queryHandler = getQueryHandler("from CmsDictionaryData bean");
        queryHandler.condition("bean.id.siteId = :siteId").setParameter("siteId", siteId);
        queryHandler.condition("bean.id.dictionaryId = :dictionaryId").setParameter("dictionaryId", dictionaryId);
        queryHandler.order("bean.id.value asc");
        return (List<CmsDictionaryData>) getList(queryHandler);
    }

    /**
     * @param siteId
     * @param dictionaryIds
     * @return the number of entities deleted
     */
    public int delete(short siteId, String[] dictionaryIds) {
        QueryHandler queryHandler = getQueryHandler("delete from CmsDictionaryData bean");
        queryHandler.condition("bean.id.siteId = :siteId").setParameter("siteId", siteId);
        queryHandler.condition("bean.id.dictionaryId in (:dictionaryIds)").setParameter("dictionaryIds", dictionaryIds);
        return delete(queryHandler);
    }

    @Override
    protected CmsDictionaryData init(CmsDictionaryData entity) {
        return entity;
    }

}
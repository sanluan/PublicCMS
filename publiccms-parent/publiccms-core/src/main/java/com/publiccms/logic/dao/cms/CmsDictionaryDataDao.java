package com.publiccms.logic.dao.cms;

import java.util.List;

// Generated 2016-11-20 14:50:55 by com.publiccms.common.generator.SourceGenerator

import org.springframework.stereotype.Repository;

import com.publiccms.common.base.BaseDao;
import com.publiccms.common.handler.QueryHandler;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.entities.cms.CmsDictionaryData;

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
     * @param parentValue
     * @return results page
     */
    public List<CmsDictionaryData> getList(short siteId, String dictionaryId, String parentValue) {
        QueryHandler queryHandler = getQueryHandler("from CmsDictionaryData bean");
        queryHandler.condition("bean.id.siteId = :siteId").setParameter("siteId", siteId);
        queryHandler.condition("bean.id.dictionaryId = :dictionaryId").setParameter("dictionaryId", dictionaryId);
        if (CommonUtils.notEmpty(parentValue)) {
            queryHandler.condition("bean.parentValue = :parentValue").setParameter("parentValue", parentValue);
        }else {
            queryHandler.condition("bean.parentValue is null");
        }
        queryHandler.order("bean.sort asc");
        queryHandler.order("bean.id.value asc");
        return getList(queryHandler);
    }
    
    /**
     * @param siteId
     * @param dictionaryId
     * @return results page
     */
    public List<CmsDictionaryData> getList(short siteId, String dictionaryId) {
        QueryHandler queryHandler = getQueryHandler("from CmsDictionaryData bean");
        queryHandler.condition("bean.id.siteId = :siteId").setParameter("siteId", siteId);
        queryHandler.condition("bean.id.dictionaryId = :dictionaryId").setParameter("dictionaryId", dictionaryId);
        queryHandler.order("bean.sort asc");
        queryHandler.order("bean.id.value asc");
        return getList(queryHandler);
    }

    /**
     * @param siteId
     * @param dictionaryId
     * @param parentValue
     * @return the number of entities deleted
     */
    public int deleteByParentValue(short siteId, String dictionaryId, String parentValue) {
        QueryHandler queryHandler = getQueryHandler("delete from CmsDictionaryData bean");
        queryHandler.condition("bean.id.siteId = :siteId").setParameter("siteId", siteId);
        queryHandler.condition("bean.id.dictionaryId = :dictionaryId").setParameter("dictionaryId", dictionaryId);
        queryHandler.condition("bean.parentValue = :parentValue").setParameter("parentValue", parentValue);
        return delete(queryHandler);
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
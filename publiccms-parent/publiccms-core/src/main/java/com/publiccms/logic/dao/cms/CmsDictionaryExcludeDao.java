package com.publiccms.logic.dao.cms;

import java.util.List;

// Generated 2022-03-15 by com.publiccms.common.generator.SourceGenerator

import org.springframework.stereotype.Repository;

import com.publiccms.common.base.BaseDao;
import com.publiccms.common.handler.QueryHandler;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.entities.cms.CmsDictionaryExclude;

/**
 *
 * CmsDictionaryExcludeDao
 * 
 */
@Repository
public class CmsDictionaryExcludeDao extends BaseDao<CmsDictionaryExclude> {

    /**
     * @param siteId
     * @param dictionaryId
     * @param excludeDictionaryId
     * @return results page
     */
    @SuppressWarnings("unchecked")
    public List<CmsDictionaryExclude> getList(short siteId, String dictionaryId, String excludeDictionaryId) {
        QueryHandler queryHandler = getQueryHandler("from CmsDictionaryExclude bean");
        queryHandler.condition("bean.id.siteId = :siteId").setParameter("siteId", siteId);
        if (CommonUtils.notEmpty(dictionaryId)) {
            queryHandler.condition("bean.id.dictionaryId = :dictionaryId").setParameter("dictionaryId", dictionaryId);
        }
        if (CommonUtils.notEmpty(excludeDictionaryId)) {
            queryHandler.condition("bean.id.excludeDictionaryId = :excludeDictionaryId").setParameter("excludeDictionaryId",
                    excludeDictionaryId);
        }
        return (List<CmsDictionaryExclude>) getList(queryHandler);
    }

    /**
     * @param siteId
     * @param dictionaryIds
     * @return the number of entities deleted
     */
    public int delete(short siteId, String[] dictionaryIds) {
        QueryHandler queryHandler = getQueryHandler("delete from CmsDictionaryExclude bean");
        queryHandler.condition("bean.id.siteId = :siteId").setParameter("siteId", siteId);
        queryHandler.condition("(bean.id.dictionaryId in (:dictionaryIds) or bean.id.excludeDictionaryId in (:dictionaryIds))")
                .setParameter("dictionaryIds", dictionaryIds);
        return delete(queryHandler);
    }

    @Override
    protected CmsDictionaryExclude init(CmsDictionaryExclude entity) {
        return entity;
    }

}
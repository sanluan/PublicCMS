package com.publiccms.logic.dao.cms;

import java.util.List;

// Generated 2022-03-15 by com.publiccms.common.generator.SourceGenerator

import org.springframework.stereotype.Repository;

import com.publiccms.common.base.BaseDao;
import com.publiccms.common.handler.QueryHandler;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.entities.cms.CmsDictionaryExcludeId;
import com.publiccms.entities.cms.CmsDictionaryExcludeValue;

/**
 *
 * CmsDictionaryExcludeValueDao
 * 
 */
@Repository
public class CmsDictionaryExcludeValueDao extends BaseDao<CmsDictionaryExcludeValue> {
    /** 
     * @param siteId
     * @param dictionaryId
     * @param excludeDictionaryId
     * @return results page
     */
    @SuppressWarnings("unchecked")
    public List<CmsDictionaryExcludeValue> getList(short siteId, String dictionaryId, String excludeDictionaryId) {
        QueryHandler queryHandler = getQueryHandler("from CmsDictionaryExcludeValue bean");
        queryHandler.condition("bean.id.siteId = :siteId").setParameter("siteId", siteId);
        if (CommonUtils.notEmpty(dictionaryId)) {
            queryHandler.condition("bean.id.dictionaryId = :dictionaryId").setParameter("dictionaryId", dictionaryId);
        }
        if (CommonUtils.notEmpty(excludeDictionaryId)) {
            queryHandler.condition("bean.id.excludeDictionaryId = :excludeDictionaryId").setParameter("excludeDictionaryId",
                    excludeDictionaryId);
        }
        return (List<CmsDictionaryExcludeValue>) getList(queryHandler);
    }

    /**
     * @param excludeId
     * @return the number of entities deleted
     */
    public int delete(CmsDictionaryExcludeId excludeId) {
        QueryHandler queryHandler = getQueryHandler("delete from CmsDictionaryExcludeValue bean");
        queryHandler.condition("bean.id.siteId = :siteId").setParameter("siteId", excludeId.getSiteId());
        queryHandler.condition("bean.id.dictionaryId = :dictionaryId").setParameter("dictionaryId", excludeId.getDictionaryId());
        queryHandler.condition("bean.id.excludeDictionaryId = :excludeDictionaryId").setParameter("excludeDictionaryId",
                excludeId.getExcludeDictionaryId());
        return delete(queryHandler);
    }

    /**
     * @param siteId
     * @param dictionaryIds
     * @return the number of entities deleted
     */
    public int delete(short siteId, String[] dictionaryIds) {
        QueryHandler queryHandler = getQueryHandler("delete from CmsDictionaryExcludeValue bean");
        queryHandler.condition("bean.id.siteId = :siteId").setParameter("siteId", siteId);
        queryHandler.condition("(bean.id.dictionaryId in (:dictionaryIds) or bean.id.excludeDictionaryId in (:dictionaryIds) )")
                .setParameter("dictionaryIds", dictionaryIds);
        return delete(queryHandler);
    }

    /**
     * @param siteId
     * @param dictionaryId
     * @param value
     * @return the number of entities deleted
     */
    public int deleteByValue(short siteId, String dictionaryId, String value) {
        QueryHandler queryHandler = getQueryHandler("delete from CmsDictionaryExcludeValue bean");
        queryHandler.condition("bean.id.siteId = :siteId").setParameter("siteId", siteId);
        queryHandler.condition("bean.id.dictionaryId = :dictionaryId").setParameter("dictionaryId", dictionaryId);
        queryHandler.condition("bean.id.value  = :value").setParameter("value", value);
        return delete(queryHandler);
    }

    @Override
    protected CmsDictionaryExcludeValue init(CmsDictionaryExcludeValue entity) {
        return entity;
    }

}
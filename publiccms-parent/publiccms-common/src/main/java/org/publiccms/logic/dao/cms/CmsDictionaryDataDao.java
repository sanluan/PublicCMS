package org.publiccms.logic.dao.cms;

import java.util.List;

import org.publiccms.entities.cms.CmsDictionaryData;

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
     * @param dictionaryId
     * @param pageIndex
     * @param pageSize
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<CmsDictionaryData> getList(long dictionaryId) {
        QueryHandler queryHandler = getQueryHandler("from CmsDictionaryData bean");
        queryHandler.condition("bean.id.dictionaryId = :dictionaryId").setParameter("dictionaryId", dictionaryId);
        queryHandler.order("bean.id.value asc");
        return (List<CmsDictionaryData>) getList(queryHandler);
    }

    @Override
    protected CmsDictionaryData init(CmsDictionaryData entity) {
        return entity;
    }

}
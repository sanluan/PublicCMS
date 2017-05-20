package org.publiccms.logic.dao.cms;

import org.publiccms.entities.cms.CmsDictionaryData;

// Generated 2016-11-20 14:50:55 by com.publiccms.common.source.SourceGenerator


import org.springframework.stereotype.Repository;

import com.publiccms.common.base.BaseDao;
import com.publiccms.common.handler.PageHandler;
import com.publiccms.common.handler.QueryHandler;

/**
 *
 * CmsDictionaryDataDao
 * 
 */
@Repository
public class CmsDictionaryDataDao extends BaseDao<CmsDictionaryData> {
    
    /**
     * @param pageIndex
     * @param pageSize
     * @return
     */
    public PageHandler getPage(
                Integer pageIndex, Integer pageSize) {
        QueryHandler queryHandler = getQueryHandler("from CmsDictionaryData bean");
        queryHandler.order("bean.id desc");
        return getPage(queryHandler, pageIndex, pageSize);
    }

    @Override
    protected CmsDictionaryData init(CmsDictionaryData entity) {
        return entity;
    }

}
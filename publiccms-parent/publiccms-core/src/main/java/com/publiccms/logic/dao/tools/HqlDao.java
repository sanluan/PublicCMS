package com.publiccms.logic.dao.tools;

import java.util.Map;
import java.util.Map.Entry;

import org.apache.lucene.analysis.Analyzer;
import org.springframework.stereotype.Repository;

import com.publiccms.common.base.BaseDao;
import com.publiccms.common.handler.PageHandler;
import com.publiccms.common.handler.QueryHandler;
import com.publiccms.common.tools.CommonUtils;

/**
 *
 * HqlDao
 * 
 */
@Repository
public class HqlDao extends BaseDao<Object> {
    
    /**
     * @param hql
     * @param parameters
     * @param pageIndex
     * @param pageSize
     * @return results page
     */
    public PageHandler getPage(String hql, Map<String, Object> parameters, Integer pageIndex, Integer pageSize) {
        QueryHandler queryHandler = getQueryHandler(hql);
        if (CommonUtils.notEmpty(parameters)) {
            for (Entry<String, Object> entry : parameters.entrySet()) {
                queryHandler.setParameter(entry.getKey(), entry.getValue());
            }
        }
        return getPage(queryHandler, pageIndex, pageSize);
    }

    /**
     * @param hql
     * @return number of data updated
     */
    public int update(String hql) {
        QueryHandler queryHandler = getQueryHandler(hql);
        return update(queryHandler);
    }

    /**
     * @param hql
     * @return number of data deleted
     */
    public int delete(String hql) {
        QueryHandler queryHandler = getQueryHandler(hql);
        return delete(queryHandler);
    }

    /**
     * @return analyzer
     */
    public Analyzer getAnalyzer() {
        return getFullTextSession().getSearchFactory().getAnalyzer("cms");
    }

    @Override
    protected Object init(Object entity) {
        return entity;
    }

    /**
     * 
     */
    public void clear() {
        sessionFactory.getCache().evictAllRegions();
    }

}

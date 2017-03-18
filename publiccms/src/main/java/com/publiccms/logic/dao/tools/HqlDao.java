package com.publiccms.logic.dao.tools;

import java.util.Map;
import java.util.Map.Entry;

import org.apache.lucene.analysis.Analyzer;
import org.springframework.stereotype.Repository;

import com.sanluan.common.base.BaseDao;
import com.sanluan.common.handler.PageHandler;
import com.sanluan.common.handler.QueryHandler;

@Repository
public class HqlDao extends BaseDao<Object> {
    public PageHandler getPage(String hql, Map<String, Object> paramters, Integer pageIndex, Integer pageSize) {
        QueryHandler queryHandler = getQueryHandler(hql);
        if (notEmpty(paramters)) {
            for (Entry<String, Object> entry : paramters.entrySet()) {
                queryHandler.setParameter(entry.getKey(), entry.getValue());
            }
        }
        return getPage(queryHandler, pageIndex, pageSize);
    }

    public int update(String hql) {
        QueryHandler queryHandler = getQueryHandler(hql);
        return update(queryHandler);
    }

    public int delete(String hql) {
        QueryHandler queryHandler = getDeleteQueryHandler(hql);
        return delete(queryHandler);
    }

    public Analyzer getAnalyzer() {
        return super.getFullTextSession().getSearchFactory().getAnalyzer("default");
    }

    @Override
    protected Object init(Object entity) {
        return entity;
    }

    public void clear() {
        sessionFactory.getCache().evictAllRegions();
    }

}

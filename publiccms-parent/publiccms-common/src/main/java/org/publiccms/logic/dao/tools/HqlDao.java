package org.publiccms.logic.dao.tools;

import static com.publiccms.common.tools.CommonUtils.notEmpty;

import java.util.Map;
import java.util.Map.Entry;

import org.apache.lucene.analysis.Analyzer;
import org.springframework.stereotype.Repository;

import com.publiccms.common.base.BaseDao;
import com.publiccms.common.handler.PageHandler;
import com.publiccms.common.handler.QueryHandler;

/**
 *
 * HqlDao
 * 
 */
@Repository
public class HqlDao extends BaseDao<Object> {
    
    /**
     * @param hql
     * @param paramters
     * @param pageIndex
     * @param pageSize
     * @return
     */
    public PageHandler getPage(String hql, Map<String, Object> paramters, Integer pageIndex, Integer pageSize) {
        QueryHandler queryHandler = getQueryHandler(hql);
        if (notEmpty(paramters)) {
            for (Entry<String, Object> entry : paramters.entrySet()) {
                queryHandler.setParameter(entry.getKey(), entry.getValue());
            }
        }
        return getPage(queryHandler, pageIndex, pageSize);
    }

    /**
     * @param hql
     * @return
     */
    public int update(String hql) {
        QueryHandler queryHandler = getQueryHandler(hql);
        return update(queryHandler);
    }

    /**
     * @param hql
     * @return
     */
    public int delete(String hql) {
        QueryHandler queryHandler = getDeleteQueryHandler(hql);
        return delete(queryHandler);
    }

    /**
     * @return
     */
    public Analyzer getAnalyzer() {
        return super.getFullTextSession().getSearchFactory().getAnalyzer("default");
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

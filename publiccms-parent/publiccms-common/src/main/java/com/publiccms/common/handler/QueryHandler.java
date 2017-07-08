package com.publiccms.common.handler;

import java.util.HashMap;
import java.util.Map;

import org.hibernate.Query;
import org.hibernate.Session;

/**
 *
 * QueryHandler
 * 
 */
public class QueryHandler {
    
    /**
     * 
     */
    public static final String COUNT_SQL = "select count(*) ";
    /**
     * 
     */
    public static final String KEYWORD_FROM = " from ";
    /**
     * 
     */
    public static final String KEYWORD_ORDER = " order by ";
    /**
     * 
     */
    public static final String KEYWORD_GROUP = " group by ";
    
    boolean whereFlag = true;
    boolean orderFlag = true;
    boolean groupFlag = true;
    private StringBuilder sqlBuilder;
    private Map<String, Object> map;
    private Map<String, Object[]> arrayMap;
    private Integer firstResult;
    private Integer maxResults;
    private Boolean cacheable;

    /**
     * @param sql
     */
    public QueryHandler(String sql) {
        this.sqlBuilder = new StringBuilder(" ");
        sqlBuilder.append(sql);
    }

    /**
     * 
     */
    public QueryHandler() {
        this.sqlBuilder = new StringBuilder();
    }

    /**
     * @param session
     * @return
     */
    public Query getQuery(Session session) {
        return getQuery(session, getSql());
    }

    /**
     * @param session
     * @return
     */
    public Query getCountQuery(Session session) {
        return getQuery(session, getCountSql());
    }

    /**
     * @param condition
     * @return
     */
    public QueryHandler condition(String condition) {
        if (whereFlag) {
            whereFlag = false;
            sqlBuilder.append(" where ");
        } else {
            sqlBuilder.append(" and ");
        }
        sqlBuilder.append(condition);
        return this;
    }

    /**
     * @param sqlString
     * @return
     */
    public QueryHandler order(String sqlString) {
        if (orderFlag) {
            orderFlag = false;
            append(KEYWORD_ORDER);
        } else {
            sqlBuilder.append(',');
        }
        sqlBuilder.append(sqlString);
        return this;
    }

    /**
     * @param sqlString
     * @return
     */
    public QueryHandler group(String sqlString) {
        if (groupFlag) {
            groupFlag = false;
            sqlBuilder.append(KEYWORD_GROUP);
        } else {
            sqlBuilder.append(',');
        }
        sqlBuilder.append(sqlString);
        return this;
    }

    /**
     * @param sqlString
     * @return
     */
    public QueryHandler append(String sqlString) {
        sqlBuilder.append(" ");
        sqlBuilder.append(sqlString);
        return this;
    }

    /**
     * @param firstResult
     * @return
     */
    public QueryHandler setFirstResult(int firstResult) {
        this.firstResult = firstResult;
        return this;
    }

    /**
     * @param maxResults
     * @return
     */
    public QueryHandler setMaxResults(int maxResults) {
        this.maxResults = maxResults;
        return this;
    }

    /**
     * @param cacheable
     * @return
     */
    public QueryHandler setCacheable(boolean cacheable) {
        this.cacheable = cacheable;
        return this;
    }

    /**
     * @param key
     * @param value
     * @return
     */
    public QueryHandler setParameter(String key, Object value) {
        if (null == map) {
            map = new HashMap<>();
        }
        map.put(key, value);
        return this;
    }

    /**
     * @param key
     * @param value
     * @return
     */
    public QueryHandler setParameter(String key, Object[] value) {
        if (null == arrayMap) {
            arrayMap = new HashMap<>();
        }
        arrayMap.put(key, value);
        return this;
    }

    private Query getQuery(Session session, String sql) {
        Query query = session.createQuery(sql);
        if (null != map) {
            for (String key : map.keySet()) {
                query.setParameter(key, map.get(key));
            }
        }
        if (null != arrayMap) {
            for (String key : arrayMap.keySet()) {
                query.setParameterList(key, arrayMap.get(key));
            }
        }
        if (null != firstResult) {
            query.setFirstResult(firstResult);
        }
        if (null != maxResults) {
            query.setMaxResults(maxResults);
        }
        if (null != cacheable) {
            query.setCacheable(cacheable);
        } else {
            query.setCacheable(true);
        }
        return query;
    }

    private String getSql() {
        return sqlBuilder.toString();
    }

    private String getCountSql() {
        String sql = getSql();
        sql = sql.substring(sql.toLowerCase().indexOf(KEYWORD_FROM));
        int orderIndex = sql.toLowerCase().indexOf(KEYWORD_ORDER);
        if (-1 != orderIndex) {
            sql = sql.substring(0, orderIndex);
        }
        return COUNT_SQL + sql;
    }
    
}

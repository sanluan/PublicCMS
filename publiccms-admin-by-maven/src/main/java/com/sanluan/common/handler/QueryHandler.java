package com.sanluan.common.handler;

import java.util.HashMap;
import java.util.Map;

import org.hibernate.Query;
import org.hibernate.Session;

public class QueryHandler {
    public static final String COUNT_SQL = "select count(*) ";
    public static final String KEYWORD_FROM = "from ";
    public static final String KEYWORD_GROUP = "group by";
    boolean whereFlag = true;
    private StringBuilder sqlBuilder;
    private Map<String, Object> map;
    private Map<String, Object[]> arrayMap;
    private Integer firstResult;
    private Integer maxResults;
    private Boolean cacheable;

    public QueryHandler(String sql) {
        this.sqlBuilder = new StringBuilder(sql);
    }

    public QueryHandler() {
        this.sqlBuilder = new StringBuilder();
    }

    public Query getQuery(Session session) {
        return getQuery(session, getSql());
    }

    public Query getCountQuery(Session session) {
        return getQuery(session, getCountSql());
    }

    public QueryHandler condition(String condition) {
        interval();
        sqlBuilder.append(condition);
        return this;
    }

    public QueryHandler append(String sqlString) {
        sqlBuilder.append(" ");
        sqlBuilder.append(sqlString);
        return this;
    }

    public QueryHandler setFirstResult(int firstResult) {
        this.firstResult = firstResult;
        return this;
    }

    public QueryHandler setMaxResults(int maxResults) {
        this.maxResults = maxResults;
        return this;
    }

    public QueryHandler setCacheable(boolean cacheable) {
        this.cacheable = cacheable;
        return this;
    }

    public QueryHandler setParameter(String key, Object value) {
        if (null == map) {
            map = new HashMap<String, Object>();
        }
        map.put(key, value);
        return this;
    }

    public QueryHandler setParameter(String key, Object[] value) {
        if (null == arrayMap) {
            arrayMap = new HashMap<String, Object[]>();
        }
        arrayMap.put(key, value);
        return this;
    }

    private void interval() {
        if (whereFlag) {
            whereFlag = false;
            sqlBuilder.append(" where ");
        } else {
            sqlBuilder.append(" and ");
        }
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
        }
        return query;
    }

    private String getSql() {
        return sqlBuilder.toString();
    }

    private String getCountSql() {
        String sql = getSql();
        sql = sql.substring(sql.toLowerCase().indexOf(KEYWORD_FROM));
        int groupIndex = sql.toLowerCase().indexOf(KEYWORD_GROUP);
        if (-1 != groupIndex) {
            sql = sql.substring(0, groupIndex);
        }
        return COUNT_SQL + sql;
    }
}

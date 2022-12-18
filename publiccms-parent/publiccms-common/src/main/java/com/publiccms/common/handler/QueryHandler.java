package com.publiccms.common.handler;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;

import com.publiccms.common.constants.Constants;

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
    private Integer firstResult;
    private Integer maxResults;

    /**
     * @param sql
     */
    public QueryHandler(String sql) {
        this.sqlBuilder = new StringBuilder(Constants.BLANK_SPACE);
        sqlBuilder.append(sql);
    }

    /**
     * 
     */
    public QueryHandler() {
        this.sqlBuilder = new StringBuilder();
    }

    /**
     * @param condition
     * @return query handler
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
     * @return query handler
     */
    public QueryHandler order(String sqlString) {
        if (orderFlag) {
            orderFlag = false;
            append(KEYWORD_ORDER);
        } else {
            sqlBuilder.append(Constants.COMMA_DELIMITED);
        }
        sqlBuilder.append(sqlString);
        return this;
    }

    /**
     * @param sqlString
     * @return query handler
     */
    public QueryHandler group(String sqlString) {
        if (groupFlag) {
            groupFlag = false;
            sqlBuilder.append(KEYWORD_GROUP);
        } else {
            sqlBuilder.append(Constants.COMMA_DELIMITED);
        }
        sqlBuilder.append(sqlString);
        return this;
    }

    /**
     * @param sqlString
     * @return query handler
     */
    public QueryHandler append(String sqlString) {
        sqlBuilder.append(Constants.BLANK_SPACE);
        sqlBuilder.append(sqlString);
        return this;
    }

    /**
     * @param firstResult
     * @return query handler
     */
    public QueryHandler setFirstResult(int firstResult) {
        this.firstResult = firstResult;
        return this;
    }

    /**
     * @param maxResults
     * @return query handler
     */
    public QueryHandler setMaxResults(int maxResults) {
        this.maxResults = maxResults;
        return this;
    }

    /**
     * @param key
     * @param value
     * @return query handler
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
     * @param values 
     * @return query handler
     */
    public QueryHandler setParameter(String key, Object[] values) {
        if (null == map) {
            map = new HashMap<>();
        }
        map.put(key, Arrays.asList(values));
        return this;
    }
    
    /**
     * @param key
     * @param value
     * @return query handler
     */
    public QueryHandler setParameter(String key, Collection<?> value) {
        if (null == map) {
            map = new HashMap<>();
        }
        map.put(key, value);
        return this;
    }

    public <T> TypedQuery<T> initQuery(TypedQuery<T> query) {
        return initQuery(query, true);
    }

    public <T> TypedQuery<T> initQuery(TypedQuery<T> query, boolean pageable) {
        if (null != map) {
            for (String key : map.keySet()) {
                query.setParameter(key, map.get(key));
            }
        }
        if (pageable) {
            if (null != firstResult) {
                query.setFirstResult(firstResult);
            }
            if (null != maxResults) {
                query.setMaxResults(maxResults);
            }
        }
        return query;
    }

    public Query initQuery(Query query) {
        return initQuery(query, true);
    }

    public Query initQuery(Query query, boolean pageable) {
        if (null != map) {
            for (String key : map.keySet()) {
                query.setParameter(key, map.get(key));
            }
        }
        if (pageable) {
            if (null != firstResult) {
                query.setFirstResult(firstResult);
            }
            if (null != maxResults) {
                query.setMaxResults(maxResults);
            }
        }
        return query;
    }

    public String getSql() {
        return sqlBuilder.toString();
    }

    public String getCountSql() {
        String sql = getSql();
        sql = sql.substring(sql.toLowerCase().indexOf(KEYWORD_FROM));
        int orderIndex = sql.toLowerCase().indexOf(KEYWORD_ORDER);
        if (-1 != orderIndex) {
            sql = sql.substring(0, orderIndex);
        }
        return COUNT_SQL + sql;
    }

}

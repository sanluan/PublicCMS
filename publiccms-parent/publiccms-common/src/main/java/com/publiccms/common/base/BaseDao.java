package com.publiccms.common.base;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Future;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.ObjectNotFoundException;
import org.hibernate.query.Query;
import org.hibernate.query.NativeQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.search.jpa.FullTextQuery;
import org.hibernate.search.FullTextSession;
import org.hibernate.search.Search;
import org.hibernate.search.query.dsl.QueryBuilder;
import org.hibernate.search.query.engine.spi.FacetManager;
import org.hibernate.search.query.facet.Facet;
import org.hibernate.search.query.facet.FacetSortOrder;
import org.hibernate.search.query.facet.FacetingRequest;
import org.springframework.beans.factory.annotation.Autowired;

import com.publiccms.common.handler.FacetPageHandler;
import com.publiccms.common.handler.PageHandler;
import com.publiccms.common.handler.QueryHandler;
import com.publiccms.common.tools.CommonUtils;

/**
 * DAO基类
 * 
 * Base DAO
 * 
 * @param <E>
 * 
 */
public abstract class BaseDao<E> {
    protected final Log log = LogFactory.getLog(getClass());
    /**
     * 分面名称搜索前缀
     * 
     * Facet name suffix
     */
    public final static String FACET_NAME_SUFFIX = "FacetRequest";
    /**
     * 倒序
     * 
     * order type desc
     */
    public final static String ORDERTYPE_DESC = "desc";
    /**
     * 顺序
     * 
     * order type desc
     */
    public final static String ORDERTYPE_ASC = "asc";
    private Class<E> clazz;

    /**
     * 查询处理器
     * 
     * @param hql
     * @return queryhandler
     */
    public static QueryHandler getQueryHandler(String hql) {
        return new QueryHandler(hql);
    }

    /**
     * 查询处理器
     * 
     * @return queryhandler
     */
    public static QueryHandler getQueryHandler() {
        return new QueryHandler();
    }

    /**
     * Like查询
     * 
     * @param var
     * @return like query
     */
    public static String like(String var) {
        return "%" + var + "%";
    }

    /**
     * 左Like查询
     * 
     * @param var
     * @return left like query
     */
    public static String leftLike(String var) {
        return "%" + var;
    }

    /**
     * 右Like查询
     * 
     * @param var
     * @return right like query
     */
    public static String rightLike(String var) {
        return var + "%";
    }

    /**
     * 获取实体
     * 
     * @param id
     * @return entity
     */
    public E getEntity(Serializable id) {
        return null != id ? (E) getSession().get(getEntityClass(), id) : null;
    }

    /**
     * 获取实体
     * 
     * @param id
     * @param primaryKeyName
     * @return entity
     */
    public E getEntity(Serializable id, String primaryKeyName) {
        QueryHandler queryHandler = getQueryHandler("from").append(getEntityClass().getSimpleName()).append("bean");
        queryHandler.condition("bean." + primaryKeyName).append("= :id").setParameter("id", id);
        return getEntity(queryHandler);
    }

    /**
     * 获取实体集合
     * 
     * @param ids
     * @return entity list
     */
    public List<E> getEntitys(Serializable[] ids) {
        return getEntitys(ids, "id");
    }

    /**
     * 获取实体集合
     * 
     * @param ids
     * @param pk
     * @return entity list
     */
    @SuppressWarnings("unchecked")
    public List<E> getEntitys(Serializable[] ids, String pk) {
        if (CommonUtils.notEmpty(ids)) {
            QueryHandler queryHandler = getQueryHandler("from").append(getEntityClass().getSimpleName()).append("bean");
            queryHandler.condition("bean." + pk).append("in (:ids)").setParameter("ids", ids);
            return (List<E>) getList(queryHandler);
        }
        return Collections.emptyList();
    }

    /**
     * 保存
     * 
     * @param entity
     * @return id
     */
    public Serializable save(E entity) {
        return getSession().save(init(entity));
    }

    /**
     * 删除
     * 
     * Delete
     * 
     * @param id
     */
    public void delete(Serializable id) {
        E entity = getEntity(id);
        if (null != entity) {
            getSession().delete(entity);
        }
    }

    /**
     * 获取实体
     * 
     * @param queryHandler
     * @return entity
     */
    @SuppressWarnings("unchecked")
    protected E getEntity(QueryHandler queryHandler) {
        try {
            return (E) getQuery(queryHandler).uniqueResult();
        } catch (Exception e) {
            return (E) getQuery(queryHandler).list().get(0);
        }
    }

    /**
     * 更新
     * 
     * @param query
     * @return number of data affected
     */
    protected int update(QueryHandler queryHandler) {
        return getQuery(queryHandler).executeUpdate();
    }

    /**
     * 刪除
     * 
     * @param query
     * @return number of data deleted
     */
    protected int delete(QueryHandler queryHandler) {
        return update(queryHandler);
    }

    /**
     * 获取列表
     * 
     * @param query
     * @return results list
     */
    protected List<?> getList(QueryHandler queryHandler) {
        try {
            return getQuery(queryHandler).list();
        } catch (ObjectNotFoundException e) {
            return getQuery(queryHandler).setCacheable(false).list();
        }
    }

    /**
     * @param queryHandler
     * @param countHql
     * @param pageIndex
     * @param pageSize
     * @param maxResults
     * @return results page
     */
    protected PageHandler getPage(QueryHandler queryHandler, String countHql, Integer pageIndex, Integer pageSize,
            Integer maxResults) {
        PageHandler page;
        if (CommonUtils.notEmpty(pageSize)) {
            page = new PageHandler(pageIndex, pageSize, countResult(queryHandler, countHql), maxResults);
            if (0 != pageSize) {
                queryHandler.setFirstResult(page.getFirstResult()).setMaxResults(page.getPageSize());
                page.setList(getList(queryHandler));
            }
        } else {
            page = new PageHandler(pageIndex, pageSize, 0, maxResults);
            queryHandler.setMaxResults(maxResults);
            page.setList(getList(queryHandler));
        }
        return page;
    }

    /**
     * @param queryHandler
     * @param pageIndex
     * @param pageSize
     * @param maxResults
     * @return results page
     */
    protected PageHandler getPage(QueryHandler queryHandler, Integer pageIndex, Integer pageSize, Integer maxResults) {
        return getPage(queryHandler, null, pageIndex, pageSize, maxResults);
    }

    /**
     * @param queryHandler
     * @param pageIndex
     * @param pageSize
     * @return page
     */
    protected PageHandler getPage(QueryHandler queryHandler, Integer pageIndex, Integer pageSize) {
        return getPage(queryHandler, pageIndex, pageSize, Integer.MAX_VALUE);
    }

    /**
     * @param query
     * @param countHql
     * @return number of results
     */
    protected long countResult(QueryHandler queryHandler, String countHql) {
        if (CommonUtils.empty(countHql)) {
            countHql = queryHandler.getCountSql();
        }
        List<?> list = queryHandler.getQuery(getSession(), countHql).list();
        if (list.isEmpty()) {
            return 0;
        } else {
            Number result = (Number) list.iterator().next();
            if (null == result) {
                return 0;
            } else {
                return result.longValue();
            }
        }
    }

    /**
     * @param query
     * @return number of data
     */
    protected long count(QueryHandler queryHandler) {
        List<?> list = getQuery(queryHandler).list();
        if (list.isEmpty()) {
            return 0;
        } else {
            Number result = (Number) list.iterator().next();
            if (null == result) {
                return 0;
            } else {
                return result.longValue();
            }
        }
    }

    private Query<?> getQuery(QueryHandler queryHandler) {
        return queryHandler.getQuery(getSession());
    }

    protected NativeQuery<?> getSqlQuery(String sql) {
        return getSession().createSQLQuery(sql);
    }

    /**
     * @param entity
     */
    protected void index(E entity) {
        getFullTextSession().index(entity);
    }

    /**
     * @return future
     */
    public Future<?> reCreateIndex() {
        FullTextSession fullTextSession = getFullTextSession();
        return fullTextSession.createIndexer().start();
    }

    /**
     * @param fields
     * @param text
     * @return full text query
     */
    protected QueryBuilder getFullTextQueryBuilder() {
        FullTextSession fullTextSession = getFullTextSession();
        return fullTextSession.getSearchFactory().buildQueryBuilder().forEntity(getEntityClass()).get();
    }

    /**
     * @param fields
     * @param facetFields
     * @param text
     * @param facetCount
     * @return full text query
     */
    protected FullTextQuery getFullTextQuery(org.apache.lucene.search.Query query) {
        FullTextSession fullTextSession = getFullTextSession();
        return fullTextSession.createFullTextQuery(query, getEntityClass());
    }

    /**
     * @param fullTextQuery
     * @param pageIndex
     * @param pageSize
     * @return results page
     */
    protected PageHandler getPage(FullTextQuery fullTextQuery, Integer pageIndex, Integer pageSize) {
        return getPage(fullTextQuery, pageIndex, pageSize, null);
    }

    /**
     * @param fullTextQuery
     * @param pageIndex
     * @param pageSize
     * @param maxResults
     * @return results page
     */
    protected PageHandler getPage(FullTextQuery fullTextQuery, Integer pageIndex, Integer pageSize, Integer maxResults) {
        PageHandler page = new PageHandler(pageIndex, pageSize, fullTextQuery.getResultSize(), maxResults);
        if (CommonUtils.notEmpty(pageSize)) {
            fullTextQuery.setFirstResult(page.getFirstResult()).setMaxResults(page.getPageSize());
        }
        page.setList(fullTextQuery.getResultList());
        return page;
    }

    /**
     * @param fullTextQuery
     * @param facetFields
     * @param valueMap
     * @param pageIndex
     * @param pageSize
     * @return facet results page
     */
    protected FacetPageHandler getFacetPage(QueryBuilder queryBuilder, FullTextQuery fullTextQuery, String[] facetFields,
            Map<String, List<String>> valueMap, int facetCount, Integer pageIndex, Integer pageSize) {
        return getFacetPage(queryBuilder, fullTextQuery, facetFields, valueMap, facetCount, pageIndex, pageSize, 100);
    }

    /**
     * @param fullTextQuery
     * @param facetFields
     * @param valueMap
     * @param pageIndex
     * @param pageSize
     * @param maxResults
     * @return facet results page
     */
    protected FacetPageHandler getFacetPage(QueryBuilder queryBuilder, FullTextQuery fullTextQuery, String[] facetFields,
            Map<String, List<String>> valueMap, int facetCount, Integer pageIndex, Integer pageSize, Integer maxResults) {
        FacetManager facetManager = fullTextQuery.getFacetManager();
        for (String facetField : facetFields) {
            FacetingRequest facetingRequest = queryBuilder.facet().name(facetField + FACET_NAME_SUFFIX).onField(facetField)
                    .discrete().orderedBy(FacetSortOrder.COUNT_DESC).includeZeroCounts(false).maxFacetCount(facetCount)
                    .createFacetingRequest();
            facetManager.enableFaceting(facetingRequest);
        }
        FacetPageHandler page = new FacetPageHandler(pageIndex, pageSize, fullTextQuery.getResultSize(), maxResults);
        if (0 < page.getTotalCount()) {
            Set<String> facetSet = new LinkedHashSet<>();
            facetSet.addAll(valueMap.keySet());
            facetSet.addAll(Arrays.asList(facetFields));
            for (String facetField : facetSet) {
                String facetingName = facetField + FACET_NAME_SUFFIX;
                List<Facet> facets = facetManager.getFacets(facetingName);
                Map<String, Integer> facetMap = new LinkedHashMap<>();
                List<String> valueList = valueMap.get(facetField);
                if (null != valueList) {
                    List<Facet> facetList = new ArrayList<>();
                    for (Facet facet : facets) {
                        facetMap.put(facet.getValue(), facet.getCount());
                        if (valueList.contains(facet.getValue())) {
                            facetList.add(facet);
                        }
                    }
                    if (!facetList.isEmpty()) {
                        facetManager.getFacetGroup(facetingName).selectFacets(facetList.toArray(new Facet[facetList.size()]));
                    }
                } else {
                    for (Facet facet : facets) {
                        facetMap.put(facet.getValue(), facet.getCount());
                    }
                }
                page.getFacetMap().put(facetField, facetMap);
            }
            page.setTotalCount(fullTextQuery.getResultSize(), maxResults);
            page.init();
        }
        if (CommonUtils.notEmpty(pageSize)) {
            fullTextQuery.setFirstResult(page.getFirstResult()).setMaxResults(page.getPageSize());
        }
        page.setList(fullTextQuery.getResultList());
        return page;
    }

    protected Session getSession() {
        return sessionFactory.getCurrentSession();
    }

    protected FullTextSession getFullTextSession() {
        return Search.getFullTextSession(getSession());
    }

    @SuppressWarnings("unchecked")
    private Class<E> getEntityClass() {
        return null == clazz
                ? this.clazz = (Class<E>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0]
                : clazz;
    }

    protected abstract E init(E entity);

    @Autowired
    protected SessionFactory sessionFactory;
}

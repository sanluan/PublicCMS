package com.sanluan.common.base;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.search.FullTextQuery;
import org.hibernate.search.FullTextSession;
import org.hibernate.search.Search;
import org.hibernate.search.query.dsl.QueryBuilder;
import org.hibernate.search.query.engine.spi.FacetManager;
import org.hibernate.search.query.facet.Facet;
import org.hibernate.search.query.facet.FacetSortOrder;
import org.hibernate.search.query.facet.FacetingRequest;
import org.springframework.beans.factory.annotation.Autowired;

import com.sanluan.common.handler.FacetPageHandler;
import com.sanluan.common.handler.PageHandler;
import com.sanluan.common.handler.QueryHandler;

public abstract class BaseDao<E> extends Base {
    public QueryHandler getQueryHandler(String sql) {
        return new QueryHandler(sql);
    }

    public QueryHandler getDeleteQueryHandler(String sql) {
        return getQueryHandler("delete").append(sql);
    }

    public QueryHandler getCountQueryHandler(String sql) {
        return getQueryHandler("select count(*)").append(sql);
    }

    public QueryHandler getQueryHandler() {
        return new QueryHandler();
    }

    public String like(String var) {
        return "%" + var + "%";
    }

    public String likeEnd(String var) {
        return "%" + var;
    }

    public String likeStart(String var) {
        return var + "%";
    }

    /**
     * @param id
     * @return
     */
    public E getEntity(Serializable id) {
        if (notEmpty(id)) {
            return (E) getSession().get(getEntityClass(), id);
        } else {
            return null;
        }
    }

    /**
     * @param id
     * @return
     */
    public E getEntity(Serializable id, String pk) {
        QueryHandler queryHandler = getQueryHandler("from").append(getEntityClass().getSimpleName()).append("bean");
        queryHandler.condition("bean." + pk).append("= :id").setParameter("id", id);
        return getEntity(queryHandler);
    }

    /**
     * @param ids
     * @return
     */
    public List<E> getEntitys(Serializable[] ids) {
        return getEntitys(ids, "id");
    }

    /**
     * @param ids
     * @param pk
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<E> getEntitys(Serializable[] ids, String pk) {
        if (notEmpty(ids)) {
            QueryHandler queryHandler = getQueryHandler("from").append(getEntityClass().getSimpleName()).append("bean");
            queryHandler.condition("bean." + pk).append("in (:ids)").setParameter("ids", ids);
            return (List<E>) getList(queryHandler);
        }
        return new ArrayList<E>();
    }

    protected abstract E init(E entity);

    /**
     * @param entity
     * @return
     */
    public E save(E entity) {
        getSession().save(init(entity));
        return entity;
    }

    /**
     * @param id
     * @return
     */
    public E delete(Serializable id) {
        E entity = getEntity(id);
        if (notEmpty(entity)) {
            getSession().delete(entity);
        }
        return entity;
    }

    /**
     * @param queryHandler
     * @return
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
     * @param query
     * @return
     */
    protected int update(QueryHandler queryHandler) {
        return getQuery(queryHandler).executeUpdate();
    }

    /**
     * @param query
     * @return
     */
    protected int delete(QueryHandler queryHandler) {
        return update(queryHandler);
    }

    /**
     * @param query
     * @return
     */
    protected List<?> getList(QueryHandler queryHandler) {
        return getQuery(queryHandler).list();
    }

    /**
     * @param entityList
     */
    protected void index(List<E> entityList) {
        if (notEmpty(entityList)) {
            FullTextSession fullTextSession = getFullTextSession();
            for (E entity : entityList) {
                fullTextSession.index(entity);
            }
        }
    }

    /**
     * @return
     */
    public Future<?> reCreateIndex() {
        FullTextSession fullTextSession = getFullTextSession();
        return fullTextSession.createIndexer().start();
    }

    /**
     * @param fields
     * @param facetFields
     * @param text
     * @return
     */
    protected FullTextQuery getQuery(String[] fields, String[] facetFields, String text) {
        FullTextSession fullTextSession = getFullTextSession();
        QueryBuilder queryBuilder = fullTextSession.getSearchFactory().buildQueryBuilder().forEntity(getEntityClass()).get();
        org.apache.lucene.search.Query query = queryBuilder.keyword().onFields(fields).matching(text).createQuery();
        FullTextQuery fullTextQuery = fullTextSession.createFullTextQuery(query, getEntityClass());
        FacetManager facetManager = fullTextQuery.getFacetManager();
        if (null != facetFields) {
            for (String facetField : facetFields) {
                FacetingRequest facetingRequest = queryBuilder.facet().name(facetField + "FacetRequest").onField(facetField)
                        .discrete().orderedBy(FacetSortOrder.COUNT_DESC).includeZeroCounts(false).maxFacetCount(10)
                        .createFacetingRequest();
                facetManager.enableFaceting(facetingRequest);
            }
        }
        return fullTextQuery;
    }

    /**
     * @param fullTextQuery
     * @param pageIndex
     * @param pageSize
     * @param maxResults
     * @return
     */
    protected PageHandler getPage(FullTextQuery fullTextQuery, Integer pageIndex, Integer pageSize, Integer maxResults) {
        PageHandler page = new PageHandler(pageIndex, pageSize, fullTextQuery.getResultSize(), maxResults);
        if (notEmpty(pageSize)) {
            fullTextQuery.setFirstResult(page.getFirstResult()).setMaxResults(page.getPageSize());
        }
        page.setList(fullTextQuery.list());
        return page;
    }

    /**
     * @param fullTextQuery
     * @param pageIndex
     * @param pageSize
     * @return
     */
    protected PageHandler getPage(FullTextQuery fullTextQuery, Integer pageIndex, Integer pageSize) {
        return getPage(fullTextQuery, pageIndex, pageSize, null);
    }

    /**
     * @param fullTextQuery
     * @param facetFields
     * @param valueMap
     * @param pageIndex
     * @param pageSize
     * @param maxResults
     * @return
     */
    protected FacetPageHandler getFacetPage(FullTextQuery fullTextQuery, String[] facetFields, Map<String, String> valueMap,
            Integer pageIndex, Integer pageSize, Integer maxResults) {
        FacetPageHandler page = new FacetPageHandler(pageIndex, pageSize, fullTextQuery.getResultSize(), maxResults);
        if (notEmpty(pageSize)) {
            fullTextQuery.setFirstResult(page.getFirstResult()).setMaxResults(page.getPageSize());
        }
        if (0 < page.getTotalCount()) {
            page.setList(fullTextQuery.list());
            if (null != facetFields) {
                for (String facetField : facetFields) {
                    FacetManager facetManager = fullTextQuery.getFacetManager();
                    List<Facet> facets = facetManager.getFacets(facetField + "FacetRequest");
                    Map<String, Integer> facetMap = new LinkedHashMap<String, Integer>();
                    for (Facet facet : facets) {
                        facetMap.put(facet.getValue(), facet.getCount());
                        page.getMap().put(facetField, facetMap);
                        if (facet.getValue().equalsIgnoreCase(valueMap.get(facetField))) {
                            facetManager.getFacetGroup(facetField + "FacetRequest").selectFacets(facet);
                            page = new FacetPageHandler(pageIndex, pageSize, fullTextQuery.getResultSize(), maxResults);
                            if (notEmpty(pageSize)) {
                                fullTextQuery.setFirstResult(page.getFirstResult()).setMaxResults(page.getPageSize());
                            }
                            page.setList(fullTextQuery.list());
                        }
                    }
                }
            }
        }
        return page;
    }

    /**
     * @param fullTextQuery
     * @param facetFields
     * @param valueMap
     * @param pageIndex
     * @param pageSize
     * @return
     */
    protected FacetPageHandler getFacetPage(FullTextQuery fullTextQuery, String[] facetFields, Map<String, String> valueMap,
            Integer pageIndex, Integer pageSize) {
        return getFacetPage(fullTextQuery, facetFields, valueMap, pageIndex, pageSize, null);
    }

    /**
     * @param queryHandler
     * @param pageIndex
     * @param pageSize
     * @param maxResults
     * @return
     */
    protected PageHandler getPage(QueryHandler queryHandler, Integer pageIndex, Integer pageSize, Integer maxResults) {
        PageHandler page;
        if (notEmpty(pageSize)) {
            int totalCount = countResult(queryHandler);
            page = new PageHandler(pageIndex, pageSize, totalCount, maxResults);
            queryHandler.setFirstResult(page.getFirstResult()).setMaxResults(page.getPageSize());
        } else {
            page = new PageHandler(pageIndex, pageSize, 0, maxResults);
        }
        queryHandler.setCacheable(true);
        page.setList(getList(queryHandler));
        return page;
    }

    /**
     * @param queryHandler
     * @param pageIndex
     * @param pageSize
     * @return
     */
    protected PageHandler getPage(QueryHandler queryHandler, Integer pageIndex, Integer pageSize) {
        return getPage(queryHandler, pageIndex, pageSize, null);
    }

    /**
     * @param query
     * @return
     */
    protected int countResult(QueryHandler queryHandler) {
        return ((Number) getCountQuery(queryHandler).iterate().next()).intValue();
    }

    /**
     * @param query
     * @return
     */
    protected int count(QueryHandler queryHandler) {
        return ((Number) getQuery(queryHandler).iterate().next()).intValue();
    }

    private Query getQuery(QueryHandler queryHandler) {
        return queryHandler.getQuery(getSession());
    }

    private Query getCountQuery(QueryHandler queryHandler) {
        return queryHandler.getCountQuery(getSession());
    }

    @Autowired
    protected SessionFactory sessionFactory;

    protected Session getSession() {
        return sessionFactory.getCurrentSession();
    }

    protected FullTextSession getFullTextSession() {
        return Search.getFullTextSession(getSession());
    }

    protected abstract Class<E> getEntityClass();
}

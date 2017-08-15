package com.publiccms.common.base;

import static com.publiccms.common.tools.CommonUtils.notEmpty;
import static org.apache.commons.lang3.time.DateUtils.addDays;
import static org.apache.commons.logging.LogFactory.getLog;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;

import org.apache.commons.logging.Log;
import org.hibernate.ObjectNotFoundException;
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

import com.publiccms.common.handler.FacetPageHandler;
import com.publiccms.common.handler.PageHandler;
import com.publiccms.common.handler.QueryHandler;

/**
 * DAO基类
 * 
 * Base DAO
 * 
 * @param <E>
 * 
 */
public abstract class BaseDao<E> implements Base  {
    protected final Log log = getLog(getClass());
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
     * 明天
     * 
     * Tomorrow
     * 
     * @param date
     * @return
     */
    public static Date tomorrow(Date date) {
        return addDays(date, 1);
    }

    /**
     * 获取查询处理器
     * 
     * Get queryhandler
     * 
     * @param sql
     * @return
     */
    public static QueryHandler getQueryHandler(String sql) {
        return new QueryHandler(sql);
    }

    /**
     * 获取查询处理器
     * 
     * Get queryhandler
     * 
     * @return
     */
    public static QueryHandler getQueryHandler() {
        return new QueryHandler();
    }

    /**
     * 获取删除查询处理器
     * 
     * Get delete queryhandler
     * 
     * @param sql
     * @return
     */
    public static QueryHandler getDeleteQueryHandler(String sql) {
        return getQueryHandler("delete").append(sql);
    }

    /**
     * 获取统计查询处理器
     * 
     * Get count queryhandler
     * 
     * @param sql
     * @return
     */
    public static QueryHandler getCountQueryHandler(String sql) {
        return getQueryHandler("select count(*)").append(sql);
    }

    /**
     * Like查询
     * 
     * like Search
     * 
     * @param var
     * @return
     */
    public static String like(String var) {
        return "%" + var + "%";
    }

    /**
     * 左Like查询
     * 
     * Left like search
     * 
     * @param var
     * @return
     */
    public static String leftLike(String var) {
        return "%" + var;
    }

    /**
     * 右Like查询
     * 
     * Right like search
     * 
     * @param var
     * @return
     */
    public static String rightLike(String var) {
        return var + "%";
    }

    /**
     * 获取实体
     * 
     * Get entity
     * 
     * @param id
     * @return
     */
    public E getEntity(Serializable id) {
        return null != id ? (E) getSession().get(getEntityClass(), id) : null;
    }

    /**
     * 获取实体
     * 
     * Get entity
     * 
     * @param id
     * @param primaryKeyName
     * @return
     */
    public E getEntity(Serializable id, String primaryKeyName) {
        QueryHandler queryHandler = getQueryHandler("from").append(getEntityClass().getSimpleName()).append("bean");
        queryHandler.condition("bean." + primaryKeyName).append("= :id").setParameter("id", id);
        return getEntity(queryHandler);
    }

    /**
     * 获取实体集合
     * 
     * Get entity list
     * 
     * @param ids
     * @return
     */
    public List<E> getEntitys(Serializable[] ids) {
        return getEntitys(ids, "id");
    }

    /**
     * 获取实体集合
     * 
     * Get entity list
     * 
     * @param ids
     * @param pk
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<E> getEntitys(Serializable[] ids, String pk) {
        if (notEmpty(ids)) {
            QueryHandler queryHandler = getQueryHandler("from").append(getEntityClass().getSimpleName()).append("bean");
            queryHandler.condition("bean." + pk).append("in (:ids)").setParameter("ids", ids);
            queryHandler.setCacheable(false);
            return (List<E>) getList(queryHandler);
        }
        return new ArrayList<>();
    }

    /**
     * 保存
     * 
     * Save
     * 
     * @param entity
     * @return
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
     * Get entity
     * 
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
     * 更新
     * 
     * Update
     * 
     * @param query
     * @return
     */
    protected int update(QueryHandler queryHandler) {
        return getQuery(queryHandler).executeUpdate();
    }

    /**
     * 刪除
     * 
     * Delete
     * 
     * @param query
     * @return
     */
    protected int delete(QueryHandler queryHandler) {
        return update(queryHandler);
    }

    /**
     * 获取列表
     * 
     * Get list
     * 
     * @param query
     * @return
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
        return ((Number) getCountQuery(queryHandler).list().iterator().next()).intValue();
    }

    /**
     * @param query
     * @return
     */
    protected int count(QueryHandler queryHandler) {
        return ((Number) getQuery(queryHandler).list().iterator().next()).intValue();
    }

    private Query getQuery(QueryHandler queryHandler) {
        return queryHandler.getQuery(getSession());
    }

    private Query getCountQuery(QueryHandler queryHandler) {
        return queryHandler.getCountQuery(getSession());
    }

    /**
     * @param entityList
     */
    protected void index(E entity) {
        getFullTextSession().index(entity);
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
     * @param text
     * @return
     */
    protected FullTextQuery getQuery(String[] fields, String text) {
        return getFacetQuery(fields, null, text, 0);
    }

    /**
     * @param fields
     * @param facetFields
     * @param text
     * @param facetCount
     * @return
     */
    protected FullTextQuery getFacetQuery(String[] fields, String[] facetFields, String text, int facetCount) {
        FullTextSession fullTextSession = getFullTextSession();
        QueryBuilder queryBuilder = fullTextSession.getSearchFactory().buildQueryBuilder().forEntity(getEntityClass()).get();
        org.apache.lucene.search.Query query = queryBuilder.keyword().onFields(fields).matching(text).createQuery();
        FullTextQuery fullTextQuery = fullTextSession.createFullTextQuery(query, getEntityClass());
        if (notEmpty(facetFields)) {
            FacetManager facetManager = fullTextQuery.getFacetManager();
            for (String facetField : facetFields) {
                FacetingRequest facetingRequest = queryBuilder.facet().name(facetField + FACET_NAME_SUFFIX).onField(facetField)
                        .discrete().orderedBy(FacetSortOrder.COUNT_DESC).includeZeroCounts(false).maxFacetCount(facetCount)
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
     * @return
     */
    protected PageHandler getPage(FullTextQuery fullTextQuery, Integer pageIndex, Integer pageSize) {
        return getPage(fullTextQuery, pageIndex, pageSize, null);
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
     * @param facetFields
     * @param valueMap
     * @param pageIndex
     * @param pageSize
     * @return
     */
    protected FacetPageHandler getFacetPage(FullTextQuery fullTextQuery, String[] facetFields, Map<String, List<String>> valueMap,
            Integer pageIndex, Integer pageSize) {
        return getFacetPage(fullTextQuery, facetFields, valueMap, pageIndex, pageSize, null);
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
    protected FacetPageHandler getFacetPage(FullTextQuery fullTextQuery, String[] facetFields, Map<String, List<String>> valueMap,
            Integer pageIndex, Integer pageSize, Integer maxResults) {
        FacetPageHandler page = new FacetPageHandler(pageIndex, pageSize, fullTextQuery.getResultSize(), maxResults);
        if (notEmpty(pageSize)) {
            fullTextQuery.setFirstResult(page.getFirstResult()).setMaxResults(page.getPageSize());
        }
        if (0 < page.getTotalCount() && notEmpty(facetFields) && notEmpty(valueMap)) {
            FacetManager facetManager = fullTextQuery.getFacetManager();
            for (String facetField : facetFields) {
                List<Facet> facets = facetManager.getFacets(facetField + FACET_NAME_SUFFIX);
                Map<String, Integer> facetMap = new LinkedHashMap<>();
                if (notEmpty(valueMap.get(facetField))) {
                    List<Facet> facetList = new ArrayList<>();
                    for (Facet facet : facets) {
                        facetMap.put(facet.getValue(), facet.getCount());
                        if (valueMap.get(facetField).contains(facet.getValue())) {
                            facetList.add(facet);
                        }
                    }
                    if (0 < facetList.size()) {
                        facetManager.getFacetGroup(facetField + FACET_NAME_SUFFIX)
                                .selectFacets(facetList.toArray(new Facet[] {}));
                    } else {
                        page.setFacetResult(false);
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
            if (notEmpty(pageSize)) {
                fullTextQuery.setFirstResult(page.getFirstResult()).setMaxResults(page.getPageSize());
            }
        }
        page.setList(fullTextQuery.list());
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
                ? this.clazz = (Class<E>) ((ParameterizedType) this.getClass().getGenericSuperclass()).getActualTypeArguments()[0]
                : clazz;
    }

    protected abstract E init(E entity);

    @Autowired
    protected SessionFactory sessionFactory;
}

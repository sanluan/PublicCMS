package com.publiccms.common.base;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.lucene.search.highlight.Highlighter;
import org.apache.lucene.search.highlight.QueryScorer;
import org.apache.lucene.search.highlight.SimpleHTMLFormatter;
import org.hibernate.ObjectNotFoundException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.hibernate.search.FullTextSession;
import org.hibernate.search.Search;
import org.hibernate.search.query.dsl.QueryBuilder;
import org.hibernate.search.query.engine.spi.FacetManager;
import org.hibernate.search.query.facet.Facet;
import org.hibernate.search.query.facet.FacetSortOrder;
import org.hibernate.search.query.facet.FacetingRequest;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.util.HtmlUtils;

import com.publiccms.common.constants.Constants;
import com.publiccms.common.handler.CmsFullTextQuery;
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
        return null == id ? null : getSession().get(getEntityClass(), id);
    }

    /**
     * 获取实体
     * 
     * @param id
     * @param primaryKeyName
     * @return entity
     */
    public E getEntity(Serializable id, String primaryKeyName) {
        if (null == id) {
            return null;
        } else {
            QueryHandler queryHandler = getQueryHandler("from").append(getEntityClass().getSimpleName()).append("bean");
            queryHandler.condition("bean." + primaryKeyName).append("= :id").setParameter("id", id);
            return getEntity(queryHandler);
        }
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
    public List<E> getEntitys(Serializable[] ids, String pk) {
        if (CommonUtils.notEmpty(ids)) {
            QueryHandler queryHandler = getQueryHandler("from").append(getEntityClass().getSimpleName()).append("bean");
            queryHandler.condition("bean." + pk).append("in (:ids)").setParameter("ids", ids);
            Query<E> query = getSession().createQuery(queryHandler.getSql(), getEntityClass());
            return getList(query, queryHandler);
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
     * @param entity
     * 
     */
    public void delete(E entity) {
        if (null != entity) {
            getSession().delete(entity);
        }
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
    protected E getEntity(QueryHandler queryHandler) {
        Query<E> query = getSession().createQuery(queryHandler.getSql(), getEntityClass());
        queryHandler.initQuery(query);
        try {
            return query.uniqueResult();
        } catch (Exception e) {
            return query.list().get(0);
        }
    }

    /**
     * 更新
     * 
     * @param query
     * @return number of data affected
     */
    protected int update(QueryHandler queryHandler) {
        Query<?> query = getSession().createQuery(queryHandler.getSql());
        queryHandler.initQuery(query);
        return query.executeUpdate();
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
    protected <T> List<T> getList(Query<T> query, QueryHandler queryHandler) {
        queryHandler.initQuery(query);
        try {
            return query.list();
        } catch (ObjectNotFoundException e) {
            return query.setCacheable(false).list();
        }
    }

    /**
     * 获取列表
     * 
     * @param query
     * @return results list
     */
    protected List<E> getEntityList(QueryHandler queryHandler) {
        Query<E> query = getSession().createQuery(queryHandler.getSql(), getEntityClass());
        return getList(query, queryHandler);
    }

    /**
     * 获取列表
     * 
     * @param query
     * @return results list
     */
    protected List<?> getList(QueryHandler queryHandler) {
        Query<?> query = getSession().createQuery(queryHandler.getSql());
        return getList(query, queryHandler);
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
            queryHandler.setMaxResults(maxResults);
            List<?> list = getList(queryHandler);
            page = new PageHandler(pageIndex, pageSize, list.size(), maxResults);
            page.setList(list);
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
        Query<?> query = getSession().createQuery(countHql);
        List<?> list = queryHandler.initQuery(query).list();
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
        Query<?> query = getSession().createQuery(queryHandler.getSql());
        List<?> list = queryHandler.initQuery(query).list();
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
        return getFullTextSession().getSearchFactory().buildQueryBuilder().forEntity(getEntityClass()).get();
    }

    /**
     * @param fields
     * @param facetFields
     * @param text
     * @param facetCount
     * @return full text query
     */
    protected CmsFullTextQuery getCmsFullTextQuery(org.apache.lucene.search.Query query) {
        return new CmsFullTextQuery(getFullTextSession().createFullTextQuery(query, getEntityClass()), query);
    }

    /**
     * @param fullTextQuery
     * @param highlight
     * @param highLighterFieldNames
     * @param preTag
     * @param postTag
     * @param pageIndex
     * @param pageSize
     * @return results page
     */
    protected PageHandler getPage(CmsFullTextQuery fullTextQuery, boolean highlight, String[] highLighterFieldNames,
            String preTag, String postTag, Integer pageIndex, Integer pageSize) {
        return getPage(fullTextQuery, highlight, highLighterFieldNames, preTag, postTag, pageIndex, pageSize, Integer.MAX_VALUE);
    }

    /**
     * @param fullTextQuery
     * @param highlight
     * @param highLighterFieldNames
     * @param preTag
     * @param postTag
     * @param pageIndex
     * @param pageSize
     * @param maxResults
     * @return results page
     */
    protected PageHandler getPage(CmsFullTextQuery fullTextQuery, boolean highlight, String[] highLighterFieldNames,
            String preTag, String postTag, Integer pageIndex, Integer pageSize, Integer maxResults) {
        PageHandler page = new PageHandler(pageIndex, pageSize, fullTextQuery.getFullTextQuery().getResultSize(), maxResults);
        if (CommonUtils.notEmpty(pageSize)) {
            fullTextQuery.getFullTextQuery().setFirstResult(page.getFirstResult()).setMaxResults(page.getPageSize());
        }
        @SuppressWarnings("unchecked")
        List<E> resultList = fullTextQuery.getFullTextQuery().getResultList();
        if (highlight) {
            higtLighter(resultList, fullTextQuery, highLighterFieldNames, preTag, postTag);
        }
        page.setList(resultList);
        return page;
    }

    /**
     * @param fullTextQuery
     * @param facetFields
     * @param facetCount
     * @param highLighterFieldNames
     * @param preTag
     * @param postTag
     * @param pageIndex
     * @param pageSize
     * @return facet results page
     */
    protected FacetPageHandler getFacetPage(QueryBuilder queryBuilder, CmsFullTextQuery fullTextQuery, String[] facetFields,
            int facetCount, boolean highlight, String[] highLighterFieldNames, String preTag, String postTag, Integer pageIndex,
            Integer pageSize) {
        return getFacetPage(queryBuilder, fullTextQuery, facetFields, facetCount, highlight, highLighterFieldNames, preTag,
                postTag, pageIndex, pageSize, Integer.MAX_VALUE);
    }

    /**
     * @param fullTextQuery
     * @param facetFields
     * @param facetCount
     * @param highlight
     * @param highLighterFieldNames
     * @param preTag
     * @param postTag
     * @param pageIndex
     * @param pageSize
     * @param maxResults
     * @return facet results page
     */
    protected FacetPageHandler getFacetPage(QueryBuilder queryBuilder, CmsFullTextQuery fullTextQuery, String[] facetFields,
            int facetCount, boolean highlight, String[] highLighterFieldNames, String preTag, String postTag, Integer pageIndex,
            Integer pageSize, Integer maxResults) {
        FacetManager facetManager = fullTextQuery.getFullTextQuery().getFacetManager();
        Map<String, Map<String, Integer>> facetMap = new LinkedHashMap<>();
        for (String facetField : facetFields) {
            String facetFieldName = facetField + FACET_NAME_SUFFIX;
            FacetingRequest facetingRequest = queryBuilder.facet().name(facetFieldName).onField(facetField).discrete()
                    .orderedBy(FacetSortOrder.COUNT_DESC).includeZeroCounts(false).maxFacetCount(facetCount)
                    .createFacetingRequest();
            List<Facet> facets = facetManager.enableFaceting(facetingRequest).getFacets(facetFieldName);
            Map<String, Integer> valueMap = facets.stream().collect(Collectors.toMap(facet -> facet.getValue(),
                    facet -> facet.getCount(), Constants.defaultMegerFunction(), LinkedHashMap::new));
            facetMap.put(facetField, valueMap);
        }
        FacetPageHandler page = new FacetPageHandler(pageIndex, pageSize, fullTextQuery.getFullTextQuery().getResultSize(),
                maxResults);
        if (CommonUtils.notEmpty(pageSize)) {
            fullTextQuery.getFullTextQuery().setFirstResult(page.getFirstResult()).setMaxResults(page.getPageSize());
        }
        @SuppressWarnings("unchecked")
        List<E> resultList = fullTextQuery.getFullTextQuery().getResultList();
        if (highlight) {
            higtLighter(resultList, fullTextQuery, highLighterFieldNames, preTag, postTag);
        }
        page.setList(resultList);
        page.setFacetMap(facetMap);
        return page;
    }

    /**
     * @param resultList
     * @param fullTextQuery
     * @param highLighterFieldNames
     * @param preTag
     * @param postTag
     */
    protected void higtLighter(List<E> resultList, CmsFullTextQuery fullTextQuery, String[] highLighterFieldNames, String preTag,
            String postTag) {
        try {
            if (CommonUtils.notEmpty(highLighterFieldNames)) {
                SimpleHTMLFormatter formatter;
                if (CommonUtils.notEmpty(preTag) && CommonUtils.notEmpty(postTag)) {
                    formatter = new SimpleHTMLFormatter(preTag, postTag);
                } else {
                    formatter = new SimpleHTMLFormatter();
                }
                QueryScorer queryScorer = new QueryScorer(fullTextQuery.getLuceneQuery());
                Highlighter highlighter = new Highlighter(formatter, queryScorer);
                for (E e : resultList) {
                    for (String fieldName : highLighterFieldNames) {
                        Object fieldValue = ReflectionUtils
                                .invokeMethod(BeanUtils.getPropertyDescriptor(getEntityClass(), fieldName).getReadMethod(), e);
                        String hightLightFieldValue = null;
                        if (fieldValue instanceof String && CommonUtils.notEmpty(String.valueOf(fieldValue))) {
                            String safeValue = HtmlUtils.htmlEscape(String.valueOf(fieldValue), Constants.DEFAULT_CHARSET_NAME);
                            hightLightFieldValue = highlighter.getBestFragment(
                                    getFullTextSession().getSearchFactory().getAnalyzer("cms"), fieldName, safeValue);
                            if (CommonUtils.notEmpty(hightLightFieldValue)) {
                                ReflectionUtils.invokeMethod(
                                        BeanUtils.getPropertyDescriptor(getEntityClass(), fieldName).getWriteMethod(), e,
                                        hightLightFieldValue);
                            } else {
                                ReflectionUtils.invokeMethod(
                                        BeanUtils.getPropertyDescriptor(getEntityClass(), fieldName).getWriteMethod(), e,
                                        safeValue);
                            }
                        }
                    }
                }
            } else {
                for (E e : resultList) {
                    for (String fieldName : highLighterFieldNames) {
                        Object fieldValue = ReflectionUtils
                                .invokeMethod(BeanUtils.getPropertyDescriptor(getEntityClass(), fieldName).getReadMethod(), e);
                        if (fieldValue instanceof String && CommonUtils.notEmpty(String.valueOf(fieldValue))) {
                            String safeValue = HtmlUtils.htmlEscape(String.valueOf(fieldValue), Constants.DEFAULT_CHARSET_NAME);
                            ReflectionUtils.invokeMethod(
                                    BeanUtils.getPropertyDescriptor(getEntityClass(), fieldName).getWriteMethod(), e, safeValue);
                        }
                    }
                }

            }
        } catch (Exception ignore) {
        }
    }

    /**
     * @return session
     */
    protected Session getSession() {
        return sessionFactory.getCurrentSession();
    }

    /**
     * @return fulltext session
     */
    protected FullTextSession getFullTextSession() {
        return Search.getFullTextSession(sessionFactory.getCurrentSession());
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

package com.publiccms.logic.dao.cms;

import java.io.Serializable;

// Generated 2015-5-8 16:50:23 by com.publiccms.common.source.SourceGenerator

import java.util.Date;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.Sort;
import org.apache.lucene.search.SortField;
import org.apache.lucene.search.TermQuery;
import org.hibernate.search.query.dsl.BooleanJunction;
import org.hibernate.search.query.dsl.MustJunction;
import org.hibernate.search.query.dsl.QueryBuilder;
import org.hibernate.search.query.dsl.TermContext;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import com.publiccms.common.base.BaseDao;
import com.publiccms.common.constants.CommonConstants;
import com.publiccms.common.handler.CmsFullTextQuery;
import com.publiccms.common.handler.FacetPageHandler;
import com.publiccms.common.handler.PageHandler;
import com.publiccms.common.handler.QueryHandler;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.entities.cms.CmsContent;
import com.publiccms.views.pojo.query.CmsContentQuery;

/**
 *
 * CmsContentDao
 * 
 */
@Repository
public class CmsContentDao extends BaseDao<CmsContent> {
    private static final String[] textFields = new String[] { "title", "author", "editor", "description" };
    private static final String[] tagFields = new String[] { "tagIds" };
    private static final String dictionaryField = "dictionaryValues";
    private static final String[] facetFields = new String[] { "categoryId", "modelId" };
    private static final String[] projectionFields = new String[] { "title", "categoryId", "modelId", "parentId", "author",
            "editor", "onlyUrl", "hasImages", "hasFiles", "url", "description", "tagIds", "publishDate" };

    /**
     * @param projection
     * @param fuzzy
     * @param highlight 
     * @param siteId
     * @param text
     * @param tagIds
     * @param dictionaryValues
     * @param categoryIds
     * @param modelIds
     * @param fields
     * @param startPublishDate
     * @param endPublishDate
     * @param expiryDate
     * @param preTag
     * @param postTag
     * @param orderField
     * @param pageIndex
     * @param pageSize
     * @return results page
     */
    public PageHandler query(boolean projection, boolean fuzzy, boolean highlight, Short siteId, Integer[] categoryIds, String[] modelIds,
            String text, String[] fields, String tagIds, String[] dictionaryValues, String preTag, String postTag,
            Date startPublishDate, Date endPublishDate, Date expiryDate, String orderField, Integer pageIndex, Integer pageSize) {
        QueryBuilder queryBuilder = getFullTextQueryBuilder();
        CmsFullTextQuery query = getQuery(queryBuilder, projection, fuzzy, siteId, categoryIds, modelIds, text, fields, tagIds,
                dictionaryValues, startPublishDate, endPublishDate, expiryDate, orderField);
        return getPage(query, highlight, CommonUtils.notEmpty(text) ? textFields : null, preTag, postTag, pageIndex, pageSize);
    }

    /**
     * @param projection
     * @param fuzzy
     * @param highlight 
     * @param siteId
     * @param categoryIds
     * @param modelIds
     * @param text
     * @param fields
     * @param tagIds
     * @param dictionaryValues
     * @param startPublishDate
     * @param endPublishDate
     * @param expiryDate
     * @param preTag
     * @param postTag
     * @param orderField
     * @param pageIndex
     * @param pageSize
     * @return results page
     */
    public FacetPageHandler facetQuery(boolean projection, boolean fuzzy, boolean highlight, Short siteId, Integer[] categoryIds, String[] modelIds,
            String text, String[] fields, String tagIds, String[] dictionaryValues, String preTag, String postTag,
            Date startPublishDate, Date endPublishDate, Date expiryDate, String orderField, Integer pageIndex, Integer pageSize) {
        QueryBuilder queryBuilder = getFullTextQueryBuilder();
        CmsFullTextQuery query = getQuery(queryBuilder, projection, fuzzy, siteId, categoryIds, modelIds, text, fields, tagIds,
                dictionaryValues, startPublishDate, endPublishDate, expiryDate, orderField);
        return getFacetPage(queryBuilder, query, facetFields, 10, highlight,  CommonUtils.notEmpty(text) ? textFields : null, preTag, postTag,
                pageIndex, pageSize);
    }

    private CmsFullTextQuery getQuery(QueryBuilder queryBuilder, boolean projection, boolean fuzzy, Short siteId,
            Integer[] categoryIds, String[] modelIds, String text, String[] fields, String tagIds, String[] dictionaryValues,
            Date startPublishDate, Date endPublishDate, Date expiryDate, String orderField) {
        MustJunction termination = queryBuilder.bool().must(new TermQuery(new Term("siteId", siteId.toString())));
        if (CommonUtils.notEmpty(text)) {
            TermContext term = queryBuilder.keyword();
            if (!fuzzy) {
                term.fuzzy().withEditDistanceUpTo(0);
            }
            if (CommonUtils.notEmpty(fields)) {
                for (String field : fields) {
                    if (!ArrayUtils.contains(textFields, field)) {
                        fields = textFields;
                    }
                }
            } else {
                fields = textFields;
            }
            termination.must(term.onFields(textFields).matching(text).createQuery());
        }
        if (CommonUtils.notEmpty(tagIds)) {
            TermContext term = queryBuilder.keyword();
            if (!fuzzy) {
                term.fuzzy().withEditDistanceUpTo(0);
            }
            termination.must(term.onFields(tagFields).matching(tagIds).createQuery());
        }
        if (CommonUtils.notEmpty(dictionaryValues)) {
            for (String value : dictionaryValues) {
                termination.must(queryBuilder.phrase().onField(dictionaryField).sentence(value).createQuery());
            }
        }
        if (null != startPublishDate) {
            termination.must(queryBuilder.range().onField("publishDate").above(startPublishDate).createQuery());
        }
        if (null != endPublishDate) {
            termination.must(queryBuilder.range().onField("publishDate").below(endPublishDate).createQuery());
        }
        if (null != expiryDate) {
            termination.must(queryBuilder.range().onField("expiryDate").from(1L).to(expiryDate.getTime()).createQuery()).not();
        }
        if (CommonUtils.notEmpty(categoryIds)) {
            @SuppressWarnings("rawtypes")
            BooleanJunction<BooleanJunction> tempJunction = queryBuilder.bool();
            for (Integer categoryId : categoryIds) {
                tempJunction.should(new TermQuery(new Term("categoryId", categoryId.toString())));
            }
            termination.must(tempJunction.createQuery());
        }
        if (CommonUtils.notEmpty(modelIds)) {
            @SuppressWarnings("rawtypes")
            BooleanJunction<BooleanJunction> tempJunction = queryBuilder.bool();
            for (String modelId : modelIds) {
                tempJunction.should(new TermQuery(new Term("modelId", modelId)));
            }
            termination.must(tempJunction.createQuery());
        }
        CmsFullTextQuery query = getCmsFullTextQuery(termination.createQuery());
        if ("publishDate".equals(orderField)) {
            Sort sort = new Sort(new SortField("publishDate", SortField.Type.LONG, true));
            query.getFullTextQuery().setSort(sort);
        }
        if (projection) {
            query.getFullTextQuery().setProjection(projectionFields);
            query.getFullTextQuery().setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        }
        return query;
    }

    /**
     * @param siteId
     * @param categoryIds
     * @return number of data deleted
     */
    public int deleteByCategoryIds(short siteId, Integer[] categoryIds) {
        if (CommonUtils.notEmpty(categoryIds)) {
            QueryHandler queryHandler = getQueryHandler("update CmsContent bean set bean.disabled = :disabled");
            queryHandler.condition("bean.siteId = :siteId").setParameter("siteId", siteId);
            queryHandler.condition("bean.categoryId in (:categoryIds)").setParameter("categoryIds", categoryIds)
                    .setParameter("disabled", true);
            return update(queryHandler);
        }
        return 0;
    }

    /**
     * @param siteId
     * @param ids
     */
    public void index(short siteId, Serializable[] ids) {
        for (CmsContent entity : getEntitys(ids)) {
            if (siteId == entity.getSiteId()) {
                index(entity);
            }
        }
    }

    /**
     * @param queryEntitry
     * @param orderField
     * @param orderType
     * @param pageIndex
     * @param pageSize
     * @return results page
     */
    public PageHandler getPage(CmsContentQuery queryEntitry, String orderField, String orderType, Integer pageIndex,
            Integer pageSize) {
        QueryHandler queryHandler = getQueryHandler("from CmsContent bean");
        if (CommonUtils.notEmpty(queryEntitry.getSiteId())) {
            queryHandler.condition("bean.siteId = :siteId").setParameter("siteId", queryEntitry.getSiteId());
        }
        if (CommonUtils.notEmpty(queryEntitry.getStatus())) {
            queryHandler.condition("bean.status in (:status)").setParameter("status", queryEntitry.getStatus());
        }
        if (CommonUtils.notEmpty(queryEntitry.getParentId())) {
            queryHandler.condition("bean.parentId = :parentId").setParameter("parentId", queryEntitry.getParentId());
        } else {
            if (CommonUtils.notEmpty(queryEntitry.getCategoryIds())) {
                queryHandler.condition("bean.categoryId in (:categoryIds)").setParameter("categoryIds",
                        queryEntitry.getCategoryIds());
            } else if (CommonUtils.notEmpty(queryEntitry.getCategoryId())) {
                queryHandler.condition("bean.categoryId = :categoryId").setParameter("categoryId", queryEntitry.getCategoryId());
            }
            if (null != queryEntitry.getEmptyParent() && queryEntitry.getEmptyParent()) {
                queryHandler.condition("bean.parentId is null");
            }
        }
        if (null != queryEntitry.getDisabled()) {
            queryHandler.condition("bean.disabled = :disabled").setParameter("disabled", queryEntitry.getDisabled());
        }
        if (CommonUtils.notEmpty(queryEntitry.getQuoteId())) {
            queryHandler.condition("bean.quoteContentId = :quoteContentId").setParameter("quoteContentId",
                    queryEntitry.getQuoteId());
        } else {
            if (null != queryEntitry.getEmptyQuote() && queryEntitry.getEmptyQuote()) {
                queryHandler.condition("bean.quoteContentId is null");
            }
        }
        if (CommonUtils.notEmpty(queryEntitry.getModelIds())) {
            queryHandler.condition("bean.modelId in (:modelIds)").setParameter("modelIds", queryEntitry.getModelIds());
        }
        if (CommonUtils.notEmpty(queryEntitry.getUserId())) {
            queryHandler.condition("bean.userId = :userId").setParameter("userId", queryEntitry.getUserId());
        }
        if (null != queryEntitry.getOnlyUrl()) {
            queryHandler.condition("bean.onlyUrl = :onlyUrl").setParameter("onlyUrl", queryEntitry.getOnlyUrl());
        }
        if (null != queryEntitry.getHasImages()) {
            queryHandler.condition("bean.hasImages = :hasImages").setParameter("hasImages", queryEntitry.getHasImages());
        }
        if (null != queryEntitry.getHasFiles()) {
            queryHandler.condition("bean.hasFiles = :hasFiles").setParameter("hasFiles", queryEntitry.getHasFiles());
        }
        if (null != queryEntitry.getHasCover()) {
            if (queryEntitry.getHasCover()) {
                queryHandler.condition("bean.cover is not null");
            } else {
                queryHandler.condition("bean.cover is null");
            }
        }
        if (CommonUtils.notEmpty(queryEntitry.getTitle())) {
            queryHandler.condition("(bean.title like :title)").setParameter("title", like(queryEntitry.getTitle()));
        }
        if (null != queryEntitry.getStartPublishDate()) {
            queryHandler.condition("bean.publishDate > :startPublishDate").setParameter("startPublishDate",
                    queryEntitry.getStartPublishDate());
        }
        if (null != queryEntitry.getEndPublishDate()) {
            queryHandler.condition("bean.publishDate <= :endPublishDate").setParameter("endPublishDate",
                    queryEntitry.getEndPublishDate());
        }
        if (null != queryEntitry.getExpiryDate()) {
            queryHandler.condition("(bean.expiryDate is null or bean.expiryDate >= :expiryDate)").setParameter("expiryDate",
                    queryEntitry.getExpiryDate());
        }
        if (!ORDERTYPE_ASC.equalsIgnoreCase(orderType)) {
            orderType = ORDERTYPE_DESC;
        }
        if (null == orderField) {
            orderField = CommonConstants.BLANK;
        }
        switch (orderField) {
        case "scores":
            queryHandler.order("bean.scores ").append(orderType);
            break;
        case "comments":
            queryHandler.order("bean.comments ").append(orderType);
            break;
        case "clicks":
            queryHandler.order("bean.clicks ").append(orderType);
            break;
        case "publishDate":
            queryHandler.order("bean.publishDate ").append(orderType);
            break;
        case "updateDate":
            queryHandler.order("bean.updateDate ").append(orderType);
            break;
        case "checkDate":
            queryHandler.order("bean.checkDate ").append(orderType);
            break;
        default:
            if (ORDERTYPE_DESC.equals(orderType)) {
                queryHandler.order("bean.sort desc");
            }
            queryHandler.order("bean.publishDate ").append(orderType);
        }
        queryHandler.order("bean.id desc");
        return getPage(queryHandler, pageIndex, pageSize);
    }

    @Override
    protected CmsContent init(CmsContent entity) {
        Date now = CommonUtils.getDate();
        if (null == entity.getCreateDate()) {
            entity.setCreateDate(now);
        }
        if (null == entity.getPublishDate()) {
            entity.setPublishDate(now);
        }
        if (CommonUtils.empty(entity.getTagIds())) {
            entity.setTagIds(null);
        }
        if (CommonUtils.empty(entity.getAuthor())) {
            entity.setAuthor(null);
        }
        if (CommonUtils.empty(entity.getCover())) {
            entity.setCover(null);
        }
        return entity;
    }

}
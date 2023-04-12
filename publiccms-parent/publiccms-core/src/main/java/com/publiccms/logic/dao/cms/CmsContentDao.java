package com.publiccms.logic.dao.cms;

import java.io.Serializable;
import java.math.BigDecimal;

// Generated 2015-5-8 16:50:23 by com.publiccms.common.generator.SourceGenerator

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.ObjIntConsumer;
import java.util.function.UnaryOperator;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.ParseException;
import org.hibernate.search.backend.lucene.LuceneBackend;
import org.hibernate.search.engine.backend.Backend;
import org.hibernate.search.engine.search.aggregation.AggregationKey;
import org.hibernate.search.engine.search.predicate.dsl.BooleanPredicateClausesStep;
import org.hibernate.search.engine.search.query.SearchResult;
import org.hibernate.search.engine.search.query.dsl.SearchQueryOptionsStep;
import org.hibernate.search.engine.search.query.dsl.SearchQuerySelectStep;
import org.hibernate.search.mapper.orm.common.EntityReference;
import org.hibernate.search.mapper.orm.search.loading.dsl.SearchLoadingOptionsStep;
import org.hibernate.search.util.common.data.Range;
import org.springframework.stereotype.Repository;

import com.publiccms.common.base.BaseDao;
import com.publiccms.common.base.HighLighterQuery;
import com.publiccms.common.constants.Constants;
import com.publiccms.common.handler.FacetPageHandler;
import com.publiccms.common.handler.PageHandler;
import com.publiccms.common.handler.QueryHandler;
import com.publiccms.common.search.CmsContentTextBinder;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.entities.cms.CmsContent;
import com.publiccms.views.pojo.query.CmsContentQuery;
import com.publiccms.views.pojo.query.CmsContentSearchQuery;

/**
 *
 * CmsContentDao
 *
 */
@Repository
public class CmsContentDao extends BaseDao<CmsContent> {
    private static final String titleField = "title";
    private static final String siteIdField = "siteId";
    private static final String userIdField = "userId";
    private static final String parentIdField = "parentId";
    private static final String categoryIdField = "categoryId";
    private static final String modelIdField = "modelId";
    private static final String descriptionField = "description";
    private static final String[] textFields = new String[] { titleField, "author", "editor", descriptionField, "text", "files" };
    private static final String[] highLighterTextFields = new String[] { titleField, "author", "editor", descriptionField };
    private static final String[] tagFields = new String[] { "tagIds" };
    private static final String dictionaryField = "dictionaryValues";

    private static final Date startDate = new Date(1);

    /**
     * @param queryEntity
     * @param orderField
     * @param pageIndex
     * @param pageSize
     * @param maxResults
     * @return results page
     */
    public PageHandler query(CmsContentSearchQuery queryEntity, String orderField, Integer pageIndex, Integer pageSize,
            Integer maxResults) {
        if (CommonUtils.notEmpty(queryEntity.getFields())) {
            for (String field : queryEntity.getFields()) {
                if (!ArrayUtils.contains(textFields, field)) {
                    queryEntity.setFields(textFields);
                }
            }
        } else {
            queryEntity.setFields(textFields);
        }
        initHighLighterQuery(queryEntity.getHighLighterQuery(), queryEntity.getText());
        SearchQueryOptionsStep<?, CmsContent, ?, ?, ?> optionsStep = getOptionsStep(queryEntity, orderField);
        return getPage(optionsStep, queryEntity.getHighLighterQuery(), pageIndex, pageSize, maxResults);
    }

    /**
     * @param queryEntity
     * @param orderField
     * @param pageIndex
     * @param pageSize
     * @param maxResults
     * @return results page
     */
    public FacetPageHandler facetQuery(CmsContentSearchQuery queryEntity, String orderField, Integer pageIndex, Integer pageSize,
            Integer maxResults) {
        if (CommonUtils.notEmpty(queryEntity.getFields())) {
            for (String field : queryEntity.getFields()) {
                if (!ArrayUtils.contains(textFields, field)) {
                    queryEntity.setFields(textFields);
                }
            }
        } else {
            queryEntity.setFields(textFields);
        }
        initHighLighterQuery(queryEntity.getHighLighterQuery(), queryEntity.getText());
        SearchQueryOptionsStep<?, CmsContent, ?, ?, ?> optionsStep = getOptionsStep(queryEntity, orderField);

        AggregationKey<Map<Integer, Long>> categoryIdKey = AggregationKey.of("categoryIdKey");
        AggregationKey<Map<String, Long>> modelIdKey = AggregationKey.of("modelIdKey");

        UnaryOperator<SearchQueryOptionsStep<?, CmsContent, ?, ?, ?>> facetFieldKeys = o -> {
            o.aggregation(categoryIdKey, f -> f.terms().field(categoryIdField, Integer.class).orderByCountDescending()
                    .minDocumentCount(1).maxTermCount(10));
            o.aggregation(modelIdKey, f -> f.terms().field(modelIdField, String.class).orderByCountDescending()
                    .minDocumentCount(1).maxTermCount(10));
            return o;
        };

        Function<SearchResult<CmsContent>, Map<String, Map<String, Long>>> facetFieldResult = r -> {
            Map<Integer, Long> temp = r.aggregation(categoryIdKey);
            Map<String, Long> value = new LinkedHashMap<>();
            for (Entry<Integer, Long> entry : temp.entrySet()) {
                value.put(entry.getKey().toString(), entry.getValue());
            }
            Map<String, Map<String, Long>> map = new LinkedHashMap<>();
            map.put(categoryIdField, value);
            map.put(modelIdField, r.aggregation(modelIdKey));
            return map;
        };
        return getFacetPage(optionsStep, facetFieldKeys, facetFieldResult, queryEntity.getHighLighterQuery(), pageIndex, pageSize,
                maxResults);
    }

    private void initHighLighterQuery(HighLighterQuery highLighterQuery, String text) {
        if (highLighterQuery.isHighlight() && CommonUtils.notEmpty(text)) {
            highLighterQuery.setFields(highLighterTextFields);
            Backend backend = getSearchBackend();
            Optional<? extends Analyzer> analyzer;
            if (backend instanceof LuceneBackend) {
                analyzer = backend.unwrap(LuceneBackend.class).analyzer(CmsContentTextBinder.ANALYZER_NAME);
            } else {
                analyzer = Optional.of(new StandardAnalyzer());
            }
            if (analyzer.isPresent()) {
                MultiFieldQueryParser queryParser = new MultiFieldQueryParser(highLighterTextFields, analyzer.get());
                try {
                    highLighterQuery.setQuery(queryParser.parse(text));
                } catch (ParseException e) {
                }
            }
        }
    }

    private SearchQueryOptionsStep<?, CmsContent, ?, ?, ?> getOptionsStep(CmsContentSearchQuery queryEntity, String orderField) {

        Consumer<? super BooleanPredicateClausesStep<?>> clauseContributor = b -> {
            b.must(t -> t.match().field(siteIdField).matching(queryEntity.getSiteId()));
            if (CommonUtils.notEmpty(queryEntity.getParentId())) {
                b.must(t -> t.match().field(parentIdField).matching(queryEntity.getParentId()));
            } else {
                if (CommonUtils.notEmpty(queryEntity.getCategoryIds())) {
                    Consumer<? super BooleanPredicateClausesStep<?>> categoryContributor = c -> {
                        for (Integer categoryId : queryEntity.getCategoryIds()) {
                            c.should(t -> t.match().field(categoryIdField).matching(categoryId));
                        }
                    };
                    b.must(f -> f.bool(categoryContributor));
                }
            }
            if (CommonUtils.notEmpty(queryEntity.getModelIds())) {
                Consumer<? super BooleanPredicateClausesStep<?>> modelContributor = c -> {
                    for (String modelId : queryEntity.getModelIds()) {
                        c.should(t -> t.match().field(modelIdField).matching(modelId));
                    }
                };
                b.must(f -> f.bool(modelContributor));
            }
            if (CommonUtils.notEmpty(queryEntity.getUserId())) {
                b.must(t -> t.match().field(userIdField).matching(queryEntity.getUserId()));
            }
            if (CommonUtils.notEmpty(queryEntity.getText())) {
                Consumer<? super BooleanPredicateClausesStep<?>> keywordFiledsContributor = c -> {
                    if (ArrayUtils.contains(queryEntity.getFields(), titleField)) {
                        c.should(queryEntity.isPhrase()
                                ? t -> t.phrase().field(titleField).matching(queryEntity.getText()).boost(2.0f)
                                : t -> t.match().field(titleField).matching(queryEntity.getText()).boost(2.0f));
                    }
                    if (ArrayUtils.contains(queryEntity.getFields(), descriptionField)) {
                        c.should(queryEntity.isPhrase()
                                ? t -> t.phrase().field(descriptionField).matching(queryEntity.getText()).boost(1.5f)
                                : t -> t.match().field(descriptionField).matching(queryEntity.getText()).boost(1.5f));
                    }
                    String[] tempFields = ArrayUtils.removeElements(queryEntity.getFields(), titleField, descriptionField);
                    if (CommonUtils.notEmpty(tempFields)) {
                        c.should(queryEntity.isPhrase() ? t -> t.phrase().fields(tempFields).matching(queryEntity.getText())
                                : t -> t.match().fields(tempFields).matching(queryEntity.getText()));
                    }
                };
                b.must(f -> f.bool(keywordFiledsContributor));
            }
            if (CommonUtils.notEmpty(queryEntity.getExclude())) {
                b.mustNot(queryEntity.isPhrase()
                        ? t -> t.phrase().fields(queryEntity.getFields()).matching(queryEntity.getExclude())
                        : t -> t.match().fields(queryEntity.getFields()).matching(queryEntity.getExclude()));
            }
            if (CommonUtils.notEmpty(queryEntity.getTagIds())) {
                Consumer<? super BooleanPredicateClausesStep<?>> tagIdsFiledsContributor = c -> {
                    for (Long tagId : queryEntity.getTagIds()) {
                        if (CommonUtils.notEmpty(tagId)) {
                            c.should(t -> t.match().fields(tagFields).matching(tagId.toString()));
                        }
                    }
                };
                b.must(f -> f.bool(tagIdsFiledsContributor));
            }
            if (CommonUtils.notEmpty(queryEntity.getExtendsValues())) {
                Consumer<? super BooleanPredicateClausesStep<?>> extendsFiledsContributor = c -> {
                    for (String value : queryEntity.getExtendsValues()) {
                        if (CommonUtils.notEmpty(value)) {
                            String[] vs = StringUtils.split(value, ":", 2);
                            if (2 == vs.length) {
                                c.should(queryEntity.isPhrase()
                                        ? t -> t.phrase().field(CommonUtils.joinString("extend.", vs[0])).matching(vs[1])
                                                .boost(2.0f)
                                        : t -> t.match().field(CommonUtils.joinString("extend.", vs[0])).matching(vs[1])
                                                .boost(2.0f));
                            }
                        }
                    }
                };
                b.must(f -> f.bool(extendsFiledsContributor));
            }
            if (CommonUtils.notEmpty(queryEntity.getDictionaryValues())) {
                Consumer<? super BooleanPredicateClausesStep<?>> dictionaryFiledsContributor = c -> {
                    for (String value : queryEntity.getDictionaryValues()) {
                        if (CommonUtils.notEmpty(value)) {
                            if (null != queryEntity.getDictionaryUnion() && queryEntity.getDictionaryUnion()) {
                                c.should(t -> t.match().fields(dictionaryField).matching(value));
                            } else {
                                c.must(t -> t.match().fields(dictionaryField).matching(value));
                            }
                        }
                    }
                };
                b.must(f -> f.bool(dictionaryFiledsContributor));
            }
            if (null != queryEntity.getStartPublishDate()) {
                b.must(t -> t.range().field("publishDate").greaterThan(queryEntity.getStartPublishDate()));
            }
            if (null != queryEntity.getEndPublishDate()) {
                b.must(t -> t.range().field("publishDate").atMost(queryEntity.getEndPublishDate()));
            }
            if (null != queryEntity.getExpiryDate()) {
                b.must(t -> t.bool()
                        .mustNot(t.range().field("expiryDate").range(Range.canonical(startDate, queryEntity.getExpiryDate()))));
            }
        };
        SearchQuerySelectStep<?, EntityReference, CmsContent, SearchLoadingOptionsStep, ?, ?> selectStep = getSearchSession()
                .search(getEntityClass());
        SearchQueryOptionsStep<?, CmsContent, ?, ?, ?> optionsStep;
        if (queryEntity.isProjection()) {
            optionsStep = selectStep.select(f -> f.entity()).where(f -> f.bool(clauseContributor));
        } else {
            optionsStep = selectStep.where(f -> f.bool(clauseContributor));
        }
        if ("sort".equals(orderField)) {
            optionsStep.sort(f -> f.field("sort").desc());
        }
        if ("publishDate".equals(orderField)) {
            optionsStep.sort(f -> f.field("publishDate").desc());
        }
        if ("clicks".equals(orderField)) {
            optionsStep.sort(f -> f.field("clicks").desc());
        }
        if ("score".equals(orderField)) {
            optionsStep.sort(f -> f.field("score").desc());
        }
        return optionsStep;
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
     * @param topId
     * @param categoryId
     * @return number of data updated
     */
    public int moveByTopId(short siteId, Long topId, Integer categoryId) {
        if (null != topId && null != categoryId) {
            QueryHandler queryHandler = getQueryHandler("update CmsContent bean set bean.categoryId = :categoryId");
            queryHandler.condition("bean.siteId = :siteId").setParameter("siteId", siteId);
            queryHandler.condition("bean.parentId is not null");
            queryHandler.condition("bean.quoteContentId  = :topId").setParameter("topId", topId).setParameter("categoryId",
                    categoryId);
            return update(queryHandler);
        }
        return 0;
    }

    public int deleteByQuoteId(short siteId, Long quoteId) {
        if (CommonUtils.notEmpty(quoteId)) {
            QueryHandler queryHandler = getQueryHandler("update CmsContent bean set bean.disabled = :disabled");
            queryHandler.condition("bean.siteId = :siteId").setParameter("siteId", siteId);
            queryHandler.condition("bean.quoteContentId = :quoteContentId").setParameter("quoteContentId", quoteId)
                    .setParameter("disabled", true);
            return delete(queryHandler);
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

    public void batchWorkContent(short siteId, Integer categoryId, String modelId, ObjIntConsumer<List<CmsContent>> worker,
            int batchSize) {
        QueryHandler queryHandler = getQueryHandler("from CmsContent bean");
        queryHandler.condition("bean.siteId = :siteId").setParameter("siteId", siteId);
        queryHandler.condition("bean.disabled = :disabled").setParameter("disabled", false);
        if (CommonUtils.notEmpty(categoryId)) {
            queryHandler.condition("bean.categoryId = :categoryId").setParameter("categoryId", categoryId);
        }
        if (CommonUtils.notEmpty(modelId)) {
            queryHandler.condition("bean.modelId = :modelId").setParameter("modelId", modelId);
        }
        batchWork(queryHandler, worker, batchSize);
    }

    public void batchWorkId(short siteId, Integer categoryId, String modelId, ObjIntConsumer<List<Serializable>> worker,
            int batchSize) {
        QueryHandler queryHandler = getQueryHandler("select bean.id from CmsContent bean");
        queryHandler.condition("bean.siteId = :siteId").setParameter("siteId", siteId);
        queryHandler.condition("bean.disabled = :disabled").setParameter("disabled", false);
        if (CommonUtils.notEmpty(categoryId)) {
            queryHandler.condition("bean.categoryId = :categoryId").setParameter("categoryId", categoryId);
        }
        if (CommonUtils.notEmpty(modelId)) {
            queryHandler.condition("bean.modelId = :modelId").setParameter("modelId", modelId);
        }
        batchWork(queryHandler, worker, batchSize, Serializable.class);
    }

    /**
     * @param queryEntitry
     * @param orderField
     * @param orderType
     * @param firstResult
     * @param pageIndex
     * @param pageSize
     * @param maxResults
     * @return results page
     */
    public PageHandler getPage(CmsContentQuery queryEntitry, String orderField, String orderType, Integer firstResult,
            Integer pageIndex, Integer pageSize, Integer maxResults) {
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
            }
            if (null != queryEntitry.getEmptyParent()) {
                if (Boolean.TRUE.equals(queryEntitry.getEmptyParent())) {
                    queryHandler.condition("bean.parentId is null");
                } else {
                    queryHandler.condition("bean.parentId is not null");
                }
            }
        }
        if (null != queryEntitry.getDisabled()) {
            queryHandler.condition("bean.disabled = :disabled").setParameter("disabled", queryEntitry.getDisabled());
        }
        if (CommonUtils.notEmpty(queryEntitry.getModelIds())) {
            queryHandler.condition("bean.modelId in (:modelIds)").setParameter("modelIds", queryEntitry.getModelIds());
        }
        if (CommonUtils.notEmpty(queryEntitry.getUserId())) {
            queryHandler.condition("bean.userId = :userId").setParameter("userId", queryEntitry.getUserId());
        }
        if (CommonUtils.notEmpty(queryEntitry.getDeptId())) {
            queryHandler.condition("bean.deptId = :deptId").setParameter("deptId", queryEntitry.getDeptId());
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
        if (null != queryEntitry.getHasProducts()) {
            queryHandler.condition("bean.hasProducts = :hasProducts").setParameter("hasProducts", queryEntitry.getHasProducts());
        }
        if (null != queryEntitry.getHasCover()) {
            if (Boolean.TRUE.equals(queryEntitry.getHasCover())) {
                queryHandler.condition("bean.cover is not null");
            } else {
                queryHandler.condition("bean.cover is null");
            }
        }
        if (CommonUtils.notEmpty(queryEntitry.getTitle())) {
            queryHandler.condition("bean.title like :title ").setParameter("title", like(queryEntitry.getTitle()));
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
            orderField = Constants.BLANK;
        }
        switch (orderField) {
        case "scores":
        case "score":
            queryHandler.order("bean.score").append(orderType);
            break;
        case "comments":
            queryHandler.order("bean.comments").append(orderType);
            break;
        case "clicks":
            queryHandler.order("bean.clicks").append(orderType);
            break;
        case "publishDate":
            queryHandler.order("bean.publishDate").append(orderType);
            break;
        case "updateDate":
            queryHandler.order("bean.updateDate").append(orderType);
            break;
        case "checkDate":
            queryHandler.order("bean.checkDate").append(orderType);
            break;
        default:
            if (ORDERTYPE_DESC.equals(orderType)) {
                queryHandler.order("bean.sort desc");
            }
            queryHandler.order("bean.publishDate").append(orderType);
        }
        queryHandler.order("bean.id desc");
        return getPage(queryHandler, firstResult, pageIndex, pageSize, maxResults);
    }

    /**
     * @param siteId
     * @param status
     * @param startCreateDate
     * @param endCreateDate
     * @param workloadType
     * @param dateField
     * @param pageIndex
     * @param pageSize
     * @return result page
     */
    public PageHandler getWorkLoadPage(short siteId, Integer[] status, Date startCreateDate, Date endCreateDate,
            String workloadType, String dateField, Integer pageIndex, Integer pageSize) {
        QueryHandler queryHandler = getQueryHandler("select new com.publiccms.views.pojo.entities.Workload(");
        if ("dept".equalsIgnoreCase(workloadType)) {
            queryHandler.append("0,bean.deptId");
        } else if ("user".equalsIgnoreCase(workloadType)) {
            queryHandler.append("0,bean.userId");
        } else if ("categoryUser".equalsIgnoreCase(workloadType)) {
            queryHandler.append("bean.categoryId,bean.userId");
        } else if ("categoryDept".equalsIgnoreCase(workloadType)) {
            queryHandler.append("bean.categoryId,bean.deptId");
        } else {
            queryHandler.append("bean.categoryId");
        }
        queryHandler.append(",count(*)) from CmsContent bean");
        queryHandler.condition("bean.siteId = :siteId").setParameter("siteId", siteId);
        if (CommonUtils.notEmpty(status)) {
            queryHandler.condition("bean.status in (:status)").setParameter("status", status);
        }
        if (!"publishDate".equals(dateField)) {
            dateField = "createDate";
        }
        if (null != startCreateDate) {
            queryHandler.condition("bean.").appendWithoutSpace(dateField).appendWithoutSpace(" > :startCreateDate")
                    .setParameter("startCreateDate", startCreateDate);
        }
        if (null != endCreateDate) {
            queryHandler.condition("bean.").appendWithoutSpace(dateField).appendWithoutSpace(" <= :endCreateDate")
                    .setParameter("endCreateDate", endCreateDate);
        }
        queryHandler.condition("bean.disabled = :disabled").setParameter("disabled", false);
        if ("dept".equalsIgnoreCase(workloadType)) {
            queryHandler.group("bean.deptId");
        } else if ("user".equalsIgnoreCase(workloadType)) {
            queryHandler.group("bean.userId");
        } else if ("categoryUser".equalsIgnoreCase(workloadType)) {
            queryHandler.group("bean.categoryId,bean.userId");
        } else if ("categoryDept".equalsIgnoreCase(workloadType)) {
            queryHandler.group("bean.categoryId,bean.deptId");
        } else {
            queryHandler.group("bean.categoryId");
        }
        queryHandler.order("count(*) desc");
        return getPage(queryHandler, pageIndex, pageSize);
    }

    public List<CmsContent> getListByQuoteId(short siteId, long quoteId) {
        QueryHandler queryHandler = getQueryHandler("from CmsContent bean");
        queryHandler.condition("bean.siteId = :siteId").setParameter("siteId", siteId);
        queryHandler.condition("bean.parentId is null");
        queryHandler.condition("bean.disabled = :disabled").setParameter("disabled", false);
        queryHandler.condition("bean.quoteContentId = :quoteContentId").setParameter("quoteContentId", quoteId);
        return getEntityList(queryHandler);
    }

    /**
     * @param siteId
     * @param topId
     * @return number of data deleted
     */
    public List<CmsContent> getListByTopId(short siteId, long topId) {
        QueryHandler queryHandler = getQueryHandler("from CmsContent bean");
        queryHandler.condition("bean.siteId = :siteId").setParameter("siteId", siteId);
        queryHandler.condition("bean.parentId is not null");
        queryHandler.condition("bean.disabled = :disabled").setParameter("disabled", false);
        queryHandler.condition("bean.quoteContentId = :topId").setParameter("topId", topId);
        return getEntityList(queryHandler);
    }

    @Override
    protected CmsContent init(CmsContent entity) {
        if (null == entity.getId()) {
            entity.setId(getId());
        }
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
        if (null == entity.getScore()) {
            entity.setScore(BigDecimal.ZERO);
        }
        if (CommonUtils.notEmpty(entity.getTitle())) {
            entity.setTitle(CommonUtils.keep(entity.getTitle(), 255));
        }
        if (CommonUtils.notEmpty(entity.getDescription())) {
            entity.setDescription(CommonUtils.keep(entity.getDescription(), 300));
        }
        return entity;
    }

}
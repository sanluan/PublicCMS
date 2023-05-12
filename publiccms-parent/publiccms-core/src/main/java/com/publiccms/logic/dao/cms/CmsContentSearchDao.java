package com.publiccms.logic.dao.cms;

// Generated 2015-5-8 16:50:23 by com.publiccms.common.generator.SourceGenerator

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.UnaryOperator;

import javax.annotation.Resource;

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
import com.publiccms.common.handler.FacetPageHandler;
import com.publiccms.common.handler.PageHandler;
import com.publiccms.common.search.CmsContentTextBinder;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.entities.cms.CmsContent;
import com.publiccms.views.pojo.query.CmsContentSearchQuery;

/**
 *
 * CmsContentDao
 *
 */
@Repository
public class CmsContentSearchDao extends BaseDao<CmsContent> {
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
        if ("collections".equals(orderField)) {
            optionsStep.sort(f -> f.field("collections").desc());
        }
        if ("score".equals(orderField)) {
            optionsStep.sort(f -> f.field("score").desc());
        }
        return optionsStep;
    }

    @Override
    protected CmsContent init(CmsContent entity) {
        return dao.init(entity);
    }

    @Resource
    private CmsContentDao dao;
}
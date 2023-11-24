package com.publiccms.logic.dao.sys;

import java.util.function.Consumer;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.search.engine.search.predicate.dsl.BooleanPredicateOptionsCollector;
import org.hibernate.search.engine.search.query.dsl.SearchQueryOptionsStep;
import org.hibernate.search.engine.search.query.dsl.SearchQuerySelectStep;
import org.hibernate.search.mapper.orm.common.EntityReference;
import org.hibernate.search.mapper.orm.search.loading.dsl.SearchLoadingOptionsStep;
import org.springframework.stereotype.Repository;

import com.publiccms.common.handler.PageHandler;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.entities.cms.CmsContent;
import com.publiccms.logic.dao.cms.CmsContentDao;
import com.publiccms.views.pojo.query.SysUserSearchQuery;

import jakarta.annotation.Resource;

/**
 *
 * SysUserSearchDao
 *
 */
@SuppressWarnings("deprecation")
@Repository
public class SysUserSearchDao {
    private static final String nameField = "nickname";
    private static final String siteIdField = "siteId";
    private static final String deptIdField = "deptId";
    private static final String[] textFields = new String[] { nameField, "text" };
    private static final String dictionaryField = "dictionaryValues";

    /**
     * @param queryEntity
     * @param orderField
     * @param pageIndex
     * @param pageSize
     * @param maxResults
     * @return results page
     */
    public PageHandler query(SysUserSearchQuery queryEntity, String orderField, Integer pageIndex, Integer pageSize,
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
        SearchQueryOptionsStep<?, CmsContent, ?, ?, ?> optionsStep = getOptionsStep(queryEntity, orderField);
        return dao.getPage(optionsStep, null, pageIndex, pageSize, maxResults);
    }

    private SearchQueryOptionsStep<?, CmsContent, ?, ?, ?> getOptionsStep(SysUserSearchQuery queryEntity, String orderField) {

        Consumer<? super BooleanPredicateOptionsCollector<?>> clauseContributor = b -> {
            b.must(t -> t.match().field(siteIdField).matching(queryEntity.getSiteId()));
            if (CommonUtils.notEmpty(queryEntity.getDeptId())) {
                b.must(t -> t.match().field(deptIdField).matching(queryEntity.getDeptId()));
            }
            if (CommonUtils.notEmpty(queryEntity.getText())) {
                Consumer<? super BooleanPredicateOptionsCollector<?>> keywordFiledsContributor = c -> {
                    if (ArrayUtils.contains(queryEntity.getFields(), nameField)) {
                        c.should(queryEntity.isPhrase()
                                ? t -> t.phrase().field(nameField).matching(queryEntity.getText()).boost(2.0f)
                                : t -> t.match().field(nameField).matching(queryEntity.getText()).boost(2.0f));
                    }
                    String[] tempFields = ArrayUtils.removeElements(queryEntity.getFields(), nameField);
                    if (CommonUtils.notEmpty(tempFields)) {
                        c.should(queryEntity.isPhrase() ? t -> t.phrase().fields(tempFields).matching(queryEntity.getText())
                                : t -> t.match().fields(tempFields).matching(queryEntity.getText()));
                    }
                };
                b.must(f -> f.bool().with(keywordFiledsContributor));
            }
            if (CommonUtils.notEmpty(queryEntity.getExclude())) {
                b.mustNot(queryEntity.isPhrase()
                        ? t -> t.phrase().fields(queryEntity.getFields()).matching(queryEntity.getExclude())
                        : t -> t.match().fields(queryEntity.getFields()).matching(queryEntity.getExclude()));
            }
            if (CommonUtils.notEmpty(queryEntity.getExtendsValues())) {
                Consumer<? super BooleanPredicateOptionsCollector<?>> extendsFiledsContributor = c -> {
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
                b.must(f -> f.bool().with(extendsFiledsContributor));
            }
            if (CommonUtils.notEmpty(queryEntity.getDictionaryValues())) {
                Consumer<? super BooleanPredicateOptionsCollector<?>> dictionaryFiledsContributor = c -> {
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
                b.must(f -> f.bool().with(dictionaryFiledsContributor));
            }
        };
        SearchQuerySelectStep<?, EntityReference, CmsContent, SearchLoadingOptionsStep, ?, ?> selectStep = dao.getSearchSession()
                .search(dao.getEntityClass());
        SearchQueryOptionsStep<?, CmsContent, ?, ?, ?> optionsStep;
        if (queryEntity.isProjection()) {
            optionsStep = selectStep.select(f -> f.entity()).where(f -> f.bool().with(clauseContributor));
        } else {
            optionsStep = selectStep.where(f -> f.bool().with(clauseContributor));
        }
        if ("registeredDate".equals(orderField)) {
            optionsStep.sort(f -> f.field("registeredDate").desc());
        }
        if ("lastLoginDate".equals(orderField)) {
            optionsStep.sort(f -> f.field("lastLoginDate").desc());
        }
        if ("loginCount".equals(orderField)) {
            optionsStep.sort(f -> f.field("loginCount").desc());
        }
        if ("followers".equals(orderField)) {
            optionsStep.sort(f -> f.field("followers").desc());
        }
        return optionsStep;
    }

    @Resource
    private CmsContentDao dao;
}
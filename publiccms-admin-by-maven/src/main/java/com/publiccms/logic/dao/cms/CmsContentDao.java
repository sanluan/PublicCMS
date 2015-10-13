package com.publiccms.logic.dao.cms;

// Generated 2015-5-8 16:50:23 by SourceMaker

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.hibernate.search.FullTextQuery;
import org.springframework.stereotype.Repository;

import com.publiccms.entities.cms.CmsContent;
import com.sanluan.common.base.BaseDao;
import com.sanluan.common.handler.FacetPageHandler;
import com.sanluan.common.handler.PageHandler;
import com.sanluan.common.handler.QueryHandler;

@Repository
public class CmsContentDao extends BaseDao<CmsContent> {
    String[] fields = new String[] { "title", "author", "editor", "description" };

    public PageHandler query(String text, Integer pageIndex, Integer pageSize) {
        FullTextQuery query = getQuery(fields, null, text);
        query.enableFullTextFilter("publishDate").setParameter("publishDate", getDate());
        return getPage(query, pageIndex, pageSize);
    }

    public FacetPageHandler facetQuery(final String categoryId, final String modelId, String text, Integer pageIndex,
            Integer pageSize) {
        String[] facetFields = new String[] { "categoryId", "modelId" };
        FullTextQuery query = getQuery(fields, facetFields, text);
        query.enableFullTextFilter("publishDate").setParameter("publishDate", getDate());
        return getFacetPage(query, facetFields, new HashMap<String, String>() {
            private static final long serialVersionUID = 1L;
            {
                if (notEmpty(categoryId)) {
                    put("categoryId", categoryId);
                }
                if (notEmpty(modelId)) {
                    put("modelId", modelId);
                }
            }
        }, pageIndex, pageSize);
    }

    public void index(Integer[] ids) {
        List<CmsContent> list = getEntitys(ids);
        index(list);
    }

    public PageHandler getPage(Integer[] status, Integer categoryId, Integer[] categoryIds, Boolean disabled, Integer[] modelId,
            Integer parentId, String title, Integer userId, Date startPublishDate, Date endPublishDate, String extend1,
            String extend2, String extend3, String extend4, String modelExtend1, String modelExtend2, String modelExtend3,
            String modelExtend4, String orderField, String orderType, Integer pageIndex, Integer pageSize) {
        QueryHandler queryHandler = getQueryHandler("from CmsContent bean");
        if (notEmpty(status)) {
            queryHandler.condition("bean.status in (:status)").setParameter("status", status);
        }
        if (notEmpty(categoryIds)) {
            queryHandler.condition("bean.categoryId in (:categoryIds)").setParameter("categoryIds", categoryIds);
        } else if (notEmpty(categoryId)) {
            queryHandler.condition("bean.categoryId = :categoryId").setParameter("categoryId", categoryId);
        }
        if (notEmpty(disabled)) {
            queryHandler.condition("bean.disabled = :disabled").setParameter("disabled", disabled);
        }
        if (notEmpty(modelId)) {
            queryHandler.condition("bean.modelId in (:modelId)").setParameter("modelId", modelId);
        }
        if (notEmpty(parentId)) {
            queryHandler.condition("bean.parentId = :parentId").setParameter("parentId", parentId);
        } else {
            queryHandler.condition("bean.parentId is null");
        }
        if (notEmpty(title)) {
            queryHandler.condition("(bean.title like :title)").setParameter("title", like(title));
        }
        if (notEmpty(userId)) {
            queryHandler.condition("bean.userId = :userId").setParameter("userId", userId);
        }
        if (notEmpty(startPublishDate)) {
            queryHandler.condition("bean.publishDate > :startPublishDate").setParameter("startPublishDate", startPublishDate);
        }
        if (notEmpty(endPublishDate)) {
            queryHandler.condition("bean.publishDate <= :endPublishDate").setParameter("endPublishDate", endPublishDate);
        }
        if (notEmpty(extend1)) {
            queryHandler.condition("bean.extend1 = :extend1").setParameter("extend1", extend1);
        }
        if (notEmpty(extend2)) {
            queryHandler.condition("bean.extend2 = :extend2").setParameter("extend2", extend2);
        }
        if (notEmpty(extend3)) {
            queryHandler.condition("bean.extend3 = :extend3").setParameter("extend3", extend3);
        }
        if (notEmpty(extend4)) {
            queryHandler.condition("bean.extend4 = :extend4").setParameter("extend4", extend4);
        }
        if (notEmpty(modelExtend1)) {
            queryHandler.condition("bean.modelExtend1 = :modelExtend1").setParameter("modelExtend1", modelExtend1);
        }
        if (notEmpty(modelExtend2)) {
            queryHandler.condition("bean.modelExtend2 = :modelExtend2").setParameter("modelExtend2", modelExtend2);
        }
        if (notEmpty(modelExtend3)) {
            queryHandler.condition("bean.modelExtend3 = :modelExtend3").setParameter("modelExtend3", modelExtend3);
        }
        if (notEmpty(modelExtend4)) {
            queryHandler.condition("bean.modelExtend4 = :modelExtend4").setParameter("modelExtend4", modelExtend4);
        }
        if ("asc".equalsIgnoreCase(orderType)) {
            orderType = "asc";
        } else {
            orderType = "desc";
        }
        if (!notEmpty(orderField)) {
            orderField = "";
        }
        switch (orderField) {
        case "scores":
            queryHandler.append("order by bean.scores " + orderType);
            break;
        case "comments":
            queryHandler.append("order by bean.comments " + orderType);
            break;
        case "clicks":
            queryHandler.append("order by bean.clicks " + orderType);
            break;
        case "publishDate":
            queryHandler.append("order by bean.publishDate " + orderType);
            break;
        default:
            queryHandler.append("order by bean.id " + orderType);
        }
        return getPage(queryHandler, pageIndex, pageSize);
    }

    @Override
    protected CmsContent init(CmsContent entity) {
        if (null == entity.getCreateDate()) {
            entity.setCreateDate(getDate());
        }
        if (null == entity.getPublishDate()) {
            entity.setPublishDate(getDate());
        }
        return entity;
    }

    @Override
    protected Class<CmsContent> getEntityClass() {
        return CmsContent.class;
    }

}
package com.publiccms.logic.dao.cms;

import java.io.Serializable;
import java.math.BigDecimal;

// Generated 2015-5-8 16:50:23 by com.publiccms.common.generator.SourceGenerator

import java.util.Date;
import java.util.List;
import java.util.function.ObjIntConsumer;

import org.springframework.stereotype.Repository;

import com.publiccms.common.base.BaseDao;
import com.publiccms.common.constants.Constants;
import com.publiccms.common.handler.PageHandler;
import com.publiccms.common.handler.QueryHandler;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.entities.cms.CmsContent;
import com.publiccms.views.pojo.entities.Workload;
import com.publiccms.views.pojo.query.CmsContentQuery;

/**
 *
 * CmsContentDao
 *
 */
@Repository
public class CmsContentDao extends BaseDao<CmsContent> {

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
            queryHandler.condition("bean.quoteContentId  = :topId").setParameter("topId", topId)
                    .setParameter("categoryId", categoryId);
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
        case "collections":
            queryHandler.order("bean.collections").append(orderType);
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
        return getPage(queryHandler, pageIndex, pageSize, Workload.class);
    }

    public List<CmsContent> getListByQuoteId(short siteId, long quoteId) {
        QueryHandler queryHandler = getQueryHandler("from CmsContent bean");
        queryHandler.condition("bean.siteId = :siteId").setParameter("siteId", siteId);
        queryHandler.condition("bean.parentId is null");
        queryHandler.condition("bean.disabled = :disabled").setParameter("disabled", false);
        queryHandler.condition("bean.quoteContentId = :quoteContentId").setParameter("quoteContentId", quoteId);
        return getList(queryHandler);
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
        return getList(queryHandler);
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
package com.publiccms.logic.service.cms;

import static org.springframework.util.StringUtils.arrayToDelimitedString;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Future;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.publiccms.common.base.BaseService;
import com.publiccms.common.constants.CommonConstants;
import com.publiccms.common.handler.FacetPageHandler;
import com.publiccms.common.handler.PageHandler;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.entities.cms.CmsCategory;
import com.publiccms.entities.cms.CmsContent;
import com.publiccms.logic.dao.cms.CmsCategoryDao;
import com.publiccms.logic.dao.cms.CmsContentDao;
import com.publiccms.views.pojo.entities.CmsContentStatistics;
import com.publiccms.views.pojo.query.CmsContentQuery;

/**
 *
 * CmsContentService
 * 
 */
@Service
@Transactional
public class CmsContentService extends BaseService<CmsContent> {

    /**
     * 
     */
    public static final int STATUS_DRAFT = 0;
    /**
     * 
     */
    public static final int STATUS_NORMAL = 1;
    /**
     * 
     */
    public static final int STATUS_PEND = 2;

    /**
     * @param siteId
     * @param text
     * @param tagIds
     * @param categoryId
     * @param containChild
     * @param categoryIds
     * @param modelIds
     * @param startPublishDate
     * @param endPublishDate
     * @param orderField
     * @param pageIndex
     * @param pageSize
     * @return results page
     */
    @Transactional(readOnly = true)
    public PageHandler query(Short siteId, String text, Long[] tagIds, Integer categoryId, Boolean containChild,
            Integer[] categoryIds, String[] modelIds, Date startPublishDate, Date endPublishDate, String orderField,
            Integer pageIndex, Integer pageSize) {
        return dao.query(siteId, text, arrayToDelimitedString(tagIds, CommonConstants.BLANK_SPACE),
                getCategoryIds(containChild, categoryId, categoryIds), modelIds, startPublishDate, endPublishDate, orderField,
                pageIndex, pageSize);
    }

    /**
     * @param siteId
     * @param categoryIds
     * @param modelIds
     * @param text
     * @param tagIds
     * @param startPublishDate
     * @param endPublishDate
     * @param orderField 
     * @param pageIndex
     * @param pageSize
     * @return results page
     */
    @Transactional(readOnly = true)
    public FacetPageHandler facetQuery(Short siteId, String[] categoryIds, String[] modelIds, String text, Long[] tagIds,
            Date startPublishDate, Date endPublishDate, String orderField, Integer pageIndex, Integer pageSize) {
        return dao.facetQuery(siteId, categoryIds, modelIds, text, arrayToDelimitedString(tagIds, CommonConstants.BLANK_SPACE),
                startPublishDate, endPublishDate, orderField, pageIndex, pageSize);
    }

    /**
     * @param siteId
     * @param ids
     */
    public void index(short siteId, Serializable[] ids) {
        dao.index(siteId, ids);
    }

    /**
     * @return results page
     */
    public Future<?> reCreateIndex() {
        return dao.reCreateIndex();
    }

    /**
     * @param queryEntity
     * @param containChild
     * @param orderField
     * @param orderType
     * @param pageIndex
     * @param pageSize
     * @return results page
     */
    @Transactional(readOnly = true)
    public PageHandler getPage(CmsContentQuery queryEntity, Boolean containChild, String orderField, String orderType,
            Integer pageIndex, Integer pageSize) {
        queryEntity.setCategoryIds(getCategoryIds(containChild, queryEntity.getCategoryId(), queryEntity.getCategoryIds()));
        return dao.getPage(queryEntity, orderField, orderType, pageIndex, pageSize);
    }

    /**
     * @param siteId
     * @param ids
     */
    public void refresh(short siteId, Serializable[] ids) {
        List<CmsContent> list = getEntitys(ids);
        Collections.reverse(list);
        for (CmsContent entity : list) {
            if (null != entity && STATUS_NORMAL == entity.getStatus() && siteId == entity.getSiteId()) {
                Date now = CommonUtils.getDate();
                if (now.after(entity.getPublishDate())) {
                    entity.setPublishDate(now);
                }
            }
        }
    }

    /**
     * @param siteId
     * @param userId
     * @param ids
     * @return results list
     */
    public List<CmsContent> check(short siteId, Long userId, Serializable[] ids) {
        List<CmsContent> entityList = new ArrayList<>();
        for (CmsContent entity : getEntitys(ids)) {
            if (null != entity && siteId == entity.getSiteId() && STATUS_PEND == entity.getStatus()) {
                entity.setStatus(STATUS_NORMAL);
                entity.setCheckUserId(userId);
                entity.setCheckDate(CommonUtils.getDate());
                entityList.add(entity);
            }
        }
        return entityList;
    }

    /**
     * @param siteId
     * @param userId
     * @param ids
     * @return results list
     */
    public List<CmsContent> uncheck(short siteId, Long userId, Serializable[] ids) {
        List<CmsContent> entityList = new ArrayList<>();
        for (CmsContent entity : getEntitys(ids)) {
            if (null != entity && siteId == entity.getSiteId() && STATUS_NORMAL == entity.getStatus()) {
                entity.setStatus(STATUS_PEND);
                entityList.add(entity);
            }
        }
        return entityList;
    }

    /**
     * @param id
     * @param tagIds
     * @return result
     */
    public CmsContent updateTagIds(Serializable id, String tagIds) {
        CmsContent entity = getEntity(id);
        if (null != entity) {
            entity.setTagIds(tagIds);
        }
        return entity;
    }

    /**
     * @param entitys
     */
    public void updateStatistics(Collection<CmsContentStatistics> entitys) {
        for (CmsContentStatistics entityStatistics : entitys) {
            CmsContent entity = getEntity(entityStatistics.getId());
            if (null != entity) {
                entity.setClicks(entity.getClicks() + entityStatistics.getClicks());
                entity.setComments(entity.getComments() + entityStatistics.getComments());
                entity.setScores(entity.getScores() + entityStatistics.getScores());
            }
        }
    }

    /**
     * @param siteId
     * @param id
     * @param categoryId
     * @return result
     */
    public CmsContent updateCategoryId(short siteId, Serializable id, int categoryId) {
        CmsContent entity = getEntity(id);
        if (null != entity && siteId == entity.getSiteId()) {
            entity.setCategoryId(categoryId);
        }
        return entity;
    }

    /**
     * @param id
     * @param num
     * @return result
     */
    public CmsContent updateChilds(Serializable id, int num) {
        CmsContent entity = getEntity(id);
        if (null != entity) {
            entity.setChilds(entity.getChilds() + num);
        }
        return entity;
    }

    /**
     * @param id
     * @param modelId
     */
    public void changeModel(Serializable id, String modelId) {
        CmsContent entity = getEntity(id);
        if (null != entity) {
            entity.setModelId(modelId);
        }
    }

    /**
     * @param siteId
     * @param id
     * @param sort
     * @return result
     */
    public CmsContent sort(Short siteId, Long id, int sort) {
        CmsContent entity = getEntity(id);
        if (null != entity && siteId == entity.getSiteId()) {
            entity.setSort(sort);
        }
        return entity;
    }

    /**
     * @param id
     * @param url
     * @param hasStatic
     * @return result
     */
    public CmsContent updateUrl(Serializable id, String url, boolean hasStatic) {
        CmsContent entity = getEntity(id);
        if (null != entity) {
            entity.setUrl(url);
            entity.setHasStatic(hasStatic);
        }
        return entity;
    }

    /**
     * @param siteId
     * @param categoryIds
     * @return number of data deleted
     */
    public int deleteByCategoryIds(short siteId, Integer[] categoryIds) {
        return dao.deleteByCategoryIds(siteId, categoryIds);
    }

    /**
     * @param siteId
     * @param ids
     * @return list of data deleted
     */
    @SuppressWarnings("unchecked")
    public List<CmsContent> delete(short siteId, Serializable[] ids) {
        List<CmsContent> entityList = new ArrayList<>();
        for (CmsContent entity : getEntitys(ids)) {
            if (siteId == entity.getSiteId() && !entity.isDisabled()) {
                if (0 < entity.getChilds()) {
                    for (CmsContent child : (List<CmsContent>) getPage(new CmsContentQuery(siteId, null, null, null, null, null,
                            entity.getId(), null, null, null, null, null, null, null, null, null), null, null, null, null, null)
                                    .getList()) {
                        child.setDisabled(true);
                        entityList.add(child);
                    }
                }
                entity.setDisabled(true);
                entityList.add(entity);
            }
        }
        return entityList;
    }

    private Integer[] getCategoryIds(Boolean containChild, Integer categoryId, Integer[] categoryIds) {
        if (CommonUtils.empty(categoryId)) {
            return categoryIds;
        } else if (null != containChild && containChild) {
            CmsCategory category = categoryDao.getEntity(categoryId);
            if (null != category && CommonUtils.notEmpty(category.getChildIds())) {
                String[] categoryStringIds = ArrayUtils.add(
                        StringUtils.splitByWholeSeparator(category.getChildIds(), CommonConstants.COMMA_DELIMITED),
                        String.valueOf(categoryId));
                categoryIds = new Integer[categoryStringIds.length + 1];
                for (int i = 0; i < categoryStringIds.length; i++) {
                    categoryIds[i] = Integer.parseInt(categoryStringIds[i]);
                }
                categoryIds[categoryStringIds.length] = categoryId;
            } else {
                categoryIds = new Integer[] { categoryId };
            }
        }
        return categoryIds;
    }

    /**
     * @param siteId
     * @param ids
     * @return list of data deleted
     */
    @SuppressWarnings("unchecked")
    public List<CmsContent> recycle(short siteId, Serializable[] ids) {
        List<CmsContent> entityList = new ArrayList<>();
        for (CmsContent entity : getEntitys(ids)) {
            if (siteId == entity.getSiteId() && entity.isDisabled()) {
                if (0 < entity.getChilds()) {
                    for (CmsContent child : (List<CmsContent>) getPage(new CmsContentQuery(siteId, null, null, null, null, null,
                            entity.getId(), null, null, null, null, null, null, null, null, null), false, null, null, null, null)
                                    .getList()) {
                        child.setDisabled(false);
                        entityList.add(child);
                    }
                }
                entity.setDisabled(false);
                entityList.add(entity);
            }
        }
        return entityList;
    }

    /**
     * @param siteId
     * @param ids
     * @return list of data deleted
     */
    @SuppressWarnings("unchecked")
    public List<CmsContent> realDelete(Short siteId, Long[] ids) {
        List<CmsContent> entityList = new ArrayList<>();
        for (CmsContent entity : getEntitys(ids)) {
            if (siteId == entity.getSiteId() && entity.isDisabled()) {
                if (0 < entity.getChilds()) {
                    for (CmsContent child : (List<CmsContent>) getPage(new CmsContentQuery(siteId, null, null, null, null, null,
                            entity.getId(), null, null, null, null, null, null, null, null, null), false, null, null, null, null)
                                    .getList()) {
                        delete(child.getId());
                    }
                }
                delete(entity.getId());
            }
        }
        return entityList;
    }

    @Autowired
    private CmsContentDao dao;
    @Autowired
    private CmsCategoryDao categoryDao;
}
package com.publiccms.logic.service.cms;

// Generated 2015-5-8 16:50:23 by com.sanluan.common.source.SourceMaker

import static org.apache.commons.lang3.ArrayUtils.add;
import static org.apache.commons.lang3.StringUtils.splitByWholeSeparator;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Future;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.publiccms.entities.cms.CmsCategory;
import com.publiccms.entities.cms.CmsContent;
import com.publiccms.logic.dao.cms.CmsCategoryDao;
import com.publiccms.logic.dao.cms.CmsContentDao;
import com.publiccms.views.pojo.CmsContentStatistics;
import com.sanluan.common.base.BaseService;
import com.sanluan.common.handler.FacetPageHandler;
import com.sanluan.common.handler.PageHandler;

@Service
@Transactional
public class CmsContentService extends BaseService<CmsContent> {
    public static final int STATUS_DRAFT = 0, STATUS_NORMAL = 1, STATUS_PEND = 2;

    @Transactional(readOnly = true)
    public PageHandler query(Integer siteId, String text, String tagId, Integer pageIndex, Integer pageSize) {
        return dao.query(siteId, text, tagId, pageIndex, pageSize);
    }

    @Transactional(readOnly = true)
    public FacetPageHandler facetQuery(Integer siteId, String categoryId, String modelId, String text, String tagId,
            Integer pageIndex, Integer pageSize) {
        return dao.facetQuery(siteId, categoryId, modelId, text, tagId, pageIndex, pageSize);
    }

    public void index(int siteId, Serializable[] ids) {
        dao.index(siteId, ids);
    }

    public Future<?> reCreateIndex() {
        return dao.reCreateIndex();
    }

    @Transactional(readOnly = true)
    public PageHandler getPage(Integer siteId, Integer[] status, Integer categoryId, Boolean containChild, Boolean disabled,
            Integer[] modelId, Long parentId, Boolean emptyParent, Boolean onlyUrl, Boolean hasImages, Boolean hasFiles,
            String title, Long userId, Long checkUserId, Date startPublishDate, Date endPublishDate, String orderField,
            String orderType, Integer pageIndex, Integer pageSize) {
        return dao.getPage(siteId, status, categoryId, getCategoryIds(containChild, categoryId), disabled, modelId, parentId,
                emptyParent, onlyUrl, hasImages, hasFiles, title, userId, checkUserId, startPublishDate, endPublishDate,
                orderField, orderType, pageIndex, pageSize);
    }

    public void refresh(int siteId, Serializable[] ids) {
        Date now = getDate();
        for (CmsContent entity : getEntitys(ids)) {
            if (notEmpty(entity) && STATUS_NORMAL == entity.getStatus() && siteId == entity.getSiteId()) {
                if (now.after(entity.getPublishDate())) {
                    entity.setPublishDate(now);
                }
            }
        }
    }

    public List<CmsContent> check(int siteId, Long userId, Serializable[] ids) {
        List<CmsContent> entityList = new ArrayList<CmsContent>();
        for (CmsContent entity : getEntitys(ids)) {
            if (notEmpty(entity) && siteId == entity.getSiteId() && STATUS_PEND == entity.getStatus()) {
                entity.setStatus(STATUS_NORMAL);
                entity.setCheckUserId(userId);
                entityList.add(entity);
            }
        }
        return entityList;
    }

    public CmsContent updateTagIds(Serializable id, String tagIds) {
        CmsContent entity = getEntity(id);
        if (notEmpty(entity)) {
            entity.setTagIds(tagIds);
        }
        return entity;
    }

    public void updateStatistics(Collection<CmsContentStatistics> entitys) {
        for (CmsContentStatistics entityStatistics : entitys) {
            CmsContent entity = getEntity(entityStatistics.getId());
            if (notEmpty(entity)) {
                entity.setClicks(entity.getClicks() + entityStatistics.getClicks());
                entity.setComments(entity.getComments() + entityStatistics.getComments());
                entity.setScores(entity.getScores() + entityStatistics.getScores());
            }
        }
    }

    public CmsContent updateCategoryId(int siteId, Serializable id, int categoryId) {
        CmsContent entity = getEntity(id);
        if (notEmpty(entity) && siteId == entity.getSiteId()) {
            entity.setCategoryId(categoryId);
        }
        return entity;
    }

    public CmsContent updateChilds(Serializable id, int num) {
        CmsContent entity = getEntity(id);
        if (notEmpty(entity)) {
            entity.setChilds(entity.getChilds() + num);
        }
        return entity;
    }

    public CmsContent updateUrl(Serializable id, String url, boolean hasStatic) {
        CmsContent entity = getEntity(id);
        if (notEmpty(entity)) {
            entity.setUrl(url);
            entity.setHasStatic(hasStatic);
        }
        return entity;
    }

    public int deleteByCategoryIds(int siteId, Integer[] categoryIds) {
        return dao.deleteByCategoryIds(siteId, categoryIds);
    }

    @SuppressWarnings("unchecked")
    public List<CmsContent> delete(int siteId, Serializable[] ids) {
        List<CmsContent> entityList = new ArrayList<CmsContent>();
        for (CmsContent entity : getEntitys(ids)) {
            if (siteId == entity.getSiteId() && !entity.isDisabled()) {
                if (0 < entity.getChilds()) {
                    for (CmsContent child : (List<CmsContent>) getPage(siteId, null, null, null, false, null, entity.getId(),
                            null, null, null, null, null, null, null, null, null, null, null, null, null).getList()) {
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

    private Integer[] getCategoryIds(Boolean containChild, Integer categoryId) {
        Integer[] categoryIds = null;
        if (notEmpty(containChild) && containChild && notEmpty(categoryId)) {
            CmsCategory category = categoryDao.getEntity(categoryId);
            if (notEmpty(category) && notEmpty(category.getChildIds())) {
                String[] categoryStringIds = add(splitByWholeSeparator(category.getChildIds(), COMMA_DELIMITED), String.valueOf(categoryId));
                categoryIds = new Integer[categoryStringIds.length + 1];
                for (int i = 0; i < categoryStringIds.length; i++) {
                    categoryIds[i] = Integer.parseInt(categoryStringIds[i]);
                }
                categoryIds[categoryStringIds.length] = categoryId;
            }
        }
        return categoryIds;
    }

    @Autowired
    private CmsContentDao dao;
    @Autowired
    private CmsCategoryDao categoryDao;
}
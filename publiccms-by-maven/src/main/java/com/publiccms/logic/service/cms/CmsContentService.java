package com.publiccms.logic.service.cms;

// Generated 2015-5-8 16:50:23 by SourceMaker

import static org.apache.commons.lang3.ArrayUtils.add;
import static org.apache.commons.lang3.StringUtils.splitByWholeSeparator;

import java.io.Serializable;
import java.util.Date;
import java.util.concurrent.Future;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.publiccms.entities.cms.CmsCategory;
import com.publiccms.entities.cms.CmsContent;
import com.publiccms.logic.dao.cms.CmsCategoryDao;
import com.publiccms.logic.dao.cms.CmsContentDao;
import com.sanluan.common.base.BaseService;
import com.sanluan.common.handler.FacetPageHandler;
import com.sanluan.common.handler.PageHandler;

@Service
@Transactional
public class CmsContentService extends BaseService<CmsContent> {
    public static final int STATUS_DRAFT = 0, STATUS_NORMAL = 1, STATUS_PEND = 2;

    @Autowired
    private CmsContentDao dao;
    @Autowired
    private CmsCategoryDao categoryDao;

    @Transactional(readOnly = true)
    public PageHandler query(String text, Integer pageIndex, Integer pageSize) {
        return dao.query(text, pageIndex, pageSize);
    }

    @Transactional(readOnly = true)
    public FacetPageHandler facetQuery(String categoryId, String modelId, String text, Integer pageIndex, Integer pageSize) {
        return dao.facetQuery(categoryId, modelId, text, pageIndex, pageSize);
    }

    public void index(Integer[] ids) {
        dao.index(ids);
    }

    public Future<?> reCreateIndex() {
        return dao.reCreateIndex();
    }

    @Transactional(readOnly = true)
    public PageHandler getPage(Integer[] status, Integer categoryId, Boolean containChild, Boolean disabled, Integer[] modelId,
            Integer parentId, String title, Integer userId, Date startPublishDate, Date endPublishDate, String extend1,
            String extend2, String extend3, String extend4, String modelExtend1, String modelExtend2, String modelExtend3,
            String modelExtend4, String orderField, String orderType, Integer pageIndex, Integer pageSize) {
        return dao.getPage(status, categoryId, getCategoryIds(containChild, categoryId), disabled, modelId, parentId, title,
                userId, startPublishDate, endPublishDate, extend1, extend2, extend3, extend4, modelExtend1, modelExtend2,
                modelExtend3, modelExtend4, orderField, orderType, pageIndex, pageSize);
    }

    public CmsContent check(Serializable id) {
        CmsContent entity = getEntity(id);
        if (notEmpty(entity) && STATUS_PEND == entity.getStatus()) {
            entity.setStatus(STATUS_NORMAL);
        }
        return entity;
    }

    public CmsContent updateComments(Serializable id, int num) {
        CmsContent entity = getEntity(id);
        if (notEmpty(entity)) {
            entity.setComments(entity.getComments() + num);
        }
        return entity;
    }

    public CmsContent updateTags(Serializable id, String tags) {
        CmsContent entity = getEntity(id);
        if (notEmpty(entity)) {
            entity.setTags(tags);
        }
        return entity;
    }

    public CmsContent updateClicks(Serializable id, int num) {
        CmsContent entity = getEntity(id);
        if (notEmpty(entity)) {
            entity.setClicks(entity.getClicks() + num);
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

    public CmsContent updateUrl(Serializable id, String url) {
        CmsContent entity = getEntity(id);
        if (notEmpty(entity)) {
            entity.setUrl(url);
        }
        return entity;
    }

    @Override
    public CmsContent delete(Serializable id) {
        CmsContent entity = getEntity(id);
        if (notEmpty(entity)) {
            entity.setDisabled(true);
        }
        return entity;
    }

    private Integer[] getCategoryIds(Boolean containChild, Integer categoryId) {
        Integer[] categoryIds = null;
        if (notEmpty(containChild) && containChild && notEmpty(categoryId)) {
            CmsCategory category = categoryDao.getEntity(categoryId);
            if (notEmpty(category) && notEmpty(category.getChildIds())) {
                String[] categoryStringIds = add(splitByWholeSeparator(category.getChildIds(), ","), String.valueOf(categoryId));
                categoryIds = new Integer[categoryStringIds.length + 1];
                for (int i = 0; i < categoryStringIds.length; i++) {
                    categoryIds[i] = Integer.parseInt(categoryStringIds[i]);
                }
                categoryIds[categoryStringIds.length] = categoryId;
            }
        }
        return categoryIds;
    }
}
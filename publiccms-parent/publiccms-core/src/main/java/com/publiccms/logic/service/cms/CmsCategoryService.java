package com.publiccms.logic.service.cms;

import java.io.Serializable;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.publiccms.common.base.BaseService;
import com.publiccms.common.handler.PageHandler;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.entities.cms.CmsCategory;
import com.publiccms.logic.dao.cms.CmsCategoryDao;
import com.publiccms.views.pojo.query.CmsCategoryQuery;

/**
 *
 * CmsCategoryService
 *
 */
@Service
@Transactional
public class CmsCategoryService extends BaseService<CmsCategory> {

    /**
     * @param queryEntity
     * @param pageIndex
     * @param pageSize
     * @return results page
     */
    @Transactional(readOnly = true)
    public PageHandler getPage(CmsCategoryQuery queryEntity, Integer pageIndex, Integer pageSize) {
        return dao.getPage(queryEntity, pageIndex, pageSize);
    }

    /**
     * @param parentId
     * @param id
     */
    public void addChildIds(Serializable parentId, Serializable id) {
        if (null != parentId) {
            CmsCategory parent = getEntity(parentId);
            if (null != parent) {
                addChildIds(parent.getParentId(), id);
                String childIds;
                if (CommonUtils.notEmpty(parent.getChildIds())) {
                    childIds = parent.getChildIds() + COMMA_DELIMITED + String.valueOf(id);
                } else {
                    childIds = String.valueOf(id);
                }
                updateChildIds(parent.getId(), childIds);
            }
        }
    }

    /**
     * @param id
     * @param typeId
     */
    public void changeType(Integer id, Integer typeId) {
        CmsCategory entity = getEntity(id);
        if (null != entity) {
            entity.setTypeId(typeId);
        }
    }

    private String getChildIds(short siteId, Integer parentId) {
        StringBuilder childIds = new StringBuilder();
        @SuppressWarnings("unchecked")
        List<CmsCategory> list = (List<CmsCategory>) getPage(
                new CmsCategoryQuery(siteId, parentId, false, null, null, null, false), null, null).getList();
        if (0 < list.size()) {
            for (CmsCategory category : list) {
                childIds.append(category.getId());
                childIds.append(COMMA_DELIMITED);
                String childChildIds = getChildIds(siteId, category.getId());
                if (CommonUtils.notEmpty(childChildIds)) {
                    childIds.append(childChildIds);
                    childIds.append(COMMA_DELIMITED);
                }
            }
            if (0 < childIds.length()) {
                childIds.setLength(childIds.length() - 1);
            }
        }
        if (0 < childIds.length()) {
            return childIds.toString();
        } else {
            return null;
        }
    }

    /**
     * @param siteId
     * @param parentId
     */
    public void generateChildIds(short siteId, Integer parentId) {
        if (null != parentId) {
            updateChildIds(parentId, getChildIds(siteId, parentId));
            CmsCategory parent = getEntity(parentId);
            if (null != parent) {
                generateChildIds(siteId, parent.getParentId());
            }
        }
    }

    /**
     * @param siteId
     * @param ids
     */
    public void delete(short siteId, Integer[] ids) {
        for (CmsCategory entity : getEntitys(ids)) {
            if (siteId == entity.getSiteId() && !entity.isDisabled()) {
                @SuppressWarnings("unchecked")
                List<CmsCategory> list = (List<CmsCategory>) getPage(
                        new CmsCategoryQuery(siteId, entity.getId(), false, null, null, null, null), null, null).getList();
                for (CmsCategory child : list) {
                    child.setParentId(entity.getParentId());
                }
                entity.setDisabled(true);
                generateChildIds(entity.getSiteId(), entity.getParentId());
            }
        }
    }

    /**
     * @param id
     * @param childIds
     */
    public void updateChildIds(Serializable id, String childIds) {
        CmsCategory entity = getEntity(id);
        if (null != entity) {
            entity.setChildIds(childIds);
        }
    }

    /**
     * @param id
     * @param tagTypeIds
     */
    public void updateTagTypeIds(Serializable id, String tagTypeIds) {
        CmsCategory entity = getEntity(id);
        if (null != entity) {
            entity.setTagTypeIds(tagTypeIds);
        }
    }

    /**
     * @param id
     * @param extendId
     * @return result
     */
    public CmsCategory updateExtendId(Integer id, Integer extendId) {
        CmsCategory entity = getEntity(id);
        if (null != entity) {
            entity.setExtendId(extendId);
        }
        return entity;
    }

    /**
     * @param siteId
     * @param id
     * @param parentId
     */
    public void updateParentId(short siteId, Serializable id, Integer parentId) {
        CmsCategory entity = getEntity(id);
        if (null != entity && siteId == entity.getSiteId()) {
            entity.setParentId(parentId);
        }
    }

    /**
     * @param id
     * @param url
     * @param hasStatic
     */
    public void updateUrl(Serializable id, String url, boolean hasStatic) {
        CmsCategory entity = getEntity(id);
        if (null != entity) {
            entity.setUrl(url);
            entity.setHasStatic(hasStatic);
        }
    }

    @Autowired
    private CmsCategoryDao dao;
}
package com.publiccms.logic.service.cms;

// Generated 2015-5-8 16:50:23 by com.sanluan.common.source.SourceMaker
import java.io.Serializable;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.publiccms.entities.cms.CmsCategory;
import com.publiccms.logic.dao.cms.CmsCategoryDao;
import com.sanluan.common.base.BaseService;
import com.sanluan.common.handler.PageHandler;

@Service
@Transactional
public class CmsCategoryService extends BaseService<CmsCategory> {

    @Transactional(readOnly = true)
    public PageHandler getPage(Integer siteId, Integer parentId, Integer typeId, Boolean allowContribute, Boolean hidden,
            Boolean disabled, Integer pageIndex, Integer pageSize) {
        return dao.getPage(siteId, parentId, typeId, allowContribute, hidden, disabled, pageIndex, pageSize);
    }

    public void addChildIds(Serializable parentId, Serializable id) {
        if (notEmpty(parentId)) {
            CmsCategory parent = getEntity(parentId);
            if (notEmpty(parent)) {
                addChildIds(parent.getParentId(), id);
                String childIds;
                if (notEmpty(parent.getChildIds())) {
                    childIds = parent.getChildIds() + COMMA_DELIMITED + String.valueOf(id);
                } else {
                    childIds = String.valueOf(id);
                }
                updateChildIds(parent.getId(), childIds);
            }
        }
    }

    private String getChildIds(int siteId, Integer parentId) {
        StringBuilder childIds = new StringBuilder();
        @SuppressWarnings("unchecked")
        List<CmsCategory> list = (List<CmsCategory>) getPage(siteId, parentId, null, null, null, false, null, null).getList();
        if (0 < list.size()) {
            for (CmsCategory category : list) {
                if (childIds.length() > 0) {
                    childIds.append(COMMA_DELIMITED);
                }
                childIds.append(category.getId());
                String childChildIds = getChildIds(siteId, category.getId());
                if (childChildIds.length() > 0) {
                    childIds.append(COMMA_DELIMITED);
                    childIds.append(childChildIds);
                }
            }
        }
        return childIds.toString();
    }

    public void generateChildIds(int siteId, Integer parentId) {
        if (notEmpty(parentId)) {
            updateChildIds(parentId, getChildIds(siteId, parentId));
            CmsCategory parent = getEntity(parentId);
            if (notEmpty(parent)) {
                generateChildIds(siteId, parent.getParentId());
            }
        }
    }

    public void delete(int siteId, Integer[] ids) {
        for (CmsCategory entity : getEntitys(ids)) {
            if (siteId == entity.getSiteId() && !entity.isDisabled()) {
                @SuppressWarnings("unchecked")
                List<CmsCategory> list = (List<CmsCategory>) getPage(siteId, entity.getId(), null, null, null, null, null, null)
                        .getList();
                for (CmsCategory child : list) {
                    child.setParentId(entity.getParentId());
                }
                generateChildIds(entity.getSiteId(), entity.getParentId());
                entity.setDisabled(true);
            }
        }
    }

    public void updateChildIds(Serializable id, String childIds) {
        CmsCategory entity = getEntity(id);
        if (notEmpty(entity)) {
            entity.setChildIds(childIds);
        }
    }

    public void updateTagTypeIds(Serializable id, String tagTypeIds) {
        CmsCategory entity = getEntity(id);
        if (notEmpty(entity)) {
            entity.setTagTypeIds(tagTypeIds);
        }
    }

    public CmsCategory updateExtendId(Integer id, Integer extendId) {
        CmsCategory entity = getEntity(id);
        if (notEmpty(entity)) {
            entity.setExtendId(extendId);
        }
        return entity;
    }

    public void updateParentId(int siteId, Serializable id, Integer parentId) {
        CmsCategory entity = getEntity(id);
        if (notEmpty(entity) && siteId == entity.getSiteId()) {
            entity.setParentId(parentId);
        }
    }

    public void updateUrl(Serializable id, String url, boolean hasStatic) {
        CmsCategory entity = getEntity(id);
        if (notEmpty(entity)) {
            entity.setUrl(url);
            entity.setHasStatic(hasStatic);
        }
    }

    public void updateContents(Serializable id, int num) {
        CmsCategory entity = getEntity(id);
        if (notEmpty(entity)) {
            entity.setContents(entity.getContents() + num);
        }
    }

    @Autowired
    private CmsCategoryDao dao;
}
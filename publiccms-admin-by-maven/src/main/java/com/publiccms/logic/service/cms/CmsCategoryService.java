package com.publiccms.logic.service.cms;

// Generated 2015-5-8 16:50:23 by SourceMaker
import static org.apache.commons.lang3.StringUtils.splitByWholeSeparator;

import java.io.Serializable;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.publiccms.entities.cms.CmsCategory;
import com.publiccms.logic.dao.cms.CmsCategoryDao;
import com.publiccms.logic.dao.log.LogOperateDao;
import com.sanluan.common.base.BaseService;
import com.sanluan.common.handler.PageHandler;

@Service
@Transactional
public class CmsCategoryService extends BaseService<CmsCategory> {

    @Autowired
    private CmsCategoryDao dao;
    @Autowired
    private LogOperateDao logOperateDao;

    @Transactional(readOnly = true)
    public PageHandler getPage(Integer parentId, String extend1, String name, String extend3, String extend2, String extend4,
            Boolean disabled, Integer pageIndex, Integer pageSize) {
        return dao.getPage(parentId, extend1, name, extend3, extend2, extend4, disabled, pageIndex, pageSize);
    }

    public void addChildIds(Serializable parentId, Serializable id) {
        if (notEmpty(parentId)) {
            CmsCategory parent = getEntity(parentId);
            if (notEmpty(parent)) {
                addChildIds(parent.getParentId(), id);
                String childIds;
                if (notEmpty(parent.getChildIds())) {
                    childIds = parent.getChildIds() + "," + String.valueOf(id);
                } else {
                    childIds = String.valueOf(id);
                }
                updateChildIds(parent.getId(), childIds);
            }
        }
    }

    public void generateChildIds(Integer parentId) {
        if (notEmpty(parentId)) {
            @SuppressWarnings("unchecked")
            List<CmsCategory> list = (List<CmsCategory>) getPage(parentId, null, null, null, null, null, false, null, null)
                    .getList();
            if (0 < list.size()) {
                StringBuilder childIds = new StringBuilder();
                for (CmsCategory category : list) {
                    if (childIds.length() > 0) {
                        childIds.append(",");
                    }
                    childIds.append(category.getId());
                }
                updateChildIds(parentId, childIds.toString());
            } else {
                updateChildIds(parentId, null);
            }
            CmsCategory parent = getEntity(parentId);
            if (notEmpty(parent)) {
                generateChildIds(parent.getParentId());
            }
        }
    }

    public CmsCategory updateChildIds(Serializable id, String childIds) {
        CmsCategory entity = getEntity(id);
        if (notEmpty(entity)) {
            entity.setChildIds(childIds);
        }
        return entity;
    }

    public CmsCategory updateUrl(Serializable id, String url) {
        CmsCategory entity = getEntity(id);
        if (notEmpty(entity)) {
            entity.setUrl(url);
        }
        return entity;
    }

    public CmsCategory updateContents(Serializable id, int num) {
        CmsCategory entity = getEntity(id);
        if (notEmpty(entity)) {
            entity.setContents(entity.getContents() + num);
        }
        return entity;
    }

    @Override
    public CmsCategory delete(Serializable id) {
        CmsCategory entity = getEntity(id);
        if (notEmpty(entity) && !entity.isDisabled()) {
            entity.setDisabled(true);
            if (notEmpty(entity.getChildIds())) {
                String[] categoryStringIds = splitByWholeSeparator(entity.getChildIds(), ",");
                Integer[] categoryIds = new Integer[categoryStringIds.length];
                for (int i = 0; i < categoryStringIds.length; i++) {
                    categoryIds[i] = Integer.parseInt(categoryStringIds[i]);
                    delete(categoryIds[i]);
                }
                dao.delete(categoryIds);
            }
            generateChildIds(entity.getParentId());
        }
        return entity;
    }
}
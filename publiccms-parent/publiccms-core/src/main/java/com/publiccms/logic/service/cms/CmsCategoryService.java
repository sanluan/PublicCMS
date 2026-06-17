package com.publiccms.logic.service.cms;

import static org.springframework.util.StringUtils.arrayToCommaDelimitedString;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.publiccms.common.base.BaseService;
import com.publiccms.common.constants.Constants;
import com.publiccms.common.handler.PageHandler;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.common.tools.ExtendUtils;
import com.publiccms.entities.cms.CmsCategory;
import com.publiccms.entities.cms.CmsCategoryAttribute;
import com.publiccms.entities.sys.SysExtend;
import com.publiccms.logic.dao.cms.CmsCategoryDao;
import com.publiccms.logic.service.sys.SysExtendFieldService;
import com.publiccms.logic.service.sys.SysExtendService;
import com.publiccms.views.pojo.entities.CmsCategoryType;
import com.publiccms.views.pojo.model.CmsCategoryModelParameters;
import com.publiccms.views.pojo.model.CmsCategoryParameters;
import com.publiccms.views.pojo.query.CmsCategoryQuery;

import jakarta.annotation.Resource;

/**
 *
 * CmsCategoryService
 *
 */
@Service
@Transactional
public class CmsCategoryService extends BaseService<CmsCategory> {

    private String[] ignoreCopyProperties = new String[] { "id", "childIds", "parentId", "extendId", "code", "name", "sort" };
    private String[] ignoreProperties = new String[] { "id", "siteId", "childIds", "tagTypeIds", "url", "disabled", "extendId",
            "hasStatic", "typeId" };

    @Resource
    private CmsTagTypeService tagTypeService;
    @Resource
    private CmsCategoryAttributeService attributeService;
    @Resource
    private CmsCategoryModelService categoryModelService;
    @Resource
    private SysExtendService extendService;
    @Resource
    private SysExtendFieldService extendFieldService;
    @Resource
    private CmsEditorHistoryService editorHistoryService;

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
     * @param siteId
     * @param sitePath
     * @param entity
     * @param oldEntity
     * @param userId
     * @param attribute
     * @param categoryType
     * @param categoryParameters
     */
    public void saveTagAndAttribute(short siteId, String sitePath, CmsCategory entity, CmsCategory oldEntity, Long userId,
            CmsCategoryAttribute attribute, CmsCategoryType categoryType, CmsCategoryParameters categoryParameters) {
        if (null != entity.getId()) {
            entity.setParentIds(getParentIds(entity.getSiteId(), entity.getParentId(), false));
            update(entity.getId(), entity, ignoreProperties);
            if (null != oldEntity.getParentId() && !oldEntity.getParentId().equals(entity.getParentId())) {
                generateChildIds(siteId, oldEntity.getParentId());
                generateChildIds(siteId, entity.getParentId());
            } else if (null != entity.getParentId() && null == oldEntity.getParentId()) {
                generateChildIds(siteId, entity.getParentId());
            }
        } else {
            entity.setSiteId(siteId);
            save(entity);
        }
        if (CommonUtils.notEmpty(categoryParameters.getCategoryModelList())) {
            for (CmsCategoryModelParameters cmsCategoryModelParameters : categoryParameters.getCategoryModelList()) {
                if (null != cmsCategoryModelParameters.getCategoryModel()) {
                    cmsCategoryModelParameters.getCategoryModel().getId().setCategoryId(entity.getId());
                    if (cmsCategoryModelParameters.isUse()) {
                        categoryModelService.updateCategoryModel(siteId, cmsCategoryModelParameters.getCategoryModel());
                    } else {
                        categoryModelService.delete(cmsCategoryModelParameters.getCategoryModel().getId());
                    }
                }
            }
        }
        Integer[] tagTypeIds = tagTypeService.update(siteId, categoryParameters.getTagTypes());
        entity = getEntity(entity.getId());
        entity.setTagTypeIds(arrayToCommaDelimitedString(tagTypeIds));
        if (CommonUtils.notEmpty(categoryParameters.getContentExtends()) || null != entity.getExtendId()) {
            if (null == extendService.getEntity(entity.getExtendId())) {
                SysExtend extend = new SysExtend("category", entity.getId());
                extendService.save(extend);
                entity.setExtendId(extend.getId());
            }
            extendFieldService.update(entity.getExtendId(), categoryParameters.getContentExtends());// 修改或增加内容扩展字段
        }
        if (null != categoryType && CommonUtils.notEmpty(categoryType.getExtendList())) {
            attribute.setData(
                    ExtendUtils.getExtendString(categoryParameters.getExtendData(), sitePath, categoryType.getExtendList()));
        } else {
            attribute.setData(null);
        }

        saveEditorHistory(attributeService.getEntity(entity.getId()), siteId, entity.getId(), entity.getLang(), userId,
                categoryType, categoryParameters.getExtendData());// 保存编辑器字段历史记录
        attributeService.updateAttribute(entity.getId(), attribute);
    }

    public void copy(short siteId, CmsCategory entity, CmsCategory copy) {
        if (null == entity.getParentId() && null != copy.getParentId()) {
            entity.setParentId(copy.getParentId());
        }
        BeanUtils.copyProperties(copy, entity, ignoreCopyProperties);
        save(entity);
        categoryModelService.copy(siteId, entity.getId(), copy.getId());

        CmsCategoryAttribute copyAttribute = attributeService.getEntity(copy.getId());
        CmsCategoryAttribute attribute = new CmsCategoryAttribute();
        attribute.setCategoryId(entity.getId());
        if (null != copyAttribute) {
            attribute.setData(copyAttribute.getData());
        }
        attributeService.save(attribute);

        if (CommonUtils.notEmpty(copy.getExtendId())) {
            SysExtend extend = new SysExtend("category", entity.getId());
            extendService.save(extend);
            entity.setExtendId(extend.getId());
            extendFieldService.copy(extend.getId(), copy.getExtendId());
        }
    }

    private void saveEditorHistory(CmsCategoryAttribute oldAttribute, short siteId, int entityId, String lang, long userId,
            CmsCategoryType categoryType, Map<String, String> map) {
        if (null != oldAttribute && (CommonUtils.notEmpty(oldAttribute.getData()) && null != categoryType
                && CommonUtils.notEmpty(categoryType.getExtendList()))) {
            Map<String, String> oldMap = ExtendUtils.getExtendMap(oldAttribute.getData());
            editorHistoryService.saveHistory(siteId, userId, CmsEditorHistoryService.ITEM_TYPE_CATEGORY_EXTEND,
                    String.valueOf(entityId), lang, oldMap, map, categoryType.getExtendList());
        }
    }

    /**
     * @param siteId
     * @param code
     * @return
     */
    public CmsCategory getEntityByCode(short siteId, String code) {
        return dao.getEntityByCode(siteId, code);
    }

    /**
     * @param siteId
     * @param codes
     * @return
     */
    public List<CmsCategory> getEntitysByCodes(short siteId, String[] codes) {
        return dao.getEntitysByCodes(siteId, codes);
    }

    /**
     * @param entity
     */
    @Override
    public void save(CmsCategory entity) {
        if (entity.isOnlyUrl()) {
            entity.setUrl(entity.getPath());
        }
        entity.setParentIds(getParentIds(entity.getSiteId(), entity.getParentId(), false));
        super.save(entity);
        addChildIds(entity.getParentId(), entity.getId());
    }

    /**
     * @param id
     * @param typeId
     */
    public void changeType(Integer id, String typeId) {
        CmsCategory entity = getEntity(id);
        if (null != entity) {
            entity.setTypeId(typeId);
        }
    }

    /**
     * @param siteId
     * @param parentId
     */
    @SuppressWarnings("unchecked")
    public void generateChildIds(short siteId, Integer parentId) {
        if (null != parentId) {
            generateChildIds(siteId, parentId, true);
        } else {
            CmsCategoryQuery query = new CmsCategoryQuery();
            query.setSiteId(siteId);
            query.setQueryAll(true);
            PageHandler page = getPage(query, null, null);
            for (CmsCategory category : (List<CmsCategory>) page.getList()) {
                generateChildIds(category.getSiteId(), category.getId(), false);
            }
        }
    }

    /**
     * @param siteId
     * @param parentId
     */
    @SuppressWarnings("unchecked")
    public void generateParentIds(short siteId) {
        CmsCategoryQuery query = new CmsCategoryQuery();
        query.setSiteId(siteId);
        query.setQueryAll(true);
        PageHandler page = getPage(query, null, null);
        for (CmsCategory category : (List<CmsCategory>) page.getList()) {
            if (null == category.getParentId()) {
                updateParentIds(category.getId(), null);
            } else {
                updateParentIds(category.getId(), getParentIds(siteId, category.getParentId(), true));
            }
        }
    }

    /**
     * @param siteId
     * @param ids
     * @return
     */
    public List<CmsCategory> delete(short siteId, Integer[] ids) {
        List<CmsCategory> entityList = new ArrayList<>();
        for (CmsCategory entity : getEntitys(ids)) {
            if (siteId == entity.getSiteId() && !entity.isDisabled()) {
                @SuppressWarnings("unchecked")
                List<CmsCategory> list = (List<CmsCategory>) getPage(
                        new CmsCategoryQuery(siteId, entity.getId(), false, null, null, null, null), null, null).getList();
                for (CmsCategory child : list) {
                    child.setParentId(entity.getParentId());
                }
                entity.setCode(CommonUtils.keep(CommonUtils.joinString(entity.getCode(), "-", UUID.randomUUID().toString()), 50));
                entity.setDisabled(true);
                entityList.add(entity);
                generateChildIds(entity.getSiteId(), entity.getParentId());
            }
        }
        return entityList;
    }

    /**
     * @param id
     * @param parentIds
     */
    public void updateParentIds(Serializable id, String parentIds) {
        CmsCategory entity = getEntity(id);
        if (null != entity) {
            entity.setParentIds(parentIds);
        }
    }

    /**
     * @param id
     * @param childIds
     */
    private void updateChildIds(Serializable id, String childIds) {
        CmsCategory entity = getEntity(id);
        if (null != entity) {
            entity.setChildIds(childIds);
        }
    }

    /**
     * @param siteId
     * @param ids
     * @param workflowId
     */
    public void updateWrokflow(short siteId, Integer[] ids, Integer workflowId) {
        if (CommonUtils.notEmpty(ids)) {
            dao.deleteWorkflowIds(siteId, new Integer[] { workflowId });
            List<CmsCategory> entityList = getEntitys(ids);
            for (CmsCategory entity : entityList) {
                if (null != entity && siteId == entity.getSiteId()) {
                    entity.setWorkflowId(workflowId);
                }
            }
        }
    }

    /**
     * @param siteId
     * @param workflowIds
     * @return
     */
    public int deleteWorkflowIds(short siteId, Integer[] workflowIds) {
        return dao.deleteWorkflowIds(siteId, workflowIds);
    }

    /**
     * @param siteId
     * @param id
     * @param parentId
     */
    public void updateParentId(short siteId, Serializable id, Integer parentId) {
        CmsCategory entity = getEntity(id);
        if (null != entity && siteId == entity.getSiteId()) {
            entity.setParentIds(getParentIds(entity.getSiteId(), entity.getParentId(), false));
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

    private String getChildIds(short siteId, Integer categoryId) {
        StringBuilder childIds = new StringBuilder();
        @SuppressWarnings("unchecked")
        List<CmsCategory> list = (List<CmsCategory>) getPage(
                new CmsCategoryQuery(siteId, categoryId, false, null, null, null, false), null, null).getList();
        if (!list.isEmpty()) {
            for (CmsCategory category : list) {
                childIds.append(category.getId());
                childIds.append(Constants.COMMA);
                String childChildIds = getChildIds(siteId, category.getId());
                if (CommonUtils.notEmpty(childChildIds)) {
                    childIds.append(childChildIds);
                    childIds.append(Constants.COMMA);
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

    private String getParentIds(short siteId, Integer parentId, boolean rebuild) {
        if (null != parentId) {
            CmsCategory parent = getEntity(parentId);
            if (null == parent.getParentId()) {
                if (rebuild) {
                    parent.setParentId(null);
                }
                return String.valueOf(parentId);
            } else {
                if (CommonUtils.empty(parent.getParentIds()) || rebuild) {
                    parent.setParentIds(getParentIds(siteId, parent.getParentId(), rebuild));
                }
                StringBuilder parentIds = new StringBuilder();
                parentIds.append(parent.getParentIds());
                parentIds.append(Constants.COMMA);
                parentIds.append(parent.getId());
                return parentIds.toString();
            }
        } else {
            return null;
        }
    }

    private void generateChildIds(short siteId, Integer categoryId, boolean generateParent) {
        if (null != categoryId) {
            updateChildIds(categoryId, getChildIds(siteId, categoryId));
            if (generateParent) {
                CmsCategory category = getEntity(categoryId);
                if (null != category && null != category.getParentId()) {
                    generateChildIds(siteId, category.getParentId(), true);
                }
            }
        }
    }

    /**
     * @param parentId
     * @param id
     */
    private void addChildIds(Serializable parentId, Serializable id) {
        if (null != parentId) {
            CmsCategory parent = getEntity(parentId);
            if (null != parent) {
                addChildIds(parent.getParentId(), id);
                String childIds;
                if (CommonUtils.notEmpty(parent.getChildIds())) {
                    childIds = CommonUtils.joinString(parent.getChildIds(), Constants.COMMA, id);
                } else {
                    childIds = String.valueOf(id);
                }
                updateChildIds(parent.getId(), childIds);
            }
        }
    }

    @Resource
    private CmsCategoryDao dao;
}
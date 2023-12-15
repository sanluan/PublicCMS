package com.publiccms.logic.service.cms;

import static org.springframework.util.StringUtils.arrayToCommaDelimitedString;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang3.ArrayUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.publiccms.common.api.Config;
import com.publiccms.common.base.BaseService;
import com.publiccms.common.constants.CommonConstants;
import com.publiccms.common.handler.PageHandler;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.common.tools.ExtendUtils;
import com.publiccms.entities.cms.CmsCategory;
import com.publiccms.entities.cms.CmsCategoryAttribute;
import com.publiccms.entities.cms.CmsEditorHistory;
import com.publiccms.entities.sys.SysExtend;
import com.publiccms.entities.sys.SysExtendField;
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
     * @param id
     * @param userId
     * @param attribute
     * @param categoryType
     * @param categoryParameters
     */
    public void saveTagAndAttribute(short siteId, Integer id, Long userId, CmsCategoryAttribute attribute,
            CmsCategoryType categoryType, CmsCategoryParameters categoryParameters) {
        if (CommonUtils.notEmpty(id)) {
            if (CommonUtils.notEmpty(categoryParameters.getCategoryModelList())) {
                for (CmsCategoryModelParameters cmsCategoryModelParameters : categoryParameters.getCategoryModelList()) {
                    if (null != cmsCategoryModelParameters.getCategoryModel()) {
                        cmsCategoryModelParameters.getCategoryModel().getId().setCategoryId(id);
                        if (cmsCategoryModelParameters.isUse()) {
                            cmsCategoryModelParameters.getCategoryModel().setSiteId(siteId);
                            categoryModelService.saveOrUpdate(cmsCategoryModelParameters.getCategoryModel());
                        } else {
                            categoryModelService.delete(cmsCategoryModelParameters.getCategoryModel().getId());
                        }
                    }
                }
            }
            Integer[] tagTypeIds = tagTypeService.update(siteId, categoryParameters.getTagTypes());
            CmsCategory entity = getEntity(id);
            if (null != entity) {
                entity.setTagTypeIds(arrayToCommaDelimitedString(tagTypeIds));
            }
            if (CommonUtils.notEmpty(categoryParameters.getContentExtends()) || CommonUtils.notEmpty(entity.getExtendId())) {
                if (null == extendService.getEntity(entity.getExtendId())) {
                    SysExtend extend = new SysExtend("category", id);
                    extendService.save(extend);
                    entity.setExtendId(extend.getId());
                }
                extendFieldService.update(entity.getExtendId(), categoryParameters.getContentExtends());// 修改或增加内容扩展字段
            }
            Map<String, String> map = null;
            if (null != categoryType && CommonUtils.notEmpty(categoryType.getExtendList())) {
                map = ExtendUtils.getSysExtentDataMap(categoryParameters.getExtendDataList(), categoryType.getExtendList());
                attribute.setData(ExtendUtils.getExtendString(map));
            } else {
                attribute.setData(null);
            }

            saveEditorHistory(attributeService.getEntity(entity.getId()), siteId, entity.getId(), userId, categoryType, map);// 保存编辑器字段历史记录

            attributeService.updateAttribute(id, attribute);
        }
    }

    private void saveEditorHistory(CmsCategoryAttribute oldAttribute, short siteId, int contentId, long userId,
            CmsCategoryType categoryType, Map<String, String> map) {
        if (null != oldAttribute) {
            if (CommonUtils.notEmpty(oldAttribute.getData()) && null != categoryType
                    && CommonUtils.notEmpty(categoryType.getExtendList())) {
                Map<String, String> oldMap = ExtendUtils.getExtendMap(oldAttribute.getData());
                for (SysExtendField extendField : categoryType.getExtendList()) {
                    if (ArrayUtils.contains(Config.INPUT_TYPE_EDITORS, extendField.getInputType())) {
                        if (CommonUtils.notEmpty(oldMap) && CommonUtils.notEmpty(oldMap.get(extendField.getId().getCode()))
                                && (CommonUtils.notEmpty(map) || !oldMap.get(extendField.getId().getCode())
                                        .equals(map.get(extendField.getId().getCode())))) {
                            CmsEditorHistory history = new CmsEditorHistory(siteId,
                                    CmsEditorHistoryService.ITEM_TYPE_CATEGORY_EXTEND, String.valueOf(contentId), "text",
                                    CommonUtils.getDate(), userId, map.get(extendField.getId().getCode()));
                            editorHistoryService.save(history);
                        }
                    }
                }
            }
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
     * @param entity
     */
    public void save(CmsCategory entity) {
        if (entity.isOnlyUrl()) {
            entity.setUrl(entity.getPath());
        }
        super.save(entity);
        addChildIds(entity.getParentId(), entity.getId());
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
                    childIds = parent.getChildIds() + CommonConstants.COMMA_DELIMITED + String.valueOf(id);
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
    public void changeType(Integer id, String typeId) {
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
                childIds.append(CommonConstants.COMMA_DELIMITED);
                String childChildIds = getChildIds(siteId, category.getId());
                if (CommonUtils.notEmpty(childChildIds)) {
                    childIds.append(childChildIds);
                    childIds.append(CommonConstants.COMMA_DELIMITED);
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

    private void generateChildIds(short siteId, Integer parentId, boolean generateParent) {
        if (null != parentId) {
            updateChildIds(parentId, getChildIds(siteId, parentId));
            if (generateParent) {
                CmsCategory parent = getEntity(parentId);
                if (null != parent && null != parent.getParentId()) {
                    generateChildIds(siteId, parent.getParentId(), true);
                }
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
                entity.setCode(CommonUtils.keep(new StringBuilder(entity.getCode()).append("-").append(UUID.randomUUID().toString()).toString(), 50));
                entity.setDisabled(true);
                entityList.add(entity);
                generateChildIds(entity.getSiteId(), entity.getParentId());
            }
        }
        return entityList;
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

    @Resource
    private CmsCategoryDao dao;
}
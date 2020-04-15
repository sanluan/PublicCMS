package com.publiccms.logic.service.cms;

import static org.springframework.util.StringUtils.arrayToDelimitedString;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.publiccms.common.api.Config;
import com.publiccms.common.base.BaseService;
import com.publiccms.common.constants.CommonConstants;
import com.publiccms.common.handler.FacetPageHandler;
import com.publiccms.common.handler.PageHandler;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.common.tools.ExtendUtils;
import com.publiccms.common.tools.HtmlUtils;
import com.publiccms.entities.cms.CmsCategory;
import com.publiccms.entities.cms.CmsContent;
import com.publiccms.entities.cms.CmsContentAttribute;
import com.publiccms.entities.sys.SysExtendField;
import com.publiccms.entities.sys.SysUser;
import com.publiccms.logic.dao.cms.CmsCategoryDao;
import com.publiccms.logic.dao.cms.CmsContentDao;
import com.publiccms.logic.service.sys.SysExtendFieldService;
import com.publiccms.logic.service.sys.SysExtendService;
import com.publiccms.views.pojo.entities.CmsContentStatistics;
import com.publiccms.views.pojo.entities.CmsModel;
import com.publiccms.views.pojo.model.CmsContentParameters;
import com.publiccms.views.pojo.query.CmsContentQuery;

/**
 *
 * CmsContentService
 * 
 */
@Service
@Transactional
public class CmsContentService extends BaseService<CmsContent> {
    @Autowired
    private SysExtendService extendService;
    @Autowired
    private SysExtendFieldService extendFieldService;
    @Autowired
    private CmsTagService tagService;
    @Autowired
    private CmsContentFileService contentFileService;
    @Autowired
    private CmsContentAttributeService attributeService;
    @Autowired
    private CmsContentRelatedService cmsContentRelatedService;
    private static String[] DICTIONARY_INPUT_TYPES = { Config.INPUTTYPE_NUMBER, Config.INPUTTYPE_BOOLEAN, Config.INPUTTYPE_USER,
            Config.INPUTTYPE_CONTENT, Config.INPUTTYPE_CATEGORY, Config.INPUTTYPE_DICTIONARY, Config.INPUTTYPE_CATEGORYTYPE,
            Config.INPUTTYPE_TAGTYPE };

    private static String[] FULLTEXT_SEARCHABLE_EDITOR = { "kindeditor", "ckeditor", "editor" };
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
     * 
     */
    public static final int STATUS_REJECT = 3;

    /**
     * 
     */
    public static final Integer[] STATUS_NORMAL_ARRAY = new Integer[] { STATUS_NORMAL };

    /**
     * @param projection
     * @param fuzzy
     * @param highlight
     * @param siteId
     * @param categoryId
     * @param containChild
     * @param categoryIds
     * @param modelIds
     * @param text
     * @param fields
     * @param tagIds
     * @param dictionaryValues
     * @param preTag
     * @param postTag
     * @param startPublishDate
     * @param endPublishDate
     * @param expiryDate
     * @param orderField
     * @param pageIndex
     * @param pageSize
     * @return results page
     */
    @Transactional(readOnly = true)
    public PageHandler query(boolean projection, boolean fuzzy, boolean highlight, Short siteId, String text, String[] fields,
            Long[] tagIds, Integer categoryId, Boolean containChild, Integer[] categoryIds, String[] modelIds,
            String[] dictionaryValues, String preTag, String postTag, Date startPublishDate, Date endPublishDate, Date expiryDate,
            String orderField, Integer pageIndex, Integer pageSize) {
        return dao.query(projection, fuzzy, highlight, siteId, getCategoryIds(containChild, categoryId, categoryIds), modelIds,
                text, fields, arrayToDelimitedString(tagIds, CommonConstants.BLANK_SPACE), dictionaryValues, preTag, postTag,
                startPublishDate, endPublishDate, expiryDate, orderField, pageIndex, pageSize);
    }

    /**
     * @param projection
     * @param fuzzy
     * @param highlight 
     * @param siteId
     * @param categoryIds
     * @param modelIds
     * @param text
     * @param fields
     * @param tagIds
     * @param dictionaryValues
     * @param startPublishDate
     * @param endPublishDate
     * @param expiryDate
     * @param preTag
     * @param postTag
     * @param orderField
     * @param pageIndex
     * @param pageSize
     * @return results page
     */
    @Transactional(readOnly = true)
    public FacetPageHandler facetQuery(boolean projection, boolean fuzzy, boolean highlight, Short siteId, String text, String[] fields,
            Long[] tagIds, Integer[] categoryIds, String[] modelIds, String[] dictionaryValues, String preTag, String postTag,
            Date startPublishDate, Date endPublishDate, Date expiryDate, String orderField, Integer pageIndex, Integer pageSize) {
        return dao.facetQuery(projection, fuzzy, highlight, siteId, categoryIds, modelIds, text, fields,
                arrayToDelimitedString(tagIds, CommonConstants.BLANK_SPACE), dictionaryValues, preTag, postTag, startPublishDate,
                endPublishDate, expiryDate, orderField, pageIndex, pageSize);
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

    public CmsContent saveTagAndAttribute(Short siteId, Long userId, Long id, CmsContentParameters contentParameters,
            CmsModel cmsModel, Integer extendId, CmsContentAttribute attribute) {
        CmsContent entity = getEntity(id);
        if (null != entity) {
            Long[] tagIds = tagService.update(siteId, contentParameters.getTags());
            entity.setTagIds(arrayToDelimitedString(tagIds, CommonConstants.BLANK_SPACE));
            if (entity.isHasImages() || entity.isHasFiles()) {
                contentFileService.update(entity.getId(), userId, entity.isHasFiles() ? contentParameters.getFiles() : null,
                        entity.isHasImages() ? contentParameters.getImages() : null);// 更新保存图集，附件
            }
            String text = HtmlUtils.removeHtmlTag(attribute.getText());
            if (null != text) {
                attribute.setWordCount(text.length());
            }
            if (CommonUtils.empty(entity.getDescription())) {
                entity.setDescription(StringUtils.substring(text, 0, 150));
            }

            List<SysExtendField> modelExtendList = cmsModel.getExtendList();
            List<SysExtendField> categoryExtendList = null;
            Map<String, String> map = ExtendUtils.getExtentDataMap(contentParameters.getModelExtendDataList(), modelExtendList);
            if (null != extendId && null != extendService.getEntity(extendId)) {
                categoryExtendList = extendFieldService.getList(extendId);
                Map<String, String> categoryMap = ExtendUtils.getSysExtentDataMap(contentParameters.getCategoryExtendDataList(),
                        categoryExtendList);
                if (CommonUtils.notEmpty(map)) {
                    map.putAll(categoryMap);
                } else {
                    map = categoryMap;
                }
            }
            if (CommonUtils.notEmpty(map)) {
                StringBuilder sb = new StringBuilder();
                if (cmsModel.isSearchable() && CommonUtils.notEmpty(text)) {
                    sb.append(text).append(CommonConstants.BLANK_SPACE);
                }
                List<String> dictionaryValueList = new ArrayList<>();
                dealExtend(modelExtendList, dictionaryValueList, map, sb);
                dealExtend(categoryExtendList, dictionaryValueList, map, sb);
                if (CommonUtils.notEmpty(dictionaryValueList)) {
                    String[] dictionaryValues = dictionaryValueList.toArray(new String[dictionaryValueList.size()]);
                    entity.setDictionaryValues(arrayToDelimitedString(dictionaryValues, CommonConstants.BLANK_SPACE));
                }
                attribute.setData(ExtendUtils.getExtendString(map));
                attribute.setSearchText(sb.toString());
            } else {
                attribute.setData(null);
                entity.setDictionaryValues(null);
                if (cmsModel.isSearchable()) {
                    attribute.setSearchText(text);
                } else {
                    attribute.setSearchText(null);
                }
            }

            attributeService.updateAttribute(id, attribute);// 更新保存扩展字段，文本字段
            if (CommonUtils.notEmpty(contentParameters.getContentRelateds())) {
                cmsContentRelatedService.update(id, userId, contentParameters.getContentRelateds());// 更新保存推荐内容
            }
        }
        return entity;
    }

    private void dealExtend(List<SysExtendField> extendList, List<String> dictionaryValueList, Map<String, String> map,
            StringBuilder sb) {
        if (CommonUtils.notEmpty(extendList)) {
            for (SysExtendField extendField : extendList) {
                if (extendField.isSearchable()) {
                    if (ArrayUtils.contains(DICTIONARY_INPUT_TYPES, extendField.getInputType())) {
                        if (Config.INPUTTYPE_DICTIONARY.equals(extendField.getInputType())) {
                            String[] values = StringUtils.split(map.get(extendField.getId().getCode()), CommonConstants.COMMA);
                            if (CommonUtils.notEmpty(values)) {
                                for (String value : values) {
                                    dictionaryValueList.add(extendField.getId().getCode() + "_" + value);
                                }
                            }
                        } else {
                            String value = map.get(extendField.getId().getCode());
                            if (null != value) {
                                dictionaryValueList.add(extendField.getId().getCode() + "_" + value);
                            }
                        }
                    } else {
                        String value = map.get(extendField.getId().getCode());
                        if (null != value) {
                            if (ArrayUtils.contains(FULLTEXT_SEARCHABLE_EDITOR, extendField.getInputType())) {
                                value = HtmlUtils.removeHtmlTag(value);
                            }
                            sb.append(value).append(CommonConstants.BLANK_SPACE);
                        }
                    }
                }
            }
        }
    }

    /**
     * @param siteId
     * @param user
     * @param ids
     * @return results list
     */
    public List<CmsContent> refresh(short siteId, SysUser user, Serializable[] ids) {
        List<CmsContent> entityList = new ArrayList<>();
        List<CmsContent> list = getEntitys(ids);
        Collections.reverse(list);
        for (CmsContent entity : list) {
            if (null != entity && STATUS_NORMAL == entity.getStatus() && siteId == entity.getSiteId()
                    && (user.isOwnsAllContent() || entity.getUserId() == user.getId())) {
                Date now = CommonUtils.getDate();
                if (now.after(entity.getPublishDate())) {
                    entity.setPublishDate(now);
                    entityList.add(entity);
                }
            }
        }
        return entityList;
    }

    /**
     * @param siteId
     * @param user
     * @param ids
     * @return results list
     */
    public List<CmsContent> check(short siteId, SysUser user, Serializable[] ids) {
        List<CmsContent> entityList = new ArrayList<>();
        for (CmsContent entity : getEntitys(ids)) {
            if (null != entity && siteId == entity.getSiteId() && STATUS_DRAFT != entity.getStatus()
                    && STATUS_NORMAL != entity.getStatus() && (user.isOwnsAllContent() || entity.getUserId() == user.getId())) {
                entity.setStatus(STATUS_NORMAL);
                entity.setCheckUserId(user.getId());
                entity.setCheckDate(CommonUtils.getDate());
                entityList.add(entity);
            }
        }
        return entityList;
    }

    /**
     * @param siteId
     * @param user
     * @param ids
     * @return results list
     */
    public List<CmsContent> reject(short siteId, SysUser user, Serializable[] ids) {
        List<CmsContent> entityList = new ArrayList<>();
        for (CmsContent entity : getEntitys(ids)) {
            if (null != entity && siteId == entity.getSiteId() && STATUS_PEND == entity.getStatus()
                    && (user.isOwnsAllContent() || entity.getUserId() == user.getId())) {
                entity.setStatus(STATUS_REJECT);
                entity.setCheckUserId(user.getId());
                entity.setCheckDate(CommonUtils.getDate());
                entityList.add(entity);
            }
        }
        return entityList;
    }

    /**
     * @param siteId
     * @param user
     * @param ids
     * @return results list
     */
    public List<CmsContent> uncheck(short siteId, SysUser user, Serializable[] ids) {
        List<CmsContent> entityList = new ArrayList<>();
        for (CmsContent entity : getEntitys(ids)) {
            if (siteId == entity.getSiteId() && STATUS_NORMAL == entity.getStatus()
                    && (user.isOwnsAllContent() || entity.getUserId() == user.getId())) {
                entity.setStatus(STATUS_PEND);
                entityList.add(entity);
            }
        }
        return entityList;
    }

    /**
     * @param siteId
     * @param id
     * @param contentParameters
     * @param categoryList
     * @param cmsModel
     * @param category
     * @param attribute
     */
    public void saveQuote(short siteId, Serializable id, CmsContentParameters contentParameters, List<CmsCategory> categoryList,
            CmsModel cmsModel, CmsCategory category, CmsContentAttribute attribute) {
        CmsContent entity = getEntity(id);
        if (CommonUtils.notEmpty(categoryList) && null != entity) {
            for (CmsCategory c : categoryList) {
                if (null != c && !category.getId().equals(c.getId())) {
                    CmsContent quote = new CmsContent(entity.getSiteId(), entity.getTitle(), entity.getUserId(), c.getId(),
                            entity.getModelId(), entity.isCopied(), true, entity.isHasImages(), entity.isHasFiles(),
                            entity.isHasStatic(), 0, 0, 0, 0, entity.getPublishDate(), entity.getCreateDate(), 0,
                            entity.getStatus(), false);
                    quote.setUrl(entity.getUrl());
                    quote.setDescription(entity.getDescription());
                    quote.setAuthor(entity.getAuthor());
                    quote.setCover(entity.getCover());
                    quote.setEditor(entity.getEditor());
                    quote.setExpiryDate(entity.getExpiryDate());
                    quote.setQuoteContentId(entity.getId());
                    quote.setCheckUserId(entity.getCheckUserId());
                    quote.setCheckDate(entity.getCheckDate());
                    quote.setPublishDate(entity.getPublishDate());
                    save(quote);
                }
            }
        }
    }

    /**
     * @param siteId
     * @param id
     * @param contentParameters
     * @param cmsModel
     * @param category
     * @param attribute
     */
    @SuppressWarnings("unchecked")
    public void updateQuote(short siteId, Serializable id, CmsContentParameters contentParameters, CmsModel cmsModel,
            CmsCategory category, CmsContentAttribute attribute) {
        CmsContent entity = getEntity(id);
        if (null != entity) {
            CmsContentQuery query = new CmsContentQuery();
            query.setSiteId(siteId);
            query.setQuoteId(entity.getId());
            for (CmsContent quote : (List<CmsContent>) getPage(query, null, null, null, null, null).getList()) {
                if (null != contentParameters.getContentIds() && contentParameters.getContentIds().contains(quote.getId())) {
                    quote.setUrl(entity.getUrl());
                    quote.setTitle(entity.getTitle());
                    quote.setDescription(entity.getDescription());
                    quote.setAuthor(entity.getAuthor());
                    quote.setCover(entity.getCover());
                    quote.setEditor(entity.getEditor());
                    quote.setExpiryDate(entity.getExpiryDate());
                    quote.setStatus(entity.getStatus());
                    quote.setCheckUserId(entity.getCheckUserId());
                    quote.setCheckDate(entity.getCheckDate());
                    quote.setPublishDate(entity.getPublishDate());
                } else {
                    delete(quote.getId());
                }
            }
        }
    }

    /**
     * @param entitys
     */
    public void updateStatistics(Collection<CmsContentStatistics> entitys) {
        for (CmsContentStatistics entityStatistics : entitys) {
            CmsContent entity = getEntity(entityStatistics.getId());
            if (null != entity) {
                entity.setClicks(entity.getClicks() + entityStatistics.getClicks());
                entity.setScores(entity.getScores() + entityStatistics.getScores());
            }
        }
    }

    /**
     * @param siteId
     * @param id
     * @param comments
     * @return
     */
    public CmsContent updateComments(short siteId, Serializable id, int comments) {
        CmsContent entity = getEntity(id);
        if (null != entity && siteId == entity.getSiteId()) {
            entity.setComments(entity.getComments() + comments);
        }
        return entity;
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
     * @param user
     * @param ids
     * @return list of data deleted
     */
    @SuppressWarnings("unchecked")
    public List<CmsContent> delete(short siteId, SysUser user, Serializable[] ids) {
        List<CmsContent> entityList = new ArrayList<>();
        for (CmsContent entity : getEntitys(ids)) {
            if (siteId == entity.getSiteId() && !entity.isDisabled()
                    && (user.isOwnsAllContent() || entity.getUserId() == user.getId())) {
                if (null == entity.getParentId()) {
                    CmsContentQuery query = new CmsContentQuery();
                    query.setSiteId(siteId);
                    query.setQuoteId(entity.getId());
                    for (CmsContent quote : (List<CmsContent>) getPage(query, null, null, null, null, null).getList()) {
                        quote.setDisabled(true);
                    }
                } else {
                    updateChilds(entity.getParentId(), -1);
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
            }
        } else {
            categoryIds = new Integer[] { categoryId };
        }
        return categoryIds;
    }

    /**
     * @param siteId
     * @param ids
     * @return
     */
    public List<CmsContent> recycle(short siteId, Serializable[] ids) {
        List<CmsContent> entityList = new ArrayList<>();
        for (CmsContent entity : getEntitys(ids)) {
            if (siteId == entity.getSiteId() && entity.isDisabled()) {
                entity.setDisabled(false);
                entityList.add(entity);
                if (null != entity.getParentId()) {
                    updateChilds(entity.getParentId(), 1);
                }
            }
        }
        return entityList;
    }

    /**
     * @param siteId
     * @param ids
     */
    public void realDelete(Short siteId, Long[] ids) {
        for (CmsContent entity : getEntitys(ids)) {
            if (siteId == entity.getSiteId() && entity.isDisabled()) {
                delete(entity.getId());
                attributeService.delete(entity.getId());
            }
        }
    }

    @Autowired
    private CmsContentDao dao;
    @Autowired
    private CmsCategoryDao categoryDao;
}
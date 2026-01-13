package com.publiccms.logic.service.cms;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.publiccms.common.base.BaseService;
import com.publiccms.common.tools.CmsLangUtils;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.common.tools.ExtendUtils;
import com.publiccms.entities.cms.CmsCategory;
import com.publiccms.entities.cms.CmsCategoryLang;
import com.publiccms.logic.component.config.SiteAttributeComponent;
import com.publiccms.logic.dao.cms.CmsCategoryLangDao;
import com.publiccms.views.pojo.entities.CmsCategoryType;
import com.publiccms.views.pojo.model.CmsCategoryLangListParameters;
import com.publiccms.views.pojo.model.CmsCategoryLangParameters;

/**
 *
 * CmsCategoryLangService
 * 
 */
@Service
@Transactional
public class CmsCategoryLangService extends BaseService<CmsCategoryLang> {
    private String[] ignoreProperties = new String[] { "id", "url" };
    @Resource
    private CmsCategoryService categoryService;
    @Resource
    private CmsEditorHistoryService editorHistoryService;
    @Resource
    protected SiteAttributeComponent siteAttributeComponent;

    /**
     * @param categoryId
     * @return data list
     */
    public List<CmsCategoryLang> getList(Integer categoryId) {
        return dao.getList(categoryId);
    }

    /**
     * @param siteId
     * @param sitePath
     * @param category
     * @param userId
     * @param categoryType
     * @param categoryLangListParameters
     */
    public void save(short siteId, String sitePath, CmsCategory category, Long userId, CmsCategoryType categoryType,
            CmsCategoryLangListParameters categoryLangListParameters) {
        if (null != categoryLangListParameters && null != categoryLangListParameters.getCategoryLangList()) {
            for (CmsCategoryLangParameters langParameter : categoryLangListParameters.getCategoryLangList()) {
                CmsCategoryLang entity = langParameter.getEntity();
                entity.getId().setCategoryId(category.getId());
                if (null != categoryType && CommonUtils.notEmpty(categoryType.getExtendList())) {
                    entity.setData(
                            ExtendUtils.getExtendString(langParameter.getExtendData(), sitePath, categoryType.getExtendList()));
                } else {
                    entity.setData(null);
                }

                CmsCategoryLang oldEntity = getEntity(entity.getId());
                if (null != oldEntity) {
                    update(entity.getId(), entity, ignoreProperties);
                } else {
                    if (category.isHasStatic()) {
                        String defaultLang = siteAttributeComponent.getDefaultLanguage(siteId);
                        String fullStaticFilePath = CmsLangUtils.getFullFilepath(category.getUrl(), entity.getId().getLang(),
                                defaultLang);
                        entity.setUrl(fullStaticFilePath);
                    }
                    save(entity);
                }
                saveEditorHistory(oldEntity, siteId, entity.getId().getCategoryId(), entity.getId().getLang(), userId,
                        categoryType, langParameter.getExtendData());
            }
        }
    }

    /**
     * @param id
     * @param url
     */
    public void updateUrl(Serializable id, String url) {
        CmsCategoryLang entity = getEntity(id);
        if (null != entity) {
            entity.setUrl(url);
        }
    }

    private void saveEditorHistory(CmsCategoryLang oldEntity, short siteId, int entityId, String lang, long userId,
            CmsCategoryType categoryType, Map<String, String> map) {
        if (null != oldEntity && (CommonUtils.notEmpty(oldEntity.getData()) && null != categoryType
                && CommonUtils.notEmpty(categoryType.getExtendList()))) {
            Map<String, String> oldMap = ExtendUtils.getExtendMap(oldEntity.getData());
            editorHistoryService.saveHistory(siteId, userId, CmsEditorHistoryService.ITEM_TYPE_CATEGORY_EXTEND,
                    String.valueOf(entityId), lang, oldMap, map, categoryType.getExtendList());
        }
    }

    @Resource
    private CmsCategoryLangDao dao;
}
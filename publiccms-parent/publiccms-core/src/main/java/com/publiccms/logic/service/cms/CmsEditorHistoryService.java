package com.publiccms.logic.service.cms;


import org.apache.commons.lang3.ArrayUtils;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.publiccms.common.api.Config;
import com.publiccms.common.base.BaseService;
import com.publiccms.common.handler.PageHandler;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.entities.cms.CmsEditorHistory;
import com.publiccms.entities.sys.SysExtendField;
import com.publiccms.logic.dao.cms.CmsEditorHistoryDao;

import jakarta.annotation.Resource;

/**
 *
 * CmsContentTextHistoryService
 * 
 */
@Service
@Transactional
public class CmsEditorHistoryService extends BaseService<CmsEditorHistory> {
    public static final String ITEM_TYPE_CONTENT = "content";
    public static final String ITEM_TYPE_CONTENT_EXTEND = "contentExtend";
    public static final String ITEM_TYPE_CATEGORY_EXTEND = "categoryExtend";
    public static final String ITEM_TYPE_PLACE_EXTEND = "placeExtend";
    public static final String ITEM_TYPE_CONFIG_DATA = "configData";
    public static final String ITEM_TYPE_METADATA_EXTEND = "metadataExtend";

    /**
     * @param itemType
     * @param itemId
     * @param fieldName
     * @param lang 
     * @param userId
     * @param orderType
     * @param pageIndex
     * @param pageSize
     * @return results page
     */
    @Transactional(readOnly = true)
    public PageHandler getPage(String itemType, String itemId, String fieldName, String lang, Long userId, String orderType,
            Integer pageIndex, Integer pageSize) {
        return dao.getPage(itemType, itemId, fieldName, lang, userId, orderType, pageIndex, pageSize);
    }

    /**
     * save editor history
     * 
     * @param siteId
     * @param userId
     * @param itemType
     * @param itemId
     * @param lang
     * @param oldMap
     * @param extendData
     * @param getExtendFieldList
     */
    public void saveHistory(short siteId, long userId, String itemType, String itemId, String lang, Map<String, String> oldMap,
            Map<String, String> extendData, List<SysExtendField> getExtendFieldList) {
        if (CommonUtils.notEmpty(oldMap) && CommonUtils.notEmpty(getExtendFieldList)) {
            for (SysExtendField extendField : getExtendFieldList) {
                if (ArrayUtils.contains(Config.INPUT_TYPE_EDITORS, extendField.getInputType())
                        && (CommonUtils.notEmpty(oldMap.get(extendField.getId().getCode()))
                                && (CommonUtils.empty(extendData) || !oldMap.get(extendField.getId().getCode())
                                        .equals(extendData.get(extendField.getId().getCode()))))) {
                    CmsEditorHistory history = new CmsEditorHistory(siteId, itemType, itemId, extendField.getId().getCode(), lang,
                            CommonUtils.now(), userId, oldMap.get(extendField.getId().getCode()));
                    save(history);

                }
            }
        }
    }

    @Resource
    private CmsEditorHistoryDao dao;

}
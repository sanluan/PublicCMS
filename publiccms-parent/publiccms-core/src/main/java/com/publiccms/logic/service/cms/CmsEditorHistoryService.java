package com.publiccms.logic.service.cms;

// Generated 2022-5-10 by com.publiccms.common.generator.SourceGenerator

import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.publiccms.common.base.BaseService;
import com.publiccms.common.handler.PageHandler;
import com.publiccms.entities.cms.CmsEditorHistory;
import com.publiccms.logic.dao.cms.CmsEditorHistoryDao;

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
     * @param userId
     * @param orderType
     * @param pageIndex
     * @param pageSize
     * @return results page
     */
    @Transactional(readOnly = true)
    public PageHandler getPage(String itemType, String itemId, String fieldName, Long userId, String orderType, Integer pageIndex,
            Integer pageSize) {
        return dao.getPage(itemType, itemId, fieldName, userId, orderType, pageIndex, pageSize);
    }

    @Resource
    private CmsEditorHistoryDao dao;

}
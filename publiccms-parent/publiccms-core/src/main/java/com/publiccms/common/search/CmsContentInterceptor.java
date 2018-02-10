package com.publiccms.common.search;

import org.hibernate.search.indexes.interceptor.EntityIndexingInterceptor;
import org.hibernate.search.indexes.interceptor.IndexingOverride;
import com.publiccms.entities.cms.CmsContent;
import com.publiccms.logic.service.cms.CmsContentService;

/**
 *
 * CmsContentInterceptor
 * 
 */
public class CmsContentInterceptor implements EntityIndexingInterceptor<CmsContent> {
    @Override
    public IndexingOverride onAdd(CmsContent entity) {
        if (CmsContentService.STATUS_NORMAL == entity.getStatus() && !entity.isDisabled()) {
            return IndexingOverride.APPLY_DEFAULT;
        }
        return IndexingOverride.SKIP;
    }
    
    @Override
    public IndexingOverride onDelete(CmsContent entity) {
        return IndexingOverride.APPLY_DEFAULT;
    }

    @Override
    public IndexingOverride onUpdate(CmsContent entity) {
        if (CmsContentService.STATUS_NORMAL == entity.getStatus() && !entity.isDisabled()) {
            return IndexingOverride.UPDATE;
        }
        return IndexingOverride.REMOVE;
    }

    @Override
    public IndexingOverride onCollectionUpdate(CmsContent entity) {
        return onUpdate(entity);
    }
}
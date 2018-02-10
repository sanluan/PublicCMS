package com.publiccms.logic.dao.cms;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

// Generated 2015-5-8 16:50:23 by com.publiccms.common.source.SourceGenerator

import org.springframework.stereotype.Repository;

import com.publiccms.common.base.BaseDao;
import com.publiccms.common.handler.QueryHandler;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.entities.cms.CmsContentAttribute;

/**
 *
 * CmsContentAttributeDao
 * 
 */
@Repository
public class CmsContentAttributeDao extends BaseDao<CmsContentAttribute> {
    
    /**
     * @param ids
     * @return results list
     */
    @SuppressWarnings("unchecked")
    public List<CmsContentAttribute> getEntitysWithoutText(Serializable[] ids) {
        if (CommonUtils.notEmpty(ids)) {
            QueryHandler queryHandler = getQueryHandler(
                    "select new CmsContentAttribute(contentId, source, sourceUrl, data, wordCount) from CmsContentAttribute bean");
            queryHandler.condition("bean.contentId in (:ids)").setParameter("ids", ids);
            return (List<CmsContentAttribute>) getList(queryHandler);
        }
        return Collections.emptyList();
    }

    @Override
    protected CmsContentAttribute init(CmsContentAttribute entity) {
        if (CommonUtils.empty(entity.getSource())) {
            entity.setSource(null);
        }
        if (CommonUtils.empty(entity.getSourceUrl())) {
            entity.setSourceUrl(null);
        }
        return entity;
    }

}
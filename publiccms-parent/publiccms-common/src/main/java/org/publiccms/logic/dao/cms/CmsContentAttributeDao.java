package org.publiccms.logic.dao.cms;

import static com.publiccms.common.tools.CommonUtils.empty;
import static com.publiccms.common.tools.CommonUtils.notEmpty;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.publiccms.entities.cms.CmsContentAttribute;

// Generated 2015-5-8 16:50:23 by com.publiccms.common.source.SourceGenerator

import org.springframework.stereotype.Repository;

import com.publiccms.common.base.BaseDao;
import com.publiccms.common.handler.QueryHandler;

/**
 *
 * CmsContentAttributeDao
 * 
 */
@Repository
public class CmsContentAttributeDao extends BaseDao<CmsContentAttribute> {
    
    /**
     * @param ids
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<CmsContentAttribute> getEntitysWithoutText(Serializable[] ids) {
        if (notEmpty(ids)) {
            QueryHandler queryHandler = getQueryHandler(
                    "select new CmsContentAttribute(contentId, source, sourceUrl, data, wordCount) from CmsContentAttribute bean");
            queryHandler.condition("bean.contentId in (:ids)").setParameter("ids", ids);
            return (List<CmsContentAttribute>) getList(queryHandler);
        }
        return new ArrayList<>();
    }

    @Override
    protected CmsContentAttribute init(CmsContentAttribute entity) {
        if (empty(entity.getSource())) {
            entity.setSource(null);
        }
        if (empty(entity.getSourceUrl())) {
            entity.setSourceUrl(null);
        }
        return entity;
    }

}
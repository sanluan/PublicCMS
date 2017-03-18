package com.publiccms.logic.dao.cms;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

// Generated 2015-5-8 16:50:23 by com.sanluan.common.source.SourceGenerator

import org.springframework.stereotype.Repository;

import com.publiccms.entities.cms.CmsContentAttribute;
import com.sanluan.common.base.BaseDao;
import com.sanluan.common.handler.QueryHandler;

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
        return new ArrayList<CmsContentAttribute>();
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
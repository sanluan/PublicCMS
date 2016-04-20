package com.publiccms.logic.dao.cms;

// Generated 2015-5-8 16:50:23 by com.sanluan.common.source.SourceMaker

import org.springframework.stereotype.Repository;

import com.publiccms.entities.cms.CmsModel;
import com.sanluan.common.base.BaseDao;
import com.sanluan.common.handler.PageHandler;
import com.sanluan.common.handler.QueryHandler;

@Repository
public class CmsModelDao extends BaseDao<CmsModel> {
    public PageHandler getPage(Integer siteId, Integer parentId, Boolean hasChild, Boolean onlyUrl, Boolean hasImages,
            Boolean hasFiles, Boolean disabled, Integer pageIndex, Integer pageSize) {
        QueryHandler queryHandler = getQueryHandler("from CmsModel bean");
        if (notEmpty(siteId)) {
            queryHandler.condition("bean.siteId = :siteId").setParameter("siteId", siteId);
        }
        if (notEmpty(parentId)) {
            queryHandler.condition("bean.parentId = :parentId").setParameter("parentId", parentId);
        } else {
            queryHandler.condition("bean.parentId is null");
        }
        if (notEmpty(hasChild)) {
            queryHandler.condition("bean.hasChild = :hasChild").setParameter("hasChild", hasChild);
        }
        if (notEmpty(onlyUrl)) {
            queryHandler.condition("bean.onlyUrl = :onlyUrl").setParameter("onlyUrl", onlyUrl);
        }
        if (notEmpty(hasImages)) {
            queryHandler.condition("bean.hasImages = :hasImages").setParameter("hasImages", hasImages);
        }
        if (notEmpty(hasFiles)) {
            queryHandler.condition("bean.hasFiles = :hasFiles").setParameter("hasFiles", hasFiles);
        }
        if (notEmpty(disabled)) {
            queryHandler.condition("bean.disabled = :disabled").setParameter("disabled", disabled);
        }
        queryHandler.order("bean.id desc");
        return getPage(queryHandler, pageIndex, pageSize);
    }

    @Override
    protected CmsModel init(CmsModel entity) {
        return entity;
    }

}
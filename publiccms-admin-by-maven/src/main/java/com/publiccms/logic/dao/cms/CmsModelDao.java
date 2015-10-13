package com.publiccms.logic.dao.cms;

// Generated 2015-5-8 16:50:23 by SourceMaker

import org.springframework.stereotype.Repository;

import com.publiccms.entities.cms.CmsModel;
import com.sanluan.common.base.BaseDao;
import com.sanluan.common.handler.PageHandler;
import com.sanluan.common.handler.QueryHandler;

@Repository
public class CmsModelDao extends BaseDao<CmsModel> {
    public PageHandler getPage(Integer parentId, Boolean hasChild, Boolean isUrl, Boolean isImages,Boolean isPart, Boolean disabled,
            Integer pageIndex, Integer pageSize) {
        QueryHandler queryHandler = getQueryHandler("from CmsModel bean");
        if (notEmpty(parentId)) {
            queryHandler.condition("bean.parentId = :parentId").setParameter("parentId", parentId);
        } else {
            queryHandler.condition("bean.parentId is null");
        }
        if (notEmpty(hasChild)) {
            queryHandler.condition("bean.hasChild = :hasChild").setParameter("hasChild", hasChild);
        }
        if (notEmpty(isUrl)) {
            queryHandler.condition("bean.isUrl = :isUrl").setParameter("isUrl", isUrl);
        }
        if (notEmpty(isImages)) {
            queryHandler.condition("bean.isImages = :isImages").setParameter("isImages", isImages);
        }
        if (notEmpty(isPart)) {
            queryHandler.condition("bean.isPart = :isPart").setParameter("isPart", isPart);
        }
        if (notEmpty(disabled)) {
            queryHandler.condition("bean.disabled = :disabled").setParameter("disabled", disabled);
        }
        queryHandler.append("order by bean.id desc");
        return getPage(queryHandler, pageIndex, pageSize);
    }

    @Override
    protected CmsModel init(CmsModel entity) {
        return entity;
    }

    @Override
    protected Class<CmsModel> getEntityClass() {
        return CmsModel.class;
    }

}
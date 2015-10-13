package com.publiccms.logic.dao.cms;

// Generated 2015-5-8 16:50:23 by SourceMaker

import org.springframework.stereotype.Repository;

import com.publiccms.entities.cms.CmsExtend;
import com.sanluan.common.base.BaseDao;
import com.sanluan.common.handler.PageHandler;
import com.sanluan.common.handler.QueryHandler;

@Repository
public class CmsExtendDao extends BaseDao<CmsExtend> {
    public PageHandler getPage(Integer itemType, Integer itemId, Integer extendType, Boolean isCustom, Integer pageIndex, Integer pageSize) {
        QueryHandler queryHandler = getQueryHandler("from CmsExtend bean");
        if (notEmpty(itemType)) {
            queryHandler.condition("bean.itemType = :itemType").setParameter("itemType", itemType);
        }
        if (notEmpty(itemId)) {
            queryHandler.condition("bean.itemId = :itemId").setParameter("itemId", itemId);
        }
        if (notEmpty(extendType)) {
            queryHandler.condition("bean.extendType = :extendType").setParameter("extendType", extendType);
        }
        if (notEmpty(isCustom)) {
            queryHandler.condition("bean.isCustom = :isCustom").setParameter("isCustom", isCustom);
        }
        queryHandler.append("order by bean.id desc");
        return getPage(queryHandler, pageIndex, pageSize);
    }

    @Override
    protected CmsExtend init(CmsExtend entity) {
        return entity;
    }

    @Override
    protected Class<CmsExtend> getEntityClass() {
        return CmsExtend.class;
    }

}
package org.publiccms.logic.dao.home;

import org.publiccms.entities.home.HomeGroupApply;

// Generated 2016-11-19 9:58:46 by com.publiccms.common.source.SourceGenerator

import org.springframework.stereotype.Repository;

import com.publiccms.common.base.BaseDao;
import com.publiccms.common.handler.PageHandler;
import com.publiccms.common.handler.QueryHandler;

/**
 *
 * HomeGroupApplyDao
 * 
 */
@Repository
public class HomeGroupApplyDao extends BaseDao<HomeGroupApply> {

    /**
     * @param disabled
     * @param pageIndex
     * @param pageSize
     * @return
     */
    public PageHandler getPage(Boolean disabled, Integer pageIndex, Integer pageSize) {
        QueryHandler queryHandler = getQueryHandler("from HomeGroupApply bean");
        if (notEmpty(disabled)) {
            queryHandler.condition("bean.disabled = :disabled").setParameter("disabled", disabled);
        }
        queryHandler.order("bean.id desc");
        return getPage(queryHandler, pageIndex, pageSize);
    }

    @Override
    protected HomeGroupApply init(HomeGroupApply entity) {
        if (empty(entity.getCreateDate())) {
            entity.setCreateDate(getDate());
        }
        return entity;
    }

}
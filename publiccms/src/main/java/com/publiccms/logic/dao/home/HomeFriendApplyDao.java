package com.publiccms.logic.dao.home;

// Generated 2016-11-19 9:58:46 by com.sanluan.common.source.SourceGenerator


import org.springframework.stereotype.Repository;

import com.publiccms.entities.home.HomeFriendApply;
import com.sanluan.common.base.BaseDao;
import com.sanluan.common.handler.PageHandler;
import com.sanluan.common.handler.QueryHandler;

@Repository
public class HomeFriendApplyDao extends BaseDao<HomeFriendApply> {
    public PageHandler getPage(
                Integer pageIndex, Integer pageSize) {
        QueryHandler queryHandler = getQueryHandler("from HomeFriendApply bean");
        queryHandler.order("bean.id desc");
        return getPage(queryHandler, pageIndex, pageSize);
    }

    @Override
    protected HomeFriendApply init(HomeFriendApply entity) {
        if (empty(entity.getCreateDate())) {
            entity.setCreateDate(getDate());
        }
        return entity;
    }

}
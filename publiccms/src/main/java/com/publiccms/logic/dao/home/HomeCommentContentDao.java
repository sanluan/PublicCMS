package com.publiccms.logic.dao.home;

// Generated 2016-11-19 9:58:46 by com.sanluan.common.source.SourceGenerator


import org.springframework.stereotype.Repository;

import com.publiccms.entities.home.HomeCommentContent;
import com.sanluan.common.base.BaseDao;

@Repository
public class HomeCommentContentDao extends BaseDao<HomeCommentContent> {

    @Override
    protected HomeCommentContent init(HomeCommentContent entity) {
        return entity;
    }

}
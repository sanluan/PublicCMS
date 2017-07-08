package org.publiccms.logic.dao.home;

import org.publiccms.entities.home.HomeCommentContent;

// Generated 2016-11-19 9:58:46 by com.publiccms.common.source.SourceGenerator

import org.springframework.stereotype.Repository;

import com.publiccms.common.base.BaseDao;

/**
 *
 * HomeCommentContentDao
 * 
 */
@Repository
public class HomeCommentContentDao extends BaseDao<HomeCommentContent> {

    @Override
    protected HomeCommentContent init(HomeCommentContent entity) {
        return entity;
    }

}
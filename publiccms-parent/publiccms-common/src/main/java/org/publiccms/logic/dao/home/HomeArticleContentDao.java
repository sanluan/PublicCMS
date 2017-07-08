package org.publiccms.logic.dao.home;

import org.publiccms.entities.home.HomeArticleContent;

// Generated 2016-11-13 11:38:14 by com.publiccms.common.source.SourceGenerator

import org.springframework.stereotype.Repository;

import com.publiccms.common.base.BaseDao;

/**
 *
 * HomeArticleContentDao
 * 
 */
@Repository
public class HomeArticleContentDao extends BaseDao<HomeArticleContent> {

    @Override
    protected HomeArticleContent init(HomeArticleContent entity) {
        return entity;
    }

}
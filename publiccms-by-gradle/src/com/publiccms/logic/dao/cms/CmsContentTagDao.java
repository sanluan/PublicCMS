package com.publiccms.logic.dao.cms;

// Generated 2015-7-10 16:36:23 by com.sanluan.common.source.SourceMaker

import org.springframework.stereotype.Repository;

import com.publiccms.entities.cms.CmsContentTag;
import com.sanluan.common.base.BaseDao;
import com.sanluan.common.handler.PageHandler;
import com.sanluan.common.handler.QueryHandler;

@Repository
public class CmsContentTagDao extends BaseDao<CmsContentTag> {
    public PageHandler getPage(Long tagId, Long contentId, Long[] contentIds, Integer pageIndex, Integer pageSize) {
        QueryHandler queryHandler = getQueryHandler("from CmsContentTag bean");
        if (notEmpty(tagId)) {
            queryHandler.condition("bean.tagId = :tagId").setParameter("tagId", tagId);
        }
        if (notEmpty(contentId)) {
            queryHandler.condition("bean.contentId = :contentId").setParameter("contentId", contentId);
        }
        if (notEmpty(contentIds)) {
            queryHandler.condition("bean.contentId not in (:contentIds)").setParameter("contentIds", contentIds);
        }
        queryHandler.order("bean.id desc");
        return getPage(queryHandler, pageIndex, pageSize);
    }

    public int delete(Long[] tagIds, Long[] contentIds) {
        if (notEmpty(tagIds) || notEmpty(contentIds)) {
            QueryHandler queryHandler = getDeleteQueryHandler("from CmsContentTag bean");
            if (notEmpty(tagIds)) {
                queryHandler.condition("bean.tagId in (:tagIds)").setParameter("tagIds", tagIds);
            }
            if (notEmpty(contentIds)) {
                queryHandler.condition("bean.contentId in (:contentIds)").setParameter("contentIds", contentIds);
            }
            return delete(queryHandler);
        }
        return 0;
    }

    @Override
    protected CmsContentTag init(CmsContentTag entity) {
        return entity;
    }

}
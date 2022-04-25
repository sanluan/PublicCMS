package com.publiccms.logic.service.cms;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.publiccms.common.annotation.CopyToDatasource;
import com.publiccms.common.base.BaseService;
import com.publiccms.common.handler.PageHandler;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.entities.cms.CmsComment;
import com.publiccms.entities.cms.CmsContent;
import com.publiccms.logic.dao.cms.CmsCommentDao;

// Generated 2018-11-7 16:25:07 by com.publiccms.common.generator.SourceGenerator

import jakarta.annotation.Resource;

/**
 *
 * CmsCommentService
 * 
 */
@Service
@Transactional
public class CmsCommentService extends BaseService<CmsComment> {
    /**
     * 
     */
    public static final int STATUS_NORMAL = 1;
    /**
     * 
     */
    public static final int STATUS_PEND = 2;

    /**
     * 
     * @param siteId
     * @param userId
     * @param replyId
     * @param emptyReply
     * @param replyUserId
     * @param contentId
     * @param checkUserId
     * @param status
     * @param disabled
     * @param orderField
     * @param orderType
     * @param pageIndex
     * @param pageSize
     * @return results page
     */
    @Transactional(readOnly = true)
    public PageHandler getPage(Short siteId, Long userId, Long replyId, Boolean emptyReply, Long replyUserId, Long contentId,
            Long checkUserId, Integer status, Boolean disabled, String orderField, String orderType, Integer pageIndex,
            Integer pageSize) {
        return dao.getPage(siteId, userId, replyId, emptyReply, replyUserId, contentId, checkUserId, status, disabled, orderField,
                orderType, pageIndex, pageSize);
    }

    /**
     * @param siteIds
     * @param pageIndex
     * @param pageSize
     * @return results page
     */
    @Transactional(readOnly = true)
    public PageHandler getPage(Short[] siteIds, Integer pageIndex, Integer pageSize) {
        return dao.getPage(siteIds, pageIndex, pageSize);
    }

    /**
     * @param siteId
     * @param entity
     */
    @CopyToDatasource
    public void save(short siteId, CmsComment entity) {
        save(entity);
    }

    /**
     * @param siteId
     * @param ids
     * @param userId
     * @return
     */
    @CopyToDatasource
    public Set<CmsContent> check(short siteId, Serializable[] ids, long userId) {
        Date now = CommonUtils.getDate();
        Set<CmsContent> contentSet = new HashSet<>();
        for (CmsComment entity : getEntitys(ids)) {
            if (siteId == entity.getSiteId() && STATUS_NORMAL != entity.getStatus()) {
                if (null != entity.getReplyId()) {
                    updateReplies(siteId, entity.getReplyId(), 1);
                }
                entity.setStatus(STATUS_NORMAL);
                entity.setCheckDate(now);
                entity.setCheckUserId(userId);
                CmsContent content = contentService.updateComments(siteId, entity.getContentId(), 1);
                if (null != content && !content.isDisabled()) {
                    contentSet.add(content);
                }
            }
        }
        return contentSet;
    }

    /**
     * @param siteId
     * @param ids
     * @return
     */
    @CopyToDatasource
    public Set<CmsContent> uncheck(short siteId, Serializable[] ids) {
        Set<CmsContent> contentSet = new HashSet<>();
        for (CmsComment entity : getEntitys(ids)) {
            if (siteId == entity.getSiteId() && STATUS_NORMAL == entity.getStatus()) {
                if (null != entity.getReplyId()) {
                    updateReplies(siteId, entity.getReplyId(), -1);
                }
                entity.setStatus(STATUS_PEND);
                CmsContent content = contentService.updateComments(siteId, entity.getContentId(), -1);
                if (null != content && !content.isDisabled()) {
                    contentSet.add(content);
                }
            }
        }
        return contentSet;
    }

    /**
     * @param siteId
     * @param id
     * @param replies
     * @return
     */
    @CopyToDatasource
    @Transactional
    public CmsComment updateReplies(short siteId, Serializable id, int replies) {
        CmsComment entity = getEntity(id);
        if (null != entity && siteId == entity.getSiteId()) {
            entity.setReplies(entity.getReplies() + replies);
        }
        return entity;
    }

    /**
     * @param siteId
     * @param id
     * @param scores
     * @return
     */
    @CopyToDatasource
    @Transactional
    public CmsComment updateScores(short siteId, Serializable id, int scores) {
        CmsComment entity = getEntity(id);
        if (null != entity && siteId == entity.getSiteId()) {
            entity.setScores(entity.getScores() + scores);
        }
        return entity;
    }

    /**
     * @param siteId
     * @param ids
     * @return
     */
    @CopyToDatasource
    public Set<CmsContent> delete(short siteId, Serializable[] ids) {
        Set<CmsContent> contentSet = new HashSet<>();
        for (CmsComment entity : getEntitys(ids)) {
            if (!entity.isDisabled()) {
                entity.setDisabled(true);
                if (STATUS_NORMAL == entity.getStatus()) {
                    contentSet.add(contentService.updateComments(siteId, entity.getContentId(), -1));
                }
            }
        }
        return contentSet;
    }

    /**
     * @param entityList
     * @return
     */
    public List<CmsComment> batchUpdate(List<CmsComment> entityList) {
        List<CmsComment> resultList = new ArrayList<>();
        if (CommonUtils.notEmpty(entityList)) {
            for (CmsComment entity : entityList) {
                if (null == update(entity.getId(), entity)) {
                    resultList.add(entity);
                }
            }
        }
        return resultList;
    }

    @Resource
    private CmsCommentDao dao;
    @Resource
    private CmsContentService contentService;
}
package com.publiccms.logic.service.cms;

import java.io.Serializable;
import java.util.Date;

// Generated 2018-11-7 16:25:07 by com.publiccms.common.generator.SourceGenerator

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.publiccms.common.base.BaseService;
import com.publiccms.common.handler.PageHandler;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.entities.cms.CmsComment;
import com.publiccms.logic.dao.cms.CmsCommentDao;

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
    public PageHandler getPage(Short siteId, Long userId, Long contentId, Long checkUserId, Integer status, Boolean disabled,
            String orderField, String orderType, Integer pageIndex, Integer pageSize) {
        return dao.getPage(siteId, userId, contentId, checkUserId, status, disabled, orderField, orderType, pageIndex, pageSize);
    }

    /**
     * @param siteId
     * @param ids
     */
    public void check(short siteId, Serializable[] ids) {
        Date now = CommonUtils.getDate();
        for (CmsComment entity : getEntitys(ids)) {
            if (siteId == entity.getSiteId() && STATUS_PEND == entity.getStatus()) {
                entity.setStatus(STATUS_NORMAL);
                entity.setCheckDate(now);
            }
        }
    }

    /**
     * @param siteId
     * @param ids
     */
    public void uncheck(short siteId, Serializable[] ids) {
        for (CmsComment entity : getEntitys(ids)) {
            if (siteId == entity.getSiteId() && STATUS_NORMAL == entity.getStatus()) {
                entity.setStatus(STATUS_PEND);
            }
        }
    }

    @Override
    public void delete(Serializable id) {
        CmsComment entity = getEntity(id);
        if (null != entity) {
            entity.setDisabled(true);
        }
    }

    @Autowired
    private CmsCommentDao dao;

}
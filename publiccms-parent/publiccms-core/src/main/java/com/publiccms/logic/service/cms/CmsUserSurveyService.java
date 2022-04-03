package com.publiccms.logic.service.cms;

import java.io.Serializable;

// Generated 2021-09-23 16:55:08 by com.publiccms.common.generator.SourceGenerator

import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;

import com.publiccms.common.base.BaseService;
import com.publiccms.common.handler.PageHandler;
import com.publiccms.entities.cms.CmsUserSurvey;
import com.publiccms.logic.dao.cms.CmsUserSurveyDao;

/**
 *
 * CmsUserSurveyService
 * 
 */
@Service
@Transactional
public class CmsUserSurveyService extends BaseService<CmsUserSurvey> {

    /**
     * @param siteId
     * @param userId
     * @param surveyId
     * @param orderField
     * @param orderType
     * @param pageIndex
     * @param pageSize
     * @return results page
     */
    @Transactional
    public PageHandler getPage(Short siteId, Long userId, Long surveyId, String orderField, String orderType, Integer pageIndex,
            Integer pageSize) {
        return dao.getPage(siteId, userId, surveyId, orderField, orderType, pageIndex, pageSize);
    }

    /**
     * @param siteId
     * @param id
     * @param score
     * @return entity
     */
    public CmsUserSurvey updateScore(short siteId, Serializable id, int score) {
        CmsUserSurvey entity = getEntity(id);
        if (null != entity && siteId == entity.getSiteId()) {
            entity.setScore(score);
        }
        return entity;
    }

    @Resource
    private CmsUserSurveyDao dao;

}
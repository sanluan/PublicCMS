package com.publiccms.logic.service.cms;

// Generated 2020-3-26 11:46:48 by com.publiccms.common.generator.SourceGenerator

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.publiccms.common.base.BaseService;
import com.publiccms.common.handler.PageHandler;
import com.publiccms.entities.cms.CmsUserSurveyQuestion;
import com.publiccms.logic.dao.cms.CmsUserSurveyQuestionDao;

/**
 *
 * CmsUserSurveyQuestionService
 * 
 */
@Service
@Transactional
public class CmsUserSurveyQuestionService extends BaseService<CmsUserSurveyQuestion> {

    /**
     * @param siteId
     * @param userId
     * @param questionId
     * @param surveyId
     * @param orderField
     * @param orderType
     * @param pageIndex
     * @param pageSize
     * @return results page
     */
    @Transactional(readOnly = true)
    public PageHandler getPage(Short siteId, Long userId, Long questionId, Long surveyId, String orderField, String orderType,
            Integer pageIndex, Integer pageSize) {
        return dao.getPage(siteId, userId, questionId, surveyId, orderField, orderType, pageIndex, pageSize);
    }

    @Autowired
    private CmsUserSurveyQuestionDao dao;

}
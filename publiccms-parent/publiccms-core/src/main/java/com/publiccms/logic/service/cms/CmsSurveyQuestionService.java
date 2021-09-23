package com.publiccms.logic.service.cms;

// Generated 2020-7-1 21:06:19 by com.publiccms.common.generator.SourceGenerator

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.publiccms.entities.cms.CmsSurveyQuestion;
import com.publiccms.logic.dao.cms.CmsSurveyQuestionDao;
import com.publiccms.common.base.BaseService;
import com.publiccms.common.handler.PageHandler;

/**
 *
 * CmsSurveyQuestionService
 * 
 */
@Service
@Transactional
public class CmsSurveyQuestionService extends BaseService<CmsSurveyQuestion> {

    /**
     * @param surveyId
     * @param inputTypes
     * @param orderType
     * @param pageIndex
     * @param pageSize
     * @return results page
     */
    @Transactional(readOnly = true)
    public PageHandler getPage(Long surveyId, String[] inputTypes, String orderType, Integer pageIndex, Integer pageSize) {
        return dao.getPage(surveyId, inputTypes, orderType, pageIndex, pageSize);
    }

    @Autowired
    private CmsSurveyQuestionDao dao;

}
package com.publiccms.logic.service.cms;

import java.io.Serializable;

// Generated 2020-7-1 21:06:19 by com.publiccms.common.generator.SourceGenerator

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.publiccms.common.base.BaseService;
import com.publiccms.common.handler.PageHandler;
import com.publiccms.entities.cms.CmsSurveyQuestion;
import com.publiccms.logic.dao.cms.CmsSurveyQuestionDao;

/**
 *
 * CmsSurveyQuestionService
 * 
 */
@Service
@Transactional
public class CmsSurveyQuestionService extends BaseService<CmsSurveyQuestion> {
    public static final String QUESTION_TYPE_RADIO = "radio";
    public static final String QUESTION_TYPE_SELECT = "select";
    public static final String QUESTION_TYPE_CHECKBOX = "checkbox";
    public static final String QUESTION_TYPE_TEXT = "text";
    public static final String QUESTION_TYPE_FILE = "file";
    public static final String QUESTION_TYPE_PICTURE = "picture";
    public static final String[] QUESTION_TYPES_DICT = { QUESTION_TYPE_RADIO, QUESTION_TYPE_SELECT, QUESTION_TYPE_CHECKBOX };

    /**
     * @param surveyId
     * @param questionTypes
     * @param orderType
     * @param pageIndex
     * @param pageSize
     * @return results page
     */
    @Transactional(readOnly = true)
    public PageHandler getPage(Long surveyId, String[] questionTypes, String orderType, Integer pageIndex, Integer pageSize) {
        return dao.getPage(surveyId, questionTypes, orderType, pageIndex, pageSize);
    }

    /**
     * @param id
     * @param answer
     * @return entity
     */
    public CmsSurveyQuestion updateAnswer(Serializable id, String answer) {
        CmsSurveyQuestion entity = getEntity(id);
        if (null != entity) {
            entity.setAnswer(answer);
        }
        return entity;
    }

    @Autowired
    private CmsSurveyQuestionDao dao;

}
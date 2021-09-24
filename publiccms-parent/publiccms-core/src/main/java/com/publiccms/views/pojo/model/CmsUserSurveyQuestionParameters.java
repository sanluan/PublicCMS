package com.publiccms.views.pojo.model;

import java.util.List;

import com.publiccms.entities.cms.CmsUserSurveyQuestion;

/**
 * CmsSurveyQuestionParameters
 * 
 */
public class CmsUserSurveyQuestionParameters implements java.io.Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private List<CmsUserSurveyQuestion> answerList;

    /**
     * @return the answerList
     */
    public List<CmsUserSurveyQuestion> getAnswerList() {
        return answerList;
    }

    /**
     * @param answerList
     *            the answerList to set
     */
    public void setAnswerList(List<CmsUserSurveyQuestion> answerList) {
        this.answerList = answerList;
    }
}

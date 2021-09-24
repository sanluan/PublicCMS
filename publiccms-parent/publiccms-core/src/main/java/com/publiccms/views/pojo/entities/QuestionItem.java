package com.publiccms.views.pojo.entities;

import com.publiccms.entities.cms.CmsSurveyQuestionItem;

public class QuestionItem extends CmsSurveyQuestionItem {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private boolean answer;

    /**
     * @return the answer
     */
    public boolean isAnswer() {
        return answer;
    }

    /**
     * @param answer
     *            the answer to set
     */
    public void setAnswer(boolean answer) {
        this.answer = answer;
    }
}

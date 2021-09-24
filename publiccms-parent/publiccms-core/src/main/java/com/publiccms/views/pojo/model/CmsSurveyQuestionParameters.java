package com.publiccms.views.pojo.model;

import java.util.List;

import com.publiccms.views.pojo.entities.QuestionItem;

/**
 * CmsSurveyQuestionParameters
 * 
 */
public class CmsSurveyQuestionParameters implements java.io.Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private List<QuestionItem> itemList;

    /**
     * @return the itemList
     */
    public List<QuestionItem> getItemList() {
        return itemList;
    }

    /**
     * @param itemList
     *            the itemList to set
     */
    public void setItemList(List<QuestionItem> itemList) {
        this.itemList = itemList;
    }
}

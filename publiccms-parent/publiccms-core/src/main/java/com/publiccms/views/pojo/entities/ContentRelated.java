package com.publiccms.views.pojo.entities;

/**
 * ContentRelated 内容推荐
 * 
 */
public class ContentRelated {
    /**
     * 数据字典
     */
    private String dictionaryId;
    /**
     * 内容模型
     */
    private String modelIds;

    /**
     * @return the dictionaryId
     */
    public String getDictionaryId() {
        return dictionaryId;
    }

    /**
     * @param dictionaryId
     *            the dictionaryId to set
     */
    public void setDictionaryId(String dictionaryId) {
        this.dictionaryId = dictionaryId;
    }

    /**
     * @return the modelIds
     */
    public String getModelIds() {
        return modelIds;
    }

    /**
     * @param modelIds the modelIds to set
     */
    public void setModelIds(String modelIds) {
        this.modelIds = modelIds;
    }
}

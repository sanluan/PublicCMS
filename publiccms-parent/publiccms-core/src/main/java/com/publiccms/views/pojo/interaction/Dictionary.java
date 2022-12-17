package com.publiccms.views.pojo.interaction;

import java.util.List;

import com.publiccms.entities.cms.CmsDictionary;
import com.publiccms.entities.cms.CmsDictionaryData;
import com.publiccms.entities.cms.CmsDictionaryExclude;
import com.publiccms.entities.cms.CmsDictionaryExcludeValue;

public class Dictionary implements java.io.Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    /**
     * 
     */
    private CmsDictionary entity;
    private List<CmsDictionaryData> dataList;
    private List<CmsDictionaryExclude> excludeList;
    private List<CmsDictionaryExcludeValue> excludeValueList;

    /**
     * @return the entity
     */
    public CmsDictionary getEntity() {
        return entity;
    }

    /**
     * @param entity
     *            the entity to set
     */
    public void setEntity(CmsDictionary entity) {
        this.entity = entity;
    }

    /**
     * @return the dataList
     */
    public List<CmsDictionaryData> getDataList() {
        return dataList;
    }

    /**
     * @param dataList
     *            the dataList to set
     */
    public void setDataList(List<CmsDictionaryData> dataList) {
        this.dataList = dataList;
    }

    /**
     * @return the excludeList
     */
    public List<CmsDictionaryExclude> getExcludeList() {
        return excludeList;
    }

    /**
     * @param excludeList
     *            the excludeList to set
     */
    public void setExcludeList(List<CmsDictionaryExclude> excludeList) {
        this.excludeList = excludeList;
    }

    /**
     * @return the excludeValueList
     */
    public List<CmsDictionaryExcludeValue> getExcludeValueList() {
        return excludeValueList;
    }

    /**
     * @param excludeValueList
     *            the excludeValueList to set
     */
    public void setExcludeValueList(List<CmsDictionaryExcludeValue> excludeValueList) {
        this.excludeValueList = excludeValueList;
    }
}

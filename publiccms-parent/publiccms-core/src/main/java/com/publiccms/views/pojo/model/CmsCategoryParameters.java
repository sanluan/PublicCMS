package com.publiccms.views.pojo.model;

import java.util.List;

import com.publiccms.entities.cms.CmsTagType;
import com.publiccms.entities.sys.SysExtendField;
import com.publiccms.views.pojo.entities.ExtendData;

/**
 *
 * CmsCategoryParameters
 * 
 */
public class CmsCategoryParameters implements java.io.Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private List<CmsTagType> tagTypes;
    private List<SysExtendField> contentExtends;
    private List<ExtendData> extendDataList;
    private List<CmsCategoryModelParameters> categoryModelList;

    /**
     * @return
     */
    public List<SysExtendField> getContentExtends() {
        return contentExtends;
    }

    /**
     * @param contentExtends
     */
    public void setContentExtends(List<SysExtendField> contentExtends) {
        this.contentExtends = contentExtends;
    }

    /**
     * @return
     */
    public List<CmsTagType> getTagTypes() {
        return tagTypes;
    }

    /**
     * @param tagTypes
     */
    public void setTagTypes(List<CmsTagType> tagTypes) {
        this.tagTypes = tagTypes;
    }

    /**
     * @return
     */
    public List<ExtendData> getExtendDataList() {
        return extendDataList;
    }

    /**
     * @param extendDataList
     */
    public void setExtendDataList(List<ExtendData> extendDataList) {
        this.extendDataList = extendDataList;
    }

    /**
     * @return
     */
    public List<CmsCategoryModelParameters> getCategoryModelList() {
        return categoryModelList;
    }

    /**
     * @param categoryModelList
     */
    public void setCategoryModelList(List<CmsCategoryModelParameters> categoryModelList) {
        this.categoryModelList = categoryModelList;
    }
}
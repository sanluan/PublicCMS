package com.publiccms.views.pojo;

import java.util.List;

import com.publiccms.entities.cms.CmsTagType;
import com.publiccms.entities.sys.SysExtendField;

public class CmsCategoryParamters implements java.io.Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private List<CmsTagType> tagTypes;
    private List<SysExtendField> contentExtends;
    private List<ExtendData> extendDataList;

    public List<SysExtendField> getContentExtends() {
        return contentExtends;
    }

    public void setContentExtends(List<SysExtendField> contentExtends) {
        this.contentExtends = contentExtends;
    }

    public List<CmsTagType> getTagTypes() {
        return tagTypes;
    }

    public void setTagTypes(List<CmsTagType> tagTypes) {
        this.tagTypes = tagTypes;
    }

    public List<ExtendData> getExtendDataList() {
        return extendDataList;
    }

    public void setExtendDataList(List<ExtendData> extendDataList) {
        this.extendDataList = extendDataList;
    }
}
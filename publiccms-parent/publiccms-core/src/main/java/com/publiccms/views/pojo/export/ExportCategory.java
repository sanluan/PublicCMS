package com.publiccms.views.pojo.export;

import java.util.List;

import com.publiccms.entities.cms.CmsCategory;
import com.publiccms.entities.cms.CmsCategoryAttribute;
import com.publiccms.entities.cms.CmsCategoryModel;
import com.publiccms.entities.sys.SysExtendField;

public class ExportCategory implements java.io.Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private CmsCategory category;
    private CmsCategoryAttribute attribute;
    private List<CmsCategoryModel> modelList;
    private List<SysExtendField> extendList;
    private List<ExportCategory> childList;

    /**
     * @return the category
     */
    public CmsCategory getCategory() {
        return category;
    }

    /**
     * @param category
     *            the category to set
     */
    public void setCategory(CmsCategory category) {
        this.category = category;
    }

    /**
     * @return the attribute
     */
    public CmsCategoryAttribute getAttribute() {
        return attribute;
    }

    /**
     * @param attribute
     *            the attribute to set
     */
    public void setAttribute(CmsCategoryAttribute attribute) {
        this.attribute = attribute;
    }

    /**
     * @return the modelList
     */
    public List<CmsCategoryModel> getModelList() {
        return modelList;
    }

    /**
     * @param modelList
     *            the modelList to set
     */
    public void setModelList(List<CmsCategoryModel> modelList) {
        this.modelList = modelList;
    }

    /**
     * @return the extendList
     */
    public List<SysExtendField> getExtendList() {
        return extendList;
    }

    /**
     * @param extendList
     *            the extendList to set
     */
    public void setExtendList(List<SysExtendField> extendList) {
        this.extendList = extendList;
    }

    /**
     * @return the childList
     */
    public List<ExportCategory> getChildList() {
        return childList;
    }

    /**
     * @param childList
     *            the childList to set
     */
    public void setChildList(List<ExportCategory> childList) {
        this.childList = childList;
    }
}

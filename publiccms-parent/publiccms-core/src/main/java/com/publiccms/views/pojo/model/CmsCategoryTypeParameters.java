package com.publiccms.views.pojo.model;

import java.util.List;

import com.publiccms.entities.sys.SysExtendField;

/**
 *
 * CmsCategoryTypeParameters
 * 
 */
public class CmsCategoryTypeParameters implements java.io.Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private List<SysExtendField> categoryExtends;

    /**
     * @return
     */
    public List<SysExtendField> getCategoryExtends() {
        return categoryExtends;
    }

    /**
     * @param categoryExtends
     */
    public void setCategoryExtends(List<SysExtendField> categoryExtends) {
        this.categoryExtends = categoryExtends;
    }
}
package org.publiccms.views.pojo;

import java.util.List;

import org.publiccms.entities.sys.SysExtendField;

/**
 *
 * CmsCategoryTypeParamters
 * 
 */
public class CmsCategoryTypeParamters implements java.io.Serializable {

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
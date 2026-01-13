package com.publiccms.views.pojo.model;

import com.publiccms.entities.cms.CmsCategoryLang;

/**
 *
 * CmsCategoryListParameters
 * 
 */
public class CmsCategoryLangParameters extends ExtendDataParameters implements java.io.Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private CmsCategoryLang entity;

    /**
     * @return the entity
     */
    public CmsCategoryLang getEntity() {
        return entity;
    }

    /**
     * @param entity
     *            the entity to set
     */
    public void setEntity(CmsCategoryLang entity) {
        this.entity = entity;
    }
}
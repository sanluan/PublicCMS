package com.publiccms.views.pojo.interaction;

import com.publiccms.entities.cms.CmsPlace;
import com.publiccms.entities.cms.CmsPlaceAttribute;

public class PlaceData implements java.io.Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String categoryCode;
    private CmsPlace entity;
    private CmsPlaceAttribute attribute;

    /**
     * @return the categoryCode
     */
    public String getCategoryCode() {
        return categoryCode;
    }

    /**
     * @param categoryCode the categoryCode to set
     */
    public void setCategoryCode(String categoryCode) {
        this.categoryCode = categoryCode;
    }

    /**
     * @return the entity
     */
    public CmsPlace getEntity() {
        return entity;
    }

    /**
     * @param entity
     *            the entity to set
     */
    public void setEntity(CmsPlace entity) {
        this.entity = entity;
    }

    /**
     * @return the attribute
     */
    public CmsPlaceAttribute getAttribute() {
        return attribute;
    }

    /**
     * @param attribute
     *            the attribute to set
     */
    public void setAttribute(CmsPlaceAttribute attribute) {
        this.attribute = attribute;
    }
}

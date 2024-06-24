package com.publiccms.views.pojo.entities;

import java.util.Map;

public class Attribute implements java.io.Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private Map<String, String> attribute;

    /**
     * @return the attribute
     */
    public Map<String, String> getAttribute() {
        return attribute;
    }

    /**
     * @param attribute
     *            the attribute to set
     */
    public void setAttribute(Map<String, String> attribute) {
        this.attribute = attribute;
    }
}

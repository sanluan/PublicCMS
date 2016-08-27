package com.publiccms.views.pojo;

public class ExtendData implements java.io.Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    String name;
    String value;

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return this.value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
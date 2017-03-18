package com.publiccms.views.pojo;

public class DictionaryData implements java.io.Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    String value;
    String text;

    public String getValue() {
        return this.value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
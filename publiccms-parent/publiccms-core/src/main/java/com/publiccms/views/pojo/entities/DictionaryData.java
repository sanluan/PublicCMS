package com.publiccms.views.pojo.entities;

/**
 *
 * DictionaryData
 * 
 */
public class DictionaryData implements java.io.Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    String value;
    String text;

    /**
     * @return
     */
    public String getValue() {
        return this.value;
    }

    /**
     * @param value
     */
    public void setValue(String value) {
        this.value = value;
    }

    /**
     * @return
     */
    public String getText() {
        return text;
    }

    /**
     * @param text
     */
    public void setText(String text) {
        this.text = text;
    }
}
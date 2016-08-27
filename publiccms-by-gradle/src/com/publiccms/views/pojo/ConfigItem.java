package com.publiccms.views.pojo;

public class ConfigItem implements java.io.Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
	private String code;
    private String description;

    public ConfigItem(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}

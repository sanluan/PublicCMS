package com.publiccms.views.pojo.entities;

import com.publiccms.common.api.Config;

/**
 * ParameterType
 * 
 */
public class ParameterType {
    public static final String[] PARAMETER_TYPES = { Config.INPUTTYPE_NUMBER, Config.INPUTTYPE_TEXTAREA, Config.INPUTTYPE_CONTENT,
            Config.INPUTTYPE_USER, Config.INPUTTYPE_CATEGORY };
    private String type;
    private boolean array;
    private boolean required;
    private String alias;

    /**
     * @return the type
     */
    public String getType() {
        return type;
    }

    /**
     * @param type
     *            the type to set
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * @return the array
     */
    public boolean isArray() {
        return array;
    }

    /**
     * @param array
     *            the array to set
     */
    public void setArray(boolean array) {
        this.array = array;
    }

    /**
     * @return the required
     */
    public boolean isRequired() {
        return required;
    }

    /**
     * @param required
     *            the required to set
     */
    public void setRequired(boolean required) {
        this.required = required;
    }

    /**
     * @return the alias
     */
    public String getAlias() {
        return alias;
    }

    /**
     * @param alias
     *            the alias to set
     */
    public void setAlias(String alias) {
        this.alias = alias;
    }
}

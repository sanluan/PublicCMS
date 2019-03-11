package com.publiccms.common.interceptor;

public class CsrfCache {
    private boolean enable;

    /**
     * @param enable
     * @param parameterName
     */
    public CsrfCache(boolean enable, String parameterName) {
        super();
        this.enable = enable;
        this.parameterName = parameterName;
    }

    private String parameterName;

    /**
     * @return the enable
     */
    public boolean isEnable() {
        return enable;
    }

    /**
     * @param enable
     *            the enable to set
     */
    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    /**
     * @return the parameterName
     */
    public String getParameterName() {
        return parameterName;
    }

    /**
     * @param parameterName
     *            the parameterName to set
     */
    public void setParameterName(String parameterName) {
        this.parameterName = parameterName;
    }
}

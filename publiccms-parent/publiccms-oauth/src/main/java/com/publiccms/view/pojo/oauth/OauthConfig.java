package com.publiccms.view.pojo.oauth;

import java.io.Serializable;

/**
 *
 * OauthInfo
 *
 */
public class OauthConfig implements Serializable {

    private static final long serialVersionUID = 1L;

    private String appKey;
    private String appSecret;
    private String returnUrl;

    public OauthConfig(String appKey, String appSecret, String returnUrl) {
        this.appKey = appKey;
        this.appSecret = appSecret;
        this.returnUrl = returnUrl;
    }

    /**
     * @return the appKey
     */
    public String getAppKey() {
        return appKey;
    }

    /**
     * @param appKey
     *            the appKey to set
     */
    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }

    /**
     * @return the appSecret
     */
    public String getAppSecret() {
        return appSecret;
    }

    /**
     * @param appSecret
     *            the appSecret to set
     */
    public void setAppSecret(String appSecret) {
        this.appSecret = appSecret;
    }

    /**
     * @return the returnUrl
     */
    public String getReturnUrl() {
        return returnUrl;
    }

    /**
     * @param returnUrl
     *            the returnUrl to set
     */
    public void setReturnUrl(String returnUrl) {
        this.returnUrl = returnUrl;
    }

}

package com.publiccms.view.pojo.oauth;

import java.io.Serializable;

/**
 *
 * OauthInfo
 *
 */
public class OauthAccess implements Serializable {

    private static final long serialVersionUID = 1L;

    private String code;
    private String accessToken;
    private String openId;

    /**
     * @param code
     * @param accessToken
     */
    public OauthAccess(String code, String accessToken) {
        this.code = code;
        this.accessToken = accessToken;
    }

    /**
     * @param code
     * @param accessToken
     * @param openId
     */
    public OauthAccess(String code, String accessToken, String openId) {
        this.code = code;
        this.accessToken = accessToken;
        this.openId = openId;
    }

    /**
     * @return
     */
    public String getCode() {
        return code;
    }

    /**
     * @param code
     */
    public void setCode(String code) {
        this.code = code;
    }

    /**
     * @return
     */
    public String getAccessToken() {
        return accessToken;
    }

    /**
     * @param accessToken
     */
    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    /**
     * @return
     */
    public String getOpenId() {
        return openId;
    }

    /**
     * @param openId
     */
    public void setOpenId(String openId) {
        this.openId = openId;
    }
}

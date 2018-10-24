package com.publiccms.view.pojo.oauth;

import java.io.Serializable;

/**
 *
 * UserInfo
 *
 */
public class OauthUser implements Serializable {

    private static final long serialVersionUID = 1L;

    private String openId;
    private String nickname;

    /**
     * @param openId
     * @param nickname
     */
    public OauthUser(String openId, String nickname) {
        this.openId = openId;
        this.nickname = nickname;
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

    /**
     * @return
     */
    public String getNickname() {
        return nickname;
    }

    /**
     * @param nickname
     */
    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

}

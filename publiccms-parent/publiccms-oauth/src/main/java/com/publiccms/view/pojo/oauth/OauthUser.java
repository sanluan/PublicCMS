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
    private String avatar;
    private String gender;

    /**
     * @param openId
     * @param nickname
     * @param avatar
     * @param gender
     */
    public OauthUser(String openId, String nickname, String avatar, String gender) {
        this.openId = openId;
        this.nickname = nickname;
        this.avatar = avatar;
        this.gender = gender;
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

    /**
     * @return
     */
    public String getAvatar() {
        return avatar;
    }

    /**
     * @param avatar
     */
    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    /**
     * @return
     */
    public String getGender() {
        return gender;
    }

    /**
     * @param gender
     */
    public void setGender(String gender) {
        this.gender = gender;
    }

}

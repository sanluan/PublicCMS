package com.publiccms.logic.component.message;

public class MessageUser {
    private long id;
    private String nickName;
    private int siteId;
    private boolean remoteSync;

    public MessageUser(long id, String nickName, int siteId) {
        this.id = id;
        this.nickName = nickName;
        this.siteId = siteId;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public int getSiteId() {
        return siteId;
    }

    public void setSiteId(int siteId) {
        this.siteId = siteId;
    }

    public boolean isRemoteSync() {
        return remoteSync;
    }

    public void setRemoteSync(boolean remoteSync) {
        this.remoteSync = remoteSync;
    }
}
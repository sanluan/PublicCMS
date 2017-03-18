package com.publiccms.logic.component.message;

import org.springframework.web.socket.WebSocketMessage;

public class UserMessage implements WebSocketMessage<String> {
    public static final String[] messageTypes = new String[] { "text", "video", "image", "file", "card" };

    public UserMessage(long fromUserId, long toUserId, String payload) {
        this.fromUserId = fromUserId;
        this.toUserId = toUserId;
        this.payload = payload;
        this.type = messageTypes[0];
    }

    private long fromUserId;
    private long toUserId;
    private Long groupId;
    private String type;
    private String payload;

    @Override
    public String getPayload() {
        return payload;
    }

    @Override
    public int getPayloadLength() {
        return payload.length();
    }

    @Override
    public boolean isLast() {
        return true;
    }

    public long getFromUserId() {
        return fromUserId;
    }

    public void setFromUserId(long fromUserId) {
        this.fromUserId = fromUserId;
    }

    public long getToUserId() {
        return toUserId;
    }

    public void setToUserId(long toUserId) {
        this.toUserId = toUserId;
    }

    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
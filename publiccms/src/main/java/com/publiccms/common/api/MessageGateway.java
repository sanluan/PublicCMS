package com.publiccms.common.api;

import java.util.Map;

import org.springframework.web.socket.WebSocketSession;

import com.publiccms.logic.component.message.UserMessage;

public interface MessageGateway {
    public void addUser(long userId, WebSocketSession session);

    public void removeUser(long userId);

    public boolean isOnline(long userId);

    public Map<Long, Boolean> isOnline(long[] userId);

    public void send(UserMessage message);
}

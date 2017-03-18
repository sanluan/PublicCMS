package com.publiccms.logic.component.message;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import com.publiccms.common.api.MessageGateway;

public class LocalGateway implements MessageGateway {
    Map<Long, WebSocketSession> userMap = new HashMap<Long, WebSocketSession>();

    @Override
    public boolean isOnline(long userId) {
        return userMap.containsKey(userId);
    }

    @Override
    public Map<Long, Boolean> isOnline(long[] userIds) {
        Map<Long, Boolean> result = new HashMap<Long, Boolean>();
        for (long userId : userIds) {
            result.put(userId, userMap.containsKey(userId));
        }
        return result;
    }

    @Override
    public void send(UserMessage message) {
        WebSocketSession session = userMap.get(message.getToUserId());
        if (session != null && session.isOpen()) {
            try {
                session.sendMessage(message);
            } catch (IOException e) {
            }
        }
    }

    @Override
    public void addUser(long userId, WebSocketSession session) {
        WebSocketSession oldSession = userMap.put(userId, session);
        if (session != null && session.isOpen()) {
            try {
                TextMessage message = new TextMessage("account loginned from another client : " + session.getRemoteAddress());
                oldSession.sendMessage(message);
                oldSession.close();
            } catch (IOException e) {
            }
        }
    }

    @Override
    public void removeUser(long userId) {
        WebSocketSession session = userMap.remove(userId);
        if (null != session && session.isOpen()) {
            try {
                session.close();
            } catch (IOException e) {
            }
        }
    }
}
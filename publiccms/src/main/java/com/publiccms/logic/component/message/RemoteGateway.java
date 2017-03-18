package com.publiccms.logic.component.message;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class RemoteGateway extends LocalGateway {
    @Override
    public boolean isOnline(long userId) {
        boolean online = super.isOnline(userId);
        if (!online) {
            online = isRemoteOnline(userId);
        }
        return online;
    }

    @Override
    public Map<Long, Boolean> isOnline(long[] userIds) {
        Map<Long, Boolean> result = super.isOnline(userIds);
        if (userIds.length > result.size()) {
            Set<Long> userIdSet = new HashSet<Long>();
            for (long userId : userIds) {
                if (!result.containsKey(userId)) {
                    userIdSet.add(userId);
                }
            }
            Long[] otherUserIds = userIdSet.toArray(new Long[userIdSet.size()]);
            result.putAll(isRemoteOnline(otherUserIds));
        }
        return result;
    }

    @Override
    public void send(UserMessage message) {
        if (super.isOnline(message.getToUserId())) {
            super.send(message);
        } else {
            sendToRemoteUser(message);
        }
    }

    private void sendToRemoteUser(UserMessage message) {
        System.out.println(message);
    }

    private boolean isRemoteOnline(long userId) {
        System.out.println(userId);
        return false;
    }

    private Map<Long, Boolean> isRemoteOnline(Long[] userIds) {
        System.out.println(userIds);
        return new HashMap<Long, Boolean>();
    }
}
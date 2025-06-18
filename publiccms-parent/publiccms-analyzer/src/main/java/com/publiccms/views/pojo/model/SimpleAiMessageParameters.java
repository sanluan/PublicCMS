package com.publiccms.views.pojo.model;

import java.util.List;

public class SimpleAiMessageParameters implements java.io.Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private List<SimpleAiMessage> messages;

    public List<SimpleAiMessage> getMessages() {
        return messages;
    }

    public void setMessages(List<SimpleAiMessage> messages) {
        this.messages = messages;
    }
}